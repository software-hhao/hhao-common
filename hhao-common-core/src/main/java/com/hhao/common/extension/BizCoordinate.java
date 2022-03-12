
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

package com.hhao.common.extension;

/**
 * 扩展点的业务座标
 *
 * @author Wang
 * @since 2022/3/10 14:45
 */
public class BizCoordinate {
    public final static String DEFAULT_BIZ_ID = "#defaultBizId#";
    public final static String DEFAULT_USE_CASE = "#defaultUseCase#";
    public final static String DEFAULT_SCENARIO = "#defaultScenario#";
    private final static String DOT_SEPARATOR = ".";

    /**
     * bizId is used to identify a business, such as "tmall", it's nullable if there is only one biz
     */
    private String bizId = DEFAULT_BIZ_ID;

    /**
     * useCase is used to identify a use case, such as "placeOrder", can not be null
     */
    private String useCase = DEFAULT_USE_CASE;

    /**
     * scenario is used to identify a use case, such as "88vip","normal", can not be null
     */
    private String scenario = DEFAULT_SCENARIO;

    /**
     * For above case, the BizScenario will be "tmall.placeOrder.88vip",
     * with this code, we can provide extension processing other than "tmall.placeOrder.normal" scenario.
     *
     * @return
     */
    public String getUniqueIdentity(){
        return bizId + DOT_SEPARATOR + useCase + DOT_SEPARATOR + scenario;
    }

    public static BizCoordinate valueOf(String bizId, String useCase, String scenario){
        BizCoordinate bizCoordinate = new BizCoordinate();
        bizCoordinate.bizId = bizId;
        bizCoordinate.useCase = useCase;
        bizCoordinate.scenario = scenario;
        return bizCoordinate;
    }

    public static BizCoordinate valueOf(String bizId, String useCase){
        return BizCoordinate.valueOf(bizId, useCase, DEFAULT_SCENARIO);
    }

    public static BizCoordinate valueOf(String bizId){
        return BizCoordinate.valueOf(bizId, DEFAULT_USE_CASE, DEFAULT_SCENARIO);
    }

    public static BizCoordinate newDefault(){
        return BizCoordinate.valueOf(DEFAULT_BIZ_ID, DEFAULT_USE_CASE, DEFAULT_SCENARIO);
    }

    public String getIdentityWithDefaultScenario(){
        return bizId + DOT_SEPARATOR + useCase + DOT_SEPARATOR + DEFAULT_SCENARIO;
    }

    public String getIdentityWithDefaultUseCase(){
        return bizId + DOT_SEPARATOR + DEFAULT_USE_CASE + DOT_SEPARATOR + DEFAULT_SCENARIO;
    }
}
