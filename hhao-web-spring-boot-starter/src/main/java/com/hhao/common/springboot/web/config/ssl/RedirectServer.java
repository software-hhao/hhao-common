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

package com.hhao.common.springboot.web.config.ssl;


import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.AbstractServletWebServerFactory;

/***
 * @author Wan
 * @since 1.0.0
 */
public interface RedirectServer {
    /**
     * The enum Run state.
     */
    enum RUN_STATE {
        /**
         * 正在运行状态
         */
        RUNNING,
        /**
         * 停止状态
         */
        STOP;
    };

    /**
     * 启动服务
     *
     * @param fromPort the from port
     * @param toPort   the to port
     * @return 是否成功 boolean
     */
    boolean start(int fromPort, int toPort);

    /**
     * 停止服务
     */
    void stop();

    /**
     * 返回当前服务器状态
     *
     * @return state
     */
    RUN_STATE getState();

    /**
     * 服务当前服务器状态
     *
     * @param state the state
     */
    void setState(RUN_STATE state);

    /**
     * 构建服务器
     *
     * @param factory the factory
     * @return redirect server
     */
    static RedirectServer build(AbstractServletWebServerFactory factory) {
        if (factory instanceof TomcatServletWebServerFactory) {
            return new TomcatRedirectServer((TomcatServletWebServerFactory) factory);
        }
        return null;
    }

    /**
     * The type Tomcat redirect server.
     */
    class TomcatRedirectServer implements RedirectServer {
        private TomcatServletWebServerFactory webServerFactory;
        private RUN_STATE state = RUN_STATE.STOP;

        /**
         * Instantiates a new Tomcat redirect server.
         *
         * @param webServerFactory the web server factory
         */
        public TomcatRedirectServer(TomcatServletWebServerFactory webServerFactory) {
            this.webServerFactory = webServerFactory;
        }

        @Override
        public boolean start(int fromPort, int toPort) {
            webServerFactory.addContextCustomizers(new TomcatContextCustomizer() {
                @Override
                public void customize(Context context) {
                    SecurityConstraint securityConstraint = new SecurityConstraint();
                    securityConstraint.setUserConstraint("CONFIDENTIAL");
                    SecurityCollection collection = new SecurityCollection();
                    collection.addPattern("/*");
                    securityConstraint.addCollection(collection);
                    context.addConstraint(securityConstraint);
                }
            });

            this.webServerFactory.addAdditionalTomcatConnectors(createRedirectConnector(fromPort, toPort));
            state = RUN_STATE.RUNNING;
            return true;
        }

        @Override
        public void stop() {
            state = RUN_STATE.STOP;
        }

        @Override
        public RUN_STATE getState() {
            return state;
        }

        @Override
        public void setState(RUN_STATE state) {
            this.state = state;
        }

        private Connector createRedirectConnector(int fromPort, int toPort) {
            Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
            connector.setScheme("http");
            connector.setPort(fromPort);
            connector.setSecure(false);
            connector.setRedirectPort(toPort);
            return connector;
        }
    }
}
