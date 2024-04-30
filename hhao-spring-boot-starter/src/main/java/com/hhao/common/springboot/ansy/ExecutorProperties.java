package com.hhao.common.springboot.ansy;

import com.hhao.common.utils.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Executor properties.
 */
@ConfigurationProperties(prefix = "com.hhao.config.async.executor")
public class ExecutorProperties {
    /**
     * 核心线程数
     */
    private int corePoolSize = 1;
    /**
     * 最大线程数
     */
    private int maxPoolSize = 20;
    /**
     * 线程最大空闲时间,秒
     */
    private int keepAliveSeconds = 60 * 10;
    /**
     * 队列大小
     */
    private int queueCapacity = 1000;
    /**
     * 指定是否允许核心线程超时
     */
    private boolean allowCoreThreadTimeOut = false;
    /**
     * 指定是否启动所有核心线程，使它们空闲地等待工作。
     */
    private boolean prestartAllCoreThreads = true;
    /**
     * 等待任务在关机时完成--表明等待所有线程执行完
     */
    private boolean waitForTasksToCompleteOnShutdown = true;
    /**
     * 等待时间,0表示立即停止
     */
    private int awaitTerminationSeconds = 60 * 15;
    /**
     * 线程前缀
     */
    private String threadNamePrefix = "hhao-executor-";

    /**
     * Instantiates a new Executor properties.
     */
    public ExecutorProperties() {
        int count = Runtime.getRuntime().availableProcessors();
        if (count > 0) {
            corePoolSize = count + 1;
        }
    }

    /**
     * Gets core pool size.
     *
     * @return the core pool size
     */
    public int getCorePoolSize() {
        return corePoolSize;
    }

    /**
     * Sets core pool size.
     *
     * @param corePoolSize the core pool size
     */
    public void setCorePoolSize(int corePoolSize) {
        if (corePoolSize > 0) {
            this.corePoolSize = corePoolSize;
        }else{
            int count = Runtime.getRuntime().availableProcessors();
            if (count > 0) {
                this.corePoolSize = count + 1;
            }
        }
    }

    /**
     * Gets max pool size.
     *
     * @return the max pool size
     */
    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    /**
     * Sets max pool size.
     *
     * @param maxPoolSize the max pool size
     */
    public void setMaxPoolSize(int maxPoolSize) {
        if (maxPoolSize > 0) {
            this.maxPoolSize = maxPoolSize;
        }
    }

    /**
     * Gets keep alive seconds.
     *
     * @return the keep alive seconds
     */
    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    /**
     * Sets keep alive seconds.
     *
     * @param keepAliveSeconds the keep alive seconds
     */
    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    /**
     * Gets queue capacity.
     *
     * @return the queue capacity
     */
    public int getQueueCapacity() {
        return queueCapacity;
    }

    /**
     * Sets queue capacity.
     *
     * @param queueCapacity the queue capacity
     */
    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    /**
     * Is allow core thread time out boolean.
     *
     * @return the boolean
     */
    public boolean isAllowCoreThreadTimeOut() {
        return allowCoreThreadTimeOut;
    }

    /**
     * Sets allow core thread time out.
     *
     * @param allowCoreThreadTimeOut the allow core thread time out
     */
    public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }

    /**
     * Is prestart all core threads boolean.
     *
     * @return the boolean
     */
    public boolean isPrestartAllCoreThreads() {
        return prestartAllCoreThreads;
    }

    /**
     * Sets prestart all core threads.
     *
     * @param prestartAllCoreThreads the prestart all core threads
     */
    public void setPrestartAllCoreThreads(boolean prestartAllCoreThreads) {
        this.prestartAllCoreThreads = prestartAllCoreThreads;
    }

    /**
     * Is wait for tasks to complete on shutdown boolean.
     *
     * @return the boolean
     */
    public boolean isWaitForTasksToCompleteOnShutdown() {
        return waitForTasksToCompleteOnShutdown;
    }

    /**
     * Sets wait for tasks to complete on shutdown.
     *
     * @param waitForTasksToCompleteOnShutdown the wait for tasks to complete on shutdown
     */
    public void setWaitForTasksToCompleteOnShutdown(boolean waitForTasksToCompleteOnShutdown) {
        this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
    }

    /**
     * Gets await termination seconds.
     *
     * @return the await termination seconds
     */
    public int getAwaitTerminationSeconds() {
        return awaitTerminationSeconds;
    }

    /**
     * Sets await termination seconds.
     *
     * @param awaitTerminationSeconds the await termination seconds
     */
    public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
        this.awaitTerminationSeconds = awaitTerminationSeconds;
    }

    /**
     * Gets thread name prefix.
     *
     * @return the thread name prefix
     */
    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    /**
     * Sets thread name prefix.
     *
     * @param threadNamePrefix the thread name prefix
     */
    public void setThreadNamePrefix(String threadNamePrefix) {
        if (StringUtils.hasText(threadNamePrefix)) {
            this.threadNamePrefix = threadNamePrefix;
        }
    }
}
