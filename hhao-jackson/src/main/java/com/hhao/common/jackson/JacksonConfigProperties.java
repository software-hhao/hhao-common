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
 * The type Jackson config properties.
 *
 * @author Wan
 * @since 2024 /4/9 9:30
 */
public class JacksonConfigProperties {
    private boolean throwExceptionOnDatetimeConversionError = true;
    private MoneyJsonSerializationConfig moneyJsonSerializationConfig = new MoneyJsonSerializationConfig();

    /**
     * Gets throw exception on datetime conversion error.
     *
     * @return the throw exception on datetime conversion error
     */
    public boolean getThrowExceptionOnDatetimeConversionError() {
        return throwExceptionOnDatetimeConversionError;
    }

    /**
     * Sets throw exception on datetime conversion error.
     *
     * @param throwExceptionOnDatetimeConversionError the throw exception on datetime conversion error
     */
    public void setThrowExceptionOnDatetimeConversionError(boolean throwExceptionOnDatetimeConversionError) {
        this.throwExceptionOnDatetimeConversionError = throwExceptionOnDatetimeConversionError;
    }

    /**
     * Gets money json serialization config.
     *
     * @return the money json serialization config
     */
    public MoneyJsonSerializationConfig getMoneyJsonSerializationConfig() {
        return moneyJsonSerializationConfig;
    }

    /**
     * Sets money json serialization config.
     *
     * @param moneyJsonSerializationConfig the money json serialization config
     */
    public void setMoneyJsonSerializationConfig(MoneyJsonSerializationConfig moneyJsonSerializationConfig) {
        this.moneyJsonSerializationConfig = moneyJsonSerializationConfig;
    }
}
