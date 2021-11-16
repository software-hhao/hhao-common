
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

package com.hhao.common.metadata;

/**
 * The interface Metadata.
 *
 * @param <T> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public interface Metadata<T> {
    /**
     * 根据属性名称，判断是否是要取值的MetaData
     *
     * @param name the name
     * @return boolean boolean
     */
    boolean support(String name);

    /**
     * 元数据对应的属性名称
     *
     * @return name name
     */
    String getName();

    /**
     * 元数据对应的属性值
     *
     * @return value
     */
    String getValue();

    /**
     * 获取MetaData
     *
     * @param <R> the type parameter
     * @return metadata
     */
    <R extends T> R getMetadata();


    /**
     * 更新MetaData
     *
     * @param value the value
     * @return the t
     */
    T update(String value);

}
