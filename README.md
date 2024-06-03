# hhao-common基于SpringBoot的企业级通用基础架构

包含以下模块：

1. hhao-log:日志包装模块
2. hhao-money:货币类型模块
3. hhao-utils:工具类模块
4. hhao-common-core:基础模块
5. hhao-jackson:jackson应用模块
6. hhao-spring-boot-starter:SpringBoot基础模块
7. hhao-web-spring-boot-starter:Spring Boot Web模块
8. hhao-webflux-spring-boot-start:Spring Boot WebFlux模块
9. hhao-redis-spring-boot-starter:Spring Boot Redis模块,包含基于Redis的缓存、Session基础模块
10. hhao-extension-spring-boot-starter:扩展点模块
11. hhao-mybatis:Mybatis模块,实现分页等功能
12. hhao-mybatis-spring-boot-starter:Mybatis Spring Boot模块

# 使用方法：

1. 引入依赖

   ```
   <dependencyManagement>
           <dependencies>
               <dependency>
                   <groupId>io.github.software-hhao</groupId>
                   <artifactId>hhao-common</artifactId>
                   <version>3.0.3</version>
                   <type>pom</type>
                   <scope>import</scope>
               </dependency>
           </dependencies>
   </dependencyManagement>
   ```
