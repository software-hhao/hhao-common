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
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.UpgradeProtocol;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.net.URL;

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
@ConditionalOnProperty(prefix = "com.hhao.config.server",name = "tomcat",havingValue = "true")
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
            // customize the factory here
            //factory.addAdditionalTomcatConnectors(createSslConnector());
            tomcatServletWebServerFactory(factory);
        }
    }

    /**
     * 创建多连接
     * @return
     */
    private Connector createSslConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        try {
            URL keystore = ResourceUtils.getURL("keystore");
            URL truststore = ResourceUtils.getURL("truststore");
            connector.setScheme("https");
            connector.setSecure(true);
            connector.setPort(8443);
            protocol.setSSLEnabled(true);
            protocol.setKeystoreFile(keystore.toString());
            protocol.setKeystorePass("changeit");
            protocol.setTruststoreFile(truststore.toString());
            protocol.setTruststorePass("changeit");
            protocol.setKeyAlias("apitester");
            return connector;
        }
        catch (IOException ex) {
            throw new IllegalStateException("Fail to create ssl connector", ex);
        }
    }
}
