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
import com.hhao.common.mybatis.page.executor.SingleQueryStaticPageExecutor;

/**
 * The type Page info with count.
 *
 * @author Wang
 * @since 2021 /11/18 11:13
 */
public class PageInfoWithCount extends PageInfo{
    private String countMappedStatementId;

    /**
     * Gets count mapped statement id.
     *
     * @return the count mapped statement id
     */
    public String getCountMappedStatementId() {
        return countMappedStatementId;
    }

    /**
     * Sets count mapped statement id.
     *
     * @param countMappedStatementId the count mapped statement id
     */
    public void setCountMappedStatementId(String countMappedStatementId) {
        this.countMappedStatementId = countMappedStatementId;
    }

    /**
     * The type Builder.
     */
    public static class Builder extends PageInfo.Builder{
        /**
         * Instantiates a new Builder.
         *
         * @param pageNum                the page num
         * @param countMappedStatementId the count mapped statement id
         */
        public Builder(long pageNum,String countMappedStatementId) {
            super(PageInfoWithCount.class,pageNum);
            ((PageInfoWithCount)pageInfo).setCountMappedStatementId(countMappedStatementId);
        }

        /**
         * With single query static page executor builder.
         *
         * @return the builder
         */
        public Builder withSingleQueryStaticPageExecutor(){
            pageInfo.setPageExecutor(new SingleQueryStaticPageExecutor());
            return this;
        }

        @Override
        public PageInfo build() {
            return super.build();
        }
    }
}
