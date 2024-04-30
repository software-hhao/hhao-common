
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

import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.core.*;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * The type Redis util.
 *
 * @author Wang
 * @since 2022 /2/4 20:02
 */
public class RedisUtil {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Instantiates a new Redis util.
     *
     * @param redisTemplate the redis template
     */
    @Autowired
    public RedisUtil(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public Boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 模糊查询获取key值
     */
    public Set keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 使用Redis的消息队列
     */
    public void convertAndSend(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }


    //=======================ValueOperations=======================
    public Boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public Boolean set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public Boolean setIfAbsent(String key, Object value){
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public Boolean setIfAbsent(String key, Object value, long timeout, TimeUnit unit){
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value,timeout,unit);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public Boolean setIfPresent(String key, Object value){
        try {
            return redisTemplate.opsForValue().setIfPresent(key, value);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public Boolean setIfPresent(String key, Object value, long timeout, TimeUnit unit){
        try {
            return redisTemplate.opsForValue().setIfPresent(key, value,timeout,unit);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public Boolean multiSet(Map<String,Object> map){
        try {
            if (map==null){
                return false;
            }
            redisTemplate.opsForValue().multiSet(map);
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public Boolean multiSetIfAbsent(Map<String,Object> map){
        try {
            if (map==null){
                return false;
            }
            redisTemplate.opsForValue().multiSetIfAbsent(map);
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    public Object getAndDelete(String key) {
        return key == null ? null : redisTemplate.opsForValue().getAndDelete(key);
    }

    public Object getAndExpire(String key, long timeout, TimeUnit unit) {
        if (key==null || unit==null){
            return null;
        }
        return redisTemplate.opsForValue().getAndExpire(key,timeout,unit);
    }

    public Object getAndExpire(String key, Duration timeout) {
        if (key==null || timeout==null){
            return null;
        }
        return redisTemplate.opsForValue().getAndExpire(key,timeout);
    }

    public Object getAndPersist(String key) {
        if (key==null){
            return null;
        }
        return redisTemplate.opsForValue().getAndPersist(key);
    }

    public Object getAndSet(String key,Object value) {
        if (key==null){
            return null;
        }
        return redisTemplate.opsForValue().getAndSet(key,value);
    }

    public List<Object> multiGet(Collection<String> keys) {
        if (keys==null){
            return null;
        }
        return redisTemplate.opsForValue().multiGet(keys);
    }

    public Long increment(String key){
        return redisTemplate.opsForValue().increment(key);
    }

    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public Double increment(String key, double delta){
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public Long decrement(String key){
        return redisTemplate.opsForValue().decrement(key);
    }

    public Long decrement(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    public Integer append(String key, String value){
        return redisTemplate.opsForValue().append(key, value);
    }

    public String get(String key, long start, long end) {
        if (key==null){
            return null;
        }
        return redisTemplate.opsForValue().get(key,start,end);
    }

    public Boolean set(String key, Object value, long offset) {
        try {
            redisTemplate.opsForValue().set(key, value,offset);
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public Long size(String key) {
        if (key==null){
            return null;
        }
        return redisTemplate.opsForValue().size(key);
    }

    public Boolean setBit(String key,long offset, boolean value) {
        try {
            return redisTemplate.opsForValue().setBit(key, offset,value);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public Boolean getBit(String key, long offset) {
        if (key==null){
            return null;
        }
        return redisTemplate.opsForValue().getBit(key,offset);
    }

    public List<Long> bitField(String key, BitFieldSubCommands subCommands){
        if (key==null){
            return null;
        }
        return redisTemplate.opsForValue().bitField(key,subCommands);
    }

    public RedisOperations<String, Object> getOperations(){
        return redisTemplate.opsForValue().getOperations();
    }

    //=======================HashOperations=======================
    public Long hDelete(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    public Boolean hHasKey(String key, Object hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    public Object hGet(String key, Object hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public List<Object> hMultiGet(String key, Collection<Object> hashKeys){
        return redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    public Long hIncrement(String key, Object hashKey, long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    public Double hIncrement(String key, Object hashKey, double delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    public Object hRandomKey(String key) {
        return redisTemplate.opsForHash().randomKey(key);
    }

    public Map.Entry<Object, Object> hRandomEntry(String key) {
        return redisTemplate.opsForHash().randomEntry(key);
    }

    public List<Object> hRandomKeys(String key,long count) {
        return redisTemplate.opsForHash().randomKeys(key,count);
    }

    public Map<Object,Object> hRandomEntries(String key,long count) {
        return redisTemplate.opsForHash().randomEntries(key,count);
    }

    public Set<Object> hKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    public Long hLengthOfValue(String key,Object hashKey) {
        return redisTemplate.opsForHash().lengthOfValue(key,hashKey);
    }

    public Long hSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    public Boolean hPutAll(String key, Map<Object, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public Boolean hPut(String key, Object hashKey,Object value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey,value);
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public Boolean hPutIfAbsent(String key, Object hashKey,Object value) {
        try {
            return redisTemplate.opsForHash().putIfAbsent(key, hashKey,value);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public List<Object> hValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    public Map<Object,Object> hEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public Cursor<Map.Entry<Object, Object>> hScan(String key, ScanOptions options){
        return redisTemplate.opsForHash().scan(key,options);
    }

    public RedisOperations<String, ?> hGetOperations(){
        return redisTemplate.opsForHash().getOperations();
    }

    //=======================HashOperations=======================

    public Long sAdd(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Long sRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Object sPop(String key) {
        try {
            return redisTemplate.opsForSet().pop(key);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public List<Object> sPop(String key, long count) {
        try {
            return redisTemplate.opsForSet().pop(key,count);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Boolean sMove(String key, Object value,String destKey) {
        try {
            return redisTemplate.opsForSet().move(key,value,destKey);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public Long sSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Boolean sIsMember(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public Map<Object, Boolean> sIsMember(String key, Object... objects) {
        try {
            return redisTemplate.opsForSet().isMember(key, objects);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Set<Object> sIntersect(String key, String otherKey) {
        try {
            return redisTemplate.opsForSet().intersect(key, otherKey);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Set<Object> sIntersect(String key, Collection<String> otherKeys) {
        try {
            return redisTemplate.opsForSet().intersect(key, otherKeys);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Set<Object> sIntersect(Collection<String> keys) {
        try {
            return redisTemplate.opsForSet().intersect(keys);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Long sIntersectAndStore(String key, String otherKey, String destKey) {
        try {
            return redisTemplate.opsForSet().intersectAndStore(key,otherKey,destKey);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Long sIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        try {
            return redisTemplate.opsForSet().intersectAndStore(key,otherKeys,destKey);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Long sIntersectAndStore(Collection<String> otherKeys, String destKey) {
        try {
            return redisTemplate.opsForSet().intersectAndStore(otherKeys,destKey);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Set<Object> sUnion(String key, String otherKey) {
        try {
            return redisTemplate.opsForSet().union(key,otherKey);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Set<Object> sUnion(String key, Collection<String> otherKeys) {
        try {
            return redisTemplate.opsForSet().union(key,otherKeys);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Set<Object> sUnion(Collection<String> otherKeys) {
        try {
            return redisTemplate.opsForSet().union(otherKeys);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Long sUnionAndStore(String key, String otherKey, String destKey) {
        try {
            return redisTemplate.opsForSet().unionAndStore(key,otherKey,destKey);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Long sUnionAndStore(String key, Collection<String> otherKey, String destKey) {
        try {
            return redisTemplate.opsForSet().unionAndStore(key,otherKey,destKey);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Long sUnionAndStore(Collection<String> otherKey, String destKey) {
        try {
            return redisTemplate.opsForSet().unionAndStore(otherKey,destKey);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Set<Object> sDifference(String key, String otherKey) {
        try {
            return redisTemplate.opsForSet().difference(key,otherKey);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Set<Object> sDifference(String key, Collection<String> otherKey) {
        try {
            return redisTemplate.opsForSet().difference(key,otherKey);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Set<Object> sDifference(Collection<String> keys) {
        try {
            return redisTemplate.opsForSet().difference(keys);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Long sDifferenceAndStore(String key, String otherKey, String destKey) {
        try {
            return redisTemplate.opsForSet().differenceAndStore(key,otherKey,destKey);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Long sDifferenceAndStore(String key, Collection<String> otherKey, String destKey) {
        try {
            return redisTemplate.opsForSet().differenceAndStore(key,otherKey,destKey);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Long sDifferenceAndStore(Collection<String> keys, String destKey) {
        try {
            return redisTemplate.opsForSet().differenceAndStore(keys,destKey);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Set<Object> sMembers(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Object sRandomMember(String key) {
        try {
            return redisTemplate.opsForSet().randomMember(key);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Set<Object> sDistinctRandomMembers(String key,long count) {
        try {
            return redisTemplate.opsForSet().distinctRandomMembers(key,count);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public List<Object> sRandomMembers(String key,long count) {
        try {
            return redisTemplate.opsForSet().randomMembers(key,count);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Cursor<Object> sScan(String key,ScanOptions options) {
        try {
            return redisTemplate.opsForSet().scan(key,options);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public RedisOperations<String, Object> sGetOperations(){
        return redisTemplate.opsForSet().getOperations();
    }

    //===============================ListOperations=================================
    public List<Object> lRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key,start,end);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Boolean lTrim(String key, long start, long end) {
        try {
            redisTemplate.opsForList().trim(key,start,end);
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public Long lSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Long lLeftPushAll(String key,Object... values) {
        try {
            return redisTemplate.opsForList().leftPushAll(key,values);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Long lLeftPushAll(String key,Collection<Object> values) {
        try {
            return redisTemplate.opsForList().leftPushAll(key,values);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Long lLeftPushIfPresent(String key, Object value) {
        try {
            return redisTemplate.opsForList().leftPushIfPresent(key, value);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Long lLeftPush(String key,Object pivot, Object value) {
        try {
            return redisTemplate.opsForList().leftPush(key, pivot,value);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Long lRightPush(String key, Object value) {
        try {
            return redisTemplate.opsForList().rightPush(key,value);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Long lRightPushAll(String key,Object... values) {
        try {
            return redisTemplate.opsForList().rightPushAll(key,values);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Long lRightPushAll(String key,Collection<Object> values) {
        try {
            return redisTemplate.opsForList().rightPushAll(key,values);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Long lRightPushIfPresent(String key, Object value) {
        try {
            return redisTemplate.opsForList().rightPushIfPresent(key,value);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Long lRightPush(String key, Object pivot,Object value) {
        try {
            return redisTemplate.opsForList().rightPush(key,pivot,value);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Object lMove(String sourceKey, RedisListCommands.Direction from, String destinationKey, RedisListCommands.Direction to) {
        try {
            return redisTemplate.opsForList().move(sourceKey,from,destinationKey,to);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Object lMove(String sourceKey, RedisListCommands.Direction from, String destinationKey, RedisListCommands.Direction to, long timeout, TimeUnit unit) {
        try {
            return redisTemplate.opsForList().move(sourceKey,from,destinationKey,to,timeout,unit);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Boolean lSet(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index,value);
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public Long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return 0L;
        }
    }

    public Object lIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Long lIndexOf(String key, Object value) {
        try {
            return redisTemplate.opsForList().indexOf(key, value);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Long lLastIndexOf(String key, Object value) {
        try {
            return redisTemplate.opsForList().lastIndexOf(key, value);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Object lLeftPop(String key) {
        try {
            return redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public List<Object> lLeftPop(String key, long count) {
        try {
            return redisTemplate.opsForList().leftPop(key,count);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Object lLeftPop(String key, long timeout, TimeUnit unit) {
        try {
            return redisTemplate.opsForList().leftPop(key,timeout,unit);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Object lRightPop(String key) {
        try {
            return redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public List<Object> lRightPop(String key, long count) {
        try {
            return redisTemplate.opsForList().rightPop(key,count);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Object lRightPop(String key, long timeout, TimeUnit unit) {
        try {
            return redisTemplate.opsForList().rightPop(key,timeout,unit);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Object lRightPopAndLeftPush(String sourceKey, String destinationKey) {
        try {
            return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey,destinationKey);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public Object lRightPopAndLeftPush(String sourceKey, String destinationKey, long timeout, TimeUnit unit) {
        try {
            return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey,destinationKey,timeout,unit);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public RedisOperations<String, Object> lGetOperations(){
        return redisTemplate.opsForList().getOperations();
    }


}
