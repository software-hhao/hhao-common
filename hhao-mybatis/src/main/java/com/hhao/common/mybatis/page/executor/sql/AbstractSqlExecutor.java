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

import com.hhao.common.mybatis.page.Page;
import com.hhao.common.mybatis.page.PageInfo;
import com.hhao.common.mybatis.page.PageMetaData;
import com.hhao.common.mybatis.page.executor.PageExecutor;
import com.hhao.common.mybatis.page.executor.PageExecutorType;
import com.hhao.common.mybatis.page.executor.ParamMapping;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * sql执行器基类
 * 提供了support基本判断和其它一些方法
 *
 * @author Wang
 * @since 1.0.0
 */
public abstract class AbstractSqlExecutor implements SqlExecutor {
    private final String DEFAULT_PARAM_SYMBOL="?";
    private String [] supportDbs;
    private PageExecutorType[] supportExecutorTypes;

    /**
     * 构造
     *
     * @param supportDbs           :支持的数据库
     * @param supportExecutorTypes :支持的执行器类型
     */
    public AbstractSqlExecutor(String [] supportDbs,PageExecutorType[] supportExecutorTypes){
        this.supportDbs=supportDbs;
        this.supportExecutorTypes=supportExecutorTypes;
    }

    /**
     * 判断是否支持数据库
     * @param databaseId:数据库标记
     * @return
     */
    private boolean dbSupport(String databaseId){
        databaseId=databaseId.toLowerCase();
        for(String db:supportDbs){
            if (databaseId.indexOf(db.toLowerCase())!=-1){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否支持执行器类型
     * @param executorType
     * @return
     */
    private boolean executorTypeSupport(PageExecutorType executorType){
        for(PageExecutorType eType:supportExecutorTypes){
            if (eType.equals(executorType)){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否支持,默认判断顺序:
     * 1、执行器类弄的支持
     * 2、数据库类型的支持
     */
    @Override
    public boolean support(PageInfo pageInfo,String databaseId,PageExecutorType executorType) {
        if (executorTypeSupport(executorType)){
            if (databaseId==null || databaseId.isEmpty()){
                return true;
            }
            if (dbSupport(databaseId)){
                return true;
            }
        }
        return false;
    }

    /**
     * select语句的调整
     */
    @Override
    public String selectSql(String originalSql) {
        return originalSql;
    }

    /**
     * select语句参数的调整
     * 默认情况下，如果select语句添加了分页的2个参数,如limit和offset,则自动会补上这两个参数,注意，顺序是先limit,再offset
     * 另外注意:参数是有顺序的,这个顺序通过List先后来定
     * 新添加的参数用ParamMapping.Builder构建器构建
     */
    @Override
    public List<Object> selectParamMappings(PageInfo pageInfo, String selectSql, List<Object> selectParamMappings){
        List<Object> paramMappings=new ArrayList<>();
        for(Object pm:selectParamMappings){
            paramMappings.add(pm);
        }
        int paramCount=this.getParamCount(selectSql);
        if (paramCount-paramMappings.size()==2){
            paramMappings.add(ParamMapping.Builder.create(PageMetaData.LIMIT_PARAM_NAME,Long.class,pageInfo.getLimit()));
            paramMappings.add(ParamMapping.Builder.create(PageMetaData.OFFSET_PARAM_NAME,Long.class,pageInfo.getOffset()));
        }
        return paramMappings;
    }

    /**
     * 返回count语句
     */
    @Override
    public String countSql(String selectSql) {
        return "";
    }

    /**
     * count语句的参数
     * 默认情况,count语句的参数和select一样
     * 可能存在ParameterMapping(MyBatis解析的传入参数)和ParamMapping(自定义新添加参数)两种类型
     */
    @Override
    public List<Object> countParamMappings(PageInfo pageInfo, String countSql, List<Object> selectParamMappings) {
        int paramCount=this.getParamCount(countSql);
        List<Object> paramMappings=new ArrayList<>();

        for(int i=0;i<paramCount;i++){
            paramMappings.add(selectParamMappings.get(i));
        }
        return paramMappings;
    }

    /**
     * 判断SQL语句的参数个数
     *
     * @param sql the sql
     * @return int
     */
    protected int getParamCount(String sql){
        return getParamCount(DEFAULT_PARAM_SYMBOL,sql);
    }

    /**
     * 判断SQL语句的参数个数
     *
     * @param paramSymbol the param symbol
     * @param sql         the sql
     * @return int
     */
    protected int getParamCount(String paramSymbol,String sql){
        int index=0;
        int count=0;
        while(index>=0 && index<sql.length()) {
            index=sql.indexOf(paramSymbol,index);
            if (index!=-1){
                count++;
                index++;
            }
        }
        return count;
    }
}
