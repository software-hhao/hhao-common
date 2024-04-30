
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

import com.hhao.common.springboot.event.DefaultEventPublishErrorHandle;
import com.hhao.common.springboot.event.SpringEventBus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于Spring的事件总线配置
 *
 * @author Wang
 * @since 2022/2/25 22:28
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(SpringEventBusConfig.class)
@ConditionalOnProperty(prefix = "com.hhao.config.event",name = "enable",havingValue = "true",matchIfMissing = true)
public class SpringEventBusConfig implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher=applicationEventPublisher;
    }

    @Bean
    public SpringEventBus springEventBus(){
        return new SpringEventBus(applicationEventPublisher);
    }

    @Bean
    public DefaultEventPublishErrorHandle defaultSpringEventExceptionHandle(){
        return new DefaultEventPublishErrorHandle();
    }

}
