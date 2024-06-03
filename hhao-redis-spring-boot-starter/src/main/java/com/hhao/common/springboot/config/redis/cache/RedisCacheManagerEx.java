
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

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Map;

/**
 * 重写RedisCacheManager
 *
 * @author Wang
 * @since 2022 /2/4 18:43
 */
public class RedisCacheManagerEx  extends RedisCacheManager {
    private RedisCacheWriter cacheWriter;
    private RedisCacheConfiguration defaultCacheConfig;
    private ExCacheProperties exCacheProperties;

    /**
     * Instantiates a new Redis cache manager ex.
     *
     * @param exCacheProperties          the ex cache properties
     * @param cacheWriter                the cache writer
     * @param defaultCacheConfiguration  the default cache configuration
     * @param allowRuntimeCacheCreation  the allow runtime cache creation
     * @param initialCacheConfigurations the initial cache configurations
     */
    public RedisCacheManagerEx(ExCacheProperties exCacheProperties,RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
                               boolean allowRuntimeCacheCreation, Map<String, RedisCacheConfiguration> initialCacheConfigurations) {
        super(cacheWriter, defaultCacheConfiguration, allowRuntimeCacheCreation, initialCacheConfigurations);

        Assert.notNull(cacheWriter, "CacheWriter must not be null!");
        Assert.notNull(defaultCacheConfiguration, "DefaultCacheConfiguration must not be null!");

        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
        this.exCacheProperties=exCacheProperties;
    }

    @Override
    protected RedisCache createRedisCache(String name,  RedisCacheConfiguration cacheConfig) {
        cacheConfig=determineConfiguration(name,cacheConfig);
        return super.createRedisCache(name, cacheConfig);
    }

    private RedisCacheConfiguration determineConfiguration(String name,RedisCacheConfiguration cacheConfig){
        RedisCacheConfiguration redisCacheConfiguration=cacheConfig;
        // 从配置文件中查找是否带有ttl
        ExCacheConfig exCacheConfig=exCacheProperties.getCacheConfigs().get(name);
        if (exCacheConfig!=null){
            redisCacheConfiguration=redisCacheConfiguration.entryTtl(Duration.ofSeconds(exCacheConfig.getTimeToLive()));
            if (exCacheConfig.getEnableTimeToIdle()){
                redisCacheConfiguration = redisCacheConfiguration.enableTimeToIdle();
            }
        }
        // 从cache name中查找是否带有ttl
        String [] array= StringUtils.delimitedListToStringArray(name,"#");
        // 优先检查cache name是否带有ttl
        if (array.length>1){
            long ttl=Long.parseLong(array[1]);
            redisCacheConfiguration=redisCacheConfiguration.entryTtl(Duration.ofSeconds(ttl));
        }
        return redisCacheConfiguration;
    }
}
