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
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.UpgradeProtocol;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Shutdown;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 种重服务器的配置类
 * Server	Servlet stack	                    Reactive stack
 * Tomcat   TomcatServletWebServerFactory       TomcatReactiveWebServerFactory
 * Jetty    JettyServletWebServerFactory        JettyReactiveWebServerFactory
 * Undertow UndertowServletWebServerFactory     UndertowReactiveWebServerFactory
 * Reactor  N/A                                 NettyReactiveWebServerFactory
 * 默认本配置没有启用
 * 原配置类：ServletWebServerFactoryAutoConfiguration
 *
 * @author Wan
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(TomcatServletWebServerConfig.class)
@ConditionalOnClass({Tomcat.class, UpgradeProtocol.class})
@ConditionalOnProperty(prefix = "com.hhao.config.server", name = "tomcat", havingValue = "true")
public class TomcatServletWebServerConfig extends AbstractBaseMvcConfig {
    /***
     * 采用配置WebServerFactoryCustomizer的方式配置服务器，这样可以利用SpringBoot的属性来定义服务器
     * @return org.springframework.boot.web.server.WebServerFactoryCustomizer web server factory customizer
     */
    @Bean
    public WebServerFactoryCustomizer myWebServerFactoryCustomizer() {
        return new MyTomcatWebServerCustomizer();
    }

    /**
     * 子类可以重写此配置
     *
     * @param factory the factory
     */
    protected void tomcatServletWebServerFactory(TomcatServletWebServerFactory factory) {

    }

    private class MyTomcatWebServerCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
        /**
         * Customize.
         *
         * @param factory the factory
         */
        @Override
        public void customize(TomcatServletWebServerFactory factory) {
            factory.setShutdown(Shutdown.GRACEFUL);
            // customize the factory here
            //factory.addAdditionalTomcatConnectors(createSslConnector());
            tomcatServletWebServerFactory(factory);
        }
    }
}
