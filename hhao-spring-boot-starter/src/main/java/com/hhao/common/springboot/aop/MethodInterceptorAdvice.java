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

import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * AspectJ的Advice
 * 拦截方法
 *
 * @author Wang
 * @since 1.0.0
 */
public class MethodInterceptorAdvice implements Ordered, MethodInterceptor, Serializable {
    private static final Logger logger = LoggerFactory.getLogger(MethodInterceptorAdvice.class);
    private int order = Ordered.HIGHEST_PRECEDENCE;
    private InterceptorHandlerChain interceptorHandlerChain;
    // 缓存拦截器
    private final ConcurrentHashMap<String, List<InterceptorHandler>> cachedInterceptorChains = new ConcurrentHashMap<>();

    /**
     * Instantiates a new Method interceptor advice.
     *
     * @param interceptorHandlerChain the interceptor handler chain
     */
    public MethodInterceptorAdvice(InterceptorHandlerChain interceptorHandlerChain){
        this.interceptorHandlerChain=interceptorHandlerChain;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public Object invoke( MethodInvocation invocation) throws Throwable {
        //if (!(invocation instanceof ProxyMethodInvocation)) {
        //    throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + invocation);
        //}

        //获取拦截器
        List<InterceptorHandler> interceptorHandlers= getInterceptorHandlers(invocation);
        //执行结果
        Object result=null;
        //异常结果
        Throwable error=null;
        try{
            //前置执行
            if (interceptorHandlers!=null) {
                for (InterceptorHandler handler : interceptorHandlers) {
                    if (!handler.onBegin(invocation)) {
                        throw new RuntimeException("The conditions for continued execution are not met");
                    }
                }
            }
            //调用处理
            result=invocation.proceed();
        }catch(Throwable e0){
            logger.error("Method Invocation error", e0);
            error=e0;
            throw e0;
        }finally{
            //后置处理
            if (interceptorHandlers!=null) {
                for (InterceptorHandler handler : interceptorHandlers) {
                    try {
                        result = handler.onComplete(result, error, invocation);
                    } catch (Throwable e1) {
                        logger.error("Error during processing onComplete method", e1); //
                        throw new RuntimeException("An error occurred during processing");
                    }
                }
            }
        }
        return result;
    }

    /**
     * 根据@Aop定义获拦截器
     *
     * @param invocation the invocation
     * @return the list
     */
    @Nullable
    protected List<InterceptorHandler> getInterceptorHandlers(MethodInvocation invocation){
        Method method=invocation.getMethod();
        String key=method.toGenericString();
        return cachedInterceptorChains.computeIfAbsent(
                key,
                k -> {
                    List<InterceptorHandler> temp= buildInterceptorChainForMethod(method);
                    return CollectionUtils.isEmpty(temp) ? null : temp;
                }
        );
    }

    private List<InterceptorHandler> buildInterceptorChainForMethod(Method method) {
        String [] ids=null;

        //获取方法的@Aop，可能为null
        List<Aop> aopAnnotations=this.getMethodAopAnnotation(method);

        //获取类的@Aop
        if (CollectionUtils.isEmpty(aopAnnotations)){
            aopAnnotations=this.geClassAopAnnotation(method);
        }

        if (CollectionUtils.isEmpty(aopAnnotations)){
            return Collections.emptyList();
        }

        List<InterceptorHandler> interceptorHandlers=new ArrayList<>();
        for(Aop aop:aopAnnotations){
            interceptorHandlers.addAll(interceptorHandlerChain.getInterceptorChain(aop.interceptorIds()));
        }

        //拦截器去重
        interceptorHandlers = interceptorHandlers.stream().distinct().collect(Collectors.toList());

        //拦截器排序
        OrderComparator.sort(interceptorHandlers);
        return interceptorHandlers;
    }

    private List<Aop> getMethodAopAnnotation(Method method){
        List<Aop> annotations=Arrays.stream(method.getAnnotations()).map(
                annotation -> {
                    if (annotation.annotationType().equals(Aop.class)){
                        return (Aop)annotation;
                    }
                    return AnnotationUtils.findAnnotation(annotation.getClass(),Aop.class);
                }
        ).filter(
                aop -> {
                    return aop!=null;
                }
        ).collect(Collectors.toList());

        return annotations;
    }

    private List<Aop> geClassAopAnnotation(Method method){
        List<Aop> annotations=Arrays.stream(method.getDeclaringClass().getAnnotations()).map(
                annotation -> {
                    if (annotation.annotationType().equals(Aop.class)){
                        return (Aop)annotation;
                    }
                    return AnnotationUtils.findAnnotation(annotation.getClass(),Aop.class);
                }
        ).filter(
                aop -> {
                    return aop!=null;
                }
        ).collect(Collectors.toList());
        return annotations;
    }
}
