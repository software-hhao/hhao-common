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
import com.hhao.common.dto.Response;
import com.hhao.common.dto.SingleResponse;
import com.hhao.common.jackson.JacksonUtilFactory;
import com.hhao.common.jackson.view.Views;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * The type Result wrapper.
 *
 * @param <T> the type parameter
 * @author Wan
 * @since 1.0.0
 */
public class ResultWrapper<T> extends SingleResponse<T> {
    private static final Logger logger = LoggerFactory.getLogger(ResultWrapper.class);
    private static final long serialVersionUID = -5563703180936197552L;

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
    public ResultWrapper(T data, int status, String message) {
        super(data,status,message);
    }

    @Override
    @JsonView(Views.Default.class)
    public String getMessage() {
        return super.getMessage();
    }


    @Override
    @JsonView(Views.Default.class)
    public int getStatus() {
        return super.getStatus();
    }

    @Override
    @JsonView(Views.Default.class)
    public T getData() {
        return super.getData();
    }

    /**
     * 某些情况下，json转换不能识别data，将其转换成map，则可以利用该方法返回对象
     *
     * @param <L>         the type parameter
     * @param targetClass the target class
     * @return l l
     */
    @SuppressWarnings("unchecked")
    public <L> L getData(Class<L> targetClass){
        if (this.getData()==null || targetClass==null){
            return null;
        }
        if (!targetClass.isAssignableFrom(Map.class) && this.getData() instanceof Map){
            return JacksonUtilFactory.getJsonUtil().map2Pojo((Map<String,Object>)this.getData(),targetClass);
        }
        if (targetClass.isAssignableFrom(this.getData().getClass())){
            return (L)this.getData();
        }
        return null;
    }

    @Override
    public String toString(){
        if (this!=null){
            return JacksonUtilFactory.getJsonUtil().obj2String(this);
        }
        return "";
    }
}
