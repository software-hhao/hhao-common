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

package com.hhao.common.jackson.view;

/**
 * 用于@jsonformat注解分类
 *
 * @author Wang
 * @since 1.0.0
 */
public class Views {
    /**
     * 默认
     */
    public static class Default{}

    /**
     * 内部公开
     */
    public static class Public extends Default {}

    /**
     * 网络上公开的
     */
    public static class Internal extends Public {}
}
