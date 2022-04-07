/*
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
package com.hhao.common;

/**
 * The interface Core constant.
 *
 * @author Wang
 * @since 1.0.0
 */
public interface CoreConstant {
    /**
     * 默认成功的状态值
     */
    int DEFAULT_SUCCEED_STATUS = 200;
    /**
     * 默认成功
     */
    String DEFAULT_SUCCEED_MESSAGE = "OK";
    /**
     * The constant DEFAULT_EXCEPTION_STATUS.
     */
    int DEFAULT_EXCEPTION_STATUS=500;
    /**
     * The constant DEFAULT_EXCEPTION_MESSAGE.
     */
    String DEFAULT_EXCEPTION_MESSAGE="ERROR";


    String DEFAULT_BIZ_ID = "#defaultBizId#";
    String DEFAULT_USE_CASE = "#defaultUseCase#";
    String DEFAULT_SCENARIO = "#defaultScenario#";
}
