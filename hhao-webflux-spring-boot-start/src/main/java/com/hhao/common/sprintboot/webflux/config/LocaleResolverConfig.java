
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

package com.hhao.common.sprintboot.webflux.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;
import org.springframework.web.server.i18n.FixedLocaleContextResolver;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.util.Locale;

/**
 * @author Wang
 * @since 2022/1/13 15:04
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({WebProperties.class })
@ConditionalOnMissingBean({LocaleResolverConfig.class})
@AutoConfigureBefore(WebFluxAutoConfiguration.class)
@ConditionalOnProperty(prefix = "com.hhao.config.locale-resolver",name = "enable",havingValue = "true",matchIfMissing = true)
public class LocaleResolverConfig {

    @Bean
    @ConditionalOnMissingBean(name = WebHttpHandlerBuilder.LOCALE_CONTEXT_RESOLVER_BEAN_NAME)
    public LocaleContextResolver localeContextResolver(WebProperties webProperties) {
        if (webProperties.getLocaleResolver() == WebProperties.LocaleResolver.FIXED) {
            return new FixedLocaleContextResolver(webProperties.getLocale());
        }
        AcceptHeaderLocaleContextResolver localeContextResolver = new AcceptHeaderLocaleContextResolver();
        localeContextResolver.setDefaultLocale(webProperties.getLocale());
        return localeContextResolver;
    }

    /**
     * 自定义Local解析处理类
     **/
    public static class LocaleResolver implements LocaleContextResolver {
        @Override
        public void setLocaleContext(ServerWebExchange exchange,  LocaleContext locale) {
            throw new UnsupportedOperationException(
                    "Cannot change HTTP accept header - use a different locale context resolution strategy");
        }

        @Override
        public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
            //优先从请求查询参数中获取本地化信息
            String language = exchange.getRequest().getQueryParams().getFirst("lang");
            //其次从请求头中获取本地化信息
            if (language == null || language.isEmpty()) {
                language = exchange.getRequest().getHeaders().getFirst("Accept-Language");
            }
            //再次从Cookie中获取本地化信息
            if (language == null || language.isEmpty()) {
                if (exchange.getRequest().getCookies().containsKey("lang")) {
                    language = exchange.getRequest().getCookies().getFirst("lang").getValue();
                }
            }
            //最后采用系统默认的本地化信息
            Locale targetLocale = Locale.getDefault();
            if (language != null && !language.isEmpty()) {
                targetLocale = Locale.forLanguageTag(language);
            }
            return new SimpleLocaleContext(targetLocale);
        }
    }
}
