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

package com.hhao.common.springboot.web.config.exception;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.hhao.common.springboot.AppContext;
import com.hhao.common.springboot.exception.ErrorInfo;
import com.hhao.common.springboot.exception.ExceptionTransfer;
import com.hhao.common.springboot.exception.util.FieldErrorHelper;
import com.hhao.common.springboot.web.config.AbstractBaseMvcConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * 默认的异常配置有两处： ErrorMvcAutoConfiguration WebMvcConfigurationSupport#handlerExceptionResolver 自定义的异常处理采用了如下替换： CustomErrorAttributes替换DefaultErrorAttributes CustomErrorViewResolver替换DefaultErrorViewResolver CustomHandlerExceptionResolver替换DefaultHandlerExceptionResolver CustomErrorController替换BasicErrorController
 *
 * @author Wan
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(ErrorConfig.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties({ ServerProperties.class, WebMvcProperties.class })
@ConditionalOnProperty(prefix = "com.hhao.config.error",name = "enable",havingValue = "true",matchIfMissing = true)
public class ErrorConfig extends AbstractBaseMvcConfig {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(ErrorConfig.class);
    private final ServerProperties serverProperties;
    private final String errorMessageSource = ("classpath:" + ErrorInfo.class.getPackageName() + "/messages").replaceAll("[.]", "/");


    /**
     * Instantiates a new Error config.
     *
     * @param serverProperties the server properties
     */
    public ErrorConfig(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    /***
     * 替换DefaultErrorAttributes
     * @param exceptionTransfers the exception transfers
     * @return org.springframework.boot.web.servlet.error.ErrorAttributes error attributes
     */
    @Bean
    @Order(-2)
    public ErrorAttributes errorAttributes(List<ExceptionTransfer> exceptionTransfers) {
        return new CustomErrorAttributes(exceptionTransfers);
    }

    /**
     * Default exception transfer default exception transfer.
     *
     * @return the default exception transfer
     */
    @Bean
    public DefaultExceptionTransfer defaultExceptionTransfer(){
        return new DefaultExceptionTransfer();
    }


    /**
     * Custom error controller custom error controller.
     *
     * @param errorAttributes    the error attributes
     * @param errorViewResolvers the error view resolvers
     * @return the custom error controller
     */
    @Bean
    @ConditionalOnMissingBean(value = ErrorController.class, search = SearchStrategy.CURRENT)
    public CustomErrorController customErrorController(ErrorAttributes errorAttributes,
                                                      ObjectProvider<ErrorViewResolver> errorViewResolvers) {
        return new CustomErrorController(errorAttributes, this.serverProperties.getError(),
                errorViewResolvers.orderedStream().collect(Collectors.toList()));
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
    private class FieldErrorSerializer extends StdSerializer<FieldError> implements ContextualSerializer {
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
