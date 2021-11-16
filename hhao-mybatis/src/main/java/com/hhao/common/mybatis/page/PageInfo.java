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

import com.hhao.common.mybatis.page.executor.PageExecutor;
import com.hhao.common.mybatis.page.executor.PageExecutorType;
import com.hhao.common.mybatis.page.executor.sql.SqlExecutor;

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
public class PageInfo<T> implements Page<T> {
    private long pageNum = 1;
    private long pageSize = PageMetaData.PAGE_SIZE;
    private long preCachedPage = PageMetaData.PRE_CACHED_PAGE;
    private long postCachedPage = PageMetaData.POST_CACHED_PAGE;
    private long totalRow;

    private Map<Integer, List<T>> result;
    private PageExecutorType pageExecutorType = PageExecutorType.DYNAMIC_INCLUDE_COUNT;
    private SqlExecutor sqlExecutor;

    /**
     * Instantiates a new Page info.
     *
     * @param pageNum  the page num
     * @param pageSize the page size
     */
    public PageInfo(long pageNum,long pageSize){
        this(pageNum,pageSize, PageExecutorType.DYNAMIC_INCLUDE_COUNT);
    }

    /**
     * Instantiates a new Page info.
     *
     * @param pageNum          the page num
     * @param pageSize         the page size
     * @param pageExecutorType the page executor type
     */
    public PageInfo(long pageNum,long pageSize,PageExecutorType pageExecutorType){
        this.pageNum=pageNum;
        this.pageSize=pageSize;
        this.pageExecutorType=pageExecutorType;
    }

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
     * Gets sql executor.
     *
     * @return the sql executor
     */
    public SqlExecutor getSqlExecutor() {
        return sqlExecutor;
    }

    /**
     * Sets sql executor.
     *
     * @param sqlExecutor the sql executor
     */
    public void setSqlExecutor(SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    /**
     * Gets page executor type.
     *
     * @return the page executor type
     */
    public PageExecutorType getPageExecutorType() {
        return pageExecutorType;
    }

    /**
     * Sets page executor type.
     *
     * @param pageExecutorType the page executor type
     */
    public void setPageExecutorType(PageExecutorType pageExecutorType) {
        this.pageExecutorType = pageExecutorType;
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
     * 设置查询结果,将结果集转换成 {@code Map<Integer, List<T>>} 形式
     *
     * @param result the result
     */
    public void setResult(List<T> result) {
        this.result = new HashMap<>();
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
                if ((pageCount * getPageSize() + j) < result.size()) {
                    pageValues.add(result.get((int) (pageCount * getPageSize() + j)));
                } else {
                    isFinish = true;
                    break;
                }
            }
            if (pageValues.size() > 0) {
                this.result.put(Integer.valueOf((int) i), pageValues);
            }
        }
    }

    /**
     * Of page result.
     *
     * @return the page result
     */
    public PageResult<T> of(){
        PageResult<T> pageResult=new PageResult<>();
        pageResult.setResult(this.getResult());
        pageResult.setPageNum(this.getPageNum());
        pageResult.setPageSize(this.getPageSize());
        pageResult.setTotalRow(this.getTotalRow());
        pageResult.setTotalPage(this.getTotalPage());
        pageResult.setPostCachedPage(this.getPostCachedPage());
        pageResult.setPreCachedPage(this.getPreCachedPage());
        return pageResult;
    }

    /**
     * The type Builder.
     */
    public static class Builder{
        private PageInfo pageInfo;

        /**
         * Instantiates a new Builder.
         *
         * @param pageNum          the page num
         * @param pageSize         the page size
         * @param pageExecutorType the page executor type
         */
        public Builder(long pageNum,long pageSize,PageExecutorType pageExecutorType){
            pageInfo=new PageInfo(pageNum,pageSize,pageExecutorType);
        }

        /**
         * Instantiates a new Builder.
         *
         * @param pageNum  the page num
         * @param pageSize the page size
         */
        public Builder(long pageNum,long pageSize){
            pageInfo=new PageInfo(pageNum,pageSize);
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
         * Sets page executor type.
         *
         * @param pageExecutorType the page executor type
         * @return the page executor type
         */
        public Builder setPageExecutorType(PageExecutorType pageExecutorType) {
            pageInfo.setPageExecutorType(pageExecutorType);
            return this;
        }

        /**
         * Sets sql executor.
         *
         * @param sqlExecutor the sql executor
         * @return the sql executor
         */
        public Builder setSqlExecutor(SqlExecutor sqlExecutor) {
            pageInfo.setSqlExecutor(sqlExecutor);
            return this;
        }

        /**
         * Build page info.
         *
         * @return the page info
         */
        public PageInfo build(){
            return pageInfo;
        }
    }
}
