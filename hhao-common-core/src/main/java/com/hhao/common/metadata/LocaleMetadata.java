
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

package com.hhao.common.metadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Locale;

/**
 * Local元数据
 * 根据Local字符串，查找最匹配的Local
 * Local字符串格式如下：
 * [system|context|语言-国家]
 * system:走系统默认
 * context:走上下文
 * 语言-国家如zh-CN,en-US
 *
 * @author Wang
 * @since  1.0.0
 */
public class LocaleMetadata implements Metadata<Locale> {
    protected final Logger logger = LoggerFactory.getLogger(LocaleMetadata.class);
    private final String NAME = "LOCALE";
    private String value="system";
    private Locale locale = Locale.getDefault();

    @Override
    public boolean support(String name) {
        if (name == null) {
            return false;
        }
        if (NAME.equals(name)) {
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public Locale getMetadata() {
        return locale;
    }

    /**
     * @param value:格式：语言-国家，如：zh-CN、en-US
     * @return
     */
    @Override
    public Locale update(String value) {
        if (value==null){
            logger.debug("The locale format is language-country, for example, zh-CN");
            return locale;
        }
        String [] values=value.split("-|_");
        if (values==null || values.length!=2){
            logger.debug("The locale format is language-country, for example, zh-CN");
            return locale;
        }
        this.locale = Arrays.stream(Locale.getAvailableLocales()).filter(locale -> {
            if (locale.getLanguage().equals(values[0])){
                return true;
            }
            return false;
        }).filter(locale -> {
            if (locale.getCountry().equals(values[1])){
                return true;
            }
            return false;
        }).findFirst().orElse(locale);
        this.value=value;
        return this.locale;
    }
}

