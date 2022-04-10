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
    private MoneyProperties moneyProperties =new MoneyProperties(true,false,true);

    /**
     * Instantiates a new Money module.
     */
    public MoneyModule(){
        this(new MoneyProperties(true,false,true));
    }


    /**
     * Instantiates a new Money module.
     */
    public MoneyModule( MoneyProperties moneyProperties) {
        this.moneyProperties = moneyProperties;


        addDeserializer(MonetaryAmount.class,new MonetaryAmountDeserializer(this.moneyProperties));
        addDeserializer(FastMoney.class,new MonetaryAmountDeserializer(this.moneyProperties));
        addDeserializer(Money.class,new MonetaryAmountDeserializer(this.moneyProperties));
        addDeserializer(RoundedMoney.class,new MonetaryAmountDeserializer(this.moneyProperties));
        addDeserializer(CurrencyUnit.class, new CurrencyUnitDeserializer());

        addSerializer(MonetaryAmount.class,new MonetaryAmountSerializer(MonetaryAmount.class,this.moneyProperties));
        addSerializer(FastMoney.class,new MonetaryAmountSerializer(FastMoney.class,this.moneyProperties));
        addSerializer(Money.class,new MonetaryAmountSerializer(Money.class,this.moneyProperties));
        addSerializer(RoundedMoney.class,new MonetaryAmountSerializer(RoundedMoney.class,this.moneyProperties));
        addSerializer(CurrencyUnit.class, new CurrencyUnitSerializer());
    }

}
