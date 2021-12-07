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

package com.hhao.common.mybatis.page.interceptor;

import com.hhao.common.mybatis.page.PageInfo;
import com.hhao.common.mybatis.page.PageInfoWithCount;
import com.hhao.common.mybatis.page.PageMetaData;
import com.hhao.common.mybatis.page.executor.MultiQueriesDynamicPageExecutor;
import com.hhao.common.mybatis.page.executor.SingleQueryDynamicPageExecutor;
import com.hhao.common.mybatis.page.executor.SingleQueryStaticPageExecutor;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

import java.util.Map;
import java.util.Properties;

/**
 * MyBatis拦截器
 *
 * @author Wang
 * @since 1.0.0
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class PageInterceptor implements Interceptor {
    private static final Log log = LogFactory.getLog(PageInterceptor.class);
    private final String DEFAULT_PAGE_INFO_PARAM_NAME = PageMetaData.PAGE_INFO_PARAM_NAME;
    private Properties properties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        PageInfo pageInfo=getPageInfo(invocation);
        if (pageInfo!=null){
            //pageExecutor使用的优先级
            //1、pageInfo中自定义
            //2、如果是PageInfoWithCount，优先使用指定的CountMappedStatementId
            //3、如果开启多查询支持，优先使用MultiQueriesDynamicPageExecutor
            //4、使用SingleQueryDynamicPageExecutor
            if (pageInfo.getPageExecutor()==null) {
                if (pageInfo instanceof PageInfoWithCount) {
                    if (((PageInfoWithCount) pageInfo).getCountMappedStatementId() != null) {
                        pageInfo.setPageExecutor(new SingleQueryStaticPageExecutor());
                    }
                }
            }
            if (pageInfo.getPageExecutor()==null) {
                if (PageMetaData.SUPPORT_MULTI_QUERIES){
                    pageInfo.setPageExecutor(new MultiQueriesDynamicPageExecutor());
                }else{
                    pageInfo.setPageExecutor(new SingleQueryDynamicPageExecutor());
                }
            }
            log.debug("Use PageExecutor:" + pageInfo.getPageExecutor());
            return pageInfo.getPageExecutor().execute(invocation,pageInfo);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties=properties;
    }


    /**
     * Gets page info.
     *
     * @param invocation the invocation
     * @return the page info
     */
    protected PageInfo getPageInfo(Invocation invocation) {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameter = args[1];
        //获取PageInfo参数，如果未获取，则不支持
        return getPageInfo(parameter);
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
}
