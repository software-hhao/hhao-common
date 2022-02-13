
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

package com.hhao.common.springboot.exception.util;

/**
 * 异常字段的名称
 *
 * @author Wang
 * @since 2022/1/8 14:38
 */
public interface ErrorAttributeConstant {
    /**
     * 异常时间
     * The constant TIMESTAMP.
     */
    String TIMESTAMP="timestamp";
    /**
     * 简单的错误描述，与status关联的错误描述
     * The constant ERROR.
     */
    String ERROR="error";
    /**
     * 状态
     * The constant STATUS.
     */
    String STATUS="status";
    /**
     * 自定义的错误号
     * The constant ERROR_CODE.
     */
    String ERROR_CODE="errorCode";
    /**
     * 访问路径，可能不存在
     * The constant PATH.
     */
    String PATH="path";
    /**
     * 异常类，可能不存在
     * The constant EXCEPTION.
     */
    String EXCEPTION="exception";
    /**
     * 提示信息，可能不存在
     * The constant MESSAGE.
     */
    String MESSAGE="message";
    /**
     * 异常trace，可能不存在
     * The constant TRACE.
     */
    String TRACE="trace";
    /**
     * 请求字段错误，可能不存在
     * The constant ERRORS.
     */
    String ERRORS="errors";
}
