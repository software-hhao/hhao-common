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

package com.hhao.extension.executor;

import com.hhao.common.extension.BizScenario;
import com.hhao.extension.model.ExtensionCoordinate;
import com.hhao.extension.model.ExtensionPoint;
import com.hhao.extension.register.ExtensionRepository;
import com.hhao.extension.strategy.InterruptionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wang
 * @since 1.0.0
 */
public class ExtensionExecutor extends AbstractComponentExecutor{
    private Logger logger = LoggerFactory.getLogger(ExtensionExecutor.class);

    private ExtensionRepository extensionRepository;

    public ExtensionExecutor(ExtensionRepository extensionRepository){
        this.extensionRepository=extensionRepository;
    }

    /**
     * first try with full namespace
     *
     * example:  biz1.useCase1.scenario1
     */
    private  <C, R> ExtensionPoint<C,R> firstTry(Class<? extends ExtensionPoint<C,R>> targetClz, BizScenario bizScenario,C context) {
        logger.debug("First trying with " + bizScenario.getUniqueIdentity());
        return locate(targetClz.getName(), bizScenario.getUniqueIdentity(),context);
    }

    /**
     * second try with default scenario
     *
     * example:  biz1.useCase1.#defaultScenario#
     */
    private <C,R> ExtensionPoint<C,R> secondTry(Class<? extends ExtensionPoint<C,R>> targetClz, BizScenario bizScenario,C context){
        logger.debug("Second trying with " + bizScenario.getIdentityWithDefaultScenario());
        return locate(targetClz.getName(), bizScenario.getIdentityWithDefaultScenario(),context);
    }

    /**
     * third try with default use case + default scenario
     *
     * example:  biz1.#defaultUseCase#.#defaultScenario#
     */
    private <C, R> ExtensionPoint<C,R> defaultUseCaseTry(Class<? extends ExtensionPoint<C,R>> targetClz, BizScenario bizScenario,C context){
        logger.debug("Third trying with " + bizScenario.getIdentityWithDefaultUseCase());
        return locate(targetClz.getName(), bizScenario.getIdentityWithDefaultUseCase(),context);
    }

    /**
     * 找到第一个适合的就返回
     * @param name
     * @param uniqueIdentity
     * @param context
     * @param <C>
     * @param <R>
     * @return
     */
    private <C, R> ExtensionPoint<C,R> locate(String name, String uniqueIdentity,C context) {
        List<ExtensionPoint> extensionPoints= extensionRepository.getExtensionPoints(new ExtensionCoordinate(name, uniqueIdentity));
        for(ExtensionPoint exp:extensionPoints){
            if (exp.support(context)){
                return exp;
            }
        }
        return null;
    }

    private void checkNull(BizScenario bizScenario){
        if(bizScenario == null){
            throw new IllegalArgumentException("BizScenario can not be null for extension");
        }
    }

    @Override
    protected <C, R> ExtensionPoint<C,R> locateComponent(Class<? extends ExtensionPoint<C, R>> targetClz, BizScenario bizScenario, C context) {
        checkNull(bizScenario);
        ExtensionPoint<C,R> extension=null;
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
        throw new RuntimeException("Can not find extension with ExtensionPoint: "+targetClz+" BizScenario:"+bizScenario.getUniqueIdentity());
    }

    private  <C, R> List<ExtensionPoint> multiFirstTry(Class<? extends ExtensionPoint<C,R>> targetClz, BizScenario bizScenario,C context) {
        logger.debug("First trying with " + bizScenario.getUniqueIdentity());
        return locates(targetClz.getName(), bizScenario.getUniqueIdentity(),context);
    }

    private <C,R> List<ExtensionPoint> multiSecondTry(Class<? extends ExtensionPoint<C,R>> targetClz, BizScenario bizScenario,C context){
        logger.debug("Second trying with " + bizScenario.getIdentityWithDefaultScenario());
        return locates(targetClz.getName(), bizScenario.getIdentityWithDefaultScenario(),context);
    }

    private <C, R> List<ExtensionPoint> multiDefaultUseCaseTry(Class<? extends ExtensionPoint<C,R>> targetClz, BizScenario bizScenario,C context){
        logger.debug("Third trying with " + bizScenario.getIdentityWithDefaultUseCase());
        return locates(targetClz.getName(), bizScenario.getIdentityWithDefaultUseCase(),context);
    }

    private <C> List<ExtensionPoint> locates(String name, String uniqueIdentity,C context) {
        List<ExtensionPoint> extensionPoints= extensionRepository.getExtensionPoints(new ExtensionCoordinate(name, uniqueIdentity));
        List<ExtensionPoint> results=new ArrayList<>();
        for(ExtensionPoint ext:extensionPoints){
            if (ext.support(context)){
                results.add(ext);
            }
        }
        return results;
    }

    @Override
    protected <C,R> List<ExtensionPoint> locateComponents(Class<? extends ExtensionPoint<C, R>> targetClz, BizScenario bizScenario, C context) {
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
        throw new RuntimeException("Can not find extension with ExtensionPoint: "+targetClz+" BizScenario:"+bizScenario.getUniqueIdentity());
    }
}
