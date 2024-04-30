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

package com.hhao.common.extension.executor;

import com.hhao.common.extension.BizScenario;
import com.hhao.common.extension.model.ExtensionCoordinate;
import com.hhao.common.extension.model.ExtensionPoint;
import com.hhao.common.extension.register.ExtensionRepository;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 执行器
 * 执行器实现的匹配先按BizScenario，再按扩展点的support方法。
 * BizScenario匹配顺序从特殊到一般，顺序如下：
 * 1、biz1.useCase1.scenario1
 * 2、biz1.useCase1
 * 3、biz1
 *
 * @author Wang
 * @since 1.0.0
 */
public class ExtensionExecutor extends AbstractComponentExecutor{
    private Logger logger = LoggerFactory.getLogger(ExtensionExecutor.class);

    private ExtensionRepository extensionRepository;

    /**
     * Instantiates a new Extension executor.
     *
     * @param extensionRepository the extension repository
     */
    public ExtensionExecutor(ExtensionRepository extensionRepository,boolean isNotFoundThrowError){
        this.extensionRepository=extensionRepository;
        this.setNotFoundThrowError(isNotFoundThrowError);
    }

    /**
     * first try with full namespace
     *
     * example:  biz1.useCase1.scenario1
     */
    private ExtensionPoint firstTry(Class<? extends ExtensionPoint> targetClz, BizScenario bizScenario, Object context) {
        logger.debug("First trying with " + bizScenario.getUniqueIdentity());
        return findFirstExtensionPoint(locates(targetClz.getName(), bizScenario.getUniqueIdentity()),context);
    }

    /**
     * second try with default scenario
     *
     * example:  biz1.useCase1.#defaultScenario#
     */
    private ExtensionPoint secondTry(Class<? extends ExtensionPoint> targetClz, BizScenario bizScenario,Object context){
        logger.debug("Second trying with " + bizScenario.getIdentityWithDefaultScenario());
        return findFirstExtensionPoint(locates(targetClz.getName(), bizScenario.getIdentityWithDefaultScenario()),context);
    }

    /**
     * third try with default use case + default scenario
     *
     * example:  biz1.#defaultUseCase#.#defaultScenario#
     */
    private ExtensionPoint defaultUseCaseTry(Class<? extends ExtensionPoint> targetClz, BizScenario bizScenario,Object context){
        logger.debug("Third trying with " + bizScenario.getIdentityWithDefaultUseCase());
        return findFirstExtensionPoint(locates(targetClz.getName(), bizScenario.getIdentityWithDefaultUseCase()),context);
    }

    private List<ExtensionPoint> multiFirstTry(Class<? extends ExtensionPoint> targetClz, BizScenario bizScenario,Object context) {
        logger.debug("First trying with " + bizScenario.getUniqueIdentity());
        return findExtensionPoints(locates(targetClz.getName(), bizScenario.getUniqueIdentity()),context);
    }

    private List<ExtensionPoint> multiSecondTry(Class<? extends ExtensionPoint> targetClz, BizScenario bizScenario,Object context){
        logger.debug("Second trying with " + bizScenario.getIdentityWithDefaultScenario());
        return findExtensionPoints(locates(targetClz.getName(), bizScenario.getIdentityWithDefaultScenario()),context);
    }

    private List<ExtensionPoint> multiDefaultUseCaseTry(Class<? extends ExtensionPoint> targetClz, BizScenario bizScenario,Object context){
        logger.debug("Third trying with " + bizScenario.getIdentityWithDefaultUseCase());
        return findExtensionPoints(locates(targetClz.getName(), bizScenario.getIdentityWithDefaultUseCase()),context);
    }

    private List<ExtensionPoint> locates(String name, String uniqueIdentity) {
        return extensionRepository.getExtensionPoints(new ExtensionCoordinate(name, uniqueIdentity));
    }

    private ExtensionPoint findFirstExtensionPoint(List<ExtensionPoint> extensionPoints,Object context) {
        if (CollectionUtils.isEmpty(extensionPoints)){
            return null;
        }
        if (context!=null){
            for(ExtensionPoint ext:extensionPoints){
                if (ext.support(context)){
                    return ext;
                }
            }
        }else{
            return extensionPoints.get(0);
        }
        return null;
    }

    private List<ExtensionPoint> findExtensionPoints(List<ExtensionPoint> extensionPoints,Object context) {
        if (CollectionUtils.isEmpty(extensionPoints)){
            return null;
        }
        List<ExtensionPoint> results=new ArrayList<>();
        if (context!=null){
            for(ExtensionPoint ext:extensionPoints){
                if (ext.support(context)){
                    results.add(ext);
                }
            }
        }else{
            return extensionPoints;
        }
        return results;
    }

    private void checkNull(BizScenario bizScenario){
        if(bizScenario == null){
            throw new IllegalArgumentException("BizScenario can not be null for extension");
        }
    }

    @Override
    public <C> ExtensionPoint locateComponent(Class<? extends ExtensionPoint> targetClz, BizScenario bizScenario, C context) {
        checkNull(bizScenario);
        ExtensionPoint extension=null;
        logger.debug("BizScenario in locateExtension is : " + bizScenario.getUniqueIdentity());

        // first try with full namespace
        extension = firstTry(targetClz, bizScenario,context);
        if (extension != null) {
            return extension;
        }

        // second try with default scenario
        extension = secondTry(targetClz, bizScenario,context);
        if (extension != null) {
            return extension;
        }

        // third try with default use case + default scenario
        extension = defaultUseCaseTry(targetClz, bizScenario,context);
        if (extension != null) {
            return extension;
        }

        logger.info("Can not find extension with ExtensionPoint: "+targetClz+" BizScenario:"+bizScenario.getUniqueIdentity());

        if (this.isNotFoundThrowError()){
            throw new RuntimeException("Can not find extension with ExtensionPoint: "+targetClz+" BizScenario:"+bizScenario.getUniqueIdentity());
        }
        return null;
    }

    @Override
    public <C> List<ExtensionPoint> locateComponents(Class<? extends ExtensionPoint> targetClz, BizScenario bizScenario, C context) {
        checkNull(bizScenario);
        List<ExtensionPoint> extensions=null;
        logger.debug("BizScenario in locateExtension is : " + bizScenario.getUniqueIdentity());

        // first try with full namespace
        extensions = multiFirstTry(targetClz, bizScenario,context);
        if (extensions != null) {
            return extensions;
        }

        // second try with default scenario
        extensions = multiSecondTry(targetClz, bizScenario,context);
        if (extensions != null) {
            return extensions;
        }

        // third try with default use case + default scenario
        extensions = multiDefaultUseCaseTry(targetClz, bizScenario,context);
        if (extensions != null) {
            return extensions;
        }

        logger.info("Can not find extension with ExtensionPoint: "+targetClz+" BizScenario:"+bizScenario.getUniqueIdentity());
        if (this.isNotFoundThrowError()){
            throw new RuntimeException("Can not find extension with ExtensionPoint: "+targetClz+" BizScenario:"+bizScenario.getUniqueIdentity());
        }
        return null;
    }
}
