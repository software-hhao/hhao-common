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
 * 参数占位符?token
 *
 * @author Wang
 * @since 2021 /11/19 13:10
 */
public class SqlParamToken extends SqlToken implements ParamToken{
    /**
     * 参数位置序列
     */
    private int paramIndex;

    /**
     * Instantiates a new Sql param token.
     *
     * @param value the value
     */
    public SqlParamToken(String value) {
        super(value);
    }

    @Override
    public int getParamIndex() {
        return paramIndex;
    }

    @Override
    public void setParamIndex(int paramIndex) {
        this.paramIndex = paramIndex;
    }

    @Override
    public String toString(){
        return super.toString() + "|paramIndex:" + this.paramIndex;
    }
}
