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

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * {@code @CrossOrigin}：细粒度的控制器方法级配置
 * 默认情况下，{@code @CrossOrigin}允许:
 * 所有的origins。
 * 所有的headers。
 * 控制器方法映射到的所有HTTP方法。
 * 缺省情况下，不启用allowCredentials
 * maxAge设置为30分钟
 * allowCredentials:公开敏感的特定于用户的信息(如cookie和CSRF令牌)，注意，只在适当的地方使用，使用时要配置allowOriginPatterns
 * {@code @CrossOrigin}在类级别也受支持，并且被所有方法继承
 *
 * 全局设置
 * 缺省情况下，全局配置启用如下功能:
 * 所有的起源。
 * 所有的标题。
 * GET、HEAD和POST方法。
 * 缺省情况下，不启用allowCredentials
 * maxAge设置为30分钟
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(CorsConfig.class)
@ConditionalOnProperty(prefix = "com.hhao.config.cors",name = "enable",havingValue = "true",matchIfMissing = false)
public class CorsConfig extends AbstractBaseMvcConfig {

    /**
     * Add cors mappings.
     *
     * @param registry the registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

//        registry.addMapping("/api/**")
//                .allowedOrigins("https://domain2.com")
//                .allowedMethods("PUT", "DELETE")
//                .allowedHeaders("header1", "header2", "header3")
//                .exposedHeaders("header1", "header2")
//                .allowCredentials(true).maxAge(3600);

        // Add more mappings...
    }
}
