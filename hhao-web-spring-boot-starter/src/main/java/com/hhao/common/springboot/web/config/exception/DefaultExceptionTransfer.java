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

package com.hhao.common.springboot.web.config.exception;

import com.hhao.common.exception.ErrorInfos;
import com.hhao.common.exception.ExceptionTransfer;
import com.hhao.common.exception.error.request.DateTimeConvertException;
import com.hhao.common.exception.error.request.RequestException;
import com.hhao.common.exception.error.server.ServerException;
import com.hhao.common.exception.error.sys.SystemException;
import com.hhao.common.springboot.exception.error.request.ValidateException;
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
        ){
            return true;
        }
        return false;
    }

    @Override
    public Throwable transfer(Throwable exception) {
        if (exception instanceof MethodArgumentTypeMismatchException){
            //对请求参数缺失错误做转换处理
            MethodArgumentTypeMismatchException ex=(MethodArgumentTypeMismatchException) exception;
            if (ex.getRootCause()!=null && ex.getRootCause() instanceof DateTimeParseException){
                exception=new DateTimeConvertException();
            }else{
                exception=new RequestException(exception);
            }
        }else if (exception instanceof HttpRequestMethodNotSupportedException) {
            exception=new RequestException(ErrorInfos.ERROR_405,exception);
        } else if (exception instanceof HttpMediaTypeNotSupportedException) {
            exception=new RequestException(ErrorInfos.ERROR_415,exception);
        }else if (exception instanceof HttpMediaTypeNotAcceptableException) {
            exception=new RequestException(ErrorInfos.ERROR_406,exception);
        }else if (exception instanceof MissingPathVariableException) {
            exception=new ServerException(ErrorInfos.ERROR_500,exception);
        } else if (exception instanceof MissingServletRequestParameterException) {
            exception=new RequestException(ErrorInfos.ERROR_40X,exception);
        }else if (exception instanceof ServletRequestBindingException) {
            exception=new RequestException(ErrorInfos.ERROR_40X,exception);
        }else if (exception instanceof ConversionNotSupportedException) {
            exception=new ServerException(ErrorInfos.ERROR_500,exception);
        }else if (exception instanceof TypeMismatchException) {
            exception=new RequestException(ErrorInfos.ERROR_40X,exception);
        }else if (exception instanceof HttpMessageNotReadableException){
            //对Spring Convert日期转化错误的处理
            HttpMessageNotReadableException ex=(HttpMessageNotReadableException)exception;
            if (ex.getRootCause()!=null) {
                Throwable e=ex.getRootCause();
                if (e instanceof DateTimeParseException) {
                    exception = new DateTimeConvertException();
                }else if (e instanceof MonetaryParseException || e instanceof MonetaryException){
                    exception=new RequestException(ErrorInfos.ERROR_400_MONEY,exception);
                }else{
                    exception=new RequestException(ErrorInfos.ERROR_40X,exception);
                }
            }
        }else if (exception instanceof HttpMessageNotWritableException) {
            exception=new ServerException(ErrorInfos.ERROR_500,exception);
        }else if (exception instanceof MethodArgumentNotValidException) {
            //对验证类异常作转换处理
            exception=new ValidateException( ((MethodArgumentNotValidException) exception).getBindingResult());
        }else if (exception instanceof MissingServletRequestPartException) {
            exception=new RequestException(ErrorInfos.ERROR_40X,exception);
        }if (exception instanceof BindingResult) {
            //对验证类异常作转换处理
            exception=new ValidateException((BindingResult)exception);
        }else if (exception instanceof NoHandlerFoundException) {
            exception=new RequestException(ErrorInfos.ERROR_404,exception);
        }else if (exception instanceof AsyncRequestTimeoutException) {
            exception=new ServerException(ErrorInfos.ERROR_503,exception);
        }else if (exception instanceof IOException) {
            exception=new SystemException(ErrorInfos.ERROR_500_IO,exception);
        }else if (exception instanceof HttpMessageConversionException){
            exception=new RequestException(ErrorInfos.ERROR_40X,exception);
        }else if (exception instanceof MonetaryParseException || exception instanceof MonetaryException){
            exception=new RequestException(ErrorInfos.ERROR_400_MONEY,exception);
        }
        return exception;
    }
}
