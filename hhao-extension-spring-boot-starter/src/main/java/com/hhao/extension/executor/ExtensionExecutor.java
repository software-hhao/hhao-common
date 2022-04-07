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
    private  <C, R> ExtensionPoint<C,R> firstTry(Class<? extends ExtensionPoint<C,R>> targetClz, BizScenario bizScenario) {
        logger.debug("First trying with " + bizScenario.getUniqueIdentity());
        return locate(targetClz.getName(), bizScenario.getUniqueIdentity());
    }

    /**
     * second try with default scenario
     *
     * example:  biz1.useCase1.#defaultScenario#
     */
    private <C,R> ExtensionPoint<C,R> secondTry(Class<? extends ExtensionPoint<C,R>> targetClz, BizScenario bizScenario){
        logger.debug("Second trying with " + bizScenario.getIdentityWithDefaultScenario());
        return locate(targetClz.getName(), bizScenario.getIdentityWithDefaultScenario());
    }

    /**
     * third try with default use case + default scenario
     *
     * example:  biz1.#defaultUseCase#.#defaultScenario#
     */
    private <C, R> ExtensionPoint<C,R> defaultUseCaseTry(Class<? extends ExtensionPoint<C,R>> targetClz, BizScenario bizScenario){
        logger.debug("Third trying with " + bizScenario.getIdentityWithDefaultUseCase());
        return locate(targetClz.getName(), bizScenario.getIdentityWithDefaultUseCase());
    }

    private <C, R> ExtensionPoint<C,R> locate(String name, String uniqueIdentity) {
        final ExtensionPoint<C,R> ext= (ExtensionPoint<C,R>) extensionRepository.getExtensionRepo().get(new ExtensionCoordinate(name, uniqueIdentity));
        return ext;
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
        extension = firstTry(targetClz, bizScenario);
        if (extension != null && extension.support(context)) {
            return extension;
        }

        // second try with default scenario
        extension = secondTry(targetClz, bizScenario);
        if (extension != null && extension.support(context)) {
            return extension;
        }

        // third try with default use case + default scenario
        extension = defaultUseCaseTry(targetClz, bizScenario);
        if (extension != null && extension.support(context)) {
            return extension;
        }

        throw new RuntimeException("Can not find extension with ExtensionPoint: "+targetClz+" BizScenario:"+bizScenario.getUniqueIdentity());
    }

    @Override
    protected <C, R> List<ExtensionPoint<C, R>> locateComponents(Class<? extends ExtensionPoint<C, R>> targetClz, BizScenario bizScenario, C context, InterruptionStrategy<R> interruptionStrategy) {




        return null;
    }
}
