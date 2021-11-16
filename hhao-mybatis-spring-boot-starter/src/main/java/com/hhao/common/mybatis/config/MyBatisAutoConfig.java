
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

package com.hhao.common.mybatis.config;

import com.hhao.common.mybatis.page.PageMetaData;
import com.hhao.common.mybatis.page.executor.PageExecutor;
import com.hhao.common.mybatis.page.executor.PageExecutorBuilder;
import com.hhao.common.mybatis.page.executor.StaticPageExecutor;
import com.hhao.common.mybatis.page.executor.DynamicPageExecutor;
import com.hhao.common.mybatis.page.executor.sql.SqlExecutor;
import com.hhao.common.mybatis.page.executor.sql.SqlExecutorBuilder;
import com.hhao.common.mybatis.page.executor.sql.SqlExecutorWithMysql;
import com.hhao.common.mybatis.page.interceptor.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractResourceBasedMessageSource;

import java.util.List;

/**
 * The type My batis auto config.
 *
 * @author Wang
 * @since  1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(SqlSessionFactory.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
@ConditionalOnMissingBean(MyBatisAutoConfig.class)
@EnableConfigurationProperties(MyBatisProperties.class)
public class MyBatisAutoConfig implements ApplicationContextAware {
    private List<SqlSessionFactory> sqlSessionFactoryList;
    private MyBatisProperties myBatisProperties;
    /**
     * The Application context.
     */
    protected ApplicationContext applicationContext;
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(MyBatisAutoConfig.class);

    /**
     * Instantiates a new My batis auto config.
     *
     * @param myBatisProperties     the my batis properties
     * @param sqlSessionFactoryList the sql session factory list
     * @param messageSource         the message source
     */
    @Autowired
    public MyBatisAutoConfig(MyBatisProperties myBatisProperties,List<SqlSessionFactory> sqlSessionFactoryList,MessageSource messageSource) {
        this.myBatisProperties=myBatisProperties;
        //对所有数据源加中断
        this.sqlSessionFactoryList=sqlSessionFactoryList;
        addInterceptor();
        //初始化注册分页执行器和SQL执行器
        init(myBatisProperties);

    }

    /**
     * Init.
     *
     * @param myBatisProperties the my batis properties
     */
    public void init(MyBatisProperties myBatisProperties){
        if (myBatisProperties.getPageExecutors()!=null){
            for(String name:myBatisProperties.getPageExecutors()) {
                try {
                    Class clazz = Class.forName(name);
                    PageExecutor pageExecutor = (PageExecutor) BeanUtils.instantiateClass(clazz);
                    PageExecutorBuilder.register(pageExecutor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            PageMetaData.PRE_CACHED_PAGE= myBatisProperties.getPreCachedPage();
            PageMetaData.POST_CACHED_PAGE= myBatisProperties.getPostCachedPage();
        }
        if (myBatisProperties.getSqlExecutors()!=null){
            for(String name:myBatisProperties.getSqlExecutors()) {
                try {
                    Class clazz = Class.forName(name);
                    SqlExecutor sqlExecutor = (SqlExecutor) BeanUtils.instantiateClass(clazz);
                    SqlExecutorBuilder.register(sqlExecutor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //默认注册的跟在最后
        PageExecutorBuilder.register(new DynamicPageExecutor());
        PageExecutorBuilder.register(new StaticPageExecutor());
        SqlExecutorBuilder.register(new SqlExecutorWithMysql());
    }

    /**
     * Add interceptor.
     */
    public void addInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            if (!containsInterceptor(configuration, pageInterceptor)) {
                configuration.addInterceptor(pageInterceptor);
            }
        }
    }

    private boolean containsInterceptor(org.apache.ibatis.session.Configuration configuration, Interceptor interceptor) {
        try {
            // getInterceptors since 3.2.2
            return configuration.getInterceptors().contains(interceptor);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Gets application context.
     *
     * @return the application context
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
