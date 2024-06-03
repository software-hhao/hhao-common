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

package com.hhao.common.extension.annotation;


import com.hhao.common.extension.BizScenario;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;


/**
 * 用于注解扩展类
 *
 * @author Wang
 * @since 1.0.0
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.TYPE})
@Component
public @interface Extension {
    /**
     * 扩展对应的描述
     *
     * @return string
     */
    String desc() default "";


    /**
     * Biz id string.
     *
     * @return the string
     */
    String bizId()  default BizScenario.DEFAULT_BIZ_ID;

    /**
     * Use case string.
     *
     * @return the string
     */
    String useCase() default BizScenario.DEFAULT_USE_CASE;

    /**
     * Scenario string.
     *
     * @return the string
     */
    String scenario() default BizScenario.DEFAULT_SCENARIO;
}
