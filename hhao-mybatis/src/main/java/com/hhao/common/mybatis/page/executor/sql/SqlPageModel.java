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

package com.hhao.common.mybatis.page.executor.sql;

/**
 * The type Sql page model.
 *
 * @author Wang
 * @since 2021 /11/21 15:27
 */
public class SqlPageModel {
    /**
     * The Select.
     */
    private SqlModel select;
    /**
     * The Count.
     */
    private SqlModel count;

    /**
     * Instantiates a new Sql page model.
     */
    public SqlPageModel(){

    }

    /**
     * Instantiates a new Sql page model.
     *
     * @param select the select
     * @param count  the count
     */
    public SqlPageModel(SqlModel select,SqlModel count){
        this.select=select;
        this.count=count;
    }

    /**
     * Gets select.
     *
     * @return the select
     */
    public SqlModel getSelect() {
        return select;
    }

    /**
     * Sets select.
     *
     * @param select the select
     */
    public void setSelect(SqlModel select) {
        this.select = select;
    }

    /**
     * Gets count.
     *
     * @return the count
     */
    public SqlModel getCount() {
        return count;
    }

    /**
     * Sets count.
     *
     * @param count the count
     */
    public void setCount(SqlModel count) {
        this.count = count;
    }
}
