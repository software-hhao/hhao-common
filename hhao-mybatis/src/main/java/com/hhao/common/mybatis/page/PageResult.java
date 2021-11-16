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

import java.util.List;
import java.util.Map;

/**
 * 分页结果
 *
 * @param <T> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public class PageResult<T> implements Page<T>{
    private long pageNum;
    private long pageSize;
    private long preCachedPage;
    private long postCachedPage;
    private long totalRow;
    private long totalPage;

    private Map<Integer, List<T>> result;

    @Override
    public long getPageNum() {
        return this.pageNum;
    }

    @Override
    public long getPageSize() {
        return this.pageSize;
    }

    @Override
    public long getTotalRow() {
        return this.totalRow;
    }

    @Override
    public long getPreCachedPage() {
        return this.preCachedPage;
    }

    @Override
    public long getPostCachedPage() {
        return this.postCachedPage;
    }

    @Override
    public Map<Integer, List<T>> getResult() {
        return this.result;
    }

    /**
     * Sets page num.
     *
     * @param pageNum the page num
     */
    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * Sets page size.
     *
     * @param pageSize the page size
     */
    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Sets pre cached page.
     *
     * @param preCachedPage the pre cached page
     */
    public void setPreCachedPage(long preCachedPage) {
        this.preCachedPage = preCachedPage;
    }

    /**
     * Sets post cached page.
     *
     * @param postCachedPage the post cached page
     */
    public void setPostCachedPage(long postCachedPage) {
        this.postCachedPage = postCachedPage;
    }

    /**
     * Sets total row.
     *
     * @param totalRow the total row
     */
    public void setTotalRow(long totalRow) {
        this.totalRow = totalRow;
    }

    /**
     * Sets result.
     *
     * @param result the result
     */
    public void setResult(Map<Integer, List<T>> result) {
        this.result = result;
    }

    @Override
    public long getTotalPage() {
        return totalPage;
    }

    /**
     * Sets total page.
     *
     * @param totalPage the total page
     */
    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }
}
