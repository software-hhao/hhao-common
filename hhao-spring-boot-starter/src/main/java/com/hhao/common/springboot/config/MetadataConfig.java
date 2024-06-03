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

package com.hhao.common.springboot.config;

import com.hhao.common.metadata.CurrencyConfig;
import com.hhao.common.metadata.SystemMetadata;
import com.hhao.common.springboot.metadata.SpringMetadataProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.RoundingMode;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 从配置文件更新元数据
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(MetadataConfig.class)
@ConditionalOnProperty(prefix = "com.hhao.config.metadata",name = "enable",havingValue = "true",matchIfMissing = true)
@EnableConfigurationProperties({SpringMetadataProperties.class})
public class MetadataConfig extends AbstractBaseConfig {

    /**
     * Instantiates a new Metadata config.
     *
     * @param springMetadataProperties the metadata properties
     */
    public MetadataConfig(SpringMetadataProperties springMetadataProperties){
        // 对monetaryConfig进行处理
        Map<String, CurrencyConfig> exCurrencyConfigs=new HashMap<>();
        Map<String, CurrencyConfig> currencyConfigs=springMetadataProperties.getMonetaryConfig().getCurrencyConfigurations();
        // 加入currencyCode为key的情况
        currencyConfigs.forEach((currencyCode,config)->{
            exCurrencyConfigs.put(Currency.getInstance(currencyCode).getSymbol(config.getCurrencyLocale()),config);
        });
        // 加入symbol为key的情况
        exCurrencyConfigs.forEach((symbol,config)->{
            currencyConfigs.put(symbol,config);
        });
        if (currencyConfigs.isEmpty()){
            springMetadataProperties.getMonetaryConfig().setCurrencyConfigurations(getDefaultCurrencyConfig());
        }
        SystemMetadata.getInstance().setMetadataProperties(springMetadataProperties);
    }

    /**
     * 获取默认的CurrencyConfig
     * @return
     */
    private Map<String, CurrencyConfig> getDefaultCurrencyConfig(){
        Map<String, CurrencyConfig> currencyConfigs=new HashMap<>();
        currencyConfigs.put("CNY",new CurrencyConfig("CNY","zh_CN","SYMBOL:¤#,###0.00","CODE:¤#,###0.00",16,2, RoundingMode.HALF_EVEN,true));
        currencyConfigs.put(Currency.getInstance("CNY").getSymbol(Locale.CHINA),new CurrencyConfig("CNY","zh_CN","SYMBOL:¤#,###0.00","CODE:¤#,###0.00",16,2, RoundingMode.HALF_EVEN,true));
        return currencyConfigs;
    }


}
