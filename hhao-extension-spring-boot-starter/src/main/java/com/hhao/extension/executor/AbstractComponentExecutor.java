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

package com.hhao.extension.executor;

import com.hhao.common.extension.BizScenario;
import com.hhao.extension.strategy.DefaultInterruptionStrategy;
import com.hhao.extension.model.ExtensionPoint;
import com.hhao.extension.strategy.InterruptionStrategy;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.annotation.OrderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Wang
 * @since 1.0.0
 */
public abstract class AbstractComponentExecutor {
    /**
     * Execute extension with Response
     *
     * @param targetClz 扩展点接口定义
     * @param context   扩展点上下文信息
     * @param <R>       扩展点接口入参类型
     * @param <C>       扩展点接口出参类型
     * @return 执行结果
     */
    public <R, C> R execute(Class<? extends ExtensionPoint<C, R>> targetClz,BizScenario bizScenario, C context) {
        ExtensionPoint extensionPoint = locateComponent(targetClz,bizScenario, context);
        return (R) extensionPoint.exec(context);
    }

    /**
     * Multi Execute extension with Response
     *
     * @param targetClz 扩展点接口
     * @param context   扩展点上下文信息
     * @param <R>       扩展点接口入参类型
     * @param <C>       扩展点接口出参类型
     * @return 执行结果, 使用list包装了每个扩展点实现的返回值
     */
    public <R, C> List<R> multiExecute(Class<? extends ExtensionPoint<C, R>> targetClz,BizScenario bizScenario, C context) {
        return multiExecute(targetClz,bizScenario, context, new DefaultInterruptionStrategy<R>());
    }



    /**
     * Multi Execute extension with Response
     *
     * @param targetClz            扩展点接口
     * @param context              扩展点上下文信息
     * @param <R>                  扩展点接口入参类型
     * @param <C>                  扩展点接口出参类型
     * @param interruptionStrategy 中断策略
     * @return 执行结果, 使用list包装了每个扩展点实现的返回值
     */
    public <R, C> List<R> multiExecute(Class<? extends ExtensionPoint<C, R>> targetClz,BizScenario bizScenario, C context, InterruptionStrategy<R> interruptionStrategy) {
        List<ExtensionPoint> extensionPointIs = locateComponents(targetClz,bizScenario, context);
        List<R> combinationResult = new ArrayList<>(extensionPointIs.size());
        for (ExtensionPoint extensionPointI : extensionPointIs) {
            R result = (R) extensionPointI.exec(context);
            combinationResult.add(result);
            if (interruptionStrategy.interrupt(result)) {
                return combinationResult;
            }
        }
        return combinationResult;
    }



    /**
     * 加载扩展实现
     */
    protected abstract <C, R> ExtensionPoint<C,R> locateComponent(Class<? extends ExtensionPoint<C, R>> targetClz, BizScenario bizScenario, C context);

    /**
     * 加载多个扩展点实现
     */
    protected abstract <C, R> List<ExtensionPoint> locateComponents(Class<? extends ExtensionPoint<C, R>> targetClz, BizScenario bizScenario, C context);
}
