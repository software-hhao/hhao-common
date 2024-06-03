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

package com.hhao.common.extension.executor;

import com.hhao.common.extension.BizScenario;
import com.hhao.common.extension.model.ExtensionCoordinate;
import com.hhao.common.extension.model.ExtensionPoint;
import com.hhao.common.extension.strategy.DefaultInterruptionStrategy;
import com.hhao.common.extension.strategy.InterruptionStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 执行器
 * 包含扩展点代理执行器、函数式回调执行器、@ExtensionPointAutowired自动注入
 * 代理执行器：
 * 继承自ExtensionPoint,重写support、execute（有返回值）、executeVoid（无返回值）、onError、getOrder方法；
 * 匹配规则:
 * 1、按targetClz+bizScenario
 * 2、按support
 * 扩展点实现执行异常，根据onError方法确定下一步操作，
 *
 * <p>
 * 函数式回调执行器
 * 继承自ExtensionPoint<Void,?>,重写getOrder方法,扩展实现可以包含自定义接口；
 * 匹配规则:
 * 1、按targetClz+bizScenario
 * 其它的，如support、执行异常等需要在自定义的回调函数中处理；
 *
 * <p>
 * @ExtensionPointAutowired自动注入
 * 继承自ExtensionPoint,重写support、onError、getOrder方法,扩展实现可以包含自定义接口,自定义接口应包含BizScenario类型的参数；
 * 匹配规则：
 * 1、根据输入的BizScenario参数，按targetClz+bizScenario
 * 2、接support
 * 3、调用自定义接口方法
 * 如果是组合式，返回值必须采用MultiValues类型
 * 扩展点实现执行异常，根据onError方法确定下一步操作，
 * ！！！注意，接口方法的返回值必须是类对象类型，不能是基本类型
 *
 * <p>
 * 如果未发现匹配的扩展实现，由isNotFoundThrowError确定是否抛出异常。
 *
 * @author Wang
 * @since 1.0.0
 */
public abstract class AbstractComponentExecutor {
    private boolean isNotFoundThrowError=true;

    /**
     * 单一扩展器代理执行接口，带返回值
     *
     * @param <R>         the type parameter
     * @param <C>         the type parameter
     * @param targetClz   the target clz
     * @param bizScenario the biz scenario
     * @param context     the context
     * @return r
     */
    public <R, C> R execute(Class<? extends ExtensionPoint<R,C>> targetClz, BizScenario bizScenario, C context) {
        ExtensionPoint extensionPoint = locateComponent(targetClz,bizScenario, context);
        try {
            return (R) extensionPoint.execute(context);
        } catch (Exception e) {
            extensionPoint.onError(e,context);
        }
        return null;
    }

    /**
     * 单一扩展器代理执行接口，带返回值
     *
     * @param <R>                 the type parameter
     * @param <C>                 the type parameter
     * @param extensionCoordinate the extension coordinate
     * @param context             the context
     * @return the r
     */
    public <R, C> R execute(ExtensionCoordinate extensionCoordinate, C context) {
        return (R)execute(extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(), context);
    }

    /**
     * 单一扩展器代理执行接口，无返回值
     *
     * @param <C>                 the type parameter
     * @param extensionCoordinate the extension coordinate
     * @param bizScenario         the biz scenario
     * @param context             the context
     */
    public <C> void executeVoid(ExtensionCoordinate extensionCoordinate,BizScenario bizScenario, C context) {
        executeVoid(extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(), context);
    }

    /**
     * 单一扩展器代理执行接口，无返回值
     *
     * @param <C>         the type parameter
     * @param targetClz   the target clz
     * @param bizScenario the biz scenario
     * @param context     the context
     */
    public <C> void executeVoid(Class<? extends ExtensionPoint<Void,C>> targetClz,BizScenario bizScenario, C context) {
        ExtensionPoint extensionPoint = locateComponent(targetClz,bizScenario, context);
        try {
            extensionPoint.executeVoid(context);
        } catch (Exception e) {
            extensionPoint.onError(e,context);
        }
    }

    /**
     * 组合扩展器代理执行接口，带返回值
     *
     * @param <R>         the type parameter
     * @param <C>         the type parameter
     * @param targetClz   the target clz
     * @param bizScenario the biz scenario
     * @param context     the context
     * @return the list
     */
    public <R, C> List<R> multiExecute(Class<? extends ExtensionPoint<R,C>> targetClz,BizScenario bizScenario, C context) {
        return multiExecute(targetClz,bizScenario, context, new DefaultInterruptionStrategy<R>());
    }

    /**
     * 组合扩展器代理执行接口，带返回值
     *
     * @param <R>                 the type parameter
     * @param <C>                 the type parameter
     * @param extensionCoordinate the extension coordinate
     * @param context             the context
     * @return the list
     */
    public <R, C> List<R> multiExecute(ExtensionCoordinate extensionCoordinate, C context) {
        return multiExecute(extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(), context, new DefaultInterruptionStrategy<R>());
    }

    /**
     * 组合扩展器代理执行接口，带返回值
     *
     * @param <R>                  the type parameter
     * @param <C>                  the type parameter
     * @param extensionCoordinate  the extension coordinate
     * @param context              the context
     * @param interruptionStrategy the interruption strategy
     * @return the list
     */
    public <R, C> List<R> multiExecute(ExtensionCoordinate extensionCoordinate, C context, InterruptionStrategy<R> interruptionStrategy){
        return multiExecute(extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(), context,interruptionStrategy);
    }


    /**
     * 组合扩展器代理执行接口，带返回值
     *
     * @param <R>                  the type parameter
     * @param <C>                  the type parameter
     * @param targetClz            the target clz
     * @param bizScenario          the biz scenario
     * @param context              the context
     * @param interruptionStrategy the interruption strategy
     * @return the list
     */
    public <R, C> List<R> multiExecute(Class<? extends ExtensionPoint<R,C>> targetClz,BizScenario bizScenario, C context, InterruptionStrategy<R> interruptionStrategy) {
        List<ExtensionPoint> extensionPoints = locateComponents(targetClz,bizScenario, context);
        if (extensionPoints==null){
            return Collections.emptyList();
        }
        List<R> combinationResult = new ArrayList<>(extensionPoints.size());
        R result =null;
        for (ExtensionPoint exp : extensionPoints) {
            try {
                result=(R) exp.execute(context);
                combinationResult.add(result);
                if (interruptionStrategy.interrupt(result)) {
                    return combinationResult;
                }
            } catch (Exception e) {
                exp.onError(e,context);
            }
        }
        return combinationResult;
    }

    /**
     * 组合扩展器代理执行接口，不带返回值
     *
     * @param <C>                 the type parameter
     * @param extensionCoordinate the extension coordinate
     * @param context             the context
     */
    public <C> void multiExecuteVoid(ExtensionCoordinate extensionCoordinate, C context) {
        multiExecuteVoid(extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(),context);
    }

    /**
     * 组合扩展器代理执行接口，不带返回值
     *
     * @param <C>         the type parameter
     * @param targetClz   the target clz
     * @param bizScenario the biz scenario
     * @param context     the context
     */
    public <C> void multiExecuteVoid(Class<? extends ExtensionPoint<Void,C>> targetClz,BizScenario bizScenario, C context) {
        List<ExtensionPoint> extensionPoints = locateComponents(targetClz,bizScenario, context);
        if (extensionPoints==null){
            return;
        }
        for (ExtensionPoint exp : extensionPoints) {
            try {
                exp.executeVoid(context);
            } catch (Exception e) {
                exp.onError(e,context);
            }
        }
    }

    /**
     * 单扩展器函数回调执行接口，带返回值
     *
     * @param <R>         the type parameter
     * @param <T>         the type parameter
     * @param targetClz   the target clz
     * @param bizScenario the biz scenario
     * @param exeFunction the exe function
     * @return the r
     */
    public <R, T extends ExtensionPoint> R callback(Class<T> targetClz, BizScenario bizScenario, Function<T, R> exeFunction) {
        T component =(T) locateComponent(targetClz, bizScenario,null);
        return exeFunction.apply(component);
    }

    /**
     * 单扩展器函数回调执行接口，带返回值
     *
     * @param <R>                 the type parameter
     * @param <T>                 the type parameter
     * @param extensionCoordinate the extension coordinate
     * @param exeFunction         the exe function
     * @return the r
     */
    public <R, T extends ExtensionPoint> R callback(ExtensionCoordinate extensionCoordinate, Function<T, R> exeFunction){
        T component =(T) locateComponent((Class<T>) extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(),null);
        return exeFunction.apply(component);
    }

    /**
     * 组合扩展器函数回调执行接口，带返回值
     *
     * @param <R>         the type parameter
     * @param <T>         the type parameter
     * @param targetClz   the target clz
     * @param bizScenario the biz scenario
     * @param exeFunction the exe function
     * @return the r
     */
    public <R, T extends ExtensionPoint> R multiCallback(Class<T> targetClz, BizScenario bizScenario, Function<List<T>, R> exeFunction) {
        List<T> component =(List<T>) locateComponents(targetClz, bizScenario,null);
        return exeFunction.apply(component);
    }

    /**
     * 组合扩展器函数回调执行接口，带返回值
     *
     * @param <R>                 the type parameter
     * @param <T>                 the type parameter
     * @param extensionCoordinate the extension coordinate
     * @param exeFunction         the exe function
     * @return the r
     */
    public <R, T extends ExtensionPoint> R multiCallback(ExtensionCoordinate extensionCoordinate, Function<List<T>, R> exeFunction){
        List<T> component =(List<T>) locateComponents((Class<T>) extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(),null);
        return exeFunction.apply(component);
    }

    /**
     * 单一扩展器函数回调执行接口，不带返回值
     *
     * @param <T>         the type parameter
     * @param targetClz   the target clz
     * @param bizScenario the biz scenario
     * @param exeFunction the exe function
     */
    public <T extends ExtensionPoint> void callbackVoid(Class<T> targetClz, BizScenario bizScenario, Consumer<T> exeFunction) {
        T component =(T) locateComponent(targetClz, bizScenario,null);
        exeFunction.accept(component);
    }

    /**
     * 单一扩展器函数回调执行接口，不带返回值
     *
     * @param <T>                 the type parameter
     * @param extensionCoordinate the extension coordinate
     * @param exeFunction         the exe function
     */
    public <T extends ExtensionPoint> void callbackVoid(ExtensionCoordinate extensionCoordinate, Consumer<T> exeFunction) {
        T component =(T) locateComponent((Class<T>) extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(),null);
        exeFunction.accept(component);
    }

    /**
     * 组合扩展器函数回调执行接口，不带返回值
     *
     * @param <T>         the type parameter
     * @param targetClz   the target clz
     * @param bizScenario the biz scenario
     * @param exeFunction the exe function
     */
    public <T extends ExtensionPoint> void multiCallbackVoid(Class<T> targetClz, BizScenario bizScenario, Consumer<List<T>> exeFunction) {
        List<T> component =(List<T>) locateComponents(targetClz, bizScenario,null);
        exeFunction.accept(component);
    }

    /**
     * 组合扩展器函数回调执行接口，不带返回值
     *
     * @param <T>                 the type parameter
     * @param extensionCoordinate the extension coordinate
     * @param exeFunction         the exe function
     */
    public <T extends ExtensionPoint> void multiCallbackVoid(ExtensionCoordinate extensionCoordinate, Consumer<List<T>> exeFunction) {
        List<T> component =(List<T>) locateComponents((Class<T>) extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(),null);
        exeFunction.accept(component);
    }

    public boolean isNotFoundThrowError() {
        return isNotFoundThrowError;
    }

    public void setNotFoundThrowError(boolean notFoundThrowError) {
        isNotFoundThrowError = notFoundThrowError;
    }

    /**
     * 加载扩展实现
     *
     * @param <C>         the type parameter
     * @param targetClz   the target clz
     * @param bizScenario the biz scenario
     * @param context     the context
     * @return the extension point
     */
    public abstract <C> ExtensionPoint locateComponent(Class<? extends ExtensionPoint> targetClz, BizScenario bizScenario, C context);

    /**
     * 加载多个扩展点实现
     *
     * @param <C>         the type parameter
     * @param targetClz   the target clz
     * @param bizScenario the biz scenario
     * @param context     the context
     * @return the list
     */
    public abstract <C> List<ExtensionPoint> locateComponents(Class<? extends ExtensionPoint> targetClz, BizScenario bizScenario, C context);
}
