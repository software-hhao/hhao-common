/*
 * Copyright 2002-2019 the original author or authors.
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

package com.hhao.common.utils.path;

import java.util.Comparator;
import java.util.Map;


/**
 * The interface Path matcher.
 */
public interface PathMatcher {


    /**
     * Is pattern boolean.
     *
     * @param path the path
     * @return the boolean
     */
    boolean isPattern(String path);


    /**
     * Match boolean.
     *
     * @param pattern the pattern
     * @param path    the path
     * @return the boolean
     */
    boolean match(String pattern, String path);

    /**
     * Match start boolean.
     *
     * @param pattern the pattern
     * @param path    the path
     * @return the boolean
     */
    boolean matchStart(String pattern, String path);


    /**
     * Extract path within pattern string.
     *
     * @param pattern the pattern
     * @param path    the path
     * @return the string
     */
    String extractPathWithinPattern(String pattern, String path);


    /**
     * Extract uri template variables map.
     *
     * @param pattern the pattern
     * @param path    the path
     * @return the map
     */
    Map<String, String> extractUriTemplateVariables(String pattern, String path);

    /**
     * Gets pattern comparator.
     *
     * @param path the path
     * @return the pattern comparator
     */
    Comparator<String> getPatternComparator(String path);


    /**
     * Combine string.
     *
     * @param pattern1 the pattern 1
     * @param pattern2 the pattern 2
     * @return the string
     */
    String combine(String pattern1, String pattern2);
}
