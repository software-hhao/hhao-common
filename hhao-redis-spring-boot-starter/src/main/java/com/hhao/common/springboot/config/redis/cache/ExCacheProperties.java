
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

package com.hhao.common.springboot.config.redis.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Ex cache properties.
 *
 * @author Wang
 * @since 2022 /2/4 18:37
 */
@ConfigurationProperties(prefix = "com.hhao.config.redis.cache")
public class ExCacheProperties {
    /**
     * 是否支持事务
     */
    private Boolean enableTransactions = true;
    /**
     * 是否允许动态创建RedisCache
     */
    private Boolean allowRuntimeCacheCreation = true;

    /**
     * 是否启用缓存
     */
    private Boolean enable=true;

    /**
     * 缓存实现默认使用KEYS和DEL来清除缓存。KEYS可能会在大型key空间下引起性能问题。
     * 因此，默认的RedisCacheWriter可以使用BatchStrategy创建，以切换到基于SCAN的批处理策略。
     * SCAN策略需要一个批处理大小来避免过多的Redis命令往返。
     */
    private Integer batchSize=1000;

    /**
     * TTL（Time-To-Live）：TTL 是一个固定的时间值，从缓存条目被创建或最后更新的那一刻开始计时。
     * 一旦这个时间过去，缓存条目就被认为是过期的，并且可能会被新的条目替换。
     *
     * TTI（Time-To-Idle）：与 TTL 不同，TTI 是在缓存条目最后一次被访问（读取）后开始计时的。
     * 如果缓存条目在 TTI 时间内没有被再次访问，那么它就被认为是过期的。
     * 但是，如果条目在 TTI 时间内被访问了，那么 TTI 计时器就会被重置，从该次访问的时间点重新开始计时。
     *
     * 这种策略在某些场景中可能很有用，因为它允许缓存系统根据条目的实际使用情况来管理其生命周期。
     * 如果一个条目经常被访问，那么它可以在缓存中保留更长的时间，即使它已经存在了一段时间。
     * 相反，如果一个条目长时间没有被访问，那么即使它的 TTL 值还没有到期，它也可能因为 TTI 到期而被移除。
     */
    private Boolean enableTimeToIdle=false;

    /**
     * key:cache name
     * value:ExCacheConfig
     */
    Map<String,ExCacheConfig> cacheConfigs=new HashMap<>();

    /**
     * AOP模式，非常重要，一定要设置
     * 取值PROXY、ASPECTJ或者NONE，NONE代表不启用
     */
    private String mode="PROXY";

    /**
     * Gets enable transactions.
     *
     * @return the enable transactions
     */
    public Boolean getEnableTransactions() {
        return enableTransactions;
    }

    /**
     * Sets enable transactions.
     *
     * @param enableTransactions the enable transactions
     */
    public void setEnableTransactions(Boolean enableTransactions) {
        this.enableTransactions = enableTransactions;
    }

    /**
     * Gets allow runtime cache creation.
     *
     * @return the allow runtime cache creation
     */
    public Boolean getAllowRuntimeCacheCreation() {
        return allowRuntimeCacheCreation;
    }

    /**
     * Sets allow runtime cache creation.
     *
     * @param allowRuntimeCacheCreation the allow runtime cache creation
     */
    public void setAllowRuntimeCacheCreation(Boolean allowRuntimeCacheCreation) {
        this.allowRuntimeCacheCreation = allowRuntimeCacheCreation;
    }

    /**
     * Gets enable.
     *
     * @return the enable
     */
    public Boolean getEnable() {
        return enable;
    }

    /**
     * Sets enable.
     *
     * @param enable the enable
     */
    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    /**
     * Gets mode.
     *
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * Sets mode.
     *
     * @param mode the mode
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Gets cache configs.
     *
     * @return the cache configs
     */
    public Map<String, ExCacheConfig> getCacheConfigs() {
        return cacheConfigs;
    }

    /**
     * Sets cache configs.
     *
     * @param cacheConfigs the cache configs
     */
    public void setCacheConfigs(Map<String, ExCacheConfig> cacheConfigs) {
        this.cacheConfigs = cacheConfigs;
    }

    /**
     * Gets batch size.
     *
     * @return the batch size
     */
    public Integer getBatchSize() {
        return batchSize;
    }

    /**
     * Sets batch size.
     *
     * @param batchSize the batch size
     */
    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    /**
     * Gets enable time to idle.
     *
     * @return the enable time to idle
     */
    public Boolean getEnableTimeToIdle() {
        return enableTimeToIdle;
    }

    /**
     * Sets enable time to idle.
     *
     * @param enableTimeToIdle the enable time to idle
     */
    public void setEnableTimeToIdle(Boolean enableTimeToIdle) {
        this.enableTimeToIdle = enableTimeToIdle;
    }
}
