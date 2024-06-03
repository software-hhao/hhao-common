# Jackson模块

封装了Jackson的处理，提供日期、时间、Enum等数据类型的支持。

主要的几个类：

JacksonUtilFactory：生成JacksonUtil的工厂类。

JacksonUtil：封装了对Jackson的序列化与反序列化操作。

# 使用方法

1. 依赖引用：

   ```
           <dependency>
               <groupId>io.github.software-hhao</groupId>
               <artifactId>hhao-jackson</artifactId>
           </dependency>
   ```
2. 获取JacksonUtil:

   JacksonUtilFactory.getJsonUtil()：获取对Json处理的工具类。

   JacksonUtilFactory.getXmlUtil()：获取对Xml处理的工具类。
