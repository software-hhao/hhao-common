/*
 * Copyright (c) 2024 wangsheng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.hhao.common.springboot.config.redis.cache;

/**
 * The type Ex cache config.
 */
public class ExCacheConfig {
    private Integer timeToLive=0;
    private Boolean enableTimeToIdle=false;

    /**
     * Gets time to live.
     *
     * @return the time to live
     */
    public Integer getTimeToLive() {
        return timeToLive;
    }

    /**
     * Sets time to live.
     *
     * @param timeToLive the time to live
     */
    public void setTimeToLive(Integer timeToLive) {
        this.timeToLive = timeToLive;
    }

    public Boolean getEnableTimeToIdle() {
        return enableTimeToIdle;
    }

    public void setEnableTimeToIdle(Boolean enableTimeToIdle) {
        this.enableTimeToIdle = enableTimeToIdle;
    }
}
