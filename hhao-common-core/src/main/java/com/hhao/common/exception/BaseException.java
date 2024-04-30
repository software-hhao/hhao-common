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
    /**
     * 获取异常描述
     *
     * @return ErrorInfo error info
     */
    ErrorCode getErrorCode();
}
