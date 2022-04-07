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


import com.hhao.common.exception.error.request.AuthorizeException;
import com.hhao.common.security.AbstractUser;
import com.hhao.common.springboot.web.config.websocket.Authorization;
import com.hhao.common.utils.Assert;
import com.hhao.common.utils.StringUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.ArrayList;
import java.util.Map;

/**
 * 拦截器，用于连接时安全认证
 * 也可以用ExecutorChannelInterceptor
 *
 * @author Wang
 * @since 1.0.0
 */
public class DefaultChannelInterceptor implements ChannelInterceptor {
    private Authorization authorization=null;
    private static final String TOKEN_NAME="token";

    /**
     * Instantiates a new Default channel interceptor.
     *
     * @param authorization the authorization
     */
    public DefaultChannelInterceptor(Authorization authorization){
        this.authorization=authorization;
    }

    /**
     * nativeHeaders这里存放了header中的信息
     * login,passcode,client-id,accept-version,heart-beat,token
     * @param messageHeaders
     * @return
     */
    private Map<String, Object> getNativeHeaders(MessageHeaders messageHeaders){
        return (Map<String, Object>)messageHeaders.get("nativeHeaders");
    }

    /**
     * simpSessionAttributes这里存放了cookie中的信息
     * token
     * @param messageHeaders
     * @return
     */
    private Map<String, Object> getSimpSessionAttributes(MessageHeaders messageHeaders){
        return (Map<String, Object>)messageHeaders.get("simpSessionAttributes");
    }

    /**
     * 返回token信息，按以下顺序查找
     * 1、header
     * 2、cookie
     * @param messageHeaders
     * @return
     */
    private String getToken(MessageHeaders messageHeaders){
        Assert.notNull(messageHeaders,"NativeHeaders must not null");

        String token="";
        //1、header
        Map<String, Object> headerValues=this.getNativeHeaders(messageHeaders);
        if (headerValues!=null){
            ArrayList value=(ArrayList) headerValues.get(TOKEN_NAME);
            if (value!=null){
                token=(String)value.get(0);
            }
        }
        if (StringUtils.hasLength(token)){
            return token;
        }
        //2.cookie
        headerValues=this.getSimpSessionAttributes(messageHeaders);
        if (headerValues!=null){
            ArrayList value=(ArrayList) headerValues.get(TOKEN_NAME);
            if (value!=null){
                token=(String)value.get(0);
            }
        }
        return token;
    }

    /**
     * Pre send message.
     *
     * @param message the message
     * @param channel the channel
     * @return the message
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token=this.getToken(accessor.toMessageHeaders());
            if (!StringUtils.hasLength(token)){
                throw new AuthorizeException();
            }
            AbstractUser user=authorization.authorize(token);
            accessor.setUser(user);
        }
        return message;
    }

    /**
     * Post send.
     *
     * @param message the message
     * @param channel the channel
     * @param sent    the sent
     */
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        ChannelInterceptor.super.postSend(message, channel, sent);
    }

    /**
     * After send completion.
     *
     * @param message the message
     * @param channel the channel
     * @param sent    the sent
     * @param ex      the ex
     */
    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        ChannelInterceptor.super.afterSendCompletion(message, channel, sent, ex);
    }

    /**
     * Pre receive boolean.
     *
     * @param channel the channel
     * @return the boolean
     */
    @Override
    public boolean preReceive(MessageChannel channel) {
        return ChannelInterceptor.super.preReceive(channel);
    }

    /**
     * Post receive message.
     *
     * @param message the message
     * @param channel the channel
     * @return the message
     */
    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        return ChannelInterceptor.super.postReceive(message, channel);
    }

    /**
     * After receive completion.
     *
     * @param message the message
     * @param channel the channel
     * @param ex      the ex
     */
    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        ChannelInterceptor.super.afterReceiveCompletion(message, channel, ex);
    }
}
