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

package com.hhao.common.springboot.web.config.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * 可以重写RequestMappingHandlerMapping，进行拦截处理
 *
 * @author Wang
 * @since 1.0.0
 */
public class CustomRequestMappingHandlerMapping  extends RequestMappingHandlerMapping {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(CustomRequestMappingHandlerMapping.class);

    /**
     * Gets mapping for method.
     *
     * @param method      the method
     * @param handlerType the handler type
     * @return the mapping for method
     */
    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        logger.info("{}:{}",method.toString(),handlerType.toString());

        RequestMappingInfo mappinginfo = super.getMappingForMethod(method, handlerType);
        return mappinginfo;
    }

}
