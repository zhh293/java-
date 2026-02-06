//package 分而治之.util;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ForkJoinPool;
//import java.util.concurrent.RecursiveTask;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//public class IpStatUtil {
//    public static IpCount statSingleBucketFile(String bucketFilePath) {
//        // 校验文件路径
//        if (bucketFilePath == null || new File(bucketFilePath).exists() == false) {
//            return new IpCount(IpStatConstant.EMPTY_FILE, 0L);
//        }
//
//        // 本地哈希表：键=IP，值=出现次数（核心计数，100MB文件最多几千万键值对，1GB内存足够）
//        Map<String, Long> ipCountMap = new HashMap<>(16); // 初始容量16，自动扩容
//
//        // 按行读取文件（缓冲流，逐行加载，不占内存）
//        try (BufferedReader br = new BufferedReader(
//                new FileReader(bucketFilePath, java.nio.charset.StandardCharsets.forName(IpStatConstant.CHARSET)),
//                IpStatConstant.BUFFER_SIZE
//        )) {
//            String ip;
//            while ((ip = br.readLine()) != null) {
//                if (ip.isBlank()) {
//                    continue;
//                }
//                // 哈希表计数：存在则+1，不存在则初始化为1（JDK8+简化写法）
//                ipCountMap.put(ip, ipCountMap.getOrDefault(ip, 0L) + 1L);
//            }
//        } catch (Exception e) {
//            System.err.println("统计单桶文件失败：" + bucketFilePath + "，原因：" + e.getMessage());
//            return new IpCount(IpStatConstant.INVALID_IP, 0L);
//        }
//
//        // 处理空文件（哈希表无数据）
//        if (ipCountMap.isEmpty()) {
//            return new IpCount(IpStatConstant.EMPTY_FILE, 0L);
//        }
//
//        // 遍历哈希表，找桶内出现次数最多的IP（利用IpCount的排序特性）
//        return ipCountMap.entrySet().stream()
//                .map(entry -> new IpCount(entry.getKey(), entry.getValue()))
//                .max(Comparator.naturalOrder())
//                .orElse(new IpCount(IpStatConstant.EMPTY_FILE, 0L));
//
//    }
//
//    public static IpCount getTop(List<String>bucketList){
//        // 校验文件列表
//        if(bucketList==null||bucketList.isEmpty()){
//            System.out.println("文件列表为空");
//            return null;
//        }
////        bucketList.parallelStream()
////                .map(bucketFilePath -> IpStatUtil.statSingleBucketFile(bucketFilePath))
////                .max(Comparator.naturalOrder())
////                .orElse(new IpCount(IpStatConstant.EMPTY_FILE, 0L));
//
//
//        // 使用 ForkJoinPool 来并行处理文件
//        ForkJoinPool forkJoinPool = new ForkJoinPool();
//        try {
//            // 提交任务并获取最终结果
//            IpCount result = forkJoinPool.invoke(new IpCountTask(bucketList, 0, bucketList.size()));
//            return result;
//        } finally {
//            forkJoinPool.shutdown(); // 关闭线程池
//        }
//
//
//
//
//
//
//
//
//
//        //转换成map
////        Map<String,Long> ipCountMap = ipCountStream.collect(Collectors.toMap(IpCount::getIp, IpCount::getCount));
//
//    }
//}
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
//class IpCount implements Comparable<IpCount> {
//    // IP地址（含无效IP/空文件标记）
//    private String ip;
//    // 出现次数（用Long避免int溢出，100GB文件IP次数可能超2^31-1）
//    private long count;
//
//    // 无参构造（反射/序列化用）
//    public IpCount() {}
//
//    // 有参构造（核心使用）
//    public IpCount(String ip, long count) {
//        this.ip = ip;
//        this.count = count;
//    }
//
//    // 按出现次数降序排序，次数相同按IP升序（不影响核心结果）
//    @Override
//    public int compareTo(IpCount o) {
//        int countCompare = Long.compare(o.count, this.count);
//        return countCompare != 0 ? countCompare : this.ip.compareTo(o.ip);
//    }
//
//    // Getter & Setter
//    public String getIp() {
//        return ip;
//    }
//
//    public void setIp(String ip) {
//        this.ip = ip;
//    }
//
//    public long getCount() {
//        return count;
//    }
//
//    public void setCount(long count) {
//        this.count = count;
//    }
//
//    // 重写toString，方便打印结果
//    @Override
//    public String toString() {
//        return "IpCount{" +
//                "ip='" + ip + '\'' +
//                ", count=" + count +
//                '}';
//    }
//}
//
