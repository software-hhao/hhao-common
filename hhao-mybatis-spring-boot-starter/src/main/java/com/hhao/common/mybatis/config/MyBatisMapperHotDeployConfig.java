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

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Mybatis热发布
 *
 * @author Wang
 * @since 1.0.0
 */
//@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(SqlSessionFactory.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
@ConditionalOnMissingBean(MyBatisMapperHotDeployConfig.class)
@EnableConfigurationProperties(MybatisProperties.class)
@Deprecated
public class MyBatisMapperHotDeployConfig {
    private final static Logger logger = LoggerFactory.getLogger(MyBatisMapperHotDeployConfig.class);
    private List<SqlSessionFactory> sqlSessionFactoryList;
    private List<Resource> resourceList=new ArrayList<>();

    /**
     * Instantiates a new My batis mapper hot deploy config.
     *
     * @param sqlSessionFactoryList the sql session factory list
     */
    @Autowired
    public MyBatisMapperHotDeployConfig(List<SqlSessionFactory> sqlSessionFactoryList){
        this.sqlSessionFactoryList=sqlSessionFactoryList;
        for(SqlSessionFactory sqlSessionFactory:sqlSessionFactoryList){
            Configuration configuration=sqlSessionFactory.getConfiguration();
            Set<String> loadResources=(Set<String>)getFieldValue(configuration,"loadedResources");
            for(String path:loadResources){
                if (path.endsWith(".xml")){
                    resourceList.add(new ClassPathResource(path));
                }
            }
//            Collection<MappedStatement> mappedStatements=sqlSessionFactory.getConfiguration().getMappedStatements();
//            for(Object obj:mappedStatements){
//                if (obj instanceof MappedStatement) {
//                    MappedStatement mappedStatement=(MappedStatement)obj;
//                    logger.debug("id:{},resoure:{}",mappedStatement.getId(),mappedStatement.getResource());
//                    String resource = mappedStatement.getResource();
//                    if (resource != null && resource.endsWith(".xml")) {
//                        resourceList.add(new ClassPathResource(resource));
//                    }
//                }
//            }
//            resourceList=resourceList.stream().distinct().collect(Collectors.toList());
        };
        new WatchThread().start();
    }

    /**
     * 获取对象指定属性
     */
    private Object getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            boolean accessible = field.canAccess(obj);
            field.setAccessible(true);
            Object value = field.get(obj);
            field.setAccessible(accessible);
            return value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The type Watch thread.
     */
    class WatchThread extends Thread {
        private final Logger logger = LoggerFactory.getLogger(WatchThread.class);

        @Override
        public void run() {
            startWatch();
        }

        private void startWatch() {
            try {
                WatchService watcher = FileSystems.getDefault().newWatchService();
                getWatchPaths().forEach(p -> {
                    try {
                        Paths.get(p).register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                while (true) {
                    WatchKey watchKey = watcher.take();
                    Set<String> set = new HashSet<>();
                    for (WatchEvent<?> event : watchKey.pollEvents()) {
                        set.add(event.context().toString());
                    }
                    // 重新加载xml
                    for(SqlSessionFactory sqlSessionFactory:sqlSessionFactoryList) {
                        reloadXml(set,sqlSessionFactory.getConfiguration());
                    }
                    boolean valid = watchKey.reset();
                    if (!valid) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 加载需要监控的文件父路径
         */
        private Set<String> getWatchPaths() {
            Set<String> set = new HashSet<>();
            resourceList.forEach(r -> {
                try {
                    set.add(r.getFile().getParentFile().getAbsolutePath());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            return set;
        }

        /**
         * 删除xml元素的节点缓存
         *
         * @param nameSpace xml中命名空间
         * @date ：2018/12/19
         * @author ：zc.ding@foxmail.com
         */
        private void clearMap(String nameSpace,Configuration configuration) {
            //清理Mybatis的namespace={}在mappedStatements、caches、resultMaps、parameterMaps、keyGenerators、sqlFragments(loadedResources?)中的缓存;
            Arrays.asList("mappedStatements", "caches", "resultMaps", "parameterMaps", "keyGenerators", "sqlFragments").forEach(fieldName -> {
                Object value = getFieldValue(configuration, fieldName);
                if (value instanceof Map) {
                    Map<?, ?> map = (Map) value;
                    List<Object> list = map.keySet().stream().filter(o -> o.toString().startsWith(nameSpace + ".")).collect(Collectors.toList());
                    list.forEach(k -> map.remove((Object) k));
                }
            });
        }

        /**
         * 清除文件记录缓存
         *
         * @param resource xml文件路径
         */
        private void clearSet(String resource,Configuration configuration) {
            Object value = getFieldValue(configuration, "loadedResources");
            if (value instanceof Set) {
                Set<?> set = (Set) value;
                set.remove(resource);
                set.remove("namespace:" + resource);
            }
        }

        /**
         * 重新加载set中xml
         */
        private void reloadXml(Set<String> set,Configuration configuration) {
            logger.debug("reload xml: {}", set);
            List<Resource> list = resourceList.stream()
                    .filter(p -> set.contains(p.getFilename()))
                    .collect(Collectors.toList());
            list.forEach(r -> {
                try {
                    clearMap(getNamespace(r),configuration);
                    clearSet(r.toString(),configuration);
                    XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(r.getInputStream(), configuration, r.toString(), configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    ErrorContext.instance().reset();
                }
            });
        }

        /**
         * 获取xml的namespace
         */
        private String getNamespace(Resource resource) {
            try {
                XPathParser parser = new XPathParser(resource.getInputStream(), true, null, new XMLMapperEntityResolver());
                return parser.evalNode("/mapper").getStringAttribute("namespace");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
