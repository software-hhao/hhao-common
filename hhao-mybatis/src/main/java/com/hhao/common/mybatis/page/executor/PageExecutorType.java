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

package com.hhao.common.mybatis.page.executor;

/**
 * 分页处理器类型
 *
 * @author Wang
 * @since 1.0.0
 */
public enum PageExecutorType {
    /**
     * 自动处理select
     * 不包含count
     */
    DYNAMIC_EXCLUDE_COUNT,
    /**
     * 自动处理select、count
     * 包含count
     */
    DYNAMIC_INCLUDE_COUNT,
    /**
     * 只对结果集处理
     * 结果集包含count
     */
    STATIC_INCLUDE_COUNT,
    /**
     * 只对结果集处理
     * 结果集不包含count
     */
    STATIC_EXCLUDE_COUNT
}
