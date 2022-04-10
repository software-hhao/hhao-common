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

package com.hhao.common.extension.model;

import org.springframework.core.Ordered;

/**
 * 扩展点接口，所有扩展点应继承自该接口
 *
 * @param <R> the type parameter
 * @param <C> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public interface ExtensionPoint<R, C> extends Ordered {

    /**
     * 扩展点代理执行器调用
     * 返回true时表示支持，返回false时表示不支持
     *
     * @param context the context
     * @return the boolean
     */
    default boolean support(C context){
        return true;
    }

    /**
     * 扩展点代理执行器调用，带参数返回
     *
     * @param context the context
     * @return the r
     */
    default R execute(C context){
        throw new IllegalCallerException("exec method calls are not supported; exec is undefined");
    }

    /**
     *  扩展点代理执行器调用，不带参数返回
     *
     * @param context the context
     */
    default void executeVoid(C context){
        throw new IllegalCallerException("executeVoid method calls are not supported; executeVoid is undefined");
    }

    /**
     *  扩展点代理执行器执行异常时调用
     *
     * @param exception the exception
     * @param context   the context
     * @return the boolean
     */
    default void onError(Throwable exception,C context){
        throw new RuntimeException("An error occurred during execution!",exception);
    }

    /**
     * 执行顺序
     *
     * @return
     */
    @Override
    default int getOrder(){
        return 0;
    }
}
