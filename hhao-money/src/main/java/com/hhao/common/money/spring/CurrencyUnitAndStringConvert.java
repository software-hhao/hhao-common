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

package com.hhao.common.money.spring;

import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.util.StringUtils;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.HashSet;
import java.util.Set;

/**
 * CurrencyUnit与String的转换
 *
 * @author Wang
 * @since 1.0.0
 */
public class CurrencyUnitAndStringConvert  implements ConditionalGenericConverter {
    protected final Logger logger = LoggerFactory.getLogger(CurrencyUnitAndStringConvert.class);

    @Override
    public boolean matches(TypeDescriptor sourceType, @NotNull TypeDescriptor targetType) {
        if ((String.class.isAssignableFrom(sourceType.getType()) && CurrencyUnit.class.isAssignableFrom(targetType.getType()))
        || (String.class.isAssignableFrom(targetType.getType()) && CurrencyUnit.class.isAssignableFrom(sourceType.getType()))){
            return true;
        }

        return false;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        Set<ConvertiblePair> pairs = new HashSet<ConvertiblePair>();
        //注册可支持源类型为String，目标类型为CurrencyUnit的转换
        pairs.add(new ConvertiblePair(String.class, CurrencyUnit.class));
        //注册可支持源类型为CurrencyUnit，目标类型为String的转换
        pairs.add(new ConvertiblePair(CurrencyUnit.class, String.class));
        return pairs;
    }

    @Override
    public Object convert(Object source, @NotNull TypeDescriptor sourceType, @NotNull TypeDescriptor targetType) {
        if (source==null){
            return null;
        }
        if (sourceType.getType().equals(String.class) && targetType.getType().equals(CurrencyUnit.class)) {
            //字符串转CurrencyUnit
            String str = ((String) source).trim();
            if (StringUtils.hasLength(str)) {
                try {
                    CurrencyUnit currencyUnit = Monetary.getCurrency(str);
                    return currencyUnit;
                }catch (Exception e){
                    logger.debug(e.getMessage());
                }
            }
        } else {
            CurrencyUnit currencyUnit=(CurrencyUnit)source;
            return currencyUnit.getCurrencyCode();
        }
        return null;
    }

}