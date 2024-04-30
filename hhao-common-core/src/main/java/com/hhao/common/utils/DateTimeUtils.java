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

import com.hhao.common.Context;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.metadata.SystemMetadata;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * 对于系统时间的处理建议：
 * MySQL数据库采用datetime，无时区概念，存进什么出什么
 * 系统全部采用Context中的时区
 * 这样系统和数据存储都统一成Context时区
 * 如果用户有不同的时区，则显示的时候做时区转换
 *
 * 这里的时区默认采用Context时区
 *
 * The type Date time utils.
 *
 * @author Wan
 * @since 1.0.0
 */
public class DateTimeUtils {
    protected static final Logger logger = LoggerFactory.getLogger(DateTimeUtils.class);
    /**
     * Now instant.
     *
     * @return the instant
     */
    public static Instant nowInstantUTC() {
        return Instant.now();
    }

    public static LocalDateTime nowLocalDateTimeUTC() {
        return LocalDateTime.now(Clock.systemUTC());
    }

    public static LocalDateTime nowLocalDateTimeContext() {
        return LocalDateTime.now(Context.getInstance().getZoneId());
    }

    public static Instant nowInstantContext() {
        return Instant.now(Clock.system(Context.getInstance().getZoneId()));
    }
    /**
     * Instant to zoned date time zoned date time.
     *
     * @param instant the instant
     * @return the zoned date time
     */
    public static ZonedDateTime instantToZonedDateTime(Instant instant) {
        return instantToZonedDateTime(instant, SystemMetadata.getInstance().getZoneId());
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
        return instantToLocalDateTime(instant, Context.getInstance().getZoneId());
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
        return instantToLocalDate(instant,Context.getInstance().getZoneId());
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
        return LocalTime.ofInstant(instant,Context.getInstance().getZoneId());
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
        return localDateTimeToInstant(localDateTime, Context.getInstance().getZoneId());
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

    public static String localDateToString(LocalDate localDate){
        if (localDate==null){
            return "";
        }
        return SystemMetadata.getInstance().getDateFormatter().format(localDate);
    }

    public static String localTimeToString(LocalTime localTime){
        if (localTime==null){
            return "";
        }
        return SystemMetadata.getInstance().getTimeFormatter().format(localTime);
    }

    public static String localDateTimeToString(LocalDateTime localDateTime){
        if (localDateTime==null){
            return "";
        }
        return SystemMetadata.getInstance().getDateTimeFormatter().format(localDateTime);
    }

    public static String instantToLocalDateTimeString(Instant instant,ZoneId zoneId){
        if (instant==null){
            return "";
        }
        return instant.atZone(zoneId).format(SystemMetadata.getInstance().getDateTimeFormatter());
    }

    public static LocalDate localDateFromString(String localDateStr){
        if (!StringUtils.hasText(localDateStr)){
            return null;
        }
        try {
            return LocalDate.parse(localDateStr,SystemMetadata.getInstance().getDateFormatter());
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    public static LocalTime localTimeFromString(String localTimeStr){
        if (!StringUtils.hasText(localTimeStr)){
            return null;
        }
        try {
            return LocalTime.parse(localTimeStr,SystemMetadata.getInstance().getTimeFormatter());
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    public static LocalDateTime localDateTimeFromString(String localDateTimeStr){
        if (!StringUtils.hasText(localDateTimeStr)){
            return null;
        }
        try {
            return LocalDateTime.parse(localDateTimeStr,SystemMetadata.getInstance().getDateTimeFormatter());
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    public static Instant instantFromLocalDateTimeString(String instantStr,ZoneId zoneId){
        if (!StringUtils.hasText(instantStr)){
            return null;
        }
        try {
            return ZonedDateTime.of(LocalDateTime.parse(instantStr, SystemMetadata.getInstance().getDateTimeFormatter()),zoneId).toInstant();
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

}
