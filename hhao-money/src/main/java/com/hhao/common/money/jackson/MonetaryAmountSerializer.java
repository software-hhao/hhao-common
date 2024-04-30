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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.metadata.CurrencyConfig;
import com.hhao.common.metadata.SystemMetadata;
import com.hhao.common.money.MoneyFormat;
import com.hhao.common.utils.MoneyUtils;
import org.javamoney.moneta.format.CurrencyStyle;

import javax.money.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MonetaryAmount转字符串
 * 输出如下：{"amount":"12.3","currency":"CNY","formatted":"CNY12.3"}
 * 其中amount、currency直接来自Money，用于反序列化时使用
 * formatted根据元数据的设置或@MoneyFormat设置，会有不同格式的输出
 *
 * @param <T> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public class MonetaryAmountSerializer<T extends MonetaryAmount> extends StdSerializer<T> implements ContextualSerializer {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(MonetaryAmountSerializer.class);

    private MoneyJsonSerializationConfig moneyJsonSerializationConfig =null;
    private MonetaryAmountSerializerWithMetadata serializerWithMetadata=null;
    private Map<String,MonetaryAmountSerializerWithMoneyFormat> serializerWithMoneyFormatMap =new ConcurrentHashMap<>();

    /**
     * Instantiates a new Monetary amount serializer.
     *
     * @param t               the t
     * @param moneyJsonSerializationConfig the money properties
     */
    protected MonetaryAmountSerializer(Class<T> t, MoneyJsonSerializationConfig moneyJsonSerializationConfig) {
        super(t);
        this.moneyJsonSerializationConfig = moneyJsonSerializationConfig;
        serializerWithMetadata=new MonetaryAmountSerializerWithMetadata(t);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (!moneyJsonSerializationConfig.getPreferMoneyFormatAnnotationForSerialization() || property==null){
            return serializerWithMetadata;
        }
        MoneyFormat moneyFormat=property.getAnnotation(MoneyFormat.class);
        if (moneyFormat==null){
            return serializerWithMetadata;
        }
        String key=moneyFormat.currencyStyle().name()+moneyFormat.locale()+moneyFormat.pattern()+moneyFormat.scale()+moneyFormat.roundingMode().name();
        MonetaryAmountSerializerWithMoneyFormat  deserializer=serializerWithMoneyFormatMap.get(key);
        if (deserializer==null){
            deserializer=new MonetaryAmountSerializerWithMoneyFormat(moneyFormat);
            serializerWithMoneyFormatMap.put(key,deserializer);
        }
        return deserializer;
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {

    }

    /**
     * Serialize.
     *
     * @param <S>           the type parameter
     * @param value         the value
     * @param gen           the gen
     * @param currencyStyle the currency style
     * @param rounding      the rounding
     * @param pattern       the pattern
     * @throws IOException the io exception
     */
    protected <S extends MonetaryAmount> void exSerialize(S value,JsonGenerator gen,CurrencyStyle currencyStyle,MonetaryRounding rounding,String pattern) throws IOException{
        MonetaryAmount money=value;
        CurrencyUnit currencyUnit = money.getCurrency();
        CurrencyConfig currencyConfig = SystemMetadata.getInstance().getCurrencyConfig(currencyUnit.getCurrencyCode());
        String amountFormater="";
        if (currencyStyle!=null && rounding!=null && pattern!=null){
            // 说明从MoneyFormat注解中获取
            amountFormater=format(money.with(rounding),currencyConfig.getCurrencyLocale(),currencyStyle,rounding,pattern);
        }else{
            // 从元数据中获取
            amountFormater=format(money.with(currencyConfig.getMonetaryRounding()),currencyConfig.getCurrencyLocale(),currencyConfig.getCurrencyStyleForDisplay(),currencyConfig.getMonetaryRounding(),currencyConfig.getPatternForDisplay());
        }
        BigDecimal amount=money.getNumber().numberValueExact(BigDecimal.class);

        gen.writeStartObject();
        gen.writeObjectField(moneyJsonSerializationConfig.getMoneyAmountFieldName(), amount.toPlainString());
        gen.writeStringField(moneyJsonSerializationConfig.getCurrencyCodeFieldName(), currencyUnit.getCurrencyCode());
        gen.writeStringField(moneyJsonSerializationConfig.getFormattedFieldName(), amountFormater);
        gen.writeEndObject();
    }

    protected String format(MonetaryAmount money,Locale locale,CurrencyStyle currencyStyle,MonetaryRounding rounding,String pattern){
        // 如果是MoneyFormater，则locale,currencyStyle,rounding,pattern就有值，否则根据money的CurrencyCode获取配置文件
        if (locale!=null && currencyStyle!=null && rounding!=null && pattern!=null){
            return MoneyUtils.moneyToString(money.with(rounding),locale,currencyStyle,pattern);
        }
        CurrencyConfig currencyConfig = SystemMetadata.getInstance().getCurrencyConfig(money.getCurrency().getCurrencyCode());
        return MoneyUtils.moneyToString(money,currencyConfig.getCurrencyLocale(),currencyConfig.getCurrencyStyleForDisplay(),currencyConfig.getPatternForDisplay());
    }

    /**
     * 使用@MoneyFormat定义的格式转换
     *
     * @param <T> the type parameter
     */
    public class MonetaryAmountSerializerWithMoneyFormat<T extends MonetaryAmount> extends JsonSerializer<T>{
        private MoneyFormat moneyFormat=null;
        private CurrencyStyle currencyStyle;
        private String pattern;
        private MonetaryRounding rounding;

        /**
         * Instantiates a new Monetary amount serializer with money format.
         *
         * @param moneyFormat the money format
         */
        public MonetaryAmountSerializerWithMoneyFormat(MoneyFormat moneyFormat){
            this.moneyFormat = moneyFormat;
            this.currencyStyle = moneyFormat.currencyStyle();
            this.pattern = moneyFormat.pattern();
            //取元数据精度
            this.rounding = Monetary.getRounding(
                    RoundingQueryBuilder.of().setScale(moneyFormat.scale()).set(moneyFormat.roundingMode()).build()
            );
        }

        @Override
        public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            MonetaryAmountSerializer.this.exSerialize(value,gen,currencyStyle,rounding,pattern);
        }
    }

    /**
     * 用元数据设置的MONETARY_AMOUNT_TO_STRING、MONETARY_ROUNDING转换
     *
     * @param <T> the type parameter
     */
    public class MonetaryAmountSerializerWithMetadata<T extends MonetaryAmount> extends StdSerializer<T> {
        public MonetaryAmountSerializerWithMetadata(Class<T> t){
            super(t);
        }

        @Override
        public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            MonetaryAmountSerializer.this.exSerialize(value,gen,null,null,null);
        }
    }
}
