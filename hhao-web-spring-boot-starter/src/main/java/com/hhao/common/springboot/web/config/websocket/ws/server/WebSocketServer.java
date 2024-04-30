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

package com.hhao.common.springboot.web.config.websocket.ws.server;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * websocket服务处理执行器注解
 *
 * @author Wang
 * @since 1.0.0
 */
@Target({TYPE})
@Retention(RUNTIME)
@Documented
@Component
public @interface WebSocketServer {
    /**
     * 提供的服务端口uri
     *
     * @return string [ ]
     */
    @AliasFor("paths")
    String[] value() default {};

    /**
     * 同上
     *
     * @return string [ ]
     */
    @AliasFor("value")
    String[] paths() default {};

    /**
     * 跨域设置
     *
     * @return string [ ]
     */
    String[] origins() default {};
}
