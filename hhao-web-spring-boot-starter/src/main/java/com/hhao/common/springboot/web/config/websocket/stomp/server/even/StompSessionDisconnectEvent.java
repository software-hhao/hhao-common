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
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * 当STOMP会话结束时发布。
 * DISCONNECT可能是从客户端发送的，也可能是在WebSocket会话关闭时自动生成的。
 * 在某些情况下，每个会话发布此事件不止一次。对于多个断开事件，组件应该是幂等的。
 *
 * @author Wang
 * @since 1.0.0
 */
//@Component
//@ConditionalOnBean(StompServerConfig.class)
public class StompSessionDisconnectEvent implements ApplicationListener<SessionDisconnectEvent> {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(StompSessionDisconnectEvent.class);

    /**
     * On application event.
     *
     * @param event the event
     */
    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        logger.debug("SessionDisconnectEvent");
    }
}
