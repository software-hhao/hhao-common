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


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 对request、response加一层包装，可以多次读取body内容
 * 采用spring自已的ContentCachingRequestWrapper、ContentCachingResponseWrapper
 * ContentCachingRequestWrapper有局限性，只能在doFilter调用链之后才能访问
 * 但性能优
 *
 * @author Wang
 * @since 1.0.0
 */
public class CachingAfterRequestFilter implements Filter {
    private static final int DEFAULT_MAX_PAYLOAD_LENGTH = 50;
    private int maxPayloadLength = DEFAULT_MAX_PAYLOAD_LENGTH;
    private MatchProperties matchProperties;

    /**
     * Instantiates a new Caching after request filter.
     *
     * @param maxPayloadLength the max payload length
     * @param matchProperties  the match properties
     */
    public CachingAfterRequestFilter(int maxPayloadLength, MatchProperties matchProperties) {
        this.maxPayloadLength = maxPayloadLength;
        this.matchProperties = matchProperties;
    }

    /**
     * Gets max payload length.
     *
     * @return the max payload length
     */
    protected int getMaxPayloadLength() {
        return this.maxPayloadLength;
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
        if (matchProperties.match(httpRequest.getRequestURI())) {
            if (!isAsyncDispatch(httpRequest) && !(httpRequest instanceof CachingAfterRequestWrapper)) {
                httpRequest = new CachingAfterRequestWrapper(httpRequest, getMaxPayloadLength());
            }
        }
        chain.doFilter(httpRequest, response);
    }
}
