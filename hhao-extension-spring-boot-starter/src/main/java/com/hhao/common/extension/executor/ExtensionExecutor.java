
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

package com.hhao.common.extension.executor;

import com.hhao.common.extension.BizScenario;
import com.hhao.common.extension.model.ExtensionCoordinate;
import com.hhao.common.extension.register.ExtensionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @author Wang
 * @since 2022/3/10 17:08
 */
public class ExtensionExecutor extends AbstractComponentExecutor {

    private Logger logger = LoggerFactory.getLogger(ExtensionExecutor.class);

    @Resource
    private ExtensionRepository extensionRepository;

    @Override
    protected <C> C locateComponent(Class<C> targetClz, BizScenario bizScenario) {
        C extension = locateExtension(targetClz, bizScenario);
        logger.debug("[Located Extension]: " + extension.getClass().getSimpleName());
        return extension;
    }

    /**
     * if the bizScenarioUniqueIdentity is "ali.tmall.supermarket"
     *
     * the search path is as below:
     * 1、first try to get extension by "ali.tmall.supermarket", if get, return it.
     * 2、loop try to get extension by "ali.tmall", if get, return it.
     * 3、loop try to get extension by "ali", if get, return it.
     * 4、if not found, try the default extension
     * @param targetClz
     */
    protected <Ext> Ext locateExtension(Class<Ext> targetClz, BizScenario bizScenario) {
        checkNull(bizScenario);

        Ext extension;

        logger.debug("BizScenario in locateExtension is : " + bizScenario.getUniqueIdentity());

        // first try with full namespace
        extension = firstTry(targetClz, bizScenario);
        if (extension != null) {
            return extension;
        }

        // second try with default scenario
        extension = secondTry(targetClz, bizScenario);
        if (extension != null) {
            return extension;
        }

        // third try with default use case + default scenario
        extension = defaultUseCaseTry(targetClz, bizScenario);
        if (extension != null) {
            return extension;
        }

        throw new RuntimeException("Can not find extension with ExtensionPoint: "+targetClz+" BizScenario:"+bizScenario.getUniqueIdentity());
    }

    /**
     * first try with full namespace
     *
     * example:  biz1.useCase1.scenario1
     */
    private  <Ext> Ext firstTry(Class<Ext> targetClz, BizScenario bizScenario) {
        logger.debug("First trying with " + bizScenario.getUniqueIdentity());
        return locate(targetClz.getName(), bizScenario.getUniqueIdentity());
    }

    /**
     * second try with default scenario
     *
     * example:  biz1.useCase1.#defaultScenario#
     */
    private <Ext> Ext secondTry(Class<Ext> targetClz, BizScenario bizScenario){
        logger.debug("Second trying with " + bizScenario.getIdentityWithDefaultScenario());
        return locate(targetClz.getName(), bizScenario.getIdentityWithDefaultScenario());
    }

    /**
     * third try with default use case + default scenario
     *
     * example:  biz1.#defaultUseCase#.#defaultScenario#
     */
    private <Ext> Ext defaultUseCaseTry(Class<Ext> targetClz, BizScenario bizScenario){
        logger.debug("Third trying with " + bizScenario.getIdentityWithDefaultUseCase());
        return locate(targetClz.getName(), bizScenario.getIdentityWithDefaultUseCase());
    }

    private <Ext> Ext locate(String name, String uniqueIdentity) {
        final Ext ext = (Ext) extensionRepository.getExtensionRepo().
                get(new ExtensionCoordinate(name, uniqueIdentity));
        return ext;
    }

    private void checkNull(BizScenario bizScenario){
        if(bizScenario == null){
            throw new IllegalArgumentException("BizScenario can not be null for extension");
        }
    }

}
