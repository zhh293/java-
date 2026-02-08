package 并行流使用;

import java.util.List;

public class 并行流Demo {
    public static void main(String[] args) {

    }
}



//
//一、并行流核心概念
//并行流是 Java 8 Stream API 提供的并行处理能力，底层基于ForkJoinPool.commonPool()（默认公共线程池），将流操作拆分成多个子任务，在多个线程上并行执行，最终合并结果。
//核心优势：无需手动编写 Fork/Join 代码，一行代码即可实现并行计算；
//核心风险：滥用会导致线程安全问题、性能下降甚至程序错误。
//二、并行流的基础使用方法
//1. 并行流的创建
//有两种核心创建方式，效果完全一致：
//java
//        运行
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Stream;
//
//public class ParallelStreamDemo {
//    public static void main(String[] args) {
//        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
//
//        // 方式1：直接创建并行流（推荐）
//        Stream<Integer> parallelStream1 = list.parallelStream();
//
//        // 方式2：将串行流转为并行流
//        Stream<Integer> parallelStream2 = list.stream().parallel();
//    }
//}
//2. 基本使用示例（统计 / 过滤 / 映射）
//以 “统计列表中偶数的平方和” 为例，对比串行与并行：
//java
//        运行
//import java.util.Arrays;
//import java.util.List;
//
//public class ParallelStreamBasic {
//    public static void main(String[] args) {
//        List<Integer> dataList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//
//        // 串行流实现
//        int serialSum = dataList.stream()
//                .filter(n -> n % 2 == 0)  // 过滤偶数
//                .mapToInt(n -> n * n)     // 平方（用mapToInt避免装箱）
//                .sum();                   // 求和
//        System.out.println("串行结果：" + serialSum); // 输出：4+16+36+64+100=220
//
//        // 并行流实现（仅改parallelStream()）
//        int parallelSum = dataList.parallelStream()
//                .filter(n -> n % 2 == 0)
//                .mapToInt(n -> n * n)
//                .sum();
//        System.out.println("并行结果：" + parallelSum); // 输出：220
//    }
//}












//
//
//三、并行流的关键细节（避坑重点）
//并行流的坑大多源于对 “并行” 特性的忽视，以下是必须掌握的细节：
//        1. 线程安全问题（最核心的坑）
//禁止在并行流中修改非线程安全的外部集合 / 变量（比如ArrayList、普通int变量）；
//错误示例（线程不安全）：
//java
//        运行
//List<Integer> result = new ArrayList<>(); // 非线程安全
//dataList.parallelStream()
//        .filter(n -> n % 2 == 0)
//        .forEach(result::add); // 多线程同时add，会导致元素丢失/数组越界
//正确做法：
//使用线程安全的集合（如CopyOnWriteArrayList）；
//优先使用collect()而非forEach()（collect()是为并行流设计的，底层会处理线程安全）：
//java
//        运行
//// 正确：collect自动处理线程安全，合并结果
//List<Integer> safeResult = dataList.parallelStream()
//        .filter(n -> n % 2 == 0)
//        .collect(Collectors.toList());
//2. 顺序性问题
//并行流默认不保证元素的处理顺序（为了性能）；
//如果需要保持顺序，需显式调用ordered()（但会损失部分并行性能）：
//java
//        运行
//// 保持处理顺序（比如按列表原有顺序输出）
//dataList.parallelStream()
//        .ordered()
//        .forEach(n -> System.out.println(n));
//        3. 避免 “副作用”
//副作用：流操作中修改外部状态（比如修改变量、打印日志、调用非纯函数）；
//并行流中副作用会导致不可预期的结果，尽量用map/reduce/collect替代：
//java
//        运行
//// 错误（有副作用：修改外部变量）
//int sum = 0;
//dataList.parallelStream().forEach(n -> sum += n); // sum最终结果错误
//
//// 正确（无副作用：纯函数式操作）
//int correctSum = dataList.parallelStream().mapToInt(Integer::intValue).sum();
//4. 原始类型流优化
//避免使用Stream<Integer>，优先使用IntStream/LongStream/DoubleStream；
//原因：减少自动装箱 / 拆箱的开销（并行场景下，装箱开销会被放大）：
//java
//        运行
//// 低效：有装箱拆箱
//long count1 = dataList.parallelStream()
//        .filter(n -> n > 5)
//        .count();
//
//// 高效：无装箱拆箱
//long count2 = dataList.parallelStream()
//        .mapToInt(Integer::intValue)
//        .filter(n -> n > 5)
//        .count();









//
//
//四、并行流的优化技巧
//并行流不是 “用了就快”，需结合场景优化，否则可能比串行流还慢：
//        1. 数据量阈值：小数据量别用并行流
//并行流有任务拆分 / 调度 / 合并的开销，只有当数据量足够大（比如 10 万 + 元素）时，并行的收益才会超过开销；
//经验阈值：集合元素少于 1000 时，优先用串行流；超过 1 万时，并行流优势明显（可根据 CPU 核心数调整）。
//        2. 自定义 ForkJoinPool（避免占用公共池）
//        并行流默认使用ForkJoinPool.commonPool()，该线程池是全局共享的（比如其他并行流、CompletableFuture 也会用）；
//如果并行流处理耗时任务，会阻塞其他任务，此时需自定义线程池：
//java
//        运行
//import java.util.concurrent.ForkJoinPool;
//import java.util.stream.Collectors;
//
//public class CustomForkJoinPoolDemo {
//    public static void main(String[] args) {
//        List<Integer> dataList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//
//        // 自定义线程池（核心数=CPU核心数）
//        ForkJoinPool customPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
//
//        // 用自定义线程池执行并行流
//        List<Integer> result = customPool.submit(() ->
//                dataList.parallelStream()
//                        .filter(n -> n % 2 == 0)
//                        .collect(Collectors.toList())
//        ).join();
//
//        customPool.shutdown(); // 用完关闭线程池
//        System.out.println(result); // 输出：[2, 4, 6, 8, 10]
//    }
//}
//3. 减少中间操作，简化任务拆分
//并行流会为每个中间操作拆分任务，中间操作越多，调度开销越大；
//优化：合并冗余的中间操作（比如把多个map合并为一个）：
//java
//        运行
//// 低效：多个中间操作，拆分多次任务
//dataList.parallelStream()
//        .map(n -> n * 2)
//        .map(n -> n + 1)
//        .map(n -> n / 2)
//        .collect(Collectors.toList());
//
//// 高效：合并为一个map，减少任务拆分
//        dataList.parallelStream()
//        .map(n -> (n * 2 + 1) / 2)
//        .collect(Collectors.toList());
//        4. 不需要顺序时，用unordered()减少开销
//如果业务不关心处理顺序，调用unordered()可以让并行流更高效（避免维护顺序的开销）：
//java
//        运行
//// 无需顺序，优化并行性能
//long count = dataList.parallelStream()
//        .unordered()
//        .filter(n -> n > 3)
//        .count();
//5. 避免在并行流中做耗时 IO 操作
//并行流适合CPU 密集型任务（如计算、过滤、映射）；
//如果流操作中有耗时 IO（比如读文件、调接口），优先用CompletableFuture+ 线程池，而非并行流（IO 阻塞会占满 commonPool）。
//五、并行流与其他流方法的结合技巧
//并行流可以和 Stream 的所有中间 / 终止操作结合，但需注意 “哪些操作适合并行，哪些需要规避”：
//        1. 与中间操作的结合
//        表格
//流方法	并行流结合注意事项
//filter()	完全兼容，无额外开销，是并行流的常用操作
//map()	优先用mapToInt/Long/Double（避免装箱），单个map比多个map高效
//flatMap()	兼容，但拆分后的子流会增加任务数，数据量大时需评估性能
//sorted()	并行流中sorted()开销极大（需全局排序），尽量避免；若必须排序，先串行排序再并行处理
//distinct()	并行流中需要全局去重，开销大，小数据量建议串行
//示例：高效结合 filter+map+limit
//        java
//运行
//// 并行流结合多中间操作：找大于5的偶数，平方后取前3个
//List<Integer> result = dataList.parallelStream()
//        .filter(n -> n > 5)       // 过滤
//        .filter(n -> n % 2 == 0)  // 过滤偶数
//        .map(n -> n * n)          // 平方
//        .limit(3)                 // 取前3个（并行流中limit会尽早终止任务）
//        .collect(Collectors.toList());
//System.out.println(result); // 输出：[36, 64, 100]
//2. 与终止操作的结合
//        表格
//终止操作	并行流结合注意事项
//collect()	优先使用！底层自动处理线程安全，推荐用Collectors.toList()/toMap()等
//reduce()	必须满足结合律（比如加法、乘法），否则并行结果错误；推荐用带初始值的重载方法
//forEach()	非线程安全，尽量用forEachOrdered()（保持顺序）或collect()替代
//sum()/count()	原始类型流（IntStream）的sum/count是并行流的最优终止操作，无线程安全问题
//示例 1：并行流 + reduce（满足结合律）
//java
//        运行
//// 并行流reduce求和（加法满足结合律，结果正确）
//int sum = dataList.parallelStream()
//        .reduce(0, (a, b) -> a + b); // 初始值0，累加器
//System.out.println(sum); // 输出：55
//示例 2：并行流 + collect（分组统计）
//java
//        运行
//// 并行流分组统计：按奇偶分组，计算每组的和
//import java.util.Map;
//import java.util.stream.Collectors;
//
//Map<String, Integer> groupSum = dataList.parallelStream()
//        .collect(Collectors.groupingBy(
//                n -> n % 2 == 0 ? "偶数" : "奇数", // 分组条件
//                Collectors.summingInt(Integer::intValue) // 组内求和
//        ));
//System.out.println(groupSum); // 输出：{奇数=25, 偶数=30}
//总结
//核心使用：并行流通过parallelStream()/parallel()创建，底层是 Fork/Join，适合 CPU 密集型、大数据量场景；
//关键避坑：禁止修改非线程安全的外部变量，避免副作用，注意顺序性问题；
//优化技巧：用原始类型流、自定义线程池、合并中间操作，小数据量用串行流；
//结合原则：与filter/map/collect/reduce结合高效，与sorted/distinct结合需谨慎，优先用collect替代forEach。

//你就把那个parallelStream方法当作一个性能提高器，对你的数据业务逻辑操作没什么影响，只不过是把这一个任务让好几个线程一起完成罢了。。

//当作开了一个外挂就行，OK


