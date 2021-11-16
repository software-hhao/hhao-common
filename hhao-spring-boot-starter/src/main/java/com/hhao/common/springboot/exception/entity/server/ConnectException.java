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

package com.hhao.common.springboot.exception.entity.server;

import com.hhao.common.springboot.exception.ErrorInfo;
import com.hhao.common.springboot.exception.ErrorInfoBuilder;

import javax.validation.constraints.NotNull;

/**
 * 连接错误
 *
 * @author Wang
 * @since 1.0.0
 */
public class ConnectException  extends AbstractServerRuntimeException {
    /**
     * The constant ERROR_500_CONNECTION.
     */
    public static final ErrorInfo ERROR_500_CONNECTION = ErrorInfoBuilder.build("500.100", "error.code.500.100");

    /**
     * Instantiates a new Connect exception.
     *
     * @param errorInfo the error info
     * @param cause     the cause
     */
    public ConnectException(@NotNull ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo,cause);
    }

    /**
     * Instantiates a new Connect exception.
     *
     * @param errorInfo the error info
     */
    public ConnectException(@NotNull ErrorInfo errorInfo) {
        this(errorInfo,null);
    }

    /**
     * Instantiates a new Connect exception.
     */
    public ConnectException() {
        this(ERROR_500_CONNECTION);
    }

    /**
     * Instantiates a new Connect exception.
     *
     * @param cause the cause
     */
    public ConnectException(Throwable cause) {
        this(ERROR_500_CONNECTION,cause);
    }
}
