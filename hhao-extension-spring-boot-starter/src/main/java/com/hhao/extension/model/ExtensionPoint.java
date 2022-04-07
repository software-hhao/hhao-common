/*
 * Copyright 2018-2022 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        https://www.gnu.org/licenses/gpl-3.0.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.hhao.extension.model;

import org.springframework.core.Ordered;

/**
 * @author Wang
 * @since 1.0.0
 */
public interface ExtensionPoint<C, R> extends ExtensionPointBase {
    /**
     * 是否执行当前实现的条件
     *
     * @param context 调用上下文
     * @return 是否满足条件
     */
    default boolean support(C context){
        return true;
    }

    /**
     * 扩展点实现的具体操作
     *
     * @param context 调用上下文
     * @return 执行结果
     */
    R exec(C context);
}
