/*
 * Copyright 2018-2021 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.springboot.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.hhao.common.springboot.response.ResultWrapper;
import com.hhao.common.springboot.utils.BeanUtils;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * http://springfox.github.io/springfox/docs/current/#quick-start-guides
 * 访问地址：http://127.0.0.1:8080/swagger-ui/index.html
 * http://127.0.0.1:8080/doc.html
 * swagger配置
 * swagger包要在项目中加载
 * 要开启swagger配置
 *
 * @author Wang
 * @since 1.0.0
 */
public class SwaggerBaseConfig {
    /**
     * The Type resolver.
     */
    protected TypeResolver typeResolver;
    /**
     * The Swagger group properties.
     */
    protected SwaggerGroupProperties swaggerGroupProperties;

    protected ApplicationContext applicationContext;

    /**
     * Instantiates a new Swagger config.
     *
     * @param typeResolver           the type resolver
     * @param swaggerGroupProperties the swagger group properties
     * @param applicationContext     the application context
     */
    public SwaggerBaseConfig(TypeResolver typeResolver, SwaggerGroupProperties swaggerGroupProperties, ApplicationContext applicationContext){
        this.typeResolver=typeResolver;
        this.swaggerGroupProperties=swaggerGroupProperties;
        this.applicationContext=applicationContext;
        this.generateDocketBean();
    }

    /**
     * 自注册，生成Docket Bean
     */
    private void generateDocketBean(){
        if (swaggerGroupProperties.getEnable()){
            for(SwaggerProperties properties:swaggerGroupProperties.getSwaggerGroups()){
                Docket docket= BeanUtils.registerBean(Docket.class,properties.getGroupName()+ UUID.randomUUID().toString(),new Object[]{DocumentationType.SWAGGER_2},this.applicationContext);
                buildDocket(docket,properties);
            }
        }
    }

    /**
     * Gets api info.
     *
     * @param properties the properties
     * @return the api info
     */
    protected ApiInfo getApiInfo(SwaggerProperties properties)  {
        String description= "";
        try (
                FileReader fileReader = new FileReader(ResourceUtils.getFile(properties.getApiDescriptionUrl()));
                BufferedReader bufferedReader = new BufferedReader(fileReader);
        ) {
            description = bufferedReader
                    .lines()
                    .map(line->{
                        line=line.replaceAll("\\s|\\t","&nbsp;&nbsp;&nbsp;");
                        return line + "<br/>";
                    })
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Contact contact = new Contact(properties.getContactName(), properties.getContactUrl(), properties.getContactEmail());
        ApiInfo apiInfo=new ApiInfo(
                //标题
                properties.getApiTitle(),
                //描述
                description,
                //版本
                properties.getApiVersion(),
                //组织链接
                properties.getApiTermsOfServiceUrl(),
                //联系人信息
                contact,
                //许可
                properties.getApiLicense(),
                //许可连接
                properties.getApiLicenseUrl(),
                //扩展
                new ArrayList<>()
        );
        return apiInfo;
    }

    /**
     * requestHandler选择器
     * 取值：any|none|basePackage:包路径
     *
     * @param properties the properties
     * @return predicate
     */
    protected Predicate<RequestHandler> getRequestHandlerSelector(SwaggerProperties properties){
        String [] selectors=StringUtils.split(properties.getRequestHandlerSelectors(),":");
        if (selectors==null){
            return RequestHandlerSelectors.any();
        }
        if (selectors.length==1){
            if (StringUtils.startsWithIgnoreCase("any",selectors[0])) {
                return RequestHandlerSelectors.any();
            }
            return RequestHandlerSelectors.none();
        }else if (selectors.length==2 && StringUtils.startsWithIgnoreCase("basePackage",selectors[0])){
            return RequestHandlerSelectors.basePackage(selectors[1]);
        }
        return RequestHandlerSelectors.none();
    }

    /**
     * 路径选择器
     * 取值：none|any|regex:路径|ant:路径
     *
     * @param properties the properties
     * @return predicate
     */
    protected Predicate<String>  getPathSelector(SwaggerProperties properties){
        String [] selectors=StringUtils.split(properties.getPathSelectors(),":");
        if (selectors==null){
            return PathSelectors.any();
        }
        if (selectors.length==1){
            if (StringUtils.startsWithIgnoreCase("any",selectors[0])) {
                return PathSelectors.any();
            }else if(StringUtils.startsWithIgnoreCase("none",selectors[0])){
                return PathSelectors.none();
            }
        }else if (selectors.length==2){
            if (StringUtils.startsWithIgnoreCase("regex",selectors[0])){
                return PathSelectors.regex(selectors[1]);
            }else if (StringUtils.startsWithIgnoreCase("ant",selectors[0])) {
                return PathSelectors.ant(selectors[1]);
            }
        }
        return PathSelectors.none();
    }

    /**
     * Build docket docket.
     *
     * @param docket     the docket
     * @param properties the properties
     * @return the docket
     */
    protected Docket buildDocket(Docket docket, SwaggerProperties properties){
        return  docket
                //指定分组名称
                .groupName(properties.getGroupName())
                //添加api详情信息，包含标题、描述、版本之类的
                .apiInfo(getApiInfo(properties))
                .enable(true)
                //返回ApiSelectorBuilder的实例，以便对通过swagger公开的端点进行细粒度控制
                .select()
                //对公开的端点进行细粒度控制
                //api过滤
                //.apis(RequestHandlerSelectors.any())
                .apis(getRequestHandlerSelector(properties))
                //路径过滤
                .paths(getPathSelector(properties))
                //选择器需要在配置api和路径选择器之后构建
                .build()
                //如果servlet路径不为/，这里设置路径的前缀
                .pathMapping(properties.getPathMapping())
                //替换规则的构建，在解析Model类属性的时候，将LocalDate用String替换
                .directModelSubstitute(LocalDate.class, String.class)
                .directModelSubstitute(DateTime.class, String.class)
                .directModelSubstitute(LocalDateTime.class, String.class)
                .directModelSubstitute(ZonedDateTime.class, String.class)
                .directModelSubstitute(Instant.class, String.class)
                .useDefaultResponseMessages(properties.isUseDefaultResponseMessages())
                .enableUrlTemplating(properties.isEnableUrlTemplating())
                .additionalModels(typeResolver.resolve(ResultWrapper.class));
    }

//    @Bean
//    @ConditionalOnBean(name="swaggerPropertiesDefault")
//    public Docket docketDefault(@Qualifier("swaggerPropertiesDefault") SwaggerProperties properties){
//        return buildDocket(properties);
//    }
//
//    @Bean
//    @ConditionalOnBean(name="swaggerPropertiesGroup1")
//    public Docket DocketGroup1(@Qualifier("swaggerPropertiesGroup1") SwaggerProperties properties){
//        return buildDocket(properties);
//    }
//
//    @Bean
//    @ConditionalOnBean(name="swaggerPropertiesGroup2")
//    public Docket DocketGroup2(@Qualifier("swaggerPropertiesGroup2") SwaggerProperties properties){
//        return buildDocket(properties);
//    }

}

// 以下是部份来自手册的代码，可参考
// public Docket build(SwaggerProperties properties){
// return new Docket(DocumentationType.SWAGGER_2)
// //指定分组名称
// .groupName(Docket.DEFAULT_GROUP_NAME)
// //添加api详情信息，包含标题、描述、版本之类的
// .apiInfo(apiInfo(properties))
// .enable(properties.enable)
// //返回ApiSelectorBuilder的实例，以便对通过swagger公开的端点进行细粒度控制
// .select()
// //对公开的端点进行细粒度控制
// //api过滤
// //.apis(RequestHandlerSelectors.any())
// .apis(RequestHandlerSelectors.basePackage("com.hhao.common.springboot.web.mvc.test.api"))
// //路径过滤
// .paths(PathSelectors.any())
// //选择器需要在配置api和路径选择器之后构建
// .build()
// //如果servlet路径不为/，这里设置路径的前缀
// .pathMapping("/")
// //替换规则的构建，在解析Model类属性的时候，将LocalDate用String替换
// .directModelSubstitute(LocalDate.class, String.class)
// .directModelSubstitute(DateTime.class, String.class)
// .directModelSubstitute(LocalDateTime.class, String.class)
// .directModelSubstitute(ZonedDateTime.class, String.class)
// .directModelSubstitute(Instant.class, String.class)
// //泛型替换规则的构建，下面是将ResponseEntity<T>替换成一个具体类型的参数
// //.genericModelSubstitutes(ResponseEntity.class)
// //具体替换设置
// //.alternateTypeRules(
// //newRule的参数：原类型、替换类型
// //下例将DeferredResult<ResponseEntity<WildcardType>>替换为WildcardType类型
// //        newRule(typeResolver.resolve(DeferredResult.class, typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
// //                typeResolver.resolve(WildcardType.class)))
// //标志，指示是否需要使用默认的http响应代码。
// .useDefaultResponseMessages(false)
// //允许全局覆盖不同http方法的响应消息。
// //参数：http方法,对应Response消息覆盖
// //                .globalResponses(HttpMethod.GET,
// //                        singletonList(new ResponseBuilder()
// //                                .code("500")
// //                                .description("500 message")
// //                                .representation(MediaType.TEXT_XML)
// //                                .apply(r ->
// //                                        r.model(m ->
// //                                                m.referenceModel(ref ->
// //                                                        ref.key(k ->
// //                                                                //并指出它将使用响应模型Error(将在其他地方定义)
// //                                                                k.qualifiedModelName(q ->
// //                                                                        q.namespace("some:namespace")
// //                                                                                .name("ERROR"))))))
// //                                .build()))
// //设置用于保护api的安全方案。支持的方案有ApiKey、BasicAuth和OAuth
// //.securitySchemes(singletonList(apiKey()))
// //提供全局设置操作的安全上下文的方法。
// //.securityContexts(singletonList(securityContext()))
// //向处理器发出信号，要求生成的路径应该尝试使用表单样式的查询扩展
// //例如：
// //http://example.org/findCustomersBy?name=Test解析为http://example.org/findCustomersBy{?name}
// //http://example.org/findCustomersBy?zip=76051解析为http://example.org/findCustomersBy{?zip}
// .enableUrlTemplating(true)
// //配置全局默认的path-/request-/headerparameters
// //即那些在每个rest api请求中都需要，但是却不在Control参数中的，例如身份验证信息
// //.globalRequestParameters(
// //        singletonList(new springfox.documentation.builders.RequestParameterBuilder()
// //                .name("someGlobalParameter")
// //                .description("Description of someGlobalParameter")
// //                .in(ParameterType.QUERY)
// //               .required(true)
// //                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
// //                .build()))
// //添加标记定义services/operations可以选择的所有可用标记的一种方法。目前只有名称和描述。
// //.tags(new Tag("Pet Service", "All apis relating to pets"))
// //应用程序中是否存在不可“到达”的模型?不可达是当我们有我们想要描述的模型，但没有在任何操作中显式使用。这方面的一个例子是返回序列化为字符串的模型的操作。
// .additionalModels(typeResolver.resolve(ResultWrapper.class));
// }
//
// @Autowired
// private TypeResolver typeResolver;
//
// /**
//  * 这里我们使用ApiKey作为安全模式，它由mykey名称标识
//  * @return
// */
//private ApiKey apiKey() {
//    return new ApiKey("mykey", "api_key", "header");
//}
//
//    private SecurityContext securityContext() {
//        return SecurityContext.builder()
//                .securityReferences(defaultAuth())
//                //此安全上下文应用于的路径的选择器
//                .forPaths(regex("/anyPath.*"))
//                .build();
//    }
//
//    List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope= new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return singletonList(
//                //这里我们使用与安全方案mykey中定义的相同的密钥
//                new SecurityReference("mykey", authorizationScopes));
//    }
//
//    @Bean
//    SecurityConfiguration security() {
//        //swagger-ui安全配置为oauth和apiKey设置
//        return SecurityConfigurationBuilder.builder()
//                .clientId("test-app-client-id")
//                .clientSecret("test-app-client-secret")
//                .realm("test-app-realm")
//                .appName("test-app")
//                .scopeSeparator(",")
//                .additionalQueryStringParams(null)
//                .useBasicAuthenticationWithAccessCodeGrant(false)
//                .enableCsrfSupport(false)
//                .build();
//    }
//
//    @Bean
//    UiConfiguration uiConfig() {
//        //swagger-ui ui配置目前只支持验证url
//        return UiConfigurationBuilder.builder()
//                .deepLinking(true)
//                .displayOperationId(false)
//                .defaultModelsExpandDepth(1)
//                .defaultModelExpandDepth(1)
//                .defaultModelRendering(ModelRendering.EXAMPLE)
//                .displayRequestDuration(false)
//                .docExpansion(DocExpansion.NONE)
//                .filter(false)
//                .maxDisplayedTags(null)
//                .operationsSorter(OperationsSorter.ALPHA)
//                .showExtensions(false)
//                .showCommonExtensions(false)
//                .tagsSorter(TagsSorter.ALPHA)
//                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
//                .validatorUrl(null)
//                .build();
//    }
