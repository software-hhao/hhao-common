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
package com.hhao.common.springboot.event;

import com.hhao.common.Context;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.function.Consumer;

/**
 * Spring事件总线
 *
 * @author Wang
 * @since 2022 /2/25 22:13
 */
public class SpringEventBus{
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(SpringEventBus.class);
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * Instantiates a new Spring event bus.
     *
     * @param applicationEventPublisher the application event publisher
     */
    public SpringEventBus(ApplicationEventPublisher applicationEventPublisher){
        this.applicationEventPublisher=applicationEventPublisher;
    }

    /**
     * Publish boolean.
     *
     * @param event the event
     * @return the boolean
     */
    public boolean publish(SpringEvent event){
        return publish(event,null);
    }

    /**
     * Publish boolean.
     *
     * @param event         the event
     * @param errorConsumer the error consumer
     * @return the boolean
     */
    public boolean publish(SpringEvent event, Consumer<Exception> errorConsumer){
        try {
            applicationEventPublisher.publishEvent(event);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (errorConsumer!=null) {
                errorConsumer.accept(e);
            }
            this.publish(new EventPublishErrorEvent(e,event));
            return false;
        }
    }

    /**
     * 待完成
     *
     * @param event
     * @return
     */
    public boolean store(SpringEvent event){
        return false;
    }

    public EventBuilder eventBuilderInstance(Object source){
        return new EventBuilder(source);
    }

    public static class EventBuilder{
        private String version= Context.getInstance().getVersion().toFullString();
        private Object source;
        private long id=-1;
        private long occurredTime=-1;

        public EventBuilder(Object source){
            this.source=source;
        }

        public EventBuilder setVersion(String version) {
            this.version = version;
            return this;
        }

        public EventBuilder setSource(Object source) {
            this.source = source;
            return this;
        }

        public EventBuilder setId(long id) {
            this.id = id;
            return this;
        }

        public EventBuilder setOccurredTime(long occurredTime) {
            this.occurredTime = occurredTime;
            return this;
        }

        public SpringEvent build(){
            SpringEvent springEvent;

            if (this.occurredTime!=-1){
                springEvent=new SpringEvent(source, Clock.fixed(Instant.ofEpochMilli(this.occurredTime), ZoneOffset.UTC));
            }else{
                springEvent=new SpringEvent(source);
            }
            springEvent.setEventId(id);
            springEvent.setVersion(version);

            return springEvent;
        }
    }
}