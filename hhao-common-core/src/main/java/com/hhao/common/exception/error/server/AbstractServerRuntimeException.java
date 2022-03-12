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

package com.hhao.common.exception.error.server;

import com.hhao.common.exception.AbstractBaseRuntimeException;
import com.hhao.common.exception.ErrorInfo;
import com.hhao.common.lang.NonNull;


/**
 * 服务异常，属于业务异常
 * 所有服务异常的根类
 *
 * @author  Wang
 * @since 1.0.0
 */
public class AbstractServerRuntimeException extends AbstractBaseRuntimeException {
    /**
     * Instantiates a new Abstract server runtime exception.
     *
     * @param errorInfo the error info
     * @param cause     the cause
     */
    public AbstractServerRuntimeException(@NonNull ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo,cause);
    }

    /**
     * Instantiates a new Abstract server runtime exception.
     *
     * @param errorInfo the error info
     */
    public AbstractServerRuntimeException(@NonNull ErrorInfo errorInfo) {
        this(errorInfo,null);
    }
}
