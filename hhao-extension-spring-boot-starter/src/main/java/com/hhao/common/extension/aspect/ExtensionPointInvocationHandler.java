
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

import com.hhao.common.extension.BizScenario;
import com.hhao.common.extension.annotation.ExtensionPointAutowired;
import com.hhao.common.extension.executor.AbstractComponentExecutor;
import com.hhao.common.extension.executor.ExtensionExecutorUtil;
import com.hhao.common.extension.model.ExtensionPoint;
import com.hhao.common.extension.model.MultiValues;
import com.hhao.common.extension.strategy.InterruptionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理方法调用
 *
 * @author Wang
 * @since 2022 /3/11 22:01
 */
public class ExtensionPointInvocationHandler implements InvocationHandler {
    private Logger logger = LoggerFactory.getLogger(ExtensionPointInvocationHandler.class);
    private final String MODEL_PARAMETER_NAME="model";
    private final String INTERRUPTION_STRATEGY_CLZ_PARAMETER_NAME="interruptionStrategy";

    private final Class<?> interfaceClass;
    private AnnotationAttributes annotationAttributes;
    private ExtensionPointAutowired.Model model;
    private InterruptionStrategy interruptionStrategy;

    /**
     * Gets annotation attributes.
     *
     * @return the annotation attributes
     */
    public AnnotationAttributes getAnnotationAttributes() {
        return annotationAttributes;
    }

    /**
     * Sets annotation attributes.
     *
     * @param annotationAttributes the annotation attributes
     */
    public void setAnnotationAttributes(AnnotationAttributes annotationAttributes) {
        this.annotationAttributes = annotationAttributes;
        initProperty(annotationAttributes);
    }

    /**
     * Instantiates a new Extension point invocation handler.
     *
     * @param interfaceClass the interface class
     */
    public ExtensionPointInvocationHandler(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        AbstractComponentExecutor executor = ExtensionExecutorUtil.getExecutor();

        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0) {
            if ("toString".equals(methodName)) {
                return this.toString();
            } else if ("hashCode".equals(methodName)) {
                return this.hashCode();
            }
        } else if (parameterTypes.length == 1 && "equals".equals(methodName)) {
            return this.equals(args[0]);
        }

        BizScenario bizScenario = getBizScenario(args);

        if (model.equals(ExtensionPointAutowired.Model.SIMPLE)) {
            ExtensionPoint exp = executor.locateComponent((Class<ExtensionPoint>) interfaceClass, bizScenario, args);
            if (exp != null) {
                try {
                    return ReflectionUtils.invokeMethod(method, exp, args);
                } catch (Exception e) {
                    exp.onError(e,args);
                }
            }
        } else {
            checkMultiReturnType(method);
            List<ExtensionPoint> exps = executor.locateComponents((Class<ExtensionPoint>) interfaceClass, bizScenario, args);
            if (exps!=null) {
                MultiValues multiValues = new MultiValues(exps.size());
                Object result = null;

                for (ExtensionPoint exp : exps) {
                    try {
                        result = ReflectionUtils.invokeMethod(method, exp, args);
                        if (result!=null) {
                            multiValues.mergerReturnValue((MultiValues) result);
                        }
                        if (interruptionStrategy.interrupt(result)) {
                            return multiValues;
                        }
                    } catch (Exception e) {
                        exp.onError(e, args);
                    }
                }
                return multiValues;
            }
        }
        return null;
    }

    private BizScenario getBizScenario(Object[] args) {
        for (Object obj : args) {
            if (obj instanceof BizScenario) {
                return (BizScenario) obj;
            }
        }
        logger.info("use default BizScenario");
        return BizScenario.newDefault();
    }

    private void initProperty(AnnotationAttributes annotationAttributes){
        this.interruptionStrategy=getInterruptionStrategy(annotationAttributes);
        this.model=getModel(annotationAttributes);
    }

    private ExtensionPointAutowired.Model getModel(AnnotationAttributes annotationAttributes) {
        return annotationAttributes.getEnum(MODEL_PARAMETER_NAME);
    }

    private InterruptionStrategy getInterruptionStrategy(AnnotationAttributes annotationAttributes) {
        try {
            Class<? extends InterruptionStrategy> interruptionStrategyClz=annotationAttributes.getClass(INTERRUPTION_STRATEGY_CLZ_PARAMETER_NAME);
            return interruptionStrategyClz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 扩展点组合且有返回值的情况下，只支持MultiValues返回值类型
     * @param method
     */
    private void checkMultiReturnType(Method method){
        if (!method.getReturnType().getName().equals("void") &&
        !method.getReturnType().getName().equals(MultiValues.class.getName())){
            throw new RuntimeException("The return value of the composite extension point with ExtensionPointAutowired only supports MultiValues type");
        }
    }
}