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

package com.hhao.common.metadata;

/**
 * The enum Mdm.
 *
 * @author Wang
 * @since 1.0.0
 */
public enum Mdm {

    /**
     * The Date time formatter.
     */
    DATE_TIME_FORMATTER(new DateTimeFormatterMetadata()),
    /**
     * The Date formatter.
     */
    DATE_FORMATTER(new DateFormatterMetadata()),
    /**
     * The Time formatter.
     */
    TIME_FORMATTER(new TimeFormatterMetadata()),
    /**
     * The Locale.
     */
    LOCALE(new LocaleMetadata()),
    /**
     * The Zone.
     */
    ZONE(new ZoneMetadata()),
    /**
     * The Monetary amount to string.
     */
    MONETARY_AMOUNT_TO_STRING(new MonetaryAmountToStringFormatMetadata()),
    /**
     * The Monetary amount from string.
     */
    MONETARY_AMOUNT_FROM_STRING(new MonetaryAmountFromStringFormatMetadata()),
    /**
     * The Monetary rounding.
     */
    MONETARY_ROUNDING(new MonetaryRoundingMetadata()),
    /**
     * The Currency unit.
     */
    CURRENCY_UNIT(new CurrencyUnitMetadata()),

    /**
     * The Version.
     */
    VERSION(new VersionMetadata());

    private Metadata metadata;


    private Mdm(Metadata metadata){
        this.metadata=metadata;
    }

    /**
     * Metadata metadata.
     *
     * @return the metadata
     */
    public Metadata metadata(){
        return this.metadata;
    }

    /**
     * Value t.
     *
     * @param <T>    the type parameter
     * @param tClass the t class
     * @return the t
     */
    public <T> T value(Class<T> tClass) {
        return (T)metadata.getMetadata();
    }
}
