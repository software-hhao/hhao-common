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

import java.util.List;

/**
 * ErrorInfo构建类
 *
 * @author Wang
 * @since 1.0.0
 */
public class ErrorInfoBuilder {
    /**
     * Build error info.
     *
     * @param code           the code
     * @param messageId      the message id
     * @param args           the args
     * @param defaultMessage the default message
     * @return the error info
     */
    public static ErrorInfo build(String code, String messageId, List<Object> args,String defaultMessage){
        return new ErrorInfo(code,defaultMessage,messageId,args);
    }

    /**
     * Build error info.
     *
     * @param code           the code
     * @param messageId      the message id
     * @param defaultMessage the default message
     * @return the error info
     */
    public static ErrorInfo build(String code, String messageId,String defaultMessage){
        return new ErrorInfo(code,defaultMessage,messageId,null);
    }

    /**
     * Build error info.
     *
     * @param code      the code
     * @param messageId the message id
     * @param args      the args
     * @return the error info
     */
    public static ErrorInfo build(String code, String messageId,List<Object> args){
        return new ErrorInfo(code,messageId,args);
    }

    /**
     * Build error info.
     *
     * @param code      the code
     * @param messageId the message id
     * @return the error info
     */
    public static ErrorInfo build(String code, String messageId){
        return new ErrorInfo(code,messageId);
    }
}
