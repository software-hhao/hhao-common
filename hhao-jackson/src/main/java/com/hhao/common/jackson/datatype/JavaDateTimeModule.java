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
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hhao.common.metadata.Mdm;

import java.io.Serial;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

/**
 * The type Java date time module.
 *
 * @author Wang
 * @since 1.0.0
 */
public class JavaDateTimeModule extends SimpleModule {
    @Serial
    private static final long serialVersionUID = 3796028264604640166L;
    private Function<BeanProperty, JsonFormat.Value> jsonFormatFilterFunction;

    /**
     * Instantiates a new Java date time module.
     *
     * @param jsonFormatFilterFunction the json format filter function
     */
    public JavaDateTimeModule(Function<BeanProperty, JsonFormat.Value> jsonFormatFilterFunction){
        this.jsonFormatFilterFunction=jsonFormatFilterFunction;

        //自定义JDK8的Instant数据类型的序列化与反序列化
        addSerializer(Instant.class, new InstantResolver.InstantSerializer());
        addDeserializer(Instant.class, new InstantResolver.InstantDeserializer());

        //自定义JDK8的LocalDate数据类型的序列化与反序列化
        addSerializer(LocalDate.class, new DateTimeResolver<LocalDate>().getDateTimeSerializer(LocalDate.class, Mdm.DATE_FORMATTER.value(DateTimeFormatter.class), jsonFormatFilterFunction));
        addDeserializer(LocalDate.class, new DateTimeResolver<LocalDate>().getDateTimeDeserializer(LocalDate.class, Mdm.DATE_FORMATTER.value(DateTimeFormatter.class), jsonFormatFilterFunction));
        //自定义JDK8的LocalDateTime数据类型的序列化与反序列化
        addSerializer(LocalDateTime.class, new DateTimeResolver<LocalDateTime>().getDateTimeSerializer(LocalDateTime.class, Mdm.DATE_TIME_FORMATTER.value(DateTimeFormatter.class), jsonFormatFilterFunction));
        addDeserializer(LocalDateTime.class, new DateTimeResolver<LocalDateTime>().getDateTimeDeserializer(LocalDateTime.class, Mdm.DATE_TIME_FORMATTER.value(DateTimeFormatter.class), jsonFormatFilterFunction));
        //自定义JDK8的LocalTime数据类型的序列化与反序列化
        addSerializer(LocalTime.class, new DateTimeResolver<LocalTime>().getDateTimeSerializer(LocalTime.class,Mdm.TIME_FORMATTER.value(DateTimeFormatter.class), jsonFormatFilterFunction));
        addDeserializer(LocalTime.class,new DateTimeResolver<LocalTime>().getDateTimeDeserializer(LocalTime.class, Mdm.TIME_FORMATTER.value(DateTimeFormatter.class), jsonFormatFilterFunction));
        //自定义JDK8的LocalTime数据类型的序列化与反序列化
        addSerializer(ZonedDateTime.class, new DateTimeResolver<ZonedDateTime>().getDateTimeSerializer(ZonedDateTime.class, Mdm.DATE_TIME_FORMATTER.value(DateTimeFormatter.class), jsonFormatFilterFunction));
        addDeserializer(ZonedDateTime.class, new DateTimeResolver<ZonedDateTime>().getDateTimeDeserializer(ZonedDateTime.class, Mdm.DATE_TIME_FORMATTER.value(DateTimeFormatter.class), jsonFormatFilterFunction));
    }

}