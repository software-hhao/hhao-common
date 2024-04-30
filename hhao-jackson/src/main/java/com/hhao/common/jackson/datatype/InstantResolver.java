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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;

/**
 * Instant序列与反序列
 *
 * @author Wang
 * @since 1.0.0
 */
public class InstantResolver {
    /**
     * 自定义Instant数据类型的序列化操作
     * 序列化Instant输出自1970-01-01T00:00:00Z以来经历的秒数
     */
    public static class InstantSerializer extends JsonSerializer<Instant> {
        @Override
        public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeObject(value.getEpochSecond());
        }

        @Override
        public Class<Instant> handledType() {
            return Instant.class;
        }
    }

    /**
     * 自定义Instant数据类型的反序列化操作
     * 接收1970-01-01T00:00:00Z以来经历的秒数，可以是字符串，也可以是长整型
     */
    public static class InstantDeserializer extends JsonDeserializer<Instant> {
        @Override
        public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (p.getCurrentToken().equals(JsonToken.VALUE_STRING)) {
                return Instant.ofEpochSecond(Long.parseLong(p.getText()));
            } else if (p.getCurrentToken().equals(JsonToken.VALUE_NUMBER_INT)) {
                return Instant.ofEpochSecond(p.getBigIntegerValue().longValue());
            }
            return null;
        }

        @Override
        public Class<Instant> handledType() {
            return Instant.class;
        }
    }
}
