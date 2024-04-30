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
import org.springframework.web.socket.messaging.SessionConnectedEvent;

/**
 * 当代理发送了一个STOMP CONNECTED帧来响应CONNECT时，在SessionConnectEvent之后不久发布。
 * 此时，可以认为STOMP会话已完全建立。
 *
 * @author Wang
 * @since 1.0.0
 */
//@Component
//@ConditionalOnBean(StompServerConfig.class)
public class StompSessionConnectedEvent implements ApplicationListener<SessionConnectedEvent> {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(StompSessionConnectedEvent.class);

    /**
     * On application event.
     *
     * @param event the event
     */
    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        logger.debug("SessionConnectedEvent");
    }
}
