package com.hhao.common.utils.collections.buffer.executor;

/**
 * 缓存填充执行器
 *
 * @author Wang
 * @since 1.0.0
 */
public interface BufferPaddingExecutor {
    /**
     * Start.
     */
    void start();

    /**
     * Shutdown.
     */
    void shutdown();

    /**
     * Gets id.
     *
     * @return the id
     */
    long getId();
}
