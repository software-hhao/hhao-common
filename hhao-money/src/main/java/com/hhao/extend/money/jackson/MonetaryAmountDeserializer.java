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

package com.hhao.extend.money.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.hhao.common.Context;
import com.hhao.common.metadata.Mdm;
import com.hhao.common.metadata.MonetaryAmountFromStringFormatMetadata;
import com.hhao.extend.money.MoneyFormat;
import com.hhao.extend.money.MoneyUtils;
import org.javamoney.moneta.format.CurrencyStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryRounding;
import javax.money.RoundingQueryBuilder;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字符串转MonetaryAmount
 *
 * @param <T> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public class MonetaryAmountDeserializer<T extends MonetaryAmount> extends JsonDeserializer<T> implements ContextualDeserializer {
    protected final Logger logger = LoggerFactory.getLogger(MonetaryAmountDeserializer.class);
    /**
     * 是否采用@MoneyFormat注解定义的格式，或者是使用元数据MONETARY_AMOUNT_FROM_STRING、MONETARY_ROUNDING定义的格式进行转换;
     */
    private boolean useMoneyFormat=false;
    private MonetaryAmountDeserializerWithMetadata deserializerWithMetadata=new MonetaryAmountDeserializerWithMetadata();
    private Map<String,MonetaryAmountDeserializerWithMoneyFormat> deserializerWithMoneyFormatMap =new ConcurrentHashMap<>();

    public MonetaryAmountDeserializer(){

    }

    public MonetaryAmountDeserializer(boolean useMoneyFormat){
        this.useMoneyFormat=useMoneyFormat;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        if (!useMoneyFormat || property==null){
            return deserializerWithMetadata;
        }
        MoneyFormat moneyFormat=property.getAnnotation(MoneyFormat.class);
        if (moneyFormat==null){
            return deserializerWithMetadata;
        }
        String key=moneyFormat.currencyStyle().name()+moneyFormat.locale()+moneyFormat.pattern()+moneyFormat.scale()+moneyFormat.roundingMode().name();
        MonetaryAmountDeserializerWithMoneyFormat  deserializer=deserializerWithMoneyFormatMap.get(key);
        if (deserializer==null){
            deserializer=new MonetaryAmountDeserializerWithMoneyFormat(moneyFormat);
            deserializerWithMoneyFormatMap.put(key,deserializer);
        }
        return deserializer;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return null;
    }

    /**
     * 使用@MoneyFormat定义的格式转换
     * @param <T>
     */
    public class MonetaryAmountDeserializerWithMoneyFormat<T extends MonetaryAmount> extends JsonDeserializer<T>{
        private MoneyFormat moneyFormat=null;
        private CurrencyStyle currencyStyle;
        private String pattern;
        private MonetaryRounding rounding;

        public MonetaryAmountDeserializerWithMoneyFormat(MoneyFormat format){
            this.moneyFormat = moneyFormat;
            this.currencyStyle = moneyFormat.currencyStyle();
            this.pattern = moneyFormat.pattern();
            //取元数据精度
            this.rounding = Monetary.getRounding(
                    RoundingQueryBuilder.of().setScale(moneyFormat.scale()).set(moneyFormat.roundingMode()).build()
            );
        }

        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (p.getText()==null || p.getText().isBlank()){
                return null;
            }
            String str=p.getText().trim();
            try {
                //locale设置有可能会根据上下文信息取，所以不能缓存
                Locale locale= Context.findLocale(moneyFormat.locale());
                //判断是否是完整的Money字符串，完整的串形如：CNY 23.45,¥ 12.8789478
                if (!MoneyUtils.isCompleteMoneyText(str, locale, currencyStyle)) {
                    if (pattern.startsWith(MonetaryAmountFromStringFormatMetadata.PLACE_SYMBOL)) {
                        str = MoneyUtils.prefixMoneyText(str, locale, currencyStyle);
                    } else if (pattern.endsWith(MonetaryAmountFromStringFormatMetadata.PLACE_SYMBOL)) {
                        str = MoneyUtils.suffixMoneyText(str, locale, currencyStyle);
                    }
                }
                MonetaryAmount money = MoneyUtils.stringToMoney(str, locale, currencyStyle, pattern);
                //返回取精后的值
                return (T)money.with(rounding);
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug(e.getMessage());
            }
            return null;
        }
    }

    /**
     * 使用元数据MONETARY_AMOUNT_FROM_STRING、MONETARY_ROUNDING定义的格式进行转换;
     * @param <T>
     */
    public class MonetaryAmountDeserializerWithMetadata <T extends MonetaryAmount> extends JsonDeserializer<T>{
        Map<String,Object> formatAttrs=null;
        private CurrencyStyle currencyStyle=null;
        private String pattern=null;
        private MonetaryRounding rounding=null;

        public MonetaryAmountDeserializerWithMetadata(){
            //取元数据
            formatAttrs= Mdm.MONETARY_AMOUNT_FROM_STRING.value(Map.class);
            currencyStyle=(CurrencyStyle)formatAttrs.get(MonetaryAmountFromStringFormatMetadata.CURRENCY_STYLE);
            pattern=(String)formatAttrs.get(MonetaryAmountFromStringFormatMetadata.CURRENCY_PATTERN);
            rounding=Mdm.MONETARY_ROUNDING.value(MonetaryRounding.class);
        }

        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (p.getText()==null || p.getText().isBlank()){
                return null;
            }
            try {
                String str=p.getText().trim();
                //locale设置有可能会根据上下文信息取，所以不能缓存
                Locale locale=(Locale) formatAttrs.get(MonetaryAmountFromStringFormatMetadata.LOCALE);

                //判断是否是完整的Money字符串，完整的串形如：CNY 23.45,¥ 12.8789478
                if (!MoneyUtils.isCompleteMoneyText(str,locale,currencyStyle)){
                    if (pattern.startsWith(MonetaryAmountFromStringFormatMetadata.PLACE_SYMBOL)) {
                        str = MoneyUtils.prefixMoneyText(str, locale, currencyStyle);
                    }else if (pattern.endsWith(MonetaryAmountFromStringFormatMetadata.PLACE_SYMBOL)) {
                        str = MoneyUtils.suffixMoneyText(str, locale, currencyStyle);
                    }
                }
                MonetaryAmount money=MoneyUtils.stringToMoney(str,locale,currencyStyle,pattern);
                //返回取精后的值
                return (T)money.with(rounding);
            } catch (IOException e) {
                e.printStackTrace();
                logger.debug(e.getMessage());
            }
            return null;
        }
    }
}
