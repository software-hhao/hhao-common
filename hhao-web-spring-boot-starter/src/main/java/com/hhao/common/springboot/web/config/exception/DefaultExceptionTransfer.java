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

package com.hhao.common.springboot.web.config.exception;

import com.hhao.common.exception.DefaultErrorCodes;
import com.hhao.common.exception.ExceptionTransfer;
import com.hhao.common.exception.error.SystemRuntimeException;
import com.hhao.common.exception.error.request.ConvertException;
import com.hhao.common.exception.error.request.RequestRuntimeException;
import com.hhao.common.exception.error.server.ServerRuntimeException;
import com.hhao.common.springboot.exception.ValidateRuntimeException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.money.MonetaryException;
import javax.money.format.MonetaryParseException;
import java.io.IOException;
import java.time.format.DateTimeParseException;

/**
 * The type Default exception transfer.
 *
 * @author Wang
 * @since 1.0.0
 */
public class DefaultExceptionTransfer implements ExceptionTransfer {
    @Override
    public boolean support(Throwable exception) {
        if (exception instanceof MethodArgumentTypeMismatchException
                || exception instanceof HttpRequestMethodNotSupportedException
                || exception instanceof HttpMediaTypeNotSupportedException
                || exception instanceof HttpMediaTypeNotAcceptableException
                || exception instanceof MissingPathVariableException
                || exception instanceof MissingServletRequestParameterException
                || exception instanceof ServletRequestBindingException
                || exception instanceof ConversionNotSupportedException
                || exception instanceof TypeMismatchException
                || exception instanceof HttpMessageNotReadableException
                || exception instanceof HttpMessageNotWritableException
                || exception instanceof MethodArgumentNotValidException
                || exception instanceof MissingServletRequestPartException
                || exception instanceof BindingResult
                || exception instanceof NoHandlerFoundException
                || exception instanceof AsyncRequestTimeoutException
                || exception instanceof IOException
                || exception instanceof HttpMessageConversionException
                || exception instanceof MonetaryParseException
                || exception instanceof MonetaryException
                || exception instanceof ConstraintViolationException
                || exception instanceof RuntimeException
        ) {
            return true;
        }
        return false;
    }

    @Override
    public Throwable transfer(Throwable exception) {
        if (exception instanceof MethodArgumentTypeMismatchException) {
            //对请求参数缺失错误做转换处理
            MethodArgumentTypeMismatchException ex = (MethodArgumentTypeMismatchException) exception;
            if (ex.getRootCause() != null && ex.getRootCause() instanceof DateTimeParseException) {
                exception = new ConvertException();
            } else {
                exception = new RequestRuntimeException(exception);
            }
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            exception = new RequestRuntimeException(DefaultErrorCodes.ERROR_405, exception);
        } else if (exception instanceof HttpMediaTypeNotSupportedException) {
            exception = new RequestRuntimeException(DefaultErrorCodes.ERROR_415, exception);
        } else if (exception instanceof HttpMediaTypeNotAcceptableException) {
            exception = new RequestRuntimeException(DefaultErrorCodes.ERROR_406, exception);
        } else if (exception instanceof MissingPathVariableException) {
            exception = new ServerRuntimeException(DefaultErrorCodes.ERROR_500, exception);
        } else if (exception instanceof MissingServletRequestParameterException) {
            exception = new RequestRuntimeException(DefaultErrorCodes.ERROR_40X, exception);
        } else if (exception instanceof ServletRequestBindingException) {
            exception = new RequestRuntimeException(DefaultErrorCodes.ERROR_40X, exception);
        } else if (exception instanceof ConversionNotSupportedException) {
            exception = new ServerRuntimeException(DefaultErrorCodes.ERROR_500, exception);
        } else if (exception instanceof TypeMismatchException) {
            exception = new RequestRuntimeException(DefaultErrorCodes.ERROR_40X, exception);
        } else if (exception instanceof HttpMessageNotReadableException) {
            //对Spring Convert日期转化错误的处理
            HttpMessageNotReadableException ex = (HttpMessageNotReadableException) exception;
            if (ex.getRootCause() != null) {
                Throwable e = ex.getRootCause();
                if (e instanceof DateTimeParseException) {
                    exception = new ConvertException();
                } else if (e instanceof MonetaryParseException || e instanceof MonetaryException) {
                    exception = new RequestRuntimeException(DefaultErrorCodes.ERROR_400_MONEY, exception);
                } else {
                    exception = new RequestRuntimeException(DefaultErrorCodes.ERROR_40X, exception);
                }
            }
        } else if (exception instanceof HttpMessageNotWritableException) {
            exception = new ServerRuntimeException(DefaultErrorCodes.ERROR_500, exception);
        } else if (exception instanceof MethodArgumentNotValidException) {
            //对验证类异常作转换处理
            exception = new ValidateRuntimeException(((MethodArgumentNotValidException) exception).getBindingResult(), exception);
        } else if (exception instanceof MissingServletRequestPartException) {
            exception = new RequestRuntimeException(DefaultErrorCodes.ERROR_40X, exception);
        } else if (exception instanceof BindingResult) {
            //对验证类异常作转换处理
            exception = new ValidateRuntimeException((BindingResult) exception, exception);
        } else if (exception instanceof NoHandlerFoundException) {
            exception = new RequestRuntimeException(DefaultErrorCodes.ERROR_404, exception);
        } else if (exception instanceof AsyncRequestTimeoutException) {
            exception = new ServerRuntimeException(DefaultErrorCodes.ERROR_503, exception);
        } else if (exception instanceof IOException) {
            exception = new SystemRuntimeException(DefaultErrorCodes.ERROR_500_IO, exception);
        } else if (exception instanceof HttpMessageConversionException) {
            exception = new RequestRuntimeException(DefaultErrorCodes.ERROR_40X, exception);
        } else if (exception instanceof MonetaryParseException || exception instanceof MonetaryException) {
            exception = new RequestRuntimeException(DefaultErrorCodes.ERROR_400_MONEY, exception);
        } else if (exception instanceof ConstraintViolationException) {
            exception = new RequestRuntimeException("40X", exception.getMessage(), exception, null);
        } else if (exception instanceof RuntimeException) {
            exception = new ServerRuntimeException(DefaultErrorCodes.ERROR_500, exception.getCause() == null ? exception : exception.getCause());
        }
        return exception;
    }
}
