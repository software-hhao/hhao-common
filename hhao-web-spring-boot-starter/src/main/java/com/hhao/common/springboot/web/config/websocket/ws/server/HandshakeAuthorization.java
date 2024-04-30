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

import com.hhao.common.springboot.web.config.websocket.Authorization;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;

import java.util.Map;

/**
 * The interface Handshake authorization.
 *
 * @author Wang
 * @since 1.0.0
 */
public interface HandshakeAuthorization extends Authorization {
    /**
     * Handshake boolean.
     *
     * @param request    the request
     * @param wsHandler  the ws handler
     * @param attributes the attributes
     * @return the boolean
     */
    boolean handshake(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes);
}
