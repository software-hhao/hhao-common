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

package com.hhao.extend.money.spring;

import com.hhao.common.metadata.Mdm;
import com.hhao.common.metadata.MonetaryAmountFromStringFormatMetadata;
import com.hhao.extend.money.MoneyUtils;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.format.CurrencyStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.util.StringUtils;

import javax.money.MonetaryAmount;
import javax.money.MonetaryRounding;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
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
//        if ((String.class.isAssignableFrom(sourceType.getType()) && MonetaryAmount.class.isAssignableFrom(targetType.getType()))
//        || (String.class.isAssignableFrom(targetType.getType()) && MonetaryAmount.class.isAssignableFrom(sourceType.getType()))){
//            return true;
//        }
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
            String str = ((String) source).trim();
            if (StringUtils.hasLength(str)) {
                try {
                    //取元数据
                    Map<String,Object> formatAttrs=Mdm.MONETARY_AMOUNT_FROM_STRING.value(Map.class);
                    CurrencyStyle currencyStyle=(CurrencyStyle)formatAttrs.get(MonetaryAmountFromStringFormatMetadata.CURRENCY_STYLE);
                    String pattern=(String)formatAttrs.get(MonetaryAmountFromStringFormatMetadata.CURRENCY_PATTERN);
                    Locale locale=(Locale) formatAttrs.get(MonetaryAmountFromStringFormatMetadata.LOCALE);
                    //取元数据精度
                    MonetaryRounding rounding=Mdm.MONETARY_ROUNDING.value(MonetaryRounding.class);

                    //判断是否是完整的Money字符串，完整的串形如：CNY 23.45,¥ 12.8789478
                    if (!MoneyUtils.isCompleteMoneyText(str,locale,CurrencyStyle.CODE)){
                        if (pattern.startsWith(MonetaryAmountFromStringFormatMetadata.PLACE_SYMBOL)) {
                            str = MoneyUtils.prefixMoneyText(str, locale, CurrencyStyle.CODE);
                        }else if (pattern.endsWith(MonetaryAmountFromStringFormatMetadata.PLACE_SYMBOL)) {
                            str = MoneyUtils.suffixMoneyText(str, locale, CurrencyStyle.CODE);
                        }
                    }
                    MonetaryAmount money=MoneyUtils.stringToMoney(str,locale,CurrencyStyle.CODE,pattern);

                    //返回取精后的值
                    return money.with(rounding);
                }catch (Exception e){
                    logger.debug(e.getMessage());
                    throw e;
                }
            }
        } else {
            MonetaryAmount money=(MonetaryAmount)source;
            try {
                //取元数据
                Map<String,Object> formatAttrs=Mdm.MONETARY_AMOUNT_TO_STRING.value(Map.class);
                CurrencyStyle currencyStyle=(CurrencyStyle)formatAttrs.get(MonetaryAmountFromStringFormatMetadata.CURRENCY_STYLE);
                String pattern=(String)formatAttrs.get(MonetaryAmountFromStringFormatMetadata.CURRENCY_PATTERN);
                Locale locale= (Locale) formatAttrs.get(MonetaryAmountFromStringFormatMetadata.LOCALE);
                //取元数据精度
                MonetaryRounding rounding=Mdm.MONETARY_ROUNDING.value(MonetaryRounding.class);
                //先取精度，再转换
                return MoneyUtils.moneyToString(money.with(rounding),locale,currencyStyle,pattern);
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw e;
            }
        }

        return null;
    }

}
