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

package com.hhao.common.springboot.web.config.websocket.ws.server;

import com.hhao.common.springboot.web.config.AbstractBaseMvcConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.AbstractHandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.util.Map;

/**
 * websocket服务端配置文件
 * 只适用于tomcat
 *
 * <pre>{@code
 * @Bean
 * public ServletServerContainerFactoryBean createWebSocketContainer() {
 *      ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
 *      container.setMaxTextMessageBufferSize(8192);
 *      container.setMaxBinaryMessageBufferSize(8192);
 *      return container;
 * }
 * }
 * </pre>
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration
@EnableWebSocket
@ConditionalOnMissingBean(WebSocketServerConfig.class)
@ConditionalOnClass({WebSocketHandlerRegistry.class})
@ConditionalOnProperty(prefix = "com.hhao.config.websocket-server",name = "enable",havingValue = "true",matchIfMissing = false)
public class WebSocketServerConfig extends AbstractBaseMvcConfig implements WebSocketConfigurer {
    /**
     * 注册用@WebSocketServer标注的websocket服务端
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        Map<String, Object> handlerMap=this.getApplicationContext().getBeansWithAnnotation(WebSocketServer.class);
        handlerMap.keySet().forEach(key->{
            WebSocketHandler webSocketHandler=(WebSocketHandler) handlerMap.get(key);
            WebSocketServer webSocket=webSocketHandler.getClass().getDeclaredAnnotation(WebSocketServer.class);

            registry.addHandler(webSocketHandler,webSocket.value()!=null?webSocket.value():webSocket.paths())
                    .setAllowedOrigins(webSocket.origins())
                    .setHandshakeHandler(getHandshakeHandler())
                    .addInterceptors(getHandshakeInterceptor())
                    .withSockJS()
                    .setDisconnectDelay(10*1000)
                    .setSessionCookieNeeded(true);
                    //.setHttpMessageCacheSize(1000)
                    //.setStreamBytesLimit(512*1024)
        });
    }

    /**
     * 默认的握手Handler
     *
     * @return the handshake handler
     */
    protected AbstractHandshakeHandler getHandshakeHandler() {
        return new DefaultHandshakeHandler();
    }


    /**
     * 默认的握手拦截
     *
     * @return the handshake interceptor
     */
    protected HandshakeInterceptor getHandshakeInterceptor() {
        return new DefaultHandshakeInterceptor(getHandshakeAuthorization());
    }

    /**
     * 验证类
     * 可以重写这个方法
     *
     * @return the handshake authorization
     */
    protected HandshakeAuthorization getHandshakeAuthorization(){
        return new HandshakeAuthorizationWithToken();
    }

    /**
     * 默认的websocketsession管理器
     *
     * @return the default web socket session manager
     */
    @Bean
    public DefaultWebSocketSessionManager webSocketSessionManager(){
        return new DefaultWebSocketSessionManager();
    }
}
