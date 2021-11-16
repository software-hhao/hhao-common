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

package com.hhao.extend.money.spring;

import com.hhao.extend.money.MoneyFormat;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import javax.money.MonetaryAmount;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Monetary amount annotation formatter factory.
 *
 * @author Wang
 * @since 1.0.0
 */
public class MonetaryAmountAnnotationFormatterFactory implements AnnotationFormatterFactory<MoneyFormat> {
    private Set<Class<?>> fieldTypes = new HashSet<Class<?>>();

    /**
     * Instantiates a new Monetary amount annotation formatter factory.
     */
    public MonetaryAmountAnnotationFormatterFactory(){
        fieldTypes.add(MonetaryAmount.class);
    }

    @Override
    public Set<Class<?>> getFieldTypes() {
        return fieldTypes;
    }

    private Formatter<MonetaryAmount> getFormatter(MoneyFormat format) {
        Formatter<MonetaryAmount> formatter = new MonetaryAmountFormatImpl(format);
        return formatter;
    }


    @Override
    public Printer<?> getPrinter(MoneyFormat annotation, Class<?> fieldType) {
        return getFormatter(annotation);
    }

    @Override
    public Parser<?> getParser(MoneyFormat annotation, Class<?> fieldType) {
        return getFormatter(annotation);
    }
}
