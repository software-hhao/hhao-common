
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

package com.hhao.common.exception.error;

import com.hhao.common.CoreConstant;
import com.hhao.common.exception.AbstractBaseRuntimeException;
import com.hhao.common.exception.ErrorInfo;

/**
 * 业务类异常的根类
 * 有明确的业务语义，不需要记录Error日志，不需要Retry
 *
 * @author Wang
 * @since 1.0.0
 */
public class AbstractBizRuntimeException extends AbstractBaseRuntimeException {

    /**
     * Instantiates a new Abstract biz runtime exception.
     *
     * @param message the message
     */
    public AbstractBizRuntimeException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Abstract biz runtime exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public AbstractBizRuntimeException(String message,Throwable cause) {
        super(message,cause);
    }

    /**
     * Instantiates a new Abstract biz runtime exception.
     *
     * @param message the message
     * @param args    the args
     */
    public AbstractBizRuntimeException(String message,Object [] args) {
        super(message,args);
    }

    /**
     * Instantiates a new Abstract biz runtime exception.
     *
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public AbstractBizRuntimeException(String message,Throwable cause,Object [] args) {
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
    public AbstractBizRuntimeException(String code,String message,Throwable cause,Object [] args) {
        super(code,message,cause,args);
    }


    /**
     * Instantiates a new Abstract biz runtime exception.
     *
     * @param errorInfo the error info
     * @param cause     the cause
     */
    public AbstractBizRuntimeException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }

    /**
     * Instantiates a new Abstract biz runtime exception.
     *
     * @param errorInfo the error info
     */
    public AbstractBizRuntimeException(ErrorInfo errorInfo) {
        super(errorInfo);
    }
}
