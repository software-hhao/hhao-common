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
 * 根片段的token
 *
 * @author Wang
 * @since 2021 /11/19 11:37
 */
public interface FragmentToken extends Token{
    /**
     * 片段的起始位置
     *
     * @return begin token
     */
    Token getBeginToken();

    /**
     * Sets begin token.
     *
     * @param beginToken the begin token
     */
    void setBeginToken(Token beginToken);

    /**
     * 片段的终止位置
     *
     * @return end token
     */
    Token getEndToken();

    /**
     * Sets end token.
     *
     * @param endToken the end token
     */
    void setEndToken(Token endToken);

    /**
     * Of fragment token.
     *
     * @param token the token
     * @return the fragment token
     */
    static FragmentToken of(Token token){
        if (!(token instanceof FragmentToken)) {
            token = new SqlFragmentToken(token.getValue());
            token.setFragmentToken(token.getFragmentToken());
            token.setClean(token.isClean());
            token.setIndex(token.getIndex());
        }
        return (FragmentToken)token;
    }
}
