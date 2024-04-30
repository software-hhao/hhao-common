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

package com.hhao.common.mybatis.page.support;

import com.hhao.common.mybatis.page.PageInfo;
import com.hhao.common.mybatis.page.PageMetaData;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 代理SelectStatementProvider
 * 加入PageInfo参数
 *
 * @author Wang
 * @since 1.0.0
 */
public class PageSelectStatementProvider implements SelectStatementProvider {
    private SelectStatementProvider proxySelectStatementProvider;
    private Map<String,Object> newParams=null;
    private PageInfo pageInfo=null;


    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    /**
     * Instantiates a new Page select statement provider.
     *
     * @param pageInfo                the page info
     * @param selectStatementProvider the select statement provider
     */
    public PageSelectStatementProvider(PageInfo pageInfo,SelectStatementProvider selectStatementProvider){
        proxySelectStatementProvider=selectStatementProvider;
        this.pageInfo=pageInfo;
    }

    @Override
    public Map<String, Object> getParameters() {
        if (newParams==null){
            Map<String,Object> params=proxySelectStatementProvider.getParameters();
            newParams=new ConcurrentHashMap<>();
            params.entrySet().forEach(entry -> {
                newParams.put(entry.getKey(),entry.getValue());
            });
            newParams.put(PageMetaData.PAGE_INFO_PARAM_NAME,pageInfo);
        }
        return newParams;
    }

    @Override
    public String getSelectStatement() {
        return proxySelectStatementProvider.getSelectStatement();
    }
}
