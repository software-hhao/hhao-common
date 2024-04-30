
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
    private Long eventId;

    public SpringEvent(T source) {
        super(source);
    }

    public SpringEvent(T source, Clock clock) {
        super(source, clock);
    }


    public void setVersion(String version) {
        this.version = version;
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
    public String getVersion() {
        return this.version;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Override
    public T getSource() {
        return (T) super.getSource();
    }

    @Override
    public String getSourceType() {
        return null;
    }
}
