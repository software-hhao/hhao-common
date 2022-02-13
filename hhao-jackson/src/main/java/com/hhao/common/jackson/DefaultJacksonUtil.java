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


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Jackson操作的封装
 *
 * @author Wang
 * @since 1.0.0
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class DefaultJacksonUtil implements JacksonUtil {
    /**
     * 定义Jackson解析器
     **/
    private ObjectMapper mapper;

    /**
     * Instantiates a new Default jackson util.
     *
     * @param mapper the mapper
     */
    public DefaultJacksonUtil(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return mapper;
    }


    /**
     * Sets object mapper.
     *
     * @param objectMapper the object mapper
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.mapper = objectMapper;
    }


    /**
     * 将Java对象、数组序列化JSON字符串
     *
     * @param obj
     * @param classView
     * @return
     */
    @Override
    public String obj2String(Object obj, Class<?> classView) {
        try {
            if (classView != null) {
                return mapper.writerWithView(classView).writeValueAsString(obj);
            }
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String obj2String(Object obj) {
        return obj2String(obj, null);
    }

    /**
     * 将JSON字符串反序列化为Java对象
     *
     * @param text
     * @param clazz
     * @param classView
     * @param <T>
     * @return
     */
    @Override
    public <T> T string2Pojo(String text, Class<T> clazz, Class<?> classView) {
        try {
            if (classView != null) {
                return mapper.readerWithView(classView).forType(clazz).readValue(text);
            } else {
                return mapper.readValue(text, clazz);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T,H> T string2Pojo(String text,Class<T> clazz,Class<H> valueRefClass, Class<?> classView) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(clazz,valueRefClass);
        try {
            if (classView != null) {
                return mapper.readerWithView(classView).forType(javaType).readValue(text);
            } else {
                return mapper.readValue(text, javaType);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将JSON字符串反序列化为Java对象
     *
     * @param text
     * @param type
     * @param classView
     * @param <T>
     * @return
     */
    @Override
    public <T> T string2Pojo(String text, JavaType type, Class<?> classView) {
        try {
            if (classView != null) {
                return mapper.readerWithView(classView).forType(type).readValue(text);
            } else {
                return mapper.readValue(text, type);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将JSON字符串反序列化为Java对象
     *
     * @param text
     * @param clazz
     * @param <T>
     * @return
     */
    @Override
    public <T> T string2Pojo(String text, Class<T> clazz) {
        return string2Pojo(text, clazz, null);
    }

    /**
     * 将JSON字符串反序列化为Java对象
     *
     * @param text
     * @param type
     * @param <T>
     * @return
     */
    @Override
    public <T> T string2Pojo(String text, JavaType type) {
        return string2Pojo(text, type, null);
    }

    /**
     * JSON数组字符串转换化List
     *
     * @param text
     * @param clazz
     * @param classView
     * @param <T>
     * @return
     */
    @Override
    public <T> List<T> string2List(String text, Class<T> clazz, Class<?> classView) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
        List<T> list = null;
        try {
            if (classView != null) {
                list = (List<T>) mapper.readerWithView(classView).forType(javaType).readValue(text);
            } else {
                list = (List<T>) mapper.readValue(text, javaType);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * JSON数组字符串转换化List
     *
     * @param text
     * @param clazz
     * @param <T>
     * @return
     */
    @Override
    public <T> List<T> string2List(String text, Class<T> clazz) {
        return string2List(text, clazz, null);
    }

    /**
     * 将JSON字符串反序列化为Map对象（固定类型）
     *
     * @param text
     * @param keyClazz
     * @param valueClazz
     * @param classView
     * @param <E>
     * @param <V>
     * @return
     */
    @Override
    public <E, V> Map<E, V> string2Map(String text, Class<E> keyClazz, Class<V> valueClazz, Class<?> classView) {
        MapType mapType = mapper.getTypeFactory().constructMapType(Map.class, keyClazz, valueClazz);
        try {
            if (classView == null) {
                return mapper.readValue(text, mapType);
            } else {
                return mapper.readerWithView(classView).forType(mapType).readValue(text);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将JSON字符串反序列化为Map对象（固定类型）
     *
     * @param text
     * @param keyClazz
     * @param valueClazz
     * @param <E>
     * @param <T>
     * @return
     */
    @Override
    public <E, T> Map<E, T> string2Map(String text, Class<E> keyClazz, Class<T> valueClazz) {
        return string2Map(text, keyClazz, valueClazz, null);
    }

    /**
     * 将JSON字符串反序列化为Map对象（非固定类型）
     *
     * @param text
     * @return
     */
    @Override
    public Map<String, Object> string2Map(String text) {
        try {
            return mapper.readValue(text, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将JSON字符串深度反序列化为Map对象（非固定类型）
     * 把JSON解析成List，如果List内部的元素存在jsonString，继续解析
     *
     * @param text:
     * @return java.util.List<java.lang.Object>
     **/
    private List<Object> json2ListRecursion(String text) {
        if (text == null) {
            return null;
        }
        List<Object> list = null;
        try {
            list = mapper.readValue(text, List.class);
            for (Object obj : list) {
                if (obj != null && obj instanceof String) {
                    String str = (String) obj;
                    if (str.startsWith("[")) {
                        obj = json2ListRecursion(str);
                    } else if (obj.toString().startsWith("{")) {
                        obj = json2mapRecursion(str);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * 把JSON字符串解析成Map，如果Map内部的value存在jsonString，继续解析
     *
     * @param text:
     * @return java.util.Map<java.lang.Object, java.lang.Object>
     **/
    private Map<Object, Object> json2mapRecursion(String text) {
        if (text == null) {
            return null;
        }
        Map<Object, Object> map = null;
        try {
            map = mapper.readValue(text, Map.class);
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                Object obj = entry.getValue();
                if (obj != null && obj instanceof String) {
                    String str = ((String) obj);

                    if (str.startsWith("[")) {
                        List<?> list = json2ListRecursion(str);
                        map.put(entry.getKey(), list);
                    } else if (str.startsWith("{")) {
                        Map<Object, Object> mapRecursion = json2mapRecursion(str);
                        map.put(entry.getKey(), mapRecursion);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    /**
     * 把字符串解析成Map,深度解析
     *
     * @param text
     * @return
     */
    @Override
    public Map<Object, Object> string2MapDeeply(String text) {
        return json2mapRecursion(text);
    }

    /**
     * 把Map转换为JavaBean
     *
     * @param map:
     * @param clazz:
     * @return T
     **/
    @Override
    public <T> T map2Pojo(Map map, Class<T> clazz) {
        return mapper.convertValue(map, clazz);
    }

    /**
     * 把Map转换为JSON
     *
     * @param map
     * @param classView
     * @return
     */
    @Override
    public String map2String(Map map, Class<?> classView) {
        try {
            if (classView != null) {
                return mapper.writerWithView(classView).forType(Map.class).writeValueAsString(map);
            } else {
                return mapper.writeValueAsString(map);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 把Map转换为JSON
     *
     * @param map
     * @return
     */
    @Override
    public String map2String(Map map) {
        return map2String(map, null);
    }
}




