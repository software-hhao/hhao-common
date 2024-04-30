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

import com.hhao.common.mybatis.page.PageInfo;
import com.hhao.common.mybatis.page.executor.sql.dialect.Dialect;
import com.hhao.common.mybatis.page.executor.sql.dialect.DialectFactory;
import com.hhao.common.mybatis.page.executor.sql.parse.SqlParse;
import com.hhao.common.mybatis.page.executor.sql.parse.TokenInfo;
import com.hhao.common.mybatis.page.executor.sql.token.ParamToken;
import com.hhao.common.mybatis.page.executor.sql.token.Token;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * sql执行器类
 *
 * @author Wang
 * @since 1.0.0
 */
public class DefaultSqlExecutor implements SqlExecutor {
    private final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public SqlPageModel generalSqlPageModel(PageInfo pageInfo, String sql, List<Object> paramMappings,String dbName){
        Dialect dialect=findDialect(pageInfo,dbName);
        SqlParse parse=dialect.getSqlParse(pageInfo,dbName);

        //解析原始的sql语句
        List<TokenInfo> tokenInfos=parse.parseSql(sql);
        //定位select与count语句
        TokenInfo select=null;
        TokenInfo count=null;
        if(tokenInfos.size()==1){
            //说明只有单条select语句
            select=tokenInfos.get(0);
        }else if(tokenInfos.size()==2){
            //说明包含两条语句，一条是select,一条是count
            select=tokenInfos.get(0);
            count=tokenInfos.get(1);
        }else{
            throw new RuntimeException("wrong sql");
        }

        //对select语句进行处理
        SqlModel selectSqlModel=null;
        if (!dialect.hasPaged(pageInfo,select)){
            //说明未包含分页，需要处理
            if(count!=null){
                //如果是多语句,则解析出select的参数
                selectSqlModel=dialect.buildPageSql(pageInfo,select,resolveParams(select,paramMappings));
            }else{
                //如果是单语句,则参数直接使用
                selectSqlModel=dialect.buildPageSql(pageInfo,select,paramMappings);
            }
        }else{
            //说明已经包含了分页，直接解析成selectSqlModel
            selectSqlModel=new SqlModel();
            selectSqlModel.setSql(select.getSql());
            if(count!=null){
                //如果是多语句，则解析出select的参数
                selectSqlModel.setParams(resolveParams(select,paramMappings));
            }else{
                selectSqlModel.setParams(paramMappings);
            }
        }

        //对count语句进行处理
        SqlModel countSqlModel=null;
        if (pageInfo.isIncludeTotalRows()){
            if (count==null){
                //说明不存在count语句
                countSqlModel=dialect.buildCountSql(pageInfo,select,paramMappings);
            }else{
                //说明存在count语句
                countSqlModel=new SqlModel();
                countSqlModel.setSql(count.getSql());
                countSqlModel.setParams(resolveParams(count,paramMappings));
            }
        }

        return new SqlPageModel(selectSqlModel,countSqlModel);
    }

    private List<Object> resolveParams(TokenInfo tokenInfo,List<Object> paramMappings){
        List<Object> params=new ArrayList<>();
        List<Token> paramTokens=tokenInfo.getParamTokens();

        for(Token paramToken:paramTokens){
            int parmaIndex=((ParamToken)paramToken).getParamIndex();
            params.add(paramMappings.get(parmaIndex));
        }

        return params;
    }

    private Dialect findDialect(PageInfo pageInfo,String dbName){
        return DialectFactory.getDialect(pageInfo,dbName);
    }
}
