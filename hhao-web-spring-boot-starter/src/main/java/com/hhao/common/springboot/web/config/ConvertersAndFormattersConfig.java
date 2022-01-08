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

package com.hhao.common.springboot.web.config;

import com.hhao.common.metadata.Mdm;
import com.hhao.common.springboot.convert.*;
import com.hhao.common.springboot.format.DateTimeAnnotationFormatterFactory;
import com.hhao.extend.money.spring.CurrencyUnitAndStringConvert;
import com.hhao.extend.money.spring.MonetaryAmountAndStringConverter;
import com.hhao.extend.money.spring.MonetaryAmountAnnotationFormatterFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import java.time.format.DateTimeFormatter;

/**
 * The type Converters and formatters config.
 *
 * @author Wan
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(ConvertersAndFormattersConfig.class)
@ConditionalOnProperty(prefix = "com.hhao.config.converters-and-formatters",name = "enable",havingValue = "true",matchIfMissing = true)
public class ConvertersAndFormattersConfig extends AbstractBaseMvcConfig {

    /***
     * 转换与格式化处理
     **/
    @Override
    public void addFormatters(FormatterRegistry registry) {
        //日期时间类与字符串互换
        //registry.addConverter(new InstantAndLocalDateTimeStringConverter());
        registry.addConverter(new InstantAndNumberStringConverter());
        registry.addConverter(new LocalDateAndStringConverter());
        registry.addConverter(new LocalTimeAndStringConverter());
        registry.addConverter(new LocalDateTimeAndStringConverter());
        registry.addConverter(new ZonedDateTimeAndStringConverter());
        //Enum与String的互转
        registry.addConverterFactory(new StringToEnumConverterFactory());
        registry.addConverterFactory(new EnumToStringConverterFactory());
        //Enum与Integer的互转
        registry.addConverterFactory(new IntegerToEnumConverterFactory());
        registry.addConverterFactory(new EnumToIntegerConvertFactory());
        //FiledError与String的转换
        registry.addConverterFactory(new FieldErrorToStringConvertFactory());
        //Money与String的转换
        registry.addConverter(new MonetaryAmountAndStringConverter());
        registry.addConverter(new CurrencyUnitAndStringConvert());

        //Instant格式化转换类,根据注解DateTimeFormat进行转换
        registry.addFormatterForFieldAnnotation(new DateTimeAnnotationFormatterFactory());
        registry.addFormatterForFieldAnnotation(new MonetaryAmountAnnotationFormatterFactory());
    }

    /**
     * 转换与格式化处理
     *
     * @return the formatting conversion service
     */
    @Bean
    public FormattingConversionService conversionService() {
        // Use the DefaultFormattingConversionService but do not register defaults
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);

        // Ensure @NumberFormat is still supported
        conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());

        // Register JSR-310 date conversion with a specific global format
        DateTimeFormatterRegistrar dateTimeFormatterRegistrar = new DateTimeFormatterRegistrar();
        dateTimeFormatterRegistrar.setDateFormatter(Mdm.DATE_FORMATTER.value(DateTimeFormatter.class));
        dateTimeFormatterRegistrar.setTimeFormatter(Mdm.TIME_FORMATTER.value(DateTimeFormatter.class));
        dateTimeFormatterRegistrar.setDateTimeFormatter(Mdm.DATE_TIME_FORMATTER.value(DateTimeFormatter.class));
        dateTimeFormatterRegistrar.registerFormatters(conversionService);

        // Register date conversion with a specific global format
        DateFormatterRegistrar dateFormatterRegistrar = new DateFormatterRegistrar();
        dateFormatterRegistrar.setFormatter(new DateFormatter(Mdm.DATE_FORMATTER.metadata().getValue()));
        dateFormatterRegistrar.registerFormatters(conversionService);

        return conversionService;
    }


}
