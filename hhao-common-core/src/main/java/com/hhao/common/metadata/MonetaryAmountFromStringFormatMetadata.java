
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

import com.hhao.common.Context;
import org.javamoney.moneta.format.CurrencyStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 将字符串解析为Money时的格式
 * 格式：Locale:CurrencyStyle:PATTERN,如:[default|system|context|语言-国家]:[SYMBOL,CODE]:¤####,####,####,###0.########
 * Locale:
 * default:采用元数据设置的locale
 * context:采用用户上下文的locale
 * zh-CN:指定locale
 * 注意：PATTERN设置的精度一定要大于Money取精的精度，否则会发生四舍五入
 *
 * @author Wang
 * @since  1.0.0
 */
public class MonetaryAmountFromStringFormatMetadata implements Metadata<Map<String,Object>>{
    /**
     * The constant CURRENCY_STYLE.
     */
    public static final String CURRENCY_STYLE="CURRENCY_STYLE";
    /**
     * The constant CURRENCY_PATTERN.
     */
    public static final String CURRENCY_PATTERN="CURRENCY_PATTERN";
    /**
     * The constant LOCALE.
     */
    public static final String LOCALE="LOCALE";
    /**
     * The constant PLACE_SYMBOL.
     */
    public static final String PLACE_SYMBOL="¤";

    private final Logger logger = LoggerFactory.getLogger(MonetaryAmountToStringFormatMetadata.class);
    private String name = "MONETARY_AMOUNT_FROM_STRING";
    private String value="default:CODE:¤ ####,####,####,###0.########";
    private boolean checkPattern=true;
    private Map<String,Object> formatAttributes=new ConcurrentHashMap<>(2);

    /**
     * Instantiates a new Monetary amount from string format metadata.
     */
    public MonetaryAmountFromStringFormatMetadata(){
        this.update(value);
    }

    /**
     * Instantiates a new Monetary amount from string format metadata.
     *
     * @param name         the name
     * @param formatValue  the format value
     * @param checkPattern the check pattern
     */
    public MonetaryAmountFromStringFormatMetadata(String name, String formatValue,boolean checkPattern){
        this.name=name;
        this.value=formatValue;
        this.checkPattern=checkPattern;
        this.update(value);
    }

    @Override
    public boolean support(String name) {
        if (name == null) {
            return false;
        }
        if (this.name.equals(name)) {
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public Map<String,Object> getMetadata() {
        return formatAttributes;
    }

    /**
     * @param value:格式 CurrencyStyle:PATTERN,如:[SYMBOL,CODE]:¤####,####,####,###0.0000
     * @return
     */
    @Override
    public Map<String,Object>  update(String value) {
        if (value==null){
            logger.debug("value format is CurrencyStyle:PATTERN,example:zh-CN:SYMBOL:¤####,###0.00");
            return formatAttributes;
        }
        String [] values=value.split(":");
        if (values==null || values.length!=3){
            logger.debug("value format is CurrencyStyle:PATTERN,example:zh-CN:SYMBOL:¤####,###0.00");
            return formatAttributes;
        }
        formatAttributes.put(LOCALE, Context.findLocale(values[0]));

        formatAttributes.put(CURRENCY_STYLE,this.findCurrencyStyle(values[1]));

        //如果字符串没有加占位符，则采用前加方式
        String pattern=values[2].trim();
        if (checkPattern && !pattern.startsWith(PLACE_SYMBOL) && !pattern.endsWith(PLACE_SYMBOL)){
            pattern=PLACE_SYMBOL + " " + pattern;
        }
        formatAttributes.put(CURRENCY_PATTERN,pattern);
        this.value=value;
        return formatAttributes;
    }

    /**
     * Find currency style currency style.
     *
     * @param value the value
     * @return the currency style
     */
    protected CurrencyStyle findCurrencyStyle(String value){
        if (CurrencyStyle.SYMBOL.name().equals(value)){
            return CurrencyStyle.SYMBOL;
        }else{
            return CurrencyStyle.CODE;
        }
    }
}


