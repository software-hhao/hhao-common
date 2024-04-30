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

import java.util.HashMap;
import java.util.Map;

public class MonetaryConfig {
    private String defaultCurrency = "CNY";
    private String patternPlaceSymbol = "¤";
    private Map<String, CurrencyConfig> currencyConfigurations = new HashMap<>();

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public Map<String, CurrencyConfig> getCurrencyConfigurations() {
        return currencyConfigurations;
    }

    public void setCurrencyConfigurations(Map<String, CurrencyConfig> currencyConfigurations) {
        this.currencyConfigurations = currencyConfigurations;
    }

    public void addCurrencyConfig(CurrencyConfig currencyConfig) {
        this.currencyConfigurations.put(currencyConfig.getCurrencyCode(), currencyConfig);
    }

    public String getPatternPlaceSymbol() {
        return patternPlaceSymbol;
    }

    public void setPatternPlaceSymbol(String patternPlaceSymbol) {
        this.patternPlaceSymbol = patternPlaceSymbol;
    }

}
