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

package com.hhao.common.exception.error;

import com.hhao.common.exception.AbstractBaseRuntimeException;
import com.hhao.common.exception.DefaultErrorCodes;
import com.hhao.common.exception.ErrorCode;

/**
 * 业务类异常的根类
 * 有明确的业务语义，不需要记录Error日志，不需要Retry
 *
 * @author Wang
 * @since 1.0.0
 */
public class BusinessRuntimeException extends AbstractBaseRuntimeException {

    /**
     * Instantiates a new Abstract biz runtime exception.
     *
     * @param message the message
     */
    public BusinessRuntimeException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Abstract biz runtime exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public BusinessRuntimeException(String message, Throwable cause) {
        super(message,cause);
    }

    /**
     * Instantiates a new Abstract biz runtime exception.
     *
     * @param message the message
     * @param args    the args
     */
    public BusinessRuntimeException(String message, Object [] args) {
        super(message,args);
    }

    /**
     * Instantiates a new Abstract biz runtime exception.
     *
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public BusinessRuntimeException(String message, Throwable cause, Object [] args) {
        super(message,cause,args);
    }

    /**
     * Instantiates a new Abstract biz runtime exception.
     *
     * @param code    the code
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public BusinessRuntimeException(String code, String message, Throwable cause, Object [] args) {
        super(code,message,cause,args);
    }

    public BusinessRuntimeException(String code,String message,Object [] args) {
        super(code,message,args);
    }

    public BusinessRuntimeException(String code,String message) {
        super(code,message);
    }

    /**
     * Instantiates a new Abstract biz runtime exception.
     *
     * @param errorCode the error info
     * @param cause     the cause
     */
    public BusinessRuntimeException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    /**
     * Instantiates a new Abstract biz runtime exception.
     *
     * @param errorCode the error info
     */
    public BusinessRuntimeException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BusinessRuntimeException(Throwable cause) {
        super(DefaultErrorCodes.ERROR_998,cause);
    }
}
