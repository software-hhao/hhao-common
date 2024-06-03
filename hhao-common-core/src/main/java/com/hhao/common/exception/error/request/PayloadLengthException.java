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

/**
 * 请求内容(body)长度超限
 *
 * @author Wang
 * @since 1.0.0
 */
public class PayloadLengthException extends RequestRuntimeException {
    /**
     * Instantiates a new Payload length exception.
     *
     * @param message the message
     */
    public PayloadLengthException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Payload length exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public PayloadLengthException(String message,Throwable cause) {
        super(message,cause);
    }

    /**
     * Instantiates a new Payload length exception.
     *
     * @param message the message
     * @param args    the args
     */
    public PayloadLengthException(String message,Object [] args) {
        super(message,args);
    }

    /**
     * Instantiates a new Payload length exception.
     *
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public PayloadLengthException(String message,Throwable cause,Object [] args) {
        super(message,cause,args);
    }

    /**
     * Instantiates a new Payload length exception.
     *
     * @param code    the code
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public PayloadLengthException(String code,String message,Throwable cause,Object [] args) {
        super(code,message,cause,args);
    }

    public PayloadLengthException(String code,String message,Object [] args) {
        super(code,message,args);
    }

    public PayloadLengthException(String code,String message) {
        super(code,message);
    }

    /**
     * Instantiates a new Payload length exception.
     *
     * @param errorCode the error info
     */
    public PayloadLengthException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Instantiates a new Payload length exception.
     *
     * @param contentCacheLimit the content cache limit
     */
    public PayloadLengthException(Integer contentCacheLimit) {
        super(DefaultErrorCodes.ERROR_400_PAYLOAD_LENGTH.applyArgs(new Object[]{contentCacheLimit}));
    }
}
