
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

package com.hhao.common.springboot.extend.mapstruct;

import com.hhao.common.springboot.AppContext;
import com.hhao.common.utils.DateTimeUtils;
import com.hhao.extend.money.MoneyUtils;
import org.javamoney.moneta.Money;

import java.math.BigDecimal;
import java.time.*;

/**
 * MapStruct的基类
 * 定义各种非常规的类型转换
 *
 * @author Wang
 * @since 2021 /12/14 8:42
 */
public interface MapperStructBase {
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
     * @param localDateTime the local date time
     * @return the string
     */
    default String localTimeToString(LocalTime localDateTime){
        return DateTimeUtils.localTimeToString(localDateTime);
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
     * @param zoneId  the zone id
     * @return the string
     */
    default String instantToLocalDateTimeString(Instant instant, ZoneId zoneId){
        return DateTimeUtils.instantToLocalDateTimeString(instant,zoneId);
    }

    /**
     * String to local date local date.
     *
     * @param localDateStr the local date str
     * @return the local date
     */
    default LocalDate stringToLocalDate(String localDateStr){
        return DateTimeUtils.localDateFromString(localDateStr);
    }

    /**
     * String to local time local time.
     *
     * @param localTimeStr the local time str
     * @return the local time
     */
    default LocalTime stringToLocalTime(String localTimeStr){
        return DateTimeUtils.localTimeFromString(localTimeStr);
    }

    /**
     * String to local date time local date time.
     *
     * @param localDateTimeStr the local date time str
     * @return the local date time
     */
    default LocalDateTime stringToLocalDateTime(String localDateTimeStr){
        return DateTimeUtils.localDateTimeFromString(localDateTimeStr);
    }

    /**
     * Local date time string to instant instant.
     *
     * @param instantStr the instant str
     * @param zoneId     the zone id
     * @return the instant
     */
    default Instant localDateTimeStringToInstant(String instantStr,ZoneId zoneId){
        return DateTimeUtils.instantFromLocalDateTimeString(instantStr,zoneId);
    }

    /**
     * Instant to local date time local date time.
     *
     * @param instant the instant
     * @param zoneId  the zone id
     * @return the local date time
     */
    default LocalDateTime instantToLocalDateTime(Instant instant, ZoneId zoneId){
        return DateTimeUtils.instantToLocalDateTime(instant, zoneId);
    }

    /**
     * Instant to local date time local date time.
     *
     * @param instant the instant
     * @return the local date time
     */
    default LocalDateTime instantToLocalDateTime(Instant instant){
        return DateTimeUtils.instantToLocalDateTime(instant, AppContext.getInstance().getZoneId());
    }

    /**
     * Local date time to instant instant.
     *
     * @param localDateTime the local date time
     * @param zoneId        the zone id
     * @return the instant
     */
    default Instant localDateTimeToInstant(LocalDateTime localDateTime, ZoneId zoneId){
        return DateTimeUtils.localDateTimeToInstant(localDateTime, zoneId);
    }

    /**
     * Local date time to instant instant.
     *
     * @param localDateTime the local date time
     * @return the instant
     */
    default Instant localDateTimeToInstant(LocalDateTime localDateTime){
        return DateTimeUtils.localDateTimeToInstant(localDateTime, AppContext.getInstance().getZoneId());
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
        return MoneyUtils.moneyToBigDecimal(money);
    }

    /**
     * Money get currency string.
     *
     * @param money the money
     * @return the string
     */
    default String moneyGetCurrency(Money money){
        if (money==null){
            return "";
        }
        return money.getCurrency().getCurrencyCode();
    }
}
