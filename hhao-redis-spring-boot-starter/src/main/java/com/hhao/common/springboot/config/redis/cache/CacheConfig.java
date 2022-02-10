
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

package com.hhao.common.springboot.config.redis.cache;

import com.hhao.common.springboot.config.redis.RedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurationSelector;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheStatisticsCollector;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 相关类：
 * org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
 * org.springframework.boot.autoconfigure.cache.CacheConfigurations
 * org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
 * org.springframework.data.redis.cache.RedisCacheConfiguration
 *
 * AspectJCachingConfiguration
 * CachingConfigurationSelector
 * RedisCacheManager
 * RedisCache
 *
 * 目前缓存只适用于web，不适用于webflux，需要改造
 *
 * @author Wang
 * @since 2022/2/3 10:52
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(CacheManager.class)
@AutoConfigureAfter(RedisConfig.class)
@AutoConfigureBefore(RedisCacheConfiguration.class)
@ConditionalOnWebApplication(type=ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(CacheProperties.class)
@ConditionalOnProperty(prefix = "com.hhao.config.redis.cache",name = "enable",havingValue = "true",matchIfMissing = true)
public class CacheConfig {

    /**
     * 作为动态生成RedisCache配置模板的RedisCacheConfiguration名称
     */
    private final String DEFAULT_REDIS_CACHE_CONFIG_NAME="default";
    private CacheProperties cacheProperties;

    @Autowired
    public CacheConfig(CacheProperties cacheProperties){
        new CachingConfigurationSelector();
        this.cacheProperties=cacheProperties;
    }

    @Bean
    @SuppressWarnings("all")
    public RedisCacheConfiguration defaultRedisCacheConfiguration(@Qualifier("genericJackson2JsonRedisSerializer") RedisSerializer<Object> redisSerializer){
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();

        defaultCacheConfig=defaultCacheConfig
                //设置缓存管理器管理的缓存的默认过期时间
                //Duration.ZERO表示不过期
                .entryTtl(Duration.ofMinutes(cacheProperties.getDefaultTtl()))
                //设置 key为string序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                //设置value为json序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                //自定义前缀
                .computePrefixWith(cacheName -> "spring:cache:" + cacheName + "-");

        return defaultCacheConfig;
    }

    /**
     * 预处理RedisCacheConfiguration的bean名称，把后缀去掉，留下的名称作为注册的RedisCache的名称
     *
     * @param cacheConfigs
     * @return
     */
    private Map<String, RedisCacheConfiguration> preProcessCacheConfigurations(Map<String, RedisCacheConfiguration> cacheConfigs){
        Map<String, RedisCacheConfiguration> cacheConfigurationMap=new HashMap<>();
        for (Map.Entry<String, RedisCacheConfiguration> entry : cacheConfigs.entrySet()) {
            cacheConfigurationMap.put(preProcessCacheName(entry.getKey()),entry.getValue());
        }
        return cacheConfigurationMap;
    }

    /**
     * 后缀去掉，留下的名称作为注册的RedisCache的名称
     *
     * @param name
     * @return
     */
    protected String preProcessCacheName(String name){
        String suffix=cacheProperties.getSuffixConfigName();
        if (StringUtils.endsWithIgnoreCase(name,suffix)){
            return name.substring(0,name.lastIndexOf(suffix));
        }
        return name;
    }

    /**
     * 构建默认的CacheManager
     * 注入系统注册的RedisCacheConfiguration Bean，根据这些Bean生成对应配置的RedisCache
     * 生成的RedisCache名称(name)=RedisCacheConfiguration Bean的名称去掉后缀，后缀由CacheProperties中定义
     * 默认的后缀名为RedisCacheConfiguration
     * 如：defaultRedisCacheConfiguration Bean，则它对应的RedisCache名称为default
     *
     *
     * defaultCacheConfig=defaultCacheConfig
     *       //设置缓存管理器管理的缓存的默认过期时间
     *       //Duration.ZERO表示不过期
     *       .entryTtl(Duration.ofMinutes(30))
     *       //设置 key为string序列化
     *       .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
     *       //设置value为json序列化
     *       .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
     *       //自定义前缀
     *       .computePrefixWith(cacheName -> "spring:" + cacheName + "-");
     *       //强制前缀，底层还是调用computePrefixWith()
     *       //.prefixCacheNameWith("spring:")
     *       //禁用Key的前缀
     *       //.disableKeyPrefix()
     *       //不缓存空值
     *       //.disableCachingNullValues();
     *
     *       return  RedisCacheManager.builder(factory)
     *                 .cacheDefaults(defaultCacheConfig)
     *                 //可以提供一个Map，针对每个Cache进行个性化配置，否则采用默认的设置
     *                 //.withInitialCacheConfigurations()
     *                 //关闭动态创建cache
     *                 //.disableCreateOnMissingCache()
     *                 //初始化时就放进去的cache name，如关闭了动态创建，这个就是必须的
     *                 //.initialCacheNames( )
     *                 //支持事务
     *                 .transactionAware()
     *                 .build();
     *
     * @return
     */
    @Bean
    @SuppressWarnings("all")
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory, Map<String, RedisCacheConfiguration> cacheConfigurations) {
        //RedisCacheConfiguration预处理
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        cacheConfigurations=preProcessCacheConfigurations(cacheConfigurations);
        //获取模板RedisCacheConfiguration
        if (cacheConfigurations.get(DEFAULT_REDIS_CACHE_CONFIG_NAME)!=null){
            defaultCacheConfig=cacheConfigurations.get("default");
        }

        //以下代码由于采用了自定义的RedisCacheManagerEx
        //相关代码参照RedisCacheManager.RedisCacheManagerBuilder
        RedisCacheWriter cacheWriter=RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        CacheStatisticsCollector statisticsCollector = CacheStatisticsCollector.none();

        if (!statisticsCollector.equals(CacheStatisticsCollector.none())) {
            cacheWriter = cacheWriter.withStatisticsCollector(statisticsCollector);
        }

        RedisCacheManager redisCacheManager= new RedisCacheManagerEx(cacheWriter
                ,defaultCacheConfig
                ,cacheConfigurations
                ,cacheProperties.getAllowInFlightCacheCreation()
        );
        redisCacheManager.setTransactionAware(cacheProperties.getEnableTransactions());
        return redisCacheManager;
    }

    /**
     * 采用Java Proxy模式进行AOP
     * 该模式相同对象的方法间调用不会被缓存
     */
    @Configuration
    @EnableCaching(mode = AdviceMode.PROXY)
    @ConditionalOnProperty(prefix = "com.hhao.config.redis.cache",name = "mode",havingValue = "PROXY")
    public static class EnableCachingWithProxy{
        final Logger logger = LoggerFactory.getLogger(EnableCachingWithProxy.class);
        public EnableCachingWithProxy(){
            logger.debug("EnableCachingWithProxy");
        }
    }

    /**
     * 采用ASPECTJ模式进行AOP
     * 该模式要设置POM，编译时织入代码
     *
     * IntelJ中启用ASPECTJ
     * 1、从官网https://www.eclipse.org/aspectj/downloads.php#install下载aspectj-1.9.6.jar
     * 执行安装：java -jar aspectj-1.9.6.jar
     * 2、把路径C:\aspectj1.9\bin加入到path，可能需要重启
     * 3、IntelJ安装插件：AspectJ
     *      插件官网：https://www.jetbrains.com/help/idea/2021.1/aspectj-facet.html
     * 4、设置File->Settings->Build,Execution,Deployment->Compiler->Java Compiler
     *      User compiler:Ajc
     *      Path to aspectjtools.jar:aspectjtools-1.9.8.RC3.jar的安装路径
     *      Command line parameters:-showWeaveInfo
     *      钩选：Delegate to Javac，这样可以省时间，即能不用ajc就不用它编译
     * 5、pom.xml添加以下依赖：
     *         <dependency>
     *             <groupId>org.springframework</groupId>
     *             <artifactId>spring-aspects</artifactId>
     *         </dependency>
     *
     *         <dependency>
     *             <groupId>org.aspectj</groupId>
     *             <artifactId>aspectjrt</artifactId>
     *         </dependency>
     *
     *         <dependency>
     *             <groupId>org.aspectj</groupId>
     *             <artifactId>aspectjtools</artifactId>
     *         </dependency>
     *
     *         <dependency>
     *             <groupId>org.springframework</groupId>
     *             <artifactId>spring-instrument</artifactId>
     *         </dependency>
     * 6、工程项设置：
     *      File->Project Structure->Modules，选需要采用AspectJ编译的module，加入AspectJ Facets
     *      Post-compile weave mode：如果钩选，会尽可能避免使用ajc
     *      Aspect path：org.springframework:spring-aspects:.3.15
     * 7、META-INF下建aop.xml文件，添加要ajc处理包路径
     * <aspectj>
     *     <weaver options="-verbose -showWeaveInfo">
     *         <include within="com.hhao.common.springboot.web.mvc.test.server..*"/>
     *     </weaver>
     * </aspectj>
     * 经过以上步骤，IntelJ就可以编译带aspectj的文件了；
     *
     * 如果要在Maven打包时处理aspectj，则在pom.xml中加入以下：
     *    <build>
     *         <plugins>
     *             <plugin>
     *                 <groupId>org.springframework.boot</groupId>
     *                 <artifactId>spring-boot-maven-plugin</artifactId>
     *                 <version>2.6.3</version>
     *                 <configuration>
     *                     <mainClass>com.hhao.common.springboot.web.mvc.test.SpringBootMvcApp</mainClass>
     *                     <fork>true</fork>
     *                 </configuration>
     *                 <executions>
     *                     <execution>
     *                         <goals>
     *                             <goal>repackage</goal>
     *                         </goals>
     *                     </execution>
     *                 </executions>
     *             </plugin>
     *             <plugin>
     *                 <artifactId>maven-compiler-plugin</artifactId>
     *                 <version>3.8.1</version>
     *                 <configuration>
     *                     <release>11</release>
     *                     <useIncrementalCompilation>false</useIncrementalCompilation>
     *                 </configuration>
     *             </plugin>
     *             <plugin>
     *                 <groupId>org.codehaus.mojo</groupId>
     *                 <artifactId>aspectj-maven-plugin</artifactId>
     *                 <version>1.4</version>
     *                 <dependencies>
     *                     <dependency>
     *                         <groupId>org.aspectj</groupId>
     *                         <artifactId>aspectjrt</artifactId>
     *                         <version>1.9.8.RC3</version>
     *                     </dependency>
     *                     <dependency>
     *                         <groupId>org.aspectj</groupId>
     *                         <artifactId>aspectjtools</artifactId>
     *                         <version>1.9.8.RC3</version>
     *                     </dependency>
     *                 </dependencies>
     *                 <executions>
     *                     <execution>
     *                         <goals>
     *                             <goal>compile</goal>
     *                             <goal>test-compile</goal>
     *                         </goals>
     *                     </execution>
     *                 </executions>
     *                 <configuration>
     *                     <outxml>true</outxml>
     *                     <showWeaveInfo>true</showWeaveInfo>
     *                     <verbose>true</verbose>
     *                     <aspectLibraries>
     *                         <aspectLibrary>
     *                             <groupId>org.springframework</groupId>
     *                             <artifactId>spring-aspects</artifactId>
     *                         </aspectLibrary>
     *                     </aspectLibraries>
     *                     <source>11</source>
     *                     <target>11</target>
     *                 </configuration>
     *             </plugin>
     *         </plugins>
     *     </build>
     */
    @Configuration
    @EnableCaching(mode = AdviceMode.ASPECTJ)
    @ConditionalOnProperty(prefix = "com.hhao.config.redis.cache",name = "mode",havingValue = "ASPECTJ")
    public static class EnableCachingWithAspectj{
        final Logger logger = LoggerFactory.getLogger(EnableCachingWithAspectj.class);
        public EnableCachingWithAspectj(){
            logger.debug("EnableCachingWithAspectj");
        }
    }
}

