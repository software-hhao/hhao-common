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
 * 字符串到Enum的转换,根据enum的name转换
 *
 * @author Wan
 * @since 1.0.0
 */
public class StringToEnumConverterFactory implements ConverterFactory<String, Enum<?>> {
    @Override
    public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnum<T>(targetType);
    }

    private static class StringToEnum<T extends Enum<?>> implements Converter<String, T> {
        private Class<T> enumType = null;

        /**
         * Instantiates a new String to enum.
         *
         * @param enumType the enum type
         */
        public StringToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            if (source == null || source.length() == 0) {
                return null;
            }

            T[] entitys = enumType.getEnumConstants();
            for (T entity : entitys) {
                if (entity.name().equalsIgnoreCase(source)) {
                    return entity;
                }

            }
            return null;
        }
    }
}
