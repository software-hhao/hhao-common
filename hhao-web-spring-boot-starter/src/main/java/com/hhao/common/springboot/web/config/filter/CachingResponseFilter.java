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

package com.hhao.common.springboot.web.config.filter;

import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 增出response的包装过滤
 *
 * @author Wang
 * @since 1.0.0
 */
public class CachingResponseFilter implements Filter {
    private MatchProperties matchProperties;

    /**
     * Instantiates a new Caching response filter.
     *
     * @param matchProperties the match properties
     */
    public CachingResponseFilter(MatchProperties matchProperties){
        this.matchProperties=matchProperties;
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
        if (matchProperties.match(httpRequest.getRequestURI())) {
            if (!isAsyncDispatch(httpRequest) && !(response instanceof ContentCachingResponseWrapper)) {
                httpResponse = new CachingResponseWrapper(httpResponse);
            }
        }

        chain.doFilter(httpRequest, httpResponse);

        if (!isAsyncStarted(httpRequest) && httpResponse instanceof ContentCachingResponseWrapper) {
            ((ContentCachingResponseWrapper) httpResponse).copyBodyToResponse();
        }
    }
}
