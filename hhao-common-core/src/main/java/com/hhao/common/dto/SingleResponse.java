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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Wang
 * @since 1.0.0
 */
public class SingleResponse<T> extends Response{
    private static final long serialVersionUID=245745747426768L;
    private static final Logger logger = LoggerFactory.getLogger(SingleResponse.class);

    private T data;

    public SingleResponse() {

    }

    public SingleResponse(T data, int status, String message) {
        this.data = data;
        this.setMessage(message);
        this.setStatus(status);
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public static <T> SingleResponse<T> of(T data) {
        return of(data,Constant.DEFAULT_SUCCEED_STATUS);
    }

    public static <T> SingleResponse<T> of(T data,int status) {
        SingleResponse<T> singleResponse = new SingleResponse<>();
        singleResponse.setStatus(status);
        singleResponse.setMessage(Constant.DEFAULT_SUCCEED_MESSAGE);
        singleResponse.setData(data);
        return singleResponse;
    }

    public static SingleResponse buildFailure(int status, String errMessage,Throwable exception) {
        SingleResponse response = new SingleResponse();
        response.setStatus(status);
        response.setMessage(errMessage);
        response.setData(exception);
        return response;
    }

    public static SingleResponse buildSuccess(){
        return buildSuccess(Constant.DEFAULT_SUCCEED_STATUS);
    }

    public static SingleResponse buildSuccess(int status){
        SingleResponse response = new SingleResponse();
        response.setStatus(status);
        response.setMessage(Constant.DEFAULT_SUCCEED_MESSAGE);
        return response;
    }
}
