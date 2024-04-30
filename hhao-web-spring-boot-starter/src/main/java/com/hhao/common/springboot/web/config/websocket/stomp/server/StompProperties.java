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

package com.hhao.common.springboot.web.config.websocket.stomp.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The type Stomp properties.
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({StompProperties.class, StompProperties.SimpleBrokerProperties.class, StompProperties.StompBrokerRelayProperties.class})
@ConfigurationProperties("com.hhao.config.stomp")
public class StompProperties {
    private String[] endpoint = new String[]{"/stomp-endpoint"};

    private String applicationDestinationPrefixes = "/app";

    private String userDestinationPrefix = "/user/";

    private Boolean preservePublishOrder = false;

    private int messageSizeLimit = 64 * 1024;

    private int sendTimeLimit = 10 * 1000;

    private int sendBufferSizeLimit = 512 * 1024;

    private int timeToFirstMessage = 60000;

    private Boolean enableSimpleBroker = false;

    private Boolean enableStompBrokerRelay = false;

    private SimpleBrokerProperties simpleBroker;

    //注意，数组的名称要和属性名称一样
    private StompBrokerRelayProperties[] stompBrokerRelays;

    /**
     * Get endpoint string [ ].
     *
     * @return the string [ ]
     */
    public String[] getEndpoint() {
        return endpoint;
    }

    /**
     * Sets endpoint.
     *
     * @param endpoint the endpoint
     */
    public void setEndpoint(String[] endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Gets application destination prefixes.
     *
     * @return the application destination prefixes
     */
    public String getApplicationDestinationPrefixes() {
        return applicationDestinationPrefixes;
    }

    /**
     * Sets application destination prefixes.
     *
     * @param applicationDestinationPrefixes the application destination prefixes
     */
    public void setApplicationDestinationPrefixes(String applicationDestinationPrefixes) {
        this.applicationDestinationPrefixes = applicationDestinationPrefixes;
    }

    /**
     * Gets enable simple broker.
     *
     * @return the enable simple broker
     */
    public Boolean getEnableSimpleBroker() {
        return enableSimpleBroker;
    }

    /**
     * Sets enable simple broker.
     *
     * @param enableSimpleBroker the enable simple broker
     */
    public void setEnableSimpleBroker(Boolean enableSimpleBroker) {
        this.enableSimpleBroker = enableSimpleBroker;
    }

    /**
     * Gets simple broker.
     *
     * @return the simple broker
     */
    public SimpleBrokerProperties getSimpleBroker() {
        return simpleBroker;
    }

    /**
     * Sets simple broker.
     *
     * @param simpleBroker the simple broker
     */
    @Autowired
    public void setSimpleBroker(SimpleBrokerProperties simpleBroker) {
        this.simpleBroker = simpleBroker;
    }

    /**
     * Gets enable stomp broker relay.
     *
     * @return the enable stomp broker relay
     */
    public Boolean getEnableStompBrokerRelay() {
        return enableStompBrokerRelay;
    }

    /**
     * Sets enable stomp broker relay.
     *
     * @param enableStompBrokerRelay the enable stomp broker relay
     */
    public void setEnableStompBrokerRelay(Boolean enableStompBrokerRelay) {
        this.enableStompBrokerRelay = enableStompBrokerRelay;
    }

    /**
     * Get stomp broker relays stomp broker relay properties [ ].
     *
     * @return the stomp broker relay properties [ ]
     */
    public StompBrokerRelayProperties[] getStompBrokerRelays() {
        return stompBrokerRelays;
    }

    /**
     * Sets stomp broker relays.
     *
     * @param stompBrokerRelays the stomp broker relays
     */
    @Autowired
    public void setStompBrokerRelays(StompBrokerRelayProperties[] stompBrokerRelays) {
        this.stompBrokerRelays = stompBrokerRelays;
    }

    /**
     * Gets user destination prefix.
     *
     * @return the user destination prefix
     */
    public String getUserDestinationPrefix() {
        return userDestinationPrefix;
    }

    /**
     * Sets user destination prefix.
     *
     * @param userDestinationPrefix the user destination prefix
     */
    public void setUserDestinationPrefix(String userDestinationPrefix) {
        this.userDestinationPrefix = userDestinationPrefix;
    }

    /**
     * Gets preserve publish order.
     *
     * @return the preserve publish order
     */
    public Boolean getPreservePublishOrder() {
        return preservePublishOrder;
    }

    /**
     * Sets preserve publish order.
     *
     * @param preservePublishOrder the preserve publish order
     */
    public void setPreservePublishOrder(Boolean preservePublishOrder) {
        this.preservePublishOrder = preservePublishOrder;
    }

    /**
     * Gets message size limit.
     *
     * @return the message size limit
     */
    public int getMessageSizeLimit() {
        return messageSizeLimit;
    }

    /**
     * Sets message size limit.
     *
     * @param messageSizeLimit the message size limit
     */
    public void setMessageSizeLimit(int messageSizeLimit) {
        this.messageSizeLimit = messageSizeLimit;
    }

    /**
     * Gets send time limit.
     *
     * @return the send time limit
     */
    public int getSendTimeLimit() {
        return sendTimeLimit;
    }

    /**
     * Sets send time limit.
     *
     * @param sendTimeLimit the send time limit
     */
    public void setSendTimeLimit(int sendTimeLimit) {
        this.sendTimeLimit = sendTimeLimit;
    }

    /**
     * Gets send buffer size limit.
     *
     * @return the send buffer size limit
     */
    public int getSendBufferSizeLimit() {
        return sendBufferSizeLimit;
    }

    /**
     * Sets send buffer size limit.
     *
     * @param sendBufferSizeLimit the send buffer size limit
     */
    public void setSendBufferSizeLimit(int sendBufferSizeLimit) {
        this.sendBufferSizeLimit = sendBufferSizeLimit;
    }

    /**
     * Gets time to first message.
     *
     * @return the time to first message
     */
    public int getTimeToFirstMessage() {
        return timeToFirstMessage;
    }

    /**
     * Sets time to first message.
     *
     * @param timeToFirstMessage the time to first message
     */
    public void setTimeToFirstMessage(int timeToFirstMessage) {
        this.timeToFirstMessage = timeToFirstMessage;
    }

    /**
     * The type Simple broker properties.
     */
    @ConfigurationProperties("com.hhao.config.stomp.simple-broker")
    public static class SimpleBrokerProperties {
        private String[] destinationPrefixes = new String[]{"/topic", "/queue"};
        private long[] heartbeatValue = new long[]{10000, 10000};

        /**
         * Get destination prefixes string [ ].
         *
         * @return the string [ ]
         */
        public String[] getDestinationPrefixes() {
            return destinationPrefixes;
        }

        /**
         * Sets destination prefixes.
         *
         * @param destinationPrefixes the destination prefixes
         */
        public void setDestinationPrefixes(String[] destinationPrefixes) {
            this.destinationPrefixes = destinationPrefixes;
        }

        /**
         * Get heartbeat value long [ ].
         *
         * @return the long [ ]
         */
        public long[] getHeartbeatValue() {
            return heartbeatValue;
        }

        /**
         * Sets heartbeat value.
         *
         * @param heartbeatValue the heartbeat value
         */
        public void setHeartbeatValue(long[] heartbeatValue) {
            this.heartbeatValue = heartbeatValue;
        }
    }

    /**
     * The type Stomp broker relay properties.
     */
    @ConfigurationProperties("com.hhao.config.stomp.stomp-broker-relay")
    public static class StompBrokerRelayProperties {
        private String[] destinationPrefixes = new String[]{"/topic", "/queue"};

        private String relayHost = "";
        private Integer relayPort = 0;
        private String systemLogin = "guest";
        private String systemPasscode = "guest";
        private String clientLogin = "guest";
        private String clientPasscode = "guest";

        /**
         * Get destination prefixes string [ ].
         *
         * @return the string [ ]
         */
        public String[] getDestinationPrefixes() {
            return destinationPrefixes;
        }

        /**
         * Sets destination prefixes.
         *
         * @param destinationPrefixes the destination prefixes
         */
        public void setDestinationPrefixes(String[] destinationPrefixes) {
            this.destinationPrefixes = destinationPrefixes;
        }

        /**
         * Gets relay host.
         *
         * @return the relay host
         */
        public String getRelayHost() {
            return relayHost;
        }

        /**
         * Sets relay host.
         *
         * @param relayHost the relay host
         */
        public void setRelayHost(String relayHost) {
            this.relayHost = relayHost;
        }

        /**
         * Gets relay port.
         *
         * @return the relay port
         */
        public Integer getRelayPort() {
            return relayPort;
        }

        /**
         * Sets relay port.
         *
         * @param relayPort the relay port
         */
        public void setRelayPort(Integer relayPort) {
            this.relayPort = relayPort;
        }

        /**
         * Gets system login.
         *
         * @return the system login
         */
        public String getSystemLogin() {
            return systemLogin;
        }

        /**
         * Sets system login.
         *
         * @param systemLogin the system login
         */
        public void setSystemLogin(String systemLogin) {
            this.systemLogin = systemLogin;
        }

        /**
         * Gets system passcode.
         *
         * @return the system passcode
         */
        public String getSystemPasscode() {
            return systemPasscode;
        }

        /**
         * Sets system passcode.
         *
         * @param systemPasscode the system passcode
         */
        public void setSystemPasscode(String systemPasscode) {
            this.systemPasscode = systemPasscode;
        }

        /**
         * Gets client login.
         *
         * @return the client login
         */
        public String getClientLogin() {
            return clientLogin;
        }

        /**
         * Sets client login.
         *
         * @param clientLogin the client login
         */
        public void setClientLogin(String clientLogin) {
            this.clientLogin = clientLogin;
        }

        /**
         * Gets client passcode.
         *
         * @return the client passcode
         */
        public String getClientPasscode() {
            return clientPasscode;
        }

        /**
         * Sets client passcode.
         *
         * @param clientPasscode the client passcode
         */
        public void setClientPasscode(String clientPasscode) {
            this.clientPasscode = clientPasscode;
        }
    }
}
