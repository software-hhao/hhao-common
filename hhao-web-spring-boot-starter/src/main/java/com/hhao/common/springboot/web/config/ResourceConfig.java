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

package com.hhao.common.springboot.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.resource.VersionResourceResolver;

import java.util.concurrent.TimeUnit;

/**
 * The type Resource config.
 * VersionResourceResolver:
 * VersionResourceResolver 可以为资源添加版本号。其所作的工作如下：首先使用下一个 resolver 获取资源，如果找到资源则返回，不做其它处理；如果 下一个 resolver 找不到资源，则尝试去掉 url 中的 version 信息，重新调用下一个 resolver 处理，然后无论下一个 resolver 能否处理，都返回其结果。
 * 版本号的策略有两种:
 * 1、指定固定值作为版本号
 * .addResolver(new VersionResourceResolver().addFixedVersionStrategy("1.0.0", "/**"));
 * 在请求资源时，加上 /1.0.0 前缀，即 http://localhost:8080/static/1.0.0/style.css 也可正确访问。
 * 2、使用 MD5 作为版本号
 * 请求资源时，加上资源的 md5，即 http://localhost:8080/static/style-dfbe630979d120fe54a50593f2621225.css 也可正确访问。
 * <p>
 * CssLinkResourceTransformer：
 * 会将 css 文件中的 @import 或 url() 函数中的资源路径自动转换为包含版本号的路径
 * <p>
 * Spring boot设置
 * 设置静态资源的存放地址
 * spring.resources.static-locations=classpath:/resources
 * 开启 chain cache
 * spring.resources.chain.cache=true
 * 开启 gzip
 * spring.resources.chain.gzipped=true
 * 指定版本号
 * spring.resources.chain.strategy.fixed.enabled=true
 * spring.resources.chain.strategy.fixed.paths=/static
 * spring.resources.chain.strategy.fixed.version=1.0.0
 * 使用 MD5 作为版本号
 * spring.resources.chain.strategy.content.enable=true
 * spring.resources.chain.strategy.content.paths=/**
 * http 缓存过期时间
 * spring.resources.cachePeriod=60
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(ResourceConfig.class)
@ConditionalOnProperty(prefix = "com.hhao.config.resource", name = "enable", havingValue = "true", matchIfMissing = true)
public class ResourceConfig extends AbstractBaseMvcConfig {
    /***
     * 静态资源文件配置
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry
                //指定访问路径
                .addResourceHandler("/static/**")
                //指定访问路径对应的本地路径
                .addResourceLocations("classpath:static/")
                //设置HTTP缓存,cachePrivate为指示在代理服务器中不缓存
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic())
                //开启服务端的缓存
                .resourceChain(true)
                //静态文件的解析，可以添加多个，此处加入版本解析
                .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
        //静态文件的转换处理，可以添加多个，此处加入CSS导入文件名的处理；
        //.addTransformer(new CssLinkResourceTransformer());
    }
}
