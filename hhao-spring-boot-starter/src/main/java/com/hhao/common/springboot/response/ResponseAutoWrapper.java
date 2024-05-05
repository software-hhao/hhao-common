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

package com.hhao.common.springboot.response;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * 用于返回的包装类
 * 在control类或方法的上面加上此注解，则将返回内容包装
 * 详细看GlobalReturnConfig
 *
 * @author Wang
 * @since 1.0.0
 */
@Target({METHOD, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseAutoWrapper {
    /**
     * Value boolean.
     *
     * @return the boolean
     */
    boolean value() default true;
}
