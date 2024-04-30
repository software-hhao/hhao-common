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

package com.hhao.common.springboot.web.config.filter;


import com.hhao.common.jackson.JacksonUtilFactory;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.springboot.jackson.SpringJacksonKeyType;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志过滤器
 *
 * @author Wang
 * @since 1.0.0
 */
public class LoggingFilter implements Filter {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private final String RUNTIME_ATTRIBUTE = "RUNTIME_ATTRIBUTE";
    private MatchProperties matchProperties;

    /**
     * Instantiates a new Logging filter.
     *
     * @param matchProperties the match properties
     */
    public LoggingFilter(MatchProperties matchProperties) {
        this.matchProperties = matchProperties;
    }

    /**
     * Is async dispatch boolean.
     *
     * @param request the request
     * @return the boolean
     */
    protected boolean isAsyncDispatch(HttpServletRequest request) {
        return DispatcherType.ASYNC.equals(request.getDispatcherType());
    }

    /**
     * Is async started boolean.
     *
     * @param request the request
     * @return the boolean
     */
    protected boolean isAsyncStarted(HttpServletRequest request) {
        return WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted();
    }

    /**
     * Do filter.
     *
     * @param request  the request
     * @param response the response
     * @param chain    the chain
     * @throws IOException      the io exception
     * @throws ServletException the servlet exception
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("OncePerRequestFilter just supports HTTP requests");
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        //路径过滤
        if (!matchProperties.match(httpRequest.getRequestURI())) {
            chain.doFilter(httpRequest, httpResponse);
        } else {
            if (!isAsyncDispatch(httpRequest)) {
                beforeRequest(httpRequest, httpResponse);
            }
            try {
                chain.doFilter(httpRequest, httpResponse);
            } finally {
                if (!isAsyncStarted(httpRequest)) {
                    afterRequest(httpRequest, httpResponse);
                }
            }
        }
    }

    /**
     * Before request.
     *
     * @param request  the request
     * @param response the response
     * @throws IOException the io exception
     */
    protected void beforeRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setAttribute(RUNTIME_ATTRIBUTE, Long.valueOf(System.currentTimeMillis()));
    }

    /**
     * After request.
     *
     * @param request  the request
     * @param response the response
     * @throws IOException the io exception
     */
    protected void afterRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> infoMap = new HashMap<>(16);
        infoMap.put("RequestURL", request.getRequestURL());
        infoMap.put("RequestURI", request.getRequestURI());
        infoMap.put("Method", request.getMethod());
        infoMap.put("DispatcherType", request.getDispatcherType());

        infoMap.put("RemoteAddr", request.getRemoteAddr());
        infoMap.put("RemoteHost", request.getRemoteHost());
        infoMap.put("RemotePort", request.getRemotePort());
        infoMap.put("RemoteUser", request.getRemoteUser());

        infoMap.put("Locale", request.getLocale().getDisplayName());
        infoMap.put("QueryString", request.getQueryString());
        infoMap.put("ContentLength", request.getContentLength());

        Map<String, String> headMap = new HashMap<>(32);
        request.getHeaderNames().asIterator().forEachRemaining((name) -> {
            headMap.put(name, request.getHeader(name));
        });
        infoMap.put("Heads", headMap);

        if (request instanceof CachingAfterRequestWrapper) {
            infoMap.put("PayLoad", new String(((CachingAfterRequestWrapper) request).getContentAsByteArray(), request.getCharacterEncoding()));
        } else if (request instanceof CachingBeforeRequestWrapper) {
            infoMap.put("PayLoad", new String(((CachingBeforeRequestWrapper) request).getContentAsByteArray(), request.getCharacterEncoding()));
        }

        Long startTime = (Long) request.getAttribute(RUNTIME_ATTRIBUTE);
        infoMap.put("Status", response.getStatus());
        infoMap.put("Time", String.valueOf(System.currentTimeMillis() - startTime.longValue()));

        if (response instanceof ContentCachingResponseWrapper) {
            infoMap.put("Response", new String(((ContentCachingResponseWrapper) response).getContentAsByteArray(), request.getCharacterEncoding()));
        }

        logger.debug(JacksonUtilFactory.getJsonUtil(SpringJacksonKeyType.SPRING_DEFAULT).map2String(infoMap));
    }
}
