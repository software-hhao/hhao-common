
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

package com.hhao.common.exception.error.server;

import com.hhao.common.exception.ErrorInfo;
import com.hhao.common.exception.ErrorInfos;

/**
 * 系统繁忙错误，如系统被降级、限流
 *
 * @author Wang
 * @since 2022 /1/6 17:06
 */
public class ServiceUnavailableException extends AbstractServerRuntimeException {
    /**
     * Instantiates a new Service unavailable exception.
     *
     * @param message the message
     */
    public ServiceUnavailableException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Service unavailable exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ServiceUnavailableException(String message,Throwable cause) {
        super(message,cause);
    }

    /**
     * Instantiates a new Service unavailable exception.
     *
     * @param message the message
     * @param args    the args
     */
    public ServiceUnavailableException(String message,Object [] args) {
        super(message,args);
    }

    /**
     * Instantiates a new Service unavailable exception.
     *
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public ServiceUnavailableException(String message,Throwable cause,Object [] args) {
        super(message,cause,args);
    }

    /**
     * Instantiates a new Service unavailable exception.
     *
     * @param code    the code
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public ServiceUnavailableException(String code,String message,Throwable cause,Object [] args) {
        super(code,message,cause,args);
    }

    /**
     * Instantiates a new System busy runtime exception.
     *
     * @param errorInfo the error info
     * @param cause     the cause
     */
    public ServiceUnavailableException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }

    /**
     * Instantiates a new System busy runtime exception.
     *
     * @param errorInfo the error info
     */
    public ServiceUnavailableException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    /**
     * Instantiates a new System busy runtime exception.
     *
     * @param cause the cause
     */
    public ServiceUnavailableException(Throwable cause) {
        super(ErrorInfos.ERROR_503, cause);
    }

    /**
     * Instantiates a new System busy runtime exception.
     */
    public ServiceUnavailableException() {
        super(ErrorInfos.ERROR_503);
    }
}
