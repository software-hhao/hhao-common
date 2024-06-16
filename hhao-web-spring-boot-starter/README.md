# HHAO Spring Boot Web基础架构

本模块是Spring Boot Web的企业级开发的基础加构。

# 使用方法

* 导入依赖

```
        <dependency>
            <groupId>io.github.software-hhao</groupId>
            <artifactId>hhao-web-spring-boot-starter</artifactId>
        </dependency>
```

# 统一返回处理

#### 几个重要的类

@ResponseAutoWrapper注解:在control类或方法的上面加上此注解，则将返回内容包装。

**统一返回成功的数据结构：**

```
{
    // 版本
    "version": "io.github.software-hhao:hhao-common-test-springboot3:3.0.4-SNAPSHOT",
    // HTML状态码
    "status": 201,
    // HTML状态码描述
    "message": "ok",
    // 返回数据
    "data": {
        "id": 1,
        "name": "book"
    }
}
```

**统一返回异常结构：**

请查阅“统一异常处理”。

**使用示例：**

```
@RestController
public class ResponseWrapperController {
    @PostMapping("/books")
    @ResponseAutoWrapper
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.created(null).body(book);
    }

    @PutMapping("/books")
    @ResponseAutoWrapper
    public Book updateBook(@RequestBody Book book) {
        return book;
    }
}
```

或者

```
@RestController
@ResponseAutoWrapper
public class ResponseWrapperController {
    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.created(null).body(book);
    }

    @PutMapping("/books")
    public Book updateBook(@RequestBody Book book) {
        return book;
    }
}
```

# 统一异常处理

#### 几个重要的类：

**异常编码相关：**
ErrorCode： 用于描述一个异常编码的类。 包含自定义的异常编码、异常提示信息等。该类支持本地化异常提示信息。
DefaultErrorCodes：系统默认提供的异常编码。
**异常类相关：**
AbstractBaseException：受检异常类的基类,从Exception继承。
AbstractBaseRuntimeException：非受检异常的根类，从RuntimeException继承。
BusinessRuntimeException：业务异常类，从AbstractBaseRuntimeException继承。
SystemRuntimeException：系统异常类，从AbstractBaseRuntimeException继承。
UnknowRuntimeException：未知异常类，从AbstractBaseRuntimeException继承。
RequestRuntimeException：请求异常类，从BusinessRuntimeException继承。
ServerRuntimeException：服务器异常类，从BusinessRuntimeException继承。
**异常转换：**
ExceptionTransfer：异常转换器接口。
DefaultExceptionTransfer：默认异常转换器。

#### 异常返回信息：

Spring Boot异常信息配置输出全部异常信息:

```

server.error.include-message=always（建议开启）
server.error.include-binding-errors=always（建议开启）
server.error.include-path=always（默认开启）
server.error.include-exception=true（适时开启）
server.error.include-stacktrace=always（适时开启）

```

此时，采用统一返回配置的异常信息结构如下：

```

{
    // 版本信息
    "version": "io.github.software-hhao:hhao-common-test-springboot3:3.0.4-SNAPSHOT",
    // HTML状态码
    "status": 400,
    // HTML状态码对应的错误信息
    "message": "Bad Request",
    // 返回结果，此处为异常信息
    "data": {
    // 时间戳
    "timestamp": "2024-06-14T09:22:28.287+00:00",
    // HTML状态码
    "status": 400,
    // HTML状态码对应的错误信息
    "error": "Bad Request",
    // 异常类名，server.error.include-exception开启时，包含该信息
    "exception": "...略去...",
    // 异常本地化概要信息
    "message": "共计1个请求参数错误，请核查",
    // 异常请求路径
    "path": "/handleBadRequestScenarioWithBindError",
    // 参数字段异常的详细信息，server.error.include-binding-errors开启时，包含该信息
    "errors": [
        {
        // 字段异常本地化描述信息
        "message": "个数必须在1和10之间",
        // 字段名称
        "field": "name",
        // 字段异常类型
        "code": "Size",
        // 字段异常值
        "rejectedValue": ""
        }
    ],
    // 异常编码
    "errorCode": "400",
    // 异常详细信息
    "details": "...略去..."
    // 异常堆栈信息,server.error.include-stacktrace开启时，包含该信息
    "trace": "...略去..."
    }
}

```

#### 自定义异常示例：

1. 根据异常类型，选择异常基类，实现自定义异常类：

```
public class CustomException extends ServerRuntimeException {
    public CustomException(String message) {
    super(message);
    }
  
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
  
    public CustomException(String message, Object[] args) {
        super(message, args);
    }
  
    public CustomException(String code, String message, Object[] args) {
        super(code,message, args);
    }
  
    public CustomException(String message, Throwable cause, Object[] args) {
        super(message, cause, args);
    }
  
    public CustomException(String code, String message, Throwable cause, Object[] args) {
        super(code, message, cause, args);
    }
  
    public CustomException(String code, String message) {
        super(code,message);
    }
  
    public CustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
  
    public CustomException(ErrorCode errorCode) {
        super(errorCode);
    }
  
    public CustomException() {
        super();
    }
  
    public CustomException(Throwable cause) {
        super(cause);
    }
}
```

2. 资源文件message.properties中定义异常提示信息：

```
error.code.1000=check error args1 is {0}，args2 is {1}
error.code.1001=Date format error
```

3. 定义异常编码：

```
public class BuzErrorCodes {
    public static final ErrorCode ERROR_REQUEST_ERROR_1000 = new ErrorCode("1000","${error.code.1000}");
    public static final ErrorCode ERROR_REQUEST_ERROR_1001 = new ErrorCode("1001","${error.code.1001}");
}
```

主要的抛出异常方式：

* **方式一：直接抛出异常**

```
   throw new CustomException("1001","${error.code.1001}");
   throw new CustomException("1000","${error.code.1000}",new Object[]{"1","2"});
```

* **方式二：通过异常编码抛出异常**（推荐）

```
   throw new CustomException(BuzErrorCodes.ERROR_REQUEST_ERROR_1001);
   throw new CustomException(BuzErrorCodes.ERROR_REQUEST_ERROR_1000.applyArgs(new Object[]{"1","2"}));
```

# AOP链处理

#### 相关重要的类

@AOP：定义在类、方法上，用于定义拦截器

InterceptorHandler：拦截处理器

#### 自定义AOP处理链示例

1. 定义拦截处理器CustomInterceptorHandler1和CustomInterceptorHandler2，1的优先级大于2：

   CustomInterceptorHandler1：

```
public class CustomInterceptorHandler1 implements InterceptorHandler {
    private static final String ID="testaop";
    private int order=1;

    // 优先级
    @Override
    public int getOrder() {
        return order;
    }

    private List<String> getParam(Invocation invocation){
        List<String> p=(List<String>)invocation.getArguments()[0];
        if (p==null){
            p=new ArrayList<>();
        }
        return p;
    }

    // 前拦截方法
    @Override
    public boolean onBegin(Invocation invocation) {
        List<String> param=getParam(invocation);
        param.add("begin" + order);
        return true;
    }

    // 后拦截方法
    @Override
    public Object onComplete(Object result, Throwable error, Invocation invocation) {
        List<String> param=getParam(invocation);
        param.add("end" + order);
        return result;
    }

    // 判断拦截器是否适用
    @Override
    public boolean support(String id) {
        if(ID.equalsIgnoreCase(id)){
            return true;
        }
        return false;
    }
}

```

CustomInterceptorHandler2：

```
public class CustomInterceptorHandler2  implements InterceptorHandler {
 private static final String ID="testaop";
 private int order=2;

 @Override
 public int getOrder() {
     return order;
 }

 private List<String> getParam(Invocation invocation){
     List<String> p=(List<String>)invocation.getArguments()[0];
     if (p==null){
         p=new ArrayList<>();
     }
     return p;
 }

 @Override
 public boolean onBegin(Invocation invocation) {
     List<String> param=getParam(invocation);
     param.add("begin" + order);
     return true;
 }

 @Override
 public Object onComplete(Object result, Throwable error, Invocation invocation) {
     List<String> param=getParam(invocation);
     param.add("end" + order);
     return result;
 }

 @Override
 public boolean support(String id) {
     if(ID.equalsIgnoreCase(id)){
         return true;
     }
     return false;
 }
}

```

2. 拦截埋点

```
@RestController
public class AopController {
    @PostMapping("/testAop")
    @Aop(interceptorIds={"testaop"})
    public List<String> testAop(@RequestBody List<String> param) {
        return param;
    }
}
```

4. 实例化加载拦截器
   有两种常用的实例化加载拦截器的方式：

* 方法一：将拦截器定义成Bean

```
@Component
public class CustomInterceptorHandler1 implements InterceptorHandler {
    //...略去...
}
```

* 方法二：SPI加载方式

创建META-INF/services/com.hhao.common.springboot.aop.InterceptorHandler文件，文件内容：
com.hhao.common.springboot.aop.CustomInterceptorHandler1

# XSS、编解码过滤处理

#### 相关重要的类

@SafeHtml：Html安全过滤注解。可以开启Xss过滤、解码过滤。

注解属性：

xssFilterModel：xss过滤模式

* XssFilterModel.CLEAR:保留html标记，进行xss过滤;
* XssFilterModel.ESCAPE:对String类型进行html编码转换，对html标记及元素进行转换
* XssFilterModel.NONE:不执行任何操作

depthTraversal: 是否深度遍历，默认为false
decode: 解码设置。目前支持base64解码。

DecodeHandler: 解码器接口。

#### 示例

   ```
    @PostMapping("/safeHtml")
    @SafeHtml
    public SafeHtmlBean testSafeHtml(@RequestBody SafeHtmlBean safeHtml) {
        return safeHtml;
    }
   ```

# 客户端防重提交处理

#### 几个重要的类

@DuplicatePrevent: 防重提交注解
注解属性：
uniqueKey: 指定用于识别唯一性的键的属性名,建议用UUID
openServerCheck: 是否采用服务端防重提交检查。
如果为true，则采用服务端防重提交检查，否则采用客户端防重提交检查。
如果采用客户端防重提交检查，则必须在提交请求参数或head中设置request-parameter-hash参数。
request-parameter-hash参数为提交数据的md5值。
expirationTime: 防重提交过期时间，单位为秒。默认为10秒。

#### 示例

   ```
    @PostMapping("/testDuplicatePrevent")
    @DuplicatePrevent(uniqueKey = "testDuplicatePrevent",openServerCheck = true)
    public Book duplicatePrevent(@RequestBody Book book) {
        return book;
    }
   ```

# 资源文件处理

默认开启message.properties、i18n/message.properties国际化配置。

# SmartLifecycle 过滤链处理

#### 几个重要的类

SmartLifecycleHandler: smartLifecycle处理器接口。

# Request\Response过滤处理

#### 配置属性

   ```
com:
  hhao:
    config:
      #过滤器模块
      filter:
        enable: true
        #代理request
        caching-request:
          enable: true
          max-payload-length: 2097152
          #取值[cache_before,cache_after]
          #前缓存更浪费资源
          type: cache_after
          include:
          exclude: "/static/**,/favicon.ico"
        #代理response
        caching-response:
          enable: true
          include:
          exclude: "/static/**,/favicon.ico,/async1"
        #日志
        log:
          enable: true
          include:
          exclude: "/static/**,/favicon.ico"
          #exclude: "/**"
        #转发头
        forwarded-header:
          enable: true
  
   ```

# AppContext上下文处理

#### 几个重要的类

AppContext: 上下文对象

# Spring事件总线

#### 几个重要的类

SpringEventBus: 事件总线
SpringEvent: Spring事件

#### 示例

1. 定义事件

   ```
   public class CustomEvent extends SpringEvent<Book> {

       public CustomEvent(Book source) {
           super(source);
       }

       public CustomEvent(Book source, Clock clock) {
           super(source, clock);
       }
   }
   ```
2. 定义事件监听

   ```

   @Component
   public class CustomEventListener {
       @EventListener
       public void handleCustomEvent(CustomEvent customEvent) {
           System.out.println("Received custom event with annotation - " + customEvent.toString());
       }
   }

   ```
3. 发布事件

   ```
   SpringEvent<Book> event = new CustomEvent(book).setEventType("update");
   boolean result=springEventBus.publish(event,e -> {
      System.out.println("testEvent:"+e.getMessage());
   });
   ```
