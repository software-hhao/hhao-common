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

package com.hhao.common.springboot;

import com.hhao.common.Context;
import org.springframework.context.ApplicationContext;

/**
 * The interface Web context.
 *
 * @author Wang
 * @since 1.0.0
 */
public interface AppContext extends Context {
    /**
     * Application context application context.
     *
     * @return the application context
     */
    ApplicationContext applicationContext();


    /***
     * 获取上下文对象，需要先调用工厂类初始化
     * @return com.hhao.common.entity.Context instance
     */
    static AppContext getInstance() {
        return Context.getInstance(AppContext.class);
    }
}
