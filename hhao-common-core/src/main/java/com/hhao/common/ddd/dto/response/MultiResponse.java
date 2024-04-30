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
package com.hhao.common.ddd.dto.response;

import com.hhao.common.CoreConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Multi response.
 *
 * @author Wang
 * @since 1.0.0
 */
public class MultiResponse extends Response {
    private static final long serialVersionUID=245745747426700L;

    //存放返回的数据
    private Map<String,Object> dataMap=new HashMap<>(16);

    /**
     * Instantiates a new Multi response.
     */
    public MultiResponse() {

    }

    /**
     * Instantiates a new Multi response.
     *
     * @param dataMap the data map
     * @param status  the status
     * @param message the message
     */
    public MultiResponse(Map<String,Object> dataMap, int status, String message) {
        super(status,message);
        this.dataMap = dataMap;
    }

    /**
     * Put data multi response.
     *
     * @param key   the key
     * @param value the value
     * @return the multi response
     */
    public MultiResponse putData(String key,Object value){
        dataMap.put(key,value);
        return this;
    }

    /**
     * Gets data map.
     *
     * @return the data map
     */
    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    /**
     * Sets data map.
     *
     * @param dataMap the data map
     */
    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    /**
     * Ok multi response.
     *
     * @param dataMap the data map
     * @return the multi response
     */
    public static MultiResponse ok(Map<String,Object> dataMap) {
        MultiResponse multiResponse=new MultiResponse(dataMap,CoreConstant.DEFAULT_SUCCEED_STATUS,CoreConstant.DEFAULT_SUCCEED_MESSAGE);
        return multiResponse;
    }

    /**
     * Ok multi response.
     *
     * @param dataMap the data map
     * @param status  the status
     * @param message the message
     * @return the multi response
     */
    public static MultiResponse ok(Map<String,Object> dataMap,int status, String message) {
        MultiResponse multiResponse=new MultiResponse(dataMap,status,message);
        return multiResponse;
    }

    /**
     * Error multi response.
     *
     * @param exception the exception
     * @return the multi response
     */
    public static MultiResponse error(Throwable exception) {
        return error(exception,CoreConstant.DEFAULT_EXCEPTION_STATUS,exception.getMessage());
    }

    /**
     * Error multi response.
     *
     * @param exception  the exception
     * @param status     the status
     * @param errMessage the err message
     * @return the multi response
     */
    public static MultiResponse error(Throwable exception,int status, String errMessage) {
        MultiResponse response = new MultiResponse();
        response.setStatus(status);
        response.setMessage(errMessage);
        response.putData("exception",exception);
        return response;
    }
}
