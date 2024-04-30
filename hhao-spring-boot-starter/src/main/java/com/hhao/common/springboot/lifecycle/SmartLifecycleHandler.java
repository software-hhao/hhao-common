package com.hhao.common.springboot.lifecycle;

import org.springframework.boot.ApplicationArguments;
import org.springframework.core.Ordered;

/**
 * The interface Smart lifecycle handler.
 *
 * @author Wang
 * @since 1.0.0
 */
public interface SmartLifecycleHandler extends Ordered {
    @Override
    default int getOrder(){
        return 0;
    }

    /**
     * Start.
     */
    default void start(ApplicationArguments applicationArguments){};

    /**
     * Stop.
     */
    default void stop(ApplicationArguments applicationArguments){}
}
