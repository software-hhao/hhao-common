package com.hhao.common.springboot.event;

import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import org.springframework.context.ApplicationListener;

/**
 * 默认的事件异常处理类
 *
 * @author Wang
 * @since 1.0.0
 */
public class DefaultEventPublishErrorHandle implements ApplicationListener<EventPublishErrorEvent> {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(DefaultEventPublishErrorHandle.class);
    @Override
    public void onApplicationEvent(EventPublishErrorEvent event) {
        logger.error("public event error;error info:[{}];event source:[{}]",event.getSource(),event.getEventSource());
    }
}
