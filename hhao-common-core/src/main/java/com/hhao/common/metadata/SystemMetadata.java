package com.hhao.common.metadata;
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

import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.support.Version;

import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;

public class SystemMetadata {
    private static final Logger logger = LoggerFactory.getLogger(SystemMetadata.class);
    private MetadataProperties metadataProperties;

    private static class SingletonHolder {
        private static final SystemMetadata INSTANCE = new SystemMetadata();
    }

    private SystemMetadata() {
        metadataProperties=new MetadataProperties();
        metadataProperties.getMonetaryConfig().addCurrencyConfig(new CurrencyConfig("CNY", "zh-CN", "SYMBOL:¤#,###0.00", "CODE:¤#,###0.00", 16, 2, RoundingMode.HALF_EVEN, true));
        metadataProperties.getMonetaryConfig().addCurrencyConfig(new CurrencyConfig("USD", "en-US", "SYMBOL:¤#,###0.00", "CODE:¤#,###0.00", 16, 2, RoundingMode.HALF_EVEN, true));
    }

    public static SystemMetadata getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public MetadataProperties getMetadataProperties() {
        return metadataProperties;
    }

    public void setMetadataProperties(MetadataProperties metadataProperties) {
        this.metadataProperties = metadataProperties;
    }

    private ZoneId zoneId=null;
    public ZoneId getZoneId() {
        if (zoneId==null){
            synchronized (this) {
                try {
                    zoneId = ZoneId.of(metadataProperties.getTimezone());
                } catch (Exception e) {
                    logger.warn(e.getMessage());
                    zoneId = ZoneId.systemDefault();
                }
            }
        }
        return zoneId;
    }

    private Version version=null;
    public Version getVersion() {
        if (version==null){
            synchronized (this) {
                version = Version.of(metadataProperties.getVersion());
            }
        }
        return version;
    }

    private Locale locale=null;
    public Locale getLocale() {
        if (locale==null){
            synchronized (this) {
                String value=metadataProperties.getLocale();
                if (value==null){
                    logger.warn("The locale format is language-country, for example, zh-CN");
                    locale=Locale.getDefault();
                }else {
                    String[] values = value.split("-|_");
                    if (values == null || values.length != 2) {
                        logger.debug("The locale format is language-country, for example, zh-CN");
                        locale = Locale.getDefault();
                    }else {
                        locale = Arrays.stream(Locale.getAvailableLocales()).filter(locale -> {
                            if (locale.getLanguage().equals(values[0])) {
                                return true;
                            }
                            return false;
                        }).filter(locale -> {
                            if (locale.getCountry().equals(values[1])) {
                                return true;
                            }
                            return false;
                        }).findFirst().orElse(Locale.getDefault());
                    }
                }
            }
        }
        return locale;
    }

    private DateTimeFormatter dateFormatter = null;
    public DateTimeFormatter getDateFormatter() {
        if (dateFormatter == null) {
            synchronized (this) {
                String value = metadataProperties.getFormatters().getDate();
                if (value == null) {
                    dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                } else {
                    dateFormatter = DateTimeFormatter.ofPattern(value);
                }
            }
        }
        return dateFormatter;
    }

    private DateTimeFormatter dateTimeFormatter=null;
    public DateTimeFormatter getDateTimeFormatter() {
        if (dateTimeFormatter==null){
            synchronized (this) {
                String value = metadataProperties.getFormatters().getDateTime();
                if (value == null) {
                    dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                } else {
                    dateTimeFormatter = DateTimeFormatter.ofPattern(value);
                }
            }
        }
        return dateTimeFormatter;
    }

    private DateTimeFormatter timeFormatter=null;
    public DateTimeFormatter getTimeFormatter() {
        if (timeFormatter==null){
            synchronized (this) {
                String value = metadataProperties.getFormatters().getTime();
                if (value == null) {
                    timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                } else {
                    timeFormatter = DateTimeFormatter.ofPattern(value);
                }
            }
        }
        return timeFormatter;
    }

    private Currency defaultCurrency=null;
    public Currency getDefaultCurrency() {
        if (defaultCurrency==null){
            synchronized (this) {
                String currencyCode=metadataProperties.getMonetaryConfig().getDefaultCurrency();
                if (currencyCode==null){
                    defaultCurrency=Currency.getInstance(Locale.getDefault());
                }else {
                    defaultCurrency=Currency.getInstance(currencyCode);
                }
            }
        }
        return defaultCurrency;
    }


    public CurrencyConfig getCurrencyConfig(String currencyCode) {
        CurrencyConfig currencyConfig= metadataProperties.getMonetaryConfig().getCurrencyConfigurations().get(currencyCode);
        if (currencyConfig==null){
            throw new IllegalArgumentException("CurrencyConfig is not found:" + currencyCode);
        }
        return currencyConfig;
    }

}
