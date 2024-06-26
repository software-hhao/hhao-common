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

package com.hhao.common.springboot.safe;

import com.hhao.common.springboot.aop.Aop;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Html安全过滤注解
 * 可以开启Xss过滤、解码过滤
 * 用在api接口的方法、方法参数、参数类、参数类的字段上
 * 如果要使用在方法参数上，需要在方法上也要标注@SafeHtml注解，但是这样的话，方法上所有的参数都会被过滤（因为单独的参数不会被拦截到）
 * 或者自定义配置参数pointcutApi,使用AspectJ拦截，如：@Args({@annotation(com.hhao.common.springboot.safe.SafeHtml)})
 * 开启过滤的条件：
 * 1、开启api接口的aop;
 * 2、显式的标注SafeHtml注解
 * 注解的优先级：
 * a、api接口参数的SafeHtml注解效力-接口方法的SafeHtml-接口类的SafeHtml，这三个SafeHtml决定了是否对参数进行过滤
 * b、参数类型字段的SafeHtml注解效力-参数类的SafeHtml注解效力
 * 注解的传递：
 * api接口类-api接口方法-api接口参数-参数类-参数字段
 * 注解在参数类上的作用：
 * 该类下，String类型的字段均会被xss过滤；非String类型的，要级联过滤需要显式定义@SafeHtml
 * 支持List,Set,Map的处理
 *
 * @author Wang
 * @since 1.0.0
 */
@Target({FIELD,TYPE,METHOD,PARAMETER,ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@Aop(interceptorIds = {SafeHtmlInterceptorHandler.ID})
public @interface SafeHtml{
    /**
     * 过滤模式
     *
     * @return xss filter model
     */
    @AliasFor("xssFilterModel")
    XssFilterModel value() default XssFilterModel.ESCAPE;

    /**
     * 过滤模式
     *
     * @return xss filter model
     */
    @AliasFor("value")
    XssFilterModel xssFilterModel() default XssFilterModel.ESCAPE;

    /**
     * xss过滤时采用的规则
     *
     * @return the string
     */
    String xssPolicy() default "default";

    /**
     * 过滤前解码器
     * 目前支持"base64"
     *
     * @return string string
     */
    String decode() default "";

    /**
     * 是否深度遍历，默认开启
     *
     * @return boolean
     */
    boolean depthTraversal() default true;

    /**
     * 过滤模式
     */
    enum XssFilterModel{
        /**
         * 保留html标记，进行xss过滤
         */
        CLEAR,
        /**
         * 对String类型进行html编码转换，对html标记及元素进行转换
         */
        ESCAPE,
        /**
         * 不执行任何操作
         */
        NONE
    }
}
