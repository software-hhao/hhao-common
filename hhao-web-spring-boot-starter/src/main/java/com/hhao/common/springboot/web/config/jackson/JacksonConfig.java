package com.hhao.common.springboot.web.config.jackson;

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

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.hhao.common.jackson.DefaultJacksonUtil;
import com.hhao.common.jackson.DefaultJacksonUtilBuilder;
import com.hhao.common.jackson.JacksonUtilFactory;
import com.hhao.common.springboot.jackson.SpringJacksonConfigProperties;
import com.hhao.common.springboot.jackson.SpringJacksonKeyType;
import com.hhao.common.springboot.web.config.AbstractBaseMvcConfig;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
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
@EnableConfigurationProperties({SpringJacksonConfigProperties.class})
@ConditionalOnProperty(prefix = "com.hhao.config.jackson", name = "enable", havingValue = "true", matchIfMissing = true)
public class JacksonConfig extends AbstractBaseMvcConfig {
    private SpringJacksonConfigProperties springJacksonConfigProperties = null;

    /**
     * Instantiates a new Jackson config.
     *
     * @param springJacksonConfigProperties the money jackson properties
     */
    public JacksonConfig(SpringJacksonConfigProperties springJacksonConfigProperties) {
        this.springJacksonConfigProperties = springJacksonConfigProperties;
    }

    /***
     * 这部份的代码是借助JacksonAutoConfiguration的Jackson2ObjectMapperBuilder生成ObjectMapper
     * 然后配置生成后的ObjectMapper 这样可以利用Spring的初始化配置功能，同时加入后期需要改进的功能，
     * 并使全局的JacksonUtil与Spring的ObjectMapper一致 注意，Bean的名称不能变
     *
     * @param builder the builder
     * @return the object mapper
     */
    @Primary
    @Bean("jacksonObjectMapper")
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        // 构建统一的JacksonUtilBuilder
        DefaultJacksonUtilBuilder defaultJacksonUtilBuilder = new DefaultJacksonUtilBuilder<ObjectMapper>(springJacksonConfigProperties);
        // 将统一的JacksonUtilBuilder中的自定义Modules加入到Jackson2ObjectMapperBuilder中
        builder.modules(defaultJacksonUtilBuilder.getModules());
        // 允许模块重复注册，以自定义的模块替换原模块
        builder.featuresToDisable(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS);
        // Jackson2ObjectMapperBuilder构建ObjectMapper
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        // 用统一的JacksonUtilBuilder配置ObjectMapper
        defaultJacksonUtilBuilder.configure(objectMapper);
        // MVC特有的扩展配置
        extendConfigObjectMapper(objectMapper);
        // 保存到JacksonUtilFactory中，键值为SPRING_MVC_DEFAULT
        JacksonUtilFactory.addJsonUtil(SpringJacksonKeyType.SPRING_DEFAULT, new DefaultJacksonUtil(objectMapper));

        // 为全局返回创建ObjectMapper
        buildObjectMapperForJsonReturn(objectMapper);
        return objectMapper;
    }

    /**
     * 在jacksonObjectMapper的基础配制上为JSON返回创建的ObjectMapper
     * 可以为全局返回设置特殊的配置
     *
     * @param objectMapper
     * @return
     */
    protected ObjectMapper buildObjectMapperForJsonReturn(ObjectMapper objectMapper) {
        ObjectMapper returnObjectMapper = objectMapper.copy();
        // 此处可以为全局返回封装的ObjectMapper进行配置
        // 。。。。。。
        // 保存到JacksonUtilFactory中，键值为SPRING_RETURN
        JacksonUtilFactory.addJsonUtil(SpringJacksonKeyType.SPRING_RETURN, new DefaultJacksonUtil(objectMapper));
        return returnObjectMapper;
    }

    /**
     * Xml mapper xml mapper.
     *
     * @param builder the builder
     * @return the xml mapper
     */
    @Bean("xmlMapper")
    public XmlMapper xmlMapper(Jackson2ObjectMapperBuilder builder) {
        DefaultJacksonUtilBuilder defaultJacksonUtilBuilder = new DefaultJacksonUtilBuilder<ObjectMapper>(springJacksonConfigProperties);
        // 将统一的JacksonUtilBuilder中的自定义Modules加入到Jackson2ObjectMapperBuilder中
        builder.modules(defaultJacksonUtilBuilder.getModules());
        // 允许模块重复注册，以自定义的模块替换原模块
        builder.featuresToDisable(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS);
        // Jackson2ObjectMapperBuilder构建ObjectMapper
        XmlMapper xmlMapper = builder.createXmlMapper(true).build();
        // 用统一的JacksonUtilBuilder配置ObjectMapper
        defaultJacksonUtilBuilder.configure(xmlMapper);
        // MVC特有的扩展配置
        extendConfigObjectMapper(xmlMapper);
        // 保存到JacksonUtilFactory中，键值为SPRING_MVC_DEFAULT
        JacksonUtilFactory.addXmlUtil(SpringJacksonKeyType.SPRING_DEFAULT, new DefaultJacksonUtil(xmlMapper));

        // 为全局返回创建XmlMapper
        buildXmlMapperForJsonReturn(xmlMapper);
        return xmlMapper;
    }

    /**
     * 在jacksonObjectMapper的基础配制上为XML返回创建的ObjectMapper
     * 可以为全局返回设置特殊的配置
     *
     * @param xmlMapper
     * @return
     */
    protected XmlMapper buildXmlMapperForJsonReturn(XmlMapper xmlMapper) {
        XmlMapper returnXmlMapper = xmlMapper.copy();
        // 此处可以为全局返回封装的ObjectMapper进行配置
        // 。。。。。。
        // 保存到JacksonUtilFactory中，键值为SPRING_RETURN
        JacksonUtilFactory.addXmlUtil(SpringJacksonKeyType.SPRING_RETURN, new DefaultJacksonUtil(returnXmlMapper));
        return returnXmlMapper;
    }

    /**
     * 关于Spring MVC特定的扩展配置
     *
     * @param objectMapper
     */
    protected void extendConfigObjectMapper(ObjectMapper objectMapper) {
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        int index = 0;
        for (HttpMessageConverter converter : converters) {
            if (converter instanceof StringHttpMessageConverter) {
                break;
            }
            index++;
        }
        //这两个要加在StringHttpMessageConverter之前，否则当String类型的application/json将会被StringHttpMessageConverter先行转换
        converters.add(index, new MappingJackson2XmlHttpMessageConverter(JacksonUtilFactory.getXmlUtil(SpringJacksonKeyType.SPRING_DEFAULT).getObjectMapper()));
        converters.add(index, new MappingJackson2HttpMessageConverter(JacksonUtilFactory.getJsonUtil(SpringJacksonKeyType.SPRING_DEFAULT).getObjectMapper()));
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

    }

}
