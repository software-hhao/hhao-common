/*
 * Copyright 2020-2021 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hhao.common.springboot.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.util.function.Consumer;

/**
 * @author Wang
 * @since 2022/2/25 22:13
 */
public class EventBus{
    protected final Logger logger = LoggerFactory.getLogger(EventBus.class);
    private ApplicationEventPublisher applicationEventPublisher;

    public EventBus(ApplicationEventPublisher applicationEventPublisher){
        this.applicationEventPublisher=applicationEventPublisher;
    }

    public boolean publish(Object event){
        return publish(event,null);
    }

    public boolean publish(Object event, Consumer<Exception> errorConsumer){
        try {
            applicationEventPublisher.publishEvent(event);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (errorConsumer!=null) {
                errorConsumer.accept(e);
            }
            return false;
        }
    }
}