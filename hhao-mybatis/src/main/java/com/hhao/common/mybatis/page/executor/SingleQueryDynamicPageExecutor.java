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

import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.mybatis.page.PageInfo;
import com.hhao.common.mybatis.page.executor.sql.SqlExecutor;
import com.hhao.common.mybatis.page.executor.sql.SqlPageModel;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 单语句动态分页执行器
 * 适用于JDBC只支持单语句执行的情况
 * 执行器对以下内容进行处理
 * 1、对select语句进行分析，如果未分页，则加入分页，并对查询参数进行检查、补齐
 * 2、对count语句进行分析，如果不存在，则自动生成，并对参数进行检查、删除或补齐
 * 构建一个新的MappedStatement执行count语句
 * 该执行器比较灵活，但受到不同数据库语言差异的影响，效率次于SingleQueryStaticPageExecutor
 *
 * @author Wang
 * @since 1.0.0
 */
public class SingleQueryDynamicPageExecutor extends AbstractPageExecutor {
    /**
     * The Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(SingleQueryStaticPageExecutor.class);


    @Override
    public Object execute(Invocation invocation, PageInfo pageInfo) throws Throwable {
        //取出各种参数
        Executor executor=this.getExecutor(invocation);
        if (executor==null){
            log.info("SingleQueryDynamicPageExecutor need org.apache.ibatis.executor.Executor,Please change other PageExecutor.");
            return invocation.proceed();
        }
        MappedStatement mappedStatement=this.getMappedStatement(invocation);
        RowBounds rowBounds=this.getRowBounds(invocation);
        ResultHandler resultHandler=this.getResultHandler(invocation);
        Object parameter=this.getParameter(invocation);
        BoundSql boundSql=mappedStatement.getBoundSql(parameter);
        List<Object> parameterMappings= Collections.unmodifiableList(boundSql.getParameterMappings());
        //获取SqlExecutor
        SqlExecutor sqlExecutor = this.getSqlExecutor(pageInfo, this.getDatabaseId(mappedStatement));
        //解析SQL语句
        //生成的SqlPageModel包含select语句、select语句的入参;count语句、count语句的入参
        String dbName=this.getDatabaseId(mappedStatement);
        SqlPageModel sqlPageModel=sqlExecutor.generalSqlPageModel(pageInfo,boundSql.getSql(),parameterMappings,dbName);
        //select分页处理
        SqlSource pageSqlSource=buildPageSqlSource(pageInfo,sqlPageModel,mappedStatement,parameter);
        //用新的SqlSource构建MappedStatement
        MappedStatement pageMappedStatement=buildMappedStatement(mappedStatement,pageSqlSource,mappedStatement.getResultMaps());
        //用新的MappedStatement替换原来的MappedStatement
        resetMappedStatement(invocation,pageMappedStatement);
        //count处理
        if (pageInfo.isIncludeTotalRows()) {
            SqlSource countSqlSource = buildCountSqlSource(pageInfo, sqlPageModel, mappedStatement, parameter);
            MappedStatement countMappedStatement = buildMappedStatement(mappedStatement, countSqlSource, addCountResultMap(new ArrayList<>()));
            BoundSql countSql=countMappedStatement.getBoundSql(parameter);
            CacheKey cacheKey = executor.createCacheKey(countMappedStatement, parameter, rowBounds, countSql);
            List<Object> result = executor.query(countMappedStatement, parameter, rowBounds, resultHandler, cacheKey, countSql);
            setCountResult(pageInfo, result);
            //分页溢出处理
            pageOverflowToLast(pageInfo,pageMappedStatement,parameter,pageSqlSource.getBoundSql(parameter));
        }
        //对结果集进行处理
        return setPageResult(pageInfo, invocation.proceed());
    }

    private SqlSource buildPageSqlSource(PageInfo pageInfo,SqlPageModel sqlPageModel,MappedStatement mappedStatement, Object parameter){
        //原来select的BoundSql
        BoundSql boundSql=mappedStatement.getBoundSql(parameter);
        //将ParamMapping转成ParameterMapping
        List<ParameterMapping> parameterMappings=new ArrayList<>();
        for(Object param :sqlPageModel.getSelect().getParams()){
            if (param instanceof  ParameterMapping){
                parameterMappings.add((ParameterMapping)param);
            }else if(param instanceof ParamMapping){
                parameterMappings.add(new ParameterMapping.Builder(mappedStatement.getConfiguration(), ((ParamMapping) param).getProperty(),((ParamMapping) param).getJavaType()).build());
            }else{
                throw new RuntimeException("only support ParameterMapping or ParamMapping");
            }
        }
        //生成新的BoundSql
        BoundSql pageBoundSql=new BoundSql(mappedStatement.getConfiguration(),
                sqlPageModel.getSelect().getSql(), parameterMappings,
                boundSql.getParameterObject());
        //设置在修改select、count时新添加带值的参数
        for(Object param :parameterMappings){
            if(param instanceof ParamMapping){
                pageBoundSql.setAdditionalParameter(((ParamMapping) param).getProperty(),((ParamMapping) param).getValue());
            }
        }
        //生成新的SqlSource
        return new SqlSource() {
            @Override
            public BoundSql getBoundSql(Object parameterObject) {
                return pageBoundSql;
            }
        };
    }

    private SqlSource buildCountSqlSource(PageInfo pageInfo,SqlPageModel sqlPageModel,MappedStatement mappedStatement, Object parameter){
        //原来select的BoundSql
        if (sqlPageModel.getCount()==null){
            return null;
        }
        BoundSql boundSql=mappedStatement.getBoundSql(parameter);
        //将ParamMapping转成ParameterMapping
        List<ParameterMapping> parameterMappings=new ArrayList<>();
        for(Object param :sqlPageModel.getCount().getParams()){
            if (param instanceof  ParameterMapping){
                parameterMappings.add((ParameterMapping)param);
            }else if(param instanceof ParamMapping){
                parameterMappings.add(new ParameterMapping.Builder(mappedStatement.getConfiguration(), ((ParamMapping) param).getProperty(),((ParamMapping) param).getJavaType()).build());
            }else{
                throw new RuntimeException("only support ParameterMapping or ParamMapping");
            }
        }
        //生成新的BoundSql
        BoundSql countBoundSql=new BoundSql(mappedStatement.getConfiguration(),
                sqlPageModel.getCount().getSql(), parameterMappings,
                boundSql.getParameterObject());
        //设置在修改select、count时新添加带值的参数
        for(Object param :parameterMappings){
            if(param instanceof ParamMapping){
                countBoundSql.setAdditionalParameter(((ParamMapping) param).getProperty(),((ParamMapping) param).getValue());
            }
        }
        //生成新的SqlSource
        return new SqlSource() {
            @Override
            public BoundSql getBoundSql(Object parameterObject) {
                return countBoundSql;
            }
        };
    }
}
