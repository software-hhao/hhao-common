
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

package com.hhao.common.mybatis.page.executor.sql.dialect;

import com.hhao.common.mybatis.page.PageInfo;
import com.hhao.common.mybatis.page.executor.ParamMapping;
import com.hhao.common.mybatis.page.executor.sql.parse.SqlParse;
import com.hhao.common.mybatis.page.executor.sql.parse.SqlParseFactory;
import com.hhao.common.mybatis.page.executor.sql.parse.TokenInfo;
import com.hhao.common.mybatis.page.executor.sql.token.ParamToken;
import com.hhao.common.mybatis.page.executor.sql.token.Token;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Abstract dialect.
 */
public abstract class AbstractDialect implements Dialect{
    /**
     * The Logger.
     */
    protected final Log logger = LogFactory.getLog(this.getClass());
    /**
     * The Support dbs.
     */
    protected String [] supportDbs;

    /**
     * Instantiates a new Abstract dialect.
     *
     * @param supportDbs the support dbs
     */
    public AbstractDialect(String [] supportDbs){
        this.supportDbs=supportDbs;
    }

    @Override
    public SqlParse getSqlParse(PageInfo pageInfo, String dbName){
        return SqlParseFactory.getSqlParse(dbName);
    }

    @Override
    public boolean support(PageInfo pageInfo, String databaseId) {
        if (databaseId == null || databaseId.isEmpty()) {
            return true;
        }
        if (dbSupport(databaseId)) {
            return true;
        }
        return false;
    }

    /**
     * ???????????????????????????
     * @param databaseId:???????????????
     * @return
     */
    private boolean dbSupport(String databaseId){
        databaseId=databaseId.toLowerCase();
        for(String db:supportDbs){
            if (databaseId.indexOf(db.toLowerCase())!=-1){
                return true;
            }
        }
        return false;
    }

    /**
     * ?????????count????????????
     * ????????????????????????select???????????????????????????
     *
     * @param selectSql     ??????sql??????
     * @param paramMappings ??????sql???????????????
     * @return the list
     */
    protected List<Object> doCountParams(TokenInfo selectSql, List<Object> paramMappings){
        //????????????
        List<Object> params=new ArrayList<>();
        for(Object param:paramMappings){
            params.add(param);
        }
        //???select??????????????????????????????
        List<Token> paramTokens=selectSql.getParamTokens();
        int index=0;
        for(Token token:paramTokens){
            if(token.isClean()){
                params.remove(((ParamToken)token).getParamIndex()-index);
                index++;
            }
        }
        return params;
    }

    /**
     * select??????????????????
     * ?????????????????????????????????limit???offset??????
     *
     * @param pageInfo      the page info
     * @param selectSql     the select sql
     * @param paramMappings the param mappings
     * @return the list
     */
    protected List<Object> doSelectParams(PageInfo pageInfo,TokenInfo selectSql, List<Object> paramMappings){
        List<Object> params=new ArrayList<>();
        for(Object param:paramMappings){
            params.add(param);
        }
        //??????????????????
        params.add(ParamMapping.Builder.create(pageInfo.getLimitParamName(),Long.class,pageInfo.getLimit()));
        params.add(ParamMapping.Builder.create(pageInfo.getOffsetParamName(),Long.class,pageInfo.getOffset()));

        return params;
    }
}
