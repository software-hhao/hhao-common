/*
 * Copyright (c) 2024 wangsheng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.ddd.dto.request;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Dynamic params page query.
 *
 * @author wang sheng
 * @since 2024 /6/7 上午11:59
 */
public class DynamicParamsPageQuery extends PageQuery{
    private List<String> params=new ArrayList<>();

    /**
     * Gets params.
     *
     * @return the params
     */
    public List<String> getParams() {
        return params;
    }

    /**
     * Sets params.
     *
     * @param params the params
     */
    public void setParams(List<String> params) {
        this.params = params;
    }

    /**
     * Add params.
     *
     * @param param the param
     */
    public void addParams(String param){
        this.params.add(param);
    }

    /**
     * Clean params.
     */
    public void cleanParams(){
        this.params.clear();
    }

    /**
     * Is contain param boolean.
     *
     * @param paramName the param name
     * @return the boolean
     */
    public boolean isContainParam(String paramName){
        if (paramName==null){
            return false;
        }
        for(String param:this.params){
            if (param.equalsIgnoreCase(paramName)){
                return true;
            }
        }
        return false;
    }
}
