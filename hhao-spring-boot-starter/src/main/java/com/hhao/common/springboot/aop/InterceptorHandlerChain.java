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

package com.hhao.common.springboot.aop;


import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 拦截器链
 *
 * @author Wang
 * @since 1.0.0
 */
public class InterceptorHandlerChain {
    private List<InterceptorHandler> interceptorChain = new ArrayList<>();

    /**
     * Add handler.
     *
     * @param handler the handler
     */
    public void addHandler(InterceptorHandler handler){
        interceptorChain.add(handler);
        //AnnotationAwareOrderComparator.sort(interceptorChain);
    }

    /**
     * 根据id，返回匹配的拦截器
     *
     * @param ids the chain ids
     * @return the list
     */
    public List<InterceptorHandler> getInterceptorChain(String[] ids){
        if(CollectionUtils.isEmpty(interceptorChain) || ids==null || ids.length==0){
            return Collections.emptyList();
        }

        List<InterceptorHandler> chain=new ArrayList<>();
        for(String id:ids){
            for(InterceptorHandler handler:interceptorChain){
                if (handler.support(id)){
                    chain.add(handler);
                }
            }
        }
        return Collections.unmodifiableList(chain);
    }
}
