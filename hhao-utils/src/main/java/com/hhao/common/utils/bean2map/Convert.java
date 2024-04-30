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

package com.hhao.common.utils.bean2map;

/**
 * 类型转换
 *
 * @param <S> the type parameter
 * @param <T> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public interface Convert<S,T> {
     /**
      * 是否支持
      *
      * @param sourceClass :源类型
      * @param targetClass :目标类型
      * @return boolean
      */
     boolean support(Class<S> sourceClass,Class<T> targetClass);

     /**
      * 将源类型值转换成目标类型值
      *
      * @param <R>         the type parameter
      * @param source      the source
      * @param targetClass the target class
      * @return r
      */
     <R extends T> R convert(S source,Class<R> targetClass);
}
