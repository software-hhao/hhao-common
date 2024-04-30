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

package com.hhao.common.extension.register;

import com.hhao.common.extension.BizScenario;
import com.hhao.common.extension.model.ExtensionCoordinate;
import com.hhao.common.extension.model.ExtensionPoint;
import com.hhao.common.extension.annotation.Extension;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;


/**
 * 扩展点注册
 *
 * @author Wang
 * @since 1.0.0
 */
public class ExtensionRegister {
    private ExtensionRepository extensionRepository;

    /**
     * Instantiates a new Extension register.
     *
     * @param extensionRepository the extension repository
     */
    public ExtensionRegister(ExtensionRepository extensionRepository){
        this.extensionRepository=extensionRepository;
    }

    /**
     * Do registration.
     *
     * @param extensionObject the extension object
     */
    public void doRegistration(ExtensionPoint extensionObject){
        Class<?>  extensionClz = extensionObject.getClass();
        if (AopUtils.isAopProxy(extensionObject)) {
            extensionClz = ClassUtils.getUserClass(extensionObject);
        }
        Extension extensionAnn = AnnotationUtils.findAnnotation(extensionClz, Extension.class);
        BizScenario bizScenario = BizScenario.valueOf(extensionAnn.bizId(), extensionAnn.useCase(), extensionAnn.scenario());
        ExtensionCoordinate extensionCoordinate = new ExtensionCoordinate(calculateExtensionPoint(extensionClz), bizScenario.getUniqueIdentity());
        extensionRepository.putExtensionPoint(extensionCoordinate,extensionObject);
    }

    private String calculateExtensionPoint(Class<?> targetClz) {
        Class<?>[] interfaces = ClassUtils.getAllInterfacesForClass(targetClz);
        if (interfaces == null || interfaces.length == 0) {
            throw new RuntimeException("Please assign a extension point interface for " + targetClz);
        }
        for (Class intf : interfaces) {
            if (isImplementExtensionPoint(intf)){
                return intf.getName();
            }
        }
        throw new RuntimeException("Your name of ExtensionPoint for "+targetClz+" is not valid, must be implement "+ ExtensionPoint.class);
    }

    /**
     * 判断targetClz是否继承Extension接口
     *
     * @param targetClz
     * @return
     */
    private boolean isImplementExtensionPoint(Class<?> targetClz){
        if (targetClz==null){
            return false;
        }
        Class<?>[] interfaces=targetClz.getInterfaces();
        if (interfaces == null || interfaces.length == 0) {
            return false;
        }
        for (Class intf : interfaces) {
            if (intf.equals(ExtensionPoint.class)){
                return true;
            }
            return isImplementExtensionPoint(intf);
        }
        return false;
    }
}
