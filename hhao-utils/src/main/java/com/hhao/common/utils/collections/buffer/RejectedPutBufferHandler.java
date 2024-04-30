package com.hhao.common.utils.collections.buffer;

/**
 *
 * If tail catches the cursor it means that the ring buffer is full, any more buffer put request will be rejected.
 * Specify the policy to handle the reject. This is a Lambda supported interface
 *
 * @author Wang
 * @since 1.0.0
 */
@FunctionalInterface
public interface RejectedPutBufferHandler {

    /**
     * Reject put buffer request
     *
     * @param ringBuffer
     * @param value
     */
    void rejectPutBuffer(RingBuffer ringBuffer, Object value);
}
