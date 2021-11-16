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

package com.hhao.common.springboot.config;

import com.hhao.common.metadata.Mdm;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 从配置文件更新元数据
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "com.hhao.config.metadata",name = "enable",havingValue = "true",matchIfMissing = true)
@EnableConfigurationProperties({MetadataConfig.MetadataProperties.class})
public class MetadataConfig extends AbstractBaseConfig {

    /**
     * Instantiates a new Metadata config.
     *
     * @param metadataProperties the metadata properties
     */
    public MetadataConfig(MetadataProperties metadataProperties){
        Map<String,String> values=metadataProperties.getValues();
        values.entrySet().forEach(entry->{
            for(Mdm mdm:Mdm.values()){
                if (mdm.metadata().support(entry.getKey())){
                    mdm.metadata().update(entry.getValue());
                }
            }
        });
    }

    /**
     * Metadata properties metadata properties.
     *
     * @return the metadata properties
     */
    @Bean
    @ConditionalOnMissingBean(MetadataProperties.class)
    public MetadataProperties metadataProperties(){
        return new MetadataProperties();
    }

    /**
     * The type Metadata properties.
     */
    @ConfigurationProperties("com.hhao.metadata")
    public static class MetadataProperties{
        private Map<String,String> values=new HashMap<>();

        /**
         * Gets values.
         *
         * @return the values
         */
        public Map<String, String> getValues() {
            return values;
        }

        /**
         * Sets values.
         *
         * @param values the values
         */
        public void setValues(Map<String, String> values) {
            this.values = values;
        }
    }
}
