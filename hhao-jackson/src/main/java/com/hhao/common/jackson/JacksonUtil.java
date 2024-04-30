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

package com.hhao.common.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * The interface Jackson util.
 *
 * @author Wang
 * @since 1.0.0
 */
public interface JacksonUtil {
    /**
     * 返回ObjectMapper
     *
     * @return object mapper
     */
    ObjectMapper getObjectMapper();

    /**
     * 将Java对象、数组序列化字符串
     *
     * @param obj       the obj
     * @param classView the class view
     * @return string
     */
    String obj2String(Object obj, Class<?> classView);

    /**
     * 将Java对象、数组序列化字符串
     *
     * @param obj the obj
     * @return string
     */
    String obj2String(Object obj);

    /**
     * 将字符串反序列化为Java对象
     *
     * @param <T>       the type parameter
     * @param text      the text
     * @param clazz     the clazz
     * @param classView the class view
     * @return t
     */
    <T> T string2Pojo(String text, Class<T> clazz, Class<?> classView);

    /**
     * 将字符串反序列化为Java对象
     *
     * @param <T>       the type parameter
     * @param text      the text
     * @param type      the type
     * @param classView the class view
     * @return t
     */
    <T> T string2Pojo(String text, JavaType type, Class<?> classView);

    /**
     * 将字符串反序列化为Java对象
     *
     * @param <T>   the type parameter
     * @param text  the text
     * @param clazz the clazz
     * @return t
     */
    <T> T string2Pojo(String text, Class<T> clazz);

    /**
     * 将字符串反序列化为Java对象
     *
     * @param <T>  the type parameter
     * @param text the text
     * @param type the type
     * @return t
     */
    <T> T string2Pojo(String text, JavaType type);

    /**
     * 数组字符串转换化List
     *
     * @param <T>       the type parameter
     * @param text      the text
     * @param clazz     the clazz
     * @param classView the class view
     * @return list
     */
    <T> List<T> string2List(String text, Class<T> clazz, Class<?> classView);

    /**
     * 数组字符串转换化List
     *
     * @param <T>   the type parameter
     * @param text  the text
     * @param clazz the clazz
     * @return list
     */
    <T> List<T> string2List(String text, Class<T> clazz);

    /**
     * 字符串反序列化为Map对象（固定类型）
     *
     * @param <E>        the type parameter
     * @param <V>        the type parameter
     * @param text       the text
     * @param keyClazz   the key clazz
     * @param valueClazz the value clazz
     * @param classView  the class view
     * @return map
     */
    <E, V> Map<E, V> string2Map(String text, Class<E> keyClazz, Class<V> valueClazz, Class<?> classView);

    /**
     * 字符串反序列化为Map对象（固定类型）
     *
     * @param <E>        the type parameter
     * @param <T>        the type parameter
     * @param text       the text
     * @param keyClazz   the key clazz
     * @param valueClazz the value clazz
     * @return map
     */
    <E, T> Map<E, T> string2Map(String text, Class<E> keyClazz, Class<T> valueClazz);

    /**
     * 将字符串反序列化为Map对象（非固定类型）
     *
     * @param text the text
     * @return map
     */
    Map<String, Object> string2Map(String text);

    /**
     * 把字符串解析成Map,深度解析
     *
     * @param text the text
     * @return map
     */
    Map<Object, Object> string2MapDeeply(String text);

    /**
     * 把Map转换为JavaBean
     *
     * @param <T>   the type parameter
     * @param map   the map
     * @param clazz the clazz
     * @return t
     */
    <T> T map2Pojo(Map map, Class<T> clazz);

    /**
     * 把Map转换为String
     *
     * @param map       the map
     * @param classView the class view
     * @return string
     */
    String map2String(Map map, Class<?> classView);

    /**
     * 把Map转换为String
     *
     * @param map the map
     * @return string
     */
    String map2String(Map map);

    <T,H> T string2Pojo(String text, Class<T> clazz, Class<H> valueRefClass,Class<?> classView);

}
