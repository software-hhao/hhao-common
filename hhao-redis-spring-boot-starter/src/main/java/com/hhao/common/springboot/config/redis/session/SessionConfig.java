
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

package com.hhao.common.springboot.config.redis.session;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.boot.autoconfigure.session.StoreType;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 相关类：
 * org.springframework.boot.autoconfigure.session.SessionAutoConfiguration
 * org.springframework.boot.autoconfigure.session.RedisSessionConfiguration
 * org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration:主要是配置了RedisIndexedSessionRepository Session仓库类
 * org.springframework.session.config.annotation.web.http.SpringHttpSessionConfiguration:主要是配置了SessionRepositoryFilter Session仓库过滤器
 * RedisSessionProperties
 * SessionProperties
 * ==================
 * RedisSentinelConfiguration can also be defined with a PropertySource, which lets you set the following properties:
 * Configuration Properties
 * spring.redis.sentinel.master: name of the master node.
 * spring.redis.sentinel.nodes: Comma delimited list of host:port pairs.
 * spring.redis.sentinel.password: The password to apply when authenticating with Redis Sentinel
 *
 * 有时候，直接与哨兵互动是必要的。使用RedisConnectionFactory.getSentinelConnection()或RedisConnection.getSentinelCommands()可以让你访问第一个已配置的活动哨兵
 * ==================
 * 默认情况下，由LettuceConnectionFactory创建的所有LettuceConnection实例为所有非阻塞和非事务性操作共享相同的线程安全的本地连接。
 * 如果每次都使用专用连接，请将shareNativeConnection设置为false。
 * 如果shareNativeConnection设置为false, LettuceConnectionFactory也可以被配置为使用一个LettucePool池阻塞和事务连接或所有连接。
 *
 *     @Bean
 *     public LettuceConnectionFactory connectionFactory() {
 *         //本地读写
 *         //return new LettuceConnectionFactory(new RedisSocketConfiguration("/var/run/redis.sock"));
 *         //单机读写
 *         //return new LettuceConnectionFactory(new RedisStandaloneConfiguration("server", 6379));
 *         //return new LettuceConnectionFactory();
 *
 *         //主从读写
 *         LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
 *                 .readFrom(REPLICA_PREFERRED)
 *                 .build();
 *         RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration("server", 6379);
 *         return new LettuceConnectionFactory(serverConfig, clientConfig);
 *     }
 *  ====================================
 *
 *     //自定义Spring Session的HttpSession集成来使用HTTP头来传递当前的会话信息，而不是cookie。
 *      @Bean
 *     public HttpSessionIdResolver httpSessionIdResolver() {
 *         return HeaderHttpSessionIdResolver.xAuthToken();
 *     }
 *
 *     //自定义cookie解析
 *      @Bean
 *     public CookieSerializer cookieSerializer() {
 *         DefaultCookieSerializer serializer = new DefaultCookieSerializer();
 *         serializer.setCookieName("JSESSIONID"); // <1>
 *         serializer.setCookiePath("/"); // <2>
 *         serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$"); // <3>
 *         return serializer;
 *     }
 *
 *
 * 这个类没啥用，RedisSessionConfiguration都会被SessionAutoConfiguration启动
 * 如果不想使用session，只能去掉包
 *
 * @author Wang
 * @since 2022/1/30 21:19
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SessionProperties.class)
//不能启用这个注解，走SpringBoot自动启动session的RedisSessionConfiguration这个类来启动,否则无法通过配置设置过期时间
//@EnableRedisHttpSession()
public class SessionConfig {
    public SessionConfig(SessionProperties sessionProperties){
        sessionProperties.setStoreType(StoreType.REDIS);
    }

    @Bean("springSessionDefaultRedisSerializer")
    @SuppressWarnings("all")
    public RedisSerializer<Object> springSessionDefaultRedisSerializer(@Qualifier("genericJackson2JsonRedisSerializer") RedisSerializer<Object> redisSerializer) {
        return redisSerializer;
    }
}