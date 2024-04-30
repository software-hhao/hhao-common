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
import com.hhao.common.exception.ErrorCode;

/**
 * 系统类错误，需要记录Error日志，需要Retry
 *
 * @author Wang
 * @since 1.0.0
 */
public class SystemRuntimeException extends AbstractBaseRuntimeException {
    /**
     * Instantiates a new Abstract sys runtime exception.
     *
     * @param message the message
     */
    public SystemRuntimeException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Abstract sys runtime exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public SystemRuntimeException(String message, Throwable cause) {
        super(message,cause);
    }

    /**
     * Instantiates a new Abstract sys runtime exception.
     *
     * @param message the message
     * @param args    the args
     */
    public SystemRuntimeException(String message, Object [] args) {
        super(message,args);
    }

    /**
     * Instantiates a new Abstract sys runtime exception.
     *
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public SystemRuntimeException(String message, Throwable cause, Object [] args) {
        super(message,cause,args);
    }

    /**
     * Instantiates a new Abstract sys runtime exception.
     *
     * @param code    the code
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public SystemRuntimeException(String code, String message, Throwable cause, Object [] args) {
        super(code,message,cause,args);
    }

    /**
     * Instantiates a new Abstract sys runtime exception.
     *
     * @param errorCode the error info
     * @param cause     the cause
     */
    public SystemRuntimeException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    /**
     * Instantiates a new Abstract sys runtime exception.
     *
     * @param errorCode the error info
     */
    public SystemRuntimeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
