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

package com.hhao.common.springboot.web.config.component;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 加载自定的RequestMappingHandlerMapping、RequestMappingHandlerAdapter、ExceptionHandlerExceptionResolver
 * 目前暂时没用上
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnMissingBean(CustomConfig.class)
public class CustomConfig {

    /**
     * Custom web mvc registrations web mvc registrations.
     *
     * @return the web mvc registrations
     */
    @Bean
    public WebMvcRegistrations customWebMvcRegistrations() {
        return new CustomWebMvcRegistrations();
    }
}
