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
package com.hhao.common.local;

import com.hhao.common.Context;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.metadata.SystemMetadata;

import java.util.Arrays;
import java.util.Locale;

/**
 * @author Wan
 * @since 2024/3/22 18:00
 */
public class LocaleBuilder {
    static final Logger logger = LoggerFactory.getLogger(LocaleBuilder.class);
    private static LocaleBuilder INSTANCE=new LocaleBuilder();

    private LocaleBuilder() {
        // 私有构造函数，防止外部直接实例化
    }

    public static LocaleBuilder getINSTANCE() {
        return INSTANCE;
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
    
    public Locale findLocale(String value){
        if (value == null || value.isBlank()) {
            logger.warn("The locale input is empty or null.");
            return null;
        }
        Locale locale=null;
        if ("default".equals(value)){
            //走元数据定义的locale
            locale= SystemMetadata.getInstance().getLocale();
            if (locale!=null){
                return locale;
            }
        }
        if ("system".equals(value)){
            //走系统定义
            locale=Locale.getDefault();
            if (locale!=null){
                return locale;
            }
        }
        if ("context".equals(value)){
            //先走context，再走元数据
            locale= Context.getInstance().getLocale();
            if (locale!=null){
                return locale;
            }
        }
        //语言-国家解析
        String [] values=value.split("-|_");
        if (values.length != 1 && values.length != 2) {
            logger.warn("The locale format is incorrect. It should be language-country, for example, zh-CN.");
            return null;
        }
        return resolveLocaleFromValues(values);
    }

    /**
     * 根据解析出的语言和国家代码私有方法，返回匹配的Locale
     *
     * @param values 解析后的语言和国家代码数组
     * @return Locale 匹配的Locale对象，如果无匹配则返回null
     */
    private Locale resolveLocaleFromValues(String[] values) {
        Locale[] availableLocales = Locale.getAvailableLocales();
        // 只指定语言或国家
        if (values.length == 1) {
            return findLocaleByLanguageOrCountry(availableLocales, values[0]);
        } else {
            return findLocaleByLanguageAndCountry(availableLocales, values[0], values[1]);
        }
    }

    /**
     * 根据语言或国家代码在可用的Locale中查找匹配项
     *
     * @param availableLocales 可用的Locale数组
     * @param code 语言或国家代码
     * @return Locale 匹配的Locale对象，如果无匹配则返回null
     */
    private Locale findLocaleByLanguageOrCountry(Locale[] availableLocales, String code) {
        Locale localeByLanguage = Arrays.stream(availableLocales)
                .filter(locale -> locale.getLanguage().equals(code))
                .findFirst()
                .orElse(null);
        if (localeByLanguage != null) {
            return localeByLanguage;
        }
        return Arrays.stream(availableLocales)
                .filter(locale -> locale.getCountry().equals(code))
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据语言和国家代码在可用的Locale中查找匹配项
     *
     * @param availableLocales 可用的Locale数组
     * @param language 语言代码
     * @param country 国家代码
     * @return Locale 匹配的Locale对象，如果无匹配则返回null
     */
    private Locale findLocaleByLanguageAndCountry(Locale[] availableLocales, String language, String country) {
        return Arrays.stream(availableLocales)
                .filter(locale -> locale.getLanguage().equals(language) && locale.getCountry().equals(country))
                .findFirst()
                .orElse(null);
    }
}
