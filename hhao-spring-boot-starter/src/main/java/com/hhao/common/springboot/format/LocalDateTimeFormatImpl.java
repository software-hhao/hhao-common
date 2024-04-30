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

package com.hhao.common.springboot.format;

import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * The type Local date time format.
 *
 * @author Wang
 * @since 1.0.0
 */
public class LocalDateTimeFormatImpl implements Formatter<LocalDateTime> {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(LocalDateTimeFormatImpl.class);
    private Boolean dataTimeErrorThrow=false;
    private String pattern;
    private String[] fallbackPatterns;
    private DateTimeFormatter dateTimeFormatter;
    private List<DateTimeFormatter> fallbackDateTimeFormatters= Collections.emptyList();


    /**
     * Instantiates a new Local date time format.
     *
     * @param pattern            the pattern
     * @param dataTimeErrorThrow the data time error throw
     * @param fallbackPatterns   the fallback patterns
     */
    public LocalDateTimeFormatImpl(String pattern,Boolean dataTimeErrorThrow,String[] fallbackPatterns) {
        this.pattern = pattern;
        this.fallbackPatterns=fallbackPatterns;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        this.dataTimeErrorThrow=dataTimeErrorThrow;
        for(String p:fallbackPatterns) {
            this.fallbackDateTimeFormatters=new ArrayList<>();
            fallbackDateTimeFormatters.add(DateTimeFormatter.ofPattern(p));
        }
    }

    @Override
    public LocalDateTime parse(String text, Locale locale) throws ParseException {
        Throwable throwable=null;
        if (StringUtils.hasLength(text)) {
            try {
                return LocalDateTime.parse(text, dateTimeFormatter);
            }catch (Exception e){
            }
            for(DateTimeFormatter df:fallbackDateTimeFormatters){
                try {
                    return LocalDateTime.parse(text, df);
                } catch (Exception e) {
                }
            }
            if (throwable!=null){
                logger.debug(throwable.getMessage());
                if(dataTimeErrorThrow){
                    throw new RuntimeException(throwable);
                }
            }
        }
        return null;
    }

    @Override
    public String print(LocalDateTime object, Locale locale) {
        if (object!=null){
            return object.format(dateTimeFormatter);
        }
        return null;
    }
}
