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

package com.hhao.common.springboot.web.config.websocket;


import com.hhao.common.exception.error.request.AuthorizeException;
import com.hhao.common.security.AbstractUser;

/**
 * The interface Authorization.
 *
 * @author Wang
 * @since 1.0.0
 */
public interface Authorization {
    /**
     * 传处token
     * 如果正确，则返回Principal
     * 否则，抛出AuthorizeException
     *
     * @param token the token
     * @return user
     * @throws AuthorizeException the authorize exception
     */
    AbstractUser authorize(String token) throws AuthorizeException;
}
