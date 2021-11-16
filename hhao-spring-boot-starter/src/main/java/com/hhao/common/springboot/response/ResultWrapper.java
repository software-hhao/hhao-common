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

package com.hhao.common.springboot.response;

import com.fasterxml.jackson.annotation.JsonView;
import com.hhao.common.entity.BaseEntity;
import com.hhao.common.jackson.view.Views;
import com.hhao.common.utils.bean2map.Bean2MapUtils;

import java.util.Map;

/**
 * The type Result wrapper.
 *
 * @param <T> the type parameter
 * @author Wan
 * @since 1.0.0
 */
public class ResultWrapper<T> extends BaseEntity {
    private static final long serialVersionUID=245745747426768L;

    /**
     * status
     */
    @JsonView(Views.Default.class)
    private String status;

    /**
     * message
     */
    @JsonView(Views.Default.class)
    private String message;

    /**
     * data
     */
    @JsonView(Views.Default.class)
    private T data;

    /**
     * Instantiates a new Result wrapper.
     */
    public ResultWrapper() {

    }

    /**
     * Instantiates a new Result wrapper.
     *
     * @param data    the data
     * @param status  the status
     * @param message the message
     */
    public ResultWrapper(T data, String status, String message) {
        this.status = status;
        this.data = data;
        this.message=message;
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
     * @return the message
     */
    public ResultWrapper<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     * @return the status
     */
    public ResultWrapper<T> setStatus(String status) {
        this.status = status;
        return this;
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
     * Sets data.
     *
     * @param data the data
     * @return the data
     */
    public ResultWrapper<T> setData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 某些情况下，json转换不能识别data，将其转换成map，则可以利用该方法返回对象
     *
     * @param <L>         the type parameter
     * @param targetClass the target class
     * @return l
     */
    @SuppressWarnings("unchecked")
    public <L> L getData(Class<L> targetClass){
        if (this.getData()==null || targetClass==null){
            return null;
        }
        if (this.getData() instanceof Map){
            return Bean2MapUtils.map2Bean(targetClass,(Map<String,Object>)this.getData());
        }
        if (targetClass.isAssignableFrom(this.getData().getClass())){
            return (L)this.getData();
        }
        return null;
    }
}
