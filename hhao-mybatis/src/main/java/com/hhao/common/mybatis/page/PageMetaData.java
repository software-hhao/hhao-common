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

/**
 * 元数据
 *
 * @author Wang
 * @since 1.0.0
 */
public class PageMetaData {
    /**
     * 默认的PageInfo传递参数的名称
     */
    public static String PAGE_INFO_PARAM_NAME="pageInfo";
    /**
     * 默认的limit参数的名称
     */
    public static String LIMIT_PARAM_NAME="pageInfo.limit";
    /**
     * 默认的offset参数的名称
     */
    public static String OFFSET_PARAM_NAME="pageInfo.offset";

    /**
     * 默认情况下向前缓冲的页数
     */
    public static Long PRE_CACHED_PAGE=0L;
    /**
     * 默认情况下向后缓冲的页数
     */
    public static Long POST_CACHED_PAGE=0L;
    /**
     * 默认情况页面大小
     */
    public static Long PAGE_SIZE=10L;

    /**
     * The constant PAGE_SIZE_LIMIT.
     */
    public static Long PAGE_SIZE_LIMIT=20L;

    /**
     * 是否支持分页溢出跳转到最后一页
     * 需要正确指定PageInfo#setLimitParamName、PageInfo#setOffsetParamName
     */
    public static Boolean PAGE_OVERFLOW_TO_LAST=true;

    /**
     * The constant SUPPORT_MULTI_QUERIES.
     */
    public static Boolean SUPPORT_MULTI_QUERIES=false;
}
