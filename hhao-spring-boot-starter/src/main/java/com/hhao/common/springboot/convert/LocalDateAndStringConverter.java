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

package com.hhao.common.springboot.convert;

import com.hhao.common.metadata.Mdm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Local date and string converter.
 *
 * @author Wang
 * @since 1.0.0
 */
public class LocalDateAndStringConverter implements ConditionalGenericConverter {
    protected final Logger logger = LoggerFactory.getLogger(LocalDateAndStringConverter.class);
    private Boolean dataTimeErrorThrow=false;

    public LocalDateAndStringConverter(Boolean dataTimeErrorThrow){
        this.dataTimeErrorThrow=dataTimeErrorThrow;
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (sourceType.getType().equals(String.class) && targetType.getType().equals(LocalDate.class)) {
            return true;
        } else if (sourceType.getType().equals(LocalDate.class) && targetType.getType().equals(String.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        Set<ConvertiblePair> pairs = new HashSet<ConvertiblePair>();
        //注册可支持源类型为String，目标类型为LocalDate的转换
        pairs.add(new ConvertiblePair(String.class, LocalDate.class));
        //注册可支持源类型为LocalDate，目标类型为String的转换
        pairs.add(new ConvertiblePair(LocalDate.class, String.class));
        return pairs;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source==null){
            return null;
        }
        //String<->LocalDate的转换
        if (sourceType.getType().equals(String.class) && targetType.getType().equals(LocalDate.class)) {
            String str = (String) source;
            if (StringUtils.hasLength(str)) {
                try {
                    return LocalDate.parse(str, Mdm.DATE_FORMATTER.value(DateTimeFormatter.class));
                }catch (Exception e){
                    if (dataTimeErrorThrow){
                        throw new RuntimeException(e);
                    }
                    logger.debug(e.getMessage());
                }
            }
        } else {
            LocalDate date = (LocalDate) source;
            return date.format(Mdm.DATE_FORMATTER.value(DateTimeFormatter.class));
        }

        return null;
    }
}
