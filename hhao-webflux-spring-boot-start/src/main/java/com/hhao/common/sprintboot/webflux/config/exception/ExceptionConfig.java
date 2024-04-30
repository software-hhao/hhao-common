
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

package com.hhao.common.sprintboot.webflux.config.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.hhao.common.exception.ErrorCode;
import com.hhao.common.exception.ExceptionTransfer;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.springboot.AppContext;
import com.hhao.common.springboot.exception.support.FieldErrorHelper;
import com.hhao.common.sprintboot.webflux.config.AbstractBaseWebFluxConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Wang
 * @since 2022/1/14 11:07
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnClass(WebFluxConfigurer.class)
@AutoConfigureBefore({WebFluxAutoConfiguration.class, ErrorWebFluxAutoConfiguration.class})
@EnableConfigurationProperties({ServerProperties.class, WebProperties.class})
@ConditionalOnProperty(prefix = "com.hhao.config.error",name = "enable",havingValue = "true",matchIfMissing = true)
public class ExceptionConfig extends AbstractBaseWebFluxConfig {
    protected final Logger logger = LoggerFactory.getLogger(ExceptionConfig.class);
    private final ServerProperties serverProperties;
    private final String errorMessageSource = ("classpath:" + ErrorCode.class.getPackageName() + "/messages").replaceAll("[.]", "/");


    public ExceptionConfig(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Bean
    public DefaultExceptionTransfer defaultExceptionTransfer() {
        return new DefaultExceptionTransfer();
    }


    @Bean
    @ConditionalOnMissingBean(value = ErrorWebExceptionHandler.class, search = SearchStrategy.CURRENT)
    @Order(-2)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes,
                                                             WebProperties webProperties, ObjectProvider<ViewResolver> viewResolvers,
                                                             ServerCodecConfigurer serverCodecConfigurer, ApplicationContext applicationContext,
                                                             List<ExceptionTransfer> exceptionTransfers) {
        DefaultErrorWebExceptionHandler exceptionHandler = new CustomErrorWebExceptionHandler(errorAttributes,
                webProperties.getResources(), this.serverProperties.getError(), applicationContext, exceptionTransfers);
        exceptionHandler.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
        exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }

    @Bean
    @ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
    @Order(-2)
    public DefaultErrorAttributes errorAttributes() {
        //return new DefaultErrorAttributes();
        return new CustomErrorAttributes();
    }


    /***
     * 在容器启动之后执行 1、如果采用jackson，则加入FieldError的反序列化 2、加入自定义默认的错误消息文件
     *
     * @author Wang
     */
    @Component
    public class ApplicationAfterRunner implements ApplicationRunner, Ordered {
        /**
         * The Message source.
         */
        MessageSource messageSource;

        /**
         * Instantiates a new Application after runner.
         *
         * @param messageSource the message source
         */
        @Autowired
        public ApplicationAfterRunner(MessageSource messageSource) {
            this.messageSource = messageSource;
        }

        /**
         * Run.
         *
         * @param args the args
         * @throws Exception the exception
         */
        @Override
        public void run(ApplicationArguments args) throws Exception {
            updateJsonObjectMapper();
            addMessageSource(messageSource);
        }

        /**
         * Gets order.
         *
         * @return the order
         */
        @Override
        public int getOrder() {
            return 0;
        }
    }


    /***
     * 添加错误信息的资源文件
     * @param :
     * @return void
     **/
    private void addMessageSource(MessageSource messageSource) {
        try {
            if (messageSource instanceof AbstractResourceBasedMessageSource) {
                ((AbstractResourceBasedMessageSource) messageSource).addBasenames(errorMessageSource);
            } else {
                logger.info("can't find ResourceBasedMessageSource");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 如果有加载Jackson，则加入FieldError的序列化处理
     **/
    private void updateJsonObjectMapper() {
        try {
            ObjectMapper objectMapper = this.applicationContext.getBean(ObjectMapper.class);
            SimpleModule simpleModule = new SimpleModule("ErrorModule");
            simpleModule.addSerializer(FieldError.class, new FieldErrorSerializer(FieldError.class, super.applicationContext, Locale.getDefault()));
            objectMapper.registerModule(simpleModule);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 序列化输出FieldError错误对象
     *
     * @author Wang
     * @Date 16:29 2019/7/18
     **/
    private static class FieldErrorSerializer extends StdSerializer<FieldError> implements ContextualSerializer {
        private ApplicationContext applicationContext;
        private Locale locale;

        /**
         * Instantiates a new Field error serializer.
         *
         * @param t                  the t
         * @param applicationContext the application context
         * @param locale             the locale
         */
        protected FieldErrorSerializer(Class<FieldError> t, ApplicationContext applicationContext, Locale locale) {
            super(t);
            this.applicationContext = applicationContext;
            this.locale = locale;
        }

        /**
         * Create contextual json serializer.
         *
         * @param prov     the prov
         * @param property the property
         * @return the json serializer
         * @throws JsonMappingException the json mapping exception
         */
        @Override
        public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
            if (LocaleContextHolder.getLocale().equals(locale)) {
                return this;
            } else {
                return new FieldErrorSerializer(FieldError.class, applicationContext, AppContext.getInstance().getLocale());
            }
        }

        /**
         * Serialize.
         *
         * @param fieldError the field error
         * @param gen        the gen
         * @param provider   the provider
         * @throws IOException the io exception
         */
        @Override
        public void serialize(FieldError fieldError, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("message", FieldErrorHelper.getMessage(fieldError));
            gen.writeStringField("field", fieldError.getField());
            gen.writeStringField("code", fieldError.getCode());
            gen.writeObjectField("rejectedValue", fieldError.getRejectedValue());
            gen.writeEndObject();
        }
    }
}

