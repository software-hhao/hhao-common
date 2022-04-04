
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

package com.hhao.common.extension.model;

import org.springframework.core.Ordered;

/**
 * 扩展点接口定义，从该接口继承
 *
 * @author Wang
 * @since 2022/3/10 22:50
 */
public interface ExtensionPoint extends Ordered {
    @Override
    default int getOrder(){
        return Ordered.LOWEST_PRECEDENCE;
    }
}
