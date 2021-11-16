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

package com.hhao.common.springboot.web.config.websocket.stomp.server.even;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;

/**
 * 指示代理何时可用或不可用。
 * 虽然“简单”代理在启动时立即可用，并在应用程序运行时保持可用，但STOMP“代理中继”可能会失去与功能完整的代理的连接(例如，如果代理重新启动)。
 * 代理中继具有重新连接逻辑，并在代理返回时重新建立到代理的“系统”连接。
 * 因此，每当状态从连接变为断开时，都会发布此事件，反之亦然。使用SimpMessagingTemplate的组件应该订阅此事件
 * <p>
 * 当您使用一个功能完整的代理时，如果代理暂时不可用，STOMP“代理中继”会自动重新连接“系统”连接。
 * 然而，客户端连接不会自动重新连接。假设启用了心跳，客户端通常会注意到代理在10秒内没有响应。
 * 客户端需要实现自己的重新连接逻辑。
 *
 * @author Wang
 * @since 1.0.0
 */
//@Component
//@ConditionalOnBean(StompServerConfig.class)
public class StompBrokerAvailabilityEvent implements ApplicationListener<BrokerAvailabilityEvent> {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(StompBrokerAvailabilityEvent.class);

    /**
     * On application event.
     *
     * @param event the event
     */
    @Override
    public void onApplicationEvent(BrokerAvailabilityEvent event) {
        logger.debug("BrokerAvailabilityEvent");


    }
}
