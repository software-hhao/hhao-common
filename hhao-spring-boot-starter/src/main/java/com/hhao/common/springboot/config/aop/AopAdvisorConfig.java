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

import com.hhao.common.springboot.config.AbstractBaseConfig;
import com.hhao.common.springboot.safe.filter.SafeFilter;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 接口api的aop拦截处理
 * 拦截路径由参数属性文件中参数com.hhao.aop.pointcut.api定义;
 * 如果该参数未定义或为空，则拦截不生效;
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(AopAdvisorConfig.class)
@EnableAspectJAutoProxy
public class AopAdvisorConfig extends AbstractBaseConfig {
    /**
     * pointcut定义，如未定义或为空，则不会启动接口的aop
     */
    @Value("${com.hhao.aop.pointcut:}")
    private String pointcutApi;

    /**
     * 是否开启xss过滤
     * 前提是需要开启接口的aop
     */
    @Value("${com.hhao.aop.enable-safe-filter:true}")
    private Boolean enableSafeFilter;

    /**
     * 定义api接口的advisor
     * 当设置com.hhao.aop.pointcut-api值时生成Bean
     *
     * @param safeFilter the safe filter
     * @return aspect j expression pointcut advisor
     */
    @Bean
    @ConditionalOnExpression("not ('${com.hhao.aop.pointcut:}' matches '(\\s)*')")
    public AspectJExpressionPointcutAdvisor apiAdvisor(SafeFilter safeFilter) {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(pointcutApi);
        ApiAroundAdvice apiAroundAdvice=new ApiAroundAdvice(safeFilter);
        apiAroundAdvice.setEnableSafeFilter(enableSafeFilter);

        advisor.setAdvice(apiAroundAdvice);
        return advisor;
    }
}
