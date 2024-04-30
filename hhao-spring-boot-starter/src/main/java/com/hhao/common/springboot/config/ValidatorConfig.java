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

import com.hhao.common.springboot.validate.SpringELScriptEvaluatorFactory;
import jakarta.validation.Validator;
import org.hibernate.validator.internal.engine.ConfigurationImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.Order;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodeFormatter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.util.List;

/**
 * 默认初始化类：ValidationAutoConfiguration
 * 自定义的验证器配置，加强了以下几个方面：
 * 1、对验证错误返回的FieldError或ObjectError重写了解析，方式如下：如果存在defaultMessage，则优先采用defaultMessage
 * 如果defaultMessgae为空，则通过errorCodes和errorArgs从消息资源文件中加载消息，并将其设置为defaultMessage
 * 2、对验证器加入自定义消息插值处理，用于从消息资源文件中加载消息时自动加入message定义的前缀，默认加入的前缀名为：com.xxxx.xxx.xxx.validator.消息键
 *
 * @author Wan
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(ValidatorConfig.class)
@AutoConfigureBefore(ValidationAutoConfiguration.class)
@ConditionalOnProperty(prefix = "com.hhao.config.validator", name = "enable", havingValue = "true", matchIfMissing = true)
public class ValidatorConfig extends AbstractBaseConfig implements BeanPostProcessor {

    @Bean
    public MessageCodesResolver messageCodesResolver() {
        return new MyMessageCodeResolver();
    }


    static class MyMessageCodeResolver extends DefaultMessageCodesResolver {
        @Override
        public String[] resolveMessageCodes(String errorCode, String objectName) {
            return super.resolveMessageCodes(errorCode, objectName);
        }

        @Override
        public void setPrefix(String prefix) {
            super.setPrefix(prefix);
        }

        @Override
        protected String getPrefix() {
            return super.getPrefix();
        }

        @Override
        public void setMessageCodeFormatter(MessageCodeFormatter formatter) {
            super.setMessageCodeFormatter(formatter);
        }

        @Override
        public String[] resolveMessageCodes(String errorCode, String objectName, String field, Class<?> fieldType) {
            return super.resolveMessageCodes(errorCode, objectName, field, fieldType);
        }

        @Override
        protected void buildFieldList(String field, List<String> fieldList) {
            super.buildFieldList(field, fieldList);
        }

        @Override
        protected String postProcessMessageCode(String code) {
            return super.postProcessMessageCode(code);
        }
    }


    @Bean
    @Order(-1)
    public ValidationConfigurationCustomizer myValidationConfigurationCustomizer() {
        return (configuration) -> {
            if (configuration instanceof ConfigurationImpl) {
                //重写方法，设置脚本执行工厂，指定为spel,针对hibernate validate
                //针对hibernate validate的配置可以在这里设置
                ConfigurationImpl hibernateConfiguration = ((ConfigurationImpl) configuration);
                hibernateConfiguration.scriptEvaluatorFactory(new SpringELScriptEvaluatorFactory());
            }
        };
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public MethodValidationPostProcessor methodValidationPostProcessor(@Lazy Validator validator) {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        postProcessor.setValidator(validator);
        return postProcessor;
    }
}
