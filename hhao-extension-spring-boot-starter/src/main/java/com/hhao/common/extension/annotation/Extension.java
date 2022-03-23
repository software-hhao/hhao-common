
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

package com.hhao.common.extension.annotation;

import com.hhao.common.extension.BizScenario;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 扩展点实现注解，单例
 *
 * @author Wang
 * @since 2022/3/10 12:19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Component
public @interface Extension {
    String bizId()  default BizScenario.DEFAULT_BIZ_ID;
    String useCase() default BizScenario.DEFAULT_USE_CASE;
    String scenario() default BizScenario.DEFAULT_SCENARIO;

//    /**
//     * 适用条件
//     *
//     * @return
//     */
//    String condition() default "";
//
//    /**
//     * 排序，大的优先级高
//     *
//     * @return
//     */
//    int order() default Ordered.LOWEST_PRECEDENCE;
//
//    /**
//     * 覆盖其它低的同名扩展
//     *
//     * @return
//     */
//    boolean override() default true;
//
//    /**
//     * 排斥其它低的扩展
//     *
//     * @return
//     */
//    String [] rejection() default {};
}
