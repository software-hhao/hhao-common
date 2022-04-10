/*
 * Copyright 2020-2021 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hhao.common.extension.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 扩展点组合且有返回值的情况下，只支持MultiValues返回值类型
 *
 * @param <R> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public class MultiValues<R> {
    private R returnValue;
    private List<R> combinationResult=new ArrayList<>(16);

    /**
     * Instantiates a new Multi values.
     */
    public MultiValues(){

    }

    /**
     * Instantiates a new Multi values.
     *
     * @param size the size
     */
    public MultiValues(int size){
        if (size>16) {
            combinationResult = new ArrayList<>(size);
        }
    }

    /**
     * Instantiates a new Multi values.
     *
     * @param returnValue the return value
     */
    public MultiValues(R returnValue){
        this.returnValue=returnValue;
    }

    /**
     * Gets return value.
     *
     * @return the return value
     */
    public R getReturnValue() {
        return returnValue;
    }

    /**
     * Sets return value.
     *
     * @param returnValue the return value
     */
    public void setReturnValue(R returnValue) {
        this.returnValue = returnValue;
    }

    /**
     * Gets combination result.
     *
     * @return the combination result
     */
    public List<R> getCombinationResult() {
        return combinationResult;
    }

    /**
     * Sets combination result.
     *
     * @param combinationResult the combination result
     */
    public void setCombinationResult(List<R> combinationResult) {
        this.combinationResult = combinationResult;
    }

    /**
     * Merger return value.
     *
     * @param multiValues the multi values
     */
    public void mergerReturnValue(MultiValues multiValues){
        if (multiValues==null){
            return;
        }
        combinationResult.add((R)multiValues.getReturnValue());
    }

    /**
     * Of multi values.
     *
     * @param returnValue the return value
     * @return the multi values
     */
    public static MultiValues of(Object returnValue){
        return new MultiValues(returnValue);
    }
}
