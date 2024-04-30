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

package com.hhao.common.springboot.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * Enum转换为Integer，Enum的ordinal
 *
 * @author Wan
 * @since 1.0.0
 */
public class EnumToIntegerConvertFactory implements ConverterFactory<Enum<?>, Integer> {
    @Override
    public <T extends Integer> Converter<Enum<?>, T> getConverter(Class<T> targetType) {
        return new EnumToInteger();
    }

    private static class EnumToInteger<T extends Integer> implements Converter<Enum<?>, T> {
        @Override
        public T convert(Enum<?> source) {
            if (source == null) {
                return null;
            }
            return (T) Integer.valueOf(source.ordinal());
        }
    }
}
