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


/**
 * The interface Exception transfer.
 *
 * @author Wang
 * @since 1.0.0
 */
public interface ExceptionTransfer{
    /**
     * 是否支持该异常的转换
     */
    boolean support(Throwable exception);

    /**
     * 将源类异常转换成目标类异常
     */
    Throwable transfer(Throwable exception);

    /**
     * Get order int.
     *
     * @return the int
     */
    default int getOrder(){
        return 0;
    }
}
