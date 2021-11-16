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

package com.hhao.common.jackson.datatype;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Function;

/**
 * 日期时间的序列与反序列化
 *
 * @param <T> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public class DateTimeResolver<T> {

    /**
     * Get date time serializer date time serializer.
     *
     * @param t                        the t
     * @param formatter                the formatter
     * @param jsonFormatFilterFunction the json format filter function
     * @return the date time serializer
     */
    public DateTimeSerializer getDateTimeSerializer(Class<T> t, DateTimeFormatter formatter, Function<BeanProperty, JsonFormat.Value> jsonFormatFilterFunction){
        return new DateTimeSerializer(t,formatter,jsonFormatFilterFunction);
    }

    /**
     * Get date time deserializer date time deserializer.
     *
     * @param t                        the t
     * @param formatter                the formatter
     * @param jsonFormatFilterFunction the json format filter function
     * @return the date time deserializer
     */
    public DateTimeDeserializer getDateTimeDeserializer(Class<T> t, DateTimeFormatter formatter, Function<BeanProperty, JsonFormat.Value> jsonFormatFilterFunction){
        return new DateTimeDeserializer(t,formatter,jsonFormatFilterFunction);
    }

    /**
     * 在日期序列化与返序列化转换中使用，将JsonFormat.Value的值赋给DateTimeFormatter
     *
     * @param jsonFormat:
     * @param dtf:
     * @return java.time.format.DateTimeFormatter
     **/
    private DateTimeFormatter setDateTimeFormatter(JsonFormat.Value jsonFormat, DateTimeFormatter dtf) {
        Objects.requireNonNull(dtf, "DateTimeFormatter require not null");
        if (jsonFormat == null) {
            return dtf;
        }
        if (jsonFormat.hasPattern()) {
            dtf = DateTimeFormatter.ofPattern(jsonFormat.getPattern());
            if (jsonFormat.hasLocale()) {
                dtf = dtf.withLocale(jsonFormat.getLocale());
            }
            if (jsonFormat.hasTimeZone()) {
                dtf = dtf.withZone(jsonFormat.getTimeZone().toZoneId());
            }
        }
        return dtf;
    }

    /**
     * 自定义JDK8日期时间类序列化输出
     */
    public class DateTimeSerializer extends StdSerializer<T> implements ContextualSerializer {
        /**
         * The Date time formatter.
         */
        protected DateTimeFormatter dateTimeFormatter;
        /**
         * The Json format filter function.
         */
        protected Function<BeanProperty, JsonFormat.Value> jsonFormatFilterFunction;
        /**
         * The To string function.
         */
        protected Function<T, String> toStringFunction;

        /**
         * Instantiates a new Date time serializer.
         *
         * @param t                        the t
         * @param formatter                the formatter
         * @param jsonFormatFilterFunction the json format filter function
         */
        protected DateTimeSerializer(Class<T> t, DateTimeFormatter formatter, Function<BeanProperty, JsonFormat.Value> jsonFormatFilterFunction) {
            super(t);
            this.dateTimeFormatter = formatter;
            this.jsonFormatFilterFunction = jsonFormatFilterFunction;
        }


        @Override
        public JsonSerializer<T> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
            JsonFormat.Value format = findFormatOverrides(prov, property, handledType());
            if (format == null && jsonFormatFilterFunction != null) {
                format = jsonFormatFilterFunction.apply(property);
            }
            DateTimeFormatter dtf = setDateTimeFormatter(format, dateTimeFormatter);
            if (dtf != dateTimeFormatter) {
                return new DateTimeSerializer(super.handledType(), dtf, jsonFormatFilterFunction);
            }
            return this;
        }

        @Override
        public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (value != null) {
                if (value instanceof ChronoLocalDateTime){
                    gen.writeObject(((ChronoLocalDateTime<?>) value).format(this.dateTimeFormatter));
                }else if (value instanceof ChronoLocalDate){
                    gen.writeObject(((ChronoLocalDate) value).format(this.dateTimeFormatter));
                }else if (value instanceof LocalTime){
                    gen.writeObject(((LocalTime) value).format(this.dateTimeFormatter));
                }else if (value instanceof ChronoZonedDateTime){
                    gen.writeObject(((ChronoZonedDateTime) value).format(this.dateTimeFormatter));
                }
            }
        }
    }

    /**
     * 自定义JDK8日期时间类序反列化输出
     */
    public class DateTimeDeserializer extends StdDeserializer<T> implements ContextualDeserializer {
        /**
         * The Date time formatter.
         */
        protected DateTimeFormatter dateTimeFormatter;
        /**
         * The Json format filter function.
         */
        protected Function<BeanProperty, JsonFormat.Value> jsonFormatFilterFunction;

        /**
         * Instantiates a new Date time deserializer.
         *
         * @param t                        the t
         * @param formatter                the formatter
         * @param jsonFormatFilterFunction the json format filter function
         */
        protected DateTimeDeserializer(Class<T> t, DateTimeFormatter formatter, Function<BeanProperty, JsonFormat.Value> jsonFormatFilterFunction) {
            super(t);
            this.dateTimeFormatter = formatter;
            this.jsonFormatFilterFunction = jsonFormatFilterFunction;
        }

        @Override
        public JsonDeserializer<T> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
            JsonFormat.Value format = this.findFormatOverrides(ctxt, property, handledType());
            if (format == null && jsonFormatFilterFunction != null) {
                format = jsonFormatFilterFunction.apply(property);
            }
            DateTimeFormatter dtf = setDateTimeFormatter(format, dateTimeFormatter);
            if (dtf != dateTimeFormatter) {
                return new DateTimeDeserializer(property.getType().getTypeHandler(), dtf, this.jsonFormatFilterFunction);
            }
            return this;
        }

        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (p.getCurrentToken().equals(JsonToken.VALUE_STRING) && p.getText() != null && !p.getText().isEmpty()) {
                try {
                    Method method = super.handledType().getMethod("parse", CharSequence.class, DateTimeFormatter.class);
                    return (T) method.invoke(null, p.getText(), this.dateTimeFormatter);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
