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
import com.hhao.common.mybatis.page.executor.sql.SqlExecutor;
import com.hhao.common.mybatis.page.executor.sql.SqlExecutorBuilder;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.Invocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 需要动态处理SQL的分页执行器(处理select,count)
 *
 * @author Wang
 * @since 1.0.0
 */
public class DynamicPageExecutor extends AbstractPageExecutor {
    /**
     * Instantiates a new Dynamic page executor.
     */
    public DynamicPageExecutor() {
        super(new PageExecutorType[]{PageExecutorType.DYNAMIC_EXCLUDE_COUNT,PageExecutorType.DYNAMIC_INCLUDE_COUNT});
    }

    /**
     * 根据PageInfo判断是否支持该次拦截处理
     */
    @Override
    protected boolean support(PageInfo pageInfo, MappedStatement mappedStatement) {
        SqlExecutor sqlExecutor=getSqlExecutor(pageInfo,mappedStatement);
        if (sqlExecutor!=null){
            pageInfo.setSqlExecutor(sqlExecutor);
            return true;
        }
        return false;
    }

    /**
     * 拦截处理过程
     */
    @Override
    protected Object execute(PageInfo pageInfo, Invocation invocation) throws Throwable{
        SqlExecutor sqlExecutor=pageInfo.getSqlExecutor();
        MappedStatement mappedStatement=this.getMappedStatement(invocation);
        Object parameter=this.getParameter(invocation);

        //构建新的SqlSource
        SqlSource newSqlSource=this.newSqlSource(pageInfo,mappedStatement,parameter,sqlExecutor);

        //用新的SqlSource构建MappedStatement
        MappedStatement newMappedStatement=newMappedStatement(pageInfo,mappedStatement,newSqlSource);

        //用新的MappedStatement替换原来的MappedStatement
        resetMappedStatement(invocation,newMappedStatement);

        //继续向下执行,返回查询结果集和行数结果集
        Object result = invocation.proceed();

        //对结果集进行处理
        return this.doResult(pageInfo, result);
    }

    /**
     * 获取SQL执行器
     * 如果PageInfo中有定义，直接采用定义的
     * @param pageInfo
     * @param mappedStatement
     * @return
     */
    private SqlExecutor getSqlExecutor(PageInfo pageInfo,MappedStatement mappedStatement){
        SqlExecutor sqlExecutor=pageInfo.getSqlExecutor();
        if (sqlExecutor!=null){
            return sqlExecutor;
        }
        return SqlExecutorBuilder.findSqlExecutor(pageInfo,this.getDatabaseId(mappedStatement),pageInfo.getPageExecutorType());
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
        //原来select的BoundSql
        BoundSql selectBoundSql=mappedStatement.getBoundSql(parameter);
        //原来select的参数
        List<Object> selectParameterMappings= Collections.unmodifiableList(selectBoundSql.getParameterMappings());

        //select语句处理
        String newSql=sqlExecutor.selectSql(selectBoundSql.getSql());
        //处理后的参数映射
        List<Object> newParamMappings=sqlExecutor.selectParamMappings(pageInfo,newSql,selectParameterMappings);

        //是否添加count
        if (this.isIncludeCount(pageInfo.getPageExecutorType())){
            //返回count语句
            String countSql=sqlExecutor.countSql(newSql);
            //返因count语句的参数映射
            List<Object> countParamMappings=sqlExecutor.countParamMappings(pageInfo,countSql,Collections.unmodifiableList(newParamMappings));

            //合并查询语句与count语句
            newSql=newSql + sqlExecutor.getSqlSeparator() + countSql;

            //合并select、count语句参数映射
            for(Object param : countParamMappings){
               newParamMappings.add(param);
            }
        }

        //将ParamMapping转成ParameterMapping
        List<ParameterMapping> newParameterMappings=new ArrayList<>();
        for(Object param :newParamMappings){
            if (param instanceof  ParameterMapping){
                newParameterMappings.add((ParameterMapping)param);
            }else if(param instanceof ParamMapping){
                newParameterMappings.add(new ParameterMapping.Builder(mappedStatement.getConfiguration(), ((ParamMapping) param).getProperty(),((ParamMapping) param).getJavaType()).build());
            }else{
                throw new RuntimeException("only support ParameterMapping or ParamMapping");
            }
        }

        //生成新的BoundSql
        BoundSql newBoundSql=new BoundSql(mappedStatement.getConfiguration(),
                newSql, newParameterMappings,
                selectBoundSql.getParameterObject());

        //设置在修改select、count时新添加带值的参数
        for(Object param :newParamMappings){
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
