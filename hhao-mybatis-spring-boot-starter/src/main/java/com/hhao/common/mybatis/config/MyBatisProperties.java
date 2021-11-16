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
    private String [] pageExecutors;
    private String [] sqlExecutors;
    private Integer preCachedPage=0;
    private Integer postCachedPage=0;

    /**
     * Get page executors string [ ].
     *
     * @return the string [ ]
     */
    public String[] getPageExecutors() {
        return pageExecutors;
    }

    /**
     * Sets page executors.
     *
     * @param pageExecutors the page executors
     */
    public void setPageExecutors(String[] pageExecutors) {
        this.pageExecutors = pageExecutors;
    }

    /**
     * Get sql executors string [ ].
     *
     * @return the string [ ]
     */
    public String[] getSqlExecutors() {
        return sqlExecutors;
    }

    /**
     * Sets sql executors.
     *
     * @param sqlExecutors the sql executors
     */
    public void setSqlExecutors(String[] sqlExecutors) {
        this.sqlExecutors = sqlExecutors;
    }

    /**
     * Gets pre cached page.
     *
     * @return the pre cached page
     */
    public Integer getPreCachedPage() {
        return preCachedPage;
    }

    /**
     * Sets pre cached page.
     *
     * @param preCachedPage the pre cached page
     */
    public void setPreCachedPage(Integer preCachedPage) {
        this.preCachedPage = preCachedPage;
    }

    /**
     * Gets post cached page.
     *
     * @return the post cached page
     */
    public Integer getPostCachedPage() {
        return postCachedPage;
    }

    /**
     * Sets post cached page.
     *
     * @param postCachedPage the post cached page
     */
    public void setPostCachedPage(Integer postCachedPage) {
        this.postCachedPage = postCachedPage;
    }
}
