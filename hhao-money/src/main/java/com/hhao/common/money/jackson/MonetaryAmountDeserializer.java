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

package com.hhao.common.money.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.hhao.common.Context;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.metadata.CurrencyConfig;
import com.hhao.common.metadata.SystemMetadata;
import com.hhao.common.money.MoneyFormat;
import com.hhao.common.utils.MoneyUtils;
import org.javamoney.moneta.format.CurrencyStyle;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryRounding;
import javax.money.RoundingQueryBuilder;
import java.io.IOException;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字符串转MonetaryAmount
 * 输入格式两种
 * 一种是Object形式（推荐）:{"amount":12.3,"currency":"CNY","formatted":"CNY12.3"}
 * 一种是字符串形式:CNY20.3或20.3CNY，具体的由pattern确定，但是只支持CurrencyStyle.CODE的形式
 * 另一种字符串形式支持根据当前的locale自动补齐Currency;即可以只输入数字（不推荐，因为locale可能会变，对国际化不友好）
 * 格式化的pattern由元数据或@MoneyFormat提供
 *
 * @param <T> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public class MonetaryAmountDeserializer<T extends MonetaryAmount> extends JsonDeserializer<T> implements ContextualDeserializer {
    protected final Logger logger = LoggerFactory.getLogger(MonetaryAmountDeserializer.class);

    private MoneyJsonSerializationConfig moneyJsonSerializationConfig =null;
    private MonetaryAmountDeserializerWithMetadata deserializerWithMetadata;
    private Map<String,MonetaryAmountDeserializerWithMoneyFormat> deserializerWithMoneyFormatMap =new ConcurrentHashMap<>();

    public MonetaryAmountDeserializer(){

    }

    public MonetaryAmountDeserializer(MoneyJsonSerializationConfig moneyJsonSerializationConfig){
        this.moneyJsonSerializationConfig = moneyJsonSerializationConfig;
        deserializerWithMetadata=new MonetaryAmountDeserializerWithMetadata();
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        if (!moneyJsonSerializationConfig.getPreferMoneyFormatAnnotationForDeserialization() || property==null){
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

    protected MonetaryAmount exDeserialize(JsonParser p,Locale locale,String pattern) throws IOException {
        if (p.getText()==null || p.getText().isBlank()){
            return null;
        }
        String str=p.getText().trim();
        String amount=null;
        String currency=null;

        if (p.currentToken().equals(JsonToken.START_OBJECT)){
            //如果输入是个Object,解析其中的amount和currency
            do{
                p.nextToken();
                if (p.currentToken().equals(JsonToken.FIELD_NAME)){
                    if(p.getText().equals(moneyJsonSerializationConfig.getMoneyAmountFieldName())){
                        //while(p.currentToken()!=null && !p.currentToken().equals(JsonToken.VALUE_NUMBER_FLOAT) && !p.currentToken().equals(JsonToken.VALUE_NUMBER_INT)){
                        //    p.nextToken();
                        //}
                        while(p.currentToken()!=null && !p.currentToken().equals(JsonToken.VALUE_STRING)){
                            p.nextToken();
                        }
                        amount=p.getText().trim();
                    }else if(p.getText().equals(moneyJsonSerializationConfig.getCurrencyCodeFieldName())){
                        p.nextToken();
                        currency=p.getText().trim();
                    }
                }
            }while(!p.currentToken().equals(JsonToken.END_OBJECT) && p.currentToken()!=null);

            if (amount!=null && currency!=null){
                // 如果不是MoneyFormat定义，则需要从元数据中获取pattern
                if (pattern == null || pattern.isBlank()){
                    CurrencyConfig currencyConfig = SystemMetadata.getInstance().getCurrencyConfig(currency);
                    pattern = currencyConfig.getPatternForParse();
                }
                String patternPlaceSymbol = SystemMetadata.getInstance().getMetadataProperties().getMonetaryConfig().getPatternPlaceSymbol();
                if (pattern.startsWith(patternPlaceSymbol)){
                    // 去除前置货币占位符
                    pattern = pattern.substring(patternPlaceSymbol.length()).trim();
                }else if (pattern.endsWith(patternPlaceSymbol)){
                    // 去除后置货币占位符
                    pattern = pattern.substring(0, pattern.length() - patternPlaceSymbol.length()).trim();
                }
                // amount不带货币符号,pattern移除货币占位符
                return MoneyUtils.stringToMoney(amount,currency,pattern);
            }else{
                if (moneyJsonSerializationConfig.getThrowExceptionOnConversionError()){
                    throw new JsonMappingException(p, "Money format error,the amount and currency must be in the object");
                }
                return null;
            }
        }else{
            // 不是Object,直接解析
            amount=str.trim();
            if (locale!=null){
                // 如果MoneyFormat有定义，走MoneyFormat中的定义
                currency=Currency.getInstance(locale).getCurrencyCode();
                if (pattern == null || pattern.isBlank()){
                    CurrencyConfig currencyConfig = SystemMetadata.getInstance().getCurrencyConfig(currency);
                    pattern = currencyConfig.getPatternForParse();
                }
                return MoneyUtils.stringToMoney(amount,pattern,Currency.getInstance(currency));
            }else{
                // 直接转换
                return MoneyUtils.stringToMoney(amount);
            }
        }
    }

    /**
     * 使用@MoneyFormat定义的格式转换
     */
    public class MonetaryAmountDeserializerWithMoneyFormat<T extends MonetaryAmount> extends JsonDeserializer<T>{
        private MoneyFormat moneyFormat=null;
        private CurrencyStyle currencyStyle;
        private String pattern;
        private MonetaryRounding rounding;

        public MonetaryAmountDeserializerWithMoneyFormat(MoneyFormat moneyFormat){
            this.moneyFormat = moneyFormat;
            this.currencyStyle = moneyFormat.currencyStyle();
            this.pattern = moneyFormat.pattern();
            //取元数据精度
            this.rounding = Monetary.getRounding(
                RoundingQueryBuilder.of().setScale(moneyFormat.scale()).set(moneyFormat.roundingMode()).build()
            );
        }

        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return (T)MonetaryAmountDeserializer.this.exDeserialize(p,Context.findLocale(moneyFormat.locale()),pattern);
        }
    }

    /**
     * 使用元数据MONETARY_AMOUNT_FROM_STRING、MONETARY_ROUNDING定义的格式进行转换;
     */
    public class MonetaryAmountDeserializerWithMetadata <T extends MonetaryAmount> extends JsonDeserializer<T>{
        public MonetaryAmountDeserializerWithMetadata(){

        }

        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return (T)MonetaryAmountDeserializer.this.exDeserialize(p,null,null);
        }
    }
}
