
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

package com.hhao.common.springboot.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.hhao.common.jackson.DefaultJacksonUtil;
import com.hhao.common.jackson.JacksonUtilFactory;
import com.hhao.common.springboot.jackson.SpringJacksonKeyType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 相关类：
 * RedisAutoConfiguration
 * <p>
 * 注意：RedisConnection类不是线程安全的。
 * LettuceConnectionFactory默认情况下创建的是共享的连接，不可用于阻塞的、事务性的操作。
 * 如果要用于阻塞的、事务性的操作，要改成每次操作生成一个连接。
 *
 * @author Wang
 * @Bean public      LettuceConnectionFactory lettuceConnectionFactory() {         LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()                 //启用ssl                 .useSsl().and()                 .commandTimeout(Duration.ofSeconds(2))                 .shutdownTimeout(Duration.ZERO)                 .build();         LettuceConnectionFactory lettuceConnectionFactory=new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379), clientConfig);         // 不共享         lettuceConnectionFactory.setShareNativeConnection(false);         return lettuceConnectionFactory;     }
 * @since 2022 /2/4 18:28
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "com.hhao.config.redis",name = "enable",havingValue = "true",matchIfMissing = true)
public class RedisConfig {
    // RedisTemplate是否开启事务支持
    // 开启事务支持，如果当前线程在事务中，则使用事务管理器管理事务
    @Value("${com.hhao.config.redis.redis-template.enable-transaction-support:false}")
    private Boolean enableTransactionSupport;

    /**
     * 采用JdkSerializationRedisSerializer
     *
     * @param redisConnectionFactory the redis connection factory
     * @return redis template
     */
    @Bean
    public RedisTemplate<Object, Object> jdkSerializerRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    /**
     * 自定义redisTemplate Bean,覆盖RedisAutoConfiguration中的定义
     * key采用String,value采用JSON序列化
     *
     * @param factory         the factory
     * @param redisSerializer the redis serializer
     * @return redis template
     */
    @Bean("redisTemplate")
    @SuppressWarnings("all")
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory, @Qualifier("genericJackson2JsonRedisSerializer") RedisSerializer<Object> redisSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 是否开启事务支持
        template.setEnableTransactionSupport(enableTransactionSupport);
        // 设置默认的值解析方式
        template.setDefaultSerializer(redisSerializer);

        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(redisSerializer);

        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(redisSerializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * 自定义stringRedisTemplate Bean,覆盖RedisAutoConfiguration中的定义
     * key采用String,value采用String
     *
     * @param redisConnectionFactory the redis connection factory
     * @return string redis template
     */
    @Bean("stringRedisTemplate")
    @SuppressWarnings("all")
    @Primary
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisConnectionFactory);
        stringRedisTemplate.setEnableTransactionSupport(enableTransactionSupport);
        return stringRedisTemplate;
    }


    /**
     * 定义JSON序列化器
     * 如果有加载session，那么该bean为session默认的序列化解析器
     * bean的命名不能改变
     *
     * @param objectMapper the object mapper
     * @return redis serializer
     */
    @Bean("genericJackson2JsonRedisSerializer")
    @SuppressWarnings("all")
    public RedisSerializer<Object> genericJackson2JsonRedisSerializer(ObjectMapper objectMapper) {
        //在原来spring boot生成的ObjectMapper配置基础上，重新生成一个新的ObjectMapper
        objectMapper=objectMapper.copy();
        objectMapper=configJackson2JsonRedisSerializer(objectMapper);

        GenericJackson2JsonRedisSerializer jacksonRedisSeriaizer = new GenericJackson2JsonRedisSerializer(objectMapper);
        return jacksonRedisSeriaizer;
    }

    /**
     * 配置Jackson2JsonRedisSerializer，并保存到JacksonUtilFactory,key为REDIS_SERIALIZER
     *
     * @param objectMapper the object mapper
     * @return object mapper
     */
    protected ObjectMapper configJackson2JsonRedisSerializer(ObjectMapper objectMapper){
        //ObjectMapper的配置
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        // 保存到JacksonUtilFactory中，键值为REDIS_SERIALIZER
        JacksonUtilFactory.addJsonUtil(SpringJacksonKeyType.REDIS_SERIALIZER,new DefaultJacksonUtil(objectMapper));
        return objectMapper;
    }
}
