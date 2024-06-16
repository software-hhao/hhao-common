# HHAO Spring Boot增强模块

该模块增强同时适用于Web Servlet及Web Reactive，是hhao-spring-boot-starter与hhao-webflux-spring-boot-start的父级模块。
具体功能示例，请参见hhao-spring-boot-starter与hhao-webflux-spring-boot-start。

提供了以下基础功能：

* @Aop注解：用于自定义的统一AOP链处理。
* Spring Convert与Formatter扩展处理。
* @DuplicatePrevent注解：防止客户端重复提交处理。
* SpringEventBus：Spring事件总线处理。
* 统一异常处理。
* Jackson json/xml增强处理。
* lifecycle：Spring Boot生命周期管理。
* metadata：基于Spring Boot的元数据管理。
* 统一返回处理。
* @SafeHtml：防止XSS注入、加解密注解。
* validate：基于Hibernate Validate的增强处理。
* AppContext：统一应用上下文处理。
