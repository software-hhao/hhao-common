
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
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hhao.common.jackson.support.NullValue;

import java.io.IOException;

/**
 * @author Wang
 * @since 2022/2/7 21:34
 */
public class NullValueResolver extends SimpleModule {
    /**
     * Instantiates a new Null value resolver.
     */
    public NullValueResolver(){
        super("NullValueResolver");
        addSerializer(NullValue.class, new NullValueSerializer());
        addDeserializer(NullValue.class, new NullValueDeserializer());
    }

    private static class NullValueSerializer extends JsonSerializer<NullValue> {
        @Override
        public void serialize(NullValue value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeObject(null);
        }

        @Override
        public void serializeWithType(NullValue value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
            WritableTypeId typeIdDef = typeSer.writeTypePrefix(gen, typeSer.typeId(value, JsonToken.VALUE_STRING));
            serialize(value, gen, serializers);
            typeSer.writeTypeSuffix(gen, typeIdDef);
        }

        @Override
        public Class<NullValue> handledType() {
            return NullValue.class;
        }
    }

    private static class NullValueDeserializer extends JsonDeserializer<NullValue> {
        @Override
        public NullValue deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return new NullValue();
        }

        @Override
        public Class<NullValue> handledType() {
            return NullValue.class;
        }
    }
}
