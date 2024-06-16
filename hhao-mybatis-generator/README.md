# HHAO Mybatis Generator增强模块

增强Mybatis Generator自动生成代码。

# 使用示例

1.pom文件中添加mybatis-generator插件

```
   <build>
        <plugins>
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>${mybatis-generator-maven-plugin.version}</version>
                <configuration>
                    <configurationFile>src/main/resources/generatorConfig.xml</configurationFile>
                    <verbose>true</verbose>
                    <overwrite>true</overwrite>
                    <sqlScript>classpath:schema-h2.sql</sqlScript>
                    <jdbcDriver>org.h2.Driver</jdbcDriver>
                    <jdbcURL>jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=FALSE</jdbcURL>
                    <jdbcUserId>sa</jdbcUserId>
                    <jdbcPassword>sa</jdbcPassword>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>com.h2database</groupId>
                        <artifactId>h2</artifactId>
                        <version>${h2.version}</version>
                    </dependency>
                    <dependency>
                        <groupId>io.github.software-hhao</groupId>
                        <artifactId>hhao-mybatis-generator</artifactId>
                        <version>${hhao-mybatis-generator-version}</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
```

2.创建配置文件：src/main/resources/generatorConfig.xml

```
<!DOCTYPE generatorConfiguration PUBLIC
        "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <context id="h2" targetRuntime="MyBatis3DynamicSql">
        <plugin type="org.mybatis.generator.plugins.VirtualPrimaryKeyPlugin"></plugin>
        <!-- 为模型生成序列化方法-->
        <plugin type="org.mybatis.generator.plugins.SerializablePlugin"/>
        <!-- 为生成的Java模型创建一个toString方法 -->
        <plugin type="org.mybatis.generator.plugins.ToStringPlugin"/>
        <!-- 为生成的DataObject对象添加后缀名称-->
        <plugin type="com.hhao.common.mybatis.generator.plugin.SuffixNameDataObjectClassPlugin">
            <property name="suffixName" value="Do"/>
        </plugin>
        <!-- 生成器插件-->
        <plugin type="com.hhao.common.mybatis.generator.plugin.HaoGeneratedPlugin">
            <property name="clientObjectPackage" value="com.hhao.common.springboot.mybaits.clientobject"/>
        </plugin>

        <!--用于定义注释生成器的属性-->
        <commentGenerator>
            <!--false默认值,所有生成的元素都将包含注释说该元素是生成的元素-->
            <property name="suppressAllComments" value="false"/>
            <!-- 是否取消在注释中加上时间 -->
            <property name="suppressDate" value="true"/>
            <!--false默认值,所有生成的注释将不包括元素生成时db表中的表和列注释。-->
            <property name="addRemarkComments" value="true"/>
        </commentGenerator>

        <jdbcConnection driverClass="org.h2.Driver"
                        connectionURL="jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=FALSE"
                        userId="sa" password="sa" />

        <!--元素用于定义Java类型解析器的属性。-->
        <javaTypeResolver >
            <!--强制对DECIMAL和NUMERIC字段使用java.math.BigDecimal-->
            <property name="forceBigDecimals" value="true" />
            <!--强制使用JSR-310数据类型的DATE、TIME和TIMESTAMP字段-->
            <property name="useJSR310Types" value="true"/>
        </javaTypeResolver>

        <!--定义Java model生成器的属性-->
        <javaModelGenerator targetPackage="com.hhao.common.springboot.mybaits.repository.book.module" targetProject="src/main/java">
            <!--Java模型生成器还应该修剪字符串。这意味着任何String属性的setter将调用trim函数-->
            <property name="trimStrings" value="true" />
            <!--为所有生成的Java模型对象指定根类-->
            <property name="rootClass" value="com.hhao.common.ddd.infrastructure.repository.DataObject"/>
        </javaModelGenerator>

        <!--用于定义SQL映射生成器的属性。-->
        <sqlMapGenerator targetPackage="com.hhao.common.springboot.mybaits.repository.book.mapper" targetProject="src/main/java">

        </sqlMapGenerator>

        <!--用于定义Java客户机生成器的属性。-->
        <javaClientGenerator type="MIXEDMAPPER" targetPackage="com.hhao.common.springboot.mybaits.repository.book.mapper"  targetProject="src/main/java">
            <property name="rootInterface" value="com.hhao.common.ddd.infrastructure.repository.DataObjectMapper"/>
        </javaClientGenerator>

        <!--domainObjectName：如果要采用驼峰命名，那么数据库表应该命名成book_type或book-type,否则会被全转成小写，还有一种就是这里自己命名domainObjectName="BookType"-->
        <table schema="test" tableName="book" domainObjectName="Book" modelType="flat" enableSelectByExample="false" enableCountByExample="false">
            <property name="trimStrings" value="true"/>
            <property name="useActualColumnNames" value="true"/>
            <generatedKey column="ID" sqlStatement="MySql" identity="true" />
        </table>

        <table schema="test" tableName="bookType" domainObjectName="BookType" modelType="flat" enableSelectByExample="false"  enableCountByExample="false">
            <property name="trimStrings" value="true"/>
            <property name="useActualColumnNames" value="true"/>
            <generatedKey column="ID" sqlStatement="MySql" identity="true" />
        </table>
    </context>
</generatorConfiguration>

```

3.运行 mvn mybatis-generator:generate

4.H2示例数据库脚本：

```
CREATE SCHEMA IF NOT EXISTS "test";

CREATE TABLE IF NOT EXISTS "test"."bookType" (
    "id" INT AUTO_INCREMENT PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS "test"."book" (
     "id" INT AUTO_INCREMENT PRIMARY KEY,
     "name" VARCHAR(255) NOT NULL,
     "price" DECIMAL(10, 2) NOT NULL,
     "priceCurrencyCode" VARCHAR(3) NOT NULL,
     "publicDate" DATE NOT NULL,
     "recordDateTime" DATETIME NOT NULL,
     "type" INT,
     FOREIGN KEY ("type") REFERENCES "test"."bookType"("id") ON UPDATE CASCADE ON DELETE RESTRICT
);

```
