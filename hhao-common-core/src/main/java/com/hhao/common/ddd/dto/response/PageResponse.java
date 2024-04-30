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

package com.hhao.common.ddd.dto.response;

import com.hhao.common.CoreConstant;
import com.hhao.common.page.Page;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 分页结果返回对象
 *
 * @param <T> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public class PageResponse<T> extends Response implements Page {
    private static final long serialVersionUID = -9152107928582346177L;

    private long pageNum;
    private long pageSize;
    private long preCachedPage;
    private long postCachedPage;
    private long totalRow;
    private boolean includeTotalRows=true;
    private OrderDirection orderDirection=OrderDirection.ASC;
    private String [] orderColumns;

    private Map<Integer, List<T>> data= Collections.EMPTY_MAP;

    @Override
    public long getPageNum() {
        return pageNum;
    }

    /**
     * Sets page num.
     *
     * @param pageNum the page num
     */
    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public long getPageSize() {
        return pageSize;
    }

    /**
     * Sets page size.
     *
     * @param pageSize the page size
     */
    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public long getPreCachedPage() {
        return preCachedPage;
    }

    /**
     * Sets pre cached page.
     *
     * @param preCachedPage the pre cached page
     */
    public void setPreCachedPage(long preCachedPage) {
        this.preCachedPage = preCachedPage;
    }

    @Override
    public long getPostCachedPage() {
        return postCachedPage;
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
     * Gets total row.
     *
     * @return the total row
     */
    public long getTotalRow() {
        return totalRow;
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
     * Gets total page.
     *
     * @return the total page
     */
    public long getTotalPage() {
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

    @Override
    public boolean isIncludeTotalRows() {
        return includeTotalRows;
    }

    /**
     * Sets include total rows.
     *
     * @param includeTotalRows the include total rows
     */
    public void setIncludeTotalRows(boolean includeTotalRows) {
        this.includeTotalRows = includeTotalRows;
    }

    @Override
    public OrderDirection getOrderDirection() {
        return orderDirection;
    }

    /**
     * Sets order direction.
     *
     * @param orderDirection the order direction
     */
    public void setOrderDirection(OrderDirection orderDirection) {
        this.orderDirection = orderDirection;
    }

    @Override
    public String[] getOrderColumns() {
        return orderColumns;
    }

    /**
     * Sets order columns.
     *
     * @param orderColumns the order columns
     */
    public void setOrderColumns(String[] orderColumns) {
        this.orderColumns = orderColumns;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public Map<Integer, List<T>> getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(Map<Integer, List<T>> data) {
        this.data = data;
    }

    /**
     * Ok page response.
     *
     * @param <T>              the type parameter
     * @param data             the data
     * @param pageNum          the page num
     * @param pageSize         the page size
     * @param preCachedPage    the pre cached page
     * @param postCachedPage   the post cached page
     * @param totalRow         the total row
     * @param includeTotalRows the include total rows
     * @param orderDirection   the order direction
     * @param orderColumns     the order columns
     * @return the page response
     */
    public static <T> PageResponse<T> ok(Map<Integer, List<T>> data,long pageNum,long pageSize,long preCachedPage,long postCachedPage,long totalRow,boolean includeTotalRows,OrderDirection orderDirection,String [] orderColumns) {
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setData(data);
        pageResponse.setPageNum(pageNum);
        pageResponse.setPageSize(pageSize);
        pageResponse.setPreCachedPage(preCachedPage);
        pageResponse.setPostCachedPage(postCachedPage);
        pageResponse.setTotalRow(totalRow);
        pageResponse.setIncludeTotalRows(includeTotalRows);
        pageResponse.setOrderColumns(orderColumns);
        pageResponse.setOrderDirection(orderDirection);

        pageResponse.setStatus(CoreConstant.DEFAULT_SUCCEED_STATUS);
        pageResponse.setMessage(CoreConstant.DEFAULT_SUCCEED_MESSAGE);
        return pageResponse;
    }

    /**
     * Error page response.
     *
     * @param errMessage the err message
     * @return the page response
     */
    public static PageResponse error(String errMessage) {
        PageResponse response = new PageResponse();
        response.setStatus(CoreConstant.DEFAULT_EXCEPTION_STATUS);
        response.setMessage(errMessage);
        return response;
    }

    /**
     * Error page response.
     *
     * @param status     the status
     * @param errMessage the err message
     * @return the page response
     */
    public static PageResponse error(int status, String errMessage) {
        PageResponse response = new PageResponse();
        response.setStatus(status);
        response.setMessage(errMessage);
        return response;
    }
}
