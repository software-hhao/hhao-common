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

package com.hhao.common.extension.strategy;

/**
 * 在组合代理执行器中,每个扩展点执行完毕后，验证是否继续执行的规则
 *
 * @param <R> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public interface InterruptionStrategy<R> {

    /**
     * 返回true，则中断后续扩展点的执行
     * 返回false，则继续
     *
     * @param extensionPointResult the extension point result
     * @return the boolean
     */
    boolean interrupt(R extensionPointResult);



}
