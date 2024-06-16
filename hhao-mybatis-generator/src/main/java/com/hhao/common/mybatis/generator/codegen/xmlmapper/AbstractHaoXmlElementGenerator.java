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
package com.hhao.common.mybatis.generator.codegen.xmlmapper;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.config.Context;

import java.util.List;

/**
 * The type Abstract hao xml element generator.
 *
 * @author wang sheng
 * @since 2024 /6/9 下午3:28
 */
public abstract class AbstractHaoXmlElementGenerator extends AbstractXmlElementGenerator {
    /**
     * Instantiates a new Abstract hao xml element generator.
     *
     * @param context           the context
     * @param introspectedTable the introspected table
     * @param warnings          the warnings
     * @param progressCallback  the progress callback
     */
    public AbstractHaoXmlElementGenerator(Context context, IntrospectedTable introspectedTable, List<String> warnings, ProgressCallback progressCallback) {
        this.context=context;
        this.introspectedTable=introspectedTable;
        this.warnings=warnings;
        this.progressCallback=progressCallback;
    }

    /**
     * Generate my base column list id string.
     *
     * @return the string
     */
    protected String generateMyBaseColumnListId(){
        return introspectedTable.getTableConfiguration().getTableName()+"-cols";
    }

    /**
     * Get table alias name string.
     *
     * @param index the index
     * @return the string
     */
    protected String getTableAliasName(int index){
        return introspectedTable.getTableConfiguration().getTableName() + index;
    }
}
