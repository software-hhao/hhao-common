/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hhao.common.jackson;

import com.hhao.common.money.jackson.MoneyJsonSerializationConfig;

/**
 * @author Wan
 * @since 2024/4/9 9:30
 */
public class JacksonConfigProperties {
    private boolean throwExceptionOnDatetimeConversionError = true;
    private MoneyJsonSerializationConfig moneyJsonSerializationConfig = new MoneyJsonSerializationConfig();

    public boolean getThrowExceptionOnDatetimeConversionError() {
        return throwExceptionOnDatetimeConversionError;
    }

    public void setThrowExceptionOnDatetimeConversionError(boolean throwExceptionOnDatetimeConversionError) {
        this.throwExceptionOnDatetimeConversionError = throwExceptionOnDatetimeConversionError;
    }

    public MoneyJsonSerializationConfig getMoneyJsonSerializationConfig() {
        return moneyJsonSerializationConfig;
    }

    public void setMoneyJsonSerializationConfig(MoneyJsonSerializationConfig moneyJsonSerializationConfig) {
        this.moneyJsonSerializationConfig = moneyJsonSerializationConfig;
    }
}
