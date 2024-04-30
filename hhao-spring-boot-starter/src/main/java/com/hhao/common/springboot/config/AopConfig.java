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
package com.hhao.common.springboot.config;

import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.springboot.aop.InterceptorHandler;
import com.hhao.common.springboot.aop.InterceptorHandlerChain;
import com.hhao.common.springboot.aop.MethodInterceptorAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * The type Aop config.
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(AopConfig.class)
@EnableAspectJAutoProxy
public class AopConfig extends AbstractBaseConfig {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(AopConfig.class);
    /**
     * pointcut定义，如未定义或为空，则不会启动接口的aop
     */
    @Value("${com.hhao.config.aop.pointcut:}")
    private String pointcutApi;


    /**
     * 默认拦截@Aop与@SafeHtml
     * @return
     */
    private String getPointcutApi(){
        StringBuffer buf=new StringBuffer();
        if (StringUtils.hasLength(pointcutApi)){
            buf.append("(" + pointcutApi + ") || ");
        }
        buf.append("(");
        buf.append("(execution(public * *(..))) && ");
        buf.append("(");
        buf.append("@within(com.hhao.common.springboot.aop.Aop) || @annotation(com.hhao.common.springboot.aop.Aop) || ");
        buf.append("@within(com.hhao.common.springboot.safe.SafeHtml) || @annotation(com.hhao.common.springboot.safe.SafeHtml) ");
        buf.append(")");
        buf.append(")");
        return buf.toString();
    }

    /**
     * 定义api接口的advisor
     *
     * @param methodInterceptorAdvice the method interceptor advice
     * @return the aspect j expression pointcut advisor
     */
    @Bean
    @ConditionalOnMissingBean
    public AspectJExpressionPointcutAdvisor apiAdvisor(MethodInterceptorAdvice methodInterceptorAdvice) {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(getPointcutApi());
        advisor.setAdvice(methodInterceptorAdvice);
        logger.info("advice path:" + getPointcutApi());
        return advisor;
    }

    /**
     * Default method interceptor advice method interceptor advice.
     *
     * @param provider the provider
     * @return the method interceptor advice
     */
    @Bean
    @ConditionalOnMissingBean
    public MethodInterceptorAdvice defaultMethodInterceptorAdvice(ObjectProvider<List<InterceptorHandler>> provider){
        InterceptorHandlerChain interceptorHandlerChain = new InterceptorHandlerChain();
        loadedHandlerBySpring(provider, interceptorHandlerChain);
        loadedHandlerBySpi(interceptorHandlerChain);
        MethodInterceptorAdvice methodInterceptorAdvice = new MethodInterceptorAdvice(interceptorHandlerChain);
        return methodInterceptorAdvice;
    }

    /**
     * 从spi加载AbstractInterceptorHandler拦截器
     *
     * @param interceptorHandlerChain
     */
    private void loadedHandlerBySpi(InterceptorHandlerChain interceptorHandlerChain) {
        ServiceLoader<InterceptorHandler> serviceLoader = ServiceLoader.load(InterceptorHandler.class);
        Iterator<InterceptorHandler> iterator = serviceLoader.iterator();
        while(iterator.hasNext()){
            try {
                InterceptorHandler handler = iterator.next();
                logger.info("load interceptor by spi ->{}", handler.getClass().getName());
                interceptorHandlerChain.addHandler(handler);
            } catch (Exception e) {
                logger.error("Error loading interceptor from SPI", e);
            }
        }
    }

    /**
     * 从spring加载AbstractInterceptorHandler拦截器
     *
     * @param provider
     * @param methodInterceptorChain
     */
    private void loadedHandlerBySpring(ObjectProvider<List<InterceptorHandler>> provider, InterceptorHandlerChain methodInterceptorChain) {
        List<InterceptorHandler> getListBySpring = provider.getIfAvailable();
        if (!CollectionUtils.isEmpty(getListBySpring)) {
            for (InterceptorHandler handler : getListBySpring) {
                try {
                    logger.info("load interceptor by spring -> {}", handler.getClass().getName());
                    methodInterceptorChain.addHandler(handler);
                } catch (Exception e) {
                    logger.error("Error loading interceptor from Spring", e);
                }
            }
        }
    }
}
