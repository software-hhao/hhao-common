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

/**
 * @author Wang
 * @since 2022/3/10 9:49
 */

import java.io.Serializable;

/**
 * 自定义异常类的基类
 *
 * @author Wang
 * @since 1.0.0
 */
public interface BaseException extends Serializable {
    String MESSAGE_ID_BEGIN_MARK="${";
    String MESSAGE_ID_END_MARK="}";

    long serialVersionUID = 1L;

    /**
     * 获取异常描述
     *
     * @return ErrorInfo error info
     */
    ErrorInfo getErrorInfo();

    /**
     * 将${}包含的message解析为messageId
     * @param message
     * @return
     */
    default String getMessageIdFromMessage(String message){
        if (message==null || message.trim().length()<=3){
            return null;
        }
        String messageId=message.trim();
        if (messageId.startsWith(MESSAGE_ID_BEGIN_MARK) && messageId.endsWith(MESSAGE_ID_END_MARK)){
            return messageId.substring(2,messageId.length()-2);
        }
        return null;
    }
}
