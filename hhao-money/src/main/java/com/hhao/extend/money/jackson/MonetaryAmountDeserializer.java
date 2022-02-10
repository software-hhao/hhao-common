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
import com.fasterxml.jackson.core.JsonToken;
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

import javax.money.*;
import java.io.IOException;
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

    private MoneyProperties moneyProperties =null;
    private MonetaryAmountDeserializerWithMetadata deserializerWithMetadata;
    private Map<String,MonetaryAmountDeserializerWithMoneyFormat> deserializerWithMoneyFormatMap =new ConcurrentHashMap<>();

    public MonetaryAmountDeserializer(){

    }

    public MonetaryAmountDeserializer(MoneyProperties moneyProperties){
        this.moneyProperties=moneyProperties;
        deserializerWithMetadata=new MonetaryAmountDeserializerWithMetadata();
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        if (!moneyProperties.getDeserializerUseMoneyFormat() || property==null){
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

    protected MonetaryAmount deserialize(JsonParser p,Locale locale, String pattern,MonetaryRounding rounding) throws IOException{
        if (p.getText()==null || p.getText().isBlank()){
            return null;
        }
        String str=p.getText().trim();
        //如果输入是个Object,解析其中的amount和currency
        if (p.currentToken().equals(JsonToken.START_OBJECT)){
            String amount=null;
            String currency=null;
            do{
                p.nextToken();
                if (p.currentToken().equals(JsonToken.FIELD_NAME)){
                    if(p.getText().equals(moneyProperties.getAmountFieldName())){
                        while(p.currentToken()!=null && !p.currentToken().equals(JsonToken.VALUE_NUMBER_FLOAT) && !p.currentToken().equals(JsonToken.VALUE_NUMBER_INT)){
                            p.nextToken();
                        }
                        amount=p.getText().trim();
                    }else if(p.getText().equals(moneyProperties.getCurrencyUnitFieldName())){
                        p.nextToken();
                        currency=p.getText().trim();
                    }
                }
            }while(!p.currentToken().equals(JsonToken.END_OBJECT) && p.currentToken()!=null);

            if (amount!=null && currency!=null){
                if (pattern.startsWith(MonetaryAmountFromStringFormatMetadata.PLACE_SYMBOL)){
                    str=currency+amount;
                }else{
                    str=amount+currency;
                }
            }else{
                if (moneyProperties.getErrorThrowException()){
                    throw new MonetaryException("error json money string");
                }
                return null;
            }
        }
        try {
            //判断是否是完整的Money字符串，完整的串形如：CNY23.45
            if (!MoneyUtils.isCompleteMoneyText(str,locale,CurrencyStyle.CODE)){
                if (pattern.startsWith(MonetaryAmountFromStringFormatMetadata.PLACE_SYMBOL)) {
                    str = MoneyUtils.prefixMoneyText(str, locale, CurrencyStyle.CODE);
                } else if (pattern.endsWith(MonetaryAmountFromStringFormatMetadata.PLACE_SYMBOL)) {
                    str = MoneyUtils.suffixMoneyText(str, locale, CurrencyStyle.CODE);
                }
            }
            MonetaryAmount money = MoneyUtils.stringToMoney(str, locale, CurrencyStyle.CODE, pattern);
            //返回取精后的值
            return money.with(rounding);
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (moneyProperties.getErrorThrowException()){
                throw e;
            }
        }
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
            return (T)MonetaryAmountDeserializer.this.deserialize(p,Context.findLocale(moneyFormat.locale()),pattern,rounding);
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
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return (T)MonetaryAmountDeserializer.this.deserialize(p,(Locale) formatAttrs.get(MonetaryAmountFromStringFormatMetadata.LOCALE),pattern,rounding);
        }
    }
}
