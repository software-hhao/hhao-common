    /**
     * 采用ASPECTJ模式进行AOP
     * 该模式要设置POM，编译时织入代码
     *
     * IntelJ中启用ASPECTJ
     * 1、从官网https://www.eclipse.org/aspectj/downloads.php#install下载aspectj-1.9.6.jar
     * 执行安装：java -jar aspectj-1.9.6.jar
     * 2、把路径C:\aspectj1.9\bin加入到path，可能需要重启
     * 3、IntelJ安装插件：AspectJ
     *      插件官网：https://www.jetbrains.com/help/idea/2021.1/aspectj-facet.html
     * 4、设置File->Settings->Build,Execution,Deployment->Compiler->Java Compiler
     *      User compiler:Ajc
     *      Path to aspectjtools.jar:aspectjtools-1.9.8.RC3.jar的安装路径
     *      Command line parameters:-showWeaveInfo
     *      钩选：Delegate to Javac，这样可以省时间，即能不用ajc就不用它编译
     * 5、pom.xml添加以下依赖：
     *         <dependency>
     *             <groupId>org.springframework</groupId>
     *             <artifactId>spring-aspects</artifactId>
     *         </dependency>
     *
     *         <dependency>
     *             <groupId>org.aspectj</groupId>
     *             <artifactId>aspectjrt</artifactId>
     *         </dependency>
     *
     *         <dependency>
     *             <groupId>org.aspectj</groupId>
     *             <artifactId>aspectjtools</artifactId>
     *         </dependency>
     *
     *         <dependency>
     *             <groupId>org.springframework</groupId>
     *             <artifactId>spring-instrument</artifactId>
     *         </dependency>
     * 6、工程项设置：
     *      File->Project Structure->Modules，选需要采用AspectJ编译的module，加入AspectJ Facets
     *      Post-compile weave mode：如果钩选，会尽可能避免使用ajc
     *      Aspect path：org.springframework:spring-aspects:.3.15
     * 7、META-INF下建aop.xml文件，添加要ajc处理包路径
     * <aspectj>
     *     <weaver options="-verbose -showWeaveInfo">
     *         <include within="com.hhao.common.springboot.web.mvc.test.server..*"/>
     *     </weaver>
     * </aspectj>
     * 经过以上步骤，IntelJ就可以编译带aspectj的文件了；
     *
     * 如果要在Maven打包时处理aspectj，则在pom.xml中加入以下：
     *    <build>
     *         <plugins>
     *             <plugin>
     *                 <groupId>org.springframework.boot</groupId>
     *                 <artifactId>spring-boot-maven-plugin</artifactId>
     *                 <version>2.6.3</version>
     *                 <configuration>
     *                     <mainClass>com.hhao.common.springboot.web.mvc.test.SpringBootMvcApp</mainClass>
     *                     <fork>true</fork>
     *                 </configuration>
     *                 <executions>
     *                     <execution>
     *                         <goals>
     *                             <goal>repackage</goal>
     *                         </goals>
     *                     </execution>
     *                 </executions>
     *             </plugin>
     *             <plugin>
     *                 <artifactId>maven-compiler-plugin</artifactId>
     *                 <version>3.8.1</version>
     *                 <configuration>
     *                     <release>11</release>
     *                     <useIncrementalCompilation>false</useIncrementalCompilation>
     *                 </configuration>
     *             </plugin>
     *             <plugin>
     *                 <groupId>org.codehaus.mojo</groupId>
     *                 <artifactId>aspectj-maven-plugin</artifactId>
     *                 <version>1.4</version>
     *                 <dependencies>
     *                     <dependency>
     *                         <groupId>org.aspectj</groupId>
     *                         <artifactId>aspectjrt</artifactId>
     *                         <version>1.9.8.RC3</version>
     *                     </dependency>
     *                     <dependency>
     *                         <groupId>org.aspectj</groupId>
     *                         <artifactId>aspectjtools</artifactId>
     *                         <version>1.9.8.RC3</version>
     *                     </dependency>
     *                 </dependencies>
     *                 <executions>
     *                     <execution>
     *                         <goals>
     *                             <goal>compile</goal>
     *                             <goal>test-compile</goal>
     *                         </goals>
     *                     </execution>
     *                 </executions>
     *                 <configuration>
     *                     <outxml>true</outxml>
     *                     <showWeaveInfo>true</showWeaveInfo>
     *                     <verbose>true</verbose>
     *                     <aspectLibraries>
     *                         <aspectLibrary>
     *                             <groupId>org.springframework</groupId>
     *                             <artifactId>spring-aspects</artifactId>
     *                         </aspectLibrary>
     *                     </aspectLibraries>
     *                     <source>11</source>
     *                     <target>11</target>
     *                     <parameters>true</parameters>
     *                 </configuration>
     *             </plugin>
     *         </plugins>
     *     </build>
     */