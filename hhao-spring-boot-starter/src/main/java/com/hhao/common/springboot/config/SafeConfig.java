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

import com.hhao.common.springboot.safe.SafeHtmlInterceptorHandler;
import com.hhao.common.springboot.safe.executor.DefaultSafeHtmlExecutor;
import com.hhao.common.springboot.safe.executor.SafeHtmlExecutor;
import com.hhao.common.springboot.safe.decode.Base64DecodeHandler;
import com.hhao.common.springboot.safe.decode.DecodeHandler;
import com.hhao.common.springboot.safe.filter.DefaultSafeFilter;
import com.hhao.common.springboot.safe.filter.SafeFilter;
import com.hhao.common.springboot.safe.xss.DefaultXssPolicyHandler;
import com.hhao.common.springboot.safe.xss.XssPolicyHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * The type Safe config.
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration
@ConditionalOnMissingBean(SafeConfig.class)
@ConditionalOnProperty(prefix = "com.hhao.config.aop.safe",name = "enable",havingValue = "true",matchIfMissing = true)
public class SafeConfig extends AbstractBaseConfig {
    /**
     * Xss过滤的规则文件
     */
    @Value("${com.hhao.config.aop.safe.xss.policy:classpath:config/antisamy.xml}")
    public String policyUri;

    /**
     * 构建SafeHtmlExecutor执行器
     *
     * @param xssPolicyHandlers the xss policy handlers
     * @param decodeHandlers    the decode handlers
     * @return safe html executor
     */
    @Bean
    @ConditionalOnMissingBean
    public SafeHtmlExecutor safeHtmlExecutor(List<XssPolicyHandler> xssPolicyHandlers, List<DecodeHandler> decodeHandlers){
        return new DefaultSafeHtmlExecutor(xssPolicyHandlers,decodeHandlers);
    }

    /**
     * 创建XSS过滤规则的Bean类;
     * 可以创建更多的PolicyHandler Bean,可以在@SafeHtml的policy中指定它们，其值为PolicyHandler中的name;
     *
     * @return xss policy handler
     */
    @Bean
    @ConditionalOnMissingBean
    public XssPolicyHandler xssPolicyHandler(){
       return new DefaultXssPolicyHandler(policyUri);
    }

    /**
     * 创建XSS过滤解码器；
     * 可以创建更用的DecodeHandler
     *
     * @return decode handler
     */
    @Bean
    @ConditionalOnMissingBean
    public DecodeHandler base64DecodeHandler(){
        return new Base64DecodeHandler();
    }

    /**
     * Safe filter safe filter.
     *
     * @param safeHtmlExecutor the safe html executor
     * @return the safe filter
     */
    @Bean
    @ConditionalOnMissingBean
    public SafeFilter safeFilter(SafeHtmlExecutor safeHtmlExecutor){
        return new DefaultSafeFilter(safeHtmlExecutor);
    }

    /**
     * 构建拦截器
     * 相关查看AopConfig
     *
     * @param safeFilter the safe filter
     * @return the safe html interceptor handler
     */
    @Bean
    @ConditionalOnMissingBean
    public SafeHtmlInterceptorHandler safeHtmlInterceptorHandler(SafeFilter safeFilter){
        return new SafeHtmlInterceptorHandler(safeFilter);
    }
}
