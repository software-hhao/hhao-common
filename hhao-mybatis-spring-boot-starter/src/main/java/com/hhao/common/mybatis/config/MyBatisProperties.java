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

package com.hhao.common.mybatis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type My batis properties.
 *
 * @author Wang
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "com.hhao.config.mybatis")
public class MyBatisProperties {
    private Long preCachedPage=0L;
    private Long postCachedPage=0L;
    private Long pageSizeLimit=20L;
    private Boolean pageOverflowToLast=true;
    private Boolean supportMultiQueries=false;
    private String [] sqlDialects;


    /**
     * jdbc是否支持多语句
     *
     * @return the support multi queries
     */
    public Boolean getSupportMultiQueries() {
        return supportMultiQueries;
    }

    /**
     * Sets support multi queries.
     *
     * @param supportMultiQueries the support multi queries
     */
    public void setSupportMultiQueries(Boolean supportMultiQueries) {
        this.supportMultiQueries = supportMultiQueries;
    }

    /**
     * Gets page size limit.
     *
     * @return the page size limit
     */
    public Long getPageSizeLimit() {
        return pageSizeLimit;
    }

    /**
     * Sets page size limit.
     *
     * @param pageSizeLimit the page size limit
     */
    public void setPageSizeLimit(Long pageSizeLimit) {
        this.pageSizeLimit = pageSizeLimit;
    }

    /**
     * Gets pre cached page.
     *
     * @return the pre cached page
     */
    public Long getPreCachedPage() {
        return preCachedPage;
    }

    /**
     * Sets pre cached page.
     *
     * @param preCachedPage the pre cached page
     */
    public void setPreCachedPage(Long preCachedPage) {
        this.preCachedPage = preCachedPage;
    }

    /**
     * Gets post cached page.
     *
     * @return the post cached page
     */
    public Long getPostCachedPage() {
        return postCachedPage;
    }

    /**
     * Sets post cached page.
     *
     * @param postCachedPage the post cached page
     */
    public void setPostCachedPage(Long postCachedPage) {
        this.postCachedPage = postCachedPage;
    }

    /**
     * Gets page overflow to last.
     *
     * @return the page overflow to last
     */
    public Boolean getPageOverflowToLast() {
        return pageOverflowToLast;
    }

    /**
     * Sets page overflow to last.
     *
     * @param pageOverflowToLast the page overflow to last
     */
    public void setPageOverflowToLast(Boolean pageOverflowToLast) {
        this.pageOverflowToLast = pageOverflowToLast;
    }

    /**
     * Get sql dialects string [ ].
     *
     * @return the string [ ]
     */
    public String[] getSqlDialects() {
        return sqlDialects;
    }

    /**
     * Sets sql dialects.
     *
     * @param sqlDialects the sql dialects
     */
    public void setSqlDialects(String[] sqlDialects) {
        this.sqlDialects = sqlDialects;
    }
}
