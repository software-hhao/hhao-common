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

package com.hhao.common.springboot.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The type Swagger group properties.
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties("com.hhao.config.swagger")
@EnableConfigurationProperties({SwaggerGroupProperties.class, SwaggerProperties.class})
public class SwaggerGroupProperties {
    //是否启用swagger
    private Boolean enable=true;

    private SwaggerProperties[] swaggerGroups;

    /**
     * Get swagger groups swagger properties [ ].
     *
     * @return the swagger properties [ ]
     */
    public SwaggerProperties[] getSwaggerGroups() {
        return swaggerGroups;
    }

    /**
     * Sets swagger groups.
     *
     * @param swaggerGroups the swagger groups
     */
    @Autowired
    public void setSwaggerGroups(SwaggerProperties[] swaggerGroups) {
        this.swaggerGroups = swaggerGroups;
    }

    /**
     * Gets enable.
     *
     * @return the enable
     */
    public Boolean getEnable() {
        return enable;
    }

    /**
     * Sets enable.
     *
     * @param enable the enable
     */
    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
