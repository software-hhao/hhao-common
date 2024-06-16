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
package com.hhao.common.mybatis.generator.codegen.javahao;

import com.hhao.common.mybatis.generator.codegen.HaoContext;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.JavaBeansUtil.*;

/**
 * The type Java client object generator.
 *
 * @author wang sheng
 * @since 2024 /6/10 下午3:23
 */
public class JavaClientObjectGenerator extends AbstractHaoJavaGenerator {
    /**
     * The Root class name.
     */
    protected final String ROOT_CLASS_NAME = "com.hhao.common.ddd.dto.request.DynamicParamsPageQuery";

    /**
     * Instantiates a new Java client object generator.
     *
     * @param haoContext        the hao context
     * @param introspectedTable the introspected table
     */
    public JavaClientObjectGenerator(HaoContext haoContext, IntrospectedTable introspectedTable) {
        super(haoContext, introspectedTable);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        CommentGenerator commentGenerator = context.getCommentGenerator();

        TopLevelClass topLevelClass = new TopLevelClass(haoContext.getClientObjectType(introspectedTable));
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);

        FullyQualifiedJavaType superClass = getSuperClass();
        if (superClass != null) {
            topLevelClass.setSuperClass(superClass);
            topLevelClass.addImportedType(superClass);
        }

        List<IntrospectedColumn> introspectedColumns = getColumnsInThisClass();

        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            Field field = getJavaBeansField(introspectedColumn, context, introspectedTable);
            topLevelClass.addField(field);
            topLevelClass.addImportedType(field.getType());

            Method method = getJavaBeansGetter(introspectedColumn, context, introspectedTable);
            topLevelClass.addMethod(method);

            if (!introspectedTable.isImmutable()) {
                method = getJavaBeansSetter(introspectedColumn, context, introspectedTable);
                topLevelClass.addMethod(method);
            }
        }

        List<CompilationUnit> answer = new ArrayList<>();
        answer.add(topLevelClass);

        return answer;
    }

    /**
     * Gets super class.
     *
     * @return the super class
     */
    protected FullyQualifiedJavaType getSuperClass() {
        return new FullyQualifiedJavaType(ROOT_CLASS_NAME);
    }

    private boolean includePrimaryKeyColumns() {
        return !introspectedTable.getRules().generatePrimaryKeyClass() && introspectedTable.hasPrimaryKeyColumns();
    }

    private boolean includeBLOBColumns() {
        return !introspectedTable.getRules().generateRecordWithBLOBsClass() && introspectedTable.hasBLOBColumns();
    }

    private List<IntrospectedColumn> getColumnsInThisClass() {
        List<IntrospectedColumn> introspectedColumns;
        if (includePrimaryKeyColumns()) {
            if (includeBLOBColumns()) {
                introspectedColumns = introspectedTable.getAllColumns();
            } else {
                introspectedColumns = introspectedTable.getNonBLOBColumns();
            }
        } else {
            if (includeBLOBColumns()) {
                introspectedColumns = introspectedTable.getNonPrimaryKeyColumns();
            } else {
                introspectedColumns = introspectedTable.getBaseColumns();
            }
        }

        return introspectedColumns;
    }
}
