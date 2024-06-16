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

package com.hhao.common.mybatis.generator.plugin;

import com.hhao.common.mybatis.generator.TargetRuntime;
import com.hhao.common.mybatis.generator.codegen.HaoContext;
import com.hhao.common.mybatis.generator.codegen.HaoMyBatisGenerator;
import com.hhao.common.mybatis.generator.codegen.javamapper.FindDefaultMethodGenerator;
import com.hhao.common.mybatis.generator.codegen.javamapper.FindMethodGenerator;
import com.hhao.common.mybatis.generator.codegen.utils.ResultMapperAnnotationUtils;
import com.hhao.common.mybatis.generator.codegen.xmlmapper.ColumnListElementGenerator;
import com.hhao.common.mybatis.generator.codegen.xmlmapper.FindElementGenerator;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.PluginAggregator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自定义Mybatis Generator插件
 *
 * @author wang sheng
 * @since 2024 /6/5 下午1:33
 */
public class HaoGeneratedPlugin extends PluginAdapter {
    private List<String> warnings= new ArrayList<>();
    private ProgressCallback progressCallback=new ProgressCallback(){};
    private List<GeneratedJavaFile> generatedJavaFiles= new ArrayList<>();
    private List<GeneratedXmlFile> generatedXmlFiles= new ArrayList<>();
    private List<GeneratedKotlinFile> generatedKotlinFiles= new ArrayList<>();
    private List<GeneratedFile> otherGeneratedFiles= new ArrayList<>();
    private HaoContext haoContext;


    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        super.initialized(introspectedTable);
        haoContext=new HaoContext(this.context,warnings,progressCallback,properties);
    }

    /**
     * 这里拦截MyBatisGenerator.generate方法
     * 替换成自己的文件生成方式
     *
     * @param introspectedTable the current table
     * @return
     */
    @Override
    public boolean shouldGenerate(IntrospectedTable introspectedTable) {
        try {
            HaoMyBatisGenerator haoMyBatisGenerator =new HaoMyBatisGenerator(generatedJavaFiles,generatedXmlFiles,generatedKotlinFiles,otherGeneratedFiles,haoContext);
            haoMyBatisGenerator.generateFiles((PluginAggregator) context.getPlugins(),introspectedTable);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // 跳过原有的文件生成
        return false;
    }

    /**
     * 添加自定义生成的Java文件
     * @return
     */
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
        return this.generatedJavaFiles;
    }

    /**
     * 添加自定义生成的xml文件
     * @return
     */
    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
        if (generatedXmlFiles.size()==0){
            try {
                generatedXmlFiles=generalHaoXmlFile();
            } catch (XMLParserException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InvalidConfigurationException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return this.generatedXmlFiles;
    }

    /**
     * 添加自定义生成的文件
     * @return
     */
    @Override
    public List<GeneratedFile> contextGenerateAdditionalFiles() {
        return super.contextGenerateAdditionalFiles();
    }

    /**
     * 改生成Mapper Java类中使用@Results的注解
     * 改成直接使用XML文件中定义的ResultMap
     *
     * @param method
     *     the generated selectMany method
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *     The class containing information about the table as introspected from the database
     * @return
     */
    @Override
    public boolean clientBasicSelectManyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        ResultMapperAnnotationUtils.replaceResultAnnotation(method,introspectedTable);
        return true;
    }

    /**
     * 改生成Mapper Java类中使用@Results的注解
     * 改成直接使用XML文件中定义的ResultMap
     *
     * @param method
     *     the generated selectOne method
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *     The class containing information about the table as introspected from the database
     * @return
     */
    @Override
    public boolean clientBasicSelectOneMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        ResultMapperAnnotationUtils.replaceResultAnnotation(method,introspectedTable);
        return true;
    }

    /**
     * 在client map中添加新的方法
     *
     * @param interfaze
     *            the generated interface if any, may be null
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return
     */
    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        FindMethodGenerator findMethodGenerator=new FindMethodGenerator(haoContext, introspectedTable);
        findMethodGenerator.addInterfaceElements(interfaze);

        FindDefaultMethodGenerator findDefaultMethodGenerator=new FindDefaultMethodGenerator(haoContext, introspectedTable);
        findDefaultMethodGenerator.addInterfaceElements(interfaze);
        return true;
    }


    /**
     * 添加扩展的Java文件
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return
     */
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        return Collections.emptyList();
    }


    /**
     * 添加自定义的Select列表
     *
     * @param document
     *            the generated document (note that this is the MyBatis generator's internal
     *            Document class - not the w3c XML Document class)
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        // 生成自定义形式的列表
        ColumnListElementGenerator columnListElementGenerator = new ColumnListElementGenerator(context, introspectedTable,this.warnings,this.progressCallback);
        columnListElementGenerator.addElements(document.getRootElement());

        // 生成Select
        FindElementGenerator findElementGenerator  = new FindElementGenerator(context, introspectedTable,this.warnings,this.progressCallback);
        findElementGenerator.addElements(document.getRootElement());
        return true;
    }

    /**
     * 再次生成xml文件时，完全重写，注意，原来的内容会被替去
     *
     * @param sqlMap
     *            the generated file (containing the file name, package name,
     *            and project name)
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return
     */
    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        sqlMap.setMergeable(false);
        return true;
    }

    /**
     * 取消原来的表列输出
     * @param element
     *            the generated &lt;sql&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return
     */
    @Override
    public boolean sqlMapBaseColumnListElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }


    /**
     * 生成xml文件
     *
     * @return
     * @throws XMLParserException
     * @throws IOException
     * @throws InvalidConfigurationException
     * @throws SQLException
     * @throws InterruptedException
     */
    public List<GeneratedXmlFile> generalHaoXmlFile() throws XMLParserException, IOException, InvalidConfigurationException, SQLException, InterruptedException {
        // 将原来的Context运行模式设置为MYBATIS3_SQL
        context.setTargetRuntime(TargetRuntime.MYBATIS3_SQL);
        Configuration configuration=new Configuration();
        configuration.addContext(context);
        configuration.validate();
        // 构造一个新的MyBatisGenerator
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(configuration, null, null);
        myBatisGenerator.generate(this.progressCallback);
        return myBatisGenerator.getGeneratedXmlFiles();
    }
}
