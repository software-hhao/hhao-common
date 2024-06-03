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

package com.hhao.common.utils;

import com.hhao.common.exception.error.request.ConvertException;
import com.hhao.common.metadata.CurrencyConfig;
import com.hhao.common.metadata.SystemMetadata;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.format.AmountFormatParams;
import org.javamoney.moneta.format.CurrencyStyle;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryContext;
import javax.money.MonetaryContextBuilder;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Money类型的工具类
 *
 * @author Wang
 * @since 1.0.0
 */
public class MoneyUtils {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^-?(\\d+(\\.\\d*)?|\\.\\d+)$");
    private static final Pattern SCIENTIFIC_NOTATION_PATTERN = Pattern.compile("^-?\\d+(\\.\\d+)?[eE][-+]?\\d+$");

    /**
     * New money money.
     *
     * @param amount       the amount
     * @param currencyCode the currency code
     * @param mathContext  the math context
     * @return the money
     */
    public static Money newMoney(BigDecimal amount, String currencyCode, MathContext mathContext) {
        if (amount == null) {
            return null;
        }
        return bigDecimalToMoney(amount, currencyCode, mathContext);
    }

    /**
     * New money money.
     *
     * @param amount       the amount
     * @param currencyCode the currency code
     * @return the money
     */
    public static Money newMoney(BigDecimal amount, String currencyCode) {
        return newMoney(amount, currencyCode, null);
    }

    /**
     * 通过数值字符串生成Money,不任何裁剪
     *
     * @param amount       the amount
     * @param currencyCode the currency code
     * @param mathContext  the math context
     * @return money
     */
    public static Money newMoney(String amount, String currencyCode, MathContext mathContext) {
        if (amount == null || amount.isBlank()) {
            return null;
        }
        return bigDecimalToMoney(new BigDecimal(amount), currencyCode, mathContext);
    }

    /**
     * New money money.
     *
     * @param amount       the amount
     * @param currencyCode the currency code
     * @return the money
     */
    public static Money newMoney(String amount, String currencyCode) {
        return newMoney(amount, currencyCode, null);
    }

    /**
     * Big decimal to money money.
     *
     * @param number       the number
     * @param currencyCode the currency code
     * @param mathContext  the math context
     * @return the money
     */
    public static Money bigDecimalToMoney(BigDecimal number, String currencyCode, MathContext mathContext) {
        if (currencyCode == null) {
            throw new IllegalArgumentException("currencyCode is null");
        }
        if (number == null) {
            return null;
        }
        if (mathContext == null) {
            mathContext = SystemMetadata.getInstance().getCurrencyConfig(currencyCode).getMathContext();
        }
        //MathContext.DECIMAL128
        return Monetary.getAmountFactory(Money.class)
                .setCurrency(currencyCode).setNumber(number)
                .setContext(MonetaryContextBuilder.of().set(mathContext).build())
                .create();
    }

    /**
     * Big decimal to money money.
     *
     * @param number       the number
     * @param currencyCode the currency code
     * @return the money
     */
    public static Money bigDecimalToMoney(BigDecimal number, String currencyCode) {
        return bigDecimalToMoney(number, currencyCode, null);
    }

    /**
     * Money to big decimal big decimal.
     *
     * @param money the money
     * @return the big decimal
     */
    public static BigDecimal moneyToBigDecimal(Money money) {
        if (money == null) {
            return null;
        }
        //BigDecimal dec = money.getNumber().numberValue(BigDecimal.class);
        return money.getNumberStripped();
    }

    /**
     * money到字符串的转换
     * pattern会影响输出的精度与样式，pattern的写法参考：https://docs.oracle.com/javase/8/docs/api/java/text/DecimalFormat.html
     *
     * @param amount        the money
     * @param locale        the locale
     * @param currencyStyle the currency style
     * @param pattern       the pattern
     * @return string string
     */
    public static String moneyToString(MonetaryAmount amount,Locale locale,CurrencyStyle currencyStyle, String pattern) {
        MonetaryAmountFormat format = MonetaryFormats.getAmountFormat(
                AmountFormatQueryBuilder.of(locale)
                        .set(currencyStyle)
                        .set(AmountFormatParams.PATTERN, pattern)
                        .build()
        );
        return format.format(amount);
    }

    /**
     * 方法一、
     * MonetaryAmountFormat format = ToStringMonetaryAmountFormat.of(ToStringMonetaryAmountFormat.ToStringMonetaryAmountFormatStyle.MONEY);
     * return Money.parse(text,format);
     * 这个方法没有问题，但是没有设置locale，所以不支持locale等
     * <p>
     * 方法二、
     * MonetaryAmountFormat format = MonetaryFormats.getAmountFormat(
     * AmountFormatQueryBuilder.of(locale)
     * .set(currencyStyle)
     * .set(AmountFormatParams.PATTERN, pattern)
     * .setMonetaryAmountFactory(Monetary.getDefaultAmountFactory().setContext(monetaryContext))
     * .build()
     * );
     * return Money.parse(text,format);
     * 这个有点问题，如果没有启用decimalFormat.setParseBigDecimal(true);会出现精度问题
     * 如果要解决这个问题，只能自己重写相关的转换函数
     */

    /**
     * 将字符串转换为MonetaryAmount
     * 从amount读取货币占位符，如果存在，则从amount中分离出货币占位符和货币值，否则使用系统默认的货币
     *
     * @param amount the amount
     * @return monetary amount
     */
    public static MonetaryAmount stringToMoney(String amount)  {
        // 根据amount分离出货币占位符,货币值
        String currencyCodeOrSymbol =  "";
        String amountWithoutCurrency = "";
        String patternWithoutPlaceSymbol = "";
        amount = amount.trim();

        // 尝试从amount中取出货币占位符
        currencyCodeOrSymbol =  extractFromFront(amount);
        if (!currencyCodeOrSymbol.isBlank()){
            amountWithoutCurrency = amount.substring(currencyCodeOrSymbol.length()).trim();
        }else{
            currencyCodeOrSymbol=extractFromEnd(amount);
            if (!currencyCodeOrSymbol.isBlank()) {
                amountWithoutCurrency = amount.substring(0, amount.length() - currencyCodeOrSymbol.length()).trim();
            }
        }
        // 说明没有取到货币占位符，使用系统默认的货币
        if (currencyCodeOrSymbol.isBlank()){
            currencyCodeOrSymbol=SystemMetadata.getInstance().getMetadataProperties().getMonetaryConfig().getDefaultCurrency();
        }
        if (amountWithoutCurrency.isBlank()){
            amountWithoutCurrency=amount;
        }
        CurrencyConfig currencyConfig = SystemMetadata.getInstance().getCurrencyConfig(currencyCodeOrSymbol);
        String pattern = currencyConfig.getPatternForParse();
        String patternPlaceSymbol = SystemMetadata.getInstance().getMetadataProperties().getMonetaryConfig().getPatternPlaceSymbol();
        if (pattern.startsWith(patternPlaceSymbol)) {
            // 前置货币占位符
            patternWithoutPlaceSymbol = pattern.substring(patternPlaceSymbol.length()).trim();
        } else if (pattern.endsWith(patternPlaceSymbol)) {
            // 后置货币占位符
            patternWithoutPlaceSymbol = pattern.substring(0, pattern.length() - patternPlaceSymbol.length()).trim();
        } else{
            patternWithoutPlaceSymbol = pattern;
        }
        return stringToMoney(amountWithoutCurrency, currencyCodeOrSymbol, patternWithoutPlaceSymbol);
    }

    /**
     * 将字符串转换为MonetaryAmount
     *
     * @param amount          : 可以带有货币符号,也可以不带货币符号，不带的话用系统默认货币
     * @param pattern         : 货币格式，货币格式如果不带货币占位符，则认为amount没有货币符号，使用系统默认的货币
     * @param defaultCurrency the default currency
     * @return monetary amount
     * @throws ParseException
     */
    public static MonetaryAmount stringToMoney(String amount, String pattern,Currency defaultCurrency)  {
        // 根据amount分离出货币占位符,货币值
        String currencyCodeOrSymbol =  "";
        String amountWithoutCurrency = "";
        String patternWithoutPlaceSymbol = "";
        pattern = pattern.trim();
        amount = amount.trim();

        String patternPlaceSymbol = SystemMetadata.getInstance().getMetadataProperties().getMonetaryConfig().getPatternPlaceSymbol();
        if (pattern.startsWith(patternPlaceSymbol)) {
            // 前置货币占位符
            patternWithoutPlaceSymbol = pattern.substring(patternPlaceSymbol.length()).trim();
            // 根据amount分离出货币占位符,货币值
            currencyCodeOrSymbol =  extractFromFront(amount);
            if (!currencyCodeOrSymbol.isBlank()){
                amountWithoutCurrency = amount.substring(currencyCodeOrSymbol.length()).trim();
            }else{
                currencyCodeOrSymbol=defaultCurrency.getCurrencyCode();
                amountWithoutCurrency = amount;
            }
        } else if (pattern.endsWith(patternPlaceSymbol)) {
            // 后置货币占位符
            patternWithoutPlaceSymbol = pattern.substring(0, pattern.length() - patternPlaceSymbol.length()).trim();
            currencyCodeOrSymbol=extractFromEnd(amount);
            if (!currencyCodeOrSymbol.isBlank()) {
                amountWithoutCurrency = amount.substring(0, amount.length() - currencyCodeOrSymbol.length()).trim();
            }else{
                amountWithoutCurrency = amount;
                currencyCodeOrSymbol=defaultCurrency.getCurrencyCode();
            }
        } else{
            // 说明没有货币符号
            amountWithoutCurrency=amount;
            // 用默认的CurrencyCode
            currencyCodeOrSymbol=defaultCurrency.getCurrencyCode();
        }
        // 验证货币符号是否合法
        CurrencyConfig currencyConfig = SystemMetadata.getInstance().getCurrencyConfig(defaultCurrency.getCurrencyCode());
        if (currencyConfig.getCurrencyCode().equals(currencyCodeOrSymbol) || defaultCurrency.getSymbol(currencyConfig.getCurrencyLocale()).equals(currencyCodeOrSymbol)){
            return stringToMoney(amountWithoutCurrency, currencyCodeOrSymbol, patternWithoutPlaceSymbol);
        }
        throw new ConvertException("The currency symbol is not valid");
    }

    /**
     * 将字符串转换为MonetaryAmount
     *
     * @param amountWithoutCurrency     : 货币值，不带货币符号
     * @param currencyCodeOrSymbol      : 货币符号
     * @param patternWithoutPlaceSymbol : 货币格式,不带货币占位符
     * @return monetary amount
     */
    public static MonetaryAmount stringToMoney(String amountWithoutCurrency, String currencyCodeOrSymbol, String patternWithoutPlaceSymbol)  {
        amountWithoutCurrency=amountWithoutCurrency.trim();
        // 验证金额是否合法
        if (amountWithoutCurrency.isBlank() || !isValidNumber(amountWithoutCurrency)) {
            throw new ConvertException("The amount is not a valid number");
        }
        // 根据货币符号取货币元数据
        currencyCodeOrSymbol=currencyCodeOrSymbol.trim();
        CurrencyConfig currencyConfig = SystemMetadata.getInstance().getCurrencyConfig(currencyCodeOrSymbol);
        // 验证金额是否合法
        if (!isValidNumber(amountWithoutCurrency)) {
            throw new ConvertException("The amount is not a valid number");
        }
        MonetaryContext monetaryContext = currencyConfig.getMonetaryContext();
        MathContext mathContext = currencyConfig.getMathContext();
        NumberFormat numberFormat = NumberFormat.getNumberInstance(currencyConfig.getCurrencyLocale());
        if (numberFormat instanceof DecimalFormat) {
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            // 设置小数点后最多两位
            decimalFormat.setMaximumFractionDigits(monetaryContext.getMaxScale());
            // 设置小数点后最少两位
            decimalFormat.setMinimumFractionDigits(monetaryContext.getMaxScale());
            // 设置整数部分最大位数
            decimalFormat.setMaximumIntegerDigits(monetaryContext.getPrecision() - monetaryContext.getMaxScale());
            // 设置整数部分最少位数
            decimalFormat.setMinimumIntegerDigits(1);
            // 设置货币类型
            decimalFormat.setCurrency(Currency.getInstance(currencyConfig.getCurrencyCode()));
            // 设置四舍五入模式为向下取整
            decimalFormat.setRoundingMode(mathContext.getRoundingMode());
            // 如果设置了模式，以模式为准，前面设置的小数点位数，整数位数不生效
            // 模式的小数部份会严格按位数匹配，整数部份如果超出则不影响
            // 只影响格式化输出
            decimalFormat.applyPattern(patternWithoutPlaceSymbol);
            decimalFormat.setParseBigDecimal(true);
            try {
                BigDecimal money = (BigDecimal) decimalFormat.parse(amountWithoutCurrency);
                return bigDecimalToMoney(money, currencyConfig.getCurrencyCode(), mathContext);
            }catch (ParseException e){
                throw new ConvertException("The amount is not a valid number",e);
            }
        }
        throw new ConvertException("Not support");
    }

    /**
     * 判断是否是合法的货币符号或代码
     *
     * @param currencyCodeOrSymbol the currency code or symbol
     * @param currency             the currency
     * @param currencyStyle        the currency style
     * @return boolean
     */
    public static boolean isValidCurrencyOrSymbol(String currencyCodeOrSymbol, Currency currency, CurrencyStyle currencyStyle) {
        switch (currencyStyle) {
            case SYMBOL:
                // 根据locale获取货币符号并与输入比较
                return currency.getSymbol().equals(currencyCodeOrSymbol);
            case CODE:
                // 根据locale获取货币代码并与输入比较
                return currency.getCurrencyCode().equals(currencyCodeOrSymbol);
            default:
                throw new IllegalArgumentException("Unsupported currency style: " + currencyStyle);
        }
    }

    /**
     * Is valid currency or symbol boolean.
     *
     * @param currencyCodeOrSymbol the currency code or symbol
     * @param currency             the currency
     * @return the boolean
     */
    public static boolean isValidCurrencyOrSymbol(String currencyCodeOrSymbol, Currency currency) {
        if (isValidCurrencyOrSymbol(currencyCodeOrSymbol, currency, CurrencyStyle.SYMBOL) || isValidCurrencyOrSymbol(currencyCodeOrSymbol, currency, CurrencyStyle.CODE)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是合法的数字
     *
     * @param str the str
     * @return boolean
     */
    public static boolean isValidNumber(String str) {
        Matcher matcher = NUMBER_PATTERN.matcher(str);
        if (!matcher.matches()) {
            matcher = SCIENTIFIC_NOTATION_PATTERN.matcher(str);
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据CurrencyStyle类型，获取标记
     * 只支持SYMBOL、其余(CODE)两种
     * SYMBOL标记如:¥,$
     * CODE标记如:CNY,USD
     *
     * @param locale        the locale
     * @param currencyStyle the currency style
     * @return string currency
     */
    public static String getCurrency(Locale locale, CurrencyStyle currencyStyle) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        String currency = "";
        switch (currencyStyle) {
            case SYMBOL:
                currency = symbols.getCurrencySymbol();
                break;
            case NAME:
            case NUMERIC_CODE:
            default:
                currency = symbols.getCurrency().getCurrencyCode();
        }
        return currency;
    }

    /**
     * 判断是否是完整的Money字符串
     * 完整的Money字符串必须前面或后面包含CurrencyStyle类型指定的标记
     *
     * @param amount        the money text
     * @param locale        the locale
     * @param currencyStyle the currency style
     * @return boolean boolean
     */
    public static boolean startOrEndWithCurrency(String amount, Locale locale, CurrencyStyle currencyStyle) {
        if (amount == null || amount.trim().isEmpty()) {
            return false;
        }
        try {
            String symbol = getCurrency(locale, currencyStyle);
            amount = amount.trim();
            if (amount.startsWith(symbol) || amount.endsWith(symbol)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Start or end with currency boolean.
     *
     * @param moneyText the money text
     * @param locale    the locale
     * @return the boolean
     */
    public static boolean startOrEndWithCurrency(String moneyText, Locale locale) {
        return startOrEndWithCurrency(moneyText, locale, CurrencyStyle.SYMBOL) ||
                startOrEndWithCurrency(moneyText, locale, CurrencyStyle.CODE);
    }

    /**
     * 将不完整的Money字符串加前缀标记补完整
     *
     * @param amount        the money text
     * @param locale        the locale
     * @param currencyStyle the currency style
     * @return string string
     */
    public static String formatPrefixCurrencyWithSymbol(String amount, Locale locale, CurrencyStyle currencyStyle) {
        // 输入验证
        if (amount == null || amount.trim().isEmpty()) {
            throw new IllegalArgumentException("moneyText cannot be null or empty");
        }
        if (locale == null) {
            throw new IllegalArgumentException("Locale cannot be null");
        }
        if (currencyStyle == null) {
            throw new IllegalArgumentException("CurrencyStyle cannot be null");
        }
        String prefix = getCurrency(locale, currencyStyle);
        // 确保获取的前缀非空
        if (prefix == null || prefix.isEmpty()) {
            throw new IllegalArgumentException("Invalid locale or currencyStyle combination, unable to retrieve symbol");
        }
        amount = amount.trim();
        if (!amount.startsWith(prefix)) {
            amount = prefix + " " + amount;
        }
        return amount;
    }

    /**
     * 将不完整的Money字符串加后缀标记补完整
     *
     * @param amount        the money text
     * @param locale        the locale
     * @param currencyStyle the currency style
     * @return string string
     */
    public static String formatSuffixCurrencyWithSymbol(String amount, Locale locale, CurrencyStyle currencyStyle) {
        // 输入验证
        if (amount == null || amount.trim().isEmpty()) {
            throw new IllegalArgumentException("moneyText cannot be null or empty");
        }
        if (locale == null) {
            throw new IllegalArgumentException("Locale cannot be null");
        }
        if (currencyStyle == null) {
            throw new IllegalArgumentException("CurrencyStyle cannot be null");
        }
        String suffix = getCurrency(locale, currencyStyle);
        // 确保获取的前缀非空
        if (suffix == null || suffix.isEmpty()) {
            throw new IllegalArgumentException("Invalid locale or currencyStyle combination, unable to retrieve symbol");
        }
        amount = amount.trim();
        if (!amount.endsWith(suffix)) {
            amount = amount + " " + suffix;
        }
        return amount;
    }


    /**
     * 从金额字符串中提取货币符号。
     * 货币符号在数值的前后方，且为非数字字符。
     *
     * @param amount 金额字符串，例如 "$-23.4", "23.4USD", "23.4 USD", "-23.4 USD"
     * @return 货币符号 ，如果未找到则返回空字符串
     */
    public static String extractCurrencySymbol(String amount) {
        // 先尝试从字符串前面提取货币符号
        String symbol = extractFromFront(amount);

        // 如果前面没有找到，再尝试从字符串后面提取货币符号
        if (symbol.isEmpty()) {
            symbol = extractFromEnd(amount);
        }

        return symbol;
    }

    /**
     * Extract from front string.
     *
     * @param amount the amount
     * @return the string
     */
    public static String extractFromFront(String amount) {
        // 使用正则表达式匹配字符串开头的非数字、非正负号字符
        Pattern pattern = Pattern.compile("^[^\\d.\\-+]+");
        Matcher matcher = pattern.matcher(amount);

        if (matcher.find()) {
            return matcher.group(0);
        } else {
            return "";
        }
    }

    /**
     * Extract from end string.
     *
     * @param amount the amount
     * @return the string
     */
    public static String extractFromEnd(String amount) {
        // 使用正则表达式匹配字符串末尾的非数字、非正负号字符
        Pattern pattern = Pattern.compile("[^\\d.\\-+]+$");
        Matcher matcher = pattern.matcher(amount);

        if (matcher.find()) {
            return matcher.group(0);
        } else {
            return "";
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws ParseException the parse exception
     */
    public static void main(String[] args) throws ParseException {
        String str = "-.123";
        //BigDecimal value=(BigDecimal)decimalFormat.parse(str);

        /*
        MonetaryContext monetaryContext = MonetaryContextBuilder.of(Money.class)
                .setMaxScale(10000)
                .setFixedScale(true)
                .setPrecision(2)
                .build();

        MathContext mathContext = new MathContext(10000, RoundingMode.HALF_EVEN);
        //Money money=newMoney(str,"CNY",mathContext);
        Money money = (Money) stringToMoney(str, Locale.CHINA, "¤ ###############0.##",  ((MonetaryContextMetadata) Mdm.MONETARY_CONTEXT.metadata()).getMonetaryContext(), ((MonetaryContextMetadata) Mdm.MONETARY_CONTEXT.metadata()).getMathContext());
        System.out.println("字符串:" + str + "转换后的值:" + money.getNumberStripped());
        //System.out.println("格式化输出:"+ decimalFormat.format(value));

        String[] testCases = {"12 3", "-4a56", "7.89", ".123", "-.123", "123.", "-123."};
        for (String testCase : testCases) {
            System.out.println(testCase + ": " + NUMBER_PATTERN.matcher(testCase).matches());
        }
         */
        String amount1 = "$-23.4";
        String amount2 = "23.4USD";
        String amount3 = "USD+23.4";
        String amount4 = "USD.4";
        String amount5 = ".4   ¥   ";
        String amount6 = ".7";
        System.out.println(extractCurrencySymbol(amount1)); // 输出: $
        System.out.println(extractCurrencySymbol(amount2));
        System.out.println(extractCurrencySymbol(amount3));
        System.out.println(extractCurrencySymbol(amount4));
        System.out.println(extractCurrencySymbol(amount5));
        System.out.println(extractCurrencySymbol(amount6));
    }
}

