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
import com.hhao.common.ddd.dto.Dto;

/**
 * 返回对象
 *
 * @author Wang
 * @since 2022 /2/22 21:43
 */
public class Response extends Dto {
    private static final long serialVersionUID = 3122749136407622428L;

    private int status;
    private String message;

    /**
     * Instantiates a new Response.
     */
    public Response(){
    }

    /**
     * Instantiates a new Response.
     *
     * @param status  the status
     * @param message the message
     */
    public Response(int status,String message){
        this.status=status;
        this.message=message;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Error response.
     *
     * @param status the status
     * @return the response
     */
    public static Response error(int status) {
        return error(status,CoreConstant.DEFAULT_EXCEPTION_MESSAGE);
    }

    /**
     * Error response.
     *
     * @param errMessage the err message
     * @return the response
     */
    public static Response error(String errMessage) {
        return error(CoreConstant.DEFAULT_EXCEPTION_STATUS,errMessage);
    }

    /**
     * Error response.
     *
     * @param status     the status
     * @param errMessage the err message
     * @return the response
     */
    public static Response error(int status, String errMessage) {
        Response response = new Response(status,errMessage);
        return response;
    }

    /**
     * Ok response.
     *
     * @param status the status
     * @return the response
     */
    public static Response ok(int status){
        Response response = new Response(status,CoreConstant.DEFAULT_SUCCEED_MESSAGE);
        return response;
    }

    /**
     * Ok response.
     *
     * @param status  the status
     * @param message the message
     * @return the response
     */
    public static Response ok(int status,String message){
        Response response = new Response(status,message);
        return response;
    }

    @Override
    public String toString() {
        return "Response [status=" + status + ", message=" + message + "]";
    }
}
