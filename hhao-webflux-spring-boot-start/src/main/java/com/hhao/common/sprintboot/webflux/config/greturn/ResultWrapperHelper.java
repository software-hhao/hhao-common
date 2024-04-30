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

package com.hhao.common.sprintboot.webflux.config.greturn;

import com.hhao.common.springboot.response.ResponseAutoWrapper;
import com.hhao.common.springboot.response.ResultWrapper;
import com.hhao.common.springboot.response.ResultWrapperBuilder;
import com.hhao.common.springboot.response.UnResultWrapper;
import org.springframework.core.MethodParameter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Wang
 * @since 2022/1/15 14:43
 */
public class ResultWrapperHelper {
    // 私有的缓存，现在是实例级别的
    private final ConcurrentMap<Method, Boolean> cache = new ConcurrentHashMap<>();

    // 私有的构造方法，防止外部直接实例化
    private ResultWrapperHelper() {}

    // 单例实例
    private static class SingletonHolder {
        private static final ResultWrapperHelper INSTANCE = new ResultWrapperHelper();
    }

    // 获取单例实例的方法
    public static ResultWrapperHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 判断是否支持自动封装
     * @param returnType
     * @return
     */
    public boolean supportsAutoWrapping(MethodParameter returnType) {
        return cache.computeIfAbsent(returnType.getMethod(), k -> {
            // 计算是否支持自动封装的逻辑，用于缓存计算。
            // 如果继承自UnResultWrapper,则不支持自动包装
            if (returnType.getMethod() != null && returnType.getMethod().getReturnType() != null
                    && returnType.getMethod().getReturnType().isAssignableFrom(UnResultWrapper.class)) {
                return false;
            }

            // 判断是否有ResponseAutoWrapper的注解
            ResponseAutoWrapper responseAutoWrapper = returnType.getMethod().getAnnotation(ResponseAutoWrapper.class);
            if (responseAutoWrapper == null) {
                responseAutoWrapper = returnType.getDeclaringClass().getAnnotation(ResponseAutoWrapper.class);
            }

            // 根据注解的存在与值决定是否支持
            return responseAutoWrapper != null && responseAutoWrapper.value();
        });
    }

    public Object wrapperResult(Object result){
        if (result instanceof Mono){
            //如果是Mono，将内容封装
            result=((Mono)result).map(data-> {
                if (!(data instanceof ResultWrapper)){
                    return ResultWrapperBuilder.ok(data);
                }
                return data;
            });
        }else if (result instanceof Flux){
            //如果是Flux，则取集合后封装
            result=((Flux)result).collectList().map(data->ResultWrapperBuilder.ok(data));
        }else if (!(result instanceof ResultWrapper)){
            //一般类型
            result=ResultWrapperBuilder.ok(result);
        }
        return result;
    }

}
