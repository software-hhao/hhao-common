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

import com.hhao.common.Context;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.metadata.CurrencyConfig;
import com.hhao.common.metadata.SystemMetadata;
import com.hhao.common.money.MoneyFormat;
import com.hhao.common.utils.MoneyUtils;
import org.javamoney.moneta.format.CurrencyStyle;
import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import javax.money.*;
import java.text.ParseException;
import java.util.Currency;
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
        String amount = ((String) text).trim();
        Currency currency=Currency.getInstance(locale);
        MonetaryAmount money = MoneyUtils.stringToMoney(amount, pattern,currency);
        //返回取精后的值
        return money.with(rounding);
    }

    @Override
    public String print(MonetaryAmount money, Locale l) {
        if (money == null) {
            return "";
        }
        CurrencyUnit currencyUnit = money.getCurrency();
        CurrencyConfig currencyConfig = SystemMetadata.getInstance().getCurrencyConfig(currencyUnit.getCurrencyCode());

        return MoneyUtils.moneyToString(money.with(rounding),currencyConfig.getCurrencyLocale(),currencyStyle,pattern);
    }
}
