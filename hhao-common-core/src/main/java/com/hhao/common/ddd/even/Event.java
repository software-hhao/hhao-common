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

package com.hhao.common.ddd.even;

/**
 * The interface Event.
 *
 * @author Wang
 * @since 1.0.0
 */
public interface Event<T> {
    /**
     * 唯一Id
     *
     * @return the id
     */
    Long getEventId();

    /**
     * 事件触发时间
     *
     * @return the timestamp
     */
    Long getOccurredTime();

    /**
     * 版本
     *
     * @return the version
     */
    String getVersion();

    /**
     * 事件对象
     *
     * @return the source
     */
    T getSource();

    /**
     * 事件类型
     *
     * @return the source type
     */
    String getEventType();
}
