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

import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocketSession管理接口
 * 对连接后的WebSocketSession进行管理
 *
 * @author Wang
 * @since 1.0.0
 */
public interface WebSocketSessionManager {
    /**
     * 添加
     *
     * @param key     the key
     * @param session the session
     * @return the web socket session
     */
    WebSocketSession add(String key, WebSocketSession session);

    /**
     * 移除，不关闭
     *
     * @param key the key
     * @return the web socket session
     */
    WebSocketSession remove(String key);

    /**
     * 关闭并移除
     *
     * @param key the key
     */
    void clean(String key);

    /**
     * 获取
     *
     * @param key the key
     * @return web socket session
     */
    WebSocketSession get(String key);
}
