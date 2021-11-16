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

package com.hhao.common.springboot.exception.entity.request;

import com.hhao.common.springboot.exception.ErrorInfo;
import com.hhao.common.springboot.exception.ErrorInfos;

/**
 * 客户端请求错误
 *
 * @author Wang
 * @since 1.0.0
 */
public class RequestException extends AbstractRequestRuntimeException {
    /**
     * Instantiates a new Request exception.
     *
     * @param errorInfo the error info
     * @param cause     the cause
     */
    public RequestException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }

    /**
     * Instantiates a new Request exception.
     *
     * @param errorInfo the error info
     */
    public RequestException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    /**
     * Instantiates a new Request exception.
     *
     * @param cause the cause
     */
    public RequestException(Throwable cause) {
        super(ErrorInfos.ERROR_40X,cause);
    }

    /**
     * Instantiates a new Request exception.
     */
    public RequestException() {
        super(ErrorInfos.ERROR_40X);
    }
}
