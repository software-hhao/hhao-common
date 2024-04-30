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

package com.hhao.common.mybatis.page.executor.sql.parse;

import com.hhao.common.mybatis.page.executor.sql.token.Token;

import java.util.List;

/**
 * The type Default token info.
 *
 * @author Wang
 * @since 2021 /11/20 22:08
 */
public class DefaultTokenInfo implements TokenInfo{
    /**
     * 完整的SQL解析后的tokens
     */
    protected List<Token> tokens;
    /**
     * 完整的SQL语句中包含?参数的tokens
     */
    protected List<Token> paramTokens;
    /**
     * SQL语句是否是一条union语句
     */
    protected boolean union;

    /**
     * SQL语句是否包含limit(只适用于包含limit语句的数据库)
     */
    protected boolean containLimit;

    /**
     * Instantiates a new Default token info.
     *
     * @param tokens       the tokens
     * @param paramTokens  the param tokens
     * @param union        the union
     * @param containLimit the contain limit
     */
    public DefaultTokenInfo(List<Token> tokens, List<Token> paramTokens, boolean union,  boolean containLimit){
        this.tokens = tokens;
        this.paramTokens = paramTokens;
        this.union=union;
        this.containLimit=containLimit;
    }

    @Override
    public String getCleanSql() {
        StringBuffer buf = new StringBuffer();
        for (Token t : tokens) {
            if (!t.isClean()) {
                if(!(SqlKeyword.LEFT_PARENTHESIS.getValue().equalsIgnoreCase(t.getValue()) || SqlKeyword.RIGHT_PARENTHESIS.getValue().equalsIgnoreCase(t.getValue()))){
                    buf.append(" ");
                }
                buf.append(t.getValue());
            }
        }
        return buf.toString();
    }

    /**
     * Get sql string.
     *
     * @return the string
     */
    @Override
    public String getSql() {
        StringBuffer buf = new StringBuffer();
        for (Token t : tokens) {
            if(!(SqlKeyword.LEFT_PARENTHESIS.getValue().equalsIgnoreCase(t.getValue()) || SqlKeyword.RIGHT_PARENTHESIS.getValue().equalsIgnoreCase(t.getValue()))){
                buf.append(" ");
            }
            buf.append(t.getValue());
        }
        return buf.toString();
    }

    /**
     * Gets token list.
     *
     * @return the token list
     */
    @Override
    public List<Token> getTokens() {
        return tokens;
    }

    @Override
    public List<Token> getParamTokens(){
        return paramTokens;
    }

    @Override
    public boolean isUnion() {
        return union;
    }

    @Override
    public boolean isContainLimit() {
        return containLimit;
    }

    @Override
    public String toString(){
        StringBuffer buffer=new StringBuffer();
        buffer.append("sql:");
        buffer.append(this.getSql());
        buffer.append("\r\n");
        buffer.append("cleanSql:");
        buffer.append(this.getCleanSql());
        buffer.append("\r\n");
        buffer.append("isUnion:" + isUnion());
        buffer.append("containLimit:" +containLimit);
        buffer.append("paramTokens:" + paramTokens);
        return buffer.toString();
    }

}
