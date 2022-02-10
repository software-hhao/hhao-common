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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
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
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MonetaryAmount转字符串
 * 输出如下：{"amount":12.3,"currency":"CNY","formatted":"CNY12.3"}
 * 其中amount、currency直接来自Money，用于反序列化时使用
 * formatted根据元数据的设置或@MoneyFormat设置，会有不同格式的输出
 *
 * @param <T> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public class MonetaryAmountSerializer<T extends MonetaryAmount> extends StdSerializer<T> implements ContextualSerializer {
    protected final Logger logger = LoggerFactory.getLogger(MonetaryAmountSerializer.class);

    private MoneyProperties moneyProperties =null;
    private MonetaryAmountSerializerWithMetadata serializerWithMetadata=null;
    private Map<String,MonetaryAmountSerializerWithMoneyFormat> serializerWithMoneyFormatMap =new ConcurrentHashMap<>();

    protected MonetaryAmountSerializer(Class<T> t, MoneyProperties moneyProperties) {
        super(t);
        this.moneyProperties = moneyProperties;
        serializerWithMetadata=new MonetaryAmountSerializerWithMetadata(t);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (!moneyProperties.getSerializerUseMoneyFormat() || property==null){
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

    protected String format(MonetaryAmount money,Locale locale,CurrencyStyle currencyStyle,MonetaryRounding rounding,String pattern){
        try {
            //locale设置有可能会根据上下文信息取，所以不能缓存
            //先取精度，再转换
            return MoneyUtils.moneyToString(money.with(rounding),locale,currencyStyle,pattern);
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (MonetaryAmountSerializer.this.moneyProperties.getErrorThrowException()){
                throw e;
            }
        }
        return money.toString();
    }

    protected <S extends MonetaryAmount> void serialize(S value,JsonGenerator gen,Locale locale,CurrencyStyle currencyStyle,MonetaryRounding rounding,String pattern) throws IOException{
        //先取精
        MonetaryAmount money=value.with(rounding);
        BigDecimal amount=money.getNumber().numberValueExact(BigDecimal.class);
        CurrencyUnit currencyUnit = money.getCurrency();
        String formatted = format(money,locale,currencyStyle,rounding,pattern);

        gen.writeStartObject();
        gen.writeObjectField(moneyProperties.getAmountFieldName(), amount);
        gen.writeStringField(moneyProperties.getCurrencyUnitFieldName(), currencyUnit.getCurrencyCode());
        gen.writeStringField(moneyProperties.getFormattedFieldName(), formatted);
        gen.writeEndObject();
    }


    /**
     * 使用@MoneyFormat定义的格式转换
     * @param <T>
     */
    public class MonetaryAmountSerializerWithMoneyFormat<T extends MonetaryAmount> extends JsonSerializer<T>{
        private MoneyFormat moneyFormat=null;
        private CurrencyStyle currencyStyle;
        private String pattern;
        private MonetaryRounding rounding;

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
            MonetaryAmountSerializer.this.serialize(value,gen,Context.findLocale(moneyFormat.locale()),currencyStyle,rounding,pattern);
        }
    }

    /**
     * 用元数据设置的MONETARY_AMOUNT_TO_STRING、MONETARY_ROUNDING转换
     * @param <T>
     */
    public class MonetaryAmountSerializerWithMetadata<T extends MonetaryAmount> extends StdSerializer<T> {
        Map<String,Object> formatAttrs=null;
        private CurrencyStyle currencyStyle=null;
        private String pattern=null;
        private MonetaryRounding rounding=null;

        public MonetaryAmountSerializerWithMetadata(Class<T> t){
            super(t);
            //取元数据
            formatAttrs= Mdm.MONETARY_AMOUNT_TO_STRING.value(Map.class);
            currencyStyle=(CurrencyStyle)formatAttrs.get(MonetaryAmountFromStringFormatMetadata.CURRENCY_STYLE);
            pattern=(String)formatAttrs.get(MonetaryAmountFromStringFormatMetadata.CURRENCY_PATTERN);
            rounding=Mdm.MONETARY_ROUNDING.value(MonetaryRounding.class);
        }


        @Override
        public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            MonetaryAmountSerializer.this.serialize(value,gen,(Locale) formatAttrs.get(MonetaryAmountFromStringFormatMetadata.LOCALE),currencyStyle,rounding,pattern);
        }
    }
}
