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

package com.hhao.common.ddd.convert;

import com.hhao.common.Context;
import com.hhao.common.utils.DateTimeUtils;
import com.hhao.common.utils.MoneyUtils;
import org.javamoney.moneta.Money;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumSet;

/**
 * 用于mapstruct做转换时的基类
 * The interface Convert mapper.
 *
 * @author Wang
 * @since 1.0.0
 */
public interface ConvertMapper {
    /**
     * Local date to string string.
     *
     * @param localDate the local date
     * @return the string
     */
    default String localDateToString(LocalDate localDate){
        return DateTimeUtils.localDateToString(localDate);
    }

    /**
     * Local time to string string.
     *
     * @param localTime the local time
     * @return the string
     */
    default String localTimeToString(LocalTime localTime){
        return DateTimeUtils.localTimeToString(localTime);
    }

    /**
     * Local date time to string string.
     *
     * @param localDateTime the local date time
     * @return the string
     */
    default String localDateTimeToString(LocalDateTime localDateTime){
        return DateTimeUtils.localDateTimeToString(localDateTime);
    }

    /**
     * Instant to local date time string string.
     *
     * @param instant the instant
     * @return the string
     */
    default String instantToLocalDateTimeString(Instant instant){
        return DateTimeUtils.instantToLocalDateTimeString(instant, Context.getInstance().getZoneId());
    }

    /**
     * Local date from string local date.
     *
     * @param localDateStr the local date str
     * @return the local date
     */
    default LocalDate localDateFromString(String localDateStr){
        return DateTimeUtils.localDateFromString(localDateStr);
    }

    /**
     * Local time from string local time.
     *
     * @param localTimeStr the local time str
     * @return the local time
     */
    default LocalTime localTimeFromString(String localTimeStr){
        return DateTimeUtils.localTimeFromString(localTimeStr);
    }

    /**
     * Local date time from string local date time.
     *
     * @param localDateTimeStr the local date time str
     * @return the local date time
     */
    default LocalDateTime localDateTimeFromString(String localDateTimeStr){
        return DateTimeUtils.localDateTimeFromString(localDateTimeStr);
    }

    /**
     * Big decimal to money money.
     *
     * @param value        the value
     * @param currencyCode the currency code
     * @return the money
     */
    default Money bigDecimalToMoney(BigDecimal value, String currencyCode){
        return MoneyUtils.bigDecimalToMoney(value,currencyCode);
    }

    /**
     * Money to big decimal big decimal.
     *
     * @param money the money
     * @return the big decimal
     */
    default BigDecimal moneyToBigDecimal(Money money){
        return money.getNumberStripped();
    }

    /**
     * Money to currency string.
     *
     * @param money the money
     * @return the string
     */
    default String moneyToCurrency(Money money){
        return money.getCurrency().getCurrencyCode();
    }

    /**
     * Enum to ordinal integer.
     *
     * @param num the num
     * @return the integer
     */
    default Integer enumToOrdinal(Enum num){
        return num.ordinal();
    }

    /**
     * Ordinal to enum e.
     *
     * @param <E>     the type parameter
     * @param ordinal the ordinal
     * @param enumSet the enum set
     * @return the e
     */
    default <E extends Enum<E>> E ordinalToEnum(Integer ordinal,EnumSet<E> enumSet){
        for(E obj:enumSet){
            if (obj.ordinal()==ordinal){
                return obj;
            }
        }
        return null;
    }
}
