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

package com.hhao.common.springboot.format;


import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Instant格式化转换类,根据注解DateTimeFormat进行转换
 *
 * @author Wang
 * @since 1.0.0
 */
public class DateTimeAnnotationFormatterFactory implements AnnotationFormatterFactory<DateTimeFormat> {
    private Set<Class<?>> fieldTypes = new HashSet<Class<?>>();
    private Map<String, Formatter<Instant>> instantFormatterMap = new ConcurrentHashMap<>();
    private Map<String, Formatter<LocalDate>> localDateFormatterMap = new ConcurrentHashMap<>();
    private Map<String, Formatter<LocalTime>> localTimeFormatterMap = new ConcurrentHashMap<>();
    private Map<String, Formatter<LocalDateTime>> localDateTimeFormatterMap = new ConcurrentHashMap<>();
    private Map<String, Formatter<ZonedDateTime>> zonedDateTimeFormatterMap = new ConcurrentHashMap<>();
    private Boolean dataTimeErrorThrow;
    /**
     * Instantiates a new Date time annotation formatter factory.
     */
    public DateTimeAnnotationFormatterFactory(Boolean dataTimeErrorThrow) {
        this.fieldTypes.add(Instant.class);
        this.fieldTypes.add(LocalDate.class);
        this.fieldTypes.add(LocalTime.class);
        this.fieldTypes.add(LocalDateTime.class);
        this.fieldTypes.add(ZonedDateTime.class);
        this.dataTimeErrorThrow=dataTimeErrorThrow;
    }

    @Override
    public Set<Class<?>> getFieldTypes() {
        return fieldTypes;
    }


    private Formatter<Instant> getInstantFormatter(String pattern,String[] fallbackPatterns) {
        Formatter<Instant> formatter = instantFormatterMap.get(pattern);
        if (formatter == null) {
            formatter = new InstantAndLocalDateTimeStrFormatImpl(pattern,dataTimeErrorThrow,fallbackPatterns);
            instantFormatterMap.put(pattern, formatter);
        }
        return formatter;
    }

    private Formatter<LocalDate> getLocalDateFormatter(String pattern,String[] fallbackPatterns) {
        Formatter<LocalDate> formatter = localDateFormatterMap.get(pattern);
        if (formatter == null) {
            formatter = new LocalDateFormatImpl(pattern,dataTimeErrorThrow,fallbackPatterns);
            localDateFormatterMap.put(pattern, formatter);
        }
        return formatter;
    }

    private Formatter<LocalTime> getLocalTimeFormatter(String pattern,String[] fallbackPatterns) {
        Formatter<LocalTime> formatter = localTimeFormatterMap.get(pattern);
        if (formatter == null) {
            formatter = new LocalTimeFormatImpl(pattern,dataTimeErrorThrow,fallbackPatterns);
            localTimeFormatterMap.put(pattern, formatter);
        }
        return formatter;
    }

    private Formatter<LocalDateTime> getLocalDateTimeFormatter(String pattern,String[] fallbackPatterns) {
        Formatter<LocalDateTime> formatter = localDateTimeFormatterMap.get(pattern);
        if (formatter == null) {
            formatter = new LocalDateTimeFormatImpl(pattern,dataTimeErrorThrow,fallbackPatterns);
            localDateTimeFormatterMap.put(pattern, formatter);
        }
        return formatter;
    }

    private Formatter<ZonedDateTime> getZonedDateTimeFormatter(String pattern,String[] fallbackPatterns) {
        Formatter<ZonedDateTime> formatter = zonedDateTimeFormatterMap.get(pattern);
        if (formatter == null) {
            formatter = new ZonedDateTimeFormatImpl(pattern,dataTimeErrorThrow,fallbackPatterns);
            zonedDateTimeFormatterMap.put(pattern, formatter);
        }
        return formatter;
    }

    private Formatter<?> getFormatter(DateTimeFormat annotation, Class<?> fieldType) {
        if (fieldType.equals(Instant.class)){
            return getInstantFormatter(annotation.pattern(),annotation.fallbackPatterns());
        }else if (fieldType.equals(LocalDate.class)){
            return getLocalDateFormatter(annotation.pattern(),annotation.fallbackPatterns());
        }else if (fieldType.equals(LocalTime.class)){
            return getLocalTimeFormatter(annotation.pattern(),annotation.fallbackPatterns());
        }else if (fieldType.equals(LocalDateTime.class)){
            return getLocalDateTimeFormatter(annotation.pattern(),annotation.fallbackPatterns());
        }else if (fieldType.equals(ZonedDateTime.class)){
            return getZonedDateTimeFormatter(annotation.pattern(),annotation.fallbackPatterns());
        }
        return null;
    }

    @Override
    public Printer<?> getPrinter(DateTimeFormat annotation, Class<?> fieldType) {
        return getFormatter(annotation,fieldType);
    }

    @Override
    public Parser<?> getParser(DateTimeFormat annotation, Class<?> fieldType) {
        return getFormatter(annotation,fieldType);
    }
}
