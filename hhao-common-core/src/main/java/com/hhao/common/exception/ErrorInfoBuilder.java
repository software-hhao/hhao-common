
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

import com.hhao.common.CoreConstant;

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
     * @param code        the code
     * @param messageId   the message id
     * @param args        the args
     * @param message     the default message
     * @param retrievable the retrievable
     * @return the error info
     */
    public static ErrorInfo build(String code, String messageId, List<Object> args,String message,boolean retrievable){
        return new ErrorInfo(code,message,messageId,args,retrievable);
    }


    /**
     * Build error info.
     *
     * @param code      the code
     * @param messageId the message id
     * @param args      the args
     * @param message   the message
     * @return the error info
     */
    public static ErrorInfo build(String code, String messageId, List<Object> args,String message){
        return new ErrorInfo(code,message,messageId,args,false);
    }

    /**
     * Build error info.
     *
     * @param code        the code
     * @param messageId   the message id
     * @param message     the message
     * @param retrievable the retrievable
     * @return the error info
     */
    public static ErrorInfo build(String code, String messageId,String message,boolean retrievable){
        return new ErrorInfo(code,message,messageId,null,retrievable);
    }

    /**
     * Build error info.
     *
     * @param code      the code
     * @param messageId the message id
     * @param message   the message
     * @return the error info
     */
    public static ErrorInfo build(String code, String messageId,String message){
        return new ErrorInfo(code,message,messageId,null,false);
    }

    /**
     * Build error info.
     *
     * @param code        the code
     * @param messageId   the message id
     * @param args        the args
     * @param retrievable the retrievable
     * @return the error info
     */
    public static ErrorInfo build(String code, String messageId, List<Object> args,boolean retrievable){
        return new ErrorInfo(code,messageId,args,retrievable);
    }

    /**
     * Build error info.
     *
     * @param code      the code
     * @param messageId the message id
     * @param args      the args
     * @return the error info
     */
    public static ErrorInfo build(String code, String messageId, List<Object> args){
        return new ErrorInfo(code,messageId,args);
    }

    /**
     * Build error info.
     *
     * @param code        the code
     * @param messageId   the message id
     * @param retrievable the retrievable
     * @return the error info
     */
    public static ErrorInfo build(String code, String messageId,boolean retrievable){
        return new ErrorInfo(code,messageId,retrievable);
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

    /**
     * Build error info.
     *
     * @param message     the message
     * @param retrievable the retrievable
     * @return the error info
     */
    public static ErrorInfo build(String message,boolean retrievable){
        return new ErrorInfo(String.valueOf(CoreConstant.DEFAULT_EXCEPTION_STATUS),message,null,null,retrievable);
    }

    /**
     * Build error info.
     *
     * @param message the message
     * @return the error info
     */
    public static ErrorInfo build(String message){
        return new ErrorInfo(String.valueOf(CoreConstant.DEFAULT_EXCEPTION_STATUS),message,null,null,false);
    }
}
