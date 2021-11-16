
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

package com.hhao.common.metadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;

/**
 * 日期时间格式
 *
 * @author Wang
 * @since  1.0.0
 */
public class DateTimeFormatterMetadata implements Metadata<DateTimeFormatter> {
    protected final Logger logger = LoggerFactory.getLogger(DateTimeFormatterMetadata.class);
    private final String DATETIME_PATTERN="yyyy-MM-dd HH:mm:ss";
    private final String NAME="DATE_TIME_FORMATTER";
    private String value=DATETIME_PATTERN;
    private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    @Override
    public boolean support(String name) {
        if (name==null){
            return false;
        }
        if (NAME.equals(name)){
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public DateTimeFormatter getMetadata() {
        return dateTimeFormatter;
    }

    @Override
    public DateTimeFormatter update(String value) {
        try {
            dateTimeFormatter = DateTimeFormatter.ofPattern(value);
            this.value=value;
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return dateTimeFormatter;
    }

}
