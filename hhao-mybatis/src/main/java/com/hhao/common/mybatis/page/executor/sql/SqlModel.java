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

package com.hhao.common.mybatis.page.executor.sql;

import java.util.List;

/**
 * The type Sql model.
 *
 * @author Wang
 * @since 2021 /11/20 21:05
 */
public class SqlModel {
    /**
     * sql语句
     */
    private String sql;
    /**
     * sql语句的参数
     */
    private List<Object> params;

    /**
     * Instantiates a new Sql model.
     */
    public SqlModel(){

    }

    /**
     * Instantiates a new Sql model.
     *
     * @param sql    the sql
     * @param params the params
     */
    public SqlModel(String sql,List<Object> params){
        this.sql=sql;
        this.params=params;
    }

    /**
     * Gets sql.
     *
     * @return the sql
     */
    public String getSql() {
        return sql;
    }

    /**
     * Sets sql.
     *
     * @param sql the sql
     */
    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * Gets params.
     *
     * @return the params
     */
    public List<Object> getParams() {
        return params;
    }

    /**
     * Sets params.
     *
     * @param params the params
     */
    public void setParams(List<Object> params) {
        this.params = params;
    }
}
