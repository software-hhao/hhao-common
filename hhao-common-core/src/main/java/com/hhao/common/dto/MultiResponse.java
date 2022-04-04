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

package com.hhao.common.dto;

import com.hhao.common.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wang
 * @since 1.0.0
 */
public class MultiResponse extends Response{
    private Map<String,Object> dataMap=new HashMap<>(16);

    public MultiResponse putData(String key,Object value){
        dataMap.put(key,value);
        return this;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public static MultiResponse of(Map<String,Object> dataMap) {
        return of(dataMap,Constant.DEFAULT_SUCCEED_STATUS);
    }

    public static MultiResponse of(Map<String,Object> dataMap,int status) {
        MultiResponse multiResponse = new MultiResponse();
        multiResponse.setStatus(status);
        multiResponse.setMessage(Constant.DEFAULT_SUCCEED_MESSAGE);
        multiResponse.setDataMap(dataMap);
        return multiResponse;
    }

    public static MultiResponse buildFailure(int status, String errMessage,Throwable exception) {
        MultiResponse response = new MultiResponse();
        response.setStatus(status);
        response.setMessage(errMessage);
        response.putData("exception",exception);
        return response;
    }
}
