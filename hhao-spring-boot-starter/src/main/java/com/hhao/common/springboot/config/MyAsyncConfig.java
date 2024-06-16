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
package com.hhao.common.springboot.config;

import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.springboot.ansy.ExecutorProperties;
import com.hhao.common.springboot.lifecycle.SmartLifecycleHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * The type My async configurer.
 * TaskExecutorBuilder
 *
 * @author Wang
 * @since 2022 /2/25 8:45
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(MyAsyncConfig.class)
@ConditionalOnProperty(prefix = "com.hhao.config.async",name = "enable",havingValue = "true",matchIfMissing = true)
@EnableConfigurationProperties(ExecutorProperties.class)
public class MyAsyncConfig implements SmartLifecycleHandler {
    final Logger logger = LoggerFactory.getLogger(MyAsyncConfig.class);
    private ExecutorProperties executorProperties;
    private ThreadPoolTaskExecutor executor=null;

    /**
     * Instantiates a new My async configurer.
     *
     * @param executorProperties the executor properties
     */
    @Autowired
    public MyAsyncConfig(ExecutorProperties executorProperties){
        this.executorProperties=executorProperties;
    }

    @Bean("asyncExecutor")
    public Executor asyncExecutor() {
        executor = new ThreadPoolTaskExecutor();
        executor.initialize();

        executor.setCorePoolSize(executorProperties.getCorePoolSize());
        executor.setMaxPoolSize(executorProperties.getMaxPoolSize());
        executor.setQueueCapacity(executorProperties.getQueueCapacity());
        executor.setKeepAliveSeconds(executorProperties.getKeepAliveSeconds());
        executor.setAllowCoreThreadTimeOut(executorProperties.isAllowCoreThreadTimeOut());
        executor.setPrestartAllCoreThreads(executorProperties.isPrestartAllCoreThreads());
        executor.setWaitForTasksToCompleteOnShutdown(executorProperties.isWaitForTasksToCompleteOnShutdown());
        executor.setAwaitTerminationSeconds(executorProperties.getAwaitTerminationSeconds());
        executor.setThreadNamePrefix(executorProperties.getThreadNamePrefix());

        //拒绝策略:
        //ThreadPoolExecutor.AbortPolicy 丢弃任务并抛出RejectedExecutionException异常(默认)。
        //ThreadPoolExecutor.DiscardPolic 丢弃任务，但是不抛出异常。
        //ThreadPoolExecutor.DiscardOldestPolicy 丢弃队列最前面的任务，然后重新尝试执行任务
        //ThreadPoolExecutor.CallerRunsPolic 由调用线程处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        logger.info("ThreadPoolTaskExecutor init success");
        return executor;
    }

    @Override
    public void stop(ApplicationArguments applicationArguments) {
        executor.shutdown();
        logger.info("ThreadPoolTaskExecutor shutdown success");
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(AsyncConfiguration.class)
    @ConditionalOnProperty(prefix = "com.hhao.config.async",name = "enable",havingValue = "true",matchIfMissing = true)
    public static class AsyncConfiguration implements AsyncConfigurer {
        private Executor executor;

        @Autowired
        public AsyncConfiguration(@Qualifier("asyncExecutor") Executor executor) {
            this.executor=executor;
        }

        @Override
        public Executor getAsyncExecutor() {
            return this.executor;
        }

        // 异常处理器
        @Override
        public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
            return new SimpleAsyncUncaughtExceptionHandler();
        }
    }

    @Configuration
    @EnableAsync(mode = AdviceMode.ASPECTJ)
    @ConditionalOnProperty(prefix = "com.hhao.config.async",name = "mode",havingValue = "ASPECTJ")
    public static class EnableAsyncWithAspectj{
        /**
         * The Logger.
         */
        final Logger logger = LoggerFactory.getLogger(EnableAsyncWithAspectj.class);

        /**
         * Instantiates a new Enable caching with aspectj.
         */
        public EnableAsyncWithAspectj(){
            logger.info("Enable async with aspectj.");
        }
    }

    @Configuration
    @EnableAsync(mode = AdviceMode.PROXY)
    @ConditionalOnProperty(prefix = "com.hhao.config.async",name = "mode",havingValue = "PROXY")
    public static class EnableAsyncWithProxy{
        /**
         * The Logger.
         */
        final Logger logger = LoggerFactory.getLogger(EnableAsyncWithProxy.class);

        /**
         * Instantiates a new Enable caching with aspectj.
         */
        public EnableAsyncWithProxy(){
            logger.info("Enable async with proxy.");
        }
    }
}
