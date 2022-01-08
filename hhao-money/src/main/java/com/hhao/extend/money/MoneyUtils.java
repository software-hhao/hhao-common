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

package com.hhao.extend.money;

import org.javamoney.moneta.Money;
import org.javamoney.moneta.format.AmountFormatParams;
import org.javamoney.moneta.format.CurrencyStyle;

import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Money类型的工具类
 *
 * @author Wang
 * @since 1.0.0
 */
public class MoneyUtils {
    public static Money bigDecimalToMoney(BigDecimal value, String currencyCode){
        if (value==null || currencyCode==null){
            return null;
        }
//        MonetaryContext monetaryContext= MonetaryContextBuilder.of(Money.class)
//                .setMaxScale(2)
//                .setFixedScale(true)
//                .setPrecision(16)
//                .build();
        return Money.of(value,currencyCode);
    };

    public static BigDecimal moneyToBigDecimal(Money money){
        if (money==null){
            return null;
        }
        return money.getNumberStripped();
    };

    /**
     * money到字符串的转换
     *
     * @param money         the money
     * @param locale        the locale
     * @param currencyStyle the currency style
     * @param pattern       the pattern
     * @return string
     */
    public static String moneyToString(MonetaryAmount money, Locale locale, CurrencyStyle currencyStyle, String pattern){
        MonetaryAmountFormat format = MonetaryFormats.getAmountFormat(
                AmountFormatQueryBuilder.of(locale)
                        .set(currencyStyle)
                        .set(AmountFormatParams.PATTERN, pattern)
                        .build()
        );
        return format.format(money);
    };

    /**
     * String to money monetary amount.
     *
     * @param text          the text
     * @param locale        the locale
     * @param currencyStyle the currency style
     * @param pattern       the pattern
     * @return the monetary amount
     */
    public static MonetaryAmount stringToMoney(String text, Locale locale, CurrencyStyle currencyStyle, String pattern){
        MonetaryAmountFormat format = MonetaryFormats.getAmountFormat(
                AmountFormatQueryBuilder.of(locale)
                        .set(currencyStyle)
                        .set(AmountFormatParams.PATTERN, pattern)
                        .build()
        );

        return Money.parse(text,format);
    };

    /**
     * 根据CurrencyStyle类型，获取标记
     * 只支持SYMBOL、其余(CODE)两种
     * SYMBOL标记如:¥,$
     * CODE标记如:CNY,USD
     *
     * @param locale        the locale
     * @param currencyStyle the currency style
     * @return string
     */
    public static String getSymbol(Locale locale,CurrencyStyle currencyStyle){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        String symbol="";
        switch (currencyStyle){
            case SYMBOL:
                symbol=symbols.getCurrencySymbol();
                break;
            case NAME:
            case NUMERIC_CODE:
            default:
                symbol=symbols.getCurrency().getCurrencyCode();
        }
        return symbol;
    }

    /**
     * 判断是否是完整的Money字符串
     * 完整的Money字符串必须前面或后面包含CurrencyStyle类型指定的标记
     *
     * @param moneyText     the money text
     * @param locale        the locale
     * @param currencyStyle the currency style
     * @return boolean
     */
    public static boolean isCompleteMoneyText(String moneyText, Locale locale, CurrencyStyle currencyStyle){
        String symbol=getSymbol(locale,currencyStyle);
        moneyText=moneyText.trim();
        if (moneyText.startsWith(symbol) || moneyText.endsWith(symbol)){
            return true;
        }
        return false;
    }

    /**
     * 将不完整的Money字符串加前缀标记补完整
     *
     * @param moneyText     the money text
     * @param locale        the locale
     * @param currencyStyle the currency style
     * @return string
     */
    public static String prefixMoneyText(String moneyText, Locale locale, CurrencyStyle currencyStyle){
        String prefix=getSymbol(locale,currencyStyle);
        moneyText=moneyText.trim();
        if (!moneyText.startsWith(prefix)){
            moneyText=prefix + " " + moneyText;
        }
        return moneyText;
    }

    /**
     * 将不完整的Money字符串加后缀标记补完整
     *
     * @param moneyText     the money text
     * @param locale        the locale
     * @param currencyStyle the currency style
     * @return string
     */
    public static String suffixMoneyText(String moneyText, Locale locale, CurrencyStyle currencyStyle){
        String suffix=getSymbol(locale,currencyStyle);
        moneyText=moneyText.trim();
        if (!moneyText.endsWith(suffix)){
            moneyText=moneyText + " " + suffix;
        }
        return moneyText;
    }
}
