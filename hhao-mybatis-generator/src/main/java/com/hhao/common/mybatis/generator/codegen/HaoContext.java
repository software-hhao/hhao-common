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

import com.hhao.common.mybatis.generator.codegen.utils.GeneratorStringUtils;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.util.JavaBeansUtil;

import java.util.List;
import java.util.Properties;

/**
 * 上下文
 *
 * @author wang sheng
 * @since 2024 /6/9 下午2:26
 */
public class HaoContext {
    /**
     * The Context.
     */
    protected Context context;
    /**
     * The Warnings.
     */
    protected List<String> warnings;
    /**
     * The Progress callback.
     */
    protected ProgressCallback progressCallback;
    /**
     * The Properties.
     */
    protected Properties properties;

    /**
     * Instantiates a new Hao context.
     *
     * @param context          the context
     * @param warnings         the warnings
     * @param progressCallback the progress callback
     * @param properties       the properties
     */
    public HaoContext(Context context, List<String> warnings, ProgressCallback progressCallback, Properties properties) {
        this.context = context;
        this.warnings = warnings;
        this.progressCallback = progressCallback;
        this.properties = properties;
    }

    /**
     * Gets context.
     *
     * @return the context
     */
    public Context getContext() {
        return context;
    }

    /**
     * Sets context.
     *
     * @param context the context
     */
    public void setContext(Context context) {
        this.context = context;
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
     * Sets warnings.
     *
     * @param warnings the warnings
     */
    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    /**
     * Gets progress callback.
     *
     * @return the progress callback
     */
    public ProgressCallback getProgressCallback() {
        return progressCallback;
    }

    /**
     * Sets progress callback.
     *
     * @param progressCallback the progress callback
     */
    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    /**
     * Gets properties.
     *
     * @return the properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Sets properties.
     *
     * @param properties the properties
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * Gets client object type.
     *
     * @param introspectedTable the introspected table
     * @return the client object type
     */
    public FullyQualifiedJavaType getClientObjectType(IntrospectedTable introspectedTable) {
        String tableFieldName= JavaBeansUtil.getValidPropertyName(introspectedTable.getMyBatisDynamicSQLTableObjectName());
        String clientObjectPackage;
        // 获取包路径
        clientObjectPackage = properties.getProperty("clientObjectPackage");
        if (clientObjectPackage==null || clientObjectPackage.isBlank()){
            clientObjectPackage=context.getJavaModelGeneratorConfiguration().getTargetPackage();
        }
        return new FullyQualifiedJavaType(clientObjectPackage +"."+ GeneratorStringUtils.capitalizeFirstLetter(tableFieldName) + "PageQuery");
    }
}
