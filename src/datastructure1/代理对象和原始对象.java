package datastructure1;

public class 代理对象和原始对象 {

}
/*在 Spring 中，通过依赖注入（DI）获取的对象是否为代理对象，取决于该对象是否被 Spring AOP 增强（例如是否有 @Transactional、@Cacheable 等注解，或被切面 @Aspect 切入）。如果有 AOP 增强，注入的就是代理对象；否则注入的是原始对象（目标对象）。
一、为什么依赖注入的可能是代理对象？
Spring 的 AOP 增强（包括事务管理）是通过动态代理实现的：
当一个 Bean 被标记为需要 AOP 增强（比如加了 @Transactional），Spring 在初始化该 Bean 时，不会直接返回原始对象，而是会创建一个代理对象（代理类的实例），并将代理对象放入 IoC 容器。后续依赖注入时，容器注入的就是这个代理对象，而非原始对象。

这样做的目的是：代理对象会在调用原始对象的方法前后，自动织入增强逻辑（比如事务的开启 / 提交 / 回滚、日志记录、缓存处理等）。
二、如何判断注入的对象是否是代理对象？
可以通过以下几种方式判断注入的对象是否为代理对象，核心思路是检查对象的类型信息（因为代理对象的类名和类型特征与原始对象不同）。
        1. 查看对象的类名（最直观）
代理对象的类名通常有明显的 “代理标识”，不同代理类型（JDK 动态代理 vs CGLIB 代理）的类名特征不同：

JDK 动态代理：
基于接口的代理，代理类名通常以 $Proxy 开头，后面跟着数字（如 $Proxy123）。
例如：原始对象实现了 UserService 接口，代理类名可能是 com.sun.proxy.$Proxy42。
CGLIB 代理：
基于类继承的代理，代理类名通常以 $$EnhancerBySpringCGLIB$$ 开头，后面跟着随机字符串（如 $$EnhancerBySpringCGLIB$$5f2d1234）。
例如：原始类是 UserService，代理类名可能是 com.example.UserService$$EnhancerBySpringCGLIB$$abc123。
        2. 通过代码打印类名验证
在业务代码中，直接打印注入对象的类名即可观察是否为代理：

java
@Service
public class MyService {
    // 注入一个可能被代理的对象（比如带 @Transactional 的 Service）
    @Autowired
    private UserService userService;

    public void checkProxy() {
        // 打印注入对象的类名
        System.out.println("注入的对象类名：" + userService.getClass().getName());
    }
}

如果 UserService 有 @Transactional 注解（需要 AOP 增强），输出可能是：
注入的对象类名：com.example.UserService$$EnhancerBySpringCGLIB$$5f2d1234（CGLIB 代理）
或 注入的对象类名：com.sun.proxy.$Proxy42（JDK 动态代理，若 UserService 实现了接口）。
如果 UserService 没有任何 AOP 增强，输出就是原始类名：
注入的对象类名：com.example.UserService。
        3. 通过 instanceof 检查代理类型
不同代理类型的父类 / 接口不同，可通过 instanceof 判断：

JDK 动态代理：代理对象是 java.lang.reflect.Proxy 类的实例，且实现了原始对象的接口。
java
// 判断是否是 JDK 动态代理对象
boolean isJdkProxy = userService instanceof Proxy;

CGLIB 代理：代理对象是原始类的子类，且类名包含 EnhancerBySpringCGLIB。
可通过判断父类是否为原始类，或类名是否包含 CGLIB 标识：
java
// 判断是否是 CGLIB 代理对象（父类是原始类）
boolean isCglibProxy = userService.getClass().getSuperclass() == UserService.class;
// 或通过类名关键字判断
boolean isCglibProxy2 = userService.getClass().getName().contains("EnhancerBySpringCGLIB");

4. 通过 Spring 提供的工具类判断
Spring 提供了 AopUtils 工具类，可直接判断对象是否为代理对象：

java
import org.springframework.aop.support.AopUtils;

// 判断是否是 Spring 代理对象（无论 JDK 还是 CGLIB）
boolean isAopProxy = AopUtils.isAopProxy(userService);
// 进一步判断是 JDK 还是 CGLIB 代理
boolean isJdkProxy = AopUtils.isJdkDynamicProxy(userService);
boolean isCglibProxy = AopUtils.isCglibProxy(userService);


三、总结
何时注入的是代理对象？
当 Bean 被 Spring AOP 增强时（例如加了 @Transactional、@Cacheable，或被 @Aspect 切面切入），IoC 容器注入的是代理对象；否则注入原始对象。
判断方法：
打印类名，观察是否有 $Proxy（JDK 代理）或 $$EnhancerBySpringCGLIB$$（CGLIB 代理）前缀；
使用 AopUtils.isAopProxy(bean) 直接判断是否为代理对象；
通过 instanceof Proxy 或父类检查区分代理类型。

通过这些方法，就能明确依赖注入的对象是否是代理对象，进而理解为何事务等 AOP 逻辑能生效（因为代理对象会织入增强逻辑）。*/

