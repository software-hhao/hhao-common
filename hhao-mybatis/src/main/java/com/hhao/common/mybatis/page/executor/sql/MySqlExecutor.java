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

package com.hhao.common.mybatis.page.executor.sql;

import com.hhao.common.mybatis.page.PageInfo;
import com.hhao.common.mybatis.page.executor.ParamMapping;
import com.hhao.common.mybatis.page.executor.sql.parse.TokenInfo;
import com.hhao.common.mybatis.page.executor.sql.token.ParamToken;
import com.hhao.common.mybatis.page.executor.sql.token.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * 针对MySQL数据库,需要处理SELECT、COUNT语句的SQL执行器
 *
 * @author Wang
 * @since 1.0.0
 */
public class MySqlExecutor extends AbstractSqlExecutor{
    public MySqlExecutor() {
        super(new String[]{"mysql"});
    }

    /**
     * 构建page分页语句
     * selectSql:原始select语句解析后的TokenInfo
     * paramMappings:原始select语句匹配的参数
     */
    @Override
    public SqlModel buildPageSql(PageInfo pageInfo, TokenInfo selectSql, List<Object> paramMappings){
        StringBuilder sql =null;
        if (selectSql.isUnion()){
            throw new RuntimeException("For performance, define your own");
        }else {
            sql = new StringBuilder(selectSql.getSql())
                    .append(" LIMIT ")
                    .append(pageInfo.getLimit())
                    .append(" OFFSET ")
                    .append(pageInfo.getOffset());
        }

        List<Object> params=new ArrayList<>();
        for(Object param:paramMappings){
            params.add(param);
        }
        params.add(ParamMapping.Builder.create(pageInfo.getLimitParamName(),Long.class,pageInfo.getLimit()));
        params.add(ParamMapping.Builder.create(pageInfo.getOffsetParamName(),Long.class,pageInfo.getOffset()));

        return new SqlModel(sql.toString(),params);
    }

    @Override
    public SqlModel buildCountSql(PageInfo pageInfo, TokenInfo selectSql, List<Object> paramMappings){
        String sql="";
        if(selectSql.isUnion()){
            throw new RuntimeException("For performance, define your own");
        }else{
            sql=String.format("SELECT COUNT(*) %s ", selectSql.getCleanSql());
        }

        //参数处理
        List<Object> params=new ArrayList<>();
        for(Object param:paramMappings){
            params.add(param);
        }

        //原select参数扣减被移除的参数
        List<Token> paramTokens=selectSql.getParamTokens();
        int index=0;
        for(Token token:paramTokens){
            if(token.isClean()){
                params.remove(((ParamToken)token).getParamIndex()-index);
                index++;
            }
        }
        return new SqlModel(sql,params);
    }
}
