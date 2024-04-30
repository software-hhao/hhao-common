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

import com.hhao.common.springboot.response.ResultWrapper;
import com.hhao.common.springboot.response.ResultWrapperBuilder;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 全局的异常捕获
 * 订阅路径是：/user/queue/errors
 *
 * @author Wang
 * @since 1.0.0
 */
@ControllerAdvice
public class ErrorAdvise {
    /**
     * 处理来自@MessageMapping方法的异常
     * 支持与@MessageMapping方法相同的方法参数类型和返回值。
     * 可以在一个用@ControllerAdvice标记的类中声明它们,可以跨控制器使用
     * 如果用户有多个会话，默认情况下，目标是订阅到给定目标的所有会话。然而，有时可能需要只针对发送正在处理的消息的会话。可以将broadcast属性设置为false，
     *
     * @param throwable the throwable
     * @return result wrapper
     */
    @MessageExceptionHandler
    @SendToUser(destinations = "/queue/errors", broadcast = false)
    public ResultWrapper<Object> handException(Throwable throwable) {
        return ResultWrapperBuilder.error(throwable.getMessage(), throwable);
    }
}
