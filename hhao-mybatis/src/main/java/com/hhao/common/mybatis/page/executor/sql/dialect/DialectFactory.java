
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

package com.hhao.common.mybatis.page.executor.sql.dialect;

import com.hhao.common.mybatis.page.PageInfo;
import com.hhao.common.mybatis.page.exception.MyBatisException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The type Dialect factory.
 *
 * @author Wang
 * @since 2021 /11/27 10:03
 */
public class DialectFactory {
    private static List<Dialect> dialects = new CopyOnWriteArrayList<>();

    /**
     * 根据dbName返回查询到的Dialect
     *
     * @param pageInfo the page info
     * @param dbName   the db name
     * @return the dialect
     */
    public static Dialect getDialect(PageInfo pageInfo,String dbName){
        for(Dialect dialect:dialects){
            if (dialect.support(pageInfo,dbName)){
                return dialect;
            }
        }
        throw new MyBatisException("Could not find a matching Dialect.");
    }

    /**
     * Register.
     *
     * @param dialect the dialect
     */
    public static void register(Dialect dialect){
        dialects.add(dialect);
    }
}
