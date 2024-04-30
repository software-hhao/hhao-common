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

package com.hhao.common.springboot.web.config.websocket.ws.server;

import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * 自定义握手拦截类
 *
 * @author Wang
 * @since 1.0.0
 */
public class DefaultHandshakeInterceptor implements HandshakeInterceptor {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(DefaultHandshakeInterceptor.class);
    /**
     * The Handshake authorization.
     */
    HandshakeAuthorization handshakeAuthorization = null;

    /**
     * Instantiates a new Default handshake interceptor.
     *
     * @param handshakeAuthorization the handshake authorization
     */
    public DefaultHandshakeInterceptor(HandshakeAuthorization handshakeAuthorization) {
        this.handshakeAuthorization = handshakeAuthorization;
    }

    /**
     * Before handshake boolean.
     *
     * @param request    the request
     * @param response   the response
     * @param wsHandler  the ws handler
     * @param attributes the attributes
     * @return the boolean
     * @throws Exception the exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        logger.debug("beforeHandshake:{}", request.getHeaders().toString());

        if (handshakeAuthorization.handshake(request, wsHandler, attributes)) {
            return true;
        }
        //如果握手失败，设置返回码
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
    }

    /**
     * After handshake.
     *
     * @param request   the request
     * @param response  the response
     * @param wsHandler the ws handler
     * @param ex        the ex
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        logger.debug("afterHandshake:{}", request.getHeaders().toString());
    }
}
