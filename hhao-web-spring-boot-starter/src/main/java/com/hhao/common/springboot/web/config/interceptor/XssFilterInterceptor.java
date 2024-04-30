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

package com.hhao.common.springboot.web.config.interceptor;

import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.springboot.web.config.filter.LoggingFilter;
import com.hhao.common.utils.xss.XssUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.springframework.util.ResourceUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * 全局日志Interceptor
 * Interceptor执行顺序
 * 同步情况下：
 * 1、preHandle
 * 2、postHandle
 * 3、afterCompletion
 * 异步情况下：
 * 1、preHandle
 * 2、afterConcurrentHandlingStarted
 * 3、preHandle
 * 4、postHandle
 * 5、afterCompletion
 * 在异步请求超时或网络错误导致请求完成的场景，HandlerInterceptor的实现需要继续完成本来的工作。但是在这种情况下，Servlet容器不会分发请求，因此也不会调用postHandle和afterCompletion两个方法，相反可以使用WebAsyncManager的registerDeferredResultInterceptor和registerCallbackInterceptor方法注册拦截器以跟踪异步请求。
 * 目前弃用该类
 * 采用注解@SafeHtml
 *
 * @author Wang
 * @since 1.0.0
 */
@Deprecated
public class XssFilterInterceptor implements AsyncHandlerInterceptor {
    /**
     * The constant POLICY_DEFAULT_NAME.
     */
    final static String POLICY_DEFAULT_NAME = "default";
    /**
     * The constant POLICY_FILE_LOCATION.
     */
    final static String POLICY_FILE_LOCATION = "antisamy.xml";
    /**
     * The Default policy.
     */
    static Policy defaultPolicy;

    static {
        try {
            try {
                File file = ResourceUtils.getFile("classpath:" + POLICY_FILE_LOCATION);
                if (file != null) {
                    defaultPolicy = Policy.getInstance(file);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (defaultPolicy == null) {
                defaultPolicy = Policy.getInstance(XssUtils.class.getResourceAsStream(POLICY_FILE_LOCATION));
            }
        } catch (PolicyException e) {
            e.printStackTrace();
        }
    }

    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    /**
     * Is async dispatch boolean.
     *
     * @param request the request
     * @return the boolean
     */
    protected boolean isAsyncDispatch(HttpServletRequest request) {
        return jakarta.servlet.DispatcherType.ASYNC.equals(request.getDispatcherType());
    }

    /**
     * Pre handle boolean.
     *
     * @param request  the request
     * @param response the response
     * @param handler  the handler
     * @return the boolean
     * @throws Exception the exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否异步二次调用，如果是异步二次调用，则跳过
        if (isAsyncDispatch(request)) {
            return true;
        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Arrays.stream(handlerMethod.getMethodParameters()).forEach(methodParameter -> {

        });
        return true;
    }

    /**
     * After concurrent handling started.
     *
     * @param request  the request
     * @param response the response
     * @param handler  the handler
     * @throws Exception the exception
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //异步并发处理
    }

    /**
     * Post handle.
     *
     * @param request      the request
     * @param response     the response
     * @param handler      the handler
     * @param modelAndView the model and view
     * @throws Exception the exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //方法执行完成后调用

    }

    /**
     * After completion.
     *
     * @param request  the request
     * @param response the response
     * @param handler  the handler
     * @param ex       the ex
     * @throws Exception the exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //全部结束后调用
    }
}