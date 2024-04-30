/*
 * Copyright 2008-2024 wangsheng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.mybatis.page.executor;

import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.SQLException;

/**
 * 封装处理Sql语句过程中添加的参数
 *
 * @author Wang
 * @since 1.0.0
 */
public class ParamMapping {
    private String property;
    private Class<?> javaType;
    private Object value;

    private ParamMapping(String property,Class<?> javaType,Object value) {
        this.property=property;
        this.javaType=javaType;
        this.value=value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Gets property.
     *
     * @return the property
     */
    public String getProperty() {
        return property;
    }

    /**
     * Sets property.
     *
     * @param property the property
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * Gets java type.
     *
     * @return the java type
     */
    public Class<?> getJavaType() {
        return javaType;
    }

    /**
     * Sets java type.
     *
     * @param javaType the java type
     */
    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    /**
     * The type Builder.
     */
    public static class Builder{
        /**
         * Create param mapping.
         *
         * @param property the property
         * @param javaType the java type
         * @param value    the value
         * @return the param mapping
         */
        public static ParamMapping create(String property,Class<?> javaType,Object value){
            return new ParamMapping(property,javaType,value);
        }
    }
}
