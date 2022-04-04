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

import com.hhao.common.springboot.validate.SpringELScriptEvaluatorFactory;
import org.hibernate.validator.internal.engine.ConfigurationImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Role;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validator;

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
@ConditionalOnProperty(prefix = "com.hhao.config.validator",name = "enable",havingValue = "true",matchIfMissing = true)
public class ValidatorConfig extends AbstractBaseConfig implements BeanPostProcessor {

    /**
     * Method validation post processor method validation post processor.
     *
     * @param validator the validator
     * @return the method validation post processor
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public MethodValidationPostProcessor methodValidationPostProcessor(@Lazy Validator validator) {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        postProcessor.setValidator(validator);
        return postProcessor;
    }

    /**
     * Validator local validator factory bean.
     *
     * @param messageSource the message source
     * @return the local validator factory bean
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
        /***
         * 重写父类SpringValidatorAdapter中的方法
         * 对FieldError的defaultMessage做解析，如果它为空，则根据errorCodes和errorArgs从属性文件中获取信息，这样处理后，调用可以：
         *
         * @param messageSource:
         * @return org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
         **/
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean() {
            @Override
            protected void postProcessConfiguration(javax.validation.Configuration<?> configuration) {
                if (configuration instanceof ConfigurationImpl) {
                    //重写方法，设置脚本执行工厂，指定为spel,针对hibernate validate
                    //针对hibernate validate的配置可以在这里设置
                    ConfigurationImpl hibernateConfiguration=((ConfigurationImpl) configuration);
                    hibernateConfiguration.scriptEvaluatorFactory(new SpringELScriptEvaluatorFactory());
                }
            }


//            @Override
//            protected void processConstraintViolations(Set<ConstraintViolation<Object>> violations, Errors errors) {
//                for (ConstraintViolation<Object> violation : violations) {
//                    String field = determineField(violation);
//                    FieldError fieldError = errors.getFieldError(field);
//                    if (fieldError == null || !fieldError.isBindingFailure()) {
//                        try {
//                            ConstraintDescriptor<?> cd = violation.getConstraintDescriptor();
//                            String errorCode = determineErrorCode(cd);
//                            Object[] errorArgs = getArgumentsForConstraint(errors.getObjectName(), field, cd);
//                            if (errors instanceof BindingResult) {
//                                // Can do custom FieldError registration with invalid value from ConstraintViolation,
//                                // as necessary for Hibernate Validator compatibility (non-indexed set path in field)
//                                BindingResult bindingResult = (BindingResult) errors;
//                                String nestedField = bindingResult.getNestedPath() + field;
//                                if (nestedField.isEmpty()) {
//                                    String[] errorCodes = bindingResult.resolveMessageCodes(errorCode);
//                                    ObjectError error = new ObjectError(
//                                            errors.getObjectName(), errorCodes, errorArgs, getErrorDefaultMessage(violation, errorCodes, errorArgs)) {
//                                        @Override
//                                        public boolean shouldRenderDefaultMessage() {
//                                            return requiresMessageFormat(violation);
//                                        }
//                                    };
//                                    error.wrap(violation);
//                                    bindingResult.addError(error);
//                                } else {
//                                    Object rejectedValue = getRejectedValue(field, violation, bindingResult);
//                                    String[] errorCodes = bindingResult.resolveMessageCodes(errorCode, field);
//                                    FieldError error = new FieldError(errors.getObjectName(), nestedField,
//                                            rejectedValue, false, errorCodes, errorArgs, getErrorDefaultMessage(violation, errorCodes, errorArgs)) {
//                                        @Override
//                                        public boolean shouldRenderDefaultMessage() {
//                                            return requiresMessageFormat(violation);
//                                        }
//                                    };
//                                    error.wrap(violation);
//                                    bindingResult.addError(error);
//                                }
//                            } else {
//                                // got no BindingResult - can only do standard rejectValue call
//                                // with automatic extraction of the current field value
//                                errors.rejectValue(field, errorCode, errorArgs, getErrorDefaultMessage(violation, errorCode, errorArgs));
//                            }
//                        } catch (NotReadablePropertyException ex) {
//                            throw new IllegalStateException("JSR-303 validated property '" + field +
//                                    "' does not have a corresponding accessor for Spring data binding - " +
//                                    "check your DataBinder's configuration (bean property versus direct field access)", ex);
//                        }
//                    }
//                }
//            }
//
//            /**
//             * 从资源文件中获取key为errorCode，参数为arguments的信息
//             **/
//            private String getResourceMessage(String key, Object[] arguments) {
//                if (key == null) {
//                    return null;
//                }
//                //如果有需要，可以在这里加入前缀处理
//                try {
//                    return applicationContext.getMessage(key, arguments, Context.getContext().getLocale());
//                } catch (NoSuchMessageException e) {
//                }
//                return null;
//            }
//
//            private String getErrorDefaultMessage(ConstraintViolation<Object> violation, String errorCode, Object[] errorArgs) {
//                String message = violation.getMessage();
//                if ((message == null || message.isEmpty()) && errorCode != null && !errorCode.isEmpty()) {
//                    message = getResourceMessage(errorCode, errorArgs);
//                }
//                return message == null ? "" : message;
//            }
//
//            private String getErrorDefaultMessage(ConstraintViolation<Object> violation, String[] errorCodes, Object[] errorArgs) {
//                String message = violation.getMessage();
//                if ((message == null || message.isEmpty()) && errorCodes != null && errorCodes.length > 0) {
//                    message = getResourceMessage(errorCodes[errorCodes.length - 1], errorArgs);
//                }
//                return message == null ? "" : message;
//            }
        };
        //自定义消息插值处理
        //导入messageSource，以使其可以访问到属性
        //validator.setMessageInterpolator(new MessageInterpolator(new MessageSourceResourceBundleLocator(messageSource)));
        validator.setValidationMessageSource(messageSource);
        return validator;
    }
}
