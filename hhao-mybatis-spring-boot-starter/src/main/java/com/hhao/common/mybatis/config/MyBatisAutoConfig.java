
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

package com.hhao.common.mybatis.config;

import com.hhao.common.mybatis.page.PageMetaData;
import com.hhao.common.mybatis.page.executor.sql.dialect.Dialect;
import com.hhao.common.mybatis.page.executor.sql.dialect.DialectFactory;
import com.hhao.common.mybatis.page.executor.sql.dialect.MySqlDialect;
import com.hhao.common.mybatis.page.interceptor.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.log.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * The type My batis auto config.
 *
 * @author Wang
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(SqlSessionFactory.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
@ConditionalOnMissingBean(MyBatisAutoConfig.class)
@EnableConfigurationProperties(MyBatisProperties.class)
@ConditionalOnProperty(prefix = "com.hhao.config.mybatis",name = "enable",havingValue = "true",matchIfMissing = true)
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
        init(this.myBatisProperties);

    }

    /**
     * Init.
     *
     * @param myBatisProperties the my batis properties
     */
    public void init(MyBatisProperties myBatisProperties){
        PageMetaData.PRE_CACHED_PAGE= myBatisProperties.getPreCachedPage();
        PageMetaData.POST_CACHED_PAGE= myBatisProperties.getPostCachedPage();
        PageMetaData.PAGE_SIZE_LIMIT=myBatisProperties.getPageSizeLimit();
        PageMetaData.SUPPORT_MULTI_QUERIES=myBatisProperties.getSupportMultiQueries();

        //注册sql方言
        if (myBatisProperties.getSqlDialects()!=null){
            for(String name:myBatisProperties.getSqlDialects()) {
                try {
                    Class clazz = Class.forName(name);
                    Dialect dialect = (Dialect) BeanUtils.instantiateClass(clazz);
                    DialectFactory.register(dialect);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        DialectFactory.register(new MySqlDialect());
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
