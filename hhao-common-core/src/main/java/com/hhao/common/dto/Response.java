
/*
 * Copyright 2018-2022 WangSheng.
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

package com.hhao.common.dto;

import com.hhao.common.Constant;

/**
 * 返回对象
 *
 * @author Wang
 * @since 2022/2/22 21:43
 */
public class Response extends Dto {
    private static final long serialVersionUID = 1L;

    private int status;

    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Response buildFailure(int status, String errMessage) {
        Response response = new Response();
        response.setStatus(status);
        response.setMessage(errMessage);
        return response;
    }

    public static Response buildSuccess(){
        return buildSuccess(Constant.DEFAULT_SUCCEED_STATUS);
    }

    public static Response buildSuccess(int status){
        Response response = new Response();
        response.setStatus(status);
        response.setMessage(Constant.DEFAULT_SUCCEED_MESSAGE);
        return response;
    }

    @Override
    public String toString() {
        return "Response [status=" + status + ", message=" + message + "]";
    }
}
