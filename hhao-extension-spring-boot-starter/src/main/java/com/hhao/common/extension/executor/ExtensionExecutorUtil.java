package com.hhao.common.extension.executor;/*
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

import com.hhao.common.exception.error.request.IllegalArgumentException;

/**
 * @author Wang
 * @since 1.0.0
 */
public class ExtensionExecutorUtil {
    private static AbstractComponentExecutor executor;

    public static AbstractComponentExecutor getExecutor() {
        if (executor==null){
            throw new IllegalArgumentException("executor not initialized");
        }
        return executor;
    }

    public static void setExecutor(AbstractComponentExecutor executor) {
        ExtensionExecutorUtil.executor = executor;
    }
}
