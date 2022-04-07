
/*
 * Copyright 2018-2022 WangSheng.
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

package com.hhao.common.dto;

import com.hhao.common.CoreConstant;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 分页结果返回对象
 *
 * @author Wang
 * @since 1.0.0
 */
public class PageResponse<T> extends Response implements Page{
    private static final long serialVersionUID = 1L;

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

    public long getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(long totalRow) {
        this.totalRow = totalRow;
    }

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

    public Map<Integer, List<T>> getData() {
        return data;
    }

    public void setData(Map<Integer, List<T>> data) {
        this.data = data;
    }

    public static <T> PageResponse<T> of(Map<Integer, List<T>> data,long pageNum,long pageSize,long preCachedPage,long postCachedPage,long totalRow,boolean includeTotalRows,OrderDirection orderDirection,String [] orderColumns) {
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

    public static PageResponse buildFailure(int status, String errMessage) {
        PageResponse response = new PageResponse();
        response.setStatus(status);
        response.setMessage(errMessage);
        return response;
    }
}
