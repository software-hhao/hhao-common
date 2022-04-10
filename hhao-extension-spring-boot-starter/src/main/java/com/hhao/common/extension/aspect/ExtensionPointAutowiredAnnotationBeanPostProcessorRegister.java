
/*
 * Copyright 2018-2022 WangSheng.
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

package com.hhao.common.extension.aspect;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 注册：ExtensionPointAutowiredAnnotationBeanPostProcessor
 *
 * @author Wang
 * @since 2022/3/11 22:08
 */

public class ExtensionPointAutowiredAnnotationBeanPostProcessorRegister implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 注册ExtensionPointAutowired注解解析器
        registerInfrastructureBean(registry, ExtensionPointAutowiredAnnotationBeanPostProcessor.BEAN_NAME, ExtensionPointAutowiredAnnotationBeanPostProcessor.class);
    }

    /**
     * 注册bean
     */
    private void registerInfrastructureBean(BeanDefinitionRegistry beanDefinitionRegistry, String beanName, Class<?> beanType) {
        if (beanDefinitionRegistry.containsBeanDefinition(beanName)) {
            return;
        }
        RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
        beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        //是否单例，再测试
        //beanDefinition.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
        beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
    }
}
