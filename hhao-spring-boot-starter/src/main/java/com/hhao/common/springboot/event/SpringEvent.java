
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


import com.hhao.common.ddd.even.Event;
import com.hhao.common.jackson.JacksonUtilFactory;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * Spring事件基类
 *
 * @author Wang
 * @since 2022 /2/25 22:38
 */
public class SpringEvent<T> extends ApplicationEvent implements Event<T> {
    private String version;
    private String eventType;
    private Long eventId;

    public SpringEvent(T source) {
        super(source);
    }

    public SpringEvent(T source,String eventType) {
        super(source);
        this.eventType=eventType;
    }

    public SpringEvent(T source, Clock clock) {
        super(source, clock);
    }

    public SpringEvent(T source, Clock clock,String eventType) {
        super(source, clock);
        this.eventType=eventType;
    }

    public SpringEvent<T> setVersion(String version) {
        this.version = version;
        return this;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    public SpringEvent<T> setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    @Override
    public Long getEventId() {
        return eventId;
    }

    @Override
    public Long getOccurredTime() {
        return super.getTimestamp();
    }

    @Override
    public T getSource() {
        return (T) super.getSource();
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }

    public SpringEvent<T> setEventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    @Override
    public String toString() {
        return JacksonUtilFactory.getJsonUtil().obj2String(this);
    }
}
