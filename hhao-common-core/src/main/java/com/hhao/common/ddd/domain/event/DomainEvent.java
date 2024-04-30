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
package com.hhao.common.ddd.domain.event;

import com.hhao.common.ddd.even.Event;

import java.util.EventObject;

/**
 * 领域事件
 *
 * @param <T> the type parameter
 * @author Wang
 * @since 1.0.0
 */
public class DomainEvent<T> extends EventObject implements Event<T> {
    private static final long serialVersionUID = -6154788287192187196L;
    private String version;
    private Long eventId;
    private String sourceType;
    private Long occurredTime=System.currentTimeMillis();

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public DomainEvent(T source) {
        super(source);
    }

    /**
     * Instantiates a new Domain event.
     *
     * @param source  the source
     * @param eventId the event id
     */
    public DomainEvent(T source,Long eventId) {
        this(source,eventId,null,null);
    }

    /**
     * Instantiates a new Domain event.
     *
     * @param source       the source
     * @param eventId      the event id
     * @param version      the version
     * @param occurredTime the occurred time
     */
    public DomainEvent(T source,Long eventId,String version,Long occurredTime) {
        super(source);
        this.eventId=eventId;
        this.version=version;
        this.occurredTime=occurredTime;
    }


    /**
     * Sets occurred time.
     *
     * @param occurredTime the occurred time
     */
    public void setOccurredTime(long occurredTime) {
        this.occurredTime = occurredTime;
    }

    @Override
    public String getVersion() {
        return version;
    }

    /**
     * Sets version.
     *
     * @param version the version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public Long getEventId() {
        return eventId;
    }

    /**
     * Sets event id.
     *
     * @param eventId the event id
     */
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Override
    public Long getOccurredTime() {
        return occurredTime;
    }

    /**
     * Sets occurred time.
     *
     * @param occurredTime the occurred time
     */
    public void setOccurredTime(Long occurredTime) {
        this.occurredTime = occurredTime;
    }

    @Override
    public String getSourceType() {
        if (this.sourceType==null && this.getSource()!=null){
            return this.getSource().getClass().getTypeName();
        }
        return this.sourceType;
    }

    /**
     * Sets source type.
     *
     * @param sourceType the source type
     */
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    @Override
    public T getSource() {
        return (T)super.getSource();
    }
}