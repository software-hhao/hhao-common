# HHAO扩展点模块

基于Spring Boot的扩展点实现。

几个重要的类：

BizScenario：扩展点的业务座标，从特殊到一般进行匹配。
主要属性：bizId用于描述领域+[子域]，userCase用于描述用例，scenario用于描述场景。

@Extension：用于注解扩展点类。
主要属性：bizId用于描述领域+[子域]，userCase用于描述用例，scenario用于描述场景。

ExtensionPoint：扩展点接口，所有扩展点类应继承自该接口。

DefaultExtensionExecutor：默认的执行器。

SimpleReturn:单值返回
CombinedReturn:组合返回结果值

# 关于执行器DefaultExtensionExecutor

执行器匹配扩展点类先按扩展点座标BizScenario与@Extension进行匹配，再按扩展点类的support方法进行匹配。
BizScenario匹配顺序从特殊到一般，顺序如下：
bizId.useCase.scenario
bizId.useCase
bizId
执行器提供如下执行方法：
单一扩展器代理执行接口，带返回值
public <R, C> R execute(Class<? extends ExtensionPoint<R,C>> targetClz, BizScenario bizScenario, C context)；
public <R, C> R execute(ExtensionCoordinate extensionCoordinate, C context)；
单一扩展器代理执行接口，无返回值
public <C> void executeVoid(Class<? extends ExtensionPoint<Void,C>> targetClz,BizScenario bizScenario, C context) ;
public <C> void executeVoid(ExtensionCoordinate extensionCoordinate,BizScenario bizScenario, C context)；
单一扩展器函数回调执行接口，带返回值
public <R, T extends ExtensionPoint> R callback(Class<T> targetClz, BizScenario bizScenario, Function<T, R> exeFunction) ;
public <R, T extends ExtensionPoint> R callback(ExtensionCoordinate extensionCoordinate, Function<T, R> exeFunction);
单一扩展器函数回调执行接口，不带返回值
public <T extends ExtensionPoint> void callbackVoid(Class<T> targetClz, BizScenario bizScenario, Consumer<T> exeFunction) ;
public <T extends ExtensionPoint> void callbackVoid(ExtensionCoordinate extensionCoordinate, Consumer<T> exeFunction);

组合扩展器代理执行接口，带返回值
public <R, C> List<R> multiExecute(ExtensionCoordinate extensionCoordinate, C context);
public <R, C> List<R> multiExecute(Class<? extends ExtensionPoint<R,C>> targetClz,BizScenario bizScenario, C context)；
public <R, C> List<R> multiExecute(ExtensionCoordinate extensionCoordinate, C context, InterruptionStrategy<R> interruptionStrategy);
public <R, C> List<R> multiExecute(Class<? extends ExtensionPoint<R,C>> targetClz,BizScenario bizScenario, C context, InterruptionStrategy<R> interruptionStrategy);
组合扩展器代理执行接口，不带返回值
public <C> void multiExecuteVoid(ExtensionCoordinate extensionCoordinate, C context) ;
public <C> void multiExecuteVoid(Class<? extends ExtensionPoint<Void,C>> targetClz,BizScenario bizScenario, C context) ;
组合扩展器函数回调执行接口，带返回值
public <R, T extends ExtensionPoint> R multiCallback(Class<T> targetClz, BizScenario bizScenario, Function<List<T>, R> exeFunction);
public <R, T extends ExtensionPoint> R multiCallback(ExtensionCoordinate extensionCoordinate, Function<List<T>, R> exeFunction);
组合扩展器函数回调执行接口，不带返回值
public <T extends ExtensionPoint> void multiCallbackVoid(Class<T> targetClz, BizScenario bizScenario, Consumer<List<T>> exeFunction) ;
public <T extends ExtensionPoint> void multiCallbackVoid(ExtensionCoordinate extensionCoordinate, Consumer<List<T>> exeFunction) ;

以上接口参数：
Class<? extends ExtensionPoint<Void,C>> targetClz：扩展点类型。
BizScenario bizScenario：客户端传入的扩展点座标。
C context：上下文对象。
InterruptionStrategy：在组合代理执行器中,每个扩展点执行完毕后，验证是否继续执行的规则。

# 使用方法示例

1. 导入依赖

   ```
           <dependency>
               <groupId>io.github.software-hhao</groupId>
               <artifactId>hhao-extension-spring-boot-starter</artifactId>
           </dependency>
   ```
2. 服务端定义扩展点接口：

   ```
   public interface DoExtension extends ExtensionPoint<String,String> {

   }
   ```
3. 实现两个扩展点：

   ```
   @Extension(bizId = "test",useCase = "say")
   public class MyDoExtension implements DoExtension {
       @Override
       public String execute(String context) {
           return "my do:" + context;
       }

       @Override
       public int getOrder() {
           return 0;
       }
   }
   ```
   ```
   @Extension(bizId = "test",useCase = "say")
   public class SheDoExtension implements DoExtension{
       @Override
       public String execute(String context) {
           return "she do:" + context;
       }

       @Override
       public int getOrder() {
           return 1;
       }
   }
   ```
4. 扩展点调用

   调用单一扩展器代理执行接口：

   ```
           String str1 = executor.execute(DoExtension.class, BizScenario.valueOf("test", "say"), "wang");
           System.out.println(str1);
   ```
   输出：my do:wang

   ---

   调用组合扩展器代理执行接口：

   ```
    List<String> results1 = executor.multiExecute(DoExtension.class, BizScenario.valueOf("test", "say"), "wang");
           System.out.println(results1);
   ```
   输出：[my do:wang, she do:wang]

   ---

   调用单一扩展器函数回调执行接口：

   ```
           String str2 = executor.callback(DoExtension.class, BizScenario.valueOf("test", "say"), ext -> {
               return ext.execute("call back");
           });
           System.out.println(str2);
   ```
   输出：my do:call back

   ---

   调用组合扩展器函数回调执行接口：

   ```
         List<String> results2 = executor.multiCallback(DoExtension.class, BizScenario.valueOf("test", "say"), exps -> {
               List<String> combinationResult = new ArrayList<>(exps.size());
               String result = null;
               for (ExtensionPoint exp : exps) {
                   try {
                       result = (String) exp.execute("good");
                       combinationResult.add(result);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
               return combinationResult;
           });

           System.out.println(results2);
   ```
   输出：[my do:good, she do:good]
