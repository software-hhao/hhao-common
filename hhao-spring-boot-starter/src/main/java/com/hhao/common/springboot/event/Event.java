
/*
 * Copyright 2018-2022 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.springboot.event;


import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @author Wang
 * @since 2022/2/25 22:38
 */
public class Event extends ApplicationEvent{
    public Event(Object source) {
        super(source);
    }

    public Event(Object source, Clock clock) {
        super(source, clock);
    }
}
