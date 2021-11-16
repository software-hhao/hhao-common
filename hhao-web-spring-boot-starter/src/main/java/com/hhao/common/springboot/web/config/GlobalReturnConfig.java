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

package com.hhao.common.springboot.web.config;

import com.hhao.common.springboot.annotations.ResponseAutoWrapper;
import com.hhao.common.springboot.response.ResultWrapper;
import com.hhao.common.springboot.response.ResultWrapperBuilder;
import com.hhao.common.springboot.response.ResultWrapperProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

import javax.servlet.RequestDispatcher;

/**
 * 通过ResponseAutoWrapper注解，执行Rest统一返回
 * 支持application/json和application/xml两种形式的product(客户端access设置以上媒体类型)
 *
 * @author Wan
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(GlobalReturnConfig.class)
@ConditionalOnProperty(prefix = "com.hhao.config.global-return",name = "enable",havingValue = "true",matchIfMissing = true)
public class GlobalReturnConfig {

    /**
     * Result wrapper properties result wrapper properties.
     *
     * @return the result wrapper properties
     */
    @Bean
    @ConditionalOnMissingBean(ResultWrapperProperties.class)
    public ResultWrapperProperties resultWrapperProperties(){
        return new ResultWrapperProperties();
    }

    /**
     * Result wrapper builder result wrapper builder.
     *
     * @param resultWrapperProperties the result wrapper properties
     * @return the result wrapper builder
     */
    @Bean
    @ConditionalOnMissingBean(ResultWrapperBuilder.class)
    public ResultWrapperBuilder ResultWrapperBuilder(ResultWrapperProperties resultWrapperProperties){
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
            @Override
            public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
                //判断是否有ResponseAutoWrapper的注解
                ResponseAutoWrapper  responseAutoWrapper=returnType.getMethod().getAnnotation(ResponseAutoWrapper.class);
                if (responseAutoWrapper==null){
                    responseAutoWrapper=returnType.getDeclaringClass().getAnnotation(ResponseAutoWrapper.class);
                }
                if (responseAutoWrapper==null || responseAutoWrapper.value()==false){
                    return false;
                }
                return true;
            }

            @Override
            public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
                if (selectedContentType.includes(MediaType.APPLICATION_JSON) || selectedContentType.includes(MediaType.APPLICATION_XML)){
                    //对json返回进行包装
                    if (body instanceof ResultWrapper) {
                        return body;
                    } else if ((((ServletServerHttpRequest) request).getServletRequest().getDispatcherType().ordinal()== DispatcherType.ERROR.ordinal()) || body instanceof Exception){
                        Integer status=getStatue((ServletServerHttpRequest)request);
                        String msg=HttpStatus.valueOf(status).getReasonPhrase();
                        return ResultWrapperBuilder.error(body,String.valueOf(status),msg!=null?msg:"Http Status " + status);
                    }
                    return ResultWrapperBuilder.ok(body);
                }
                return body;
            }
        };
    };

    private Integer getStatue(ServletServerHttpRequest request){
        Integer status=(Integer) request.getServletRequest().getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status==null){
            status=999;
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
        public ResponseWrapperAdvice(ResponseBodyAdvice<Object> responseBodyAdvice){
            this.responseBodyAdvice=responseBodyAdvice;
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
