package 并行流使用;

import java.util.concurrent.RecursiveTask;

public class ForkjoinDemo {
    //作为并行流的基础，forkjoin框架的基本组成，我们必须好好的唠一下
    //在JUC文件夹中我们已经了解了基本架构和原理，现在我们扒一下这些具体方法的细节和用法
    public static void main(String[] args) {

    }
}
class Task extends RecursiveTask<Integer> {
    private int start;
    private int end;
    public Task(int start, int end) {
        this.start = start;
        this.end = end;
    }
    @Override
    protected Integer compute() {
        if (end - start <= 5) {
            int sum = 0;
            for (int i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else {
            int middle = (start + end) / 2;
            Task left = new Task(start, middle);
            Task right = new Task(middle + 1, end);
            left.fork();
            right.fork();
            return left.join() + right.join();
        }
    }
}
//class IpCountTask extends RecursiveTask<IpCount> {
//    private final List<String> bucketList;
//    private final int start;
//    private final int end;
//
//    public IpCountTask(List<String> bucketList, int start, int end) {
//        this.bucketList = bucketList;
//        this.start = start;
//        this.end = end;
//    }
//
//    @Override
//    protected IpCount compute() {
//        // 如果任务范围较小，直接处理
//        if (end - start <= 1) {
//            String filePath = bucketList.get(start);
//            return IpStatUtil.statSingleBucketFile(filePath);
//        } else {
//            // 否则拆分成两个子任务
//            int mid = (start + end) / 2;
//            IpCountTask leftTask = new IpCountTask(bucketList, start, mid);
//            IpCountTask rightTask = new IpCountTask(bucketList, mid, end);
//
//            // 异步执行左半部分任务
//            leftTask.fork();
//            // 同步执行右半部分任务
//            IpCount rightResult = rightTask.compute();
//            // 等待左半部分任务完成并获取结果
//            IpCount leftResult = leftTask.join();
//
//            // 合并左右两部分的结果
//            return leftResult.getCount() > rightResult.getCount() ? leftResult : rightResult;
//        }
//    }
//}






//不是，你都说先提交到自己的双端队列里面了，那提交完之后join的时候直接拿自己队列里面刚提交的任务呗，如果没被抢走的话
//你这个观察完全命中了 Fork/Join 队列的核心特性—— 线程自己fork()的任务会进自己的双端队列头部，自己拿的时候优先拿头部（LIFO），其他线程只能偷尾部（FIFO），所以大概率能拿到自己刚提交的右任务。但即便如此，双fork()还是不如原写法高效，核心原因是：“从自己队列拿任务执行” ≠ “直接执行任务”，中间多了「入队→出队」的关键开销，哪怕没被抢走，这个开销也存在。
//我用更底层的执行步骤，拆解 “自己队列拿任务” 和 “直接执行” 的差异，帮你彻底理清：
//第一步：先明确 ForkJoinWorkerThread 的任务执行优先级
//线程处理任务的优先级是：
//优先执行当前正在compute()的任务（比如原写法里直接调用rightTask.compute()）；
//若当前无执行中的任务，优先从自己队列的头部（LIFO） 拿自己fork()的任务执行；
//若自己队列为空，才去其他线程队列的尾部（FIFO） 窃取任务。
//你说的 “join 时拿自己队列里刚提交的任务”，对应优先级 2—— 这是对的，但优先级 1（直接执行）比优先级 2 少了关键的「队列操作」。
//第二步：拆解两种写法的执行步骤（聚焦右任务）
//假设线程 A 处理一个大任务，拆分为左、右子任务，且右任务没被其他线程偷走：
//原写法（fork 左 + compute 右）：零队列开销
//        plaintext
//线程A的操作：
//        1. 拆分大任务为左、右子任务；
//        2. leftTask.fork() → 左任务入线程A的队列尾部；
//        3. 调用rightTask.compute() → 直接执行右任务的compute()方法：
//        - 若右任务是最小单元（end-start≤1），直接统计IP；
//        - 若不是，继续拆分右任务（同步执行，仍无队列操作）；
//        4. 执行完右任务，拿到结果；
//        5. leftTask.join() → 若左任务没完成，偷其他任务干，直到左任务完成。
//        👉 右任务的执行：无任何队列入队 / 出队操作，直接调用方法体，CPU 只做业务计算。
//双 fork 写法（fork 左 + fork 右）：有队列开销（哪怕没被抢）
//plaintext
//线程A的操作：
//        1. 拆分大任务为左、右子任务；
//        2. leftTask.fork() → 左任务入线程A的队列尾部；
//        3. rightTask.fork() → 右任务入线程A的队列头部（后fork的在头部）；
//        4. 调用rightTask.join() → 检查右任务状态：
//a. 右任务未完成 → 线程A从自己队列头部拿右任务（因为没被抢）；
//b. 拿任务时：执行「出队操作」（CAS修改队列指针、更新任务状态）；
//        c. 执行右任务的compute()方法（业务计算）；
//d. 完成后，更新右任务的结果状态；
//        5. 拿到右结果，再join左任务。
//        👉 右任务的执行：多了「入队（步骤 3）+ 出队（步骤 4b）」两步核心开销—— 哪怕任务没被抢走，这两步也必须做。
//第三步：关键差异：「队列操作开销」到底是什么？
//你可能觉得 “入队 / 出队就是改个指针，能有多少开销？”，但 Fork/Join 的队列操作是为了线程安全设计的，核心开销包括：
//CAS 原子操作：队列的入队 / 出队用sun.misc.Unsafe的 CAS 实现，虽然比锁快，但比普通内存操作慢 1~2 个数量级（普通赋值是纳秒级，CAS 是十纳秒级）；
//任务状态跟踪：每个 ForkJoinTask 有status字段（未开始、运行中、完成等），入队 / 出队 /join 时都要检查 / 更新这个状态，多了额外的内存读写；
//内存屏障：CAS 操作会触发内存屏障（保证可见性），导致 CPU 缓存失效，增加后续内存访问的开销。
//这些开销单看一次是 “纳秒级”，但在递归拆分的场景下会指数级累积：
//比如拆分 10 层，双 fork 比原写法多做 1023 次 CAS + 状态检查；
//百万级任务拆分时，总额外开销会从 “纳秒级” 变成 “毫秒级”，甚至 “秒级”。
//第四步：补充 “被抢走” 的风险（雪上加霜）
//你假设 “没被抢走”，但高并发下这个假设不成立：
//线程 Afork()两个任务后，可能立刻被 CPU 调度切走；
//此时其他空闲线程（比如线程 B）会扫描线程 A 的队列，从尾部偷走左任务（先 fork 的在尾部）；
//若线程 A 的队列里任务多（比如递归拆了多层），线程 B 甚至可能偷走右任务（极端情况）；
//一旦右任务被偷走，线程 Ajoin()时只能等线程 B 执行完，哪怕 A 偷了其他活，也多了 “跨线程通信 + 结果同步” 的开销。
//总结
//你的核心观察是对的：双 fork 后，线程大概率能拿到自己队列里的右任务，不会傻等其他线程；
//关键差异在开销：哪怕没被抢，双 fork 也多了「入队 + 出队」的 CAS / 状态检查开销，而原写法直接执行，无此开销；
//高并发下风险放大：任务被抢走的概率增加，会进一步增加跨线程调度的开销。
//简单来说：
//原写法：“直接干手边的活，一步到位”；
//双 fork 写法：“先把活放自己口袋，再从口袋里拿出来干”—— 多了 “放口袋 + 拿出来” 的动作，哪怕口袋就在手边，也比直接干慢一点，次数多了差距就明显了。
































//所以说我上面问的这个问题本质上没有错误对吧
//你说得完全没错 —— 从逻辑正确性和核心机制理解的层面，你最初的判断本质上没有错误，我之前的所有分析都是在 “性能优化” 和 “工程实践” 层面补充细节，而非否定你的核心认知。
//先明确：你哪些判断是完全正确的？
//功能可行性：将rightTask.compute()改成rightTask.fork()是功能上可行的，代码能正常运行并得到正确结果，不会出现逻辑错误；
//join 的本质：join()确实不是 “傻等”，等待期间线程会去窃取其他任务执行，而非空耗 CPU—— 这是你对 Fork/Join 核心机制的正确理解；
//队列优先级：双fork()后，当前线程join()时会优先从自己的双端队列头部拿刚提交的右任务（没被抢走的话），而非直接去偷其他线程的任务 —— 这也是你精准命中了 Fork/Join 队列的 LIFO 特性。
//补充：仅有的 “偏差” 是「忽略了性能开销」
//你的判断唯一的 “不完整”，是没有考虑到 “功能可行” 和 “性能最优” 的区别：
//你聚焦的是 “能不能跑、机制对不对”（这部分完全正确）；
//而我补充的是 “跑起来效率高不高、是否符合框架最佳实践”（工程层面的优化点）。
//简单说：你回答了 “能不能做”，而我补充了 “怎么做更好”—— 二者不冲突，你的核心判断没有错误。
//总结
//你对 Fork/Join 的核心机制（fork()/compute()/join()的行为、队列窃取规则）的理解是正确的；
//你提出的 “把compute()改成fork()可行” 的判断，在功能层面完全成立；
//唯一需要补充的是：工程实践中更推荐原写法，因为能避免不必要的队列调度开销，提升性能。
//你的思考方向非常棒 —— 先搞懂 “为什么能跑”，再深究 “怎么跑更快”，这正是理解并发框架的正确路径。