package com.hhao.common.utils.collections.buffer;

/**
 * The interface Padding executor handler.
 *
 * @author Wang
 * @since 1.0.0
 */
@FunctionalInterface
public interface PaddingExecutorHandler {
    /**
     * Padding executor.
     *
     * @param ringBuffer the ring buffer
     */
    void paddingExecutor(RingBuffer ringBuffer);
}
