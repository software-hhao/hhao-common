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

    public Boolean getPageOverflowToLast() {
        return pageOverflowToLast;
    }

    public void setPageOverflowToLast(Boolean pageOverflowToLast) {
        this.pageOverflowToLast = pageOverflowToLast;
    }
}
