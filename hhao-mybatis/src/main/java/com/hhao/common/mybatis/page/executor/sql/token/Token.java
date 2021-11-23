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

package com.hhao.common.mybatis.page.executor.sql.token;

/**
 * The interface Token.
 *
 * @author Wang
 * @since 2021 /11/19 11:26
 */
public interface Token {
    /**
     * token值
     *
     * @return value
     */
    String getValue();

    /**
     * Sets value.
     *
     * @param value the value
     */
    void setValue(String value);

    /**
     * 序列号
     *
     * @return index
     */
    int getIndex();

    /**
     * Sets index.
     *
     * @param Index the index
     */
    void setIndex(int Index);

    /**
     * 所属的片段token
     *
     * @return fragment token
     */
    Token getFragmentToken();

    /**
     * Sets fragment token.
     *
     * @param fragmentToken the fragment token
     */
    void setFragmentToken(Token fragmentToken);

    /**
     * 状态
     * 是否标记为清除
     *
     * @return the boolean
     */
    boolean isClean();

    /**
     * Sets clean.
     *
     * @param clean the clean
     */
    void setClean(boolean clean);
}
