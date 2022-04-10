/*
 * Copyright 2020-2021 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hhao.common.exception.error.request;

import com.hhao.common.exception.AbstractBaseRuntimeException;
import com.hhao.common.exception.ErrorInfo;

/**
 * 参数错误
 *
 * @author Wang
 * @since 1.0.0
 */
public class IllegalArgumentException extends AbstractBaseRuntimeException {
    /**
     * Instantiates a new Illegal argument exception.
     *
     * @param message the message
     */
    public IllegalArgumentException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Illegal argument exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public IllegalArgumentException(String message,Throwable cause) {
        super(message,cause);
    }

    /**
     * Instantiates a new Illegal argument exception.
     *
     * @param message the message
     * @param args    the args
     */
    public IllegalArgumentException(String message,Object [] args) {
        super(message,args);
    }

    /**
     * Instantiates a new Illegal argument exception.
     *
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public IllegalArgumentException(String message,Throwable cause,Object [] args) {
        super(message,cause,args);
    }

    /**
     * Instantiates a new Illegal argument exception.
     *
     * @param code    the code
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public IllegalArgumentException(String code,String message,Throwable cause,Object [] args) {
        super(code,message,cause,args);
    }

    /**
     * Instantiates a new Illegal argument exception.
     *
     * @param errorInfo the error info
     * @param cause     the cause
     */
    public IllegalArgumentException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }

    /**
     * Instantiates a new Illegal argument exception.
     *
     * @param errorInfo the error info
     */
    public IllegalArgumentException(ErrorInfo errorInfo) {
        super(errorInfo);
    }
}
