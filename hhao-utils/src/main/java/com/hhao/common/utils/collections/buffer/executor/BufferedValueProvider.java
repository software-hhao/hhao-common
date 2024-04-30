package com.hhao.common.utils.collections.buffer.executor;

import java.util.List;

/**
 *
 * Buffered ID provider(Lambda supported), which provides ID in the same one second
 *
 * @author Wang
 * @since 1.0.0
 */
@FunctionalInterface
public interface BufferedValueProvider {

    /**
     * Provides ID in one second
     *
     * @param momentInSecond
     * @return
     */
    List<Object> provide(long momentInSecond);
}

