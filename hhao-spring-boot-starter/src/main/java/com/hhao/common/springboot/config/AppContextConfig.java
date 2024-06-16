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

import com.hhao.common.Context;
import com.hhao.common.metadata.SystemMetadata;
import com.hhao.common.springboot.AppContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.time.ZoneId;
import java.util.Locale;

/**
 * The type App context config.
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnMissingBean(AppContextConfig.class)
@ConditionalOnProperty(prefix = "com.hhao.config.app-context",name = "enable",havingValue = "true",matchIfMissing = true)
public class AppContextConfig extends AbstractBaseConfig {

    public AppContextConfig(){

    }

    /**
     * 初始化全局上下文
     *
     * @return the context
     */
    protected AppContext configureContext() {
        return (AppContext)Context.ContextFactory.setContext(new AppContext() {
            protected final Log logger = LogFactory.getLog(AppContext.class);
            protected String appStatus = "";

            @Override
            public ApplicationContext applicationContext() {
                return applicationContext;
            }

            @Override
            public Locale getLocale() {
                if (LocaleContextHolder.getLocale()!=null){
                    return LocaleContextHolder.getLocale();
                }
                return SystemMetadata.getInstance().getLocale();
            }

            @Override
            public ZoneId getZoneId() {
                if (LocaleContextHolder.getTimeZone()!=null) {
                    return LocaleContextHolder.getTimeZone().toZoneId();
                }
                return SystemMetadata.getInstance().getZoneId();
            }

            @Override
            public String getMessage(String code, Object[] args, Locale locale) {
                return applicationContext().getMessage(code,args,locale);
            }

            @Override
            public String getAppStatus() {
                return this.appStatus;
            }

            @Override
            public void setAppStatus(String status) {
                this.appStatus = status;
            }
        });
    }


    /**
     * 初始化全局上下文
     *
     * @return the context
     */
    @Bean
    public AppContext appContext() {
        return configureContext();
    }
}
