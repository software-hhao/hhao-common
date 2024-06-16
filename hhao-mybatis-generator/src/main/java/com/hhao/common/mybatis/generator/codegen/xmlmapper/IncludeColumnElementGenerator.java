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
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Context;

import java.util.List;

/**
 * The type Include column element generator.
 *
 * @author wang sheng
 * @since 2024 /6/9 下午2:26
 */
public class IncludeColumnElementGenerator extends AbstractHaoXmlElementGenerator {

    /**
     * Instantiates a new Include column element generator.
     *
     * @param context           the context
     * @param introspectedTable the introspected table
     * @param warnings          the warnings
     * @param progressCallback  the progress callback
     */
    public IncludeColumnElementGenerator(Context context, IntrospectedTable introspectedTable, List<String> warnings, ProgressCallback progressCallback) {
        super(context, introspectedTable, warnings, progressCallback);
    }

    /**
     * Get refid string.
     *
     * @return the string
     */
    protected String getRefid(){
        String name=introspectedTable.getMyBatis3SqlMapNamespace();
        return name + "." + generateMyBaseColumnListId();
    }

    /**
     * Get table short name string.
     *
     * @return the string
     */
    protected String getTableShortName(){
        return getTableAliasName(1) + ".";
    }

    /**
     * Get alias table short name string.
     *
     * @return the string
     */
    protected String getAliasTableShortName(){
        return "";
    }

    /**
     * Build include property element xml element.
     *
     * @param includeElement the include element
     * @return the xml element
     */
    protected XmlElement buildIncludePropertyElement(XmlElement includeElement) {
        XmlElement propertyElement1 = new XmlElement("property");
        propertyElement1.addAttribute(new Attribute("name","tableShortName"));
        propertyElement1.addAttribute(new Attribute("value", getTableShortName()));

        XmlElement propertyElement2 = new XmlElement("property");
        propertyElement2.addAttribute(new Attribute("name","aliasTableShortName"));
        propertyElement2.addAttribute(new Attribute("value", getAliasTableShortName()));

        includeElement.addElement(propertyElement1);
        includeElement.addElement(propertyElement2);
        return includeElement;
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("include");
        answer.addAttribute(new Attribute("refid", getRefid()));

        buildIncludePropertyElement(answer);
        parentElement.addElement(answer);
    }
}