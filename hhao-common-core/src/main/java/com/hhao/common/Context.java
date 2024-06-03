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

package com.hhao.common;

import com.hhao.common.locale.LocaleBuilder;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.metadata.SystemMetadata;
import com.hhao.common.support.Version;

import java.time.ZoneId;
import java.util.*;
import java.util.function.Supplier;

/**
 * 全局上下文接口
 *
 * @author Wang
 * @since 1.0.0
 */
public interface Context {
    /**
     * The constant logger.
     */
    Logger logger = LoggerFactory.getLogger(Context.class);

    /**
     * Gets locale.
     *
     * @return the locale
     */
    Locale getLocale();

    /**
     * Gets zone id.
     *
     * @return the zone id
     */
    ZoneId getZoneId();

    /**
     * Get version version.
     *
     * @return the version
     */
    default Version getVersion(){
        return SystemMetadata.getInstance().getVersion();
    }

    /**
     * 根据指定的locale字符串，返回最适合的Locale
     * 指定Locale的字符串:
     * default:采用元数据设置的locale
     * context:采用用户上下文的locale
     * 指定语言和国家,如:zh-CN
     * 先按语言优先找，再按国家找
     * 没找到返回元数据设置的locale
     * locale字符串形如：zh-CN、zh、CN
     *
     * @param value 指定locale的字符串
     * @return Locale locale
     */
    
    static Locale findLocale(String value){
        return LocaleBuilder.getINSTANCE().findLocale(value);
    }

    /**
     * 返回消息文件的内容
     *
     * @param code   the code
     * @param args   the args
     * @param locale the locale
     * @return message
     */
    String getMessage(String code, Object[] args, Locale locale);

    /***
     * Context工厂类
     */
    class ContextFactory {
        private static volatile Context instance;

        static{
            Context defaultContext = new Context() {
                // 使用Map来缓存ResourceBundle实例，提高性能
                private static final Map<Locale, ResourceBundle> resourceBundleCache = new HashMap<>();

                @Override
                public Locale getLocale() {
                    return SystemMetadata.getInstance().getLocale();
                }

                @Override
                public ZoneId getZoneId() {
                    return SystemMetadata.getInstance().getZoneId();
                }

                /**
                 * 获取国际化消息。
                 *
                 * @param code 消息代码
                 * @param args 替换占位符的参数数组
                 * @param locale 本地化对象
                 * @return 国际化后的消息字符串，如果找不到则返回错误标记
                 */
                @Override
                public String getMessage(String code, Object[] args, Locale locale) {
                    // 输入验证
                    if (code == null || code.trim().isEmpty()) {
                        return ""; // 返回空字符串作为无效code的处理
                    }

                    ResourceBundle resourceBundle = getResourceBundle(locale); // 使用缓存或加载ResourceBundle

                    try {
                        // 尝试获取消息并格式化
                        String message = resourceBundle.getString(code);
                        return args == null || args.length == 0 ? message : String.format(message, args);
                    } catch (MissingResourceException e) {
                        return "ERROR: Missing message for " + code;
                    }
                }

                /**
                 * 根据locale获取或加载ResourceBundle实例，并利用缓存提高效率。
                 *
                 * @param locale 本地化对象
                 * @return 匹配的ResourceBundle实例
                 */
                private ResourceBundle getResourceBundle(Locale locale) {
                    return resourceBundleCache.computeIfAbsent(locale, this::loadResourceBundle);
                }

                /**
                 * 加载ResourceBundle实例。
                 *
                 * @param locale 本地化对象
                 * @return 新加载的ResourceBundle实例
                 */
                private ResourceBundle loadResourceBundle(Locale locale) {
                    try {
                        // 首先尝试从类路径根加载
                        return ResourceBundle.getBundle("messages", locale);
                    } catch (MissingResourceException e1) {
                        // 类路径下未找到，尝试从i18n目录加载
                        try {
                            return ResourceBundle.getBundle("i18n/messages", locale);
                        } catch (MissingResourceException e2) {
                            // 如果两个位置都未找到，记录详细错误信息并抛出异常
                            throw new RuntimeException("Failed to load ResourceBundle from both default location and i18n directory for locale: " + locale, e2);
                        }
                    }
                }
            };
            instance = defaultContext;
        }

        /**
         * Create context.
         *
         * @param context the context
         * @return the context
         */
        public static Context setContext(Context context) {
            synchronized (ContextFactory.class) {
                instance = context;
            }
            return instance;
        }

        /**
         * Create context.
         *
         * @param supplier the supplier
         * @return the context
         */
        public static Context setContext(Supplier<Context> supplier) {
            synchronized (ContextFactory.class) {
                instance = supplier.get();
            }
            return instance;
        }
    }

    /***
     * 获取上下文对象，需要先调用工厂类初始化
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return the instance
     */
    static <T extends Context> T getInstance(Class<T> clazz) {
        Objects.requireNonNull(ContextFactory.instance, "please init Context.ContextFactory.create");
        return (T) ContextFactory.instance;
    }

    /***
     * 获取上下文对象，需要先调用工厂类初始化
     * @return the instance
     */
    static Context getInstance() {
        Objects.requireNonNull(ContextFactory.instance, "please init Context.ContextFactory.create");
        return ContextFactory.instance;
    }
}
