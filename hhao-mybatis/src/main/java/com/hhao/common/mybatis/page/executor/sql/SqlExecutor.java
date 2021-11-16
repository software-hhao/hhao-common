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
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;

import java.util.List;

/**
 * SQL执行器
 *
 * @author Wang
 * @since  1.0.0
 */
public interface SqlExecutor {
    /**
     * 判断是否支持处理某个拦截语句
     *
     * @param pageInfo     the page info
     * @param databaseId   the database id
     * @param executorType the executor type
     * @return boolean
     */
    boolean support(PageInfo pageInfo,String databaseId, PageExecutorType executorType);

    /**
     * SQL语句分隔符
     *
     * @return string
     */
    default String getSqlSeparator(){
        return ";";
    }

    /**
     * 对select语句的处理
     *
     * @param originalSql the original sql
     * @return string
     */
    String selectSql(String originalSql);

    /**
     * select参数处理
     *
     * @param pageInfo            the page info
     * @param selectSql           the select sql
     * @param selectParamMappings the select param mappings
     * @return 可能存在ParameterMapping(MyBatis解析的传入参数)和ParamMapping(自定义新添加参数)两种类型 list
     */
    List<Object> selectParamMappings(PageInfo pageInfo, String selectSql, List<Object> selectParamMappings);

    /**
     * 对count语句处理
     *
     * @param selectSql the select sql
     * @return string
     */
    String countSql(String selectSql);

    /**
     * count语句参数处理
     *
     * @param pageInfo            the page info
     * @param countSql            the count sql
     * @param selectParamMappings the select param mappings
     * @return 可能存在ParameterMapping(MyBatis解析的传入参数)和ParamMapping(自定义新添加参数)两种类型 list
     */
    List<Object> countParamMappings(PageInfo pageInfo,String countSql, List<Object> selectParamMappings);


}
