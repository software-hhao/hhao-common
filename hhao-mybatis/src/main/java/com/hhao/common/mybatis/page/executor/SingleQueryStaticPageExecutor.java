
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
import com.hhao.common.mybatis.page.PageInfoWithCount;
import com.hhao.common.utils.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Collections;
import java.util.List;

/**
 * 单语句静态分页执行器
 * 适用于JDBC只支持单语句执行的情况
 * 由用户自己定义分页查询语句与count语句，执行器完成count语句的入参与织入
 * count语句通过PageInfoWithCount#countMappedStatementId指定
 * 该执行器不受数据库语言差异的影响，分页查询语句及count语句完全由用户提供，执行效率较高
 *
 * @author Wang
 * @since 2021 /11/22 20:23
 */
public class SingleQueryStaticPageExecutor extends AbstractPageExecutor {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private final String DOT=".";


    @Override
    public Object execute(Invocation invocation, PageInfo pageInfo) throws Throwable {
        //取出各种参数
        Executor executor=this.getExecutor(invocation);
        if (executor==null){
            logger.debug("SingleQueryStaticPageExecutor need org.apache.ibatis.executor.Executor,Please change other PageExecutor.");
            return invocation.proceed();
        }
        MappedStatement mappedStatement=this.getMappedStatement(invocation);
        RowBounds rowBounds=this.getRowBounds(invocation);
        ResultHandler resultHandler=this.getResultHandler(invocation);
        Object parameter=this.getParameter(invocation);
        BoundSql boundSql=mappedStatement.getBoundSql(parameter);
        List<Object> parameterMappings= Collections.unmodifiableList(boundSql.getParameterMappings());

        if (pageInfo.isIncludeTotalRows()) {
            //获取count的MappedStatement
            MappedStatement countMs = findCountMappedStatement(mappedStatement, ((PageInfoWithCount) pageInfo).getCountMappedStatementId());
            if (countMs != null) {
                BoundSql countSql = countMs.getBoundSql(parameter);
                CacheKey cacheKey = executor.createCacheKey(countMs, parameter, rowBounds, countSql);
                List<Object> result = executor.query(countMs, parameter, rowBounds, resultHandler, cacheKey, countSql);
                setCountResult(pageInfo, result);
                //分页溢出处理
                pageOverflowToLast(pageInfo,mappedStatement,parameter,boundSql);
            }
        }
        //对结果集进行处理
        return setPageResult(pageInfo, invocation.proceed());
    }



    /**
     * Find count mapped statement mapped statement.
     *
     * @param ms      the ms
     * @param countId the count id
     * @return the mapped statement
     */
    protected MappedStatement findCountMappedStatement(MappedStatement ms, String countId) {
        if (StringUtils.hasText(countId)) {
            final String id = ms.getId();
            if (!countId.contains(DOT)) {
                countId = id.substring(0, id.lastIndexOf(DOT) + 1) + countId;
            }
            final Configuration configuration = ms.getConfiguration();
            try {
                return configuration.getMappedStatement(countId, false);
            } catch (Exception e) {
                logger.warn(String.format("can not find this countId: [\"%s\"]", countId));
            }
        }
        return null;
    }
}
