
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
 * 受检异常类的基类,从Exception继承
 *
 * @author Wang
 * @since 1.0.0
 */
public abstract class AbstractBaseException extends Exception implements BaseException {
    private static final long serialVersionUID = -6091764126559304691L;
    /**
     * errorInfo
     */
    private ErrorInfo errorInfo = null;

    public AbstractBaseException(String message) {
        this(String.valueOf(CoreConstant.DEFAULT_EXCEPTION_STATUS),message,null);
    }

    public AbstractBaseException(String message, Throwable cause) {
        this(String.valueOf(CoreConstant.DEFAULT_EXCEPTION_STATUS),message,cause);
    }

    public AbstractBaseException(String code,String message, Throwable cause) {
        super(cause);
        String messageId=getMessageIdFromMessage(message);
        if (messageId==null){
            this.errorInfo = ErrorInfoBuilder.build(message);
        }else{
            this.errorInfo = ErrorInfoBuilder.build(code,messageId);
        }
    }

    /**
     * Instantiates a new Abstract base exception.
     *
     * @param errorInfo the error info
     * @param cause     the cause
     */
    public AbstractBaseException(@NonNull ErrorInfo errorInfo, Throwable cause) {
        super(cause);
        this.errorInfo = errorInfo;
    }

    /**
     * Instantiates a new Abstract base exception.
     *
     * @param errorInfo the error info
     */
    public AbstractBaseException(@NonNull ErrorInfo errorInfo) {
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

