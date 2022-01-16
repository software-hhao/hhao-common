
/*
 * Copyright 2018-2022 WangSheng.
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

package com.hhao.common.sprintboot.webflux.config.exception;

import com.hhao.common.springboot.annotations.ResponseAutoWrapper;
import com.hhao.common.springboot.exception.BaseException;
import com.hhao.common.springboot.exception.ExceptionTransfer;
import com.hhao.common.springboot.response.ResultWrapperBuilder;
import com.hhao.common.springboot.response.UnResultWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 替换DefaultErrorWebExceptionHandler
 * WebFlux下还有几种WebExceptionHandler：
 * ResponseStatusExceptionHandler
 * WebFluxResponseStatusExceptionHandler
 *
 * @author Wang
 * @since 2022/1/14 11:02
 */
public class CustomErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {
    protected final Logger logger = LoggerFactory.getLogger(CustomErrorWebExceptionHandler.class);
    private List<ExceptionTransfer> exceptionTransfers;

    /**
     * Create a new {@code DefaultErrorWebExceptionHandler} instance.
     *
     * @param errorAttributes    the error attributes
     * @param resources          the resources configuration properties
     * @param errorProperties    the error configuration properties
     * @param applicationContext the current application context
     * @since 2.4.0
     */
    public CustomErrorWebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ErrorProperties errorProperties, ApplicationContext applicationContext, List<ExceptionTransfer> exceptionTransfers) {
        super(errorAttributes, resources, errorProperties, applicationContext);
        this.exceptionTransfers = exceptionTransfers;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        //如果要对Exception做转换，可以在这个位置
        Throwable exception=transferError(throwable);

        return super.handle(exchange,exception);
    }


    /**
     * 异常类转换，根据DefaultHandlerExceptionResolver
     *
     * @param exception the exception
     * @return throwable
     */
    protected Throwable transferError(Throwable exception){
        //如果是自定义的异常，则直接返回
        if (exception instanceof BaseException){
            return exception;
        }
        // 异常作转换
        for(ExceptionTransfer exceptionTransfer:exceptionTransfers){
            if (exceptionTransfer.support(exception)){
                return exceptionTransfer.transfer(exception);
            }
        }
        //可以根据response.status，对异常做转换，如400->RequestException,500->ServerExeption
        return exception;
    }

    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> error = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        if (supportResponseWrapper(request)) {
            return ServerResponse.status(getHttpStatus(error)).contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(ResultWrapperBuilder.error(error)));
        }
        logger.debug("don't support response auto wrapper.");
        return ServerResponse.status(getHttpStatus(error)).contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(error));
    }

    /**
     * 判断是否支持自动封装
     * @param request
     * @return
     */
    public boolean supportResponseWrapper(ServerRequest request) {
        Object bestMatchingHandler=request.attribute("org.springframework.web.reactive.HandlerMapping.bestMatchingHandler").get();
        if ((bestMatchingHandler==null) || !(bestMatchingHandler instanceof HandlerMethod)){
            return false;
        }
        HandlerMethod handlerMethod=(HandlerMethod)bestMatchingHandler;
        if (handlerMethod==null){
            return false;
        }
        Method method=handlerMethod.getMethod();
        if (method==null){
            return false;
        }
        //如果继承自UnResultWrapper,则不支持自动包装
        if (method.getReturnType()!=null && method.getReturnType().isAssignableFrom(UnResultWrapper.class)){
            return false;
        }
        //判断是否有ResponseAutoWrapper的注解
        ResponseAutoWrapper responseAutoWrapper=method.getAnnotation(ResponseAutoWrapper.class);
        if (responseAutoWrapper==null){
            //获取类级的注解
            responseAutoWrapper=method.getDeclaringClass().getAnnotation(ResponseAutoWrapper.class);
        }
        if (responseAutoWrapper==null || responseAutoWrapper.value()==false){
            return false;
        }
        return true;
    }
}
