/*
 * Copyright (c) 2024 wangsheng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hhao.common.mybatis.generator.codegen.javamapper;

import com.hhao.common.mybatis.generator.codegen.HaoContext;
import com.hhao.common.mybatis.generator.codegen.utils.GeneratorStringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.hhao.common.mybatis.generator.codegen.utils.GeneratorStringUtils.capitalizeFirstLetter;

/**
 * The type Find default method generator.
 *
 * @author wang sheng
 * @since 2024 /6/10 下午3:28
 */
public class FindDefaultMethodGenerator  extends AbstractHaoJavaMapperMethodGenerator{
    private FullyQualifiedJavaType pageResponseType=new FullyQualifiedJavaType("com.hhao.common.ddd.dto.response.PageResponse");
    private FullyQualifiedJavaType whereClauseProviderType=new FullyQualifiedJavaType("org.mybatis.dynamic.sql.where.render.WhereClauseProvider");
    private FullyQualifiedJavaType renderingStrategiesType=new FullyQualifiedJavaType("org.mybatis.dynamic.sql.render.RenderingStrategies");
    private FullyQualifiedJavaType nonRenderingWhereClauseExceptionType=new FullyQualifiedJavaType("org.mybatis.dynamic.sql.exception.NonRenderingWhereClauseException");
    private FullyQualifiedJavaType sqlBuilderType=new FullyQualifiedJavaType("org.mybatis.dynamic.sql.SqlBuilder");
    private FullyQualifiedJavaType pageQueryType;
    private FullyQualifiedJavaType dynamicSqlSupportType;
    private FullyQualifiedJavaType javaMapperType;

    /**
     * Instantiates a new Find default method generator.
     *
     * @param haoContext        the hao context
     * @param introspectedTable the introspected table
     */
    public FindDefaultMethodGenerator(HaoContext haoContext, IntrospectedTable introspectedTable) {
        super(haoContext, introspectedTable);
        pageQueryType=haoContext.getClientObjectType(introspectedTable);
        dynamicSqlSupportType=new FullyQualifiedJavaType(introspectedTable.getMyBatisDynamicSqlSupportType());
        javaMapperType=new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
    }

    /**
     * Get find method name string.
     *
     * @param interfaze the interfaze
     * @return the string
     */
    protected String getFindMethodName(Interface interfaze){
        return "find";
    }

    /**
     * Get page query param name string.
     *
     * @return the string
     */
    protected String getPageQueryParamName(){
        return GeneratorStringUtils.lowercaseFirstLetter(haoContext.getClientObjectType(introspectedTable).getShortName());
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        importedTypes.add(pageQueryType);
        importedTypes.add(pageResponseType);
        importedTypes.add(dynamicSqlSupportType);
        importedTypes.add(javaMapperType);
        importedTypes.add(whereClauseProviderType);
        importedTypes.add(renderingStrategiesType);
        importedTypes.add(nonRenderingWhereClauseExceptionType);
        importedTypes.add(sqlBuilderType);

        // 方法名称，可见性
        Method method = new Method(getFindMethodName(interfaze));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(false);
        method.setDefault(true);

        // 返回值
        FullyQualifiedJavaType returnType = pageResponseType;
        FullyQualifiedJavaType listType= new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        returnType.addTypeArgument(listType);
        method.setReturnType(returnType);

        // 函数参数
        method.addParameter(new Parameter(pageQueryType, getPageQueryParamName())); //$NON-NLS-1$

        // 添加内容
        method.addBodyLine(String.format("PageInfo pageInfo = new PageInfo.Builder(%s)",getPageQueryParamName()));
        method.addBodyLine("\t.withSingleQueryDynamicPageExecutor()");
        method.addBodyLine(String.format("\t.addOrderTable(new PageInfo.OrderTable(%s.%s.tableNameAtRuntime(), %s.selectList))",dynamicSqlSupportType.getShortName(),introspectedTable.getTableConfiguration().getTableName(),javaMapperType.getShortName()));
        method.addBodyLine("\t.build();");
        method.addBodyLine("try {");
        method.addBodyLine("WhereClauseProvider provider = SqlBuilder.where()");
        addWhereClauseProvider(method);
        method.addBodyLine("\t.build()");
        method.addBodyLine("\t.render(RenderingStrategies.MYBATIS3, \"whereProvider\").get();");
        method.addBodyLine("find(provider, pageInfo);");
        method.addBodyLine("return pageInfo.of();");
        method.addBodyLine("} catch (NonRenderingWhereClauseException e) {");
        method.addBodyLine("return pageInfo.empty();");
        method.addBodyLine("}");

        interfaze.addImportedTypes(importedTypes);
        interfaze.addStaticImport("org.mybatis.dynamic.sql.SqlBuilder.isEqualToWhenPresent");
        interfaze.addMethod(method);
    }

    /**
     * Add where clause provider.
     *
     * @param method the method
     */
    protected void addWhereClauseProvider(Method method) {
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        for (IntrospectedColumn column : columns) {
            String fieldName=column.getJavaProperty();
            method.addBodyLine(String.format("\t.and(%s.%s,isEqualToWhenPresent(%s.get%s()).filter(s->%s.isContainParam(\"%s\")))",dynamicSqlSupportType.getShortName(),fieldName,getPageQueryParamName(),capitalizeFirstLetter(fieldName), getPageQueryParamName(),fieldName));
        }


    }

}
