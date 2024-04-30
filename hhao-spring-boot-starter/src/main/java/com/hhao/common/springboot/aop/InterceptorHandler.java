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
package com.hhao.common.springboot.aop;

import org.aopalliance.intercept.Invocation;
import org.springframework.core.Ordered;

/**
 * 拦截器
 *
 * @author Wang
 * @since 1.0.0
 */
public interface InterceptorHandler extends Ordered {
    /**
     * 顺序
     *
     * @return
     */
    @Override
    default int getOrder(){
        return 0;
    }

    /**
     * 预处理
     *
     * @param invocation the invocation
     * @return true表示流程继续 ，false表示流程中断
     */
    default boolean onBegin(Invocation invocation){
        return true;
    }

    /**
     * 执行结束后处理
     * 如果执行失败，error不为空，否则为成功
     *
     * @param result     the result
     * @param error      the error
     * @param invocation the invocation
     * @return the object
     */
    default Object onComplete(Object result,Throwable error,Invocation invocation){
        return result;
    }

    /**
     * 判断是否执行该拦截器
     * id与@Aop定义的一致
     *
     * @param id the id
     * @return the boolean
     */
    boolean support(String id);
}
