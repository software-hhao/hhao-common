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

package com.hhao.common.springboot.web.config.webserver;

import com.hhao.common.springboot.web.config.AbstractBaseMvcConfig;
import io.undertow.Undertow;
import jakarta.servlet.Servlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xnio.SslClientAuthMode;
import org.springframework.boot.web.server.Shutdown;
/**
 * 原类：JettyServletWebServerFactory
 * ServletWebServerFactoryAutoConfiguration
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(UndertowServletWebServerConfig.class)
@ConditionalOnClass({Servlet.class, Undertow.class, SslClientAuthMode.class})
@ConditionalOnProperty(prefix = "com.hhao.config.server", name = "undertow", havingValue = "true")
public class UndertowServletWebServerConfig extends AbstractBaseMvcConfig {
    /**
     * My web server factory customizer web server factory customizer.
     *
     * @return the web server factory customizer
     */
    @Bean
    public WebServerFactoryCustomizer myWebServerFactoryCustomizer() {
        return new MyUndertowWebServerCustomizer();
    }

    /**
     * 子类可以重写此配置
     *
     * @param factory the factory
     */
    protected void undertowServletWebServerFactory(UndertowServletWebServerFactory factory) {

    }

    private class MyUndertowWebServerCustomizer implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {
        /**
         * Customize.
         *
         * @param factory the factory
         */
        @Override
        public void customize(UndertowServletWebServerFactory factory) {
            factory.setShutdown(Shutdown.GRACEFUL);
            // customize the factory here
            undertowServletWebServerFactory(factory);
        }
    }
}

