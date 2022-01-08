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

import javax.money.Monetary;
import javax.money.MonetaryRounding;
import javax.money.RoundingQueryBuilder;
import java.math.RoundingMode;

/**
 * Money类型的精度
 * 格式：Scale:RoundingMode,例如:2:HALF_UP
 *
 * @author Wang
 * @since 1.0.0
 */
public class MonetaryRoundingMetadata implements Metadata<MonetaryRounding> {
    protected final Logger logger = LoggerFactory.getLogger(MonetaryRoundingMetadata.class);
    private final String NAME = "MONETARY_ROUNDING";
    private String value = "2:HALF_UP";
    private Integer scale=2;
    private RoundingMode roundingMode=RoundingMode.HALF_UP;
    private MonetaryRounding rounding = Monetary.getRounding(
            RoundingQueryBuilder.of().setScale(2).set(RoundingMode.HALF_UP).build()
    );

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
    public MonetaryRounding getMetadata() {
        return rounding;
    }

    @Override
    public MonetaryRounding update(String value) {
        if (value == null) {
            logger.debug("value format is CurrencyStyle:PATTERN,example:zh-CN:SYMBOL:¤####,###0.00");
            return rounding;
        }
        String[] values = value.split(":");
        if (values == null || values.length != 2) {
            logger.debug("value format is CurrencyStyle:PATTERN,example:zh-CN:SYMBOL:¤####,###0.00");
            return rounding;
        }

        try {
            scale=Integer.parseInt(values[0]);
            roundingMode=findRoundingMode(values[1]);

            rounding = Monetary.getRounding(
                    RoundingQueryBuilder.of()
                            .setScale(scale)
                            .set(roundingMode)
                            .build()
            );
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return rounding;
    }

    /**
     * Find rounding mode rounding mode.
     *
     * @param mode the mode
     * @return the rounding mode
     */
    protected RoundingMode findRoundingMode(String mode) {
        for (RoundingMode roundingMode : RoundingMode.values()) {
            if (roundingMode.name().equals(mode)) {
                return roundingMode;
            }
        }
        return RoundingMode.HALF_UP;
    }

    public Integer getScale() {
        return scale;
    }

    public RoundingMode getRoundingMode() {
        return roundingMode;
    }
}
