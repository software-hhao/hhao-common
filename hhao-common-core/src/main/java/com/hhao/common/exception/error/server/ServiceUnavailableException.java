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

package com.hhao.common.exception.error.server;

import com.hhao.common.exception.ErrorCode;
import com.hhao.common.exception.DefaultErrorCodes;

/**
 * 系统繁忙错误，如系统被降级、限流
 *
 * @author Wang
 * @since 2022 /1/6 17:06
 */
public class ServiceUnavailableException extends ServerRuntimeException {
    /**
     * Instantiates a new Service unavailable exception.
     *
     * @param message the message
     */
    public ServiceUnavailableException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Service unavailable exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ServiceUnavailableException(String message,Throwable cause) {
        super(message,cause);
    }

    /**
     * Instantiates a new Service unavailable exception.
     *
     * @param message the message
     * @param args    the args
     */
    public ServiceUnavailableException(String message,Object [] args) {
        super(message,args);
    }

    /**
     * Instantiates a new Service unavailable exception.
     *
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public ServiceUnavailableException(String message,Throwable cause,Object [] args) {
        super(message,cause,args);
    }

    /**
     * Instantiates a new Service unavailable exception.
     *
     * @param code    the code
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public ServiceUnavailableException(String code,String message,Throwable cause,Object [] args) {
        super(code,message,cause,args);
    }

    /**
     * Instantiates a new System busy runtime exception.
     *
     * @param errorCode the error info
     * @param cause     the cause
     */
    public ServiceUnavailableException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    /**
     * Instantiates a new System busy runtime exception.
     *
     * @param errorCode the error info
     */
    public ServiceUnavailableException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Instantiates a new System busy runtime exception.
     *
     * @param cause the cause
     */
    public ServiceUnavailableException(Throwable cause) {
        super(DefaultErrorCodes.ERROR_503, cause);
    }

    /**
     * Instantiates a new System busy runtime exception.
     */
    public ServiceUnavailableException() {
        super(DefaultErrorCodes.ERROR_503);
    }
}
