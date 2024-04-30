
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

package com.hhao.common.money.jackson;

/**
 * 序列化输出包含的字段的名称
 * 反序列化时将根据amountFieldName、currencyUnitFieldName进行反序列化
 *
 * @author Wang
 * @since 2022/2/2 17:14
 */
public class MoneyJsonSerializationConfig {
    //数额
    private String moneyAmountFieldName="";
    //币代码
    private String currencyCodeFieldName="currency";
    //格式化显示字符串
    private String formattedFieldName="formatted";
    //转换失败是否抛出异常
    private Boolean throwExceptionOnConversionError =true;
    //反序列化时，是优先采用@MoneyFormat注解做格式化
    private Boolean preferMoneyFormatAnnotationForDeserialization = true;
    //序列化时，是优先采用@MoneyFormat注解做格式化
    private Boolean preferMoneyFormatAnnotationForSerialization = true;

    public MoneyJsonSerializationConfig(){

    }

    public String getMoneyAmountFieldName() {
        return moneyAmountFieldName;
    }

    public void setMoneyAmountFieldName(String moneyAmountFieldName) {
        this.moneyAmountFieldName = moneyAmountFieldName;
    }

    public String getCurrencyCodeFieldName() {
        return currencyCodeFieldName;
    }

    public void setCurrencyCodeFieldName(String currencyCodeFieldName) {
        this.currencyCodeFieldName = currencyCodeFieldName;
    }

    public String getFormattedFieldName() {
        return formattedFieldName;
    }

    public void setFormattedFieldName(String formattedFieldName) {
        this.formattedFieldName = formattedFieldName;
    }

    public Boolean getThrowExceptionOnConversionError() {
        return throwExceptionOnConversionError;
    }

    public void setThrowExceptionOnConversionError(Boolean throwExceptionOnConversionError) {
        this.throwExceptionOnConversionError = throwExceptionOnConversionError;
    }

    public Boolean getPreferMoneyFormatAnnotationForDeserialization() {
        return preferMoneyFormatAnnotationForDeserialization;
    }

    public void setPreferMoneyFormatAnnotationForDeserialization(Boolean preferMoneyFormatAnnotationForDeserialization) {
        this.preferMoneyFormatAnnotationForDeserialization = preferMoneyFormatAnnotationForDeserialization;
    }

    public Boolean getPreferMoneyFormatAnnotationForSerialization() {
        return preferMoneyFormatAnnotationForSerialization;
    }

    public void setPreferMoneyFormatAnnotationForSerialization(Boolean preferMoneyFormatAnnotationForSerialization) {
        this.preferMoneyFormatAnnotationForSerialization = preferMoneyFormatAnnotationForSerialization;
    }
}
