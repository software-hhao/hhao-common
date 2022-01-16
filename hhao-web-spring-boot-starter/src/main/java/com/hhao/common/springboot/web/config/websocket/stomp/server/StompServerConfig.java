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

package com.hhao.common.springboot.web.config.websocket.stomp.server;

import com.hhao.common.springboot.web.config.websocket.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * STOMP 的消息根据前缀的不同分为三种。如下，以 /app 开头的消息都会被路由到带有{@code @MessageMapping} 或 {@code @SubscribeMapping} 注解的方法中；以/topic 或 /queue 开头的消息都会发送到STOMP代理中，根据你所选择的STOMP代理不同，目的地的可选前缀也会有所限制；以/user开头的消息会将消息重路由到某个用户独有的目的地上。
 * Spring Security提供WebSocket子协议授权，使用ChannelInterceptor基于消息中的用户头对消息进行授权。此外，Spring Session还提供了一个WebSocket集成，以确保当WebSocket会话仍处于活动状态时，用户HTTP会话不会过期。
 * DelegatingWebSocketMessageBrokerConfiguration
 * AbstractMessageBrokerConfiguration
 * WebSocketAnnotationMethodMessageHandler
 * WebSocketMessageBrokerConfigurationSupport
 *
 *  配置多地址
 *  <pre>{@code
 *
 * @Override
 * public void configureMessageBroker(MessageBrokerRegistry registry) {
 * 	registry.enableStompBrokerRelay("/queue/", "/topic/").setTcpClient(createTcpClient());
 * 	registry.setApplicationDestinationPrefixes("/app");
 * }
 *
 * private ReactorNettyTcpClient<byte[]> createTcpClient() {
 *        return new ReactorNettyTcpClient<>(tcpClient -> {
 *             return tcpClient.addressSupplier(() -> {});
 *         },new StompReactorNettyCodec());
 * }
 *
 * @Bean
 * public DefaultHandshakeHandler handshakeHandler() {
 *         WebSocketPolicy policy = new WebSocketPolicy(WebSocketBehavior.SERVER);
 *         policy.setInputBufferSize(8192);
 *         policy.setIdleTimeout(600000);
 *
 *         return new DefaultHandshakeHandler(
 *                 new JettyRequestUpgradeStrategy(new WebSocketServerFactory(policy)));
 * }
 *
 *  }
 *  </pre>
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSocketMessageBroker
//采用自己的token拦截验证，不经过Spring Security
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@ConditionalOnMissingBean(StompServerConfig.class)
@ConditionalOnClass({WebSocketMessageBrokerConfigurer.class})
@EnableConfigurationProperties({StompProperties.class,StompProperties.SimpleBrokerProperties.class,StompProperties.StompBrokerRelayProperties.class})
@ConditionalOnProperty(prefix = "com.hhao.config.stomp",name = "enable",havingValue = "true",matchIfMissing = false)
public class StompServerConfig implements WebSocketMessageBrokerConfigurer {
    //属性
    private StompProperties stompProperties;
    //任务调度器，用于心跳
    private TaskScheduler messageBrokerTaskScheduler;

    /**
     * Instantiates a new Stomp server config.
     *
     * @param taskScheduler   the task scheduler
     * @param stompProperties the stomp properties
     */
    @Autowired
    public StompServerConfig(@Qualifier("defaultSockJsTaskScheduler") TaskScheduler taskScheduler,StompProperties stompProperties) {
        this.messageBrokerTaskScheduler = taskScheduler;
        this.stompProperties=stompProperties;
    }

    /**
     * Register stomp endpoints.
     *
     * @param registry the registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //是一个WebSocket(或SockJS)客户端在进行WebSocket握手时需要连接到的端点的HTTP URL
        //默认值:/stomp-endpoint
        registry.addEndpoint(stompProperties.getEndpoint())
                .withSockJS();
    }

    /**
     * Configure message broker.
     *
     * @param config the config
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //发往应用程序的消息将会带有“/app”前缀
        //目的地头以/app开头的STOMP消息被路由到@Controller类中的@MessageMapping或@SubscribeMapping方法。
        config.setApplicationDestinationPrefixes(stompProperties.getApplicationDestinationPrefixes());

        // 应用程序可以发送以特定用户为目标的消息，Spring的STOMP支持为此目的识别以/user/为前缀的目的地。
        // 例如，客户机可能订阅/user/queue/position-updates目的地。
        // UserDestinationMessageHandler处理这个目的地，并将其转换为用户会话唯一的目的地(例如/queue/position-updates-user123)。
        // 这提供了订阅通用命名目的地的便利，同时确保与订阅同一目的地的其他用户不发生冲突，以便每个用户都使用
        // 在使用用户目的地时，重要的是配置代理和应用程序目的地前缀，如Enable STOMP所示，否则代理将处理“/user”前缀的消息，这些消息应该只由UserDestinationMessageHandler处理。
        // 在发送端，消息可以发送到目的地，如/user/{username}/queue/position-updates，然后UserDestinationMessageHandler将其转换为一个或多个目的地，每个目的地对应与用户相关的会话。这使得应用程序中的任何组件都可以发送以特定用户为目标的消息，而不必知道用户的名称和通用目的地以外的任何信息。这也可以通过注释和消息传递模板来支持。
        //当您与外部消息代理一起使用用户目的地时，您应该检查代理文档中关于如何管理非活动队列的内容，以便在用户会话结束时，删除所有唯一的用户队列。例如，当你使用/exchange/amq.direct/position-updates这样的目的地时，RabbitMQ会创建自动删除队列。因此，在这种情况下，客户端可以订阅/user/exchange/amq.direct/position-updates。类似地，ActiveMQ有用于清除不活动目的地的配置选项。
        config.setUserDestinationPrefix(stompProperties.getUserDestinationPrefix());

        // 开启后，同一客户端会话中的消息将一次一个地发布到clientOutboundChannel，以保证发布的顺序。
        // 注意，这只会带来性能开销，所以您应该只在需要时启用它。
        config.setPreservePublishOrder(stompProperties.getPreservePublishOrder());

        //启用了STOMP代理中继（broker relay）功能，Spring将所有指定目的地址前缀的消息都会发送到STOMP代理中。
        //启用内置简单消息代理，采用内存代理，将目标头以/topic '或' /queue开头的消息路由到代理。
        //简单代理适合入门，但只支持STOMP命令的一个子集(它不支持ack、收据和其他一些特性)，依赖于一个简单的消息发送循环，不适合集群。
        if (stompProperties.getEnableSimpleBroker()) {
            config.enableSimpleBroker(stompProperties.getSimpleBroker().getDestinationPrefixes())
                    .setHeartbeatValue(stompProperties.getSimpleBroker().getHeartbeatValue())
                    .setTaskScheduler(this.messageBrokerTaskScheduler);
        }

        //启用Stomp代理中继
        //可以注册多个代理中继
        //Stomp代理中继是一个Spring MessageHandler，它通过将消息转发给外部消息代理来处理消息。为此，它建立与代理的TCP连接，将所有消息转发给代理，然后通过客户端WebSocket会话将从代理接收到的所有消息转发给客户端。本质上，它充当一个“中继”，向两个方向转发消息。
        //STOMP代理中继维护到代理的单个“系统”TCP连接。此连接仅用于来自服务器端应用程序的消息，而不用于接收消息。您可以为这个连接配置STOMP凭据。systemLogin和systemPasscode属性，其默认值为guest和guest。
        //STOMP代理中继还为每个连接的WebSocket客户端创建一个单独的TCP连接。您可以配置用于代表客户端创建的所有TCP连接的STOMP凭据。clientLogin and clientPasscode,默认值guest and guest
        //STOMP代理中继总是在它代表客户端转发给代理的每个CONNECT帧上设置login and passcode。因此，WebSocket客户端不需要设置这些头信息。
        //STOMP代理中继还通过“系统”TCP连接向消息代理发送和接收心跳。您可以配置发送和接收心跳的时间间隔(默认为10秒)。如果与代理的连接丢失，代理中继将继续尝试重新连接，每5秒一次，直到成功为止。
        //任何Spring bean都可以实现ApplicationListener<BrokerAvailabilityEvent>，以便在与代理的“系统”连接丢失并重新建立时接收通知。
        //默认情况下，STOMP代理中继总是连接到相同的主机和端口，如果连通性丢失，还会根据需要重新连接。如果您希望提供多个地址，在每次尝试连接时，您可以配置一个地址供应商，而不是一个固定的主机和端口。
        //您还可以使用virtualHost属性配置STOMP代理中继。此属性的值被设置为每个CONNECT帧的主机报头，并且可能很有用(例如，在云环境中，建立TCP连接的实际主机与提供基于云的STOMP服务的主机不同)。

        if (stompProperties.getEnableStompBrokerRelay()) {
            for(StompProperties.StompBrokerRelayProperties brokerRelayPropertie: stompProperties.getStompBrokerRelays()){
                config.enableStompBrokerRelay(brokerRelayPropertie.getDestinationPrefixes())
                        .setRelayHost(brokerRelayPropertie.getRelayHost())
                        .setRelayPort(brokerRelayPropertie.getRelayPort())
                        .setSystemLogin(brokerRelayPropertie.getSystemLogin())
                        .setSystemPasscode(brokerRelayPropertie.getSystemPasscode())
                        .setClientLogin(brokerRelayPropertie.getClientLogin())
                        .setClientPasscode(brokerRelayPropertie.getClientPasscode());
            }
        }
    }

    /**
     * 请注意，目前当您对消息使用Spring Security的授权时，您需要确保身份验证ChannelInterceptor配置的顺序在Spring Security之前。
     * 这最好通过在WebSocketMessageBrokerConfigurer自己的实现中声明自定义拦截器来实现，该实现被标记为@Order(Ordered。HIGHEST_PRECEDENCE + 99)。
     *
     * @param registration the registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(
                new DefaultChannelInterceptor(authorization()),
                new DefaultExecutorChannelInterceptor()
        );
    }

    /**
     * Configure web socket transport.
     *
     * @param registry the registry
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(stompProperties.getMessageSizeLimit());
        registry.setSendTimeLimit(stompProperties.getSendTimeLimit());
        registry.setSendBufferSizeLimit(stompProperties.getSendBufferSizeLimit());
        registry.setTimeToFirstMessage(stompProperties.getTimeToFirstMessage());
    }

    /**
     * Authorization authorization.
     *
     * @return the authorization
     */
    protected Authorization authorization(){
        return new DefaultAuthorization();
    }
}
