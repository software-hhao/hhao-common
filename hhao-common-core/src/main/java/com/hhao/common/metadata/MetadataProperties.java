package com.hhao.common.metadata;
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

public class MetadataProperties {
    private Formatters formatters = new Formatters();
    private String timezone = "UTC+8";
    //[system|context|语言-国家]
    //system:走系统默认
    //context:走上下文
    //语言-国家如zh-CN,en-US
    private String locale = "zh-CN";
    private String version = "::1.1.1-SNAPSHOT";
    private MonetaryConfig monetaryConfig = new MonetaryConfig();

    public Formatters getFormatters() {
        return formatters;
    }

    public void setFormatters(Formatters formatters) {
        this.formatters = formatters;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public MonetaryConfig getMonetaryConfig() {
        return monetaryConfig;
    }

    public void setMonetaryConfig(MonetaryConfig monetaryConfig) {
        this.monetaryConfig = monetaryConfig;
    }
}