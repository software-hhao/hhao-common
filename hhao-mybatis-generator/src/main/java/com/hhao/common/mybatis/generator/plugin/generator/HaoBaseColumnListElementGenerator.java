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

package com.hhao.common.mybatis.generator.plugin.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 生成自定义的SELECT列名
 *
 * @author wang sheng
 * @since 2024 /6/6 下午2:26
 */
public class HaoBaseColumnListElementGenerator extends AbstractXmlElementGenerator {
    private static final String  SELECT_TABLE_SHORT_NAME_PARAM ="${tableShortName}";
    private static final String  ALIAS_TABLE_SHORT_NAME_PARAM ="${aliasTableShortName}";
    private List<String> warnings= new ArrayList<>();
    private ProgressCallback progressCallback=new ProgressCallback(){};

    /**
     * Instantiates a new My base column list element generator.
     */
    public HaoBaseColumnListElementGenerator() {
        super();
    }

    private String generateMyBaseColumnListId(){
        return introspectedTable.getFullyQualifiedTableNameAtRuntime()+"-cols";
    }

    /**
     * Build my base column list list.
     *
     * @param columns the columns
     * @return the list
     */
    protected List<TextElement> buildMyBaseColumnList(List<IntrospectedColumn> columns) {
        List<TextElement> answer = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        Iterator<IntrospectedColumn> iter = columns.iterator();
        IntrospectedColumn introspectedColumn;
        while (iter.hasNext()) {
            introspectedColumn=iter.next();
            sb.append(SELECT_TABLE_SHORT_NAME_PARAM);
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" as ");
            sb.append(ALIAS_TABLE_SHORT_NAME_PARAM);
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            if (iter.hasNext()) {
                sb.append(", ");
            }
            answer.add(new TextElement(sb.toString()));
            sb.setLength(0);
        }
        if (sb.length() > 0) {
            answer.add(new TextElement(sb.toString()));
        }
        return answer;
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", generateMyBaseColumnListId()));
        buildMyBaseColumnList(introspectedTable.getNonBLOBColumns()).forEach(answer::addElement);
        parentElement.addElement(answer);
    }

    /**
     * Initialize and execute generator.
     *
     * @param parentElement     the parent element
     * @param introspectedTable the introspected table
     */
    public void initializeAndExecuteGenerator(XmlElement parentElement, IntrospectedTable introspectedTable) {
        this.setContext(context);
        this.setIntrospectedTable(introspectedTable);
        this.setProgressCallback(progressCallback);
        this.setWarnings(warnings);
        this.addElements(parentElement);
    }
}
