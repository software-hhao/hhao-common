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

package com.hhao.common.springboot.web.config;

import com.fasterxml.classmate.TypeResolver;
import com.hhao.common.springboot.swagger.SwaggerBaseConfig;
import com.hhao.common.springboot.swagger.SwaggerGroupProperties;
import com.hhao.common.springboot.swagger.SwaggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * http://springfox.github.io/springfox/docs/current/#quick-start-guides
 * 访问地址：http://127.0.0.1:8080/swagger-ui/index.html
 * http://127.0.0.1:8080/doc.html
 * swagger配置
 * swagger包要在项目中加载
 * 要开启swagger配置
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(SwaggerConfig.class)
@ConditionalOnClass({springfox.documentation.spring.web.plugins.Docket.class})
@Import({springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class})
@EnableConfigurationProperties({SwaggerGroupProperties.class, SwaggerProperties.class})
@ConditionalOnProperty(prefix = "com.hhao.config.swagger",name = "enable",havingValue = "true",matchIfMissing = true)
public class SwaggerConfig extends AbstractBaseMvcConfig {
    /**
     * Instantiates a new Swagger config.
     *
     * @param typeResolver           the type resolver
     * @param swaggerGroupProperties the swagger group properties
     * @param applicationContext     the application context
     */
    @Autowired
    public SwaggerConfig(TypeResolver typeResolver, SwaggerGroupProperties swaggerGroupProperties, ApplicationContext applicationContext) {
        new SwaggerBaseConfig(typeResolver, swaggerGroupProperties, applicationContext);
    }
}