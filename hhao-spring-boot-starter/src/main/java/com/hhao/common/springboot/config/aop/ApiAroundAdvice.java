/*
 * Copyright 2018-2021 WangSheng.
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

package com.hhao.common.springboot.config.aop;

import com.hhao.common.springboot.safe.filter.SafeFilter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.core.Ordered;

import java.io.Serializable;

/**
 * api接口的拦截处理
 * 由AopAdvisorConfig装配
 *
 * @author Wang
 * @since 1.0.0
 */
public class ApiAroundAdvice implements Ordered, MethodInterceptor, Serializable {
    /**
     * order
     */
    private int order = Ordered.HIGHEST_PRECEDENCE;

    /**
     * 是否开启安全过滤：XSS过滤、解码过滤
     */
    private Boolean enableSafeFilter;

    /**
     * 安装过滤器
     */
    private SafeFilter safeFilter;

    @Override
    public int getOrder() {
        return this.order;
    }

    /**
     * Instantiates a new Api around advice.
     *
     * @param safeFilter the safe filter
     */
    public ApiAroundAdvice(SafeFilter safeFilter){
        this.safeFilter=safeFilter;
    }


    /**
     * Sets order.
     *
     * @param order the order
     * @return the order
     */
    public ApiAroundAdvice setOrder(int order) {
        this.order = order;
        return this;
    }

    /**
     * Gets enable safe filter.
     *
     * @return the enable safe filter
     */
    public Boolean getEnableSafeFilter() {
        return enableSafeFilter;
    }

    /**
     * Sets enable safe filter.
     *
     * @param enableSafeFilter the enable safe filter
     * @return the enable safe filter
     */
    public ApiAroundAdvice setEnableSafeFilter(Boolean enableSafeFilter) {
        this.enableSafeFilter = enableSafeFilter;
        return this;
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        if (!(mi instanceof ProxyMethodInvocation)) {
            throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
        }
        //xss过滤处理
        if (enableSafeFilter) {
            safeFilter.filter(mi);
        }
        return mi.proceed();
    }
}
