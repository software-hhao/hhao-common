
/*
 * Copyright 2018-2022 WangSheng.
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

package com.hhao.common.exception;


import com.hhao.common.Context;
import com.hhao.common.CoreConstant;
import com.hhao.common.lang.NonNull;

/**
 * 非受检异常的根类，从RuntimeException继承
 * 只允许通过errorCode构建错误信息
 * 非受检异常分类：可以预测异常、需捕获的异常、可以透出的异常
 *
 * @author Wang
 * @since 1.0.0
 */
public abstract class AbstractBaseRuntimeException extends RuntimeException implements BaseException {
    private static final long serialVersionUID = -8466085224148083524L;
    /**
     * ErrorInfo
     */
    private ErrorInfo errorInfo;

    /**
     * message:如果是${}包围的，解析为messageId，否则解析为message
     *
     * @param message
     */
    public AbstractBaseRuntimeException(String message) {
        this(String.valueOf(CoreConstant.DEFAULT_EXCEPTION_STATUS),message,null);
    }

    /**
     * message:如果是${}包围的，解析为messageId，否则解析为message
     *
     * @param message
     * @param cause
     */
    public AbstractBaseRuntimeException(String message,Throwable cause) {
        this(String.valueOf(CoreConstant.DEFAULT_EXCEPTION_STATUS),message,cause);
    }

    /**
     * message:如果是${}包围的，解析为messageId，否则解析为message
     *
     * @param code:异常编码
     * @param message:如果是${}包围的，解析为messageId，否则解析为message
     * @param cause
     */
    public AbstractBaseRuntimeException(String code,String message,Throwable cause) {
        super(cause);
        String messageId=getMessageIdFromMessage(message);
        if (messageId==null){
            this.errorInfo = ErrorInfoBuilder.build(message);
        }else{
            this.errorInfo = ErrorInfoBuilder.build(code,messageId);
        }
    }

    /**
     * Instantiates a new Abstract base runtime exception.
     *
     * @param errorInfo the error info
     * @param cause     the cause
     */
    public AbstractBaseRuntimeException(@NonNull ErrorInfo errorInfo, Throwable cause) {
        super(cause);
        this.errorInfo = errorInfo;
    }

    /**
     * Instantiates a new Abstract base runtime exception.
     *
     * @param errorInfo the error info
     */
    public AbstractBaseRuntimeException(@NonNull ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }

    @Override
    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }

    /**
     * Sets error info.
     *
     * @param errorInfo the error info
     */
    public void setErrorInfo(ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }

    @Override
    public String getMessage() {
        return errorInfo.getLocalMessage(Context.getInstance());
    }
}
