
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

package com.hhao.common.sprintboot.webflux.config.exception;

import com.hhao.common.exception.ExceptionTransfer;
import com.hhao.common.exception.error.request.RequestRuntimeException;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.springboot.exception.ValidateRuntimeException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

/**
 * 异常类的转换
 *
 * @author Wang
 * @since 2022/1/14 11:04
 */
public class DefaultExceptionTransfer implements ExceptionTransfer {
    protected final Logger logger = LoggerFactory.getLogger(DefaultExceptionTransfer.class);
    @Override
    public boolean support(Throwable exception) {
        if (exception instanceof WebExchangeBindException
            || exception instanceof ServerWebInputException
        ) {
            return true;
        }
        logger.debug("don't support exception:" + exception.getClass());
        return false;
    }

    @Override
    public Throwable transfer(Throwable exception) {
        if (exception instanceof WebExchangeBindException) {
            //对验证类异常作转换处理
            exception = new ValidateRuntimeException(((WebExchangeBindException) exception).getBindingResult(),exception);
        }else if (exception instanceof ServerWebInputException) {
            exception = new RequestRuntimeException(exception.getCause()==null?exception:exception.getCause());
        };
        return exception;
    }
}
