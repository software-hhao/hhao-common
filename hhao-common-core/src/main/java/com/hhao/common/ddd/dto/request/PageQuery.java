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

package com.hhao.common.ddd.dto.request;

import com.hhao.common.page.Page;

/**
 * 分页查询条件查询对象
 *
 * @author Wang
 * @since 2022/2/22 21:54
 */
public class PageQuery extends Query implements Page {
    private static final long serialVersionUID = 1371078197560489285L;
    // 请求页数
    private long pageNum;
    // 页的大小
    private long pageSize;
    // 前置缓存页数
    private long preCachedPage;
    // 后置缓存页数
    private long postCachedPage;
    // 是否包含总记录数
    private boolean includeTotalRows=true;
    // 排序方向
    private OrderDirection orderDirection=OrderDirection.ASC;
    // 排序列
    private String [] orderColumns;

    @Override
    public long getPageNum() {
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public long getPreCachedPage() {
        return preCachedPage;
    }

    public void setPreCachedPage(long preCachedPage) {
        this.preCachedPage = preCachedPage;
    }

    @Override
    public long getPostCachedPage() {
        return postCachedPage;
    }

    public void setPostCachedPage(long postCachedPage) {
        this.postCachedPage = postCachedPage;
    }

    @Override
    public boolean isIncludeTotalRows() {
        return includeTotalRows;
    }

    public void setIncludeTotalRows(boolean includeTotalRows) {
        this.includeTotalRows = includeTotalRows;
    }

    @Override
    public OrderDirection getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(OrderDirection orderDirection) {
        this.orderDirection = orderDirection;
    }

    @Override
    public String[] getOrderColumns() {
        return orderColumns;
    }

    public void setOrderColumns(String[] orderColumns) {
        this.orderColumns = orderColumns;
    }

}
