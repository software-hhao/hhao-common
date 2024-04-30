
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

package com.hhao.common.springboot.config.redis.utils;

import com.hhao.common.jackson.support.NullValue;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Wang
 * @since 2022/2/5 20:45
 */
public class RedisReactiveUtil {
    protected final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    private ReactiveRedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisReactiveUtil(ReactiveRedisTemplate<String, Object> redisTemplate){
        this.redisTemplate=redisTemplate;
    }

    protected Object preProcessValue( Object value) {
        if (value != null) {
            return value;
        }
        return NullValue.INSTANCE;
    }

    public Mono<Boolean> set(String key, Object value) {
        try {
            value=preProcessValue(value);
            return redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Mono.just(false);
        }
    }

    public Mono<Boolean> set(String key, Object value, Duration timeout) {
        try {
            value=preProcessValue(value);
            return redisTemplate.opsForValue().set(key, value, timeout);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Mono.just(false);
        }
    }

    public Mono<Boolean> setIfAbsent(String key, Object value) {
        try {
            value=preProcessValue(value);
            return redisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Mono.just(false);
        }
    }

    public Mono<Boolean> setIfAbsent(String key, Object value, long time) {
        try {
            value=preProcessValue(value);
            if (time > 0) {
                return redisTemplate.opsForValue().setIfAbsent(key, value, Duration.ofSeconds(time));
            } else {
                return set(key, value);
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Mono.just(false);
        }
    }

    public Mono<Boolean> setIfPresent(String key, Object value) {
        try {
            value=preProcessValue(value);
            return redisTemplate.opsForValue().setIfPresent(key, value);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Mono.just(false);
        }
    }

    public Mono<Boolean> setIfPresent(String key, Object value, long time) {
        try {
            value=preProcessValue(value);
            if (time > 0) {
                return redisTemplate.opsForValue().setIfPresent(key, value, Duration.ofSeconds(time));
            } else {
                return set(key, value);
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Mono.just(false);
        }
    }

    public Mono<Boolean> multiSet(Map<String,Object> map) {
        try {
            return redisTemplate.opsForValue().multiSet(map);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Mono.just(false);
        }
    }

    public Mono<Boolean> multiSetIfAbsent(Map<String,Object> map){
        try {
            return redisTemplate.opsForValue().multiSetIfAbsent(map);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Mono.just(false);
        }
    }

    public <T> Mono<T> get(String key,Class<T> tClass) {
        if (key==null){
            return Mono.empty();
        }
        return redisTemplate.opsForValue().get(key)
                .flatMap(obj1->{
                    if (obj1 instanceof NullValue){
                        return Mono.empty();
                    }
                    return Mono.just(obj1);
                })
                .switchIfEmpty(Mono.empty())
                .cast(tClass);
    }

    public <T> Mono<T> getAndDelete(String key,Class<T> tClass) {
        if (key==null){
            return Mono.empty();
        }
        return redisTemplate.opsForValue().getAndDelete(key)
                .flatMap(obj1->{
                    if (obj1 instanceof NullValue){
                        return Mono.empty();
                    }
                    return Mono.just(obj1);
                })
                .switchIfEmpty(Mono.empty())
                .cast(tClass);
    }

    public <T> Mono<T> getAndExpire(String key,long time,Class<T> tClass) {
        if (key==null){
            return Mono.empty();
        }
        return redisTemplate.opsForValue().getAndExpire(key,Duration.ofSeconds(time))
                .flatMap(obj1->{
                    if (obj1 instanceof NullValue){
                        return Mono.empty();
                    }
                    return Mono.just(obj1);
                })
                .switchIfEmpty(Mono.empty())
                .cast(tClass);
    }

    public <T> Mono<T> getAndPersist(String key,Class<T> tClass) {
        if (key==null){
            return Mono.empty();
        }
        return redisTemplate.opsForValue().getAndPersist(key)
                .flatMap(obj1->{
                    if (obj1 instanceof NullValue){
                        return Mono.empty();
                    }
                    return Mono.just(obj1);
                })
                .switchIfEmpty(Mono.empty())
                .cast(tClass);
    }

    public <T> Mono<T> getAndSet(String key,Object value,Class<T> tClass) {
        if (key==null){
            return Mono.empty();
        }
        value=preProcessValue(value);
        return redisTemplate.opsForValue().getAndSet(key,value)
                .flatMap(obj1->{
                    if (obj1 instanceof NullValue){
                        return Mono.empty();
                    }
                    return Mono.just(obj1);
                })
                .switchIfEmpty(Mono.empty())
                .cast(tClass);
    }

    public Mono<List<Object>> multiGet(Collection<String> keys) {
        if (keys==null){
            return Mono.empty();
        }
        return redisTemplate.opsForValue().multiGet(keys);
    }

    public Mono<Long> increment(String key) {
        if (key==null){
            return Mono.empty();
        }
        return redisTemplate.opsForValue().increment(key);
    }

    public Mono<Long> increment(String key,long delta) {
        if (key==null){
            return Mono.empty();
        }
        return redisTemplate.opsForValue().increment(key,delta);
    }

    public Mono<Double> increment(String key,double delta) {
        if (key==null){
            return Mono.empty();
        }
        return redisTemplate.opsForValue().increment(key,delta);
    }

    public Mono<Long> decrement(String key) {
        if (key==null){
            return Mono.empty();
        }
        return redisTemplate.opsForValue().decrement(key);
    }

    public Mono<Long> decrement(String key, long delta) {
        if (key==null){
            return Mono.empty();
        }
        return redisTemplate.opsForValue().decrement(key,delta);
    }

    public Mono<Long> append(String key, String value) {
        if (key==null){
            return Mono.empty();
        }
        return redisTemplate.opsForValue().append(key,value);
    }

    public Mono<String> get(String key,long start, long end) {
        if (key==null){
            return Mono.empty();
        }
        return redisTemplate.opsForValue().get(key,start,end);
    }

    Mono<Long> set(String key, Object value, long offset){
        if (key==null){
            return Mono.empty();
        }
        return redisTemplate.opsForValue().set(key,value,offset);
    }

    Mono<Long> size(String key){
        if (key==null){
            return Mono.empty();
        }
        return redisTemplate.opsForValue().size(key);
    }

    Mono<Boolean> setBit(String key, long offset, boolean value){
        if (key==null){
            return Mono.empty();
        }
        return redisTemplate.opsForValue().setBit(key,offset,value);
    }

    Mono<Boolean> getBit(String key, long offset){
        if (key==null){
            return Mono.empty();
        }
        return redisTemplate.opsForValue().getBit(key,offset);
    }

    Mono<List<Long>> bitField(String key, BitFieldSubCommands command){
        if (key==null){
            return Mono.empty();
        }
        return redisTemplate.opsForValue().bitField(key,command);
    }

    Mono<Boolean> delete(String key){
        if (key==null){
            return Mono.empty();
        }
        return redisTemplate.opsForValue().delete(key);
    }
}
