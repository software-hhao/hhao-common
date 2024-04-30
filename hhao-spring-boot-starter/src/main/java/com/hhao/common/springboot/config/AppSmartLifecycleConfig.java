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
import com.hhao.common.springboot.lifecycle.SmartLifecycleHandler;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.OrderComparator;

import java.util.List;

/**
 * org.springframework.context.support.DefaultLifecycleProcessor
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(AppSmartLifecycleConfig.class)
@ConditionalOnProperty(prefix = "com.hhao.config.app-smart-lifecycle",name = "enable",havingValue = "true",matchIfMissing = true)
public class AppSmartLifecycleConfig extends AbstractBaseConfig {

    /**
     * 全局生命周期
     *
     * @param applicationArguments the application arguments
     * @return app smart lifecycle
     */
    @Bean
    public AppSmartLifecycle appSmartLifecycle(ApplicationArguments applicationArguments, List<SmartLifecycleHandler> smartLifecycleHandlers){
        return new AppSmartLifecycle(applicationArguments,smartLifecycleHandlers);
    }

    /**
     * The type App smart lifecycle.
     */
    public static class AppSmartLifecycle implements SmartLifecycle {
        /**
         * The Logger.
         */
        protected final Logger logger = LoggerFactory.getLogger(AppSmartLifecycle.class);
        private final ApplicationArguments applicationArguments;
        private List<SmartLifecycleHandler> smartLifecycleHandlers;
        private boolean isRunning = false;

        /**
         * Instantiates a new App smart lifecycle.
         *
         * @param applicationArguments the application arguments
         */
        public AppSmartLifecycle(ApplicationArguments applicationArguments,List<SmartLifecycleHandler> smartLifecycleHandlers) {
            this.applicationArguments = applicationArguments;
            this.smartLifecycleHandlers=smartLifecycleHandlers;
            OrderComparator.sort(this.smartLifecycleHandlers);
        }

        /**
         * 返回值指示了应该启动和停止此Lifecycle组件的顺序。
         * 启动过程从值最低开始，到值最高结束(Integer)。
         * Integer.MIN_VALUE是可能的最小值;Integer.MAX_VALUE是可能的最大值。
         * 关闭过程将应用相反的顺序。
         * 任何具有相同值的组件将在同一阶段内任意排序。
         */
        @Override
        public int getPhase() {
            return Integer.MAX_VALUE;
        }

        /**
         * 如果这个Lifecycle组件需要在ApplicationContext刷新时由容器自动启动，则返回true。
         * false表示组件将通过显式的start()调用启动，类似于普通的生命周期实现。
         */
        @Override
        public boolean isAutoStartup() {
            return true;
        }

        /**
         * 在该方法中启动任务或者其他异步服务,比如开启MQ接收消息;
         * 当上下文被刷新(所有对象已被实例化和初始化之后),将调用该方法;
         * 默认生命周期处理器将检查每个SmartLifecycle对象的isAutoStartup()方法返回的布尔值。
         * 如果为“true”则该方法会被调用,而不是等待显式调用自己的start()方法。
         */
        @Override
        public void start() {
            //启动行为在这里
            logger.info("================start=================");
            doStart(this.applicationArguments);
            isRunning = true;
        }

        /**
         * 子类可以重写它，真正的启动事项写在这里
         *
         * @param applicationArguments the application arguments
         */
        protected void doStart(ApplicationArguments applicationArguments) {
            for(SmartLifecycleHandler smartLifecycleHandler:smartLifecycleHandlers){
                smartLifecycleHandler.start(applicationArguments);
            }
        }


        /**
         * 当isRunning方法返回true时;该方法才会被调用。
         * 指示当前正在运行的生命周期组件必须停止。
         * LifecycleProcessor使用callback来支持具有公共关闭顺序值的所有组件的有序，可能是并发的关闭。
         * 回调必须在SmartLifecycle组件停止后执行。
         * LifecycleProcessor将只调用stop方法的这个变体;
         * 例如，Lifecycle.stop()不会在SmartLifecycle实现中被调用，除非在该方法的实现中显式地委托给它。
         * DefaultLifecycleProcessor#timeoutPerShutdownPhase可以设置超时时间
         */
        @Override
        public void stop(Runnable callback) {
            logger.info("================stop begin=================");
            stop();
            logger.info("================stop end=================");
            callback.run();
        }


        /**
         * 具体的关闭行为
         * 注意，这个停止通知并不保证在销毁之前到来:在常规的关闭中，生命周期bean首先会在常规销毁回调传播之前收到一个停止通知;
         * 但是，在上下文生命周期内的热刷新或在中断刷新尝试时，将不调用任何给定bean的destroy方法
         */
        @Override
        public void stop() {
            logger.info("================stopping=================");
            //关闭行为在这里
            doStop(this.applicationArguments);
            isRunning = false;
        }

        /**
         * 子类可以重写它，真正的关闭事项写在这里
         *
         * @param applicationArguments the application arguments
         */
        protected void doStop(ApplicationArguments applicationArguments) {
            for(int i=smartLifecycleHandlers.size();i>0;i--){
                smartLifecycleHandlers.get(i-1).stop(applicationArguments);
            }
        }


        /**
         * 检查该组件当前是否正在运行。
         * 在容器的情况下，只有当应用的所有组件当前都在运行时才返回true。
         * 返回:组件当前是否正在运行
         * 1. 只有该方法返回false时;start方法才会被执行。
         * 2. 只有该方法返回true时;stop(Runnable callback)或stop()方法才会被执行。
         */
        @Override
        public boolean isRunning() {
            return isRunning;
        }
    }
}
