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

package com.hhao.common.mybatis.page.executor.sql.parse;

import com.hhao.common.mybatis.page.executor.sql.token.Token;

/**
 * 解析sql语句时的清除规则
 *
 * @author Wang
 * @since 2021 /11/19 10:10
 */
public class ParseRule {
    private boolean cleanOrderBy=true;
    private boolean cleanLimit=true;
    private boolean cleanOffset=true;
    private boolean cleanRootSelectItems =true;

    /**
     * Is clean order by boolean.
     *
     * @return the boolean
     */
    public boolean isCleanOrderBy() {
        return cleanOrderBy;
    }

    /**
     * Sets clean order by.
     *
     * @param cleanOrderBy the clean order by
     */
    public void setCleanOrderBy(boolean cleanOrderBy) {
        this.cleanOrderBy = cleanOrderBy;
    }

    /**
     * Is clean limit boolean.
     *
     * @return the boolean
     */
    public boolean isCleanLimit() {
        return cleanLimit;
    }

    /**
     * Sets clean limit.
     *
     * @param cleanLimit the clean limit
     */
    public void setCleanLimit(boolean cleanLimit) {
        this.cleanLimit = cleanLimit;
    }

    /**
     * Is clean offset boolean.
     *
     * @return the boolean
     */
    public boolean isCleanOffset() {
        return cleanOffset;
    }

    /**
     * Sets clean offset.
     *
     * @param cleanOffset the clean offset
     */
    public void setCleanOffset(boolean cleanOffset) {
        this.cleanOffset = cleanOffset;
    }

    /**
     * Is clean root select items boolean.
     *
     * @return the boolean
     */
    public boolean isCleanRootSelectItems() {
        return cleanRootSelectItems;
    }

    /**
     * Sets clean root select items.
     *
     * @param cleanRootSelectItems the clean root select items
     */
    public void setCleanRootSelectItems(boolean cleanRootSelectItems) {
        this.cleanRootSelectItems = cleanRootSelectItems;
    }

}
