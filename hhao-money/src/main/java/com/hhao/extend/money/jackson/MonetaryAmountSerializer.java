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
 *
 * @param <T> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public class MonetaryAmountSerializer<T extends MonetaryAmount> extends StdSerializer<T> implements ContextualSerializer {
    protected final Logger logger = LoggerFactory.getLogger(MonetaryAmountSerializer.class);
    /**
     * 是否采用@MoneyFormat注解定义的格式，或者是使用元数据MONETARY_AMOUNT_TO_STRING、MONETARY_ROUNDING定义的格式进行转换;
     */
    private boolean useMoneyFormat=false;
    private FieldNames fieldNames=null;
    private MonetaryAmountSerializerWithMetadata serializerWithMetadata=null;
    private Map<String,MonetaryAmountSerializerWithMoneyFormat> serializerWithMoneyFormatMap =new ConcurrentHashMap<>();

    protected MonetaryAmountSerializer(Class<T> t, FieldNames fieldNames,boolean useMoneyFormat) {
        super(t);
        this.fieldNames=fieldNames;
        this.useMoneyFormat=useMoneyFormat;
        serializerWithMetadata=new MonetaryAmountSerializerWithMetadata(t,fieldNames);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (!useMoneyFormat || property==null){
            return serializerWithMetadata;
        }
        MoneyFormat moneyFormat=property.getAnnotation(MoneyFormat.class);
        if (moneyFormat==null){
            return serializerWithMetadata;
        }
        String key=moneyFormat.currencyStyle().name()+moneyFormat.locale()+moneyFormat.pattern()+moneyFormat.scale()+moneyFormat.roundingMode().name();
        MonetaryAmountSerializerWithMoneyFormat  deserializer=serializerWithMoneyFormatMap.get(key);
        if (deserializer==null){
            deserializer=new MonetaryAmountSerializerWithMoneyFormat(moneyFormat,fieldNames);
            serializerWithMoneyFormatMap.put(key,deserializer);
        }
        return deserializer;
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {

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
        private FieldNames fieldNames=null;

        public MonetaryAmountSerializerWithMoneyFormat(MoneyFormat moneyFormat,FieldNames fieldNames){
            this.moneyFormat = moneyFormat;
            this.currencyStyle = moneyFormat.currencyStyle();
            this.pattern = moneyFormat.pattern();
            //取元数据精度
            this.rounding = Monetary.getRounding(
                    RoundingQueryBuilder.of().setScale(moneyFormat.scale()).set(moneyFormat.roundingMode()).build()
            );
            this.fieldNames=fieldNames;
        }

        private String format(MonetaryAmount money){
            try {
                //locale设置有可能会根据上下文信息取，所以不能缓存
                Locale locale= Context.findLocale(moneyFormat.locale());
                //先取精度，再转换
                return MoneyUtils.moneyToString(money.with(rounding),locale,currencyStyle,pattern);
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug(e.getMessage());
            }
            return money.toString();
        }

        @Override
        public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            //先取精
            MonetaryAmount money=value.with(rounding);
            CurrencyUnit currencyUnit = money.getCurrency();
            String formatted = format(money);
            BigDecimal amount=money.getNumber().numberValueExact(BigDecimal.class);

            gen.writeStartObject();
            gen.writeObjectField(fieldNames.getAmountFieldName(), amount);
            gen.writeStringField(fieldNames.getCurrencyUnitFieldName(), currencyUnit.getCurrencyCode());
            gen.writeStringField(fieldNames.getFormattedFieldName(), formatted);
            gen.writeEndObject();

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
        private FieldNames fieldNames=null;

        public MonetaryAmountSerializerWithMetadata(Class<T> t,FieldNames fieldNames){
            super(t);
            //取元数据
            formatAttrs= Mdm.MONETARY_AMOUNT_TO_STRING.value(Map.class);
            currencyStyle=(CurrencyStyle)formatAttrs.get(MonetaryAmountFromStringFormatMetadata.CURRENCY_STYLE);
            pattern=(String)formatAttrs.get(MonetaryAmountFromStringFormatMetadata.CURRENCY_PATTERN);
            rounding=Mdm.MONETARY_ROUNDING.value(MonetaryRounding.class);
            this.fieldNames=fieldNames;
        }

        private String format(MonetaryAmount money){
            try {
                //locale设置有可能会根据上下文信息取，所以不能缓存
                Locale locale=(Locale) formatAttrs.get(MonetaryAmountFromStringFormatMetadata.LOCALE);
                //先取精度，再转换
                return MoneyUtils.moneyToString(money.with(rounding),locale,currencyStyle,pattern);
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug(e.getMessage());
            }
            return money.toString();
        }

        @Override
        public void serialize(T object, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            //先取精
            MonetaryAmount money=object.with(rounding);
            CurrencyUnit currencyUnit = money.getCurrency();
            String formatted = format(money);
            BigDecimal amount=money.getNumber().numberValueExact(BigDecimal.class);

            gen.writeStartObject();
            gen.writeObjectField(fieldNames.getAmountFieldName(), amount);
            gen.writeStringField(fieldNames.getCurrencyUnitFieldName(), currencyUnit.getCurrencyCode());
            gen.writeStringField(fieldNames.getFormattedFieldName(), formatted);
            gen.writeEndObject();
        }
    }


    public static class FieldNames {
        //数额
        private String amountFieldName;
        //币代码
        private String currencyUnitFieldName;
        //格式化显示字符串
        private String formattedFieldName;

        public FieldNames(){
            this.amountFieldName="amount";
            this.currencyUnitFieldName="currency";
            this.formattedFieldName="formatted";
        }

        public FieldNames(String amountFieldName,String currencyUnitFieldName,String formattedFieldName){
            this.amountFieldName=amountFieldName;
            this.currencyUnitFieldName=currencyUnitFieldName;
            this.formattedFieldName=formattedFieldName;
        }

        public String getAmountFieldName() {
            return amountFieldName;
        }

        public void setAmountFieldName(String amountFieldName) {
            this.amountFieldName = amountFieldName;
        }

        public String getCurrencyUnitFieldName() {
            return currencyUnitFieldName;
        }

        public void setCurrencyUnitFieldName(String currencyUnitFieldName) {
            this.currencyUnitFieldName = currencyUnitFieldName;
        }

        public String getFormattedFieldName() {
            return formattedFieldName;
        }

        public void setFormattedFieldName(String formattedFieldName) {
            this.formattedFieldName = formattedFieldName;
        }
    }
}
