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

package com.hhao.common.utils.bean2map;

/**
 * 将字符串转成数值类型
 *
 * @author Wang
 * @since 1.0.0
 */
public class  String2NumberConvert implements Convert<String,Number>{
    @Override
    public boolean support(Class<String> sourceClass, Class<Number> targetClass) {
        if (String.class.isAssignableFrom(sourceClass) && Number.class.isAssignableFrom(targetClass)){
            return true;
        }
        return false;
    }

    @Override
    public <R extends Number> R convert(String source,Class<R> targetClass) {
        if (Integer.class.isAssignableFrom(targetClass)){
            return (R)Integer.valueOf(source);
        }else if (Double.class.isAssignableFrom(targetClass)){
            return (R)Double.valueOf(source);
        }else if (Float.class.isAssignableFrom(targetClass)){
            return (R)Float.valueOf(source);
        }else if (Long.class.isAssignableFrom(targetClass)){
            return (R)Long.valueOf(source);
        }
        return null;
    }
}
