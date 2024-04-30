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

package com.hhao.common.springboot.web.config;

import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.springboot.response.*;
import jakarta.servlet.RequestDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 通过ResponseAutoWrapper注解，执行Rest统一返回
 * 支持application/json和application/xml两种形式的product(客户端access设置以上媒体类型)
 *
 * @author Wan
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(GlobalReturnConfig.class)
@EnableConfigurationProperties({ResultWrapperProperties.class})
@ConditionalOnProperty(prefix = "com.hhao.config.global-return", name = "enable", havingValue = "true", matchIfMissing = true)
public class GlobalReturnConfig {
    protected final Logger logger = LoggerFactory.getLogger(GlobalReturnConfig.class);
    private ResultWrapperProperties resultWrapperProperties;

    @Autowired
    public GlobalReturnConfig(ResultWrapperProperties resultWrapperProperties) {
        this.resultWrapperProperties = resultWrapperProperties;
    }

    /**
     * Result wrapper builder result wrapper builder.
     *
     * @param resultWrapperProperties the result wrapper properties
     * @return the result wrapper builder
     */
    @Bean
    @ConditionalOnMissingBean(ResultWrapperBuilder.class)
    public ResultWrapperBuilder resultWrapperBuilder(ResultWrapperProperties resultWrapperProperties) {
        return new ResultWrapperBuilder(resultWrapperProperties);
    }

    /**
     * Response wrapper response body advice.
     *
     * @return the response body advice
     */
    @Bean("responseWrapper")
    @ConditionalOnMissingBean(name = "responseWrapper")
    public ResponseBodyAdvice<Object> responseWrapper() {
        return new ResponseBodyAdvice<Object>() {
            private final ConcurrentMap<Method, Boolean> supportsCache = new ConcurrentHashMap<>();

            @Override
            public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
                return supportsCache.computeIfAbsent(returnType.getMethod(), k -> {
                    if (returnType == null) {
                        return false;
                    }
                    Method method = returnType.getMethod();
                    if (method == null) {
                        return false;
                    }
                    Class<?> returnTypeClass = method.getReturnType();
                    // 如果返回值类型继承自UnResultWrapper,则不支持自动包装
                    if (UnResultWrapper.class.isAssignableFrom(returnTypeClass)) {
                        return false;
                    }
                    // 判断是否有ResponseAutoWrapper的注解
                    //方法级注解
                    ResponseAutoWrapper responseAutoWrapper = method.getAnnotation(ResponseAutoWrapper.class);
                    if (responseAutoWrapper == null && method.getDeclaringClass() != null) {
                        //类级注解
                        responseAutoWrapper = method.getDeclaringClass().getAnnotation(ResponseAutoWrapper.class);
                    }
                    return responseAutoWrapper != null && responseAutoWrapper.value();
                });
            }

            @Override
            public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
                logger.info("MediaType:{}", selectedContentType.toString());
                //对json或xml返回进行包装
                if (selectedContentType.includes(MediaType.APPLICATION_JSON) || selectedContentType.includes(MediaType.APPLICATION_XML)) {
                    //如果已经是ResultWrapper,则直接返回
                    if (body instanceof ResultWrapper) {
                        return body;
                    } else if ((((ServletServerHttpRequest) request).getServletRequest().getDispatcherType().ordinal() == DispatcherType.ERROR.ordinal()) || body instanceof Exception) {
                        Integer status = getStatue((ServletServerHttpRequest) request);
                        String msg = HttpStatus.valueOf(status).getReasonPhrase();
                        return ResultWrapperBuilder.error(body, status, msg != null ? msg : "Http Status " + status);
                    }
                    return ResultWrapperBuilder.ok(body);
                }
                logger.warn("Return not wrapper");
                return body;
            }
        };
    }

    ;

    private Integer getStatue(ServletServerHttpRequest request) {
        Integer status = (Integer) request.getServletRequest().getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status == null) {
            status = 999;
        }
        return status;
    }


    /**
     * The type Response wrapper advice.
     */
    @RestControllerAdvice
    static class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {
        /**
         * The Response body advice.
         */
        ResponseBodyAdvice<Object> responseBodyAdvice;

        /**
         * Instantiates a new Response wrapper advice.
         *
         * @param responseBodyAdvice the response body advice
         */
        @Autowired
        @Lazy
        public ResponseWrapperAdvice(ResponseBodyAdvice<Object> responseBodyAdvice) {
            this.responseBodyAdvice = responseBodyAdvice;
        }

        /**
         * Supports boolean.
         *
         * @param returnType    the return type
         * @param converterType the converter type
         * @return the boolean
         */
        @Override
        public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
            return responseBodyAdvice.supports(returnType, converterType);
        }

        /**
         * Before body write object.
         *
         * @param body                  the body
         * @param returnType            the return type
         * @param selectedContentType   the selected content type
         * @param selectedConverterType the selected converter type
         * @param request               the request
         * @param response              the response
         * @return the object
         */
        @Override
        public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
            return responseBodyAdvice.beforeBodyWrite(body, returnType, selectedContentType, selectedConverterType, request, response);
        }
    }
}
