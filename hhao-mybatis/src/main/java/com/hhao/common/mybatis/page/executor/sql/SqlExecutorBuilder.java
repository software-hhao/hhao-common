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

package com.hhao.common.mybatis.page.executor.sql;

import com.hhao.common.mybatis.page.Page;
import com.hhao.common.mybatis.page.PageInfo;
import com.hhao.common.mybatis.page.executor.PageExecutor;
import com.hhao.common.mybatis.page.executor.PageExecutorType;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * SQL执行器构建器
 * 对于可以复用的sql执行器，可以注册到SqlExecutorBuilder
 * 执行过程通过SQL执行器的support判断是否支持本次拦截处理
 *
 * @author Wang
 * @since 1.0.0
 */
public class SqlExecutorBuilder {
    private static List<SqlExecutor> executors=new CopyOnWriteArrayList<>();

    /**
     * Register.
     *
     * @param sqlExecutor the sql executor
     */
    public static void register(SqlExecutor sqlExecutor){
        executors.add(sqlExecutor);
    }

    /**
     * Register.
     *
     * @param sqlExecutors the sql executors
     */
    public static void register(List<SqlExecutor> sqlExecutors){
        for(SqlExecutor se:sqlExecutors){
            executors.add(se);
        }
    }

    /**
     * Find sql executor sql executor.
     *
     * @param pageInfo     the page info
     * @param databaseId   the database id
     * @param executorType the executor type
     * @return the sql executor
     */
    public static SqlExecutor findSqlExecutor(PageInfo pageInfo, String databaseId, PageExecutorType executorType){
        for(SqlExecutor se:executors){
            if (se.support(pageInfo,databaseId,executorType)){
                return se;
            }
        }
        return null;
    }
}
