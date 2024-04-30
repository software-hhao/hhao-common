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

import com.hhao.common.exception.error.BusinessRuntimeException;
import com.hhao.common.exception.error.SystemRuntimeException;
import com.hhao.common.exception.error.UnknowRuntimeException;
import com.hhao.common.exception.error.request.RequestRuntimeException;
import com.hhao.common.exception.error.server.ServerRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.io.IOException;

/**
 * 替换DefaultHandlerExceptionResolver
 *
 * @author Wan
 * @since 1.0.0
 */
@Deprecated
public class CustomHandlerExceptionResolver extends DefaultHandlerExceptionResolver {
    /**
     * Do resolve exception model and view.
     *
     * @param request  the request
     * @param response the response
     * @param handler  the handler
     * @param ex       the ex
     * @return the model and view
     */
    @Override

    protected ModelAndView doResolveException(@NotNull HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //可以做进一步扩展处理，比如针对html类型，可以自定义异常处理的注解，用于指定异常的跳转；
        ModelAndView modelAndView = super.doResolveException(request, response, handler, ex);
        if (modelAndView != null) {
            return modelAndView;
        }
        try {
            if (ex instanceof RequestRuntimeException) {
                //业务类，请求错误
                return handleRequestRuntimeException(
                        (RequestRuntimeException) ex, request, response, handler);
            } else if (ex instanceof BusinessRuntimeException) {
                //业务类，业务错误
                return handleServerRuntimeException(
                        (ServerRuntimeException) ex, request, response, handler);
            } else if (ex instanceof SystemRuntimeException) {
                //系统类，系统错误
                return handleSysRuntimeException(
                        (SystemRuntimeException) ex, request, response, handler);
            } else if (ex instanceof UnknowRuntimeException) {
                //未知错误
                return handleUnknowRuntimeException(
                        (UnknowRuntimeException) ex, request, response, handler);
            }
        } catch (Exception handlerEx) {
            if (logger.isWarnEnabled()) {
                logger.warn("Failure while trying to resolve exception [" + ex.getClass().getName() + "]", handlerEx);
            }
        }
        return null;
    }

    /**
     * Handle request runtime exception model and view.
     *
     * @param ex       the ex
     * @param request  the request
     * @param response the response
     * @param handler  the handler
     * @return the model and view
     * @throws IOException the io exception
     */
    protected ModelAndView handleRequestRuntimeException(RequestRuntimeException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return new ModelAndView();
    }

    /**
     * Handle unknow runtime exception model and view.
     *
     * @param ex       the ex
     * @param request  the request
     * @param response the response
     * @param handler  the handler
     * @return the model and view
     * @throws IOException the io exception
     */
    protected ModelAndView handleUnknowRuntimeException(UnknowRuntimeException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return new ModelAndView();
    }

    /**
     * Handle server runtime exception model and view.
     *
     * @param ex       the ex
     * @param request  the request
     * @param response the response
     * @param handler  the handler
     * @return the model and view
     * @throws IOException the io exception
     */
    protected ModelAndView handleServerRuntimeException(ServerRuntimeException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return new ModelAndView();
    }

    /**
     * Handle sys runtime exception model and view.
     *
     * @param ex       the ex
     * @param request  the request
     * @param response the response
     * @param handler  the handler
     * @return the model and view
     * @throws IOException the io exception
     */
    protected ModelAndView handleSysRuntimeException(SystemRuntimeException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return new ModelAndView();
    }

}
