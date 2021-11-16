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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 分页拦截器构建类
 *
 * @author Wang
 * @since 1.0.0
 */
public class PageExecutorBuilder {
    private static List<PageExecutor> executors=new CopyOnWriteArrayList<>();

    /**
     * Register.
     *
     * @param pageExecutor the page executor
     */
    public static void register(PageExecutor pageExecutor){
        executors.add(pageExecutor);
    }

    /**
     * Register.
     *
     * @param pageExecutors the page executors
     */
    public static void register(List<PageExecutor> pageExecutors){
        for(PageExecutor pe:pageExecutors){
            executors.add(pe);
        }
    }

    /**
     * Find page executor page executor.
     *
     * @param invocation the invocation
     * @return the page executor
     */
    public static PageExecutor findPageExecutor(Invocation invocation){
        for(PageExecutor pe:executors){
            if (pe.support(invocation)){
                return pe;
            }
        }
        return null;
    }
}
