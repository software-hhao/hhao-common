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
 * The interface Token info.
 *
 * @author Wang
 * @since 2021 /11/20 21:48
 */
public interface TokenInfo {
    /**
     * 返回解析后的sql token
     *
     * @return the token list
     */
    List<Token> getTokens();

    /**
     * 参数占位符token
     *
     * @return the param tokens
     */
    List<Token> getParamTokens();

    /**
     * 返回解析后的sql语句
     *
     * @return the sql
     */
    String getSql();

    /**
     * 返回按cleanRule规则清除不必要token后的sql
     *
     * @return the clean sql
     */
    String getCleanSql();

    /**
     * 是否是unio语句
     *
     * @return the boolean
     */
    boolean isUnion();

    /**
     * 是否包含limit
     *
     * @return boolean
     */
    boolean isContainLimit();
}
