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

import java.util.List;

/**
 * The interface Parse.
 *
 * @author Wang
 * @since 2021 /11/20 14:08
 */
public interface SqlParse {
    /**
     * 将sql解析成token
     *
     * @param sql       the sql
     * @return the parse result
     */
    List<TokenInfo> parseSql(String sql);

    interface Builder{
        SqlParse build();
    }

    Builder builder= () -> new DefaultSqlParse();
}
