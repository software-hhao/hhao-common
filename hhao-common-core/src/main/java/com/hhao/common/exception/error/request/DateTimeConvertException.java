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

package com.hhao.common.exception.error.request;

import com.hhao.common.exception.ErrorInfo;
import com.hhao.common.exception.ErrorInfos;

/**
 * 自定义日期时间转换异常类
 *
 * @author Wang
 * @since 1.0.0
 */
public class DateTimeConvertException extends AbstractRequestRuntimeException {
    /**
     * Instantiates a new Date time convert exception.
     *
     * @param errorInfo the error info
     * @param cause     the cause
     */
    public DateTimeConvertException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }

    /**
     * Instantiates a new Date time convert exception.
     *
     * @param errorInfo the error info
     */
    public DateTimeConvertException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    /**
     * Instantiates a new Date time convert exception.
     *
     * @param cause the cause
     */
    public DateTimeConvertException(Throwable cause) {
        super(ErrorInfos.ERROR_400_DATETIME,cause);
    }

    /**
     * Instantiates a new Date time convert exception.
     */
    public DateTimeConvertException() {
        this(ErrorInfos.ERROR_400_DATETIME,null);
    }

}