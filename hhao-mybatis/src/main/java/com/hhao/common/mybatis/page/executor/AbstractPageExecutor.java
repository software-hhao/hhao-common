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
import com.hhao.common.mybatis.page.executor.sql.SqlExecutor;
import com.hhao.common.mybatis.page.executor.sql.SqlExecutorFactory;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分页执行器基类
 *
 * @author Wang
 * @since 1.0.0
 */
public abstract class AbstractPageExecutor implements PageExecutor {
    /**
     * The Logger.
     */
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public SqlExecutor getSqlExecutor(PageInfo pageInfo,String dbName){
        return SqlExecutorFactory.getSqlExecutor(pageInfo,dbName);
    }

    /**
     * 返回MappedStatement
     *
     * @param invocation the invocation
     * @return mapped statement
     */
    protected MappedStatement getMappedStatement(Invocation invocation) {
        return (MappedStatement) invocation.getArgs()[0];
    }

    /**
     * 替换原来的MappedStatement
     *
     * @param invocation      the invocation
     * @param mappedStatement the mapped statement
     */
    protected void resetMappedStatement(Invocation invocation, MappedStatement mappedStatement) {
        invocation.getArgs()[0] = mappedStatement;
    }

    /**
     * 返回输入参数
     *
     * @param invocation the invocation
     * @return parameter parameter
     */
    protected Object getParameter(Invocation invocation) {
        return invocation.getArgs()[1];
    }

    /**
     * 返回分页行
     *
     * @param invocation the invocation
     * @return row bounds
     */
    protected RowBounds getRowBounds(Invocation invocation) {
        return (RowBounds) invocation.getArgs()[2];
    }

    /**
     * 返回ResultHandler
     *
     * @param invocation the invocation
     * @return result handler
     */
    protected ResultHandler getResultHandler(Invocation invocation) {
        return (ResultHandler) invocation.getArgs()[3];
    }


    /**
     * Get executor executor.
     *
     * @param invocation the invocation
     * @return the executor
     */
    protected Executor getExecutor(Invocation invocation){
        Object target=invocation.getTarget();
        if (target instanceof Executor) {
            return (Executor)target;
        }
        return null;
    }

    /**
     * 处理结果集
     *
     * @param pageInfo the page info
     * @param result   the result
     * @return object object
     */
    protected Object doResult(PageInfo pageInfo, Object result) {
        //包含count语句的结果集
        if (pageInfo.isIncludeTotalRows()){
            List<List<Object>> newResult = (List<List<Object>>) result;
            setPageResult(pageInfo,newResult.get(0));
            setCountResult(pageInfo,newResult.get(1));
            return newResult.get(0);
        }else{
            //不包含count语句的结果集
            setPageResult(pageInfo,result);
            pageInfo.setTotalRow(-1);
            return result;
        }
    }

    /**
     * Set page result list.
     *
     * @param pageInfo the page info
     * @param result   the result
     * @return the list
     */
    protected List<Object> setPageResult(PageInfo pageInfo, Object result){
        List<Object> newResult = (List<Object>) result;
        pageInfo.setData(newResult);
        return newResult;
    }

    /**
     * Set count result long.
     *
     * @param pageInfo the page info
     * @param result   the result
     * @return the long
     */
    protected Long setCountResult(PageInfo pageInfo, Object result){
        List<Object> newResult = (List<Object>) result;
        long total = 0;
        if (newResult!=null && !newResult.isEmpty()){
            // 个别数据库 count 没数据不会返回 0
            Object o = newResult.get(0);
            if (o != null) {
                total = Long.parseLong(o.toString());
            }
        }
        pageInfo.setTotalRow(total);
        return total;
    }


    private String arrayToString(String [] arrays){
        String result="";
        if (arrays!=null) {
            for (String s : arrays) {
                if (result.isBlank()) {
                    result = s;
                } else {
                    result = result + "," + s;
                }
            }
        }
        return result;
    }

    /**
     * 构建新的MappedStatement
     *
     * @param ms         the ms
     * @param sqlSource  the sql source
     * @param resultMaps the result maps
     * @return the mapped statement
     */
    protected MappedStatement buildMappedStatement(MappedStatement ms, SqlSource sqlSource,List<ResultMap> resultMaps){
        //构建新的MappedStatement
        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), sqlSource, ms.getSqlCommandType())
                .resource(ms.getResource())
                .fetchSize(ms.getFetchSize())
                .timeout(ms.getTimeout())
                .statementType(ms.getStatementType())
                .keyGenerator(ms.getKeyGenerator())
                .keyProperty(arrayToString(ms.getKeyProperties()))
                .keyColumn(arrayToString(ms.getKeyColumns()))
                .databaseId(ms.getDatabaseId())
                .lang(ms.getLang())
                .resultOrdered(ms.isResultOrdered())
                .resultSets(arrayToString(ms.getResultSets()))
                .resultMaps(resultMaps)
                .resultSetType(ms.getResultSetType())
                .flushCacheRequired(ms.isFlushCacheRequired())
                .useCache(ms.isUseCache())
                .cache(ms.getCache())
                .parameterMap(ms.getParameterMap());

        return statementBuilder.build();
    }


    /**
     * Build result map list.
     *
     * @param pageInfo the page info
     * @param ms       the ms
     * @return the list
     */
    protected List<ResultMap> buildResultMap(PageInfo pageInfo,MappedStatement ms) {
        List<ResultMap> resultMaps=copyResultMap(ms);
        if (pageInfo.isIncludeTotalRows()){
            addCountResultMap(resultMaps);
        }
        return resultMaps;
    }

    /**
     * Copy result map list.
     *
     * @param ms the ms
     * @return the list
     */
    protected List<ResultMap> copyResultMap(MappedStatement ms) {
        //添加count结果集映射
        List<ResultMap> newResultMaps = new ArrayList<>();
        for (ResultMap resultMap : ms.getResultMaps()) {
            newResultMaps.add(resultMap);
        }

        return newResultMaps;
    }

    /**
     * Add count result map list.
     *
     * @param resultMaps the result maps
     * @return the list
     */
    protected List<ResultMap> addCountResultMap(List<ResultMap> resultMaps) {
        String id="_count_resultMap";
        ResultMap resultMap = new ResultMap.Builder(null, id, Long.class, new ArrayList()).build();
        resultMaps.add(resultMap);
        return resultMaps;
    }

    /**
     * 根据数据源返回数据库标记
     *
     * @param mappedStatement the mapped statement
     * @return string string
     */
    private static Map<DataSource,String> databaseIdCache=new ConcurrentHashMap<>();

    /**
     * Get database id string.
     *
     * @param mappedStatement the mapped statement
     * @return the string
     */
    protected String getDatabaseId(MappedStatement mappedStatement){
        String dataBaseId=mappedStatement.getDatabaseId();
        if (dataBaseId==null || dataBaseId.isEmpty()){
            dataBaseId=databaseIdCache.get(mappedStatement.getConfiguration().getEnvironment().getDataSource());
            if (dataBaseId==null) {
                dataBaseId = new VendorDatabaseIdProvider().getDatabaseId(mappedStatement.getConfiguration().getEnvironment().getDataSource());
                databaseIdCache.put(mappedStatement.getConfiguration().getEnvironment().getDataSource(),dataBaseId);
            }
        }
        return dataBaseId;
    }


    /**
     * 分页溢出处理
     *
     * @param pageInfo        the page info
     * @param mappedStatement the mapped statement
     * @param parameterObject the parameter object
     * @param pageBoundSql    the page bound sql
     * @return the boolean
     */
    protected boolean pageOverflowToLast(PageInfo pageInfo, MappedStatement mappedStatement, Object parameterObject,BoundSql pageBoundSql){
        if (PageMetaData.PAGE_OVERFLOW_TO_LAST){
            if (pageInfo.getPageNum()> pageInfo.getTotalPage()){
                pageInfo.setPageNum(pageInfo.getTotalPage());

                //判断是否存在分页参数,分页参数名称在PageInfo中设置
                //BoundSql pageBoundSql=mappedStatement.getBoundSql(parameterObject);
                List<ParameterMapping> parameterMappings=pageBoundSql.getParameterMappings();
                boolean containLimitParam=false;
                boolean containOffsetParam=false;
                for(ParameterMapping p:parameterMappings){
                    logger.debug("params:"+p.toString());
                    if(p.getProperty().equalsIgnoreCase(pageInfo.getLimitParamName())){
                        containLimitParam=true;
                    }else if(p.getProperty().equalsIgnoreCase(pageInfo.getOffsetParamName())){
                        containOffsetParam=true;
                    }
                }
                if (!containLimitParam || !containOffsetParam){
                    logger.debug("can't find limit or offset param name");
                    return false;

                }
                //修正原来的分页参数
                setParameters(mappedStatement,parameterObject,pageBoundSql);
                pageBoundSql.setAdditionalParameter(pageInfo.getLimitParamName(), pageInfo.getLimit());
                pageBoundSql.setAdditionalParameter(pageInfo.getOffsetParamName(),pageInfo.getOffset());

                logger.debug(pageBoundSql.getSql());
                logger.debug(pageBoundSql.getAdditionalParameter(pageInfo.getLimitParamName()).toString());
                logger.debug(pageBoundSql.getAdditionalParameter(pageInfo.getOffsetParamName()).toString());

                return true;
            }
        }
        return false;
    }

    /**
     * 设置参数,参照DefaultParameterHandler
     *
     * @param mappedStatement the mapped statement
     * @param parameterObject the parameter object
     * @param boundSql        the bound sql
     */
    public void setParameters(MappedStatement mappedStatement, Object parameterObject,BoundSql boundSql) {
        TypeHandlerRegistry typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        Configuration configuration=mappedStatement.getConfiguration();
        //BoundSql boundSql=mappedStatement.getBoundSql(parameterObject);
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        if (parameterMappings != null) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    //if (boundSql.hasAdditionalParameter(propertyName)) { // issue #448 ask first for additional params
                    //    value = boundSql.getAdditionalParameter(propertyName);
                    //} else
                    if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        MetaObject metaObject = configuration.newMetaObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                    }
                    boundSql.setAdditionalParameter(propertyName,value);
                    logger.debug(propertyName + ":" + value);
                }
            }
        }
    }

}
