/*
 * Copyright 2020-2021 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hhao.common.extension.executor;


import com.hhao.common.extension.BizScenario;
import com.hhao.common.extension.model.ExtensionCoordinate;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Wang
 * @since 2022/3/10 17:06
 */
public abstract class AbstractComponentExecutor {

    /**
     * Execute extension with Response
     *
     * @param targetClz
     * @param bizScenario
     * @param exeFunction
     * @param <R> Response Type
     * @param <T> Parameter Type
     * @return
     */
    public <R, T> R execute(Class<T> targetClz, BizScenario bizScenario, Function<T, R> exeFunction) {
        T component = locateComponent(targetClz, bizScenario);
        return exeFunction.apply(component);
    }

    public <R, T> R execute(ExtensionCoordinate extensionCoordinate, Function<T, R> exeFunction){
        return execute((Class<T>) extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(), exeFunction);
    }

    /**
     * Execute extension without Response
     *
     * @param targetClz
     * @param context
     * @param exeFunction
     * @param <T> Parameter Type
     */
    public <T> void executeVoid(Class<T> targetClz, BizScenario context, Consumer<T> exeFunction) {
        T component = locateComponent(targetClz, context);
        exeFunction.accept(component);
    }

    public <T> void executeVoid(ExtensionCoordinate extensionCoordinate, Consumer<T> exeFunction){
        executeVoid(extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(), exeFunction);
    }

    protected abstract <C> C locateComponent(Class<C> targetClz, BizScenario context);
}
