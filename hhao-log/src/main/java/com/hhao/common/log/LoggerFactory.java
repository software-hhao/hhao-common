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

package com.hhao.common.log;

/**
 * The type Logger factory.
 *
 * @author Wang
 * @since 1.0.0
 */
public class LoggerFactory {
    private static ILoggerFactory loggerFactory=new DefaultLoggerFactory();

    /**
     * Sets logger factory.
     *
     * @param customLoggerFactory the custom logger factory
     */
    public static void setLoggerFactory(ILoggerFactory customLoggerFactory) {
        synchronized (LoggerFactory.class) {
            loggerFactory = customLoggerFactory;
        }
    }

    /**
     * Gets logger.
     *
     * @param clazz the clazz
     * @return the logger
     */
    public static Logger getLogger(Class<?> clazz) {
        return loggerFactory.getLogger(clazz);
    }
}
