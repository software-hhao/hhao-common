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

package com.hhao.common;

import com.hhao.common.lang.Nullable;
import com.hhao.common.metadata.Mdm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 全局上下文接口
 *
 * @author Wang
 * @since 1.0.0
 */
public interface Context {
    static final Logger logger = LoggerFactory.getLogger(Context.class);

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
    @Nullable
    static Locale findLocale(String value){
        Locale resultLocale=null;
        //走元数据定义的locale
        if ("default".equals(value)){
            resultLocale=Mdm.LOCALE.value(Locale.class);
        }
        //走系统定义
        if ("system".equals(value)){
            resultLocale= Locale.getDefault();
        }
        //先走context，再走元数据
        if (resultLocale==null && "context".equals(value)){
            resultLocale=Context.getInstance().getLocale();
        }

        if (resultLocale!=null){
            return resultLocale;
        }
        //语言-国家解析
        String [] values=value.split("-|_");
        if (values==null) {
            logger.debug("The locale format is language-country, for example, zh-CN");
            return null;
        }
        if (values.length!=1 && values.length!=2){
            logger.debug("The locale format is language-country, for example, zh-CN");
            return null;
        }
        //只指定语言或国家
        //先按语言优先找，再按国家找
        if (values.length==1){
            if (resultLocale==null){
                resultLocale=Arrays.stream(Locale.getAvailableLocales()).filter(locale -> {
                    if (locale.getLanguage().equals(values[0])){
                        return true;
                    }
                    return false;
                }).findFirst().orElse(null);
            }
            resultLocale=Arrays.stream(Locale.getAvailableLocales()).filter(locale -> {
                if (locale.getCountry().equals(values[0])){
                    return true;
                }
                return false;
            }).findFirst().orElse(null);
        }else{
            resultLocale=Arrays.stream(Locale.getAvailableLocales()).filter(locale -> {
                if (locale.getLanguage().equals(values[0])){
                    return true;
                }
                return false;
            }).filter(locale -> {
                if (locale.getCountry().equals(values[1])){
                    return true;
                }
                return false;
            }).findFirst().orElse(null);
        }
        return resultLocale;
    }

    /**
     * 返回消息文件的内容
     *
     * @param code
     * @param args
     * @param locale
     * @return
     */
    String getMessage(String code, @Nullable Object[] args, Locale locale);

    /***
     * Context工厂类
     */
    class ContextFactory {
        private static Context instance;

        public synchronized static Context create(Context context) {
            instance = context;
            return instance;
        }

        public synchronized static Context create(Supplier<Context> supplier) {
            instance = supplier.get();
            return instance;
        }
    }

    /***
     * 获取上下文对象，需要先调用工厂类初始化
     * @param <T>  the type parameter
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
