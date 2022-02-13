/*
 * Copyright 2018-2021 WangSheng.
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

package com.hhao.common.springboot.exception;

import com.hhao.common.springboot.AppContext;

import javax.validation.constraints.NotNull;

/**
 * 自定义异常类的基类,从Exception继承
 * 未知的异常，需要完整的ErrorStack日志，可以Retry
 * 需要抛出异常的自定义类可以从它继承
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

    /**
     * Instantiates a new Abstract base exception.
     *
     * @param errorInfo the error info
     * @param cause     the cause
     */
    public AbstractBaseException(@NotNull ErrorInfo errorInfo, Throwable cause) {
        super(cause);
        this.errorInfo = errorInfo;
    }

    /**
     * Instantiates a new Abstract base exception.
     *
     * @param errorInfo the error info
     */
    public AbstractBaseException(@NotNull ErrorInfo errorInfo) {
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
        return errorInfo.getLocalMessage(AppContext.getInstance().applicationContext(), AppContext.getInstance().getLocale());
    }
}
