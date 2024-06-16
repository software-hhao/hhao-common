
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

import com.hhao.common.exception.error.server.ServerRuntimeException;
import com.hhao.common.jackson.JacksonUtil;
import com.hhao.common.jackson.JacksonUtilFactory;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.springboot.config.redis.RedisConfig;
import com.hhao.common.utils.MD5Util;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 相关类：
 * org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
 * org.springframework.boot.autoconfigure.cache.CacheConfigurations
 * org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
 * org.springframework.data.redis.cache.RedisCacheConfiguration
 * ProxyCachingConfiguration
 * AbstractCachingConfiguration
 * CompositeCacheManager
 * <p>
 * AspectJCachingConfiguration
 * CachingConfigurationSelector
 * RedisCacheManager
 * RedisCache
 * CacheAspectSupport
 * <p>
 * 如果要创建二级缓存，可以定义CompositeCacheManager将多个CacheManager组合起来
 * new CompositeCacheManager(createCacheManager(cacheManagerCustomizers,cacheConfigurations,cacheWriter));
 * <p>
 * 目前缓存只适用于web，不适用于webflux
 *
 * @author Wang
 * @since 2022 /2/3 10:52
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(CacheManager.class)
@AutoConfigureAfter(RedisConfig.class)
@AutoConfigureBefore(RedisCacheConfiguration.class)
@ConditionalOnWebApplication(type=ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties({ExCacheProperties.class, CacheProperties.class})
@ConditionalOnProperty(prefix = "com.hhao.config.redis.cache",name = "enable",havingValue = "true",matchIfMissing = true)
public class CacheConfig {
    private static final Logger logger= LoggerFactory.getLogger(CacheConfig.class);
    /**
     * 作为动态生成RedisCache配置模板的RedisCacheConfiguration名称
     */
    private final String DEFAULT_REDIS_CACHE_CONFIG_NAME="default";
    private final String DEFAULT_KEY_PREFIX="spring:cache:";
    /**
     * RedisCacheConfiguration Bean的固定后缀名
     * 启动时会注入全部的RedisCacheConfiguration Bean到RedisCacheManager中，生成不同配置的RedisCache
     * 生成的RedisCache的name默认情况就是Bean的名称去除这个固定的后缀
     * 其中default定义的RedisCacheConfiguration Bean将会作为动态生成RedisCache的模板
     */
    private final String DEFAULT_SUFFIX_CONFIG_NAME= "RedisCacheConfiguration";

    private ExCacheProperties exCacheProperties;
    private CacheProperties cacheProperties;
    private RedisSerializer<Object> jackson2JsonRedisSerializer;

    /**
     * Instantiates a new Cache config.
     *
     * @param exCacheProperties           the ex cache properties
     * @param cacheProperties             the cache properties
     * @param jackson2JsonRedisSerializer the jackson 2 json redis serializer
     */
    @Autowired
    public CacheConfig(ExCacheProperties exCacheProperties,CacheProperties cacheProperties,@Qualifier("genericJackson2JsonRedisSerializer") RedisSerializer<Object> jackson2JsonRedisSerializer){
        this.exCacheProperties = exCacheProperties;
        this.cacheProperties=cacheProperties;
        this.jackson2JsonRedisSerializer=jackson2JsonRedisSerializer;
    }

    /**
     * Cache manager customizers cache manager customizers.
     *
     * @param customizers the customizers
     * @return the cache manager customizers
     */
    @Bean
    @ConditionalOnMissingBean
    public CacheManagerCustomizers cacheManagerCustomizers(ObjectProvider<CacheManagerCustomizer<?>> customizers) {
        return new CacheManagerCustomizers(customizers.orderedStream().toList());
    }

    /**
     * 作为缓存默认的配置文件
     * 缓存key采用StringRedisSerializer，value采用genericJackson2JsonRedisSerializer
     *
     * @param redisSerializer
     * @return redis cache configuration
     */
    @Bean
    @Primary
    @SuppressWarnings("all")
    public RedisCacheConfiguration defaultRedisCacheConfiguration(){
        return createJsonRedisSerializerConfiguration(cacheProperties.getRedis(),exCacheProperties);
    }

    /**
     * Jdk serializer redis cache configuration redis cache configuration.
     *
     * @param resourceLoader the resource loader
     * @return the redis cache configuration
     */
    @Bean
    @SuppressWarnings("all")
    public RedisCacheConfiguration jdkSerializerRedisCacheConfiguration(ResourceLoader resourceLoader){
        return createJdkRedisSerializerConfiguration(cacheProperties.getRedis(),exCacheProperties,resourceLoader.getClassLoader());
    }

    /**
     * RedisCacheManager默认使用无锁RedisCacheWriter来读取和写入二进制值。 无锁缓存提高了吞吐量。
     *
     * @param cacheManagerCustomizers the cache manager customizers
     * @param redisConnectionFactory  the redis connection factory
     * @param cacheConfigurations     the cache configurations
     * @return the cache manager
     */
    @Bean
    @Primary
    public CacheManager cacheManager(CacheManagerCustomizers cacheManagerCustomizers,
                                   RedisConnectionFactory redisConnectionFactory,
                                   Map<String, RedisCacheConfiguration> cacheConfigurations) {
        // 采用非锁定的CacheWrite
        RedisCacheWriter cacheWriter=RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory, BatchStrategies.scan(exCacheProperties.getBatchSize()));
        return createCacheManager(cacheManagerCustomizers,cacheConfigurations,cacheWriter);
    }

    /**
     * 有锁RedisCacheWriter来读取和写入二进制值。
     *
     * @param cacheManagerCustomizers the cache manager customizers
     * @param redisConnectionFactory  the redis connection factory
     * @param cacheConfigurations     the cache configurations
     * @return the redis cache manager
     */
    @Bean
    public RedisCacheManager cacheManagerWithLockCacheWrite(CacheManagerCustomizers cacheManagerCustomizers,
                                          RedisConnectionFactory redisConnectionFactory,
                                          Map<String, RedisCacheConfiguration> cacheConfigurations) {
        // 采用非锁定的CacheWrite
        RedisCacheWriter cacheWriter=RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory, BatchStrategies.scan(exCacheProperties.getBatchSize()));
        return createCacheManager(cacheManagerCustomizers,cacheConfigurations,cacheWriter);
    }

    private RedisCacheManager createCacheManager(CacheManagerCustomizers cacheManagerCustomizers,
                                   Map<String, RedisCacheConfiguration> cacheConfigurations,
                                   RedisCacheWriter cacheWriter) {
        // 预处理配置对象
        cacheConfigurations=preProcessCacheConfigurations(cacheConfigurations);

        if (cacheProperties.getRedis().isEnableStatistics()) {
            CacheStatisticsCollector statisticsCollector = CacheStatisticsCollector.create();
            cacheWriter = cacheWriter.withStatisticsCollector(statisticsCollector);
        }

        // 默认的配置文件
        RedisCacheConfiguration defaultCacheConfiguration=determineConfiguration(cacheProperties.getRedis(),exCacheProperties,cacheConfigurations);

        RedisCacheManager cacheManager=new RedisCacheManagerEx(exCacheProperties,cacheWriter,
                defaultCacheConfiguration,
                exCacheProperties.getAllowRuntimeCacheCreation(),
                cacheConfigurations);
        cacheManager.setTransactionAware(exCacheProperties.getEnableTransactions());

        return cacheManagerCustomizers.customize(cacheManager);
    }

    private RedisCacheConfiguration determineConfiguration(CacheProperties.Redis redisProperties,ExCacheProperties exCacheProperties,Map<String, RedisCacheConfiguration> cacheConfigurations) {
        return cacheConfigurations.getOrDefault(DEFAULT_REDIS_CACHE_CONFIG_NAME,createJsonRedisSerializerConfiguration(redisProperties,exCacheProperties));
    }

    private RedisCacheConfiguration createJsonRedisSerializerConfiguration(CacheProperties.Redis redisProperties,ExCacheProperties exCacheProperties) {
        return createConfiguration(redisProperties,exCacheProperties,RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer(StandardCharsets.UTF_8)),RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
    }

    private RedisCacheConfiguration createJdkRedisSerializerConfiguration(CacheProperties.Redis redisProperties,ExCacheProperties exCacheProperties,ClassLoader classLoader) {
        return createConfiguration(redisProperties,exCacheProperties,RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer(StandardCharsets.UTF_8)),RedisSerializationContext.SerializationPair.fromSerializer(new JdkSerializationRedisSerializer(classLoader)));
    }

    private RedisCacheConfiguration createConfiguration(CacheProperties.Redis redisProperties,ExCacheProperties exCacheProperties,RedisSerializationContext.SerializationPair<String> keySerializationPair,RedisSerializationContext.SerializationPair<?> valueSerializationPair) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }else{
            //自定义前缀
            config = config.computePrefixWith(cacheName -> DEFAULT_KEY_PREFIX + cacheName + ":");
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        if (exCacheProperties.getEnableTimeToIdle()){
            config=config.enableTimeToIdle();
        }
        //设置value为json序列化
        config = config.serializeValuesWith(valueSerializationPair);
        //设置 key为string序列化
        config = config.serializeKeysWith(keySerializationPair);
        return config;
    }

    /**
     * 预处理RedisCacheConfiguration的bean名称，把后缀去掉，留下的名称作为注册的RedisCache的名称
     *
     * @param cacheConfigs
     * @return
     */
    private Map<String, RedisCacheConfiguration> preProcessCacheConfigurations(Map<String, RedisCacheConfiguration> cacheConfigs){
        Map<String, RedisCacheConfiguration> cacheConfigurationMap=new HashMap<>();
        for (Map.Entry<String, RedisCacheConfiguration> entry : cacheConfigs.entrySet()) {
            cacheConfigurationMap.put(preProcessCacheName(entry.getKey()),entry.getValue());
        }
        return cacheConfigurationMap;
    }

    /**
     * 后缀去掉，留下的名称作为注册的RedisCache的名称
     *
     * @param name
     * @return
     */
    private String preProcessCacheName(String name){
        if (StringUtils.endsWithIgnoreCase(name,DEFAULT_SUFFIX_CONFIG_NAME)){
            return name.substring(0,name.lastIndexOf(DEFAULT_SUFFIX_CONFIG_NAME));
        }
        return name;
    }

    /**
     * 按method+调用函数的参数JSON序列化生成md5值做key
     *
     * @return key generator
     */
    @Bean
    public KeyGenerator methodKeyGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                try {
                    JacksonUtil jacksonUtil=JacksonUtilFactory.getJsonUtil();
                    // 将方法名与参数组合成字符串
                    StringBuffer combinedKey = new StringBuffer(method.toGenericString());
                    for(Object param : params){
                        combinedKey.append(jacksonUtil.obj2String(param));
                    }
                    // 计算JSON字符串的MD5值
                    logger.debug("cache key with method key:{}",combinedKey.toString());
                    return MD5Util.getMD5(combinedKey.toString());
                } catch (Exception e) {
                    throw new ServerRuntimeException("Failed to generate key", e);
                }
            }
        };
    }


    /**
     * 采用Java Proxy模式进行AOP
     * 该模式相同对象的方法间调用不会被缓存
     * 注意和事务并存时的优先级，应比事务高
     */
    @Configuration
    @EnableCaching(mode = AdviceMode.PROXY,order = 100)
    @ConditionalOnProperty(prefix = "com.hhao.config.redis.cache",name = "mode",havingValue = "PROXY",matchIfMissing = true)
    public static class EnableCachingWithProxy{
        /**
         * The Logger.
         */
        final Logger logger = LoggerFactory.getLogger(EnableCachingWithProxy.class);

        /**
         * Instantiates a new Enable caching with proxy.
         */
        public EnableCachingWithProxy(){
            logger.info("Enable caching with proxy.");
        }
    }

    /**
     * 采用ASPECTJ模式进行AOP
     * 该模式要设置POM，编译时织入代码
     * 注意和事务并存时的优先级，应比事务高
     */
    @Configuration
    @EnableCaching(mode = AdviceMode.ASPECTJ,order = 100)
    @ConditionalOnProperty(prefix = "com.hhao.config.redis.cache",name = "mode",havingValue = "ASPECTJ")
    public static class EnableCachingWithAspectj{
        /**
         * The Logger.
         */
        final Logger logger = LoggerFactory.getLogger(EnableCachingWithAspectj.class);

        /**
         * Instantiates a new Enable caching with aspectj.
         */
        public EnableCachingWithAspectj(){
            logger.info("Enable caching with aspectj.");
        }
    }
}

