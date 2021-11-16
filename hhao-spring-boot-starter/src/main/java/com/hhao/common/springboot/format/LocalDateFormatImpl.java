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

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The type Local date format.
 *
 * @author Wang
 * @since 1.0.0
 */
public class LocalDateFormatImpl implements Formatter<LocalDate> {
    private String pattern;
    private String[] fallbackPatterns;
    private DateTimeFormatter dateTimeFormatter;
    private List<DateTimeFormatter> fallbackDateTimeFormatters;

    /**
     * Instantiates a new Local date format.
     *
     * @param pattern          the pattern
     * @param fallbackPatterns the fallback patterns
     */
    public LocalDateFormatImpl(String pattern,String[] fallbackPatterns) {
        this.pattern = pattern;
        this.fallbackPatterns=fallbackPatterns;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        for(String p:fallbackPatterns) {
            this.fallbackDateTimeFormatters=new ArrayList<>();
            fallbackDateTimeFormatters.add(DateTimeFormatter.ofPattern(p));
        }
    }

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        if (StringUtils.hasLength(text)) {
            try {
                return LocalDate.parse(text, dateTimeFormatter);
            }catch (Exception e){
            }
            for(DateTimeFormatter df:fallbackDateTimeFormatters){
                try {
                    return LocalDate.parse(text, df);
                } catch (Exception e) {
                }
            }
        }
        return null;
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        if (object!=null){
            return object.format(dateTimeFormatter);
        }
        return null;
    }
}
