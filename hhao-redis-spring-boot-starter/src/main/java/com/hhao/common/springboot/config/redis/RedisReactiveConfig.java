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


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import reactor.core.publisher.Flux;

/**
 * RedisReactiveAutoConfiguration
 *
 * @author Wang
 * @since 2022 /2/5 20:17
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type= ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnProperty(prefix = "com.hhao.config.redis",name = "enable",havingValue = "true",matchIfMissing = true)
public class RedisReactiveConfig {

    /**
     * Jdk serializer reactive redis template reactive redis template.
     *
     * @param reactiveRedisConnectionFactory the reactive redis connection factory
     * @param resourceLoader                 the resource loader
     * @return the reactive redis template
     */
    @Bean
    public ReactiveRedisTemplate<Object, Object> jdkSerializerReactiveRedisTemplate(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory, ResourceLoader resourceLoader) {
        RedisSerializer<Object> javaSerializer = RedisSerializer.java(resourceLoader.getClassLoader());
        RedisSerializationContext<Object, Object> serializationContext = RedisSerializationContext
                .newSerializationContext()
                .key(javaSerializer)
                .value(javaSerializer)
                .hashKey(javaSerializer)
                .hashValue(javaSerializer)
                .build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext);
    }


    /**
     * Reactive redis template reactive redis template.
     *
     * @param reactiveRedisConnectionFactory the reactive redis connection factory
     * @param redisSerializer                the redis serializer
     * @return the reactive redis template
     */
    @Bean("reactiveRedisTemplate")
    @Primary
    @SuppressWarnings("all")
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory, @Qualifier("genericJackson2JsonRedisSerializer") RedisSerializer<Object> redisSerializer) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        RedisSerializationContext<String, Object> serializationContext = RedisSerializationContext
                .<String,Object>newSerializationContext()
                .key(stringRedisSerializer)
                .value(redisSerializer)
                .hashKey(stringRedisSerializer)
                .hashValue(redisSerializer)
                .build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext);
    }

    /**
     * Reactive string redis template reactive string redis template.
     *
     * @param reactiveRedisConnectionFactory the reactive redis connection factory
     * @return the reactive string redis template
     */
    @Bean
    @Primary
    @SuppressWarnings("all")
    public ReactiveStringRedisTemplate reactiveStringRedisTemplate(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        return new ReactiveStringRedisTemplate(reactiveRedisConnectionFactory);
    }
}
