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

import com.hhao.common.Context;
import com.hhao.common.metadata.MonetaryAmountFromStringFormatMetadata;
import com.hhao.extend.money.MoneyFormat;
import com.hhao.extend.money.MoneyUtils;
import org.javamoney.moneta.format.CurrencyStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryRounding;
import javax.money.RoundingQueryBuilder;
import java.text.ParseException;
import java.util.Locale;

/**
 * 按照@MoneyFormat设置进行转换
 * 目前只开放了Money转String的转换，String到Money的转换参见MonetaryAmountAndStringConverter
 *
 * @author Wang
 * @since 1.0.0
 */
public class MonetaryAmountFormatImpl implements Formatter<MonetaryAmount> {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(MonetaryAmountAndStringConverter.class);
    private MoneyFormat moneyFormat;
    private CurrencyStyle currencyStyle;
    private Locale locale;
    private String pattern;
    /**
     * 取元数据精度
     */
    private MonetaryRounding rounding;

    /**
     * Instantiates a new Monetary amount format.
     *
     * @param moneyFormat the money format
     */
    public MonetaryAmountFormatImpl(MoneyFormat moneyFormat) {
        this.moneyFormat = moneyFormat;
        this.currencyStyle = moneyFormat.currencyStyle();
        this.pattern = moneyFormat.pattern();
        //取元数据精度
        this.rounding = Monetary.getRounding(
                RoundingQueryBuilder.of().setScale(moneyFormat.scale()).set(moneyFormat.roundingMode()).build()
        );
        this.locale= Context.findLocale(moneyFormat.locale());
    }

    @Override
    public MonetaryAmount parse(String text, Locale l) throws ParseException {
        if (!StringUtils.hasLength(text)) {
            return null;
        }
        //字符串转Money
        String str = ((String) text).trim();
        try {
            //判断是否是完整的Money字符串，完整的串形如：CNY 23.45,¥ 12.8789478
            if (!MoneyUtils.isCompleteMoneyText(str, locale, currencyStyle)) {
                if (pattern.startsWith(MonetaryAmountFromStringFormatMetadata.PLACE_SYMBOL)) {
                    str = MoneyUtils.prefixMoneyText(str, locale, currencyStyle);
                } else if (pattern.endsWith(MonetaryAmountFromStringFormatMetadata.PLACE_SYMBOL)) {
                    str = MoneyUtils.suffixMoneyText(str, locale, currencyStyle);
                }
            }
            MonetaryAmount money = MoneyUtils.stringToMoney(str, locale, currencyStyle, pattern);

            //返回取精后的值
            return money.with(rounding);
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
        }
        return null;
    }

    @Override
    public String print(MonetaryAmount money, Locale l) {
        if (money == null) {
            return "";
        }
        try {
            //先取精度，再转换
            return MoneyUtils.moneyToString(money.with(rounding),locale,currencyStyle,pattern);
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
        }
        return money.toString();
    }
}
