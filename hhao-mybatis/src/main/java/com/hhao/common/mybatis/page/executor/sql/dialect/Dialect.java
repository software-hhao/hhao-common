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

package com.hhao.common.mybatis.page.executor.sql.dialect;

import com.hhao.common.mybatis.page.PageInfo;
import com.hhao.common.mybatis.page.executor.sql.SqlModel;
import com.hhao.common.mybatis.page.executor.sql.parse.SqlParse;
import com.hhao.common.mybatis.page.executor.sql.parse.TokenInfo;

import java.util.List;

/**
 * The interface Dialect.
 *
 * @author Wang
 * @since 2021 /11/27 9:47
 */
public interface Dialect {

    /**
     * 判断是否支持某种数据库
     *
     * @param pageInfo   the page info
     * @param dbName the database id
     * @return boolean
     */
    boolean support(PageInfo pageInfo,String dbName);

    SqlParse getSqlParse(PageInfo pageInfo,String dbName);

    /**
     * 判断select是否已分页
     *
     * @param pageInfo  the page info
     * @param tokenInfo the token info
     * @return boolean
     */
    boolean hasPaged(PageInfo pageInfo,TokenInfo tokenInfo);

    /**
     * 构建select分页语句及参数列表
     *
     * @param pageInfo      the page info
     * @param selectSql     原始select语句
     * @param paramMappings 原始select语句的参数
     * @return sql model
     */
    SqlModel buildPageSql(PageInfo pageInfo, TokenInfo selectSql, List<Object> paramMappings);


    /**
     * 构建count语句及参数列表
     *
     * @param pageInfo      the page info
     * @param selectSql     原始select语句
     * @param paramMappings 原始select语句的参数
     * @return sql model
     */
    SqlModel buildCountSql(PageInfo pageInfo, TokenInfo selectSql, List<Object> paramMappings);
}
