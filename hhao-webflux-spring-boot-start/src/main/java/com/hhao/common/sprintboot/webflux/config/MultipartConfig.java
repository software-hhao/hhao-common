
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.multipart.SynchronossPartHttpMessageReader;

/**
 * DefaultServerWebExchange使用HttpMessageReader<MultiValueMap<String, Part>>来解析multipart/form-data内容到MultiValueMap。
 * 默认情况下，采用DefaultPartHttpMessageReader，它没有任何第三方依赖项。
 * 也可以使用SynchronossPartHttpMessageReader，它是基于Synchronoss NIO Multipart 库的。
 * 两者都是通过ServerCodecConfigurer bean配置的(请参阅Web处理程序API)。
 *
 * 要以流的方式解析multipart data，可以使用从HttpMessageReader<Part>返回的Flux<Part>。
 * @RequestPart通过名称对各个部分进行类似于map的访问，需要完整地解析multipart data。
 * @RequestBody将内容解码为Flux<Part>，而无需收集到MultiValueMap。
 *
 * @author Wang
 * @since 2022/1/9 17:03
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(MultipartConfig.class)
@ConditionalOnProperty(prefix = "com.hhao.config.multipart",name = "enable",havingValue = "true",matchIfMissing = true)
public class MultipartConfig extends AbstractBaseWebFluxConfig{
    @Bean
    public SynchronossPartHttpMessageReader synchronossPartHttpMessageReader(){
        SynchronossPartHttpMessageReader reader=new SynchronossPartHttpMessageReader();
        //限制每个part的磁盘空间量
        reader.setMaxDiskUsagePerPart(-1);
        //限制非文件部分的大小
        reader.setMaxInMemorySize(256);
        //限制多部件请求中的部件总数
        reader.setMaxParts(10);
        return reader;
    }
}
