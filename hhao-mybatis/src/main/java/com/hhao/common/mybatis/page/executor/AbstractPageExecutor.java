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
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 分页执行器基类
 *
 * @author Wang
 * @since 1.0.0
 */
public abstract class AbstractPageExecutor implements PageExecutor {
    private final String DEFAULT_PAGE_INFO_PARAM_NAME = PageMetaData.PAGE_INFO_PARAM_NAME;
    private PageExecutorType[] executorTypes;

    /**
     * 执行器支持的分页类型
     *
     * @param executorTypes the executor types
     */
    public AbstractPageExecutor(PageExecutorType[] executorTypes) {
        this.executorTypes = executorTypes;
    }

    /**
     * 判断是否支持
     * 1、存在PageInfo参数
     * 2、支持pageInfo.getExecutorType()
     */
    @Override
    public boolean support(Invocation invocation) {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameter = args[1];
        //获取PageInfo参数，如果未获取，则不支持
        PageInfo pageInfo = getPageInfo(parameter);
        if (pageInfo == null) {
            return false;
        }
        //执行器类型比较
        if (pageInfo.getPageExecutorType() == null || !supportExecutorType(pageInfo.getPageExecutorType())) {
            return false;
        }
        return support(pageInfo, mappedStatement);
    }

    /**
     * 执行器类型是否适合
     *
     * @param executorType the executor type
     * @return boolean
     */
    protected boolean supportExecutorType(PageExecutorType executorType) {
        for (PageExecutorType et : executorTypes) {
            if (et.equals(executorType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 子类可以重写
     *
     * @param pageInfo        the page info
     * @param mappedStatement the mapped statement
     * @return boolean
     */
    protected boolean support(PageInfo pageInfo, MappedStatement mappedStatement) {
        return true;
    }

    /**
     * 获取PageInfo参数
     *
     * @param parameter the parameter
     * @return page info
     */
    protected PageInfo getPageInfo(Object parameter) {
        PageInfo pageInfo = null;
        //针对动态SQL的SelectStatementProvider
        if (parameter instanceof SelectStatementProvider) {
            Map<String, Object> parameters=((SelectStatementProvider) parameter).getParameters();
            //先按名称查PageInfo参数
            pageInfo = (PageInfo) ((SelectStatementProvider) parameter).getParameters().get(DEFAULT_PAGE_INFO_PARAM_NAME);
            //再按类型查PageInfo参数
            if (pageInfo==null){
                for(Map.Entry<String, Object> entry:parameters.entrySet()){
                    if (entry.getValue() instanceof PageInfo){
                        pageInfo=(PageInfo)entry.getValue();
                        break;
                    }
                }
            }
        } else if (parameter instanceof MapperMethod.ParamMap) {
            //针对Mapper传参
            //先按名称
            MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) parameter;
            try {
                pageInfo = (PageInfo) paramMap.get(DEFAULT_PAGE_INFO_PARAM_NAME);
            } catch (Exception e) {
            }
            //再按类型
            if (pageInfo==null){
                for(Object key:paramMap.keySet()){
                    Object value=paramMap.get(key);
                    if (value instanceof PageInfo){
                        pageInfo=(PageInfo) value;
                        break;
                    }
                }
            }
        }
        return pageInfo;
    }

    /**
     * 分页拦截处理
     */
    @Override
    public Object execute(Invocation invocation) throws Throwable {
        Object parameter = this.getParameter(invocation);
        return execute(this.getPageInfo(parameter), invocation);
    }

    /**
     * 子类重写该方法
     *
     * @param pageInfo   the page info
     * @param invocation the invocation
     * @return object
     * @throws Throwable the throwable
     */
    protected abstract Object execute(PageInfo pageInfo, Invocation invocation) throws Throwable;

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
     * @return parameter
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
     * 根据分页执行器类型判断是否需要count处理
     *
     * @param executorType the executor type
     * @return boolean
     */
    protected boolean isIncludeCount(PageExecutorType executorType){
        if (executorType==null || executorType.name().indexOf("INCLUDE_COUNT",0)==-1){
            return false;
        }
        return true;
    }

    /**
     * 处理结果集
     *
     * @param pageInfo the page info
     * @param result   the result
     * @return object
     */
    protected Object doResult(PageInfo pageInfo, Object result) {
        //包含count语句的结果集
        if (isIncludeCount(pageInfo.getPageExecutorType())){
            List<List<Object>> newResult = (List<List<Object>>) result;
            pageInfo.setResult((List<Object>)newResult.get(0));
            pageInfo.setTotalRow((Long) newResult.get(1).get(0));
            return newResult.get(0);
        }else{
            //不包含count语句的结果集
            List<Object> newResult = (List<Object>) result;
            pageInfo.setResult(newResult);
            pageInfo.setTotalRow(-1);
            return newResult;
        }
    }

    /**
     * 构建新的MappedStatement
     *
     * @param pageInfo  the page info
     * @param ms        the ms
     * @param sqlSource the sql source
     * @return mapped statement
     */
    protected MappedStatement newMappedStatement(PageInfo pageInfo, MappedStatement ms, SqlSource sqlSource){
        String keyProperty="";
        if (ms.getKeyProperties()!=null) {
            for (String s : ms.getKeyProperties()) {
                if (keyProperty.isBlank()) {
                    keyProperty = s;
                } else {
                    keyProperty = keyProperty + "," + s;
                }
            }
        }

        String keyColumn="";
        if (ms.getKeyColumns()!=null) {
            for (String s : ms.getKeyColumns()) {
                if (keyColumn.isBlank()) {
                    keyColumn = s;
                } else {
                    keyColumn = keyColumn + "," + s;
                }
            }
        }

        String resultSet="";
        if (ms.getResultSets()!=null) {
            for (String s : ms.getResultSets()) {
                if (resultSet.isBlank()) {
                    resultSet = s;
                } else {
                    resultSet = resultSet + "," + s;
                }
            }
        }

        //添加count结果集映射
        List<ResultMap> newResultMaps=new ArrayList<>();
        for(ResultMap resultMap:ms.getResultMaps()){
            newResultMaps.add(resultMap);
        }

        //如果有加count,加一个结果集
        if (isIncludeCount(pageInfo.getPageExecutorType())) {
            String id = "-count";
            if (ms.getResultMaps() != null) {
                id = ms.getResultMaps().get(0).getId() + "-count";
            }
            ResultMap resultMap = new ResultMap.Builder(null, id, Long.class, new ArrayList()).build();
            newResultMaps.add(resultMap);
        }

        //构建新的MappedStatement
        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), sqlSource, ms.getSqlCommandType())
                .resource(ms.getResource())
                .fetchSize(ms.getFetchSize())
                .timeout(ms.getTimeout())
                .statementType(ms.getStatementType())
                .keyGenerator(ms.getKeyGenerator())
                .keyProperty(keyProperty)
                .keyColumn(keyColumn)
                .databaseId(ms.getDatabaseId())
                .lang(ms.getLang())
                .resultOrdered(ms.isResultOrdered())
                .resultSets(resultSet)
                .resultMaps(newResultMaps)
                .resultSetType(ms.getResultSetType())
                .flushCacheRequired(ms.isFlushCacheRequired())
                .useCache(ms.isUseCache())
                .cache(ms.getCache())
                .parameterMap(ms.getParameterMap());

        return statementBuilder.build();
    }

    /**
     * 根据数据源返回数据库标记
     *
     * @param mappedStatement the mapped statement
     * @return string
     */
    protected String getDatabaseId(MappedStatement mappedStatement){
        String dataBaseId=mappedStatement.getDatabaseId();
        if (dataBaseId==null || dataBaseId.isEmpty()){
            dataBaseId=new VendorDatabaseIdProvider().getDatabaseId(mappedStatement.getConfiguration().getEnvironment().getDataSource());
        }
        return dataBaseId;
    }
}
