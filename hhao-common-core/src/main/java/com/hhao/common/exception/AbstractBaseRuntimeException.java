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

package com.hhao.common.exception;


import com.hhao.common.Context;
import com.hhao.common.CoreConstant;

import java.util.Arrays;

/**
 * 非受检异常的根类，从RuntimeException继承
 * 只允许通过errorCode构建错误信息
 * 非受检异常分类：可以预测异常、需捕获的异常、可以透出的异常
 * 传入message，如果${}包含，则解析为messageId，否则为message
 * Object [] args为参数;如果是messageId，则为消息的参数，否则为String的formatter
 *
 * @author Wang
 * @since 1.0.0
 */
public abstract class AbstractBaseRuntimeException extends RuntimeException implements BaseException {
    private static final long serialVersionUID = -8466085224148083524L;
    /**
     * ErrorInfo
     */
    private ErrorCode errorCode;

    /**
     * message:如果是${}包围的，解析为messageId，否则解析为message
     *
     * @param message the message
     */
    public AbstractBaseRuntimeException(String message) {
        this(String.valueOf(CoreConstant.DEFAULT_EXCEPTION_STATUS),message,null,null);
    }

    /**
     * message:如果是${}包围的，解析为messageId，否则解析为message
     *
     * @param message the message
     * @param cause   the cause
     */
    public AbstractBaseRuntimeException(String message,Throwable cause) {
        this(String.valueOf(CoreConstant.DEFAULT_EXCEPTION_STATUS),message,cause,null);
    }

    /**
     * Instantiates a new Abstract base runtime exception.
     *
     * @param message the message
     * @param args    the args
     */
    public AbstractBaseRuntimeException(String message,Object [] args) {
        this(String.valueOf(CoreConstant.DEFAULT_EXCEPTION_STATUS),message,null,args);
    }

    /**
     * Instantiates a new Abstract base runtime exception.
     *
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public AbstractBaseRuntimeException(String message,Throwable cause,Object [] args) {
        this(String.valueOf(CoreConstant.DEFAULT_EXCEPTION_STATUS),message,cause,args);
    }

    public AbstractBaseRuntimeException(String code,String message,Object [] args) {
        this(code,message,null,args);
    }

    public AbstractBaseRuntimeException(String code,String message) {
        this(code,message,null,null);
    }

    /**
     * message:如果是${}包围的，解析为messageId，否则解析为message
     *
     * @param code    :异常编码
     * @param message :如果是${}包围的，解析为messageId，否则解析为message
     * @param cause   the cause
     * @param args    the args
     */
    public AbstractBaseRuntimeException(String code,String message,Throwable cause,Object [] args) {
        super(cause);
        if (args==null){
            args=new Object[]{};
        }
        this.errorCode = new ErrorCode(code,message,Arrays.asList(args));
    }

    /**
     * Instantiates a new Abstract base runtime exception.
     *
     * @param errorCode the error info
     * @param cause     the cause
     */
    public AbstractBaseRuntimeException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        if (errorCode == null) {
            throw new IllegalArgumentException("ErrorCode cannot be null");
        }
        this.errorCode = errorCode;
    }

    /**
     * Instantiates a new Abstract base runtime exception.
     *
     * @param errorCode the error info
     */
    public AbstractBaseRuntimeException( ErrorCode errorCode) {
        this(errorCode, null);
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * Sets error info.
     *
     * @param errorCode the error info
     */
    public void setErrorCode(ErrorCode errorCode) {
        if (errorCode == null) {
            throw new IllegalArgumentException("ErrorCode cannot be null");
        }
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getLocalMessage(Context.getInstance());
    }
}
