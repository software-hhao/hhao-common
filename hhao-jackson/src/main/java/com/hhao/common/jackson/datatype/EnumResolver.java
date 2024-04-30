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

package com.hhao.common.jackson.datatype;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * The type Enum resolver.
 *
 * @author Wang
 * @since 1.0.0
 */
public class EnumResolver extends SimpleModule {
    /**
     * Instantiates a new Enum resolver.
     */
    public EnumResolver() {
        super("EnumResolver");
        addSerializer(Enum.class, new EnumSerializer(Enum.class));
        addDeserializer(Enum.class, new EnumDeserializer(Enum.class));
    }
    /**
     * Enum反序列化
     * 如果值是String类型，则根据name()生成Enum；
     * 如果值是int类型，则根据ordinal()生成Enum;
     */
    private static class EnumDeserializer extends StdDeserializer<Enum> implements ContextualDeserializer {
        private Class<Enum> enumType = null;

        /**
         * Instantiates a new Enum deserializer.
         *
         * @param t the t
         */
        protected EnumDeserializer(Class<?> t) {
            super(t);
            enumType = (Class<Enum>) t;
        }

        @Override
        public Enum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            Enum[] enumConstants = enumType.getEnumConstants();
            if (p.getCurrentToken().equals(JsonToken.VALUE_STRING)) {
                String source = p.getValueAsString();
                if (source.length() == 0) {
                    return null;
                }
                for (Enum constant : enumConstants) {
                    if (constant.name().equalsIgnoreCase(source)) {
                        return constant;
                    }
                }
            } else if (p.getCurrentToken().equals(JsonToken.VALUE_NUMBER_INT)) {
                int source = p.getValueAsInt();
                for (Enum constant : enumConstants) {
                    if (constant.ordinal() == source) {
                        return constant;
                    }
                }
            }
            return null;
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
            if (property.getType().isEnumType()) {
                if (!this.enumType.equals(property.getType().getRawClass())) {
                    return new EnumDeserializer(property.getType().getRawClass());
                }
                return this;
            }
            return null;
        }
    }

    /**
     * 自定义Enum的序列化
     * 根据@JsonFormat的pattern序列化;
     * 如果值为"name",则输出name();
     * 否则输出ordinal();
     */
    private static class EnumSerializer extends StdSerializer<Enum> implements ContextualSerializer {
        private String serializerType = "name";

        /**
         * Instantiates a new Enum serializer.
         *
         * @param t the t
         */
        protected EnumSerializer(Class<Enum> t) {
            super(t);
        }

        /**
         * Instantiates a new Enum serializer.
         *
         * @param t              the t
         * @param serializerType the serializer type
         */
        protected EnumSerializer(Class<Enum> t, String serializerType) {
            super(t);
            this.serializerType = serializerType;
        }

        @Override
        public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
            JsonFormat.Value format = findFormatOverrides(prov, property, handledType());
            String serializerType = "name";
            if (format != null) {
                serializerType = format.getPattern();
            }
            if (!this.serializerType.equalsIgnoreCase(serializerType)) {
                return new EnumSerializer(Enum.class, serializerType);
            }
            return this;
        }

        @Override
        public void serialize(Enum value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeObject("name".equalsIgnoreCase(serializerType) ? value.name() : value.ordinal());
        }
    }
}
