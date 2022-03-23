
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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理方法调用
 *
 * @author Wang
 * @since 2022/3/11 20:01
 */
@Deprecated
public class ExtensionPointInvocationHandler implements InvocationHandler {

    private final Class<?> interfaceClass;

    public ExtensionPointInvocationHandler(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
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
        return "good";
//        ExtensionPointCode extensionPointCode = ExtensionPointRegister.getExtensionPointCode(interfaceClass, method);
//        ExtensionPointObject extensionPointObject = ExtensionPointRegister.getExtensionPointObject(extensionPointCode);
//        try {
//            RouterStrategy<? extends IExtensionPoint> routerStrategy = StrategyRegister.getInstance().getRouterStrategy(extensionPointObject.getRouterStrategy());
//            if (routerStrategy == null) {
//                throw new RuntimeException("please set router strategy first");
//            }
//            RouterParam routerParam = new RouterParam(extensionPointCode, args, routerStrategy.customGetParam(args));
//            List<IExtensionPoint> extensionPoints = (List<IExtensionPoint>) routerStrategy.execute(routerParam);
//            if (CollectionUtils.isEmpty(extensionPoints)) {
//                throw new RuntimeException("extension point not found, code:" + extensionPointCode.getCode());
//            }
//            // execute extensionPoints
//            List<Object> executeResultList = extensionPoints.stream().map(extensionPoint -> {
//                try {
//                    Method actualMethod = ExtensionPointRegister.getMethodByCode(extensionPointCode, extensionPoint);
//                    if (actualMethod == null) {
//                        throw new RuntimeException("method not found, code:" + extensionPointCode.getCode());
//                    }
//                    return actualMethod.invoke(extensionPoint, args);
//                } catch (IllegalAccessException | InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }).collect(Collectors.toList());
//            // use ResultStrategy
//            ResultStrategy<Object> resultStrategy = (ResultStrategy<Object>) StrategyRegister.getInstance().getResultStrategy(extensionPointObject.getResultStrategy());
//            if (resultStrategy == null) {
//                throw new RuntimeException("result strategy can not null");
//            }
//            return resultStrategy.execute(executeResultList);
//        } catch (Exception ex) {
//            // use ExceptionStrategy
//            ExceptionStrategy<?> exceptionStrategy = StrategyRegister.getInstance().getExceptionStrategy(extensionPointObject.getExceptionStrategy());
//            if (exceptionStrategy != null) {
//                return exceptionStrategy.execute(args, ex);
//            }
//            throw ex;
//        }
    }
}