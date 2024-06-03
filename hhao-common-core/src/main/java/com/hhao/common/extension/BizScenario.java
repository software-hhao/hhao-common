
/*
 * Copyright 2008-2024 wangsheng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.extension;

/**
 * 扩展点的业务座标
 * 从特殊到一般进行匹配:scenario.useCase.bizId
 * bizID:领域+[子域]的定义
 * userCase:用例定义
 * scenario:场景
 * 例如：
 * hhao.user.vip,hhao系统用户vip业务场景
 *
 * @author Wang
 * @since 1.0.0
 */
public class BizScenario {
    /**
     * The constant DEFAULT_BIZ_ID.
     */
    public static final String DEFAULT_BIZ_ID = "#defaultBizId#";
    /**
     * The constant DEFAULT_USE_CASE.
     */
    public static final String DEFAULT_USE_CASE = "#defaultUseCase#";
    /**
     * The constant DEFAULT_SCENARIO.
     */
    public static final String DEFAULT_SCENARIO = "#defaultScenario#";
    /**
     * The constant DOT_SEPARATOR.
     */
    public static final String DOT_SEPARATOR = ".";

    /**
     * bizId is used to identify a business, such as "hhao"
     */
    private String bizId = DEFAULT_BIZ_ID;

    /**
     * useCase is used to identify a use case, such as "user"
     */
    private String useCase = DEFAULT_USE_CASE;

    /**
     * scenario is used to identify a use case, such as "vip"
     */
    private String scenario = DEFAULT_SCENARIO;

    /**
     * 构建完整的扩展点座标
     *
     * @param bizId    the biz id
     * @param useCase  the use case
     * @param scenario the scenario
     * @return the biz scenario
     */
    public static BizScenario valueOf(String bizId, String useCase, String scenario) {
        BizScenario bizScenario = new BizScenario();
        bizScenario.bizId = bizId;
        bizScenario.useCase = useCase;
        bizScenario.scenario = scenario;
        return bizScenario;
    }

    /**
     * 构建bizId.useCase.DEFAULT_SCENARIO的业务座标
     *
     * @param bizId   the biz id
     * @param useCase the use case
     * @return the biz scenario
     */
    public static BizScenario valueOf(String bizId, String useCase) {
        return BizScenario.valueOf(bizId, useCase, DEFAULT_SCENARIO);
    }

    /**
     * 构建bizId.DEFAULT_USE_CASE.DEFAULT_SCENARIO的业务座标
     *
     * @param bizId the biz id
     * @return the biz scenario
     */
    public static BizScenario valueOf(String bizId) {
        return BizScenario.valueOf(bizId, DEFAULT_USE_CASE, DEFAULT_SCENARIO);
    }

    /**
     * 构建默认的扩展点座标
     * DEFAULT_BIZ_ID.DEFAULT_USE_CASE.DEFAULT_SCENARIO
     *
     * @return the biz scenario
     */
    public static BizScenario newDefault() {
        return BizScenario.valueOf(DEFAULT_BIZ_ID, DEFAULT_USE_CASE, DEFAULT_SCENARIO);
    }

    /**
     * 完整的扩展点业务座标
     * bizId.useCase.scenario
     *
     * @return unique identity
     */
    public String getUniqueIdentity() {
        return bizId + DOT_SEPARATOR + useCase + DOT_SEPARATOR + scenario;
    }

    /**
     * Gets identity with default scenario.
     *
     * @return the identity with default scenario
     */
    public String getIdentityWithDefaultScenario() {
        return bizId + DOT_SEPARATOR + useCase + DOT_SEPARATOR + DEFAULT_SCENARIO;
    }

    /**
     * Gets identity with default use case.
     *
     * @return the identity with default use case
     */
    public String getIdentityWithDefaultUseCase() {
        return bizId + DOT_SEPARATOR + DEFAULT_USE_CASE + DOT_SEPARATOR + DEFAULT_SCENARIO;
    }
}
