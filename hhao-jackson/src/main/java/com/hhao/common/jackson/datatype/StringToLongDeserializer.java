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
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;

/**
 * @author Wan
 * @since 2024/4/30 8:49
 */
public class StringToLongDeserializer extends SimpleModule {

    /**
     * 构造函数，注册Long类型的序列化器和反序列化器。
     */
    public StringToLongDeserializer() {
        super("StringToLongDeserializer");
        addDeserializer(Long.class, new DefaultStringToLongDeserializer());
    }

    /**
     * 将String转换为Long的反序列化器。
     */
    private static class DefaultStringToLongDeserializer extends JsonDeserializer<Long> {
        @Override
        public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException,NumberFormatException {
            String text = p.getValueAsString();
            return Long.parseLong(text);
        }
    }
}
