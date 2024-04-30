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

package com.hhao.common.springboot.safe.decode;

/**
 * 解码执行器
 *
 * @author Wang
 * @since 1.0.0
 */
public interface DecodeHandler {

    /**
     * 根据名称判断是否支持该类解码{@code @SafeHtml的decode与该name对应}
     *
     * @param name the name
     * @return boolean
     */
    boolean support(String name);

    /**
     * 解码操作
     *
     * @param content the content
     * @return string
     */
    String decode(String content);
}
