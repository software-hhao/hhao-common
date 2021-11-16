
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
 * 时间格式化
 *
 * @author Wang
 * @since 1.0.0
 */
public class TimeFormatterMetadata implements Metadata<DateTimeFormatter> {
    protected final Logger logger = LoggerFactory.getLogger(TimeFormatterMetadata.class);
    private final String TIME_PATTERN="HH:mm:ss";
    private final String NAME="TIME_FORMATTER";
    private String value=TIME_PATTERN;
    private DateTimeFormatter timeFormatter=DateTimeFormatter.ofPattern(TIME_PATTERN);

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
        return timeFormatter;
    }

    @Override
    public DateTimeFormatter update(String value) {
        try {
            timeFormatter = DateTimeFormatter.ofPattern(value);
            this.value=value;
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return timeFormatter;
    }
}
