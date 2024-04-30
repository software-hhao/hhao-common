package com.hhao.common.utils.collections.buffer;

import com.hhao.common.exception.error.server.ServerRuntimeException;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.utils.Assert;
import com.hhao.common.utils.collections.buffer.utils.PaddedAtomicLong;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Wang
 * @since 1.0.0
 */
public class RingBuffer<T> {
    private static final Logger logger = LoggerFactory.getLogger(RingBuffer.class);
    /**
     * Constants
     */
    private static final int START_POINT = -1;
    private static final long CAN_PUT_FLAG = 0L;
    private static final long CAN_TAKE_FLAG = 1L;

    /**
     * The size of RingBuffer's slots, each slot hold a value
     */
    private final int bufferSize;
    private final long indexMask;
    private final Object[] slots;
    private final PaddedAtomicLong[] flags;

    /**
     * Tail: last position sequence to produce
     */
    private final AtomicLong tail = new PaddedAtomicLong(START_POINT);

    /**
     * Cursor: current position sequence to consume
     */
    private final AtomicLong cursor = new PaddedAtomicLong(START_POINT);

    /**
     * Threshold for trigger padding buffer
     */
    private final int paddingThreshold;

    /**
     * Reject put/take buffer handle policy
     */
    private RejectedPutBufferHandler rejectedPutHandler = this::discardPutBuffer;
    private RejectedTakeBufferHandler rejectedTakeHandler = this::exceptionRejectedTakeBuffer;
    /**
     * Padding buffer handle policy
     */
    private PaddingExecutorHandler paddingExecutorHandler;

    /**
     * Constructor with buffer size & padding factor & padding handle policy
     *
     * @param bufferSize must be positive & a power of 2
     * @param paddingFactor percent in (0 - 100). When the count of rest available UIDs reach the threshold, it will trigger padding buffer<br>
     *        Sample: paddingFactor=20, bufferSize=1000 -> threshold=1000 * 20 /100,
     *        padding buffer will be triggered when tail-cursor<threshold
     */
    public RingBuffer(int bufferSize, int paddingFactor, PaddingExecutorHandler paddingExecutorHandler) {
        // check buffer size is positive & a power of 2; padding factor in (0, 100)
        Assert.isTrue(bufferSize > 0L, "RingBuffer size must be positive");
        Assert.isTrue(Integer.bitCount(bufferSize) == 1, "RingBuffer size must be a power of 2");
        Assert.isTrue(paddingFactor > 0 && paddingFactor < 100, "RingBuffer size must be positive");

        this.bufferSize = bufferSize;
        this.indexMask = bufferSize - 1;
        this.slots = new Object[bufferSize];
        this.flags = initFlags(bufferSize);

        this.paddingThreshold = bufferSize * paddingFactor / 100;
        this.paddingExecutorHandler=paddingExecutorHandler;
    }

    /**
     * Put a value in the ring & tail moved<br>
     * We use 'synchronized' to guarantee the value fill in slot & publish new tail sequence as atomic operations<br>
     *
     * <b>Note that: </b> It is recommended to put value in a serialize way, cause we once batch generate a series value and put
     * the one by one into the buffer, so it is unnecessary put in multi-threads
     *
     * @param value
     * @return false means that the buffer is full, apply {@link RejectedPutBufferHandler}
     */
    public synchronized boolean put(Object value) {
        long currentTail = tail.get();
        long currentCursor = cursor.get();

        // tail catches the cursor, means that you can't put any cause of RingBuffer is full
        long distance = currentTail - (currentCursor == START_POINT ? 0 : currentCursor);
        if (distance == bufferSize - 1) {
            rejectedPutHandler.rejectPutBuffer(this, value);
            return false;
        }

        // 1. pre-check whether the flag is CAN_PUT_FLAG
        int nextTailIndex = calSlotIndex(currentTail + 1);
        if (flags[nextTailIndex].get() != CAN_PUT_FLAG) {
            rejectedPutHandler.rejectPutBuffer(this, value);
            return false;
        }

        // 2. put value in the next slot
        // 3. update next slot' flag to CAN_TAKE_FLAG
        // 4. publish tail with sequence increase by one
        slots[nextTailIndex] = value;
        flags[nextTailIndex].set(CAN_TAKE_FLAG);
        tail.incrementAndGet();

        // The atomicity of operations above, guarantees by 'synchronized'. In another word,
        // the take operation can't consume the UID we just put, until the tail is published(tail.incrementAndGet())
        return true;
    }

    /**
     * Take value of the ring at the next cursor, this is a lock free operation by using atomic cursor<p>
     *
     * Before getting the value, we also check whether reach the padding threshold,
     * the padding buffer operation will be triggered in another thread<br>
     * If there is no more available UID to be taken, the specified {@link RejectedTakeBufferHandler} will be applied<br>
     *
     * @return UID
     * @throws IllegalStateException if the cursor moved back
     */
    public T take() {
        // spin get next available cursor
        long currentCursor = cursor.get();
        long nextCursor = cursor.updateAndGet(old -> old == tail.get() ? old : old + 1);

        // check for safety consideration, it never occurs
        Assert.isTrue(nextCursor >= currentCursor, "Curosr can't move back");

        // trigger padding in an async-mode if reach the threshold
        long currentTail = tail.get();
        if (currentTail - nextCursor < paddingThreshold) {
            logger.info("Reach the padding threshold:{}. tail:{}, cursor:{}, rest:{}", paddingThreshold, currentTail,
                    nextCursor, currentTail - nextCursor);
            paddingExecutorHandler.paddingExecutor(this);
        }

        // cursor catch the tail, means that there is no more available value to take
        if (nextCursor == currentCursor) {
            rejectedTakeHandler.rejectTakeBuffer(this);
        }

        // 1. check next slot flag is CAN_TAKE_FLAG
        int nextCursorIndex = calSlotIndex(nextCursor);
        if (flags[nextCursorIndex].get() != CAN_TAKE_FLAG) {
            rejectedTakeHandler.rejectTakeBuffer(this);
        }

        // 2. get value from next slot
        // 3. set next slot flag as CAN_PUT_FLAG.
        T value = (T)slots[nextCursorIndex];
        flags[nextCursorIndex].set(CAN_PUT_FLAG);

        // Note that: Step 2,3 can not swap. If we set flag before get value of slot, the producer may overwrite the
        // slot with a new value, and this may cause the consumer take the value twice after walk a round the ring
        return value;
    }

    /**
     * Calculate slot index with the slot sequence (sequence % bufferSize)
     */
    protected int calSlotIndex(long sequence) {
        return (int) (sequence & indexMask);
    }

    /**
     * Discard policy for {@link RejectedPutBufferHandler}, we just do logging
     */
    protected void discardPutBuffer(RingBuffer ringBuffer, Object value) {
        logger.warn("Rejected putting buffer for uid:{}. {}", value, ringBuffer);
    }

    /**
     * Policy for {@link RejectedTakeBufferHandler}, throws {@link RuntimeException} after logging
     */
    protected void exceptionRejectedTakeBuffer(RingBuffer ringBuffer) {
        logger.warn("Rejected take buffer. {}", ringBuffer);
        throw new ServerRuntimeException("Rejected take buffer. " + ringBuffer);
    }

    /**
     * Initialize flags as CAN_PUT_FLAG
     */
    private PaddedAtomicLong[] initFlags(int bufferSize) {
        PaddedAtomicLong[] flags = new PaddedAtomicLong[bufferSize];
        for (int i = 0; i < bufferSize; i++) {
            flags[i] = new PaddedAtomicLong(CAN_PUT_FLAG);
        }

        return flags;
    }

    /**
     * Getters
     */
    public long getTail() {
        return tail.get();
    }

    public long getCursor() {
        return cursor.get();
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setRejectedPutHandler(RejectedPutBufferHandler rejectedPutHandler) {
        this.rejectedPutHandler = rejectedPutHandler;
    }

    public void setRejectedTakeHandler(RejectedTakeBufferHandler rejectedTakeHandler) {
        this.rejectedTakeHandler = rejectedTakeHandler;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RingBuffer [bufferSize=").append(bufferSize)
                .append(", tail=").append(tail)
                .append(", cursor=").append(cursor)
                .append(", paddingThreshold=").append(paddingThreshold).append("]");

        return builder.toString();
    }

    public static class Builder{
        private final int DEFAULT_PADDING_PERCENT = 50;
        private int paddingPercent=DEFAULT_PADDING_PERCENT;
        private int bufferSize;
        private PaddingExecutorHandler paddingExecutorHandler;
        private RejectedPutBufferHandler rejectedPutHandler;
        private RejectedTakeBufferHandler rejectedTakeHandler;

        public Builder(int bufferSize, PaddingExecutorHandler paddingExecutorHandler){
            this.bufferSize=bufferSize;
            this.paddingExecutorHandler=paddingExecutorHandler;
        }

        public Builder setRejectedPutHandler(RejectedPutBufferHandler rejectedPutHandler) {
            this.rejectedPutHandler=rejectedPutHandler;
            return this;
        }

        public Builder setRejectedTakeHandler(RejectedTakeBufferHandler rejectedTakeHandler) {
            this.rejectedTakeHandler=rejectedTakeHandler;
            return this;
        }

        public Builder setBufferSize(int bufferSize) {
            this.bufferSize=bufferSize;
            return this;
        }

        public Builder setPaddingPercent(int paddingPercent) {
            this.paddingPercent=paddingPercent;
            return this;
        }

        public <T> RingBuffer<T> build(){
            RingBuffer<T> ringBuffer=new RingBuffer<T>(bufferSize,paddingPercent,paddingExecutorHandler);
            if (rejectedPutHandler!=null){
                ringBuffer.setRejectedPutHandler(rejectedPutHandler);
            }
            if (rejectedTakeHandler!=null){
                ringBuffer.setRejectedTakeHandler(rejectedTakeHandler);
            }
            return ringBuffer;
        }
    }
}
