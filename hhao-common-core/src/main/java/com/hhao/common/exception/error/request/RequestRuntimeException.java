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

package com.hhao.common.exception.error.request;

import com.hhao.common.exception.DefaultErrorCodes;
import com.hhao.common.exception.ErrorCode;
import com.hhao.common.exception.error.BusinessRuntimeException;

/**
 * 客户端请求错误
 *
 * @author Wang
 * @since 1.0.0
 */
public class RequestRuntimeException extends BusinessRuntimeException {
    /**
     * Instantiates a new Request exception.
     *
     * @param message the message
     */
    public RequestRuntimeException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Request exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public RequestRuntimeException(String message, Throwable cause) {
        super(message,cause);
    }

    /**
     * Instantiates a new Request exception.
     *
     * @param message the message
     * @param args    the args
     */
    public RequestRuntimeException(String message, Object [] args) {
        super(message,args);
    }

    /**
     * Instantiates a new Request exception.
     *
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public RequestRuntimeException(String message, Throwable cause, Object [] args) {
        super(message,cause,args);
    }

    /**
     * Instantiates a new Request exception.
     *
     * @param code    the code
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public RequestRuntimeException(String code, String message, Throwable cause, Object [] args) {
        super(code,message,cause,args);
    }

    public RequestRuntimeException(String code,String message,Object [] args) {
        super(code,message,args);
    }

    public RequestRuntimeException(String code,String message) {
        super(code,message);
    }

    /**
     * Instantiates a new Request exception.
     *
     * @param errorCode the error info
     * @param cause     the cause
     */
    public RequestRuntimeException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    /**
     * Instantiates a new Request exception.
     *
     * @param errorCode the error info
     */
    public RequestRuntimeException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Instantiates a new Request exception.
     *
     * @param cause the cause
     */
    public RequestRuntimeException(Throwable cause) {
        super(DefaultErrorCodes.ERROR_40X,cause);
    }

    /**
     * Instantiates a new Request exception.
     */
    public RequestRuntimeException() {
        super(DefaultErrorCodes.ERROR_40X);
    }
}
