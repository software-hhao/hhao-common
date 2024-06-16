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

package com.hhao.common.mybatis.generator.codegen;

import com.hhao.common.mybatis.generator.TargetRuntime;
import com.hhao.common.mybatis.generator.codegen.utils.ResultMapperAnnotationUtils;
import org.mybatis.generator.api.*;
import org.mybatis.generator.internal.PluginAggregator;

import java.sql.SQLException;
import java.util.List;

/**
 * 自定义的Mybatis Generator插件
 * 用于生成适合hhao-mybatis的代码
 *
 * @author wang sheng
 * @since 2024 /6/5 下午2:24
 */
public class HaoMyBatisGenerator {
    private List<GeneratedJavaFile> generatedJavaFiles;
    private List<GeneratedXmlFile> generatedXmlFiles;
    private List<GeneratedKotlinFile> generatedKotlinFiles;
    private List<GeneratedFile> otherGeneratedFiles;

    /**
     * The Hao context.
     */
    protected HaoContext haoContext;
    /**
     * The Hao ex my batis generator.
     */
    protected HaoExMyBatisGenerator haoExMyBatisGenerator;

    /**
     * Instantiates a new Hao my batis generator.
     *
     * @param generatedJavaFiles   the generated java files
     * @param generatedXmlFiles    the generated xml files
     * @param generatedKotlinFiles the generated kotlin files
     * @param otherGeneratedFiles  the other generated files
     * @param haoContext           the hao context
     */
    public HaoMyBatisGenerator(List<GeneratedJavaFile> generatedJavaFiles, List<GeneratedXmlFile> generatedXmlFiles, List<GeneratedKotlinFile> generatedKotlinFiles, List<GeneratedFile> otherGeneratedFiles, HaoContext haoContext) {
        this.generatedJavaFiles = generatedJavaFiles;
        this.generatedXmlFiles = generatedXmlFiles;
        this.generatedKotlinFiles = generatedKotlinFiles;
        this.otherGeneratedFiles = otherGeneratedFiles;
        this.haoContext=haoContext;
        haoExMyBatisGenerator =new HaoExMyBatisGenerator(haoContext);
    }

    /**
     * Generate files.
     *
     * @param pluginAggregator  the plugin aggregator
     * @param introspectedTable the introspected table
     * @throws InterruptedException the interrupted exception
     * @throws SQLException         the sql exception
     */
    public void generateFiles(PluginAggregator pluginAggregator,IntrospectedTable introspectedTable) throws InterruptedException, SQLException {
        if (TargetRuntime.MYBATIS3_SQL.equalsIgnoreCase(haoContext.getContext().getTargetRuntime())){
            // 改ResultSets名称
            introspectedTable.setBaseResultMapId(ResultMapperAnnotationUtils.generatorBaseResultMapId(introspectedTable));

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
        }else if (TargetRuntime.MYBATIS3_DYNAMIC_SQL.equalsIgnoreCase(haoContext.getContext().getTargetRuntime())){
            // 如果是MYBATIS3_DSQL，生成Java文件
            generatedJavaFiles.addAll(introspectedTable.getGeneratedJavaFiles());
            // 生成HaoJava文件
            generatedJavaFiles.addAll(haoExMyBatisGenerator.generalHaoJavaFile(introspectedTable));
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
     * Gets generated java files.
     *
     * @return the generated java files
     */
    public List<GeneratedJavaFile> getGeneratedJavaFiles() {
        return generatedJavaFiles;
    }

    /**
     * Sets generated java files.
     *
     * @param generatedJavaFiles the generated java files
     */
    public void setGeneratedJavaFiles(List<GeneratedJavaFile> generatedJavaFiles) {
        this.generatedJavaFiles = generatedJavaFiles;
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
     * Sets generated xml files.
     *
     * @param generatedXmlFiles the generated xml files
     */
    public void setGeneratedXmlFiles(List<GeneratedXmlFile> generatedXmlFiles) {
        this.generatedXmlFiles = generatedXmlFiles;
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
     * Sets generated kotlin files.
     *
     * @param generatedKotlinFiles the generated kotlin files
     */
    public void setGeneratedKotlinFiles(List<GeneratedKotlinFile> generatedKotlinFiles) {
        this.generatedKotlinFiles = generatedKotlinFiles;
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
     * Sets other generated files.
     *
     * @param otherGeneratedFiles the other generated files
     */
    public void setOtherGeneratedFiles(List<GeneratedFile> otherGeneratedFiles) {
        this.otherGeneratedFiles = otherGeneratedFiles;
    }

    /**
     * Gets hao context.
     *
     * @return the hao context
     */
    public HaoContext getHaoContext() {
        return haoContext;
    }

    /**
     * Sets hao context.
     *
     * @param haoContext the hao context
     */
    public void setHaoContext(HaoContext haoContext) {
        this.haoContext = haoContext;
    }
}
