package com.hhao.common.springboot.jackson;

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

import com.hhao.common.jackson.JacksonUtilFactory;

public enum SpringJacksonKeyType implements JacksonUtilFactory.JacksonKey {
    // spring mvc,webflux默认使用，在JacksonConfig配置文件中生成
    SPRING_DEFAULT,
    // spring mvc,webflux返回值包装使用
    SPRING_RETURN,
    // redis缓存？session序列化
    REDIS_SERIALIZER
}