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

import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * @author Wang
 * @since 2022/1/15 14:42
 */
public class GlobalReturnResponseBodyResultHandler extends ResponseBodyResultHandler {
    public GlobalReturnResponseBodyResultHandler(List<HttpMessageWriter<?>> writers, RequestedContentTypeResolver resolver) {
        super(writers, resolver);
    }

    public GlobalReturnResponseBodyResultHandler(List<HttpMessageWriter<?>> writers, RequestedContentTypeResolver resolver, ReactiveAdapterRegistry registry) {
        super(writers, resolver, registry);
    }

    @Override
    public int getOrder() {
        return 99;
    }

    @Override
    public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
        Object body = result.getReturnValue();
        MethodParameter bodyTypeParameter = result.getReturnTypeSource();
        if (body instanceof ProblemDetail detail) {
            exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(detail.getStatus()));
            if (detail.getInstance() == null) {
                URI path = URI.create(exchange.getRequest().getPath().value());
                detail.setInstance(path);
            }
        }
        if (ResultWrapperHelper.getInstance().supportsAutoWrapping(bodyTypeParameter)){
            //只支持显式提交MediaType为json和xml的两种结果集封装
            MediaType selectedContentType = exchange.getRequest().getHeaders().getContentType();
            if (selectedContentType==null || selectedContentType.includes(MediaType.APPLICATION_JSON) || selectedContentType.includes(MediaType.APPLICATION_XML)){
                body= ResultWrapperHelper.getInstance().wrapperResult(body);
            }
        }
        return writeBody(body, bodyTypeParameter, exchange);
    }


}