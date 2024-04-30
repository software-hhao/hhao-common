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

package com.hhao.common.mybatis.page.executor;

import com.hhao.common.mybatis.page.PageInfo;
import com.hhao.common.mybatis.page.executor.sql.SqlExecutor;
import org.apache.ibatis.plugin.Invocation;

/**
 * 分页处理器
 *
 * @author Wang
 * @since 1.0.0
 */
public interface PageExecutor {

    SqlExecutor getSqlExecutor(PageInfo pageInfo,String dbName);

    /**
     * 拦截处理
     *
     * @param invocation the invocation
     * @param pageInfo   the page info
     * @return object object
     * @throws Throwable the throwable
     */
    Object execute(Invocation invocation, PageInfo pageInfo) throws Throwable;
}
