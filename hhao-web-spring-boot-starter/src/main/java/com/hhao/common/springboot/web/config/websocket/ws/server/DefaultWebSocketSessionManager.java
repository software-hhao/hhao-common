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

import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的WebSocketSession管理类
 *
 * @author Wang
 * @since 1.0.0
 */
public class DefaultWebSocketSessionManager implements WebSocketSessionManager {
    private ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>(32);

    @Override
    public WebSocketSession add(String key, WebSocketSession session) {
        sessionMap.put(key, session);
        return session;
    }

    @Override
    public WebSocketSession remove(String key) {
        return sessionMap.remove(key);
    }

    @Override
    public void clean(String key) {
        WebSocketSession webSocketSession = sessionMap.remove(key);
        try {
            webSocketSession.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public WebSocketSession get(String key) {
        return sessionMap.get(key);
    }
}
