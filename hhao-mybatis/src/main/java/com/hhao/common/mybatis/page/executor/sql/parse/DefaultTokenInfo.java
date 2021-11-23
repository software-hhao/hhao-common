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

package com.hhao.common.mybatis.page.executor.sql.parse;

import com.hhao.common.mybatis.page.executor.sql.token.SelectToken;
import com.hhao.common.mybatis.page.executor.sql.token.Token;

import java.util.List;

/**
 * @author Wang
 * @since 2021/11/20 22:08
 */
public class DefaultTokenInfo implements TokenInfo{
    protected List<Token> tokens;
    protected List<Token> paramTokens;
    protected boolean union;
    protected boolean containCount;
    protected boolean containLimit;

    public DefaultTokenInfo(List<Token> tokens, List<Token> paramTokens, boolean union, boolean containCount, boolean containLimit){
        this.tokens = tokens;
        this.paramTokens = paramTokens;
        this.union=union;
        this.containCount=containCount;
        this.containLimit=containLimit;
    }

    @Override
    public String getCleanSql() {
        StringBuffer buf = new StringBuffer();
        for (Token t : tokens) {
            if (!t.isClean()) {
                if(!(t.getValue().equalsIgnoreCase("(") || t.getValue().equalsIgnoreCase(")"))){
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
            if(!(t.getValue().equalsIgnoreCase("(") || t.getValue().equalsIgnoreCase(")"))){
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
    public boolean isContainCount() {
        return containCount;
    }

    @Override
    public boolean isContainLimit() {
        return containLimit;
    }

    public void setContainLimit(boolean containLimit) {
        this.containLimit = containLimit;
    }
}
