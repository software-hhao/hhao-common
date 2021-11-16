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

package com.hhao.common.springboot.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * Integer转换成Enum，根据Enum的ordinal
 *
 * @author Wan
 * @since 1.0.0
 */
public class IntegerToEnumConverterFactory implements ConverterFactory<Integer, Enum<?>> {
    @Override
    public <T extends Enum<?>> Converter<Integer, T> getConverter(Class<T> targetType) {
        return new IntegerToEnum<>(targetType);
    }

    private static class IntegerToEnum<T extends Enum<?>> implements Converter<Integer, T> {
        private Class<T> enumType = null;

        /**
         * Instantiates a new Integer to enum.
         *
         * @param enumType the enum type
         */
        public IntegerToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(Integer source) {
            if (source == null || source < 0) {
                return null;
            }

            T[] entitys = enumType.getEnumConstants();
            for (T entity : entitys) {
                if (entity.ordinal() == source.intValue()) {
                    return entity;
                }

            }
            return null;
        }
    }
}
