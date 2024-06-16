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

import com.hhao.common.mybatis.generator.codegen.javahao.AbstractHaoJavaGenerator;
import com.hhao.common.mybatis.generator.codegen.javahao.JavaClientObjectGenerator;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.config.PropertyRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Hao ex my batis generator.
 *
 * @author wang sheng
 * @since 2024 /6/9 下午2:26
 */
public class HaoExMyBatisGenerator {
    /**
     * The Hao context.
     */
    protected HaoContext haoContext;

    /**
     * Instantiates a new Hao ex my batis generator.
     *
     * @param haoContext the hao context
     */
    public HaoExMyBatisGenerator(HaoContext haoContext) {
        this.haoContext = haoContext;
    }

    /**
     * General hao java file list.
     *
     * @param introspectedTable the introspected table
     * @return the list
     */
    public List<GeneratedJavaFile> generalHaoJavaFile(IntrospectedTable introspectedTable) {
        List<AbstractJavaGenerator> javaGenerators = new ArrayList<>();

        AbstractHaoJavaGenerator haoJavaGenerator = new JavaClientObjectGenerator(haoContext, introspectedTable);
        javaGenerators.add(haoJavaGenerator);

        // 开始执行
        List<GeneratedJavaFile> answer = new ArrayList<>();
        for (AbstractJavaGenerator javaGenerator : javaGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
            for (CompilationUnit compilationUnit : compilationUnits) {
                GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
                        javaGenerator.getProject(),
                        haoContext.getContext().getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                        haoContext.getContext().getJavaFormatter());
                answer.add(gjf);
            }
        }
        return answer;
    }
}
