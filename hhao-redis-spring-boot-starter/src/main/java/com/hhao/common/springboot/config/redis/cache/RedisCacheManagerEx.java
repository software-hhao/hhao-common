
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
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Map;

/**
 * 重写RedisCacheManager
 *
 * @author Wang
 * @since 2022/2/4 18:43
 */
public class RedisCacheManagerEx  extends RedisCacheManager {
    private RedisCacheWriter cacheWriter;
    private  RedisCacheConfiguration defaultCacheConfig;

    public RedisCacheManagerEx(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
                               boolean allowRuntimeCacheCreation, Map<String, RedisCacheConfiguration> initialCacheConfigurations) {
        super(cacheWriter, defaultCacheConfiguration, allowRuntimeCacheCreation, initialCacheConfigurations);

        Assert.notNull(cacheWriter, "CacheWriter must not be null!");
        Assert.notNull(defaultCacheConfiguration, "DefaultCacheConfiguration must not be null!");

        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    @Override
    protected RedisCache createRedisCache(String name,  RedisCacheConfiguration cacheConfig) {
        String [] array= StringUtils.delimitedListToStringArray(name,"#");
        name=array[0];
        if (array.length>1){
            long ttl=Long.parseLong(array[1]);
            cacheConfig=cacheConfig.entryTtl(Duration.ofSeconds(ttl));
        }
        return new RedisCacheEx(name, cacheWriter, cacheConfig != null ? cacheConfig : defaultCacheConfig);
    }
}
