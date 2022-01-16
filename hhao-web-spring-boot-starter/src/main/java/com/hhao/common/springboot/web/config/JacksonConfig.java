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

package com.hhao.common.springboot.web.config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.hhao.common.jackson.DefaultJacksonUtilBuilder;
import com.hhao.common.jackson.JacksonUtil;
import com.hhao.common.jackson.JacksonUtilFactory;
import com.hhao.extend.money.jackson.MonetaryAmountSerializer;
import com.hhao.extend.money.jackson.MoneyModule;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

import java.util.List;

/**
 * Spring Boot 默认的Jackson自动化配置类:
 * org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
 * WebMvcConfigurationSupport
 * JacksonHttpMessageConvertersConfiguration
 * 在Spring配置的基础上，加入自定义的一些配置
 *
 * @author Wan
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(JacksonConfig.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
@EnableConfigurationProperties({JacksonConfig.MoneyJacksonProperties.class})
@ConditionalOnProperty(prefix = "com.hhao.config.jackson", name = "enable", havingValue = "true", matchIfMissing = true)
public class JacksonConfig extends AbstractBaseMvcConfig {
    private MoneyJacksonProperties moneyJacksonProperties = null;

    /**
     * Instantiates a new Jackson config.
     *
     * @param moneyJacksonProperties the money jackson properties
     */
    public JacksonConfig(MoneyJacksonProperties moneyJacksonProperties) {
        this.moneyJacksonProperties = moneyJacksonProperties;
    }

    /**
     * Mapping jackson 2 http message converter mapping jackson 2 http message converter.
     *
     * @param objectMapper the object mapper
     * @return the mapping jackson 2 http message converter
     */
    //@Bean
    //public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
    //    return new MappingJackson2HttpMessageConverter(objectMapper);
    //}

    /**
     * Mapping jackson 2 xml http message converter mapping jackson 2 xml http message converter.
     *
     * @param xmlMapper the xml mapper
     * @return the mapping jackson 2 xml http message converter
     */
    //@Bean
    //public MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter(XmlMapper xmlMapper) {
    //    return new MappingJackson2XmlHttpMessageConverter(xmlMapper);
    //}

    /***
     * 这部份的代码是借助JacksonAutoConfiguration的Jackson2ObjectMapperBuilder生成ObjectMapper 然后配置生成后的ObjectMapper 这样可以利用Spring的初始化配置功能，同时加入后期需要改进的功能，并使全局的JacksonUtil与Spring的ObjectMapper一致 注意，Bean的名称不能变
     * @param builder the builder
     * @return the object mapper
     */
    @Primary
    @Bean("jacksonObjectMapper")
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        builder.featuresToDisable(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS);
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        //允许模块重复注册，以自定义的模块替换原模块
        //objectMapper.configure(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS, false);
        //注册一些模块
        objectMapper.registerModule(new MoneyModule(moneyJacksonProperties.deserializerUseMoneyFormat, moneyJacksonProperties.serializerUseMoneyFormat, new MonetaryAmountSerializer.FieldNames(moneyJacksonProperties.amountFieldName, moneyJacksonProperties.currencyUnitFieldName, moneyJacksonProperties.formattedFieldName)));

        //在此步骤中，会用自定义的模块覆盖原来的模块
        buildJsonUtil(objectMapper);
        return objectMapper;
    }

    /**
     * Build json util jackson util.
     *
     * @param objectMapper the object mapper
     * @return the jackson util
     */
    protected JacksonUtil buildJsonUtil(ObjectMapper objectMapper) {
        JacksonUtil jsonUtil = new DefaultJacksonUtilBuilder<ObjectMapper>()
                .init()
                .build(objectMapper,mapper->{

                });
        JacksonUtilFactory.addJsonUtil(JacksonUtilFactory.DEFAULT_KEY, jsonUtil);
        return jsonUtil;
    }

    /**
     * Xml mapper xml mapper.
     *
     * @param builder the builder
     * @return the xml mapper
     */
    @Bean("xmlMapper")
    public XmlMapper xmlMapper(Jackson2ObjectMapperBuilder builder) {
        builder.featuresToDisable(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS);
        XmlMapper xmlMapper = builder.createXmlMapper(true).build();
        //允许模块重复注册，以自定义的模块替换原模块
        //因为方法过时了，所以改用builder.featuresToEnable(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS);
        //xmlMapper.configure(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS, false);
        //在此步骤中，会用自定义的模块覆盖原来的模块
        //注册一些模块
        xmlMapper.registerModule(new MoneyModule(moneyJacksonProperties.deserializerUseMoneyFormat, moneyJacksonProperties.serializerUseMoneyFormat, new MonetaryAmountSerializer.FieldNames(moneyJacksonProperties.amountFieldName, moneyJacksonProperties.currencyUnitFieldName, moneyJacksonProperties.formattedFieldName)));

        buildXmlUtil(xmlMapper);
        return xmlMapper;
    }

    /**
     * Build xml util jackson util.
     *
     * @param xmlMapper the xml mapper
     * @return the jackson util
     */
    protected JacksonUtil buildXmlUtil(XmlMapper xmlMapper) {
        JacksonUtil xmlUtil = new DefaultJacksonUtilBuilder<XmlMapper>()
                .init()
                .build(xmlMapper,mapper->{

                });
        JacksonUtilFactory.addXmlUtil(JacksonUtilFactory.DEFAULT_KEY, xmlUtil);
        return xmlUtil;
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        int index=0;
        for(HttpMessageConverter converter:converters){
            if(converter instanceof StringHttpMessageConverter){
                break;
            }
            index++;
        }
        //这两个要加在StringHttpMessageConverter之前，否则当String类型的application/json将会被StringHttpMessageConverter先行转换
        converters.add(index,new MappingJackson2XmlHttpMessageConverter(JacksonUtilFactory.getXmlUtil().getObjectMapper()));
        converters.add(index,new MappingJackson2HttpMessageConverter(JacksonUtilFactory.getJsonUtil().getObjectMapper()));
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

    }



    /**
     * 关于Money Jackson的一些配置项
     */
    @ConfigurationProperties("com.hhao.config.jackson")
    public static class MoneyJacksonProperties {
        //反序列化时，是优先采用@MoneyFormat注解做格式化
        private Boolean deserializerUseMoneyFormat = false;
        //序列化时，是优先采用@MoneyFormat注解做格式化
        private Boolean serializerUseMoneyFormat = true;
        //序列化时金额显示的字段名称
        private String amountFieldName = "amount";
        //序列化时币代码显示的字段名称
        private String currencyUnitFieldName = "currency";
        //序列化时格式化Money显示的字段名称
        private String formattedFieldName = "formatted";

        /**
         * Gets deserializer use money format.
         *
         * @return the deserializer use money format
         */
        public Boolean getDeserializerUseMoneyFormat() {
            return deserializerUseMoneyFormat;
        }

        /**
         * Sets deserializer use money format.
         *
         * @param deserializerUseMoneyFormat the deserializer use money format
         */
        public void setDeserializerUseMoneyFormat(Boolean deserializerUseMoneyFormat) {
            this.deserializerUseMoneyFormat = deserializerUseMoneyFormat;
        }

        /**
         * Gets serializer use money format.
         *
         * @return the serializer use money format
         */
        public Boolean getSerializerUseMoneyFormat() {
            return serializerUseMoneyFormat;
        }

        /**
         * Sets serializer use money format.
         *
         * @param serializerUseMoneyFormat the serializer use money format
         */
        public void setSerializerUseMoneyFormat(Boolean serializerUseMoneyFormat) {
            this.serializerUseMoneyFormat = serializerUseMoneyFormat;
        }

        /**
         * Gets amount field name.
         *
         * @return the amount field name
         */
        public String getAmountFieldName() {
            return amountFieldName;
        }

        /**
         * Sets amount field name.
         *
         * @param amountFieldName the amount field name
         */
        public void setAmountFieldName(String amountFieldName) {
            this.amountFieldName = amountFieldName;
        }

        /**
         * Gets currency unit field name.
         *
         * @return the currency unit field name
         */
        public String getCurrencyUnitFieldName() {
            return currencyUnitFieldName;
        }

        /**
         * Sets currency unit field name.
         *
         * @param currencyUnitFieldName the currency unit field name
         */
        public void setCurrencyUnitFieldName(String currencyUnitFieldName) {
            this.currencyUnitFieldName = currencyUnitFieldName;
        }

        /**
         * Gets formatted field name.
         *
         * @return the formatted field name
         */
        public String getFormattedFieldName() {
            return formattedFieldName;
        }

        /**
         * Sets formatted field name.
         *
         * @param formattedFieldName the formatted field name
         */
        public void setFormattedFieldName(String formattedFieldName) {
            this.formattedFieldName = formattedFieldName;
        }
    }
}
