
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

package com.hhao.common.extension.aspect;/*
 * Copyright 2020-2021 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.common.collect.Lists;
import com.hhao.common.extension.annotation.ExtensionPointAutowired;
import com.hhao.common.extension.model.ExtensionPoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.*;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义注解自动注入的实现，参考：
 * AutowiredAnnotationBeanPostProcessor
 *
 * 本意：在实现扩展中，采用自动注入，根据调用方法的输入参数来判断调用对象
 * 现实：这种方式与扩展的主义相背，单行为调用可行，多行为调用出现重选对象的情况，性能差，也不适合语义，故写一半放弃了
 * 不过，这种自动注入的方式可以留用参考，故先保留这个包下的代码
 *
 * @author Wang
 * @since 2022/3/12 22:51
 */
@Deprecated
public class ExtensionPointAutowiredAnnotationBeanPostProcessor  implements SmartInstantiationAwareBeanPostProcessor,
        MergedBeanDefinitionPostProcessor, PriorityOrdered, BeanFactoryAware, ApplicationContextAware, DisposableBean {

    /**
     * 以下是修订部份
     */
    public static final String BEAN_NAME = "extensionPointAutowiredAnnotationBeanPostProcessor";
    /**
     * key：ExtensionPointAutowiredBean + 扩展点接口名
     * value：扩展点接口实现类beanNames
     */
    private final Map<String, List<String>> referenceBeansMap = new ConcurrentHashMap<>();
    /**
     * BeanDefinition注册中心
     */
    private BeanDefinitionRegistry beanDefinitionRegistry;

    private int order = Ordered.LOWEST_PRECEDENCE;

    /**
     * 以下未修订
     */
    protected final Log logger = LogFactory.getLog(getClass());

    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>(4);

    private String requiredParameterName = "required";

    private boolean requiredParameterValue = true;



    @Nullable
    private ConfigurableListableBeanFactory beanFactory;

    private final Set<String> lookupMethodsChecked = Collections.newSetFromMap(new ConcurrentHashMap<>(256));

    private final Map<Class<?>, Constructor<?>[]> candidateConstructorsCache = new ConcurrentHashMap<>(256);

    private final Map<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<>(256);

    /**
     * 修订：
     *
     */
    @SuppressWarnings("unchecked")
    public ExtensionPointAutowiredAnnotationBeanPostProcessor() {
        autowiredAnnotationTypes.add(ExtensionPointAutowired.class);
    }

    public void setAutowiredAnnotationType(Class<? extends Annotation> autowiredAnnotationType) {
        Assert.notNull(autowiredAnnotationType, "'autowiredAnnotationType' must not be null");
        this.autowiredAnnotationTypes.clear();
        this.autowiredAnnotationTypes.add(autowiredAnnotationType);
    }


    public void setAutowiredAnnotationTypes(Set<Class<? extends Annotation>> autowiredAnnotationTypes) {
        Assert.notEmpty(autowiredAnnotationTypes, "'autowiredAnnotationTypes' must not be empty");
        this.autowiredAnnotationTypes.clear();
        this.autowiredAnnotationTypes.addAll(autowiredAnnotationTypes);
    }

    /**
     * Set the name of an attribute of the annotation that specifies whether it is required.
     * @see #setRequiredParameterValue(boolean)
     */
    public void setRequiredParameterName(String requiredParameterName) {
        this.requiredParameterName = requiredParameterName;
    }

    /**
     * Set the boolean value that marks a dependency as required.
     * <p>For example if using 'required=true' (the default), this value should be
     * {@code true}; but if using 'optional=false', this value should be {@code false}.
     * @see #setRequiredParameterName(String)
     */
    public void setRequiredParameterValue(boolean requiredParameterValue) {
        this.requiredParameterValue = requiredParameterValue;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new IllegalArgumentException(
                    "AutowiredAnnotationBeanPostProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }


    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        InjectionMetadata metadata = findAutowiringMetadata(beanName, beanType, null);
        metadata.checkConfigMembers(beanDefinition);
    }

    @Override
    public void resetBeanDefinition(String beanName) {
        this.lookupMethodsChecked.remove(beanName);
        this.injectionMetadataCache.remove(beanName);
    }

    @Override
    @Nullable
    public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, final String beanName)
            throws BeanCreationException {

        // Let's check for lookup methods here...
        if (!this.lookupMethodsChecked.contains(beanName)) {
            if (AnnotationUtils.isCandidateClass(beanClass, Lookup.class)) {
                try {
                    Class<?> targetClass = beanClass;
                    do {
                        ReflectionUtils.doWithLocalMethods(targetClass, method -> {
                            Lookup lookup = method.getAnnotation(Lookup.class);
                            if (lookup != null) {
                                Assert.state(this.beanFactory != null, "No BeanFactory available");
                                LookupOverride override = new LookupOverride(method, lookup.value());
                                try {
                                    RootBeanDefinition mbd = (RootBeanDefinition)
                                            this.beanFactory.getMergedBeanDefinition(beanName);
                                    mbd.getMethodOverrides().addOverride(override);
                                }
                                catch (NoSuchBeanDefinitionException ex) {
                                    throw new BeanCreationException(beanName,
                                            "Cannot apply @Lookup to beans without corresponding bean definition");
                                }
                            }
                        });
                        targetClass = targetClass.getSuperclass();
                    }
                    while (targetClass != null && targetClass != Object.class);

                }
                catch (IllegalStateException ex) {
                    throw new BeanCreationException(beanName, "Lookup method resolution failed", ex);
                }
            }
            this.lookupMethodsChecked.add(beanName);
        }

        // Quick check on the concurrent map first, with minimal locking.
        Constructor<?>[] candidateConstructors = this.candidateConstructorsCache.get(beanClass);
        if (candidateConstructors == null) {
            // Fully synchronized resolution now...
            synchronized (this.candidateConstructorsCache) {
                candidateConstructors = this.candidateConstructorsCache.get(beanClass);
                if (candidateConstructors == null) {
                    Constructor<?>[] rawCandidates;
                    try {
                        rawCandidates = beanClass.getDeclaredConstructors();
                    }
                    catch (Throwable ex) {
                        throw new BeanCreationException(beanName,
                                "Resolution of declared constructors on bean Class [" + beanClass.getName() +
                                        "] from ClassLoader [" + beanClass.getClassLoader() + "] failed", ex);
                    }
                    List<Constructor<?>> candidates = new ArrayList<>(rawCandidates.length);
                    Constructor<?> requiredConstructor = null;
                    Constructor<?> defaultConstructor = null;
                    Constructor<?> primaryConstructor = BeanUtils.findPrimaryConstructor(beanClass);
                    int nonSyntheticConstructors = 0;
                    for (Constructor<?> candidate : rawCandidates) {
                        if (!candidate.isSynthetic()) {
                            nonSyntheticConstructors++;
                        }
                        else if (primaryConstructor != null) {
                            continue;
                        }
                        MergedAnnotation<?> ann = findAutowiredAnnotation(candidate);
                        if (ann == null) {
                            Class<?> userClass = ClassUtils.getUserClass(beanClass);
                            if (userClass != beanClass) {
                                try {
                                    Constructor<?> superCtor =
                                            userClass.getDeclaredConstructor(candidate.getParameterTypes());
                                    ann = findAutowiredAnnotation(superCtor);
                                }
                                catch (NoSuchMethodException ex) {
                                    // Simply proceed, no equivalent superclass constructor found...
                                }
                            }
                        }
                        if (ann != null) {
                            if (requiredConstructor != null) {
                                throw new BeanCreationException(beanName,
                                        "Invalid autowire-marked constructor: " + candidate +
                                                ". Found constructor with 'required' Autowired annotation already: " +
                                                requiredConstructor);
                            }
                            boolean required = determineRequiredStatus(ann);
                            if (required) {
                                if (!candidates.isEmpty()) {
                                    throw new BeanCreationException(beanName,
                                            "Invalid autowire-marked constructors: " + candidates +
                                                    ". Found constructor with 'required' Autowired annotation: " +
                                                    candidate);
                                }
                                requiredConstructor = candidate;
                            }
                            candidates.add(candidate);
                        }
                        else if (candidate.getParameterCount() == 0) {
                            defaultConstructor = candidate;
                        }
                    }
                    if (!candidates.isEmpty()) {
                        // Add default constructor to list of optional constructors, as fallback.
                        if (requiredConstructor == null) {
                            if (defaultConstructor != null) {
                                candidates.add(defaultConstructor);
                            }
                            else if (candidates.size() == 1 && logger.isInfoEnabled()) {
                                logger.info("Inconsistent constructor declaration on bean with name '" + beanName +
                                        "': single autowire-marked constructor flagged as optional - " +
                                        "this constructor is effectively required since there is no " +
                                        "default constructor to fall back to: " + candidates.get(0));
                            }
                        }
                        candidateConstructors = candidates.toArray(new Constructor<?>[0]);
                    }
                    else if (rawCandidates.length == 1 && rawCandidates[0].getParameterCount() > 0) {
                        candidateConstructors = new Constructor<?>[] {rawCandidates[0]};
                    }
                    else if (nonSyntheticConstructors == 2 && primaryConstructor != null &&
                            defaultConstructor != null && !primaryConstructor.equals(defaultConstructor)) {
                        candidateConstructors = new Constructor<?>[] {primaryConstructor, defaultConstructor};
                    }
                    else if (nonSyntheticConstructors == 1 && primaryConstructor != null) {
                        candidateConstructors = new Constructor<?>[] {primaryConstructor};
                    }
                    else {
                        candidateConstructors = new Constructor<?>[0];
                    }
                    this.candidateConstructorsCache.put(beanClass, candidateConstructors);
                }
            }
        }
        return (candidateConstructors.length > 0 ? candidateConstructors : null);
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) {
        InjectionMetadata metadata = findAutowiringMetadata(beanName, bean.getClass(), pvs);
        try {
            metadata.inject(bean, beanName, pvs);
        }
        catch (BeanCreationException ex) {
            throw ex;
        }
        catch (Throwable ex) {
            throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
        }
        return pvs;
    }

    @Deprecated
    @Override
    public PropertyValues postProcessPropertyValues(
            PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) {

        return postProcessProperties(pvs, bean, beanName);
    }

    /**
     * 'Native' processing method for direct calls with an arbitrary target instance,
     * resolving all of its fields and methods which are annotated with one of the
     * configured 'autowired' annotation types.
     * @param bean the target instance to process
     * @throws BeanCreationException if autowiring failed
     * @see #setAutowiredAnnotationTypes(Set)
     */
    public void processInjection(Object bean) throws BeanCreationException {
        Class<?> clazz = bean.getClass();
        InjectionMetadata metadata = findAutowiringMetadata(clazz.getName(), clazz, null);
        try {
            metadata.inject(bean, null, null);
        }
        catch (BeanCreationException ex) {
            throw ex;
        }
        catch (Throwable ex) {
            throw new BeanCreationException(
                    "Injection of autowired dependencies failed for class [" + clazz + "]", ex);
        }
    }


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

    private InjectionMetadata buildAutowiringMetadata(Class<?> clazz) {
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
                        if (logger.isInfoEnabled()) {
                            logger.info("Autowired annotation is not supported on static fields: " + field);
                        }
                        return;
                    }
                    boolean required = determineRequiredStatus(ann);
                    currElements.add(new AutowiredFieldElement(field, required));
                }
            });

            ReflectionUtils.doWithLocalMethods(targetClass, method -> {
                Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
                if (!BridgeMethodResolver.isVisibilityBridgeMethodPair(method, bridgedMethod)) {
                    return;
                }
                MergedAnnotation<?> ann = findAutowiredAnnotation(bridgedMethod);
                if (ann != null && method.equals(ClassUtils.getMostSpecificMethod(method, clazz))) {
                    if (Modifier.isStatic(method.getModifiers())) {
                        if (logger.isInfoEnabled()) {
                            logger.info("Autowired annotation is not supported on static methods: " + method);
                        }
                        return;
                    }
                    if (method.getParameterCount() == 0) {
                        if (logger.isInfoEnabled()) {
                            logger.info("Autowired annotation should only be used on methods with parameters: " +
                                    method);
                        }
                    }
                    boolean required = determineRequiredStatus(ann);
                    PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
                    currElements.add(new AutowiredMethodElement(method, required, pd));
                }
            });

            elements.addAll(0, currElements);
            targetClass = targetClass.getSuperclass();
        }
        while (targetClass != null && targetClass != Object.class);

        return InjectionMetadata.forElements(elements, clazz);
    }

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
     * Determine if the annotated field or method requires its dependency.
     * <p>A 'required' dependency means that autowiring should fail when no beans
     * are found. Otherwise, the autowiring process will simply bypass the field
     * or method when no beans are found.
     * @param ann the Autowired annotation
     * @return whether the annotation indicates that a dependency is required
     */
    @SuppressWarnings({"deprecation", "cast"})
    protected boolean determineRequiredStatus(MergedAnnotation<?> ann) {
        return determineRequiredStatus(
                (AnnotationAttributes) ann.asMap(mergedAnnotation -> new AnnotationAttributes(mergedAnnotation.getType())));
    }

    /**
     * Determine if the annotated field or method requires its dependency.
     * <p>A 'required' dependency means that autowiring should fail when no beans
     * are found. Otherwise, the autowiring process will simply bypass the field
     * or method when no beans are found.
     * @param ann the Autowired annotation
     * @return whether the annotation indicates that a dependency is required
     * @deprecated since 5.2, in favor of {@link #determineRequiredStatus(MergedAnnotation)}
     */
    @Deprecated
    protected boolean determineRequiredStatus(AnnotationAttributes ann) {
        return (!ann.containsKey(this.requiredParameterName) ||
                this.requiredParameterValue == ann.getBoolean(this.requiredParameterName));
    }

    /**
     * Obtain all beans of the given type as autowire candidates.
     * @param type the type of the bean
     * @return the target beans, or an empty Collection if no bean of this type is found
     * @throws BeansException if bean retrieval failed
     */
    protected <T> Map<String, T> findAutowireCandidates(Class<T> type) throws BeansException {
        if (this.beanFactory == null) {
            throw new IllegalStateException("No BeanFactory configured - " +
                    "override the getBeanOfType method or specify the 'beanFactory' property");
        }
        return BeanFactoryUtils.beansOfTypeIncludingAncestors(this.beanFactory, type);
    }

    /**
     * Register the specified bean as dependent on the autowired beans.
     */
    private void registerDependentBeans(@Nullable String beanName, Set<String> autowiredBeanNames) {
        if (beanName != null) {
            for (String autowiredBeanName : autowiredBeanNames) {
                if (this.beanFactory != null && this.beanFactory.containsBean(autowiredBeanName)) {
                    this.beanFactory.registerDependentBean(autowiredBeanName, beanName);
                }
                if (logger.isTraceEnabled()) {
                    logger.trace("Autowiring by type from bean name '" + beanName +
                            "' to bean named '" + autowiredBeanName + "'");
                }
            }
        }
    }

    /**
     * Resolve the specified cached method argument or field value.
     */
    @Nullable
    private Object resolvedCachedArgument(@Nullable String beanName, @Nullable Object cachedArgument) {
        if (cachedArgument instanceof DependencyDescriptor) {
            DependencyDescriptor descriptor = (DependencyDescriptor) cachedArgument;
            Assert.state(this.beanFactory != null, "No BeanFactory available");
            return this.beanFactory.resolveDependency(descriptor, beanName, null, null);
        }
        else {
            return cachedArgument;
        }
    }

    /**
     * 修订
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
    }

    /**
     * 修订
     */
    @Override
    public void destroy() {
        injectionMetadataCache.clear();
        referenceBeansMap.clear();
    }

    /**
     * 以下修订=========================
     */

    /**
     * 注册ExtensionPointAutowiredBean
     */
    private String registerExtensionPointAutowiredBean(String propertyName, Class<?> injectedType) {
        beanFactory.getBeansOfType(injectedType);

        //注册生成的bean name，这个bean name可能要复杂一点，以避免重复
        //目前只是按照字段的名称或方法名称:参数序列来命名
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
        //验证bean是否重复
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
        // 这种生成是单例，相同的扩展接口共用一个扩展接口代理
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

    /**
     * 判断类型是否是扩展接口实现类型
     *
     * @param declareType
     * @return
     */
    private boolean isExtensionPointType(Class<?> declareType){
        if (declareType==null){
            return false;
        }
        return Arrays.stream(declareType.getInterfaces()).anyMatch(cls->{
            if (cls.equals(ExtensionPoint.class)){
                return true;
            }
            return false;
        });
    }

    /**
     * 以上修订=========================
     */

    /**
     * Class representing injection information about an annotated field.
     */
    private class AutowiredFieldElement extends InjectionMetadata.InjectedElement {

        private final boolean required;

        private volatile boolean cached;

        @Nullable
        private volatile Object cachedFieldValue;

        public AutowiredFieldElement(Field field, boolean required) {
            super(field, null);
            this.required = required;
        }

        @Override
        protected void inject(Object bean, @Nullable String beanName, @Nullable PropertyValues pvs) throws Throwable {
            Field field = (Field) this.member;
            Object value;
            if (this.cached) {
                try {
                    value = resolvedCachedArgument(beanName, this.cachedFieldValue);
                }
                catch (NoSuchBeanDefinitionException ex) {
                    // Unexpected removal of target bean for cached argument -> re-resolve
                    value = resolveFieldValue(field, bean, beanName);
                }
            }
            else {
                value = resolveFieldValue(field, bean, beanName);
            }
            if (value != null) {
                ReflectionUtils.makeAccessible(field);
                field.set(bean, value);
            }
        }

        @Nullable
        private Object resolveFieldValue(Field field, Object bean, @Nullable String beanName) {
            DependencyDescriptor desc = new DependencyDescriptor(field, this.required);
            desc.setContainingClass(bean.getClass());
            Set<String> autowiredBeanNames = new LinkedHashSet<>(1);
            Assert.state(beanFactory != null, "No BeanFactory available");
            TypeConverter typeConverter = beanFactory.getTypeConverter();
            Object value;

            /**
             * 以下修订=========================
             */
            boolean isExtensionPoint=isExtensionPointType(desc.getDeclaredType());

            if (isExtensionPoint){
                // 注册ExtensionPointAutowiredBean，返回需要注入的beanName
                String injectBeanName = registerExtensionPointAutowiredBean(desc.getDependencyName(), desc.getDependencyType());
                // 通过beanName获取要注入的bean对象
                value = beanFactory.getBean(injectBeanName);
            }else {
                try {
                    value = beanFactory.resolveDependency(desc, beanName, autowiredBeanNames, typeConverter);
                } catch (BeansException ex) {
                    throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(field), ex);
                }
            }
            /**
             * 以上修订=========================
             */

            synchronized (this) {
                if (!this.cached) {
                    Object cachedFieldValue = null;
                    if (value != null || this.required) {
                        cachedFieldValue = desc;
                        registerDependentBeans(beanName, autowiredBeanNames);
                        if (autowiredBeanNames.size() == 1) {
                            String autowiredBeanName = autowiredBeanNames.iterator().next();
                            if (beanFactory.containsBean(autowiredBeanName) &&
                                    beanFactory.isTypeMatch(autowiredBeanName, field.getType())) {
                                cachedFieldValue = new ShortcutDependencyDescriptor(
                                        desc, autowiredBeanName, field.getType());
                            }
                        }
                    }
                    this.cachedFieldValue = cachedFieldValue;
                    this.cached = true;
                }
            }
            return value;
        }

        /**
         * 获取注入类型
         */
//        public Class<?> getInjectedType() {
//            if (!isField) {
//                return null;
//            }
//            return ((Field) this.member).getType();
//        }

        /**
         * 获取注入属性名
         */
//        public String getPropertyName() {
//            if (!isField) {
//                return null;
//            }
//            return ((Field) this.member).getName();
//        }
    }


    /**
     * Class representing injection information about an annotated method.
     */
    private class AutowiredMethodElement extends InjectionMetadata.InjectedElement {

        private final boolean required;

        private volatile boolean cached;

        @Nullable
        private volatile Object[] cachedMethodArguments;

        public AutowiredMethodElement(Method method, boolean required, @Nullable PropertyDescriptor pd) {
            super(method, pd);
            this.required = required;
        }

        @Override
        protected void inject(Object bean, @Nullable String beanName, @Nullable PropertyValues pvs) throws Throwable {
            if (checkPropertySkipping(pvs)) {
                return;
            }
            Method method = (Method) this.member;
            Object[] arguments;
            if (this.cached) {
                try {
                    arguments = resolveCachedArguments(beanName);
                }
                catch (NoSuchBeanDefinitionException ex) {
                    // Unexpected removal of target bean for cached argument -> re-resolve
                    arguments = resolveMethodArguments(method, bean, beanName);
                }
            }
            else {
                arguments = resolveMethodArguments(method, bean, beanName);
            }
            if (arguments != null) {
                try {
                    ReflectionUtils.makeAccessible(method);
                    method.invoke(bean, arguments);
                }
                catch (InvocationTargetException ex) {
                    throw ex.getTargetException();
                }
            }
        }

        @Nullable
        private Object[] resolveCachedArguments(@Nullable String beanName) {
            Object[] cachedMethodArguments = this.cachedMethodArguments;
            if (cachedMethodArguments == null) {
                return null;
            }
            Object[] arguments = new Object[cachedMethodArguments.length];
            for (int i = 0; i < arguments.length; i++) {
                arguments[i] = resolvedCachedArgument(beanName, cachedMethodArguments[i]);
            }
            return arguments;
        }

        @Nullable
        private Object[] resolveMethodArguments(Method method, Object bean, @Nullable String beanName) {
            int argumentCount = method.getParameterCount();
            Object[] arguments = new Object[argumentCount];
            DependencyDescriptor[] descriptors = new DependencyDescriptor[argumentCount];
            Set<String> autowiredBeans = new LinkedHashSet<>(argumentCount);
            Assert.state(beanFactory != null, "No BeanFactory available");
            TypeConverter typeConverter = beanFactory.getTypeConverter();
            for (int i = 0; i < arguments.length; i++) {
                MethodParameter methodParam = new MethodParameter(method, i);
                DependencyDescriptor currDesc = new DependencyDescriptor(methodParam, this.required);
                currDesc.setContainingClass(bean.getClass());
                descriptors[i] = currDesc;
                try {
                    /**
                     * 以下修订=========================
                     */
                    boolean isExtensionPoint=isExtensionPointType(currDesc.getDeclaredType());
                    //如果是扩展点
                    if (isExtensionPoint){
                        // 注册ExtensionPointAutowiredBean，返回需要注入的beanName
                        String injectBeanName = registerExtensionPointAutowiredBean(method.getName() + ":" +  i, currDesc.getDependencyType());
                        // 通过beanName获取要注入的bean对象
                        Object arg = beanFactory.getBean(injectBeanName);
                        arguments[i] = arg;
                    }else {
                        Object arg = beanFactory.resolveDependency(currDesc, beanName, autowiredBeans, typeConverter);
                        if (arg == null && !this.required) {
                            arguments = null;
                            break;
                        }
                        arguments[i] = arg;
                    }
                    /**
                     * 以上修订=========================
                     */
                }
                catch (BeansException ex) {
                    throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(methodParam), ex);
                }
            }
            synchronized (this) {
                if (!this.cached) {
                    if (arguments != null) {
                        DependencyDescriptor[] cachedMethodArguments = Arrays.copyOf(descriptors, arguments.length);
                        registerDependentBeans(beanName, autowiredBeans);
                        if (autowiredBeans.size() == argumentCount) {
                            Iterator<String> it = autowiredBeans.iterator();
                            Class<?>[] paramTypes = method.getParameterTypes();
                            for (int i = 0; i < paramTypes.length; i++) {
                                String autowiredBeanName = it.next();
                                if (beanFactory.containsBean(autowiredBeanName) &&
                                        beanFactory.isTypeMatch(autowiredBeanName, paramTypes[i])) {
                                    cachedMethodArguments[i] = new ShortcutDependencyDescriptor(
                                            descriptors[i], autowiredBeanName, paramTypes[i]);
                                }
                            }
                        }
                        this.cachedMethodArguments = cachedMethodArguments;
                    }
                    else {
                        this.cachedMethodArguments = null;
                    }
                    this.cached = true;
                }
            }
            return arguments;
        }
    }


    /**
     * DependencyDescriptor variant with a pre-resolved target bean name.
     */
    @SuppressWarnings("serial")
    private static class ShortcutDependencyDescriptor extends DependencyDescriptor {

        private final String shortcut;

        private final Class<?> requiredType;

        public ShortcutDependencyDescriptor(DependencyDescriptor original, String shortcut, Class<?> requiredType) {
            super(original);
            this.shortcut = shortcut;
            this.requiredType = requiredType;
        }

        @Override
        public Object resolveShortcut(BeanFactory beanFactory) {
            return beanFactory.getBean(this.shortcut, this.requiredType);
        }
    }



}
