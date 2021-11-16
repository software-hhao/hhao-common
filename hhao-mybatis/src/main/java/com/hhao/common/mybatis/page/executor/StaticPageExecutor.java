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

import com.hhao.common.mybatis.page.PageInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.ExecutorType;

/**
 * 静态分页执行器,即只判断添加count结果集,对包含count的结果集进行处理
 *
 * @author Wang
 * @since 1.0.0
 */
public class StaticPageExecutor extends AbstractPageExecutor {
    /**
     * Instantiates a new Static page executor.
     */
    public StaticPageExecutor() {
        super(new PageExecutorType[]{PageExecutorType.STATIC_INCLUDE_COUNT});
    }

    @Override
    protected Object execute(PageInfo pageInfo, Invocation invocation) throws Throwable {
        MappedStatement mappedStatement=this.getMappedStatement(invocation);

        //用新的SqlSource构建MappedStatement
        MappedStatement newMappedStatement=newMappedStatement(pageInfo,mappedStatement,mappedStatement.getSqlSource());

        //用新的MappedStatement替换原来的MappedStatement
        resetMappedStatement(invocation,newMappedStatement);

        //继续向下执行,返回查询结果集和行数结果集
        Object result = invocation.proceed();

        //对结果集进行处理
        return this.doResult(pageInfo, result);
    }
}
