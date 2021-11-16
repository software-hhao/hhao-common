/*
 * Copyright 2018-2021 WangSheng.
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

package com.hhao.common.springboot.safe.xss;

import org.owasp.validator.html.Policy;

/**
 * 规则执行器
 *
 * @author Wang
 * @since 1.0.0
 */
public interface XssPolicyHandler {

    /**
     * 输入规则名称，判断该规则是否支持
     *
     * @param name the name
     * @return the boolean
     */
    boolean support(String name);


    /**
     * 返回policy
     *
     * @return the policy
     */
    Policy getPolicy();
}
