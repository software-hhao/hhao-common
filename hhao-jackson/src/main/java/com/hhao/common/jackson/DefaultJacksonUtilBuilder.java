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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jdk7.Jdk7Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hhao.common.jackson.datatype.EnumResolver;
import com.hhao.common.jackson.datatype.JavaDateTimeModule;
import com.hhao.common.jackson.datatype.NullValueResolver;
import com.hhao.common.money.jackson.MoneyModule;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The type Default jackson util builder.
 *
 * @param <T> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public class DefaultJacksonUtilBuilder<T extends ObjectMapper> implements JacksonUtilBuilder<T>{
    private List<Module> modules = new CopyOnWriteArrayList<>();
    private Map<Enum, Boolean> configures = new ConcurrentHashMap<>();
    private Function<BeanProperty, JsonFormat.Value> jsonFormatFilterFunction;

    public DefaultJacksonUtilBuilder(JacksonConfigProperties config) {
        this.initWellKnownConfigures();
        this.initWellKnownModules(config);
    }

    public List<Module> getModules() {
        return modules;
    }

    public Map<Enum, Boolean> getConfigures() {
        return configures;
    }

    /**
     * 配置ObjectMapper
     *
     * @return void
     **/
    public DefaultJacksonUtilBuilder initWellKnownConfigures() {
        //关闭序列化时日期时间解析成数值型的TIMESTAMPS
        configures.put(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //true:未包含@JsonView的属性全部输出
        //false:未包含@JsonView的属性不输出
        //configures.put(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        configures.put(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        configures.put(JsonParser.Feature.ALLOW_COMMENTS, false);

        return this;
    }

    /**
     * 注册、加载Jackson相关模块，需要相关的模块依赖包
     *
     * @return void
     **/
    public DefaultJacksonUtilBuilder initWellKnownModules(JacksonConfigProperties config) {
        modules.add(new JavaTimeModule());
        modules.add(new Jdk8Module());
        modules.add(new Jdk7Module());
        modules.add(new ParameterNamesModule());
        modules.add(new EnumResolver());
        modules.add(new JavaDateTimeModule(config.getThrowExceptionOnDatetimeConversionError(),jsonFormatFilterFunction));
        modules.add(new MoneyModule(config.getMoneyJsonSerializationConfig()));
        modules.add(new NullValueResolver());
        return this;
    }

    public DefaultJacksonUtilBuilder addModule(Module module) {
        modules.add(module);
        return this;
    }

    public DefaultJacksonUtilBuilder addConfigure(Enum feature, Boolean value) {
        configures.put(feature, value);
        return this;
    }

    public DefaultJacksonUtilBuilder setJsonFormatFilterFunction(Function<BeanProperty, JsonFormat.Value> jsonFormatFilterFunction) {
        this.jsonFormatFilterFunction = jsonFormatFilterFunction;
        return this;
    }

    public ObjectMapper registerModule(final ObjectMapper objectMapper) {
        modules.forEach(module -> objectMapper.registerModule(module));
        return objectMapper;
    }

    public ObjectMapper configure(final ObjectMapper objectMapper) {
        configures.forEach((feature, value) -> {
            if (feature instanceof SerializationFeature) {
                objectMapper.configure((SerializationFeature) feature, value);
            } else if (feature instanceof MapperFeature) {
                //objectMapper.configure((MapperFeature) feature, value);
            } else if (feature instanceof DeserializationFeature) {
                objectMapper.configure((DeserializationFeature) feature, value);
            } else if (feature instanceof JsonGenerator.Feature) {
                objectMapper.configure((JsonGenerator.Feature) feature, value);
            } else if (feature instanceof JsonParser.Feature) {
                objectMapper.configure((JsonParser.Feature) feature, value);
            }
        });
        return objectMapper;
    }

    @Override
    public JacksonUtil build(Class<T> targetClass, Consumer<T> consumer) {
        T mapper = createObjectMapper(targetClass);
        return buildInternal((ObjectMapper) mapper, (Consumer<ObjectMapper>) consumer);
    }

    @Override
    public JacksonUtil build(Class<T> targetClass){
        return build(targetClass,null);
    }

    private T createObjectMapper(Class<T> targetClass) {
        if (targetClass.equals(ObjectMapper.class)) {
            return targetClass.cast(new ObjectMapper());
        } else if (targetClass.equals(XmlMapper.class)) {
            return targetClass.cast(new XmlMapper());
        } else {
            throw new IllegalArgumentException("Unsupported ObjectMapper type: " + targetClass.getName());
        }
    }

    private JacksonUtil buildInternal(ObjectMapper objectMapper, Consumer<ObjectMapper> consumer) {
        registerModule(objectMapper);
        configure(objectMapper);
        if (consumer != null) {
            consumer.accept(objectMapper);
        }
        return new DefaultJacksonUtil(objectMapper);
    }
}
