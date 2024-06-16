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
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

/**
 * The type Abstract hao java mapper method generator.
 *
 * @author wang sheng
 * @since 2024 /6/10 下午3:29
 */
public abstract class AbstractHaoJavaMapperMethodGenerator extends AbstractJavaMapperMethodGenerator {
    /**
     * The Hao context.
     */
    protected HaoContext haoContext;

    /**
     * Instantiates a new Abstract hao java mapper method generator.
     *
     * @param haoContext        the hao context
     * @param introspectedTable the introspected table
     */
    public AbstractHaoJavaMapperMethodGenerator(HaoContext haoContext, IntrospectedTable introspectedTable) {
        this.context=haoContext.getContext();
        this.introspectedTable=introspectedTable;
        this.warnings=haoContext.getWarnings();
        this.progressCallback=haoContext.getProgressCallback();
        this.haoContext=haoContext;
    }
}
