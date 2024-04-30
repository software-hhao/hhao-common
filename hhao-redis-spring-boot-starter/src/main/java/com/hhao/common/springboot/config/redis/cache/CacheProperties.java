
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

/**
 * @author Wang
 * @since 2022/2/4 18:37
 */
@ConfigurationProperties(prefix = "com.hhao.config.redis.cache")
public class CacheProperties {
    /**
     * RedisCacheConfiguration Bean的固定后缀名
     * 启动时会注入全部的RedisCacheConfiguration Bean到RedisCacheManager中，生成不同配置的RedisCache
     * 生成的RedisCache的name默认情况就是Bean的名称去除这个固定的后缀
     * 其中default定义的RedisCacheConfiguration Bean将会作为动态生成RedisCache的模板
     */
    private String suffixConfigName = "RedisCacheConfiguration";
    /**
     * null值的过期时间
     * 默认单位：秒
     */
    private Long nullValueTtl = 5L;
    /**
     * 默认配置模板的default的过期时间
     */
    private Long defaultTtl=30L;
    /**
     * 是否支持事务
     */
    private Boolean enableTransactions = true;
    /**
     * 是否允许动态创建RedisCache
     */
    private Boolean allowInFlightCacheCreation = true;

    /**
     * 是否启用缓存
     */
    private Boolean enable=true;

    /**
     * AOP模式，非常重要，一定要设置
     * 取值PROXY、ASPECTJ
     */
    private String mode="PROXY";

    public String getSuffixConfigName() {
        return suffixConfigName;
    }

    public void setSuffixConfigName(String suffixConfigName) {
        this.suffixConfigName = suffixConfigName;
    }

    public Long getNullValueTtl() {
        return nullValueTtl;
    }

    public void setNullValueTtl(Long nullValueTtl) {
        this.nullValueTtl = nullValueTtl;
    }

    public Boolean getEnableTransactions() {
        return enableTransactions;
    }

    public void setEnableTransactions(Boolean enableTransactions) {
        this.enableTransactions = enableTransactions;
    }

    public Boolean getAllowInFlightCacheCreation() {
        return allowInFlightCacheCreation;
    }

    public void setAllowInFlightCacheCreation(Boolean allowInFlightCacheCreation) {
        this.allowInFlightCacheCreation = allowInFlightCacheCreation;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Long getDefaultTtl() {
        return defaultTtl;
    }

    public void setDefaultTtl(Long defaultTtl) {
        this.defaultTtl = defaultTtl;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
