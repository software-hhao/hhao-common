
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
import com.hhao.common.mybatis.page.executor.sql.parse.TokenInfo;

import java.util.List;


/**
 * The type My sql dialect.
 */
public class MySqlDialect extends AbstractDialect{

    /**
     * Instantiates a new My sql dialect.
     */
    public MySqlDialect() {
        super(new String[]{"mysql"});
    }

    @Override
    public boolean hasPaged(PageInfo pageInfo, TokenInfo tokenInfo) {
        return tokenInfo.isContainLimit();
    }

    @Override
    public SqlModel buildPageSql(PageInfo pageInfo, TokenInfo selectSql, List<Object> paramMappings){
        StringBuilder sql =null;
        //如果是union复杂语句，建议自己定义分页语句
        if (selectSql.isUnion()){
            throw new RuntimeException("For performance, you need to define select page yourself");
        }else {
            sql = new StringBuilder(selectSql.getSql())
                    .append(" LIMIT ")
                    .append("?")
                    .append(" OFFSET ")
                    .append("?");
        }

        //添加分页参数
        List<Object> params=this.doSelectParams(pageInfo,selectSql,paramMappings);

        return new SqlModel(sql.toString(),params);
    }

    @Override
    public SqlModel buildCountSql(PageInfo pageInfo, TokenInfo selectSql, List<Object> paramMappings){
        //count语句处理
        String sql="";
        //如果是union复杂语句，建议自己定义count语句
        if(selectSql.isUnion()){
            throw new RuntimeException("For performance, define your own");
        }else{
            sql=String.format("SELECT COUNT(*) %s ", selectSql.getCleanSql());
        }

        //count语句参数处理
        List<Object> params=this.doCountParams(selectSql,paramMappings);
        return new SqlModel(sql,params);
    }
}
