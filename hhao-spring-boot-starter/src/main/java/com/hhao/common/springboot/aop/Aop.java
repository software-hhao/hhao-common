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

package com.hhao.common.springboot.aop;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * 定义在类、方法上，用于定义拦截器
 * 拦截器可以spi方式定义，也可以定义成Spring Bean
 * 拦截器继承自InterceptorHandler
 *
 * @author Wang
 * @since 1.0.0
 */
@Target({TYPE,METHOD,ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Aop {
    /**
     * 定义拦截器的id
     *
     * @return the string [ ]
     */
    String[] interceptorIds();
}
