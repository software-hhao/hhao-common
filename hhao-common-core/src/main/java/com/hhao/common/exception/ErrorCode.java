/*
 * Copyright 2008-2024 wangsheng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hhao.common.exception;


import com.hhao.common.Context;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * 自定义概要的、用于客户端显示简要的异常提示信息的实体类
 * 包含的自定义的异常代码、概要提示信息、本地化概要提示信息
 * 支持Locale本地化，需要资源文件定义本地化错误提示信息
 * 异常信息提示优先从消息文件中加载，如果消息文件中获取不到，则采用defaultMessage
 *
 * @author Wan
 * @since 1.0.0
 */
public class ErrorCode implements Serializable {
    private static final long serialVersionUID = -7768245330104312297L;
    protected final Logger logger = LoggerFactory.getLogger(ErrorCode.class);

    private static final String DEFAULT_MESSAGE="Exception message not defined!";
    private static final String MESSAGE_ID_BEGIN_MARK="${";
    private static final String MESSAGE_ID_END_MARK="}";

    /**
     * 自定义的错误代码
     **/
    private String code="";
    /**
     * 异常提示信息,可以是静态文本，也可以是资源文件的属性id，用于从资源文件获取本地化异常提示信息
     **/
    private String message = ErrorCode.DEFAULT_MESSAGE;

    /**
     * 能否重试
     */
    private boolean retrievable=false;

    /**
     * 资源文件中属性的参数值
     */
    private List<Object> args=new ArrayList<>();

    public ErrorCode(String code, String message, List<Object> args, boolean retrievable) {
        this.code = code;
        this.message = message;
        this.args=args;
        this.retrievable=retrievable;
    }

    public ErrorCode(String code, String message, List<Object> args) {
        this(code,message,args,false);
    }

    public ErrorCode(String code, String message) {
        this(code,message,null,false);
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets default message.
     *
     * @return the default message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Sets default message.
     *
     * @param message the default message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Is retrievable boolean.
     *
     * @return the boolean
     */
    public boolean isRetrievable() {
        return retrievable;
    }

    /**
     * Sets retrievable.
     *
     * @param retrievable the retrievable
     */
    public void setRetrievable(boolean retrievable) {
        this.retrievable = retrievable;
    }

    /**
     * 获取本地化消息
     * 优先根据messageId从资源文件上获取，如果获取不到，则返回message,最后返回DEFAULT_MESSAGE
     *
     * @param context the context
     * @param locale  the locale
     * @return local message
     */
    public String getLocalMessage(Context context, Locale locale){
        String localMessage = "";
        try {
            String messageId=this.getMessageId(this.message);
            if (messageId!=null) {
                localMessage = context.getMessage(messageId, args==null?null:args.toArray(), locale);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        } finally{
            if (localMessage==null || localMessage.isBlank()){
                if (this.message!=null){
                    if (this.args!=null) {
                        localMessage = MessageFormat.format(message, args.toArray());
                    }else{
                        localMessage=message;
                    }
                }
            }
            if (localMessage==null || localMessage.isBlank()){
                localMessage= DEFAULT_MESSAGE;
            }
        }
        return localMessage;
    }

    /**
     * Get local message string.
     *
     * @param context the context
     * @return the string
     */
    public String getLocalMessage(Context context){
        return getLocalMessage(context,context.getLocale());
    }

    /**
     * 将${}包含的message解析为messageId
     * @param message
     * @return
     */
    protected String getMessageId(String message){
        if (isMessageId(message)){
            return message.substring(2,message.length()-1);
        }
        return null;
    }

    /**
     * 检查给定的字符串是否为消息ID。
     * 消息ID必须是非空的，长度超过3个字符，并且以特定的开始标记开始，以特定的结束标记结束。
     *
     * @param message 要检查是否为消息ID的字符串。
     * @return 如果给定的字符串符合条件，即为消息ID，返回true；否则返回false。
     */
    protected boolean isMessageId(String message){
        // 检查消息字符串是否满足非空、长度条件以及开始和结束标记
        return message!=null && message.length()>3 &&  message.startsWith(MESSAGE_ID_BEGIN_MARK) && message.endsWith(MESSAGE_ID_END_MARK);
    }

    /**
     * 应用参数
     * 构造一个新的ErrorInfo对象
     *
     * @param args the args
     * @return the error info
     */
    public ErrorCode applyArgs(Object[] args){
        return new ErrorCode(code, message, Arrays.asList(args),retrievable);
    }
}
