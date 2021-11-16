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

package com.hhao.common.utils;

import com.hhao.common.metadata.Mdm;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * The type Date time utils.
 *
 * @author Wan
 * @since 1.0.0
 */
public class DateTimeUtils {
    /**
     * Now instant.
     *
     * @return the instant
     */
    public static Instant now() {
        return Instant.now();
    }

    /**
     * Instant to zoned date time zoned date time.
     *
     * @param instant the instant
     * @return the zoned date time
     */
    public static ZonedDateTime instantToZonedDateTime(Instant instant) {
        return instantToZonedDateTime(instant, Mdm.ZONE.value(ZoneId.class));
    }

    /**
     * Instant to zoned date time zoned date time.
     *
     * @param instant the instant
     * @param zoneId  the zone id
     * @return the zoned date time
     */
    public static ZonedDateTime instantToZonedDateTime(Instant instant, ZoneId zoneId) {
        return ZonedDateTime.ofInstant(instant, zoneId);
    }

    /**
     * Instant to local date time local date time.
     *
     * @param instant the instant
     * @return the local date time
     */
    public static LocalDateTime instantToLocalDateTime(Instant instant) {
        return instantToLocalDateTime(instant, Mdm.ZONE.value(ZoneId.class));
    }

    /**
     * Instant to local date time local date time.
     *
     * @param instant the instant
     * @param zoneId  the zone id
     * @return the local date time
     */
    public static LocalDateTime instantToLocalDateTime(Instant instant, ZoneId zoneId) {
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    /**
     * Instant to local date local date.
     *
     * @param instant the instant
     * @return the local date
     */
    public static LocalDate instantToLocalDate(Instant instant) {
        return instantToLocalDate(instant, Mdm.ZONE.value(ZoneId.class));
    }

    /**
     * Instant to local date local date.
     *
     * @param instant the instant
     * @param zoneId  the zone id
     * @return the local date
     */
    public static LocalDate instantToLocalDate(Instant instant, ZoneId zoneId) {
        return LocalDate.ofInstant(instant, zoneId);
    }

    /**
     * Instant to local time local time.
     *
     * @param instant the instant
     * @param zoneId  the zone id
     * @return the local time
     */
    public static LocalTime instantToLocalTime(Instant instant, ZoneId zoneId) {
        return LocalTime.ofInstant(instant, zoneId);
    }

    /**
     * Instant to local time local time.
     *
     * @param instant the instant
     * @return the local time
     */
    public static LocalTime instantToLocalTime(Instant instant) {
        return LocalTime.ofInstant(instant,Mdm.ZONE.value(ZoneId.class));
    }

    /**
     * Zoned date time to instant instant.
     *
     * @param zonedDateTime the zoned date time
     * @return the instant
     */
    public static Instant zonedDateTimeToInstant(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toInstant();
    }

    /**
     * Local date time to instant instant.
     *
     * @param localDateTime the local date time
     * @return the instant
     */
    public static Instant localDateTimeToInstant(LocalDateTime localDateTime) {
        return localDateTimeToInstant(localDateTime, Mdm.ZONE.value(ZoneId.class));
    }

    /**
     * Local date time to instant instant.
     *
     * @param localDateTime the local date time
     * @param zoneId        the zone id
     * @return the instant
     */
    public static Instant localDateTimeToInstant(LocalDateTime localDateTime, ZoneId zoneId) {
        return localDateTime.toInstant(zoneId.getRules().getOffset(localDateTime));
    }

    /**
     * Local date to local date time local date time.
     *
     * @param localDate the local date
     * @return the local date time
     */
    public static LocalDateTime localDateToLocalDateTime(LocalDate localDate) {
        return localDateToLocalDateTime(localDate, 0, 0, 0, 0);
    }

    /**
     * Local date to local date time local date time.
     *
     * @param localDate the local date
     * @param hour      the hour
     * @return the local date time
     */
    public static LocalDateTime localDateToLocalDateTime(LocalDate localDate, int hour) {
        return localDateToLocalDateTime(localDate, hour, 0, 0, 0);
    }

    /**
     * Local date to local date time local date time.
     *
     * @param localDate the local date
     * @param hour      the hour
     * @param minute    the minute
     * @return the local date time
     */
    public static LocalDateTime localDateToLocalDateTime(LocalDate localDate, int hour, int minute) {
        return localDateToLocalDateTime(localDate, hour, minute, 0, 0);
    }

    /**
     * Local date to local date time local date time.
     *
     * @param localDate the local date
     * @param hour      the hour
     * @param minute    the minute
     * @param second    the second
     * @return the local date time
     */
    public static LocalDateTime localDateToLocalDateTime(LocalDate localDate, int hour, int minute, int second) {
        return localDateToLocalDateTime(localDate, hour, minute, second, 0);
    }

    /**
     * Local date to local date time local date time.
     *
     * @param localDate    the local date
     * @param hour         the hour
     * @param minute       the minute
     * @param second       the second
     * @param nanoOfSecond the nano of second
     * @return the local date time
     */
    public static LocalDateTime localDateToLocalDateTime(LocalDate localDate, int hour, int minute, int second, int nanoOfSecond) {
        return localDate.atTime(hour, minute, second, nanoOfSecond);
    }

    /**
     * Local date time to zoned date time zoned date time.
     *
     * @param localDateTime the local date time
     * @param sourceZoneId  the source zone id
     * @param targetZoneId  the target zone id
     * @return the zoned date time
     */
    public static ZonedDateTime localDateTimeToZonedDateTime(LocalDateTime localDateTime, ZoneId sourceZoneId, ZoneId targetZoneId) {
        return ZonedDateTime.ofInstant(localDateTime, sourceZoneId.getRules().getOffset(localDateTime), targetZoneId);
    }

    /**
     * Format string.
     *
     * @param instant   the instant
     * @param formatter the formatter
     * @param zoneId    the zone id
     * @return the string
     */
    public static String format(Instant instant, DateTimeFormatter formatter, ZoneId zoneId) {
        return instantToLocalDateTime(instant, zoneId).format(formatter);
    }

    /**
     * Format string.
     *
     * @param dateTime  the date time
     * @param formatter the formatter
     * @return the string
     */
    public static String format(LocalDateTime dateTime, DateTimeFormatter formatter) {
        return dateTime.format(formatter);
    }

    /**
     * Format string.
     *
     * @param localDate the local date
     * @param formatter the formatter
     * @return the string
     */
    public static String format(LocalDate localDate, DateTimeFormatter formatter) {
        return localDate.format(formatter);
    }

    /**
     * Format string.
     *
     * @param localTime the local time
     * @param formatter the formatter
     * @return the string
     */
    public static String format(LocalTime localTime, DateTimeFormatter formatter) {
        return localTime.format(formatter);
    }

    /**
     * Format string.
     *
     * @param zonedDateTime the zoned date time
     * @param formatter     the formatter
     * @return the string
     */
    public static String format(ZonedDateTime zonedDateTime, DateTimeFormatter formatter) {
        return zonedDateTime.format(formatter);
    }
}
