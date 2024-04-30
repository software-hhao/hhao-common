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

package com.hhao.common.money.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.io.IOException;

/**
 * CurrencyUnit反序列化
 *
 * @author Wang
 * @since 1.0.0
 */
public class CurrencyUnitDeserializer extends JsonDeserializer<CurrencyUnit> {
    protected final Logger logger = LoggerFactory.getLogger(CurrencyUnitDeserializer.class);

    @Override
    public Object deserializeWithType(final JsonParser parser, final DeserializationContext context, final TypeDeserializer deserializer) throws IOException {
        // effectively assuming no type information at all
        return deserialize(parser, context);
    }

    @Override
    public CurrencyUnit deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
        final String currencyCode = parser.getValueAsString();
        try {
            CurrencyUnit currencyUnit = Monetary.getCurrency(currencyCode.trim());
            return currencyUnit;
        }catch (Exception e){
            logger.debug(e.getMessage());
        }
        return null;
    }

}
