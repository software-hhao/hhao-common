# HHAO Spring Boot Redis模块

Spring Boot Redis模块,包含基于Redis的缓存、Session处理。

# 使用方法

# 添加依赖

---

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

# 关于Redis

在Web Servlet环境下，系统定义了三个RedisTemplate：

* redisTemplate：默认采用的RedisTemplate。key采用String的序列化方式，value/hash value采用jackson json的序列化方式。
* stringRedisTemplate：key和value/hash value采用String的序列化方式。
* jdkSerializerRedisTemplate：key和value/hash value采用JDK的序列化方式。

下面的Redis Cache与Session采用的RedisTemplate，key采用String的序列化方式，value/hash value采用jackson json的序列化方式。

在Web Reactive环境下，系统也定义了三个RedisTemplate:

* reactiveRedisTemplate:默认采用的ReactiveRedisTemplate。key采用String的序列化方式，value/hash value采用jackson json的序列化方式。
* reactiveStringRedisTemplate:key和value/hash value采用String的序列化方式。
* jdkSerializerReactiveRedisTemplate:key和value/hash value采用JDK的序列化方式。

在代码中，引用RedisTemplate的示例：

```
@RestController
public class RedisController {
    private RedisTemplate redisTemplate;

    @ResponseAutoWrapper
    public RedisController(@Qualifier("redisTemplate") RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/testRedis")
    public Student testRedis(@RequestBody Student student) {
        redisTemplate.opsForValue().set("student", student);
        student = (Student) redisTemplate.opsForValue().get("student");
        return student;
    }
}
```

# 关于cache

---

**在配置文件中，配置采用cache的模式：**

* 设置com.hhao.config.redis.cache.mode=PROXY，相当于执行@EnableCaching(mode = AdviceMode.PROXY,order = 100)
* 设置com.hhao.config.redis.cache.mode=ASPECTJ，相当于执行@EnableCaching(mode = AdviceMode.ASPECTJ,order = 100)。注：该模式需要AspectJ的支持。
* 设置为NONE，需要自已定义@EnableCaching开启缓存。

---

**为不同的cache设置不同的配置：**

可以为不同的cache创建不同的RedisCacheConfiguration Bean。Spring Boot在启动时，会注入全部的RedisCacheConfiguration Bean到RedisCacheManager中，生成RedisCache。
生成的RedisCache的name默认情况就是Bean的名称去除"RedisCacheConfiguration"这个固定的后缀。

---

**为不同的cache设置TTL：**

有几种方式可以方便的为cache设置ttl。

第一种：为该cache创建一个配置Bean。参见"为不同的cache设置不同的配置"。

第二种：通过配置文件为cache设置ttl。一个示例如下：（建议）

```
com:
  hhao:
    config:
      redis:
        cache:
          # 是否启用缓存
          enable: true
          # 是否支持事务
          enableTransactions: true
          # 是否允许动态创建RedisCache
          allowRuntimeCacheCreation: true
          # SCAN策略需要一个批处理大小来避免过多的Redis命令往返
          batchSize: 1000
          # 是否开启TTI
          enableTimeToIdle: true
          # AOP模式，必须设置，取值PROXY、ASPECTJ,NONE
          mode: PROXY
          cacheConfigs:
            # 缓存名称，注意，不要有#号
            student1:
              # ttl,单位秒
              timeToLive: 30
              # 是否开启TTI
              enableTimeToIdle: false
            # 缓存名称，注意，不要有#号
            student2:
              # ttl,单位秒
              timeToLive: 30
              # 是否开启TTI
              enableTimeToIdle: false
```

第三种：织入缓存名称中。

例如：@Cacheable(value = "student1#30")。

缓存名称中，#为保留字符，#后跟一个整型数值，指定该缓存的TTL。

---

**关于CacheManager**

系统提供了两个RedisCacheManager。

默认的RedisCacheManager使用无锁RedisCacheWriter来读取和写入二进制值。 无锁缓存提高了吞吐量。

要在代码中手动引入CacheManager并操作Cache，示例如下：

```
    // 注入CacheManager
    @Resource(name = "cacheManager")
    private CacheManager cacheManager;

    // 手动清除缓存
    cacheManager.getCache("student1").clear();
```

系统另外提供了一个带锁的RedisCacheWriter，可用需要批操作的情况。引入该CacheManager示例如下：

```
    // 注入CacheManager
    @Resource(name = "cacheManagerWithLockCacheWrite")
    private CacheManager cacheManager;
```

---

**关于KeyGenerator**
系统提供了一个基于方法与方法参数值生成MD5的KeyGenerator。可用于分页查询等缓存key的生成。
与Cache注解结合使用，示例如下：

```
    @Cacheable(value = "student:find",keyGenerator="methodKeyGenerator",sync = true,condition = "#studentPageQuery!=null")
    public List<Student> find(StudentPageQuery studentPageQuery){
        //...此处代码略去...
    }
```

手动调用示例如下：

```
    // 注入KeyGenerator
    @Autowired
    KeyGenerator methodKeyGenerator;
  
    @Resource(name = "cacheManager")
    private CacheManager cacheManager;
  
    public List<Student> find(StudentPageQuery studentPageQuery){
        // 手动获取缓存
        Class<?> clazz = StudentServer.class;
        Method method = clazz.getMethod("find", StudentPageQuery.class);
        String key=(String)methodKeyGenerator.generate(clazz,method,studentPageQuery);
        List<Student> results=cacheManager.getCache("student:find").get("spring:cache:student:find:" + key,List.class)
        //...此处代码略去...
    }
```

# 关于session

默认在Web Servlet环境下，会自动开启基于RedisIndexedHttpSession。即相当于执行： @EnableRedisIndexedHttpSession
默认在Web Reactive环境下，会自动开启RedisWebSession。即相当于执行： @EnableRedisWebSession
如果需要自定义Session配置，可以在代码中自己开启@EnableRedisIndexedHttpSession或@EnableRedisWebSession，设置@Order(0)即可。

# 关于完整的Redis、Cache、Session配置项

```
com:
  hhao:
    config:
      redis:
        enable: true
        redisTemplate:
          enableTransactionSupport: false
        session:
          enable: true
        cache:
          # 是否启用缓存
          enable: true
          # 是否支持事务
          enableTransactions: true
          # 是否允许动态创建RedisCache
          allowRuntimeCacheCreation: true
          # SCAN策略需要一个批处理大小来避免过多的Redis命令往返
          batchSize: 1000
          # 是否开启TTI
          enableTimeToIdle: true
          # AOP模式，必须设置，取值PROXY、ASPECTJ,NONE
          mode: PROXY
          cacheConfigs:
            # 缓存名称，注意，不要有#号
            default:
              # ttl,单位秒
              timeToLive: 30
              # 是否开启TTI
              enableTimeToIdle: false
```
