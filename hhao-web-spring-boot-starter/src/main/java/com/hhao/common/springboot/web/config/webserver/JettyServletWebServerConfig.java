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

package com.hhao.common.springboot.web.config.webserver;

import com.hhao.common.springboot.web.config.AbstractBaseMvcConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.Loader;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;

/**
 * 原类：JettyServletWebServerFactory
 * ServletWebServerFactoryAutoConfiguration
 *
 * @author wang
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(JettyServletWebServerConfig.class)
@ConditionalOnClass({ Servlet.class, Server.class, Loader.class, WebAppContext.class })
@ConditionalOnProperty(prefix = "com.hhao.config.server",name = "jetty",havingValue = "true")
public class JettyServletWebServerConfig extends AbstractBaseMvcConfig {
    /**
     * My web server factory customizer web server factory customizer.
     *
     * @return the web server factory customizer
     */
    @Bean
    public WebServerFactoryCustomizer myWebServerFactoryCustomizer() {
        return new MyJettyWebServerCustomizer();
    }

    /**
     * 子类可以重写此配置
     *
     * @param factory the factory
     */
    protected void jettyServletWebServerFactory(JettyServletWebServerFactory factory) {

    }

    private class MyJettyWebServerCustomizer implements WebServerFactoryCustomizer<JettyServletWebServerFactory> {
        /**
         * Customize.
         *
         * @param factory the factory
         */
        @Override
        public void customize(JettyServletWebServerFactory factory) {
            // customize the factory here
            jettyServletWebServerFactory(factory);
        }
    }
}
