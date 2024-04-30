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

package com.hhao.common.mybatis.page.executor.sql.token;


/**
 * select token
 * 记录了select各个子句的位置信息
 *
 * @author Wang
 * @since 2021 /11/19 11:44
 */
public interface SelectToken extends FragmentToken{
    /**
     * Gets from token.
     *
     * @return the from token
     */
    Token getFromToken();

    /**
     * Sets from token.
     *
     * @param fromToken the from token
     */
    void setFromToken(Token fromToken);

    /**
     * Gets where token.
     *
     * @return the where token
     */
    Token getWhereToken();

    /**
     * Sets where token.
     *
     * @param whereToken the where token
     */
    void setWhereToken(Token whereToken);

    /**
     * Gets group by token.
     *
     * @return the group by token
     */
    Token getGroupByToken();

    /**
     * Sets group by token.
     *
     * @param groupByToken the group by token
     */
    void setGroupByToken(Token groupByToken);

    /**
     * Gets having token.
     *
     * @return the having token
     */
    Token getHavingToken();

    /**
     * Sets having token.
     *
     * @param havingToken the having token
     */
    void setHavingToken(Token havingToken);

    /**
     * Gets order by token.
     *
     * @return the order by token
     */
    Token getOrderByToken();

    /**
     * Sets order by token.
     *
     * @param orderByToken the order by token
     */
    void setOrderByToken(Token orderByToken);

    /**
     * Gets limit token.
     *
     * @return the limit token
     */
    Token getLimitToken();

    /**
     * Sets limit token.
     *
     * @param limitToken the limit token
     */
    void setLimitToken(Token limitToken);

    /**
     * Gets offset token.
     *
     * @return the offset token
     */
    Token getOffsetToken();

    /**
     * Sets offset token.
     *
     * @param offsetToken the offset token
     */
    void setOffsetToken(Token offsetToken);

    /**
     * Of select token.
     *
     * @param token the token
     * @return the select token
     */
    static SelectToken of(Token token){
        if (!(token instanceof SelectToken)) {
            token = new SqlSelectToken(token.getValue());
            token.setFragmentToken(token.getFragmentToken());
            token.setClean(token.isClean());
            token.setIndex(token.getIndex());
        }
        return (SelectToken)token;
    }
}
