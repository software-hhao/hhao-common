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
import com.hhao.common.mybatis.page.executor.PageExecutor;
import com.hhao.common.mybatis.page.executor.PageExecutorType;
import com.hhao.common.mybatis.page.executor.ParamMapping;
import org.apache.ibatis.session.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 针对MySQL数据库,需要处理SELECT、COUNT语句的SQL执行器
 *
 * @author Wang
 * @since 1.0.0
 */
public class SqlExecutorWithMysql extends AbstractSqlExecutor{
    /**
     * Instantiates a new Sql executor with mysql.
     */
    public SqlExecutorWithMysql() {
        super(
                new String[]{"mysql"},
                new PageExecutorType[]{PageExecutorType.DYNAMIC_EXCLUDE_COUNT, PageExecutorType.DYNAMIC_INCLUDE_COUNT}
        );
    }

    /**
     * 对select语句的处理
     * 判断select语句是否带有limit分页,如果没有,则加入分页
     */
    @Override
    public String selectSql(String originalSql) {
        if (originalSql.indexOf(" limit ")!=-1){
            return originalSql;
        }
        return originalSql + " limit ? offset ?";
    }

    /**
     * 对count语句的处理
     * 直接去调最后遇到的order by、limit
     * 将select列表改成select count(*)
     * 注意:可能不一定适用所有情况,如果不适用,则可以自定义Sql执行器,直接赋值给PageInfo
     */
    @Override
    public String countSql(String selectSql) {
        String lowerCaseSQL = selectSql.toLowerCase().replace("\n", " ").replace("\t", " ");
        int index = lowerCaseSQL.lastIndexOf(" order ");

        if (index != -1) {
            lowerCaseSQL = lowerCaseSQL.substring(0, index);
        }
        index = lowerCaseSQL.lastIndexOf(" limit ");
        if (index != -1) {
            lowerCaseSQL = lowerCaseSQL.substring(0, index);
        }
        return "select count(*) as c " + lowerCaseSQL.substring(lowerCaseSQL.indexOf(" from "));
    }

}
