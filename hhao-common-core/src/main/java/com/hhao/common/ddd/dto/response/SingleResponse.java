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

/**
 * The type Single response.
 *
 * @param <T> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public class SingleResponse<T> extends Response {
    private static final long serialVersionUID=245745747426768L;

    // 返回的数据
    private T data;

    /**
     * Instantiates a new Single response.
     */
    public SingleResponse() {

    }

    /**
     * Instantiates a new Single response.
     *
     * @param data    the data
     * @param status  the status
     * @param message the message
     */
    public SingleResponse(T data, int status, String message) {
        this.data = data;
        this.setMessage(message);
        this.setStatus(status);
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * Ok single response.
     *
     * @param <T>  the type parameter
     * @param data the data
     * @return the single response
     */
    public static <T> SingleResponse<T> ok(T data) {
        return ok(data, CoreConstant.DEFAULT_SUCCEED_STATUS,CoreConstant.DEFAULT_SUCCEED_MESSAGE);
    }

    /**
     * Ok single response.
     *
     * @param <T>    the type parameter
     * @param data   the data
     * @param status the status
     * @return the single response
     */
    public static <T> SingleResponse<T> ok(T data,int status) {
        return ok(data, status,CoreConstant.DEFAULT_SUCCEED_MESSAGE);
    }

    /**
     * Ok single response.
     *
     * @param <T>     the type parameter
     * @param data    the data
     * @param status  the status
     * @param message the message
     * @return the single response
     */
    public static <T> SingleResponse<T> ok(T data,int status,String message) {
        SingleResponse<T> singleResponse = new SingleResponse<>(data,status,message);
        return singleResponse;
    }

    /**
     * Error single response.
     *
     * @param exception the exception
     * @return the single response
     */
    public static SingleResponse error(Throwable exception) {
        return error(exception,CoreConstant.DEFAULT_EXCEPTION_STATUS,exception.getMessage());
    }

    /**
     * Error single response.
     *
     * @param exception the exception
     * @param status    the status
     * @return the single response
     */
    public static SingleResponse error(Throwable exception,int status) {
        return error(exception,status,exception.getMessage());
    }

    /**
     * Error single response.
     *
     * @param exception  the exception
     * @param status     the status
     * @param errMessage the err message
     * @return the single response
     */
    public static SingleResponse error(Throwable exception,int status, String errMessage) {
        SingleResponse singleResponse = new SingleResponse(exception,status,errMessage);
        return singleResponse;
    }
}
