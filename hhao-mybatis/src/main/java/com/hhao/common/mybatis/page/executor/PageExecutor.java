/*
 * Copyright 2018-2021 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.mybatis.page.executor;

import org.apache.ibatis.plugin.Invocation;

/**
 * 分页处理器
 *
 * @author Wang
 * @since 1.0.0
 */
public interface PageExecutor {
    /**
     * 是否支持本次拦截
     *
     * @param invocation the invocation
     * @return boolean
     */
    boolean support(Invocation invocation);

    /**
     * 拦截处理
     *
     * @param invocation the invocation
     * @return object
     * @throws Throwable the throwable
     */
    Object execute(Invocation invocation) throws Throwable;
}
