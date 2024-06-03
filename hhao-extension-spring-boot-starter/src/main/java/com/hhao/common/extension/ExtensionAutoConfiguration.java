/*
 * Copyright 2018-2022 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        https://www.gnu.org/licenses/gpl-3.0.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.hhao.common.extension;

import com.hhao.common.extension.annotation.Extension;
import com.hhao.common.extension.aspect.ExtensionPointAutowiredAnnotationBeanPostProcessorRegister;
import com.hhao.common.extension.executor.DefaultExtensionExecutor;
import com.hhao.common.extension.executor.ExtensionExecutorUtil;
import com.hhao.common.extension.model.CombinedReturnBuilder;
import com.hhao.common.extension.model.ExtensionPoint;
import com.hhao.common.extension.register.ExtensionRegister;
import com.hhao.common.extension.register.ExtensionRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.util.Map;

/**
 * Spring Boot配置扩展点的类
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration
@Import(ExtensionPointAutowiredAnnotationBeanPostProcessorRegister.class)
public class ExtensionAutoConfiguration implements ApplicationContextAware, ApplicationRunner, Ordered {
    private ApplicationContext applicationContext;
    private ExtensionRegister extensionRegister;

    @Value("${com.hhao.config.extension.isNotFoundThrowError:false}")
    private boolean isNotFoundThrowError=false;

    /**
     * Repository extension repository.
     *
     * @return the extension repository
     */
    @Bean
    @ConditionalOnMissingBean(ExtensionRepository.class)
    public ExtensionRepository repository() {
        return new ExtensionRepository();
    }

    /**
     * Executor extension executor.
     *
     * @param repository the repository
     * @return the extension executor
     */
    @Bean
    @ConditionalOnMissingBean(DefaultExtensionExecutor.class)
    public DefaultExtensionExecutor executor(ExtensionRepository repository) {
        DefaultExtensionExecutor executor=new DefaultExtensionExecutor(repository,isNotFoundThrowError);
        ExtensionExecutorUtil.setExecutor(executor);
        return executor;
    }

    /**
     * Register extension register.
     *
     * @param repository the repository
     * @return the extension register
     */
    @Bean
    @ConditionalOnMissingBean(ExtensionRegister.class)
    public ExtensionRegister register(ExtensionRepository repository) {
        extensionRegister=new ExtensionRegister(repository);
        return extensionRegister;
    }

    @Bean
    @ConditionalOnMissingBean
    public CombinedReturnBuilder combinedReturnBuilder(){
        return new CombinedReturnBuilder.DefaultCombinedReturnBuilder().builder();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    /**
     * 注册扩展点实现Bean
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, Object> extensionBeans = applicationContext.getBeansWithAnnotation(Extension.class);
        extensionBeans.values().forEach(
                extension -> extensionRegister.doRegistration((ExtensionPoint) extension)
        );
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
