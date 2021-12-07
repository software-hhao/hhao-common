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

package com.hhao.common.mybatis.page.executor.sql;

import com.hhao.common.mybatis.page.PageInfo;

import java.util.List;

/**
 * SQL执行器
 *
 * @author Wang
 * @since 1.0.0
 */
public interface SqlExecutor {
    /**
     * General sql page model sql page model.
     *
     * @param pageInfo      the page info
     * @param sql           原始的sql语句
     * @param paramMappings 原始的sql语句带的参数
     * @param dbName        the db name
     * @return the sql page model
     */
    SqlPageModel generalSqlPageModel(PageInfo pageInfo,String sql,List<Object> paramMappings,String dbName);
}
