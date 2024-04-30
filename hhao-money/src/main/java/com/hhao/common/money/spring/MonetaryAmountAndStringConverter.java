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
import com.hhao.common.metadata.CurrencyConfig;
import com.hhao.common.metadata.SystemMetadata;
import com.hhao.common.utils.MoneyUtils;
import org.javamoney.moneta.Money;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.util.StringUtils;

import javax.money.MonetaryAmount;
import java.util.HashSet;
import java.util.Set;

/**
 * Spring到Money的转换按元数据设置的MONETARY_AMOUNT_FROM_STRING、MONETARY_ROUNDING转换;
 * Money到String的转换按元数据设置的MONETARY_AMOUNT_TO_STRING、MONETARY_ROUNDING转换;
 * 目前只开放了Spring到Money的转换，Money到String的转换走formatter,可以参照MonetaryAmountFormatImpl的实现
 *
 * @author Wang
 * @since 1.0.0
 */
public class MonetaryAmountAndStringConverter implements ConditionalGenericConverter {
    protected final Logger logger = LoggerFactory.getLogger(MonetaryAmountAndStringConverter.class);

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (String.class.isAssignableFrom(sourceType.getType()) && MonetaryAmount.class.isAssignableFrom(targetType.getType())){
            return true;
        }
        return false;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        Set<ConvertiblePair> pairs = new HashSet<ConvertiblePair>();
        //注册可支持源类型为String，目标类型为Money的转换
        pairs.add(new ConvertiblePair(String.class, Money.class));
        //注册可支持源类型为Money，目标类型为String的转换
        //pairs.add(new ConvertiblePair(Money.class, String.class));
        return pairs;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source==null){
            return null;
        }
        if (sourceType.getType().equals(String.class) && targetType.getType().equals(Money.class)) {
            //字符串转Money
            String amount = ((String) source).trim();
            if (StringUtils.hasLength(amount)) {
                return MoneyUtils.stringToMoney(amount);
            }
        } else {
            //Money转字符串
            MonetaryAmount money=(MonetaryAmount)source;
            //取元数据
            CurrencyConfig currencyConfig = SystemMetadata.getInstance().getCurrencyConfig(money.getCurrency().getCurrencyCode());
            return MoneyUtils.moneyToString(money,currencyConfig.getCurrencyLocale(),currencyConfig.getCurrencyStyleForDisplay(),currencyConfig.getPatternForDisplay());
        }
        return null;
    }

}
