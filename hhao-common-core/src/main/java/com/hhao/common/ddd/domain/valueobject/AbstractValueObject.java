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

package com.hhao.common.ddd.domain.valueobject;

import java.io.Serializable;

/**
 * 值对象基类
 *
 * @author Wang
 * @since 1.0.0
 */
public abstract class AbstractValueObject implements Serializable {
    private static final long serialVersionUID = 3436018194779142235L;

    // 提供一个方法来执行验证
    public ValidationResult validate(ValueObjectValidator validator) {
        return validator.validate(this);
    }
}
