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
import com.hhao.common.mybatis.page.PageMetaData;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * 多语句静态分页执行器
 * 适用于JDBC支持多语句执行的情况
 * 由用户自己定义分页查询语句与count语句，执行器完成count语句的入参与织入
 * count语句通过多语句方式与查询语句定义在同一块中
 * 该执行器不受数据库语言差异的影响，分页查询语句及count语句完全由用户提供，执行效率最高
 *
 * @author Wang
 * @since 1.0.0
 */
public class MultiQueriesStaticPageExecutor extends AbstractPageExecutor {
    /**
     * The Logger.
     */
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public Object execute(Invocation invocation,PageInfo pageInfo) throws Throwable {
        MappedStatement mappedStatement=this.getMappedStatement(invocation);

        //用新的SqlSource构建MappedStatement
        MappedStatement newMappedStatement=buildMappedStatement(mappedStatement,mappedStatement.getSqlSource(),this.buildResultMap(pageInfo,mappedStatement));

        //用新的MappedStatement替换原来的MappedStatement
        resetMappedStatement(invocation,newMappedStatement);

        //继续向下执行,返回查询结果集和行数结果集
        Object result = invocation.proceed();

        //对结果集进行处理
        result=this.doResult(pageInfo, result);

        //分页溢出处理
        if (PageMetaData.PAGE_OVERFLOW_TO_LAST){
            Object parameter=this.getParameter(invocation);
            BoundSql boundSql=mappedStatement.getBoundSql(parameter);
            //调整分页参数
            if (pageOverflowToLast(pageInfo,mappedStatement,parameter,boundSql)){
                //重新执行一遍
                Executor executor=this.getExecutor(invocation);
                RowBounds rowBounds=this.getRowBounds(invocation);
                ResultHandler resultHandler=this.getResultHandler(invocation);

                CacheKey cacheKey = executor.createCacheKey(newMappedStatement, parameter, rowBounds, boundSql);
                result = executor.query(newMappedStatement, parameter, rowBounds, resultHandler, cacheKey, boundSql);
                result=this.doResult(pageInfo, result);
            }
        }

        return result;
    }
}
