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
package com.hhao.common.sprintboot.webflux.config.exception;

import com.hhao.common.exception.AbstractBaseRuntimeException;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.springboot.exception.ResultWrapperException;
import com.hhao.common.springboot.exception.ValidateRuntimeException;
import com.hhao.common.springboot.exception.support.ErrorAttributeConstant;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

/**
 * 替换DefaultErrorAttributes
 *
 * @author Wang
 * @since 2022/1/14 10:40
 */
public class CustomErrorAttributes extends DefaultErrorAttributes {
    protected final Logger logger = LoggerFactory.getLogger(CustomErrorAttributes.class);
    private static final ErrorAttributeOptions ERROR_ATTRIBUTE_OPTIONS=ErrorAttributeOptions.of(ErrorAttributeOptions.Include.values());

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        //异常已包含异常属性，可直接返回
        Map<String, Object> errorAttributes =directReturnErrorAttributes(request);
        if (errorAttributes!=null){
            return errorAttributes;
        }
        //从基类获取包含所有的异常信息
        errorAttributes =super.getErrorAttributes(request, ERROR_ATTRIBUTE_OPTIONS);

        //自定义对errorAttributes的处理
        getErrorAttributes(errorAttributes,request,options);

        //移除不显示的异常信息
        if (!options.isIncluded(Include.EXCEPTION)) {
            errorAttributes.remove(ErrorAttributeConstant.EXCEPTION);
        }
        if (!options.isIncluded(Include.STACK_TRACE)) {
            errorAttributes.remove(ErrorAttributeConstant.TRACE);
        }
        if (!options.isIncluded(Include.MESSAGE) && errorAttributes.get("message") != null) {
            errorAttributes.remove(ErrorAttributeConstant.MESSAGE);
            errorAttributes.remove(ErrorAttributeConstant.DETAILS);
        }
        if (!options.isIncluded(Include.BINDING_ERRORS)) {
            errorAttributes.remove(ErrorAttributeConstant.ERRORS);
        }

        return errorAttributes;
    }

    private Map<String, Object> directReturnErrorAttributes(ServerRequest request){
        Throwable error=getError(request);
        if (error!=null && error instanceof ResultWrapperException){
            ResultWrapperException resultWrapperException=((ResultWrapperException)error);
            //补齐当前链路
            resultWrapperException.addPath(request.path());
            return ((ResultWrapperException)error).getErrorAttributes();
        }
        return null;
    }

    /**
     * 自定义errorAttributes的处理
     * 可以在此处添加反馈错误的信息
     * @param errorAttributes
     * @param request
     * @param options
     * @return
     */
    private Map<String, Object> getErrorAttributes(Map<String, Object> errorAttributes,ServerRequest request, ErrorAttributeOptions options) {
        //获取异常对象。
        //父类方法调用getError,把异常保存起来了，所以这里可以直接取出来用
        Throwable error=getError(request);

        MergedAnnotation<ResponseStatus> responseStatusAnnotation = MergedAnnotations
                .from(error.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY)
                .get(ResponseStatus.class);

        //如果是数据验证错误异常，进行错误字段的处理。
        //父类中有相关代码，但是getError方法中将异常转成了自定义的异常类了，所以执行不到，这里补执行
        if (error instanceof ValidateRuntimeException && options.isIncluded(ErrorAttributeOptions.Include.BINDING_ERRORS)){
            errorAttributes.put(ErrorAttributeConstant.ERRORS, ((ValidateRuntimeException) error).getBindingResult().getAllErrors());
        }

        //对message的覆盖处理
        errorAttributes.put("message", getMessage(error,responseStatusAnnotation));
        errorAttributes.put(ErrorAttributeConstant.DETAILS, getDetails(error));

        //添加自定义异常的code
        addErrorCode(errorAttributes,error);

        return errorAttributes;
    }

    protected String getMessage(Throwable error, MergedAnnotation<ResponseStatus> responseStatusAnnotation) {
        if (error != null && StringUtils.hasLength(error.getMessage())) {
            return error.getMessage();
        }
        String reason = responseStatusAnnotation.getValue("reason", String.class).orElse("");
        if (StringUtils.hasText(reason)) {
            return reason;
        }
        return "No more information";
    }

    private String getDetails(Throwable error){
        StringBuffer details=new StringBuffer();
        getDetails(error.getCause(),details);
        if (details.isEmpty()){
            details.append("No more information");
        }
        return details.toString();
    }

    private StringBuffer getDetails(Throwable error,StringBuffer message){
        if (error==null){
            return message;
        }
        if (StringUtils.hasLength(error.getMessage())){
            message.append(error.getMessage());
            message.append(";");
        }
        getDetails(error.getCause(),message);
        return message;
    }

    /**
     * 添加自定义异常的code
     * 如果有自定义的异常，则用自定义的异常号，如果没有，则用response的status
     * @param errorAttributes
     * @param exception
     */
    private void addErrorCode(Map<String, Object> errorAttributes, Throwable exception) {
        if (exception instanceof AbstractBaseRuntimeException) {
            AbstractBaseRuntimeException error = (AbstractBaseRuntimeException) exception;
            errorAttributes.put(ErrorAttributeConstant.ERROR_CODE, error.getErrorCode().getCode());
        }else{
            Integer status =(Integer) errorAttributes.get(ErrorAttributeConstant.STATUS);
            errorAttributes.put(ErrorAttributeConstant.ERROR_CODE, status!=null?status:999);
        }
    }
}
