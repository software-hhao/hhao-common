/*
 * Copyright 2018-2021 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.mybatis.page.executor.sql.parse;

/**
 * The enum Sql keyword.
 */
public enum SqlKeyword {
    /**
     * Select sql keyword.
     */
    SELECT("select"),
    /**
     * From sql keyword.
     */
    FROM("from"),
    /**
     * Where sql keyword.
     */
    WHERE("where"),
    /**
     * Order sql keyword.
     */
    ORDER("order"),
    /**
     * Group sql keyword.
     */
    GROUP("group"),
    /**
     * Having sql keyword.
     */
    HAVING("having"),
    /**
     * Limit sql keyword.
     */
    LIMIT("limit"),
    /**
     * Offset sql keyword.
     */
    OFFSET("offset"),
    /**
     * Union sql keyword.
     */
    UNION("union"),
    /**
     * For sql keyword.
     */
    FOR("for"),
    /**
     * Param sql keyword.
     */
    PARAM("?"),
    /**
     * Left parenthesis sql keyword.
     */
    LEFT_PARENTHESIS("("),
    /**
     * Right parenthesis sql keyword.
     */
    RIGHT_PARENTHESIS(")"),
    /**
     * Separator sql keyword.
     */
    SEPARATOR(";"),
    /**
     * All sql keyword.
     */
    ALL("all"),
    /**
     * Distinct sql keyword.
     */
    DISTINCT("distinct");


    SqlKeyword(String value){
        this.value=value;
    }
    private String value;

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
