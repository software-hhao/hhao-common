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

package com.hhao.common.springboot.web.config.component;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * spring boot专用，避免继承webconfigurationsupport对spring的自动配置侵入和破坏
 *
 * @author Wang
 * @since 1.0.0
 */
public class CustomWebMvcRegistrations implements WebMvcRegistrations {
    /**
     * 返回MVC配置应该使用和处理的自定义RequestMappingHandlerMapping。
     * 返回:自定义的RequestMappingHandlerMapping实例
     *
     * @return request mapping handler mapping
     */
    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        //return new CustomRequestMappingHandlerMapping();
        return null;
    }

    /**
     * 返回MVC配置应该使用和处理的自定义RequestMappingHandlerAdapter。
     * 返回:自定义的RequestMappingHandlerAdapter实例
     *
     * @return request mapping handler adapter
     */
    @Override
    public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
        return null;
    }


    /**
     * 返回自定义的ExceptionHandlerExceptionResolver，它应该被MVC配置使用和处理。
     * 返回:自定义的ExceptionHandlerExceptionResolver实例
     *
     * @return exception handler exception resolver
     */
    @Override
    public ExceptionHandlerExceptionResolver getExceptionHandlerExceptionResolver() {
        return null;
    }
}
