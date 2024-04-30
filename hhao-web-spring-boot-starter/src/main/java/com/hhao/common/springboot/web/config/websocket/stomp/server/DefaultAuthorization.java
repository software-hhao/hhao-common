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


import com.hhao.common.exception.error.request.AuthenticationException;
import com.hhao.common.security.AbstractUser;
import com.hhao.common.springboot.web.config.websocket.Authorization;
import com.hhao.common.utils.StringUtils;

/**
 * 安全认证
 *
 * @author Wang
 * @since 1.0.0
 */
public class DefaultAuthorization implements Authorization {
    @Override
    public AbstractUser authorize(String token) throws AuthenticationException {
        if (!StringUtils.hasLength(token)) {
            throw new AuthenticationException();
        }

        AbstractUser user = new AbstractUser() {
            @Override
            public String getName() {
                return token;
            }
        };
        return user;
    }
}
