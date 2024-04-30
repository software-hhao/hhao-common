package com.hhao.common.springboot.event;

/**
 * Spring事件异常事件
 *
 * @author Wang
 * @since 1.0.0
 */
public class EventPublishErrorEvent extends SpringEvent<Throwable>{
    /**
     * The Event source.
     */
    protected SpringEvent eventSource;

    /**
     * Instantiates a new Spring event bus exception event.
     *
     * @param throwable   the source
     * @param eventSource the event source
     */
    public EventPublishErrorEvent(Throwable throwable, SpringEvent eventSource) {
        super(throwable);
        this.eventSource=eventSource;
    }

    /**
     * Gets event source.
     *
     * @return the event source
     */
    public SpringEvent getEventSource() {
        return eventSource;
    }

    /**
     * Sets event source.
     *
     * @param eventSource the event source
     */
    public void setEventSource(SpringEvent eventSource) {
        this.eventSource = eventSource;
    }
}
