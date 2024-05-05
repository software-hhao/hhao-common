
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

package com.hhao.common.mybatis.page.exception;

/**
 * The type My batis exception.
 *
 * @author Wang
 * @since 2021 /11/24 20:46
 */
public class MyBatisException extends RuntimeException{
    /**
     * Instantiates a new My batis exception.
     *
     * @param message the message
     */
    public MyBatisException(String message) {
        super(message);
    }
}
