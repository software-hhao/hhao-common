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

package com.hhao.common.mybatis.page.executor.sql.token;


/**
 * sql片段token
 * 主要用于以下sql片段:
 * from,where,group by,having,order by,limit,offset,union
 *
 * @author Wang
 * @since 2021 /11/19 12:58
 */
public class SqlFragmentToken extends SqlToken implements FragmentToken{
    private Token beginToken;
    private Token endToken;

    /**
     * Instantiates a new Sql fragment token.
     *
     * @param value the value
     */
    public SqlFragmentToken(String value) {
        super(value);
    }

    @Override
    public Token getBeginToken() {
        return beginToken;
    }

    @Override
    public void setBeginToken(Token beginToken) {
        this.beginToken = beginToken;
    }

    @Override
    public Token getEndToken() {
        return endToken;
    }

    @Override
    public void setEndToken(Token endToken) {
        this.endToken = endToken;
    }

    /**
     * Of fragment token.
     *
     * @param token the token
     * @return the fragment token
     */
    public FragmentToken of(Token token){
        FragmentToken fragmentToken=new SqlFragmentToken(token.getValue());
        fragmentToken.setFragmentToken(token.getFragmentToken());
        fragmentToken.setClean(token.isClean());
        fragmentToken.setIndex(token.getIndex());
        return fragmentToken;
    }

    @Override
    public String toString(){
        return super.toString() + "|begin:" + (beginToken!=null?beginToken.getIndex():"-") + "|end:" + (endToken!=null?endToken.getIndex():"-");
    }
}
