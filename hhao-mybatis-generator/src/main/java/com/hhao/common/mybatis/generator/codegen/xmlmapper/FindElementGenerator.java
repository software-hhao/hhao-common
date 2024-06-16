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

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.config.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The type Find element generator.
 *
 * @author wang sheng
 * @since 2024 /6/9 下午2:26
 */
public class FindElementGenerator extends AbstractHaoXmlElementGenerator {

    /**
     * Instantiates a new Find element generator.
     *
     * @param context           the context
     * @param introspectedTable the introspected table
     * @param warnings          the warnings
     * @param progressCallback  the progress callback
     */
    public FindElementGenerator(Context context, IntrospectedTable introspectedTable, List<String> warnings, ProgressCallback progressCallback) {
        super(context, introspectedTable, warnings, progressCallback);
    }

    /**
     * 取StatementId:findXXXXX
     *
     * @param introspectedTable the introspected table
     * @return string string
     */
    protected String getFindStatementId(IntrospectedTable introspectedTable){
        //String name=introspectedTable.getBaseRecordType();
        //name=GeneratorStringUtils.getLastSegmentAfterDot(name);
        return "find";
    }

    /**
     * 创建Include Element
     *
     * @param findElement the find element
     */
    protected void buildIncludeColumnElement(XmlElement findElement){
        IncludeColumnElementGenerator includeColumnElementGenerator=new IncludeColumnElementGenerator(context,introspectedTable,warnings,progressCallback);
        includeColumnElementGenerator.addElements(findElement);
    }

    /**
     * 创建自表连接的on条件
     *
     * @param aliasTable1 the alias table 1
     * @param aliasTable2 the alias table 2
     * @return list list
     */
    protected List<TextElement> buildPrimaryKeyOnAssociate(String aliasTable1,String aliasTable2) {
        List<TextElement> answer = new ArrayList<>();
        boolean first = true;
        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            String line;
            if (first) {
                line = ""; //$NON-NLS-1$
                first = false;
            } else {
                line = "  and "; //$NON-NLS-1$
            }

            line += aliasTable1 + "." + MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn);
            line += " = "; //$NON-NLS-1$
            line += aliasTable2 + "." + MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn);
            answer.add(new TextElement(line));
        }
        return answer;
    }

    /**
     * 构建主键列
     *
     * @param retract :缩进
     * @return list list
     */
    protected List<TextElement> buildMyBasePrimaryKeyColumnList(String retract) {
        List<TextElement> answer = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(retract);

        Iterator<IntrospectedColumn> iter = introspectedTable.getPrimaryKeyColumns().iterator();
        IntrospectedColumn introspectedColumn;
        while (iter.hasNext()) {
            introspectedColumn=iter.next();
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            if (iter.hasNext()) {
                sb.append(", ");
            }
            if (sb.length()>20) {
                answer.add(new TextElement(sb.toString()));
                sb.setLength(0);
                sb.append(retract);
            }
        }
        if (sb.length() > 0) {
            answer.add(new TextElement(sb.toString()));
        }
        return answer;
    }

    /**
     * 创建TextElement
     * @param answer
     * @param sb
     */
    private void buildTextElement(XmlElement answer,StringBuilder sb) {
        if (sb!=null && sb.length()>0) {
            answer.addElement(new TextElement(sb.toString()));
            sb.setLength(0);
        }
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", getFindStatementId(introspectedTable)));
        answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));

        StringBuilder sb = new StringBuilder();
        sb.append("select");
        buildTextElement(answer,sb);

        buildIncludeColumnElement(answer);

        sb.append("from "); //$NON-NLS-1$
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        sb.append(" as ");
        sb.append(getTableAliasName(1));
        sb.append(" inner join ( ");
        buildTextElement(answer,sb);

        // 分页select
        String retract="    ";
        sb.append(retract);
        sb.append("select ");
        buildTextElement(answer,sb);
        buildMyBasePrimaryKeyColumnList(retract).forEach(answer::addElement);
        sb.append(retract);
        sb.append("from ");
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        buildTextElement(answer,sb);
        sb.append(retract);
        sb.append("${whereProvider.whereClause}");
        buildTextElement(answer,sb);
        sb.append(retract);
        sb.append("order by ");
        buildTextElement(answer,sb);
        buildMyBasePrimaryKeyColumnList(retract).forEach(answer::addElement);
        sb.append(retract);
        sb.append("limit #{pageInfo.limit,jdbcType=BIGINT} ");
        buildTextElement(answer,sb);
        sb.append(retract);
        sb.append("offset #{pageInfo.offset,jdbcType=BIGINT} ");
        buildTextElement(answer,sb);

        sb.append(" ) ");
        sb.append(" as ");
        sb.append(getTableAliasName(2));
        buildTextElement(answer,sb);
        sb.append("on ");
        buildTextElement(answer,sb);

        buildPrimaryKeyOnAssociate(getTableAliasName(1),getTableAliasName(2)).forEach(answer::addElement);

        sb.append("${pageInfo.orderBySql};");
        buildTextElement(answer,sb);

        // Count
        sb.append("select count(*) as count from ");
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        buildTextElement(answer,sb);
        sb.append("${whereProvider.whereClause}");
        buildTextElement(answer,sb);

        parentElement.addElement(answer);
    }
}
