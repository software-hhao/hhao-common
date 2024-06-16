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

package com.hhao.common.mybatis.page.executor.sql.dialect;

import com.hhao.common.mybatis.page.PageInfo;

/**
 * 最后适用所有的数据库,以MySQL为模板
 *
 * @author Wang Sheng
 * @since 2024 /06/08 22:08
 */
public class DefaultDialect extends MySqlDialect {
    /**
     * 匹配所有数据库
     *
     * @param pageInfo   the page info
     * @param databaseId the database id
     * @return
     */
    @Override
    public boolean support(PageInfo pageInfo, String databaseId) {
        return true;
    }
}
