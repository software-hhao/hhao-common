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

package com.hhao.common.springboot.web.config.exception;

import com.hhao.common.springboot.exception.entity.AbstractSysRuntimeException;
import com.hhao.common.springboot.exception.entity.AbstractUnknowRuntimeException;
import com.hhao.common.springboot.exception.entity.request.AbstractRequestRuntimeException;
import com.hhao.common.springboot.exception.entity.server.AbstractServerRuntimeException;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Nullable
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
        //可以做进一步扩展处理，比如针对html类型，可以自定义异常处理的注解，用于指定异常的跳转；
        ModelAndView modelAndView = super.doResolveException(request, response, handler, ex);
        if (modelAndView != null) {
            return modelAndView;
        }
        try {

            if (ex instanceof AbstractRequestRuntimeException) {
                //业务类，请求错误
                return handleRequestRuntimeException(
                        (AbstractRequestRuntimeException) ex, request, response, handler);
            } else if (ex instanceof AbstractServerRuntimeException) {
                //业务类，业务错误
                return handleServerRuntimeException(
                        (AbstractServerRuntimeException) ex, request, response, handler);
            }else if (ex instanceof AbstractSysRuntimeException) {
                //系统类，系统错误
                return handleSysRuntimeException(
                        (AbstractSysRuntimeException) ex, request, response, handler);
            }else if (ex instanceof AbstractUnknowRuntimeException) {
                //未知错误
                return handleUnknowRuntimeException(
                        (AbstractUnknowRuntimeException) ex, request, response, handler);
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
    protected ModelAndView handleRequestRuntimeException(AbstractRequestRuntimeException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
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
    protected ModelAndView handleUnknowRuntimeException(AbstractUnknowRuntimeException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
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
    protected ModelAndView handleServerRuntimeException(AbstractServerRuntimeException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
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
    protected ModelAndView handleSysRuntimeException(AbstractSysRuntimeException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return new ModelAndView();
    }

}
