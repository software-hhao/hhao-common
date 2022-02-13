
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
import org.springframework.web.server.adapter.ForwardedHeaderTransformer;

/**
 * 当请求经过代理(如负载均衡器)时，主机、端口和方案可能会改变。从客户机的角度来看，创建指向正确主机、端口和方案的链接是一个挑战。
 *
 * RFC 7239定义了转发的HTTP头，代理可以使用它来提供原始请求的信息。
 * 还有其他非标准头，包括X-Forwarded-Host, X-Forwarded-Port, X-Forwarded-Proto, X-Forwarded-Ssl, and X-Forwarded-Prefix.
 *
 * ForwardedHeaderTransformer是一个组件，它根据转发的报头修改请求的主机、端口和方案，然后删除这些报头。
 * 转发的报头需要考虑安全问题，因为应用程序无法知道报头是由代理添加的，还是由恶意客户端添加的。
 * 这就是为什么应该将信任边界上的代理配置为删除来自外部的不受信任的转发流量。
 * 可以使用removeOnly=true配置ForwardedHeaderTransformer，在这种情况下，它删除但不使用头。
 *
 * @author Wang
 * @since 2022/1/9 16:56
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(ForwardedHeaderConfig.class)
@ConditionalOnProperty(prefix = "com.hhao.config.forwarded-header",name = "enable",havingValue = "true",matchIfMissing = true)
public class ForwardedHeaderConfig extends AbstractBaseWebFluxConfig{

    @Bean
    public ForwardedHeaderTransformer forwardedHeaderTransformer(){
        ForwardedHeaderTransformer forwardedHeaderTransformer=new ForwardedHeaderTransformer();
        return forwardedHeaderTransformer;
    }
}
