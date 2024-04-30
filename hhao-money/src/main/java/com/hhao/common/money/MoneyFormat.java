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

package com.hhao.common.money;

import org.javamoney.moneta.format.CurrencyStyle;

import java.lang.annotation.*;
import java.math.RoundingMode;

/**
 * Money格式化设置
 * Spring下默认适用Money-String,String-Money采用Convert
 * Jackson下由配置参数决定是否使用
 *
 * @author Wang
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
public @interface MoneyFormat {

    /**
     * CurrencyStyle类型
     * 对输出有用
     * @return currency style
     */
    CurrencyStyle currencyStyle() default CurrencyStyle.SYMBOL;

    /**
     * 格式字符串
     * 对输入、输出均适用
     * @return string
     */
    String pattern() default "¤#,###0.00";

    /**
     * 显示小数后的精度
     * 对输出有用
     * @return int
     */
    int scale() default 2;

    /**
     * Locale:
     * default:采用元数据设置的locale
     * context:采用用户上下文的locale
     * 语言-国家指定locale:如zh-CN
     * 对输入有用，通过它指定CurrencyCode
     * @return string
     */
    String locale() default "default";

    /**
     * 取精模式
     * 对输出有用
     *
     * @return rounding mode
     */
    RoundingMode roundingMode() default RoundingMode.HALF_EVEN;
}
