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
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;

import java.util.Set;
import java.util.TreeSet;

/**
 * The type Find method generator.
 *
 * @author wang sheng
 * @since 2024 /6/10 下午3:20
 */
public class FindMethodGenerator extends AbstractHaoJavaMapperMethodGenerator{
    private FullyQualifiedJavaType paramType=new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param");
    private FullyQualifiedJavaType whereClauseProviderType=new FullyQualifiedJavaType("org.mybatis.dynamic.sql.where.render.WhereClauseProvider");
    private FullyQualifiedJavaType pageInfoType=new FullyQualifiedJavaType("com.hhao.common.mybatis.page.PageInfo");


    /**
     * Instantiates a new Find method generator.
     *
     * @param haoContext        the hao context
     * @param introspectedTable the introspected table
     */
    public FindMethodGenerator(HaoContext haoContext, IntrospectedTable introspectedTable) {
        super(haoContext, introspectedTable);
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

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        importedTypes.add(paramType);
        importedTypes.add(whereClauseProviderType);
        importedTypes.add(pageInfoType);

        // 方法名称，可见性
        Method method = new Method(getFindMethodName(interfaze));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        method.setDefault(false);

        // 返回值
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType listType= new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        returnType.addTypeArgument(listType);
        method.setReturnType(returnType);

        // 函数参数
        FullyQualifiedJavaType type = new FullyQualifiedJavaType("WhereClauseProvider");
        method.addParameter(new Parameter(whereClauseProviderType, "whereProvider", "@Param(\"whereProvider\")",false)); //$NON-NLS-1$
        method.addParameter(new Parameter(pageInfoType, "pageInfo", "@Param(\"pageInfo\")",false)); //$NON-NLS-1$

        // 添加扩展的注解
        addMapperAnnotations(interfaze, method);
        // 添加扩展的导入
        addExtraImports(interfaze);

        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(method);
    }

    /**
     * Add mapper annotations.
     *
     * @param interfaze the interfaze
     * @param method    the method
     */
    public void addMapperAnnotations(Interface interfaze, Method method) {
        // extension point for subclasses
    }

    /**
     * Add extra imports.
     *
     * @param interfaze the interfaze
     */
    public void addExtraImports(Interface interfaze) {
        // extension point for subclasses
    }
}
