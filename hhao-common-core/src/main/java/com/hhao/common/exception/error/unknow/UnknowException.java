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

package com.hhao.common.exception.error.unknow;

import com.hhao.common.exception.AbstractBaseRuntimeException;
import com.hhao.common.exception.ErrorInfo;
import com.hhao.common.exception.ErrorInfos;

/**
 * 未知异常
 * 未知的异常，需要完整的ErrorStack日志，可以Retry
 *
 * @author Wang
 * @since 1.0.0
 */
public class UnknowException extends AbstractBaseRuntimeException {
    /**
     * Instantiates a new Unknow exception.
     *
     * @param message the message
     */
    public UnknowException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Unknow exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public UnknowException(String message,Throwable cause) {
        super(message,cause);
    }

    /**
     * Instantiates a new Unknow exception.
     *
     * @param message the message
     * @param args    the args
     */
    public UnknowException(String message,Object [] args) {
        super(message,args);
    }

    /**
     * Instantiates a new Unknow exception.
     *
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public UnknowException(String message,Throwable cause,Object [] args) {
        super(message,cause,args);
    }

    /**
     * Instantiates a new Unknow exception.
     *
     * @param code    the code
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public UnknowException(String code,String message,Throwable cause,Object [] args) {
        super(code,message,cause,args);
    }

    /**
     * Instantiates a new Unknow exception.
     *
     * @param errorInfo the error info
     * @param cause     the cause
     */
    public UnknowException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }

    /**
     * Instantiates a new Unknow exception.
     *
     * @param errorInfo the error info
     */
    public UnknowException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    /**
     * Instantiates a new Unknow exception.
     *
     * @param cause the cause
     */
    public UnknowException(Throwable cause) {
        super(ErrorInfos.ERROR_999,cause);
    }

    /**
     * Instantiates a new Unknow exception.
     */
    public UnknowException() {
        super(ErrorInfos.ERROR_999);
    }

//    @Override
//    public String getMessage() {
//        String message=super.getMessage();
//        if (this.getCause()!=null){
//            message=message + ":" + this.getCause().getMessage();
//        }
//        return message;
//    }
}
