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
import com.hhao.common.money.jackson.MoneyProperties;
import org.springframework.beans.factory.annotation.Value;
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
 * Spring Boot ?????????Jackson??????????????????:
 * org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
 * WebMvcConfigurationSupport
 * JacksonHttpMessageConvertersConfiguration
 * ???Spring???????????????????????????????????????????????????
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

    @Value("${com.hhao.config.jackson.dataTimeErrorThrow:true}")
    private Boolean dataTimeErrorThrow;
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
     * ???????????????????????????JacksonAutoConfiguration???Jackson2ObjectMapperBuilder??????ObjectMapper ????????????????????????ObjectMapper ??????????????????Spring????????????????????????????????????????????????????????????????????????????????????JacksonUtil???Spring???ObjectMapper?????? ?????????Bean??????????????????
     * @param builder the builder
     * @return the object mapper
     */
    @Primary
    @Bean("jacksonObjectMapper")
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        builder.featuresToDisable(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS);
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        //???????????????????????????????????????????????????????????????
        //objectMapper.configure(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS, false);

        //???????????????????????????????????????????????????????????????
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
                .init(dataTimeErrorThrow,new MoneyProperties(moneyJacksonProperties.getErrorThrowException(),moneyJacksonProperties.deserializerUseMoneyFormat,moneyJacksonProperties.getSerializerUseMoneyFormat()))
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
        //???????????????????????????????????????????????????????????????
        //????????????????????????????????????builder.featuresToEnable(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS);
        //xmlMapper.configure(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS, false);
        //???????????????????????????????????????????????????????????????

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
                .init(dataTimeErrorThrow,new MoneyProperties(moneyJacksonProperties.getErrorThrowException(),moneyJacksonProperties.deserializerUseMoneyFormat,moneyJacksonProperties.getSerializerUseMoneyFormat()))
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
        //??????????????????StringHttpMessageConverter??????????????????String?????????application/json?????????StringHttpMessageConverter????????????
        converters.add(index,new MappingJackson2XmlHttpMessageConverter(JacksonUtilFactory.getXmlUtil().getObjectMapper()));
        converters.add(index,new MappingJackson2HttpMessageConverter(JacksonUtilFactory.getJsonUtil().getObjectMapper()));
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

    }



    /**
     * ??????Money Jackson??????????????????
     */
    @ConfigurationProperties("com.hhao.config.jackson")
    public static class MoneyJacksonProperties {
        //?????????????????????????????????@MoneyFormat??????????????????
        private Boolean deserializerUseMoneyFormat = false;
        //??????????????????????????????@MoneyFormat??????????????????
        private Boolean serializerUseMoneyFormat = true;
        //??????????????????????????????
        private Boolean errorThrowException=true;

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

        public Boolean getErrorThrowException() {
            return errorThrowException;
        }

        public void setErrorThrowException(Boolean errorThrowException) {
            this.errorThrowException = errorThrowException;
        }
    }
}
