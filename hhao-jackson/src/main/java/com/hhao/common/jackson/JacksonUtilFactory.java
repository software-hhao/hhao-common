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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Jackson util factory.
 *
 * @author Wang
 * @since 1.0.0
 */
public class JacksonUtilFactory {
    private static final Logger logger = LoggerFactory.getLogger(JacksonUtilFactory.class);
    private static Map<JacksonKey, JacksonUtil> jsonUtilMap = new ConcurrentHashMap<>();
    private static Map<JacksonKey, JacksonUtil> xmlUtilMap = new ConcurrentHashMap<>();

    static {
        JacksonConfigProperties config = new JacksonConfigProperties();
        jsonUtilMap.put(JacksonKeyType.DEFAULT, new DefaultJacksonUtilBuilder(config)
                .build(ObjectMapper.class, mapper -> {
                }));

        xmlUtilMap.put(JacksonKeyType.DEFAULT, new DefaultJacksonUtilBuilder(config)
                .build(XmlMapper.class, mapper -> {
                }));
    }

    /**
     * Add json util.
     *
     * @param key      the key
     * @param jsonUtil the json util
     */
    public static void addJsonUtil(JacksonKey key, JacksonUtil jsonUtil) {
        Objects.requireNonNull(jsonUtil, "jacksonUtil require not null");
        jsonUtilMap.put(key, jsonUtil);
    }

    /**
     * Add xml util.
     *
     * @param key     the key
     * @param xmlUtil the xml util
     */
    public static void addXmlUtil(JacksonKey key, JacksonUtil xmlUtil) {
        Objects.requireNonNull(xmlUtil, "jacksonUtil require not null");
        xmlUtilMap.put(key, xmlUtil);
    }

    /**
     * Gets json util.
     *
     * @param key the key
     * @return the json util
     */
    public static JacksonUtil getJsonUtil(JacksonKey key) {
        JacksonUtil util = jsonUtilMap.get(key);
        if (util == null) {
            logger.warn("JacksonUtil for key '{}' not found, returning default.", key);
            util = getJsonUtil(); // 确保getJsonUtil()有一个能处理默认情况的实现
        }
        return util;
    }

    /**
     * Gets json util.
     *
     * @return the json util
     */
    public static JacksonUtil getJsonUtil() {
        return jsonUtilMap.get(JacksonKeyType.DEFAULT);
    }

    /**
     * Gets xml util.
     *
     * @param key the key
     * @return the xml util
     */
    public static JacksonUtil getXmlUtil(JacksonKey key) {
        JacksonUtil util = xmlUtilMap.get(key);
        if (util == null) {
            logger.warn("JacksonUtil for key '{}' not found, returning default.", key);
            util = getXmlUtil(); // 确保getXmlUtil()有一个能处理默认情况的实现
        }
        return util;
    }

    /**
     * Gets xml util.
     *
     * @return the xml util
     */
    public static JacksonUtil getXmlUtil() {
        return xmlUtilMap.get(JacksonKeyType.DEFAULT);
    }

    /**
     * The interface Jackson key.
     */
    public interface JacksonKey {

    }

    /**
     * The enum Jackson key type.
     */
    public enum JacksonKeyType implements JacksonKey{
        /**
         * Default jackson key type.
         */
// 默认
        DEFAULT
    }
}
