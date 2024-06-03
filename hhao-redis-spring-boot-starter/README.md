# Spring Boot Redis模块

Spring Boot Redis模块,包含基于Redis的缓存、Session处理。

# 使用方法

1. 添加依赖

   ```
           <dependency>
               <groupId>io.github.software-hhao</groupId>
               <artifactId>hhao-redis-spring-boot-starter</artifactId>
           </dependency>
           <!--开启session时添加-->
           <dependency>
               <groupId>org.springframework.session</groupId>
               <artifactId>spring-session-data-redis</artifactId>
           </dependency>
           <!--开启cache时添加-->
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-cache</artifactId>
           </dependency>
   ```
