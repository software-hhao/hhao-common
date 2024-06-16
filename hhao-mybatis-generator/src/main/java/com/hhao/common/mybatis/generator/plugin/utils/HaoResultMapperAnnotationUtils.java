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

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Method;

import java.util.Iterator;
import java.util.List;

/**
 * @author wang sheng
 * @since 2024/6/7 上午10:26
 */
public class HaoResultMapperAnnotationUtils {

    /**
     * 替换Java文件中的@Results和@ResultMap
     *
     * @param method
     * @param introspectedTable
     */
    public static void replaceResultAnnotation(Method method, IntrospectedTable introspectedTable) {
        replaceResultMapAnnotation(method, introspectedTable);
        replaceResultsAnnotation(method, introspectedTable);
    }


    /**
     * 替换Java文件中的@Results注解为@ResultMap
     *
     * @param method
     * @param introspectedTable
     */
    private static void replaceResultsAnnotation(Method method,  IntrospectedTable introspectedTable) {
        List<String> annotations=method.getAnnotations();
        Iterator<String> iterator =annotations.iterator();
        while (iterator.hasNext()) {
            String annotation = iterator.next();
            if (annotation.contains("@Result(")) {
                iterator.remove();
            }
        }
        int i=0;
        for (; i < annotations.size(); i++){
            if (annotations.get(i).contains("@Results")){
                break;
            }
        }
        if (i<annotations.size()){
            annotations.set(i,"@ResultMap(\"" + generatorBaseResultMapId(introspectedTable) + "\")");
            if (++i < annotations.size()){
                if (annotations.get(i).trim().equals("})")){
                    annotations.remove(i);
                }
            }
        }
    }

    /**
     * 替换Java文件中的ResultMap
     *
     * @param method
     * @param introspectedTable
     */
    private static void replaceResultMapAnnotation(Method method, IntrospectedTable introspectedTable) {
        List<String> annotations=method.getAnnotations();
        int i=0;
        for (; i < annotations.size(); i++){
            if (annotations.get(i).contains("@ResultMap")){
                break;
            }
        }
        if (i<annotations.size()){
            annotations.set(i,"@ResultMap(\"" + generatorBaseResultMapId(introspectedTable) + "\")");
        }
    }

    /**
     * 生成BaseResultMapId
     *
     * @param introspectedTable
     * @return
     */
    public static String generatorBaseResultMapId(IntrospectedTable introspectedTable){
        String name=introspectedTable.getBaseRecordType();
        if (name!=null && !name.isBlank()){
            name=HaoGeneratorStringUtils.getLastSegmentAfterDot(name)+ "Result";
        }else{
            name=HaoGeneratorStringUtils.capitalizeFirstLetter(introspectedTable.getFullyQualifiedTableNameAtRuntime()) + "Result";
        }
        return introspectedTable.getMyBatis3SqlMapNamespace() + "." + name;
    }
}
