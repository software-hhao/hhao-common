/*
 * Copyright 2008-2024 wangsheng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.function.Consumer;

/**
 * The interface Jackson util builder.
 *
 * @param <T> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public interface JacksonUtilBuilder<T extends ObjectMapper> {
    /**
     * 构建JsonUtil
     *
     * @param targetClass the target class
     * @param consumer    the consumer
     * @return jackson util
     */
    JacksonUtil build(Class<T> targetClass, Consumer<T> consumer);

    /**
     * 构建JsonUtil
     *
     * @param targetClass the target class
     * @return jackson util
     */
    JacksonUtil build(Class<T> targetClass);
}
