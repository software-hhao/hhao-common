
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

package com.hhao.common.springboot.exception;

import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.springboot.exception.support.ErrorAttributeConstant;
import com.hhao.common.springboot.response.ResultWrapper;
import com.hhao.common.utils.StringUtils;

import java.util.HashMap;

/**
 * ResultWrapper包含一个异常时,用该异常接收，则返回客户端时，直接返回ResultWrapper里包装的异常
 * 而不是将ResultWrapper整个嵌套到当前异常中
 *
 * @author Wang
 * @since 2022 /1/8 14:35
 */
public class ResultWrapperException extends RuntimeException{
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(ResultWrapperException.class);
    private HashMap<String,Object> errorAttributes = new HashMap<>();

    /**
     * Instantiates a new Result wrapper exception.
     *
     * @param result the result
     */
    public ResultWrapperException(ResultWrapper result){
        this(result,null);
    }

    public ResultWrapperException(ResultWrapper result,String path){
        setErrorAttributes(result);
        addPath(path);
    }

    @Override
    public String getMessage() {
        return this.getErrorMessage();
    }

    /**
     * Gets error attributes.
     *
     * @return the error attributes
     */
    public HashMap<String, Object> getErrorAttributes() {
        return errorAttributes;
    }

    /**
     * Set error attributes.
     *
     * @param errorResult the error result
     */
    public void setErrorAttributes(ResultWrapper errorResult){
        errorAttributes =(HashMap<String,Object>)errorResult.getData(HashMap.class);
    }

    private String getErrorMessage(){
        String message=(String) errorAttributes.get(ErrorAttributeConstant.MESSAGE);
        return message==null?"":message;
    }

    private String getErrorCode(){
        String errorCode=(String) errorAttributes.get(ErrorAttributeConstant.ERROR_CODE);
        return errorCode==null?"":errorCode;
    }

    private String getPath(){
        String errorCode=(String) errorAttributes.get(ErrorAttributeConstant.PATH);
        return errorCode==null?"":errorCode;
    }

    public void addPath(String newPath){
        if (!StringUtils.hasText(newPath)){
            return;
        }
        String path=this.getPath();
        if (StringUtils.hasText(path)){
            path=newPath + "->" + path;
        }else{
            path=newPath;
        }
        errorAttributes.put(ErrorAttributeConstant.PATH,path);
    }
}
