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
    private MoneyJsonSerializationConfig moneyJsonSerializationConfig =new MoneyJsonSerializationConfig();

    /**
     * Instantiates a new Money module.
     */
    public MoneyModule(){
        this(new MoneyJsonSerializationConfig());
    }


    /**
     * Instantiates a new Money module.
     */
    public MoneyModule( MoneyJsonSerializationConfig moneyJsonSerializationConfig) {
        this.moneyJsonSerializationConfig = moneyJsonSerializationConfig;

        addDeserializer(MonetaryAmount.class,new MonetaryAmountDeserializer(this.moneyJsonSerializationConfig));
        addDeserializer(FastMoney.class,new MonetaryAmountDeserializer(this.moneyJsonSerializationConfig));
        addDeserializer(Money.class,new MonetaryAmountDeserializer(this.moneyJsonSerializationConfig));
        addDeserializer(RoundedMoney.class,new MonetaryAmountDeserializer(this.moneyJsonSerializationConfig));
        addDeserializer(CurrencyUnit.class, new CurrencyUnitDeserializer());

        addSerializer(MonetaryAmount.class,new MonetaryAmountSerializer(MonetaryAmount.class,this.moneyJsonSerializationConfig));
        addSerializer(FastMoney.class,new MonetaryAmountSerializer(FastMoney.class,this.moneyJsonSerializationConfig));
        addSerializer(Money.class,new MonetaryAmountSerializer(Money.class,this.moneyJsonSerializationConfig));
        addSerializer(RoundedMoney.class,new MonetaryAmountSerializer(RoundedMoney.class,this.moneyJsonSerializationConfig));
        addSerializer(CurrencyUnit.class, new CurrencyUnitSerializer());
    }

}
