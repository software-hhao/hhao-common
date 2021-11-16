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

/**
 * The interface Exception transfer.
 *
 * @author Wang
 * @since 1.0.0
 */
public interface ExceptionTransfer {
    /**
     * 是否支持该异常的转换
     *
     * @param exception the exception
     * @return boolean
     */
    boolean support(Throwable exception);

    /**
     * 将源类异常转换成目标类异常
     *
     * @param exception the exception
     * @return throwable
     */
    Throwable transfer(Throwable exception);
}
