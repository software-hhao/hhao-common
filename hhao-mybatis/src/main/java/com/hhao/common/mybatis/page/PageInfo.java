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

package com.hhao.common.mybatis.page;

import com.hhao.common.ddd.dto.response.PageResponse;
import com.hhao.common.mybatis.page.executor.MultiQueriesDynamicPageExecutor;
import com.hhao.common.mybatis.page.executor.MultiQueriesStaticPageExecutor;
import com.hhao.common.mybatis.page.executor.PageExecutor;
import com.hhao.common.mybatis.page.executor.SingleQueryDynamicPageExecutor;
import com.hhao.common.page.Page;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.SqlColumn;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页信息
 *
 * @param <T> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public class PageInfo<T> implements Page {
    private PageExecutor pageExecutor;
    private Map<Integer, List<T>> data;

    private long pageNum = 1L;
    private long pageSize = PageMetaData.PAGE_SIZE;
    private long preCachedPage = PageMetaData.PRE_CACHED_PAGE;
    private long postCachedPage = PageMetaData.POST_CACHED_PAGE;
    private long totalRow=-1L;
    private boolean includeTotalRows=true;
    private OrderDirection orderDirection=OrderDirection.ASC;
    private String [] orderColumns;
    private String orderBySql;
    private String limitParamName=PageMetaData.LIMIT_PARAM_NAME;
    private String offsetParamName=PageMetaData.OFFSET_PARAM_NAME;

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
     * Gets order by sql.
     *
     * @return the order by sql
     */
    public String getOrderBySql() {
        return orderBySql;
    }

    /**
     * Sets order by sql.
     *
     * @param orderBySql the order by sql
     */
    public void setOrderBySql(String orderBySql) {
        this.orderBySql = orderBySql;
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


    /**
     * Instantiates a new Page info.
     */
    protected PageInfo(){

    }

    @Override
    public long getPageNum() {
        return this.pageNum;
    }

    @Override
    public long getPageSize() {
        return this.pageSize;
    }

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

    public Map<Integer, List<T>> getData() {
        return this.data;
    }


    /**
     * Gets limit param name.
     *
     * @return the limit param name
     */
    public String getLimitParamName() {
        return limitParamName;
    }

    /**
     * Sets limit param name.
     *
     * @param limitParamName the limit param name
     */
    public void setLimitParamName(String limitParamName) {
        this.limitParamName = limitParamName;
    }


    /**
     * Gets offset param name.
     *
     * @return the offset param name
     */
    public String getOffsetParamName() {
        return offsetParamName;
    }

    /**
     * Sets offset param name.
     *
     * @param offsetParamName the offset param name
     */
    public void setOffsetParamName(String offsetParamName) {
        this.offsetParamName = offsetParamName;
    }

    /**
     * Sets page num.
     *
     * @param pageNum the page num
     */
    public void setPageNum(long pageNum) {
        this.pageNum = pageNum > 0 ? pageNum : 0L;
        if (this.preCachedPage>=this.getPageNum()){
            this.preCachedPage=0;
        }
    }

    /**
     * Sets page size.
     *
     * @param pageSize the page size
     */
    public void setPageSize(long pageSize) {
        this.pageSize = pageSize > 0 ? pageSize : 1;
    }

    /**
     * Sets pre cached page.
     *
     * @param preCachedPage the pre cached page
     */
    public void setPreCachedPage(long preCachedPage) {
        this.preCachedPage = preCachedPage > 0 ? preCachedPage : 0;
        //如果向前缓存页大于等于当前页，则缓存页置0
        if (this.preCachedPage>=this.getPageNum()){
            this.preCachedPage=0;
        }
    }

    /**
     * Sets post cached page.
     *
     * @param postCachedPage the post cached page
     */
    public void setPostCachedPage(long postCachedPage) {
        this.postCachedPage = postCachedPage > 0 ? postCachedPage : 0;
    }

    /**
     * Gets page executor.
     *
     * @return the page executor
     */
    public PageExecutor getPageExecutor() {
        return pageExecutor;
    }

    /**
     * Sets page executor.
     *
     * @param pageExecutor the page executor
     */
    public void setPageExecutor(PageExecutor pageExecutor) {
        this.pageExecutor = pageExecutor;
    }

    /**
     * Sets total row.
     *
     * @param totalRow the total row
     */
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

    /**
     * 设置查询结果,将结果集转换成 {@code Map<Integer, List<T>>} 形式
     *
     * @param data the result
     */
    public void setData(List<T> data) {
        this.data = new HashMap<>();
        long beginPage = getPageNum() - getPreCachedPage();
        long endPage = getPageNum() + getPostCachedPage();
        //从第一页开始
        if (beginPage <= 0) {
            beginPage = 1;
        }

        int pageCount = 0;
        boolean isFinish = false;

        for (long i = beginPage; i <= endPage && !isFinish; ++i, ++pageCount) {
            List<T> pageValues = new ArrayList<>();
            for (long j = 0; j < getPageSize(); ++j) {
                if ((pageCount * getPageSize() + j) < data.size()) {
                    pageValues.add(data.get((int) (pageCount * getPageSize() + j)));
                } else {
                    isFinish = true;
                    break;
                }
            }
            if (pageValues.size() > 0) {
                this.data.put(Integer.valueOf((int) i), pageValues);
            }
        }
    }

    /**
     * Of page result.
     *
     * @return the page result
     */
    public PageResponse<T> of(){
        return PageResponse.ok(data,pageNum,pageSize,preCachedPage,postCachedPage,totalRow,includeTotalRows,orderDirection,orderColumns);
    }

    /**
     * The type Order table.
     */
    public static class OrderTable{
        private BasicColumn[] columns;
        private String tableAlias;
        private String tableName;


        /**
         * Instantiates a new Order table.
         *
         * @param tableName the table name
         * @param columns   the columns
         */
        public OrderTable(String tableName,BasicColumn[] columns){
            this.tableName=tableName;
            this.columns=columns;
        }


        /**
         * Instantiates a new Order table.
         *
         * @param tableName  the table name
         * @param columns    the columns
         * @param tableAlias the table alias
         */
        public OrderTable(String tableName,BasicColumn[] columns,String tableAlias){
            this.tableName=tableName;
            this.columns=columns;
            this.tableAlias=tableAlias;
        }

        /**
         * Gets table name.
         *
         * @return the table name
         */
        public String getTableName() {
            return tableName;
        }

        /**
         * Sets table name.
         *
         * @param tableName the table name
         */
        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        /**
         * Get columns basic column [ ].
         *
         * @return the basic column [ ]
         */
        public BasicColumn[] getColumns() {
            return columns;
        }

        /**
         * Sets columns.
         *
         * @param columns the columns
         */
        public void setColumns(BasicColumn[] columns) {
            this.columns = columns;
        }

        /**
         * Gets table alias.
         *
         * @return the table alias
         */
        public String getTableAlias() {
            return tableAlias;
        }

        /**
         * Sets table alias.
         *
         * @param tableAlias the table alias
         */
        public void setTableAlias(String tableAlias) {
            this.tableAlias = tableAlias;
        }
    }


    /**
     * The type Builder.
     */
    public static class Builder{
        /**
         * The Order tables.
         */
        protected List<OrderTable> orderTables=new ArrayList<>();
        /**
         * The Page info.
         */
        protected PageInfo pageInfo;

        /**
         * Instantiates a new Builder.
         *
         * @param pageNum the page num
         */
        public Builder(long pageNum){
            this(PageInfo.class,pageNum);
        }

        /**
         * Instantiates a new Builder.
         */
        public <M extends PageInfo> Builder(Class<M> tClass,long pageNum){
            try {
                pageInfo=tClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if (pageInfo==null){
                throw new RuntimeException("PageInfo new instance wrong");
            }
            pageInfo.setPageNum(pageNum);
        }

        /**
         * Set page num builder.
         *
         * @param pageNum the page num
         * @return the builder
         */
        public Builder setPageNum(long pageNum){
            pageInfo.setPageNum(pageNum);
            return this;
        }

        /**
         * Set page size builder.
         *
         * @param pageSize the page size
         * @return the builder
         */
        public Builder setPageSize(long pageSize){
            if (pageSize>PageMetaData.PAGE_SIZE_LIMIT){
                pageInfo.setPageSize(PageMetaData.PAGE_SIZE_LIMIT);
            }else {
                pageInfo.setPageSize(pageSize);
            }
            return this;
        }

        /**
         * Set pre cached page builder.
         *
         * @param preCachedPage the pre cached page
         * @return the builder
         */
        public Builder setPreCachedPage(long preCachedPage){
            pageInfo.setPreCachedPage(preCachedPage);
            return this;
        }

        /**
         * Set post cached page builder.
         *
         * @param postCachedPage the post cached page
         * @return the builder
         */
        public Builder setPostCachedPage(long postCachedPage){
            pageInfo.setPostCachedPage(postCachedPage);
            return this;
        }

        /**
         * Sets include total rows.
         *
         * @param includeTotalRows the include total rows
         * @return the include total rows
         */
        public Builder setIncludeTotalRows(boolean includeTotalRows) {
            pageInfo.setIncludeTotalRows(includeTotalRows);
            return this;
        }

        /**
         * Sets page executor.
         *
         * @param pageExecutor the page executor
         * @return the page executor
         */
        public Builder setPageExecutor(PageExecutor pageExecutor) {
            pageInfo.setPageExecutor(pageExecutor);
            return this;
        }

        /**
         * Sets order direction.
         *
         * @param orderDirection the order direction
         * @return the order direction
         */
        public Builder setOrderDirection(OrderDirection orderDirection) {
            pageInfo.setOrderDirection(orderDirection);
            return this;
        }

        /**
         * Add order table builder.
         *
         * @param orderTable the order table
         * @return the builder
         */
        public Builder addOrderTable(OrderTable orderTable) {
            this.orderTables.add(orderTable);
            return this;
        }

        /**
         * Sets order columns.
         *
         * @param orderColumns the order columns
         * @return the order columns
         */
        public Builder setOrderColumns(String [] orderColumns) {
            pageInfo.setOrderColumns(orderColumns);
            return this;
        }


        /**
         * Sets limit param name.
         *
         * @param limitParamName the limit param name
         * @return the limit param name
         */
        public Builder setLimitParamName(String limitParamName) {
            pageInfo.setLimitParamName(limitParamName);
            return this;
        }

        /**
         * Sets offset param name.
         *
         * @param offsetParamName the offset param name
         * @return the offset param name
         */
        public Builder setOffsetParamName(String offsetParamName) {
            pageInfo.setOffsetParamName(offsetParamName);
            return this;
        }

        /**
         * With multi queries dynamic page executor builder.
         *
         * @return the builder
         */
        public Builder withMultiQueriesDynamicPageExecutor(){
            pageInfo.setPageExecutor(new MultiQueriesDynamicPageExecutor());
            return this;
        }


        /**
         * With multi queries static page executor builder.
         *
         * @return the builder
         */
        public Builder withMultiQueriesStaticPageExecutor(){
            pageInfo.setPageExecutor(new MultiQueriesStaticPageExecutor());
            return this;
        }

        /**
         * With single query dynamic page executor builder.
         *
         * @return the builder
         */
        public Builder withSingleQueryDynamicPageExecutor(){
            pageInfo.setPageExecutor(new SingleQueryDynamicPageExecutor());
            return this;
        }

        /**
         * Build page info.
         *
         * @return the page info
         */
        public PageInfo build(){
            //动态order sql处理
            if (pageInfo.getOrderColumns()!=null && this.orderTables!=null && !pageInfo.getOrderDirection().equals(OrderDirection.NO)){
                StringBuffer orderSql=new StringBuffer();
                boolean isSet=false;
                for(String orderColumn: pageInfo.getOrderColumns()){
                    isSet=false;
                    //表名.列名分解
                    String [] colInfos=orderColumn.split("\\.");
                    if (colInfos.length!=2){
                        throw new RuntimeException("The order column format wrong; format is table.column");
                    }
                    for(OrderTable table:orderTables){
                        //判断表名是否一致
                        if (!table.getTableName().equalsIgnoreCase(colInfos[0])){
                            continue;
                        }
                        for(BasicColumn basicColumnColumn:table.getColumns()){
                            SqlColumn sqlColumn=(SqlColumn)basicColumnColumn;
                            //判断列名是否一致
                            if (sqlColumn.name().equalsIgnoreCase(colInfos[1])){
                                if (orderSql.length()>0){
                                    orderSql.append(",");
                                }
                                if (table.getTableAlias()!=null && !table.getTableAlias().isBlank()){
                                    orderSql.append(table.getTableAlias() + "." +  sqlColumn.orderByName());
                                }else {
                                    orderSql.append(sqlColumn.orderByName());
                                }
                                isSet=true;
                                break;
                            }
                        }
                        if (isSet){
                            break;
                        }
                    }
                    if (!isSet){
                        throw new RuntimeException("error order by column:" + orderColumn);
                    }
                }
                //生成order sql
                if (orderSql.length()>0){
                    pageInfo.setOrderBySql(" order by " + orderSql.toString() + " " + pageInfo.getOrderDirection().name() + " ");
                }
            }

            return pageInfo;
        }
    }
}
