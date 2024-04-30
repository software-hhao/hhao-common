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
 * 参数占位符?token
 *
 * @author Wang
 * @since 2021 /11/19 11:37
 */
public interface ParamToken extends Token{
    /**
     * Gets param index.
     *
     * @return the param index
     */
    int getParamIndex();

    /**
     * Sets param index.
     *
     * @param paramIndex the param index
     */
    void setParamIndex(int paramIndex);

    /**
     * Of param token.
     *
     * @param token the token
     * @return the param token
     */
    static ParamToken of(Token token){
        if (!(token instanceof ParamToken)) {
            token = new SqlParamToken(token.getValue());
            token.setFragmentToken(token.getFragmentToken());
            token.setClean(token.isClean());
            token.setIndex(token.getIndex());
        }
        return (ParamToken)token;
    }
}
