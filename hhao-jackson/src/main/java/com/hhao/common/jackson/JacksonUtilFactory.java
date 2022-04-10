/*
 * Copyright 2018-2021 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.gnu.org/licenses/gpl-3.0.html
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
import com.hhao.common.money.jackson.MoneyProperties;

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
    /**
     * The constant DEFAULT_KEY.
     */
    public static Boolean dataTimeErrorThrow=true;
    public static final String DEFAULT_KEY = "default";
    private static Map<String, JacksonUtil> jsonUtilMap = new ConcurrentHashMap<>();
    private static Map<String, JacksonUtil> xmlUtilMap = new ConcurrentHashMap<>();

    static {
        jsonUtilMap.put(DEFAULT_KEY, new DefaultJacksonUtilBuilder()
                .init(dataTimeErrorThrow,new MoneyProperties(true,false,true))
                .build(ObjectMapper.class,mapper -> {
                }));

        xmlUtilMap.put(DEFAULT_KEY, new DefaultJacksonUtilBuilder()
                .init(dataTimeErrorThrow,new MoneyProperties(true,false,true))
                .build(XmlMapper.class, mapper -> {
                }));
    }

    /**
     * Add json util.
     *
     * @param key      the key
     * @param jsonUtil the json util
     */
    public static void addJsonUtil(String key, JacksonUtil jsonUtil) {
        Objects.requireNonNull(jsonUtil, "jacksonUtil require not null");
        jsonUtilMap.put(key, jsonUtil);
    }

    /**
     * Add xml util.
     *
     * @param key     the key
     * @param xmlUtil the xml util
     */
    public static void addXmlUtil(String key, JacksonUtil xmlUtil) {
        Objects.requireNonNull(xmlUtil, "jacksonUtil require not null");
        xmlUtilMap.put(key, xmlUtil);
    }

    /**
     * Gets json util.
     *
     * @param key the key
     * @return the json util
     */
    public static JacksonUtil getJsonUtil(String key) {
        return jsonUtilMap.getOrDefault(key, getJsonUtil());
    }

    /**
     * Gets json util.
     *
     * @return the json util
     */
    public static JacksonUtil getJsonUtil() {
        return jsonUtilMap.get(DEFAULT_KEY);
    }

    /**
     * Gets xml util.
     *
     * @param key the key
     * @return the xml util
     */
    public static JacksonUtil getXmlUtil(String key) {
        return xmlUtilMap.getOrDefault(key, getXmlUtil());
    }

    /**
     * Gets xml util.
     *
     * @return the xml util
     */
    public static JacksonUtil getXmlUtil() {
        return xmlUtilMap.get(DEFAULT_KEY);
    }
}
