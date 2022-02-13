
/*
 * Copyright 2018-2022 WangSheng.
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

package com.hhao.common.sprintboot.webflux.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.CorsRegistry;

/**
 * The type Cors config.
 *
 * @author Wang
 * @since 2022 /1/14 13:35
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(CorsConfig.class)
@ConditionalOnProperty(prefix = "com.hhao.config.cors",name = "enable",havingValue = "true",matchIfMissing = false)
public class CorsConfig extends AbstractBaseWebFluxConfig{

    /**
     * 这里是通过重写父类配置Cors
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

    }

    /**
     * 通过内置的CorsWebFilter应用CORS支持，它非常适合于功能端点
     * 注意，这里启动了所有的CORS访问!!!
     *
     * @return the cors web filter
     */
    @Bean
    CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // Possibly...
        // config.applyPermitDefaultValues()
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
