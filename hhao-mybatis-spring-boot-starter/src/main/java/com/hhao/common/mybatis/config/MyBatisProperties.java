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
    // 向前缓存页数
    private Long preCachedPage=0L;
    // 向后缓存页数
    private Long postCachedPage=0L;
    // 页面大小限制
    private Long pageSizeLimit=20L;
    // 页面溢出是否跳转到最后一页
    private Boolean pageOverflowToLast=true;
    // 是否支持多语句
    private Boolean supportMultiQueries=false;
    // 是否支持默认万用的方言
    private Boolean supportDefaultDialect=true;
    // 默认的count查询语句id后缀
    private String defaultCountMappedStatementIdSuffix="-count";
    // 支持的数据库方言
    private String [] sqlDialects;


    /**
     * Gets support default dialect.
     *
     * @return the support default dialect
     */
    public Boolean getSupportDefaultDialect() {
        return supportDefaultDialect;
    }

    /**
     * Sets support default dialect.
     *
     * @param supportDefaultDialect the support default dialect
     */
    public void setSupportDefaultDialect(Boolean supportDefaultDialect) {
        this.supportDefaultDialect = supportDefaultDialect;
    }

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
     * Gets default count mapped statement id suffix.
     *
     * @return the default count mapped statement id suffix
     */
    public String getDefaultCountMappedStatementIdSuffix() {
        return defaultCountMappedStatementIdSuffix;
    }

    /**
     * Sets default count mapped statement id suffix.
     *
     * @param defaultCountMappedStatementIdSuffix the default count mapped statement id suffix
     */
    public void setDefaultCountMappedStatementIdSuffix(String defaultCountMappedStatementIdSuffix) {
        this.defaultCountMappedStatementIdSuffix = defaultCountMappedStatementIdSuffix;
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
