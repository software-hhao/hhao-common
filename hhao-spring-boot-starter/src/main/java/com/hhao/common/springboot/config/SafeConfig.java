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

import com.hhao.common.springboot.safe.DefaultSafeHtmlExecutor;
import com.hhao.common.springboot.safe.SafeHtmlExecutor;
import com.hhao.common.springboot.safe.decode.Base64DecodeHandler;
import com.hhao.common.springboot.safe.decode.DecodeHandler;
import com.hhao.common.springboot.safe.xss.DefaultXssPolicyHandler;
import com.hhao.common.springboot.safe.xss.XssPolicyHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * The type Safe config.
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(prefix = "com.hhao.config.safe",name = "enable",havingValue = "true",matchIfMissing = true)
public class SafeConfig extends AbstractBaseConfig {
    /**
     * Xss过滤的规则文件
     */
    @Value("${com.hhao.xss.policy:'classpath:config/antisamy.xml'}")
    public String policyUri;

    /**
     * 构建SafeHtmlExecutor执行器
     *
     * @param xssPolicyHandlers the xss policy handlers
     * @param decodeHandlers    the decode handlers
     * @return safe html executor
     */
    @Bean
    @ConditionalOnMissingBean
    SafeHtmlExecutor safeHtmlExecutor(List<XssPolicyHandler> xssPolicyHandlers, List<DecodeHandler> decodeHandlers){
        return new DefaultSafeHtmlExecutor(xssPolicyHandlers,decodeHandlers);
    }

    /**
     * 创建XSS过滤规则的Bean类;
     * 可以创建更多的PolicyHandler Bean,可以在@SafeHtml的policy中指定它们，其值为PolicyHandler中的name;
     *
     * @return xss policy handler
     */
    @Bean
    XssPolicyHandler xssPolicyHandler(){
       return new DefaultXssPolicyHandler(policyUri);
    }

    /**
     * 创建XSS过滤解码器；
     * 可以创建更用的DecodeHandler
     *
     * @return decode handler
     */
    @Bean
    DecodeHandler base64DecodeHandler(){
        return new Base64DecodeHandler();
    }
}
