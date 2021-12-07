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
import com.hhao.common.mybatis.page.executor.sql.token.Token;

import java.util.List;

/**
 * sql解析结果
 *
 * @author Wang
 * @since 2021 /11/20 14:10
 */
public interface TokenParse {
    /**
     * token解析
     *
     * @param token the token
     */
    void parseToken(Token token);


    /**
     * 返回解析后的结果
     * @{code List<TokenInfo>}每行一条sql语句的解析结果
     *
     * @return
     */
    List<TokenInfo> getTokenInfos();
}
