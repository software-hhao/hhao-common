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
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Role;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 用@AOP注解实现拦截，@AOP注解类或方法，如果要使方法参数上的拦截，可定义pointcutApi。
 * 使用方法：
 * 直接使用@Aop注解或从@AOP注解派生自定义的注解，可参考@SafeHtml
 * 定义拦截器，实现InterceptorHandler接口，可生成Bean或采用Spi加载
 * 将拦截器id与@AOP注解的interceptorIds关联
 * 优先级如下：方法上的拦截器优先于类上的拦截器
 * 如果方法或类上定义了多重拦截器，多重拦截器都会执行，执行顺序按order
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(AopConfig.class)
@EnableAspectJAutoProxy
@Role(RootBeanDefinition.ROLE_INFRASTRUCTURE)
public class AopConfig extends AbstractBaseConfig {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(AopConfig.class);
    /**
     * pointcut定义，可设置其它路径的拦截
     * 如果单独使用函数参数上基于@AOP的注解，则需要设置pointcutApi，否则拦截不到，要使用AspectJ的注解
     * "(execution(public * *(..))) && (@Args({@annotation(com.hhao.common.springboot.safe.SafeHtml)}))"
     * "execution(* com.hhao.common.springboot.web.mvc.test.api.*.*(..))"
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
        buf.append("@within(com.hhao.common.springboot.safe.SafeHtml) || @annotation(com.hhao.common.springboot.safe.SafeHtml) || ");
        buf.append("@within(com.hhao.common.springboot.duplicate.DuplicatePrevent) || @annotation(com.hhao.common.springboot.duplicate.DuplicatePrevent) ");
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
    @Role(RootBeanDefinition.ROLE_INFRASTRUCTURE)
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
    @Role(RootBeanDefinition.ROLE_INFRASTRUCTURE)
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
