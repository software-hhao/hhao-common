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
import com.hhao.common.springboot.AppContext;
import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/***
 * Instant格式化,根据注解DateTimeFormat进行转换
 *
 * @author Wang
 * @since 1.0.0
 */
public class InstantAndLocalDateTimeStrFormatImpl implements Formatter<Instant> {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(InstantAndLocalDateTimeStrFormatImpl.class);
    private Boolean dataTimeErrorThrow=false;
    private String pattern;
    private String[] fallbackPatterns;
    private DateTimeFormatter dateTimeFormatter;
    private List<DateTimeFormatter> fallbackDateTimeFormatters= Collections.emptyList();

    /**
     * Instantiates a new Instant format.
     *
     * @param pattern            the pattern
     * @param dataTimeErrorThrow the data time error throw
     * @param fallbackPatterns   the fallback patterns
     */
    public InstantAndLocalDateTimeStrFormatImpl(String pattern,Boolean dataTimeErrorThrow, String[] fallbackPatterns) {
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
    public Instant parse(String text, Locale locale) throws ParseException {
        Throwable throwable=null;
        if (StringUtils.hasLength(text)) {
            try {
                return ZonedDateTime.of(LocalDateTime.parse(text, dateTimeFormatter), AppContext.getInstance().getZoneId()).toInstant();
            }catch (Exception e){
            }
            for(DateTimeFormatter df:fallbackDateTimeFormatters){
                try {
                    return ZonedDateTime.of(LocalDateTime.parse(text, df), AppContext.getInstance().getZoneId()).toInstant();
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
    public String print(Instant object, Locale locale) {
        if (object==null){
            return "";
        }
        //根据全局的ZoneId转换
        return object.atZone(AppContext.getInstance().getZoneId()).format(dateTimeFormatter);
    }
}
