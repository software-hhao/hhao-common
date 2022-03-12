
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
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
public class ErrorInfo implements Serializable {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(ErrorInfo.class);
    private static final String DEFAULT_MESSAGE="unknown error message";
    private static final long serialVersionUID = 1;
    /**
     * 自定义的错误代码
     **/
    private String code;
    /**
     * 默认的异常提示信息
     **/
    private String defaultMessage=DEFAULT_MESSAGE;
    /**
     * 资源文件的属性id，用于从资源文件获取本地化异常提示信息
     **/
    private String messageId;

    /**
     * args
     */
    private List<Object> args=new ArrayList<>();

    /**
     * Instantiates a new Error info.
     *
     * @param code           the code
     * @param defaultMessage the default message
     * @param messageId      the message id
     * @param args           the args
     */
    public ErrorInfo(String code, String defaultMessage, String messageId, List<Object> args) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.messageId = messageId;
        this.args=args;
    }

    /**
     * Instantiates a new Error info.
     *
     * @param code      the code
     * @param messageId the message id
     * @param args      the args
     */
    public ErrorInfo(String code, String messageId, List<Object> args) {
        this(code,DEFAULT_MESSAGE,messageId,args);
    }

    /**
     * Instantiates a new Error info.
     *
     * @param code      the code
     * @param messageId the message id
     */
    public ErrorInfo(String code, String messageId) {
        this(code,DEFAULT_MESSAGE,messageId,null);
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
    public String getDefaultMessage() {
        return defaultMessage;
    }

    /**
     * Sets default message.
     *
     * @param defaultMessage the default message
     */
    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    /**
     * Gets message id.
     *
     * @return the message id
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets message id.
     *
     * @param messageId the message id
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * 获取本地化消息
     * 优先根据messageId从资源文件上获取，如果获取不到，则返回defaultMessage
     *
     * @param context the context
     * @param locale  the locale
     * @return local message
     */
    public String getLocalMessage(Context context, Locale locale){
        String localMessage = "";
        try {
            if (messageId!=null && messageId.length()>0) {
                localMessage = context.getMessage(messageId, args==null?null:args.toArray(), locale);
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
        } finally{
            if (localMessage!=null && localMessage.length()>0){
                localMessage=defaultMessage;
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
     * 应用参数
     * 构造一个新的AbstractErrorInfo对象
     *
     * @param args the args
     * @return the error info
     */
    public ErrorInfo applyArgs(Object[] args){
        return new ErrorInfo(code,defaultMessage,messageId, Arrays.asList(args));
    }
}
