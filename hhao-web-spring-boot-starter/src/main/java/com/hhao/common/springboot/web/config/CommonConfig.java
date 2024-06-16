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

import com.hhao.common.springboot.AppContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

/**
 * The type Common config.
 *
 * @author Wan
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(CommonConfig.class)
@ConditionalOnProperty(prefix = "com.hhao.config.common", name = "enable", havingValue = "true", matchIfMissing = true)
public class CommonConfig extends AbstractBaseMvcConfig {

    /***
     * 视图控制器
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }

    @RestController
    public static class Hello {
        AppContext appContext;

        @Autowired
        public Hello(AppContext appContext) {
            this.appContext=appContext;
        }

        @GetMapping("/hello")
        public String hello() {
            return "Nice to use hhao software, more information can visit https://github.com/software-hhao/hhao-common.";
        }

        @GetMapping("/health")
        public String health() {
            return appContext.getAppStatus();
        }
    }
}
