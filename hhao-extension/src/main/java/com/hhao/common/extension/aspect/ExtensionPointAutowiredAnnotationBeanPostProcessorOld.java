
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

import com.google.common.collect.Lists;
import com.hhao.common.extension.annotation.ExtensionPointAutowired;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AutowiredAnnotationBeanPostProcessor
 *
 * @author Wang
 * @since 2022/3/11 19:35
 */
@Deprecated
public class ExtensionPointAutowiredAnnotationBeanPostProcessorOld implements SmartInstantiationAwareBeanPostProcessor,
        MergedBeanDefinitionPostProcessor, PriorityOrdered, BeanFactoryAware, ApplicationContextAware, DisposableBean {
    /**
     * self bean name
     */
    public static final String BEAN_NAME = "extensionPointAutowiredAnnotationBeanPostProcessor";
    /**
     * 需要自动注入的注解类型
     */
    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>(4);
    /**
     * key：beanName或className
     * value：要注入的元素据
     */
    private final Map<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<>(256);
    /**
     * key：ExtensionPointAutowiredBean + 扩展点接口名
     * value：扩展点接口实现类beanNames
     */
    private final Map<String, List<String>> referenceBeansMap = new ConcurrentHashMap<>();
    /**
     * bean工厂
     */
    private ConfigurableListableBeanFactory beanFactory;
    /**
     * BeanDefinition注册中心
     */
    private BeanDefinitionRegistry beanDefinitionRegistry;

    public ExtensionPointAutowiredAnnotationBeanPostProcessorOld() {
        autowiredAnnotationTypes.add(ExtensionPointAutowired.class);
    }

    @Override
    public void setBeanFactory(@Nullable BeanFactory beanFactory) throws BeansException {
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new IllegalArgumentException("AutowiredAnnotationBeanPostProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        InjectionMetadata metadata = findAutowiringMetadata(beanName, beanType, null);
        metadata.checkConfigMembers(beanDefinition);
        if (isExtensionPointAutowiredBean(beanDefinition)) {
            // 将所有属性置为可选
            List<PropertyValue> propertyValues = beanDefinition.getPropertyValues().getPropertyValueList();
            for (PropertyValue propertyValue : propertyValues) {
                propertyValue.setOptional(true);
            }
        }
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        InjectionMetadata metadata = findAutowiringMetadata(beanName, bean.getClass(), pvs);
        try {
            metadata.inject(bean, beanName, pvs);
        } catch (BeanCreationException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
        }
        return pvs;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    /**
     * 获取bean需要注入的元素据（属性或方法，这里只处理属性）
     */
    private InjectionMetadata findAutowiringMetadata(String beanName, Class<?> clazz, @Nullable PropertyValues pvs) {
        // Fall back to class name as cache key, for backwards compatibility with custom callers.
        String cacheKey = (StringUtils.hasLength(beanName) ? beanName : clazz.getName());
        // Quick check on the concurrent map first, with minimal locking.
        InjectionMetadata metadata = this.injectionMetadataCache.get(cacheKey);
        if (InjectionMetadata.needsRefresh(metadata, clazz)) {
            synchronized (this.injectionMetadataCache) {
                metadata = this.injectionMetadataCache.get(cacheKey);
                if (InjectionMetadata.needsRefresh(metadata, clazz)) {
                    if (metadata != null) {
                        metadata.clear(pvs);
                    }
                    metadata = buildAutowiringMetadata(clazz);
                    this.injectionMetadataCache.put(cacheKey, metadata);
                }
            }
        }
        return metadata;
    }

    /**
     * 构建注入元素据，这里只构建属性元素据，不处理方法
     */
    private InjectionMetadata buildAutowiringMetadata(final Class<?> clazz) {
        if (!AnnotationUtils.isCandidateClass(clazz, this.autowiredAnnotationTypes)) {
            return InjectionMetadata.EMPTY;
        }

        List<InjectionMetadata.InjectedElement> elements = new ArrayList<>();
        Class<?> targetClass = clazz;

        do {
            final List<InjectionMetadata.InjectedElement> currElements = new ArrayList<>();

            ReflectionUtils.doWithLocalFields(targetClass, field -> {
                MergedAnnotation<?> ann = findAutowiredAnnotation(field);
                if (ann != null) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        return;
                    }
                    currElements.add(new AutowiredFieldElement(field));
                }
            });

            elements.addAll(0, currElements);
            targetClass = targetClass.getSuperclass();
        }
        while (targetClass != null && targetClass != Object.class);

        return InjectionMetadata.forElements(elements, clazz);
    }

    /**
     * 过滤出有效的注解
     */
    @Nullable
    private MergedAnnotation<?> findAutowiredAnnotation(AccessibleObject ao) {
        MergedAnnotations annotations = MergedAnnotations.from(ao);
        for (Class<? extends Annotation> type : this.autowiredAnnotationTypes) {
            MergedAnnotation<?> annotation = annotations.get(type);
            if (annotation.isPresent()) {
                return annotation;
            }
        }
        return null;
    }

    /**
     * 注册ExtensionPointAutowiredBean
     */
    private String registerExtensionPointAutowiredBean(String propertyName, Class<?> injectedType) {
        beanFactory.getBeansOfType(injectedType);

        String extensionPointAutowiredBeanName = propertyName;

        // generate reference key
        String referenceKey = ExtensionPointAutowiredBean.class.getName() + ":" + injectedType;

        // find ExtensionPointAutowired bean name by reference key
        List<String> registeredAutowiredBeans = referenceBeansMap.computeIfAbsent(referenceKey, key -> Lists.newArrayList());
        if (registeredAutowiredBeans.size() > 0) {
            // found same name and reference key
            if (registeredAutowiredBeans.contains(extensionPointAutowiredBeanName)) {
                return extensionPointAutowiredBeanName;
            }
        }

        //check bean definition
        if (beanDefinitionRegistry.containsBeanDefinition(extensionPointAutowiredBeanName)) {
            BeanDefinition prevBeanDefinition = beanDefinitionRegistry.getBeanDefinition(extensionPointAutowiredBeanName);

            if (isExtensionPointAutowiredBean(prevBeanDefinition)) {
                //check reference key
                String prevReferenceKey = ExtensionPointAutowiredBean.class.getName() + ":" + prevBeanDefinition.getAttribute("interfaceName");
                if (Objects.equals(prevReferenceKey, referenceKey)) {
                    //found matched ExtensionPointAutowired bean, ignore register
                    return extensionPointAutowiredBeanName;
                }
                //get interfaceName from attribute
                Assert.notNull(prevBeanDefinition, "The interface class of ExtensionPointAutowiredBean is not initialized");
            }

            // the prev bean type is different, rename the new ExtensionPointAutowiredBean
            int index = 2;
            String newExtensionPointAutowiredBeanName = null;
            while (newExtensionPointAutowiredBeanName == null
                    || beanDefinitionRegistry.containsBeanDefinition(newExtensionPointAutowiredBeanName)) {
                newExtensionPointAutowiredBeanName = extensionPointAutowiredBeanName + "#" + index;
                index++;
            }
            extensionPointAutowiredBeanName = newExtensionPointAutowiredBeanName;
        }

        // If registered matched reference before, just register alias
        if (registeredAutowiredBeans.size() > 0) {
            beanDefinitionRegistry.registerAlias(registeredAutowiredBeans.get(0), extensionPointAutowiredBeanName);
            referenceBeansMap.computeIfAbsent(referenceKey, key -> Lists.newArrayList()).add(extensionPointAutowiredBeanName);
            return extensionPointAutowiredBeanName;
        }

        AbstractBeanDefinition beanDefinition = buildExtensionPointAutowiredBeanDefinition(injectedType);
        beanDefinitionRegistry.registerBeanDefinition(extensionPointAutowiredBeanName, beanDefinition);

        referenceBeansMap.computeIfAbsent(referenceKey, key -> Lists.newArrayList()).add(extensionPointAutowiredBeanName);
        return extensionPointAutowiredBeanName;
    }

    /**
     * 构建ExtensionPointAutowiredBean对应的BeanDefinition
     */
    private AbstractBeanDefinition buildExtensionPointAutowiredBeanDefinition(Class<?> injectedType) {
        // Register the ExtensionPointAutowiredBean definition to the beanFactory
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClassName(ExtensionPointAutowiredBean.class.getName());

        // create decorated definition for reference bean, Avoid being instantiated when getting the beanType of ExtensionPointAutowiredBean
        // see org.springframework.beans.factory.support.AbstractBeanFactory#getTypeForFactoryBean()
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(injectedType);
        beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        GenericBeanDefinition targetDefinition = new GenericBeanDefinition();
        targetDefinition.setBeanClass(injectedType);
        String code = getPropertyValue(beanDefinition.getPropertyValues(), "code");

        beanDefinition.setDecoratedDefinition(new BeanDefinitionHolder(targetDefinition, code + "_decorated"));
        // signal object type since Spring 5.2
        beanDefinition.setAttribute("factoryBeanObjectType", injectedType);

        return beanDefinition;
    }

    /**
     * 获取属性值
     */
    @SuppressWarnings("unchecked")
    private <T> T getPropertyValue(PropertyValues pvs, String propertyName) {
        PropertyValue pv = pvs.getPropertyValue(propertyName);
        Object val = pv != null ? pv.getValue() : null;
        if (val instanceof TypedStringValue) {
            TypedStringValue typedString = (TypedStringValue) val;
            return (T) typedString.getValue();
        }
        return (T) val;
    }

    /**
     * 判断是否是扩展点注解注入的bean
     */
    private boolean isExtensionPointAutowiredBean(BeanDefinition beanDefinition) {
        return ExtensionPointAutowiredBean.class.getName().equals(beanDefinition.getBeanClassName());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
    }

    @Override
    public void destroy() {
        injectionMetadataCache.clear();
        referenceBeansMap.clear();
    }

    /**
     * 注入属性元素
     */
    private class AutowiredFieldElement extends InjectionMetadata.InjectedElement {

        public AutowiredFieldElement(Field field) {
            super(field, null);
        }

        @Override
        protected void inject(Object bean, @Nullable String beanName, @Nullable PropertyValues pvs) throws Throwable {
            if (!isField) {
                return;
            }
            // 注册ExtensionPointAutowiredBean，返回需要注入的beanName
            String injectBeanName = registerExtensionPointAutowiredBean(getPropertyName(), getInjectedType());
            // 通过beanName获取要注入的bean对象
            Object value = beanFactory.getBean(injectBeanName);

            Field field = (Field) this.member;
            ReflectionUtils.makeAccessible(field);
            field.set(bean, value);
        }

        /**
         * 获取注入类型
         */
        public Class<?> getInjectedType() {
            if (!isField) {
                return null;
            }
            return ((Field) this.member).getType();
        }

        /**
         * 获取注入属性名
         */
        public String getPropertyName() {
            if (!isField) {
                return null;
            }
            return ((Field) this.member).getName();
        }
    }
}
