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

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.RoundedMoney;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

/**
 * The type Money module.
 *
 * @author Wang
 * @since 1.0.0
 */
public class MoneyModule extends SimpleModule {
    private Boolean deserializerUseMoneyFormat=false;
    private Boolean serializerUseMoneyFormat=true;
    private MonetaryAmountSerializer.FieldNames fieldNames=new MonetaryAmountSerializer.FieldNames();

    /**
     * Instantiates a new Money module.
     */
    public MoneyModule(){
        this(false,true,new MonetaryAmountSerializer.FieldNames());
    }


    /**
     * Instantiates a new Money module.
     *
     * @param deserializerUseMoneyFormat the deserializer use money format
     * @param serializerUseMoneyFormat   the serializer use money format
     * @param fieldNames                 the field names
     */
    public MoneyModule(Boolean deserializerUseMoneyFormat,Boolean serializerUseMoneyFormat,MonetaryAmountSerializer.FieldNames fieldNames) {
        this.deserializerUseMoneyFormat=deserializerUseMoneyFormat;
        this.serializerUseMoneyFormat=serializerUseMoneyFormat;
        this.fieldNames=fieldNames;


        addDeserializer(MonetaryAmount.class,new MonetaryAmountDeserializer(this.deserializerUseMoneyFormat));
        addDeserializer(FastMoney.class,new MonetaryAmountDeserializer(this.deserializerUseMoneyFormat));
        addDeserializer(Money.class,new MonetaryAmountDeserializer(this.deserializerUseMoneyFormat));
        addDeserializer(RoundedMoney.class,new MonetaryAmountDeserializer(this.deserializerUseMoneyFormat));
        addDeserializer(CurrencyUnit.class, new CurrencyUnitDeserializer());

        addSerializer(MonetaryAmount.class,new MonetaryAmountSerializer(MonetaryAmount.class,this.fieldNames,this.serializerUseMoneyFormat));
        addSerializer(FastMoney.class,new MonetaryAmountSerializer(FastMoney.class,this.fieldNames,this.serializerUseMoneyFormat));
        addSerializer(Money.class,new MonetaryAmountSerializer(Money.class,this.fieldNames,this.serializerUseMoneyFormat));
        addSerializer(RoundedMoney.class,new MonetaryAmountSerializer(RoundedMoney.class,this.fieldNames,this.serializerUseMoneyFormat));
        addSerializer(CurrencyUnit.class, new CurrencyUnitSerializer());
    }

}
