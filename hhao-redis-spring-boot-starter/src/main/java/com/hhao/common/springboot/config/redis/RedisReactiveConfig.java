/*
 * Copyright 2020-2021 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hhao.common.springboot.config.redis;

import com.hhao.common.springboot.config.redis.utils.RedisReactiveUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
 * @since 2022/2/5 20:17
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ ReactiveRedisConnectionFactory.class, ReactiveRedisTemplate.class, Flux.class })
@ConditionalOnProperty(prefix = "com.hhao.config.redis",name = "enable",havingValue = "true",matchIfMissing = true)
public class RedisReactiveConfig {

    @Bean("reactiveRedisTemplate")
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

    @Bean
    @ConditionalOnMissingBean(name = "reactiveStringRedisTemplate")
    @SuppressWarnings("all")
    public ReactiveStringRedisTemplate reactiveStringRedisTemplate(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        return new ReactiveStringRedisTemplate(reactiveRedisConnectionFactory);
    }

    /**
     * 生成Redis工具类Bean
     * @param redisTemplate
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisReactiveUtil redisReactiveUtil(@Qualifier("reactiveRedisTemplate") ReactiveRedisTemplate<String, Object> redisTemplate){
        return new RedisReactiveUtil(redisTemplate);
    }
}
