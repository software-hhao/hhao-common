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

package com.hhao.common.springboot.web.config.websocket.stomp.server.even;

import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.messaging.SessionConnectEvent;

/**
 * 当接收到一个新的STOMP CONNECT时发布，以指示一个新的客户端会话的开始。
 * 事件包含表示连接的消息，包括会话ID、用户信息(如果有的话)和客户端发送的任何自定义头。
 * 这对于跟踪客户端会话非常有用。
 * 订阅此事件的组件可以使用SimpMessageHeaderAccessor或StompMessageHeaderAccessor包装所包含的消息.
 *
 * @author Wang
 * @since 1.0.0
 */
//@Component
//@ConditionalOnBean(StompServerConfig.class)
public class StompSessionConnectEvent implements ApplicationListener<SessionConnectEvent> {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(StompSessionConnectEvent.class);

    /**
     * On application event.
     *
     * @param event the event
     */
    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        logger.debug("SessionConnectEvent");
    }
}
