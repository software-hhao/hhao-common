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

package com.hhao.common.springboot.web.config.exception;


import com.hhao.common.exception.AbstractBaseRuntimeException;
import com.hhao.common.exception.BaseException;
import com.hhao.common.exception.ExceptionTransfer;
import com.hhao.common.exception.ExceptionTransferComparator;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.springboot.exception.ResultWrapperException;
import com.hhao.common.springboot.exception.ValidateRuntimeException;
import com.hhao.common.springboot.exception.support.ErrorAttributeConstant;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * 自定义异常输出信息
 * 替换DefaultErrorAttributes
 * 基本的思路：
 * 如果没有请求trace信息，则错误信息只返回简易的自定义信息，主要有两类，一类是服务端执行错误提示，一类是客户端提交数据错误提示；
 * 并加上自定义的错误code;
 *
 * @author Wang
 * @since 1.0.0
 */
public class CustomErrorAttributes extends DefaultErrorAttributes {
    private static final ErrorAttributeOptions ERROR_ATTRIBUTE_OPTIONS = ErrorAttributeOptions.of(Include.values());
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(CustomErrorAttributes.class);
    private List<ExceptionTransfer> exceptionTransfers;

    /**
     * Instantiates a new Custom error attributes.
     *
     * @param exceptionTransfers the exception transfers
     */
    public CustomErrorAttributes(List<ExceptionTransfer> exceptionTransfers) {
        this.exceptionTransfers = exceptionTransfers;
        this.exceptionTransfers.sort(ExceptionTransferComparator.INSTANCE);
    }

    /***
     * 顺序
     */
    @Override
    public int getOrder() {
        return -2;
    }

    /**
     * 重写父类的getErrorAttributes，加入自定义的字段信息
     *
     * @param webRequest the web request
     * @param options    the options
     * @return error attributes
     */
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        //异常已包含异常属性，可直接返回
        Map<String, Object> errorAttributes = directReturnErrorAttributes(webRequest);
        if (errorAttributes != null) {
            return errorAttributes;
        }

        //从基类获取包含所有的异常信息
        errorAttributes = super.getErrorAttributes(webRequest, ERROR_ATTRIBUTE_OPTIONS);

        //自定义对errorAttributes的处理
        getErrorAttributes(errorAttributes, webRequest, options);
        storageErrorAttributes(errorAttributes);

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

        // 添加错误详情字段
        if (options.isIncluded(Include.MESSAGE)) {
            errorAttributes.put(ErrorAttributeConstant.DETAILS, getDetails(webRequest));
        }
        return errorAttributes;
    }

    /**
     * Storage error attributes.
     *
     * @param errorAttributes the error attributes
     */
    protected void storageErrorAttributes(Map<String, Object> errorAttributes) {

    }

    private Map<String, Object> directReturnErrorAttributes(WebRequest webRequest) {
        Throwable error = getError(webRequest);
        if (error != null && error instanceof ResultWrapperException) {
            ResultWrapperException resultWrapperException = ((ResultWrapperException) error);
            //补齐当前链路
            String path = (String) webRequest.getAttribute(RequestDispatcher.ERROR_REQUEST_URI, RequestAttributes.SCOPE_REQUEST);
            resultWrapperException.addPath(path);
            return ((ResultWrapperException) error).getErrorAttributes();
        }
        return null;
    }

    /**
     * 自定义errorAttributes的处理
     * 可以在此处添加反馈错误的信息
     *
     * @param errorAttributes
     * @param webRequest
     * @param options
     * @return
     */
    private Map<String, Object> getErrorAttributes(Map<String, Object> errorAttributes, WebRequest webRequest, ErrorAttributeOptions options) {
        //获取异常对象。
        //父类方法调用getError,把异常保存起来了，所以这里可以直接取出来用
        //Throwable exception=(Throwable)webRequest.getAttribute(ErrorAttributes.ERROR_ATTRIBUTE, WebRequest.SCOPE_REQUEST);
        Throwable exception = super.getError(webRequest);

        //如果是数据验证错误异常，进行错误字段的处理。
        //父类中有相关代码，但是getError方法中将异常转成了自定义的异常类了，所以执行不到，这里补执行
        if (exception instanceof ValidateRuntimeException && options.isIncluded(ErrorAttributeOptions.Include.BINDING_ERRORS)) {
            errorAttributes.put(ErrorAttributeConstant.ERRORS, ((ValidateRuntimeException) exception).getBindingResult().getAllErrors());
        }

        //添加自定义异常的code
        addErrorCode(errorAttributes, exception);
        return errorAttributes;
    }

    /**
     * 添加自定义异常的code
     * 如果有自定义的异常，则用自定义的异常号，如果没有，则用response的status
     *
     * @param errorAttributes
     * @param exception
     */
    private void addErrorCode(Map<String, Object> errorAttributes, Throwable exception) {
        if (exception instanceof AbstractBaseRuntimeException) {
            AbstractBaseRuntimeException error = (AbstractBaseRuntimeException) exception;
            errorAttributes.put(ErrorAttributeConstant.ERROR_CODE, error.getErrorCode().getCode());
        } else {
            Integer status = (Integer) errorAttributes.get(ErrorAttributeConstant.STATUS);
            errorAttributes.put(ErrorAttributeConstant.ERROR_CODE, status != null ? status : 999);
        }
    }

    /**
     * 重写
     * 优先返回异常的提示，然后才是servlet中保存的错误提示
     *
     * @param webRequest the web request
     * @param error      the error
     * @return message
     */
    @Override
    protected String getMessage(WebRequest webRequest, Throwable error) {
        if (error != null && StringUtils.hasLength(error.getMessage())) {
            return error.getMessage();
        }
        return super.getMessage(webRequest, error);
    }

    private String getDetails(WebRequest webRequest) {
        Throwable error = getError(webRequest);
        StringBuffer details = new StringBuffer();
        getDetails(error.getCause(), details);
        if (details.isEmpty()) {
            details.append("No more information");
        }
        return details.toString();
    }

    private StringBuffer getDetails(Throwable error, StringBuffer message) {
        if (error == null) {
            return message;
        }
        if (StringUtils.hasLength(error.getMessage())) {
            message.append(error.getMessage());
            message.append(";");
        }
        getDetails(error.getCause(), message);
        return message;
    }

    /**
     * 异常类转换，根据DefaultHandlerExceptionResolver
     *
     * @param exception the exception
     * @return throwable
     */
    protected Throwable transferError(Throwable exception) {
        //如果是自定义的异常，则直接返回
        if (exception instanceof BaseException) {
            return exception;
        }
        // 异常作转换
        for (ExceptionTransfer exceptionTransfer : exceptionTransfers) {
            if (exceptionTransfer.support(exception)) {
                return exceptionTransfer.transfer(exception);
            }
        }

        //可以根据response.status，对异常做转换，如400->RequestException,500->ServerExeption
        return exception;
    }

    /**
     * 暂时不用重写
     *
     * @param request  the request
     * @param response the response
     * @param handler  the handler
     * @param ex       the ex
     * @return model and view
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //这里可以做日志记录,这里只有最原始的exception
        logger.error("Exception:{}", ex);
        //如果要对Exception做转换，可以在这个位置
        Throwable exception = transferError(ex);
        ModelAndView modelAndView = super.resolveException(request, response, handler, (Exception) exception);

        return modelAndView;
    }
}