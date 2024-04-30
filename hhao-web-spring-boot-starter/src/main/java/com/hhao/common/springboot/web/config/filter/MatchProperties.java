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

package com.hhao.common.springboot.web.config.filter;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * The type Match properties.
 *
 * @author Wang
 * @since 1.0.0
 */
public class MatchProperties {
    private final String SEPARATOR = ",";
    private String include;
    private String exclude;
    private String[] includes;
    private String[] excludes;

    private AntPathMatcher pathMatch = new AntPathMatcher(File.pathSeparator);

    /**
     * Gets include.
     *
     * @return the include
     */
    public String getInclude() {
        return include;
    }

    /**
     * Sets include.
     *
     * @param include the include
     */
    public void setInclude(String include) {
        this.include = include;
        if (StringUtils.hasLength(this.include)) {
            this.includes = this.include.split(SEPARATOR);
        }
    }

    /**
     * Gets exclude.
     *
     * @return the exclude
     */
    public String getExclude() {
        return exclude;
    }

    /**
     * Sets exclude.
     *
     * @param exclude the exclude
     */
    public void setExclude(String exclude) {
        this.exclude = exclude;
        if (StringUtils.hasLength(this.exclude)) {
            this.excludes = this.exclude.split(SEPARATOR);
        }
    }

    /**
     * Match boolean.
     *
     * @param path the path
     * @return the boolean
     */
    public boolean match(String path) {
        if (this.includes != null) {
            for (String s : includes) {
                if (pathMatch.match(s, path)) {
                    return true;
                }
            }
        }
        if (this.exclude != null) {
            for (String s : excludes) {
                if (pathMatch.match(s, path)) {
                    return false;
                }
            }
        }
        return true;
    }
}
