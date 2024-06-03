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
import com.hhao.common.mybatis.page.PageMetaData;
import com.hhao.common.mybatis.page.executor.sql.SqlExecutor;
import com.hhao.common.mybatis.page.executor.sql.SqlPageModel;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
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
 * 多语句动态分页执行器
 * 适用于JDBC支持多语句执行的情况
 * 执行器对以下内容进行处理
 * 1、对select语句进行分析，如果未分页，则加入分页，并对查询参数进行检查、补齐
 * 2、对count语句进行分析，如果不存在，则自动生成，并对参数进行检查、删除或补齐
 * 如果同时定义select与count语句，以多语句定义的方式，定义在同一个查询块中
 * 该执行器比较灵活，但受到不同数据库语言差异的影响，效率次于MultiQueriesStaticPageExecutor
 *
 * @author Wang
 * @since 1.0.0
 */
public class MultiQueriesDynamicPageExecutor extends AbstractPageExecutor {
    /**
     * The Logger.
     */
    protected final Log logger = LogFactory.getLog(this.getClass());
    /**
     * 拦截处理过程
     */
    @Override
    public Object execute(Invocation invocation,PageInfo pageInfo) throws Throwable{
        MappedStatement mappedStatement=this.getMappedStatement(invocation);
        SqlExecutor sqlExecutor=this.getSqlExecutor(pageInfo,this.getDatabaseId(mappedStatement));
        Object parameter=this.getParameter(invocation);
        //构建新的SqlSource
        SqlSource newSqlSource=this.newSqlSource(pageInfo,mappedStatement,parameter,sqlExecutor);
        //用新的SqlSource构建MappedStatement
        MappedStatement newMappedStatement=buildMappedStatement(mappedStatement,newSqlSource,this.buildResultMap(pageInfo,mappedStatement));
        //用新的MappedStatement替换原来的MappedStatement
        resetMappedStatement(invocation,newMappedStatement);
        //继续向下执行,返回查询结果集和行数结果集
        Object result = invocation.proceed();
        //对结果集进行处理
        result=this.doResult(pageInfo, result);
        //分页溢出处理
        if (pageInfo.isIncludeTotalRows()){
            BoundSql boundSql=newSqlSource.getBoundSql(parameter);
            //调整分页参数
            if (pageOverflowToLast(pageInfo,newMappedStatement,parameter,boundSql)){
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

    /**
     * 构建新的SqlSource
     * @param pageInfo
     * @param mappedStatement
     * @param parameter
     * @param sqlExecutor
     * @return
     */
    private SqlSource newSqlSource(PageInfo pageInfo,MappedStatement mappedStatement, Object parameter, SqlExecutor sqlExecutor){
        String dbName=this.getDatabaseId(mappedStatement);
        //原来select的BoundSql
        BoundSql selectBoundSql=mappedStatement.getBoundSql(parameter);
        //原来select的参
        List<Object> selectParameterMappings= Collections.unmodifiableList(selectBoundSql.getParameterMappings());
        SqlPageModel sqlPageModel=sqlExecutor.generalSqlPageModel(pageInfo,selectBoundSql.getSql(),selectParameterMappings,dbName);
        //生成sql语句
        StringBuffer sql=new StringBuffer();
        sql.append(sqlPageModel.getSelect().getSql());
        if (sqlPageModel.getCount()!=null){
            sql.append(";");
            sql.append(sqlPageModel.getCount().getSql());
        }
        //参数处理
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
        if (sqlPageModel.getCount()!=null){
            for(Object param :sqlPageModel.getCount().getParams()){
                if (param instanceof  ParameterMapping){
                    parameterMappings.add((ParameterMapping)param);
                }else if(param instanceof ParamMapping){
                    parameterMappings.add(new ParameterMapping.Builder(mappedStatement.getConfiguration(), ((ParamMapping) param).getProperty(),((ParamMapping) param).getJavaType()).build());
                }else{
                    throw new RuntimeException("only support ParameterMapping or ParamMapping");
                }
            }
        }
        //生成新的BoundSql
        BoundSql newBoundSql=new BoundSql(mappedStatement.getConfiguration(),
                sql.toString(), parameterMappings,
                selectBoundSql.getParameterObject());
        //设置在修改select、count时新添加带值的参数
        for(Object param :parameterMappings){
            if(param instanceof ParamMapping){
                newBoundSql.setAdditionalParameter(((ParamMapping) param).getProperty(),((ParamMapping) param).getValue());
           }
        }
        //生成新的SqlSource
        return new SqlSource() {
            @Override
            public BoundSql getBoundSql(Object parameterObject) {
                return newBoundSql;
            }
        };
    }
}
