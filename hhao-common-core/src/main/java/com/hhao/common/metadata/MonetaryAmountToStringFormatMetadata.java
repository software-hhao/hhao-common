
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

import org.javamoney.moneta.format.CurrencyStyle;

/**
 * Money转字符串
 * 格式 Locale:CurrencyStyle:PATTERN,如:[default|system|context|语言-国家]:[SYMBOL,CODE]:¤####,####,####,###0.########
 *
 * @author Wang
 * @since 1.0.0
 */
public class MonetaryAmountToStringFormatMetadata extends MonetaryAmountFromStringFormatMetadata {
    private static final String NAME = "MONETARY_AMOUNT_TO_STRING";
    private static String value="default:SYMBOL:¤####,####,####,###0.00######";

    /**
     * Instantiates a new Monetary amount to string format metadata.
     */
    public MonetaryAmountToStringFormatMetadata(){
        super(NAME,value,false);
    }

    @Override
    protected CurrencyStyle findCurrencyStyle(String value){
        if (CurrencyStyle.SYMBOL.name().equals(value)){
            return CurrencyStyle.SYMBOL;
        }else if (CurrencyStyle.CODE.name().equals(value)){
            return CurrencyStyle.CODE;
        }else if (CurrencyStyle.NAME.name().equals(value)){
            return CurrencyStyle.NAME;
        }else if (CurrencyStyle.NUMERIC_CODE.name().equals(value)){
            return CurrencyStyle.NUMERIC_CODE;
        }
        return CurrencyStyle.SYMBOL;
    }
}


