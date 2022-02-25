
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

package com.hhao.common.sprintboot.webflux.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.config.PathMatchConfigurer;
import reactor.core.publisher.Mono;

/**
 * WebClient:
 * 自定义解码器
 * WebClient webClient = WebClient.builder()
 *         .codecs(configurer -> {
 *                 CustomDecoder decoder = new CustomDecoder();
 *                 configurer.customCodecs().registerWithDefaultConfig(decoder);
 *         })
 *         .build();
 *
 * 展示了如何处理客户端请求(日志开启明细):
 * Consumer<ClientCodecConfigurer> consumer = configurer ->
 *         configurer.defaultCodecs().enableLoggingRequestDetails(true);
 *
 * WebClient webClient = WebClient.builder()
 *         .exchangeStrategies(strategies -> strategies.codecs(consumer))
 *         .build();
 *
 *
 * @author Wang
 * @since 2022/1/9 21:51
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(CommonConfig.class)
@ConditionalOnProperty(prefix = "com.hhao.config.common",name = "enable",havingValue = "true",matchIfMissing = true)
public class CommonConfig extends AbstractBaseWebFluxConfig {


    @Override
    public void configurePathMatching(PathMatchConfigurer configurer) {

    }

    //处理服务器端请求:
    //@Override
    //public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
    //    configurer.defaultCodecs().enableLoggingRequestDetails(true);
    //}

    @RestController
    public class Hello{
        @GetMapping("/hello")
        public Mono<String> hello(){
            return Mono.just("Nice to use hhao software, more information can visit https://github.com/software-hhao/hhao-common.");
        }
    }
}
