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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The type Local time format.
 *
 * @author Wang
 * @since 1.0.0
 */
public class LocalTimeFormatImpl implements Formatter<LocalTime> {
    protected final Logger logger = LoggerFactory.getLogger(LocalTimeFormatImpl.class);
    private Boolean dataTimeErrorThrow=false;
    private String pattern;
    private String[] fallbackPatterns;
    private DateTimeFormatter dateTimeFormatter;
    private List<DateTimeFormatter> fallbackDateTimeFormatters;

    /**
     * Instantiates a new Local time format.
     */
    public LocalTimeFormatImpl(String pattern,Boolean dataTimeErrorThrow,String[] fallbackPatterns) {
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
    public LocalTime parse(String text, Locale locale) throws ParseException {
        Throwable throwable=null;
        if (StringUtils.hasLength(text)) {
            try {
                return LocalTime.parse(text, dateTimeFormatter);
            }catch (Exception e){
            }
            for(DateTimeFormatter df:fallbackDateTimeFormatters){
                try {
                    return LocalTime.parse(text, df);
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
    public String print(LocalTime object, Locale locale) {
        if (object!=null){
            return object.format(dateTimeFormatter);
        }
        return null;
    }
}
