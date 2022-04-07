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

package com.hhao.common.exception.error.server;

import com.hhao.common.exception.ErrorInfo;
import com.hhao.common.exception.ErrorInfos;
import com.hhao.common.lang.NonNull;


/**
 * 服务业务异常
 *
 * @author Wang
 * @since 1.0.0
 */
public class ServerException extends AbstractServerRuntimeException {
    /**
     * Instantiates a new Server exception.
     *
     * @param message the message
     */
    public ServerException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Server exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ServerException(String message, Throwable cause) {
        super(message,cause);
    }

    /**
     * Instantiates a new Server exception.
     *
     * @param code    the code
     * @param message the message
     * @param cause   the cause
     */
    public ServerException(String code,String message, Throwable cause) {
        super(code,message,cause);
    }

    /**
     * Instantiates a new Server exception.
     *
     * @param errorInfo the error info
     * @param cause     the cause
     */
    public ServerException(@NonNull ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo,cause);
    }

    /**
     * Instantiates a new Server exception.
     *
     * @param errorInfo the error info
     */
    public ServerException(@NonNull ErrorInfo errorInfo) {
        this(errorInfo,null);
    }

    /**
     * Instantiates a new Server exception.
     */
    public ServerException() {
        this(ErrorInfos.ERROR_500);
    }

    /**
     * Instantiates a new Server exception.
     *
     * @param cause the cause
     */
    public ServerException(Throwable cause) {
        this(ErrorInfos.ERROR_500,cause);
    }
}