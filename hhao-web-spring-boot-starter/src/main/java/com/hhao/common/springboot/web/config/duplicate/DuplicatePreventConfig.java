package com.hhao.common.springboot.web.config.duplicate;

import com.hhao.common.springboot.duplicate.DuplicatePreventInterceptorHandler;
import com.hhao.common.springboot.duplicate.DuplicatePreventionManager;
import com.hhao.common.springboot.web.config.AbstractBaseMvcConfig;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.Order;

/**
 * 防重配置
 */
@Configuration
@ConditionalOnMissingBean(DuplicatePreventConfig.class)
@Role(RootBeanDefinition.ROLE_INFRASTRUCTURE)
@ConditionalOnProperty(prefix = "com.hhao.config.aop.duplicate-prevent",name = "enable",havingValue = "true",matchIfMissing = true)
public class DuplicatePreventConfig extends AbstractBaseMvcConfig {

    @Bean
    @Role(RootBeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean(DuplicatePreventionManager.class)
    @Order
    public DuplicatePreventionManager duplicatePreventionManager(){
        return new HttpSessionDuplicatePreventionManager();
    }

    @Bean
    @Role(RootBeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean(DuplicatePreventInterceptorHandler.class)
    public DuplicatePreventInterceptorHandler duplicatePreventInterceptorHandler(DuplicatePreventionManager duplicatePreventionManager){
        return new DuplicatePreventInterceptorHandler(duplicatePreventionManager);
    }
}
