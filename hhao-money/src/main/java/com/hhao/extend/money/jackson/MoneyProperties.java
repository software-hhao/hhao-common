
/*
 * Copyright 2018-2022 WangSheng.
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

/**
 * 序列化输出包含的字段的名称
 * 反序列化时将根据amountFieldName、currencyUnitFieldName进行反序列化
 *
 * @author Wang
 * @since 2022/2/2 17:14
 */
public class MoneyProperties {
    //数额
    private String amountFieldName;
    //币代码
    private String currencyUnitFieldName;
    //格式化显示字符串
    private String formattedFieldName;
    //转换失败是否抛出异常
    private Boolean errorThrowException=false;
    //反序列化时，是优先采用@MoneyFormat注解做格式化
    private Boolean deserializerUseMoneyFormat = false;
    //序列化时，是优先采用@MoneyFormat注解做格式化
    private Boolean serializerUseMoneyFormat = true;

    public MoneyProperties(){
        this(true,false,true);
    }

    public MoneyProperties(Boolean errorThrowException,Boolean deserializerUseMoneyFormat,Boolean serializerUseMoneyFormat){
        this.amountFieldName="amount";
        this.currencyUnitFieldName="currency";
        this.formattedFieldName="formatted";
        this.errorThrowException=errorThrowException;
        this.deserializerUseMoneyFormat=deserializerUseMoneyFormat;
        this.serializerUseMoneyFormat=serializerUseMoneyFormat;
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

    public Boolean getErrorThrowException() {
        return errorThrowException;
    }

    public void setErrorThrowException(Boolean errorThrowException) {
        this.errorThrowException = errorThrowException;
    }

    public Boolean getDeserializerUseMoneyFormat() {
        return deserializerUseMoneyFormat;
    }

    public void setDeserializerUseMoneyFormat(Boolean deserializerUseMoneyFormat) {
        this.deserializerUseMoneyFormat = deserializerUseMoneyFormat;
    }

    public Boolean getSerializerUseMoneyFormat() {
        return serializerUseMoneyFormat;
    }

    public void setSerializerUseMoneyFormat(Boolean serializerUseMoneyFormat) {
        this.serializerUseMoneyFormat = serializerUseMoneyFormat;
    }
}
