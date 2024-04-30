
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

import com.hhao.common.springboot.response.ResultWrapperBuilder;
import com.hhao.common.springboot.response.ResultWrapperProperties;
import com.hhao.common.sprintboot.webflux.config.AbstractBaseWebFluxConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityResultHandler;

/**
 * 自动结果集封装
 * 只支持同时满足以下条件的自动结果集封装
 * 1、@RestController类型
 * 2、ResponseBodyResultHandler能够解析的结果集
 * 3、@ResponseAutoWrapper注解定义
 *
 * 相关类可以查阅：
 * DispatcherHandler#handleResult
 * DispatcherHandler#resultHandlers
 *
 * 重写了两种ResponseBodyResultHandler、ResponseEntityResultHandler返回处理
 * 另外ServerResponseResultHandler没有重写，需要手动返回包装结果：
 *
 *     public Mono<ServerResponse> handleJbook1(ServerRequest request) {
 *         Mono<JBook1> jBook1Mono = request.bodyToMono(JBook1.class).doOnNext(this::validate);
 * //        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(jBook1Mono,JBook1.class);
 *
 * //        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromPublisher(jBook1Mono.map(jBook1 -> {
 * //            return ResultWrapperBuilder.ok(jBook1);
 * //        }), new ParameterizedTypeReference<ResultWrapper<JBook1>>() {}));
 *
 *         return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(jBook1Mono.map(jBook1 -> {
 *             return ResultWrapperBuilder.ok(jBook1);
 *         }), new ParameterizedTypeReference<ResultWrapper<JBook1>>() {});
 *     }
 *
 * @author Wang
 * @since 2022/1/13 17:21
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(GlobalReturnConfig.class)
@ConditionalOnProperty(prefix = "com.hhao.config.global-return",name = "enable",havingValue = "true",matchIfMissing = true)
public class GlobalReturnConfig extends AbstractBaseWebFluxConfig {

    @Bean
    @ConditionalOnMissingBean(ResultWrapperProperties.class)
    public ResultWrapperProperties resultWrapperProperties(){
        return new ResultWrapperProperties();
    }

    @Bean
    @ConditionalOnMissingBean(ResultWrapperBuilder.class)
    public ResultWrapperBuilder resultWrapperBuilder(ResultWrapperProperties resultWrapperProperties){
        return new ResultWrapperBuilder(resultWrapperProperties);
    }

    @Bean
    public ResponseBodyResultHandler globalReturnResponseBodyResultHandler(
            @Qualifier("webFluxAdapterRegistry") ReactiveAdapterRegistry reactiveAdapterRegistry,
            ServerCodecConfigurer serverCodecConfigurer,
            @Qualifier("webFluxContentTypeResolver") RequestedContentTypeResolver contentTypeResolver) {

        return new GlobalReturnResponseBodyResultHandler(serverCodecConfigurer.getWriters(),
                contentTypeResolver, reactiveAdapterRegistry);
    }

    @Bean
    public ResponseEntityResultHandler myResponseEntityResultHandler(
            @Qualifier("webFluxAdapterRegistry") ReactiveAdapterRegistry reactiveAdapterRegistry,
            ServerCodecConfigurer serverCodecConfigurer,
            @Qualifier("webFluxContentTypeResolver") RequestedContentTypeResolver contentTypeResolver) {

        return new GlobalReturnResponseEntityResultHandler(serverCodecConfigurer.getWriters(),
                contentTypeResolver, reactiveAdapterRegistry);
    }

}
