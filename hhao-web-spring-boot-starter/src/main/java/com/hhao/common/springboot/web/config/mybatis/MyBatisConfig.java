
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

package com.hhao.common.springboot.web.config.mybatis;

import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.mybatis.config.MyBatisAutoConfig;
import com.hhao.common.springboot.web.config.AbstractBaseMvcConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractResourceBasedMessageSource;

/**
 * 配置异常转换
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(MyBatisConfig.class)
@ConditionalOnClass(MyBatisAutoConfig.class)
public class MyBatisConfig extends AbstractBaseMvcConfig {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(MyBatisConfig.class);
    private final String errorMessageSource = ("classpath:" + MyBatisExceptionTransfer.class.getPackageName() + "/messages").replaceAll("[.]", "/");

    /**
     * Instantiates a new My batis config.
     *
     * @param messageSource the message source
     */
    @Autowired
    public MyBatisConfig(MessageSource messageSource) {
        //加载错误信息资源文件
        addMessageSource(messageSource, errorMessageSource);
    }

    /**
     * My batis exception transfer my batis exception transfer.
     *
     * @return the my batis exception transfer
     */
    @Bean
    public MyBatisExceptionTransfer myBatisExceptionTransfer() {
        return new MyBatisExceptionTransfer();
    }

    /***
     * 添加错误信息的资源文件
     * @param messageSource the message source
     * @param message the message
     */
    public void addMessageSource(MessageSource messageSource, String message) {
        try {
            if (messageSource instanceof AbstractResourceBasedMessageSource) {
                ((AbstractResourceBasedMessageSource) messageSource).addBasenames(message);
            } else {
                logger.info("can't find ResourceBasedMessageSource");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
