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
 * 认证异常,帐号或密码错误
 *
 * @author Wang
 * @since 1.0.0
 */
public class AuthenticationException extends RequestRuntimeException {
    /**
     * Instantiates a new Authorize exception.
     *
     * @param message the message
     */
    public AuthenticationException(String message) {
        super(DefaultErrorCodes.ERROR_401.getCode(),message,null,null);
    }

    public AuthenticationException(String code, String message, Throwable cause, Object [] args) {
        super(code,message,cause,args);
    }

    /**
     * Instantiates a new Authorize exception.
     *
     * @param message the message
     * @param args    the args
     */
    public AuthenticationException(String message, Object [] args) {
        super(DefaultErrorCodes.ERROR_401.getCode(),message,null,args);
    }

    public AuthenticationException(String code,String message,Object [] args) {
        super(code,message,args);
    }

    public AuthenticationException(String code,String message) {
        super(code,message);
    }

    /**
     * Instantiates a new Authorize exception.
     *
     * @param errorCode the error info
     */
    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Instantiates a new Authorize exception.
     */
    public AuthenticationException() {super(DefaultErrorCodes.ERROR_401_1);
    }
}
