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

package com.hhao.common.mybatis.generator.plugin.utils;

import org.mybatis.generator.api.*;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.PluginAggregator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义的Mybatis Generator插件
 * 用于生成适合hhao-mybatis的代码
 *
 * @author wang sheng
 * @since 2024 /6/5 下午2:24
 */
public class HaoGeneratorUtils {
    private final String MYBATIS3_DYNAMIC_SQL = "MyBatis3DynamicSql";
    private final String MYBATIS3_SQL = "MyBatis3";

    private List<GeneratedJavaFile> generatedJavaFiles= new ArrayList<>();
    private List<GeneratedXmlFile> generatedXmlFiles= new ArrayList<>();
    private List<GeneratedKotlinFile> generatedKotlinFiles= new ArrayList<>();
    private List<GeneratedFile> otherGeneratedFiles= new ArrayList<>();
    private List<String> warnings= new ArrayList<>();

    /**
     * Generate files.
     *
     * @param context           the context
     * @param pluginAggregator  the plugin aggregator
     * @param introspectedTable the introspected table
     * @throws InterruptedException the interrupted exception
     * @throws SQLException         the sql exception
     */
    public void generateFiles(Context context,PluginAggregator pluginAggregator,IntrospectedTable introspectedTable) throws InterruptedException, SQLException {

        if (MYBATIS3_SQL.equalsIgnoreCase(context.getTargetRuntime())){
            // 改ResultSets名称
            introspectedTable.setBaseResultMapId(HaoResultMapperAnnotationUtils.generatorBaseResultMapId(introspectedTable));

            introspectedTable.getTableConfiguration().setDeleteByExampleStatementEnabled(false);
            introspectedTable.getTableConfiguration().setCountByExampleStatementEnabled(false);
            introspectedTable.getTableConfiguration().setDeleteByPrimaryKeyStatementEnabled(false);
            introspectedTable.getTableConfiguration().setSelectByExampleStatementEnabled(false);
            introspectedTable.getTableConfiguration().setSelectByPrimaryKeyStatementEnabled(true);
            introspectedTable.getTableConfiguration().setUpdateByExampleStatementEnabled(false);
            introspectedTable.getTableConfiguration().setUpdateByPrimaryKeyStatementEnabled(false);
            introspectedTable.getTableConfiguration().setInsertStatementEnabled(false);

            // 如果是MYBATIS3，只要生成xml文件
            generatedXmlFiles.addAll(introspectedTable
                    .getGeneratedXmlFiles());
        }else if (MYBATIS3_DYNAMIC_SQL.equalsIgnoreCase(context.getTargetRuntime())){
            // 如果是MYBATIS3_DSQL，生成Java文件
            generatedJavaFiles.addAll(introspectedTable
                    .getGeneratedJavaFiles());
        }

        generatedJavaFiles.addAll(pluginAggregator
                .contextGenerateAdditionalJavaFiles(introspectedTable));
        generatedXmlFiles.addAll(pluginAggregator
                .contextGenerateAdditionalXmlFiles(introspectedTable));
        generatedKotlinFiles.addAll(pluginAggregator
                .contextGenerateAdditionalKotlinFiles(introspectedTable));
        otherGeneratedFiles.addAll(pluginAggregator
                .contextGenerateAdditionalFiles(introspectedTable));
    }

    /**
     * Gets warnings.
     *
     * @return the warnings
     */
    public List<String> getWarnings() {
        return warnings;
    }

    /**
     * Gets other generated files.
     *
     * @return the other generated files
     */
    public List<GeneratedFile> getOtherGeneratedFiles() {
        return otherGeneratedFiles;
    }

    /**
     * Gets generated kotlin files.
     *
     * @return the generated kotlin files
     */
    public List<GeneratedKotlinFile> getGeneratedKotlinFiles() {
        return generatedKotlinFiles;
    }

    /**
     * Gets generated xml files.
     *
     * @return the generated xml files
     */
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        return generatedXmlFiles;
    }

    /**
     * Gets generated java files.
     *
     * @return the generated java files
     */
    public List<GeneratedJavaFile> getGeneratedJavaFiles() {
        return generatedJavaFiles;
    }


    /**
     * Generator base result map id string.
     *
     * @param introspectedTable the introspected table
     * @return the string
     */

}
