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
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

/**
 * 当收到一个新的STOMP取消订阅时发布。
 *
 * @author Wang
 * @since 1.0.0
 */
//@Component
//@ConditionalOnBean(StompServerConfig.class)
public class StompSessionUnsubscribeEvent implements ApplicationListener<SessionUnsubscribeEvent> {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(StompSessionUnsubscribeEvent.class);

    /**
     * On application event.
     *
     * @param event the event
     */
    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent event) {
        logger.debug("SessionUnsubscribeEvent");
    }
}
