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

package com.hhao.common.mybatis.page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 分页结果
 *
 * @param <T> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public interface Page<T> extends Serializable {


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
     * 总行数
     *
     * @return total row
     */
    long getTotalRow();

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
     * 分页结果集
     *
     * @return result result
     */
    Map<Integer, List<T>> getResult();

    /**
     * 返回总页数
     *
     * @return total page
     */
    default long getTotalPage() {
        if (getPageSize() == 0) {
            return 0L;
        }
        if (getTotalRow()<0){
            return -1L;
        }
        long pages = getTotalRow() / getPageSize();
        if (getTotalRow() % getPageSize() != 0) {
            pages++;
        }
        return pages;
    }

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
