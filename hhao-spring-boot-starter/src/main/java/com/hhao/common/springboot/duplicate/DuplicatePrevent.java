package com.hhao.common.springboot.duplicate;

import com.hhao.common.springboot.aop.Aop;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Aop(interceptorIds = {DuplicatePreventInterceptorHandler.ID})
public @interface DuplicatePrevent {
    /**
     * 指定用于识别唯一性的键的属性名,建议用UUID
     */
    String uniqueKey();

    /**
     * 是否开启服务端检查MD5摘要，用于验证请求内容的一致性
     * 如果为true，则在服务端生成MD5摘要，在服务端进行验证
     * 如果为false，从客户端获取MD5摘要，在服务端进行验证
     */
    boolean openServerCheck() default false;

    /**
     * 防止重复提交的有效时间，单位秒
     */
    int expirationTime() default 10;
}