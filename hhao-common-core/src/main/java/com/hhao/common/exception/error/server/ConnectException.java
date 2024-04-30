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

import com.hhao.common.exception.DefaultErrorCodes;
import com.hhao.common.exception.ErrorCode;


/**
 * 连接错误
 *
 * @author Wang
 * @since 1.0.0
 */
public class ConnectException  extends ServerRuntimeException {
    /**
     * Instantiates a new Connect exception.
     *
     * @param message the message
     */
    public ConnectException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Connect exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ConnectException(String message,Throwable cause) {
        super(message,cause);
    }

    /**
     * Instantiates a new Connect exception.
     *
     * @param message the message
     * @param args    the args
     */
    public ConnectException(String message,Object [] args) {
        super(message,args);
    }

    /**
     * Instantiates a new Connect exception.
     *
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public ConnectException(String message,Throwable cause,Object [] args) {
        super(message,cause,args);
    }

    /**
     * Instantiates a new Connect exception.
     *
     * @param code    the code
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public ConnectException(String code,String message,Throwable cause,Object [] args) {
        super(code,message,cause,args);
    }

    /**
     * Instantiates a new Connect exception.
     *
     * @param errorCode the error info
     * @param cause     the cause
     */
    public ConnectException( ErrorCode errorCode, Throwable cause) {
        super(errorCode,cause);
    }

    /**
     * Instantiates a new Connect exception.
     *
     * @param errorCode the error info
     */
    public ConnectException( ErrorCode errorCode) {
        this(errorCode,null);
    }

    /**
     * Instantiates a new Connect exception.
     */
    public ConnectException() {
        this(DefaultErrorCodes.ERROR_500_CONNECT);
    }

    /**
     * Instantiates a new Connect exception.
     *
     * @param cause the cause
     */
    public ConnectException(Throwable cause) {
        this(DefaultErrorCodes.ERROR_500_CONNECT,cause);
    }
}
