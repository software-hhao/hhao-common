# HHAO MyBatis增强模块的SpringBoot启动集成

## 使用方法

1. 添加依赖

   ```
           <dependency>
               <groupId>io.github.software-hhao</groupId>
               <artifactId>hhao-mybatis-spring-boot-starter</artifactId>
           </dependency>
   ```
2. Spring Boot 配置文件

   ```
   seata:
     enabled: false
   spring:
     datasource:
       dynamic:
         # 启用动态数据源，默认true
         enabled: true
         # 错误是否继续 默认 true
         continueOnError: true
         primary: master #设置默认的数据源或者数据源组,默认值即为master
         # 是否启用严格模式,默认不启动. 严格模式下未匹配到数据源直接报错, 非严格模式下则使用默认数据源primary所设置的数据源
         strict: true
         #是否优雅关闭数据源，默认为false，设置为true时，关闭数据源时如果数据源中还存在活跃连接，至多等待10s后强制关闭
         grace-destroy: true

         datasource:
           master:
             url: jdbc:mysql://xxxx:xxx/test?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&allowMultiQueries=true
             username: xxx
             password: xxx
             driver-class-name: com.mysql.cj.jdbc.Driver
           slave:
             url: jdbc:mysql://xxxxx:xxxx/test?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&allowMultiQueries=true
             username: xxxx
             password: xxxx@
             driver-class-name: com.mysql.cj.jdbc.Driver

   # mybatis
   mybatis:
     #Mapper xml配置文件的位置。
     mapper-locations:
       - "classpath*:com.hhao.common.springboot.mybaits.repository.**.mapper.*.xml"
     configuration:
       #指定语句默认的滚动策略。FORWARD_ONLY | SCROLL_SENSITIVE | SCROLL_INSENSITIVE | DEFAULT（等同于未设置）
       defaultResultSetType:  DEFAULT
       #配置默认的执行器。SIMPLE 就是普通的执行器；REUSE 执行器会重用预处理语句（PreparedStatement）； BATCH 执行器不仅重用语句还会执行批量更新。
       defaultExecutorType: REUSE
       #设置超时时间，它决定数据库驱动等待数据库响应的秒数
       defaultStatementTimeout: 10
       # 指定 MyBatis 应如何自动映射列到字段或属性。 NONE 表示关闭自动映射；PARTIAL 只会自动映射没有定义嵌套结果映射的字段。 FULL 会自动映射任何复杂的结果集（无论是否嵌套）。
       autoMappingBehavior: PARTIAL
       #指定发现自动映射目标未知列（或未知属性类型）的行为。
       #NONE: 不做任何反应
       #WARNING: 输出警告日志（'org.apache.ibatis.session.AutoMappingUnknownColumnBehavior' 的日志等级必须设置为 WARN）
       #FAILING: 映射失败 (抛出 SqlSessionException)
       autoMappingUnknownColumnBehavior: FAILING
       #为驱动的结果集获取数量（fetchSize）设置一个建议值。此参数只可以在查询设置中被覆盖。
       defaultFetchSize: 100
       #是否开启驼峰命名自动映射，即从经典数据库列名 A_COLUMN 映射到经典 Java 属性名 aColumn。
       mapUnderscoreToCamelCase: false
       # 指定当结果集中值为 null 的时候是否调用映射对象的 setter（map 对象时为 put）方法，这在依赖于 Map.keySet() 或 null 值进行初始化时比较有用。注意基本类型（int、boolean 等）是不能设置成 null 的。
       callSettersOnNulls: false
       # 当返回行的所有列都是空时，MyBatis默认返回 null。 当开启这个设置时，MyBatis会返回一个空实例。 请注意，它也适用于嵌套的结果集（如集合或关联）。（新增于 3.4.2）
       returnInstanceForEmptyRow: false


   ```
3. 配置文件

```
com:
  hhao:
      mybatis:
        enable: true
        preCachedPage: 0
        postCachedPage: 3
        pageSizeLimit: 20
        pageOverflowToLast: true
        supportMultiQueries: false
        supportDefaultDialect: true
        defaultCountMappedStatementIdSuffix: "-count"
        sqlDialects:
```
