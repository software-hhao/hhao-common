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

package com.hhao.common.sprintboot.webflux.config.greturn;

import org.springframework.core.KotlinDetector;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.*;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.util.Assert;
import org.springframework.web.ErrorResponse;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * @author Wang
 * @since 2022/1/15 14:41
 */
public class GlobalReturnResponseEntityResultHandler extends ResponseEntityResultHandler {
    private static final Set<HttpMethod> SAFE_METHODS = Set.of(HttpMethod.GET, HttpMethod.HEAD);

    public GlobalReturnResponseEntityResultHandler(List<HttpMessageWriter<?>> writers, RequestedContentTypeResolver resolver) {
        super(writers, resolver);
    }

    public GlobalReturnResponseEntityResultHandler(List<HttpMessageWriter<?>> writers, RequestedContentTypeResolver resolver, ReactiveAdapterRegistry registry) {
        super(writers, resolver, registry);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
        Mono<?> returnValueMono;
        MethodParameter bodyParameter;
        ReactiveAdapter adapter = getAdapter(result);
        MethodParameter actualParameter = result.getReturnTypeSource();

        if (adapter != null) {
            Assert.isTrue(!adapter.isMultiValue(), "Only a single ResponseEntity supported");
            returnValueMono = Mono.from(adapter.toPublisher(result.getReturnValue()));
            boolean isContinuation = (KotlinDetector.isSuspendingFunction(actualParameter.getMethod()) &&
                    !COROUTINES_FLOW_CLASS_NAME.equals(actualParameter.getParameterType().getName()));
            bodyParameter = (isContinuation ? actualParameter.nested() : actualParameter.nested().nested());
        }
        else {
            returnValueMono = Mono.justOrEmpty(result.getReturnValue());
            bodyParameter = actualParameter.nested();
        }

        return returnValueMono.flatMap(returnValue -> {
            HttpEntity<?> httpEntity;
            if (returnValue instanceof HttpEntity<?> he) {
                httpEntity = he;
            }
            else if (returnValue instanceof ErrorResponse response) {
                httpEntity = new ResponseEntity<>(response.getBody(), response.getHeaders(), response.getStatusCode());
            }
            else if (returnValue instanceof ProblemDetail detail) {
                httpEntity = ResponseEntity.of(detail).build();
            }
            else if (returnValue instanceof HttpHeaders headers) {
                httpEntity = new ResponseEntity<>(headers, HttpStatus.OK);
            }
            else {
                return Mono.error(() -> new IllegalArgumentException(
                        "HttpEntity or HttpHeaders expected but got: " + returnValue.getClass()));
            }

            if (httpEntity.getBody() instanceof ProblemDetail detail) {
                if (detail.getInstance() == null) {
                    URI path = URI.create(exchange.getRequest().getPath().value());
                    detail.setInstance(path);
                }
                if (logger.isWarnEnabled() && httpEntity instanceof ResponseEntity<?> responseEntity) {
                    if (responseEntity.getStatusCode().value() != detail.getStatus()) {
                        logger.warn(actualParameter.getExecutable().toGenericString() +
                                " returned ResponseEntity: " + responseEntity + ", but its status" +
                                " doesn't match the ProblemDetail status: " + detail.getStatus());
                    }
                }
            }

            if (httpEntity instanceof ResponseEntity<?> responseEntity) {
                exchange.getResponse().setStatusCode(responseEntity.getStatusCode());
            }

            HttpHeaders entityHeaders = httpEntity.getHeaders();
            HttpHeaders responseHeaders = exchange.getResponse().getHeaders();
            if (!entityHeaders.isEmpty()) {
                responseHeaders.putAll(entityHeaders);
            }

            if (httpEntity.getBody() == null || returnValue instanceof HttpHeaders) {
                return exchange.getResponse().setComplete();
            }

            String etag = entityHeaders.getETag();
            Instant lastModified = Instant.ofEpochMilli(entityHeaders.getLastModified());
            HttpMethod httpMethod = exchange.getRequest().getMethod();
            if (SAFE_METHODS.contains(httpMethod) && exchange.checkNotModified(etag, lastModified)) {
                return exchange.getResponse().setComplete();
            }

            //以上部份未改变基类的
            //以下部份加入包装判断与返回
            Object body=httpEntity.getBody();
            if (ResultWrapperHelper.getInstance().supportsAutoWrapping(bodyParameter)){
                //只支持显式提交MediaType为json和xml的两种结果集封装
                MediaType selectedContentType = exchange.getRequest().getHeaders().getContentType();
                if (selectedContentType==null || selectedContentType.includes(MediaType.APPLICATION_JSON) || selectedContentType.includes(MediaType.APPLICATION_XML)) {
                    body= ResultWrapperHelper.getInstance().wrapperResult(body);
                }
            }
            return writeBody(body, bodyParameter, actualParameter, exchange);
        });
    }
}