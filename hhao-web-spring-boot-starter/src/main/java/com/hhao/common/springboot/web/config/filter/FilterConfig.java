/*
 * Copyright 2018-2021 WangSheng.
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

package com.hhao.common.springboot.web.config.filter;

import com.hhao.common.springboot.web.config.AbstractBaseMvcConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * 过滤器总装配
 *
 * @author Wang
 * {@code @WebServlet、@WebFilter和@WebListener}注释的类可以通过使用{@code @ServletComponentScan注释@Configuration类}并指定包含您想要注册的组件的包来自动注册到嵌入式servlet容器中。 默认情况下  ，{@code @ServletComponentScan}从注释类的包中扫描。
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(FilterConfig.class)
@EnableConfigurationProperties
@ConditionalOnProperty(prefix = "com.hhao.config.filter",name = "enable",havingValue = "true",matchIfMissing = false)
public class FilterConfig extends AbstractBaseMvcConfig {

    /**
     * request缓存模式
     */
    public enum RequestCacheType{
        /**
         * 采用spring的ContentCachingRequestWrapper
         */
        cache_after,
        /**
         * 采用自定义的CachingRequestWrapper
         */
        cache_before
    }

    @Value("${com.hhao.config.filter.caching-request.max-payload-length:1024}")
    private int maxPayloadLength;

    @Value("${com.hhao.config.filter.caching-request.type:cache_after}")
    private String requestType;


    /**
     * Request filter match properties match properties.
     *
     * @return the match properties
     */
    @Bean(name = "requestFilterMatchProperties")
    @ConfigurationProperties(prefix = "com.hhao.config.filter.caching-request")
    public MatchProperties requestFilterMatchProperties(){
        return new MatchProperties();
    }

    /**
     * 包装request，使用body可以重复读取
     *
     * @param matchProperties the match properties
     * @return filter registration bean
     */
    @Bean
    @ConditionalOnProperty(prefix = "com.hhao.config.filter.caching-request",name = "enable",havingValue = "true",matchIfMissing = true)
    FilterRegistrationBean cachingRequestFilter(@Qualifier("requestFilterMatchProperties")  MatchProperties matchProperties){
        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();
        if (RequestCacheType.cache_after.name().equals(requestType)){
            filterRegistrationBean.setFilter(new CachingAfterRequestFilter(maxPayloadLength,matchProperties));
        }else{
            filterRegistrationBean.setFilter(new CachingBeforeRequestFilter(maxPayloadLength,matchProperties));
        }
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("CachingRequestFilter");
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        return filterRegistrationBean;
    }

    /**
     * Response filter match properties match properties.
     *
     * @return the match properties
     */
    @Bean(name = "responseFilterMatchProperties")
    @ConfigurationProperties(prefix = "com.hhao.config.filter.caching-response")
    public MatchProperties responseFilterMatchProperties(){
        return new MatchProperties();
    }

    /**
     * 包装response
     *
     * @param matchProperties the match properties
     * @return filter registration bean
     */
    @Bean
    @ConditionalOnProperty(prefix = "com.hhao.config.filter.caching-response",name = "enable",havingValue = "true",matchIfMissing = false)
    FilterRegistrationBean cachingResponseFilter(@Qualifier("responseFilterMatchProperties") MatchProperties matchProperties){
        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new CachingResponseFilter(matchProperties));
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("CachingResponseFilter");
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        return filterRegistrationBean;
    }

    /**
     * forward处理
     * 来自Spring
     *
     * @return filter registration bean
     */
    @Bean
    @ConditionalOnProperty(prefix = "com.hhao.config.filter.forwarded-header",name = "enable",havingValue = "true",matchIfMissing = false)
    FilterRegistrationBean forwardedHeaderFilter(){
        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new ForwardedHeaderFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("ForwardedHeaderFilter");
        filterRegistrationBean.setOrder(3);
        filterRegistrationBean.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        return filterRegistrationBean;
    }

    /**
     * Log filter match properties match properties.
     *
     * @return the match properties
     */
    @Bean(name = "logFilterMatchProperties")
    @ConfigurationProperties(prefix = "com.hhao.config.filter.log")
    public MatchProperties logFilterMatchProperties(){
        return new MatchProperties();
    }

    /**
     * Log filter filter registration bean.
     *
     * @param matchProperties the match properties
     * @return the filter registration bean
     */
    @Bean
    @ConditionalOnProperty(prefix = "com.hhao.config.filter.log",name = "enable",havingValue = "true",matchIfMissing = true)
    FilterRegistrationBean logFilter(@Qualifier("logFilterMatchProperties") MatchProperties matchProperties){
        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new LoggingFilter(matchProperties));
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("LogFilter");
        filterRegistrationBean.setOrder(4);
        filterRegistrationBean.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        return filterRegistrationBean;
    }
}

