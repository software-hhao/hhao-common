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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.Duration;

/**
 * 原配置文件MessageSourceAutoConfiguration
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties
@ConditionalOnMissingBean(MessageSourceConfig.class)
@ConditionalOnProperty(prefix = "com.hhao.config.message-source",name = "enable",havingValue = "true",matchIfMissing = true)
public class MessageSourceConfig extends AbstractBaseConfig {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(MessageSourceConfig.class);
    private static final String BASE_FOLDER="i18n";
    private static final String BASE_NAME="messages";
    /**
     * classpath:/i18n/messages
     */
    private static final String DEFAULT_BASENAME=ResourceUtils.CLASSPATH_URL_PREFIX + BASE_FOLDER + File.separator + BASE_NAME;
    /**
     * file:应用路径/i18n/messages
     */
    private static final String EXTEND_DEFAULT_BASENAME=ResourceUtils.FILE_URL_PREFIX + System.getProperty("user.dir") + File.separator + BASE_FOLDER + File.separator + BASE_NAME;
    private static final String PREFIX_CLASSPATH="classpath(/*?):";

    private static final Resource[] NO_RESOURCES = {};

    /**
     * Message source properties message source properties.
     *
     * @return the message source properties
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.messages")
    public MessageSourceProperties messageSourceProperties() {
        MessageSourceProperties messageSourceProperties=new MessageSourceProperties();
        messageSourceProperties.setBasename(DEFAULT_BASENAME);

        return messageSourceProperties;
    }

    /**
     * Message source message source.
     *
     * @param properties the properties
     * @return the message source
     */
    @Bean
    public MessageSource messageSource(MessageSourceProperties properties) {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        if (StringUtils.hasText(properties.getBasename())) {
            messageSource.setBasenames(getBasename(properties));
            messageSource.addBasenames(getExtendBasename());
        }
        if (properties.getEncoding() != null) {
            messageSource.setDefaultEncoding(properties.getEncoding().name());
        }
        messageSource.setFallbackToSystemLocale(properties.isFallbackToSystemLocale());
        Duration cacheDuration = properties.getCacheDuration();
        if (cacheDuration != null) {
            messageSource.setCacheMillis(cacheDuration.toMillis());
        }
        messageSource.setAlwaysUseMessageFormat(properties.isAlwaysUseMessageFormat());
        messageSource.setUseCodeAsDefaultMessage(properties.isUseCodeAsDefaultMessage());

        configureMessageSource(messageSource);
        return messageSource;
    }

    private String [] getBasename(MessageSourceProperties properties){
        String [] basename=StringUtils.commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(properties.getBasename()));
        for(int i=0;i<basename.length;i++){
            //如果不是classpath:开头的，则加上
            if (!StringUtils.startsWithIgnoreCase(basename[i],PREFIX_CLASSPATH)){
                basename[i]="classpath:" + basename[i];
                for (Resource resource : getResources(getApplicationContext().getClassLoader(), basename[i])) {
                    if (resource.exists()) {
                        logger.info("find propertie files:{}",basename[i]);
                    }else{
                        logger.info("not find propertie files:{}",basename[i]);
                    }
                }
            }
        }
        return basename;
    }

    private Resource[] getResources(ClassLoader classLoader, String name) {
        try {
            return new PathMatchingResourcePatternResolver(classLoader)
                    .getResources(name+ ".properties");
        }
        catch (Exception ex) {
            return NO_RESOURCES;
        }
    }

    /**
     * Configure message source message source.
     *
     * @param messageSource the message source
     * @return the message source
     */
    protected MessageSource configureMessageSource(AbstractResourceBasedMessageSource messageSource) {
        return messageSource;
    }

    /**
     * 添加扩展的basename
     * 一个是在类路径下，一个是外部路径下，用于外部扩展，如nacos
     *
     * @return
     */
    private String [] getExtendBasename(){
        String [] basename=new String[]{
                DEFAULT_BASENAME,
                EXTEND_DEFAULT_BASENAME
        };
        return basename;
    }
}
