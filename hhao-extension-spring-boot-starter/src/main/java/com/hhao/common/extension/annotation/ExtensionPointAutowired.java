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

package com.hhao.common.extension.annotation;

import com.hhao.common.extension.strategy.DefaultInterruptionStrategy;
import com.hhao.common.extension.strategy.InterruptionStrategy;

import java.lang.annotation.*;


/**
 * The interface Extension point autowired.
 *
 * @author Wang
 * @since 1.0.0
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExtensionPointAutowired {
    /**
     * Model model.
     *
     * @return the model
     */
    Model model () default Model.SIMPLE;

    /**
     * 组合扩展点时，返回值的组合策略
     *
     * @return the class
     */
    Class<? extends InterruptionStrategy> interruptionStrategy() default DefaultInterruptionStrategy.class;

    /**
     * The enum Model.
     */
    enum Model{
        /**
         * 单一扩展点
         */
        SIMPLE,
        /**
         * 组合扩展点
         */
        MULTI
    }
}
