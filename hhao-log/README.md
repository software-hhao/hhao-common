# HHAO 日志包装模块

对日志记录进行包装。
包装后，默认采用Slf4j+logback。

# 使用示例：

1. 引入依赖

   ```
           <dependency>
               <groupId>io.github.software-hhao</groupId>
               <artifactId>hhao-log</artifactId>
           </dependency>
   ```
2. 创建Logger：

Logger logger=LoggerFactory.getLogger(xxxx);
