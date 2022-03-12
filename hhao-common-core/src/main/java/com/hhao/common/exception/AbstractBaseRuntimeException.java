
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
import com.hhao.common.lang.NonNull;

/**
 * 自定义异常的根类
 * 未知的异常，需要完整的ErrorStack日志，可以Retry
 * 只允许通过errorCode构建错误信息
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
