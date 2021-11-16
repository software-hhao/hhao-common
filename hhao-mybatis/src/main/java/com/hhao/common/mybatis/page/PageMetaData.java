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
    public static String PAGE_INFO_PARAM_NAME="PAGE_INFO";
    /**
     * 默认的limit参数的名称
     */
    public static String LIMIT_PARAM_NAME="LIMIT_PARAM";
    /**
     * 默认的offset参数的名称
     */
    public static String OFFSET_PARAM_NAME="OFFSET_PARAM";

    /**
     * 默认情况下向前缓冲的页数
     */
    public static Integer PRE_CACHED_PAGE=0;
    /**
     * 默认情况下向后缓冲的页数
     */
    public static Integer POST_CACHED_PAGE=0;
    /**
     * 默认情况页面大小
     */
    public static Integer PAGE_SIZE=10;
}
