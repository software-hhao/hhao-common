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
package com.hhao.common.sprintboot.webflux.config.exception;

import com.hhao.common.exception.AbstractBaseRuntimeException;
import com.hhao.common.springboot.exception.error.other.ResultWrapperException;
import com.hhao.common.springboot.exception.error.request.ValidateException;
import com.hhao.common.springboot.exception.util.ErrorAttributeConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
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
        Throwable exception=getError(request);

        //如果是数据验证错误异常，进行错误字段的处理。
        //父类中有相关代码，但是getError方法中将异常转成了自定义的异常类了，所以执行不到，这里补执行
        if (exception instanceof ValidateException && options.isIncluded(ErrorAttributeOptions.Include.BINDING_ERRORS)){
            errorAttributes.put(ErrorAttributeConstant.ERRORS, ((ValidateException) exception).getBindingResult().getAllErrors());
        }

        //添加自定义异常的code
        addErrorCode(errorAttributes,exception);

        return errorAttributes;
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
            errorAttributes.put(ErrorAttributeConstant.ERROR_CODE, error.getErrorInfo().getCode());
        }else{
            Integer status =(Integer) errorAttributes.get(ErrorAttributeConstant.STATUS);
            errorAttributes.put(ErrorAttributeConstant.ERROR_CODE, status!=null?status:999);
        }
    }
}
