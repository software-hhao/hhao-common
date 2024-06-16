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

import com.hhao.common.Context;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.format.CurrencyStyle;

import javax.money.*;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Locale;

public class CurrencyConfig {
    private String currencyCode;
    private String locale;
    private String displayPattern = "SYMBOL:¤#,###0.00";
    private String parsePattern = "CODE:¤#,###0.00";
    private int precision = 16;
    private int scale = 2;
    private RoundingMode roundingMode = RoundingMode.HALF_EVEN;
    private boolean fixedScale = true;

    public CurrencyConfig() {
    }

    public CurrencyConfig(String currencyCode, String locale, String displayPattern, String parsePattern, Integer precision, Integer scale, RoundingMode roundingMode, Boolean fixedScale) {
        this.currencyCode = currencyCode;
        this.locale = locale;
        this.displayPattern = displayPattern;
        this.parsePattern = parsePattern;
        this.precision = precision;
        this.scale = scale;
        this.roundingMode = roundingMode;
        this.fixedScale = fixedScale;
    }

    public CurrencyConfig(String currencyCode, String locale) {
        this.currencyCode = currencyCode;
        this.locale = locale;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

//    public String getDisplayPattern() {
//        return displayPattern;
//    }

    public void setDisplayPattern(String displayPattern) {
        this.displayPattern = displayPattern;
    }

//    public String getParsePattern() {
//        return parsePattern;
//    }

    public void setParsePattern(String parsePattern) {
        this.parsePattern = parsePattern;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public RoundingMode getRoundingMode() {
        return roundingMode;
    }

    public void setRoundingMode(RoundingMode roundingMode) {
        this.roundingMode = roundingMode;
    }

    public boolean isFixedScale() {
        return fixedScale;
    }

    public void setFixedScale(boolean fixedScale) {
        this.fixedScale = fixedScale;
    }

    private MonetaryContext monetaryContext = null;

    public MonetaryContext getMonetaryContext() {
        if (monetaryContext == null) {
            synchronized (this) {
                if (monetaryContext == null) {
                    monetaryContext = MonetaryContextBuilder.of(Money.class)
                            .setMaxScale(scale)
                            .setFixedScale(fixedScale)
                            .setPrecision(precision)
                            .build();
                }
            }
        }
        return monetaryContext;
    }

    private MonetaryRounding monetaryRounding = null;

    public MonetaryRounding getMonetaryRounding() {
        if (monetaryRounding == null) {
            synchronized (this) {
                if (monetaryRounding == null) {
                    monetaryRounding = Monetary.getRounding(
                            RoundingQueryBuilder.of().setScale(scale).set(roundingMode).build()
                    );
                }
            }
        }
        return monetaryRounding;
    }

    public MathContext getMathContext() {
        return new MathContext(precision, roundingMode);
    }

    private Locale currencyLocale = null;

    public Locale getCurrencyLocale() {
        if (currencyLocale == null) {
            synchronized (this) {
                if (currencyLocale == null) {
                    currencyLocale = Context.findLocale(this.getLocale());
                }
            }
        }
        return currencyLocale;
    }

    private CurrencyUnit currencyUnit = null;

    public CurrencyUnit getCurrencyUnit() {
        if (currencyUnit == null) {
            synchronized (this) {
                if (currencyUnit == null) {
                    currencyUnit = Monetary.getCurrency(this.getCurrencyCode());
                }
            }
        }
        return currencyUnit;
    }

    protected CurrencyStyle getCurrencyStyle(String currencyStyle) {
        switch (CurrencyStyle.valueOf(currencyStyle)) {
            case SYMBOL:
                return CurrencyStyle.SYMBOL;
            case CODE:
                return CurrencyStyle.CODE;
            case NAME:
                return CurrencyStyle.NAME;
            case NUMERIC_CODE:
                return CurrencyStyle.NUMERIC_CODE;
            default:
                throw new IllegalArgumentException("Invalid currency style: " + currencyStyle);
        }
    }

    public String[] parsePattern(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Invalid pattern: " + pattern);
        }
        String[] values = pattern.split(":");
        if (values == null || values.length != 2) {
            throw new IllegalArgumentException("Invalid pattern: " + pattern);
        }
        return values;
    }

    private CurrencyStyle currencyStyleForDisplay = null;
    private String patternForDisplay = null;

    public String getPatternForDisplay() {
        if (patternForDisplay == null) {
            synchronized (this) {
                if (patternForDisplay == null) {
                    String[] values = this.parsePattern(this.displayPattern);
                    currencyStyleForDisplay = this.getCurrencyStyle(values[0]);
                    patternForDisplay = values[1].trim();
                }
            }
        }
        return patternForDisplay;
    }

    public CurrencyStyle getCurrencyStyleForDisplay() {
        if (currencyStyleForDisplay == null) {
            synchronized (this) {
                if (currencyStyleForDisplay == null) {
                    String[] values = this.parsePattern(this.displayPattern);
                    currencyStyleForDisplay = this.getCurrencyStyle(values[0]);
                    patternForDisplay = values[1].trim();
                }
            }
        }
        return currencyStyleForDisplay;
    }

    private CurrencyStyle currencyStyleForParse = null;
    private String patternForParse = null;

    public String getPatternForParse() {
        if (patternForParse == null) {
            synchronized (this) {
                if (patternForParse == null) {
                    String[] values = this.parsePattern(this.parsePattern);
                    currencyStyleForParse = this.getCurrencyStyle(values[0]);
                    patternForParse = values[1].trim();
                }
            }
        }
        return patternForParse;
    }

    public CurrencyStyle getCurrencyStyleForParse() {
        if (currencyStyleForParse == null) {
            synchronized (this) {
                if (currencyStyleForParse == null) {
                    String[] values = this.parsePattern(this.parsePattern);
                    currencyStyleForParse = this.getCurrencyStyle(values[0]);
                    patternForParse = values[1].trim();
                }
            }
        }
        return currencyStyleForParse;
    }
}
