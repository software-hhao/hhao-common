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
 * select token
 *
 * @author Wang
 * @since 2021 /11/19 12:59
 */
public class SqlSelectToken extends SqlFragmentToken implements SelectToken{
    private Token fromToken;
    private Token whereToken;
    private Token groupByToken;
    private Token havingToken;
    private Token orderByToken;
    private Token limitToken;
    private Token offsetToken;


    /**
     * Instantiates a new Sql select token.
     *
     * @param value the value
     */
    public SqlSelectToken(String value) {
        super(value);
    }

    @Override
    public Token getFromToken() {
        return fromToken;
    }

    @Override
    public void setFromToken(Token fromToken) {
        this.fromToken = fromToken;
    }

    @Override
    public Token getWhereToken() {
        return whereToken;
    }

    @Override
    public void setWhereToken(Token whereToken) {
        this.whereToken = whereToken;
    }

    @Override
    public Token getGroupByToken() {
        return groupByToken;
    }

    @Override
    public void setGroupByToken(Token groupByToken) {
        this.groupByToken = groupByToken;
    }

    @Override
    public Token getOrderByToken() {
        return orderByToken;
    }

    @Override
    public void setOrderByToken(Token orderByToken) {
        this.orderByToken = orderByToken;
    }

    @Override
    public Token getLimitToken() {
        return limitToken;
    }

    @Override
    public void setLimitToken(Token limitToken) {
        this.limitToken = limitToken;
    }

    @Override
    public Token getOffsetToken() {
        return offsetToken;
    }

    @Override
    public void setOffsetToken(Token offsetToken) {
        this.offsetToken = offsetToken;
    }

    @Override
    public Token getHavingToken() {
        return havingToken;
    }

    @Override
    public void setHavingToken(Token havingToken) {
        this.havingToken = havingToken;
    }

    public SelectToken of(Token token){
        SelectToken selectToken=new SqlSelectToken(token.getValue());
        selectToken.setFragmentToken(token.getFragmentToken());
        selectToken.setClean(token.isClean());
        selectToken.setIndex(token.getIndex());
        return selectToken;
    }

    @Override
    public String toString(){
        return super.toString() + "|from:" + (fromToken!=null?fromToken.getIndex():"-") +
                "|where:" + (whereToken!=null?whereToken.getIndex():"-") +
                "|group:" + (groupByToken!=null?groupByToken.getIndex():"-") +
                "|having:" + (havingToken!=null?havingToken.getIndex():"-") +
                "|order:" + (orderByToken!=null?orderByToken.getIndex():"-") +
                "|limit:" + (limitToken!=null?limitToken.getIndex():"-") +
                "|offset:" + (offsetToken!=null?offsetToken.getIndex():"-");
    }
}
