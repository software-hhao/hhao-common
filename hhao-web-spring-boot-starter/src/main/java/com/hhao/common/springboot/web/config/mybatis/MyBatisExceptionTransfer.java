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

package com.hhao.common.springboot.web.config.mybatis;

import com.hhao.common.exception.ExceptionTransfer;
import com.hhao.common.exception.error.server.ConnectException;
import com.hhao.common.exception.error.server.ServerRuntimeException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;

/**
 * 错误转换类
 *
 * @author Wang
 * @since 1.0.0
 */
public class MyBatisExceptionTransfer implements ExceptionTransfer {
    private Class<? extends Throwable>[] supportExceptions = new Class[]{
            BadSqlGrammarException.class,
            MyBatisSystemException.class,
            DataIntegrityViolationException.class
    };

    @Override
    public boolean support(Throwable exception) {
        for (Class throwable : supportExceptions) {
            if (throwable.isAssignableFrom(exception.getClass())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Throwable transfer(Throwable exception) {
        if (exception instanceof MyBatisSystemException) {
            MyBatisSystemException myBatisSystemException = (MyBatisSystemException) exception;
            if (myBatisSystemException.getRootCause() != null && myBatisSystemException.getRootCause() instanceof java.net.ConnectException) {
                return new ConnectException(exception);
            }
            return new ServerRuntimeException(exception);
        }
        return new ServerRuntimeException(exception);
    }
}
