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

package com.hhao.common.page;

/**
 * 分页的基本信息
 *
 * @author Wang
 * @since 2022/2/22 22:05
 */
public interface Page {
    /**
     * 当前页
     *
     * @return page num
     */
    long getPageNum();

    /**
     * 页大小
     *
     * @return page size
     */
    long getPageSize();

    /**
     * 前置缓存页大小
     * 需大于0
     *
     * @return pre cached page
     */
    long getPreCachedPage();

    /**
     * 后置缓存页大小
     * 需大于0
     *
     * @return post cached page
     */
    long getPostCachedPage();

    /**
     * Is include total rows boolean.
     *
     * @return the boolean
     */
    boolean isIncludeTotalRows();

    /**
     * Get order columns string [ ].
     *
     * @return the string [ ]
     */
    String [] getOrderColumns();

    /**
     * Gets order direction.
     *
     * @return the order direction
     */
    OrderDirection getOrderDirection();

    /**
     * 计算分页偏移量
     *
     * @return offset offset
     */
    default long getOffset() {
        long current = getPageNum()-getPreCachedPage();
        if (current <= 1L) {
            return 0L;
        }
        return Math.max((current - 1) * getPageSize(), 0L);
    }

    /**
     * 计算分页大小
     *
     * @return limit limit
     */
    default long getLimit() {
        long pages=getPreCachedPage()+getPostCachedPage()+1;
        if (pages < 0L) {
            return 0L;
        }
        return pages * getPageSize();
    }

    /**
     * The enum Order direction.
     */
    enum OrderDirection{
        /**
         * Asc order direction.
         */
        ASC,
        /**
         * Desc order direction.
         */
        DESC,
        /**
         * No order direction.
         */
        NO
    }
}
