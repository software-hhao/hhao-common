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

package com.hhao.common.springboot.safe;

import com.hhao.common.springboot.aop.InterceptorHandler;
import com.hhao.common.springboot.config.AopConfig;
import com.hhao.common.springboot.safe.filter.SafeFilter;
import org.aopalliance.intercept.Invocation;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

/**
 * 拦截器定义，相关查看AopConfig
 *
 * @author Wang
 * @since 1.0.0
 */
public class SafeHtmlInterceptorHandler implements InterceptorHandler {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(SafeHtmlInterceptorHandler.class);
    /**
     * 拦截器id
     */
    public static final String ID="safeHtmlInterceptorHandler";
    private int order = Ordered.HIGHEST_PRECEDENCE;
    private SafeFilter safeFilter;

    /**
     * Instantiates a new Safe html interceptor handler.
     *
     * @param safeFilter the safe filter
     */
    public SafeHtmlInterceptorHandler(SafeFilter safeFilter){
        this.safeFilter=safeFilter;
    }


    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public boolean support(String id) {
        return ID.equals(id);
    }

    @Override
    public boolean onBegin(Invocation invocation) {
        try {
            safeFilter.filter((MethodInvocation) invocation);
            return true;
        }catch (Throwable e){
            logger.info(e.getMessage());
            throw e;
        }
    }
}
