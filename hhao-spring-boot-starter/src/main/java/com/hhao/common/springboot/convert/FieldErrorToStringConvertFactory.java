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

import com.hhao.common.springboot.exception.support.FieldErrorHelper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.validation.FieldError;

/**
 * FieldError和String的转换处理
 *
 * @author Wang
 * @since 1.0.0
 */
public class FieldErrorToStringConvertFactory implements ConverterFactory<FieldError, String> {
    @Override
    public <T extends String> Converter<FieldError, T> getConverter(Class<T> targetType) {
        return new FieldErrorToString();
    }

    private static class FieldErrorToString<T extends String> implements Converter<FieldError, T> {
        @Override
        public T convert(FieldError fieldError) {
            StringBuffer buf=new StringBuffer();
            if (fieldError == null) {
                return null;
            }
            buf.append("{");
            buf.append("\"field\":");
            buf.append("\"" + fieldError.getField()+"\",");
            buf.append("\"rejectedValue\":");
            buf.append("\"" + fieldError.getRejectedValue()+"\",");
            buf.append("\"message\":");
            buf.append("\"" + FieldErrorHelper.getMessage(fieldError)+"\"");
            buf.append("\"code\":");
            buf.append("\"" + fieldError.getCode()+"\"");
            buf.append("}");
            return (T) buf.toString();
        }
    }
}
