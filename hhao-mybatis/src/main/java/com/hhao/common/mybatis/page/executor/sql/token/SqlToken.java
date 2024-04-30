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

package com.hhao.common.mybatis.page.executor.sql.token;

/**
 * The type Sql token.
 *
 * @author Wang
 * @since 2021 /11/18 19:04
 */
public class SqlToken implements Token {
    /**
     * 值
     */
    private String value;
    /**
     * token序列位置
     */
    private int index=-1;
    /**
     * 所属的片段
     * 如:select列表,from子句,where子句,group by子句,having子句,order by子句,limit子句,offset子句,union子句
     */
    private Token fragmentToken;
    /**
     * 按清除规则,该token是否清除
     */
    private boolean clean;

    /**
     * Instantiates a new Sql token.
     *
     * @param value the value
     */
    public SqlToken(String value){
        this.value=value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public Token getFragmentToken() {
        return fragmentToken;
    }

    @Override
    public void setFragmentToken(Token fragmentToken) {
        this.fragmentToken = fragmentToken;
    }

    @Override
    public boolean isClean() {
        return clean;
    }

    @Override
    public void setClean(boolean clean) {
        this.clean = clean;
    }

    @Override
    public String toString(){
        return this.getValue() + "|index:" + this.getIndex() + "|isClean:" + this.isClean() + "|fragmentToken:" + (fragmentToken!=null?fragmentToken.getIndex():"-");
    }
}
