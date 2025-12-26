package datastructure1;

/**
 * HashMap中哈希值计算方法详解
 * 
 * 在Java的HashMap中，为了提高哈希值的分布均匀性，减少哈希冲突，
 * 采用了特殊的哈希值计算方法。
 */
public class HashMap哈希值计算 {

    /**
     * HashMap中的hash方法
     * 
     * 在JDK 8的HashMap实现中，hash值的计算方法如下：
     * static final int hash(Object key) {
     *     int h;
     *     return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
     * }
     * 
     * 这个方法的作用是：
     * 1. 如果key为null，返回0（HashMap允许null键）
     * 2. 如果key不为null，先获取key的hashCode值，然后将该值的高16位与低16位进行异或运算
     * 
     * 为什么要这样做？
     * 
     * 1. 扰动函数的作用：
     *    在HashMap中，数组的长度总是2的幂次方。计算元素在数组中的位置时，
     *    使用的是 hash & (length-1) 运算，这等价于 hash % length，但效率更高。
     *    
     *    但是，如果直接使用hashCode值，由于大多数情况下length比较小，
     *    只有hashCode的低位参与了运算，高位信息被忽略了，这可能导致较多的哈希冲突。
     *    
     *    通过将hashCode的高16位与低16位进行异或运算，可以让高位的信息也影响到低位，
     *    从而提高哈希值的随机性和分布均匀性，减少哈希冲突。
     * 
     * 2. 举例说明：
     *    假设有一个hashCode值为：1111 1111 1111 1111 1010 1010 0101 0101
     *    无符号右移16位后为：      0000 0000 0000 0000 1111 1111 1111 1111
     *    两者异或运算结果为：      1111 1111 1111 1111 0101 0101 1010 1010
     *    
     *    可以看到，高位的信息已经混合到了整个32位中，这样在后续的 & (length-1) 运算中，
     *    更多的位都会参与计算，提高了分布的均匀性。
     */
    public static int hash(Object key) {
        int h;
        // key为null时返回0
        // key不为null时，获取其hashCode，然后与hashCode无符号右移16位后的值进行异或运算
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * 数组索引计算方法
     * 
     * 在HashMap中，计算元素在数组中的位置使用以下方法：
     * static int indexFor(int h, int length) {
     *     return h & (length-1);
     * }
     * 
     * 这个方法等价于 h % length，但效率更高。
     * 前提是length必须是2的幂次方，这样 length-1 的二进制表示才会是低位全为1的形式。
     * 
     * 例如：
     * length = 16 (二进制: 10000)
     * length-1 = 15 (二进制: 01111)
     * 
     * 任何数与01111进行按位与运算，结果都在0-15之间，相当于取模运算。
     */
    public static int indexFor(int h, int length) {
        return h & (length - 1);
    }

    /**
     * 实际应用示例
     */
    public static void demonstrateHashMapHash() {
        // 示例键值
        String[] keys = {"apple", "banana", "orange", "grape", "watermelon"};
        
        System.out.println("HashMap哈希值计算示例：");
        System.out.println("Key\t\tOriginal HashCode\tHashMap Hash\tIndex(for length=16)");
        System.out.println("-----------------------------------------------------------------------");
        
        for (String key : keys) {
            int originalHashCode = key.hashCode();
            int hashMapHash = hash(key);
            int index = indexFor(hashMapHash, 16);
            
            System.out.printf("%-12s\t%d\t\t%d\t\t%d\n", key, originalHashCode, hashMapHash, index);
        }
        
        // 展示扰动函数的效果
        System.out.println("\n扰动函数效果展示：");
        System.out.println("Key\t\tOriginal HashCode\tBinary Representation\t\t\tHashMap Hash\tBinary Representation");
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
        
        String key = "example";
        int originalHashCode = key.hashCode();
        int hashMapHash = hash(key);
        
        System.out.printf("%-12s\t%d\t\t%s\t%d\t\t%s\n", 
                key, 
                originalHashCode, 
                String.format("%32s", Integer.toBinaryString(originalHashCode)).replace(' ', '0'),
                hashMapHash,
                String.format("%32s", Integer.toBinaryString(hashMapHash)).replace(' ', '0'));
    }

    /**
     * 为什么使用异或而不是其他位运算？
     * 
     * 1. 异或运算的特性：
     *    - 相同为0，不同为1
     *    - 既不会丢失信息，又能混合高低位信息
     *    - 计算简单，效率高
     * 
     * 2. 为什么不使用与(&)或或(|)运算？
     *    - 与运算可能会使结果趋向于0
     *    - 或运算可能会使结果趋向于1
     *    - 都不如异或运算均匀
     * 
     * 3. 为什么是右移16位？
     *    - int类型是32位，右移16位正好是中间位置
     *    - 能够较好地混合高低位信息
     *    - 对于大多数情况都能取得不错的效果
     */

    /**
     * HashMap在JDK 8中的优化
     * 
     * 1. 链表转红黑树：
     *    当链表长度超过8且数组长度大于等于64时，链表会转换为红黑树
     *    当红黑树节点数小于等于6时，又会转回链表
     *    
     * 2. 扰动函数的简化：
     *    JDK 8中的扰动函数只做一次异或运算，相比JDK 7的4次运算更加简洁高效
     *    JDK 7中的扰动函数：
     *    static int hash(int h) {
     *        h ^= (h >>> 20) ^ (h >>> 12);
     *        return h ^ (h >>> 7) ^ (h >>> 4);
     *    }
     */

    public static void main(String[] args) {
        demonstrateHashMapHash();
        
        // 展示null键的处理
        System.out.println("\nnull键的处理：");
        System.out.println("hash(null) = " + hash(null)); // 输出0
    }
}




//jdk 1.7循环链表的产生



/*
循环链表的完整形成过程（以你说的 A->B 为例）
假设初始状态：原数组某桶的链表是 A -> B（A 是头节点，B 是尾节点），此时线程 1 和线程 2 同时执行 put 操作，触发扩容：
步骤 1：线程 1 执行 resize，先处理节点 A、B
线程 1 开始迁移 A->B 到新数组，头插法的逻辑是：
先取节点 A，插入新桶的头部 → 新桶：A
再取节点 B，插入新桶的头部 → 新桶：B -> A（此时线程 1 可能因 CPU 时间片耗尽被挂起）。
步骤 2：线程 2 抢占 CPU，继续处理同一个链表
线程 2 看到的链表指针是被线程 1 改到一半的状态（B 的 next 指向 A，A 的 next 还是 B？不，这里是关键细节）：
线程 2 从头遍历原链表（但此时指针已被线程 1 篡改），先处理节点 B：
取出 B，将 B 的 next 指向新桶的「当前头节点」（此时新桶为空，B.next = null）。
把 B 插入新桶头部 → 新桶：B。
线程 2 继续处理节点 A：
取出 A，此时 A 的 next 还是指向原链表的 B（未被线程 1 完全修改）。
头插法：将 A 的 next 指向新桶的头节点 B → A.next = B。
把 A 插入新桶头部 → 新桶：A -> B。
此时线程 1 恢复执行，它之前处理到 B 的 next 指向 A，现在继续处理 A：
线程 1 会把 A 的 next 指向「当前新桶的头节点 B」（而 B 的 next 已经被线程 2 改成 A）。
步骤 3：循环链表形成
最终新桶的链表变成：A -> B -> A（A 的 next 是 B，B 的 next 是 A），形成闭环。
*/






//HashSet

/*
一、核心结论：HashSet 是 HashMap 的「包装器」
HashSet 本身没有独立的哈希实现逻辑，它完全基于 HashMap 实现 —— 可以把 HashSet 理解为「只使用 HashMap 的 key 部分，value 用一个固定常量占位」的特殊容器。
先明确术语对应：
        「哈希表」通常指 Java 中的 HashMap（哈希映射），是键值对（Key-Value） 结构；
        「哈希 Set」即 HashSet（哈希集合），是无重复元素的无序集合，本质是借用 HashMap 的「key 唯一性」来实现集合的去重特性。
二、HashSet 的底层实现细节（基于 JDK 8+）
        1. 核心成员变量：依赖 HashMap 存储
HashSet 源码中只定义了两个关键成员变量，全部围绕 HashMap：
java
        运行
public class HashSet<E> extends AbstractSet<E> implements Set<E>, Cloneable, java.io.Serializable {
    // 核心存储容器：HashSet 底层就是这个 HashMap
    private transient HashMap<E, Object> map;

    // 所有元素对应的固定 Value：因为 HashSet 只需要 key，value 用一个常量占位即可
    private static final Object PRESENT = new Object();

    // 空构造器：直接初始化内部的 HashMap
    public HashSet() {
        map = new HashMap<>();
    }
}
map：HashSet 的所有元素，最终都作为 HashMap 的 key 存储；
PRESENT：一个固定的空对象，作为 HashMap 的 value（所有元素共享这个值，节省内存）。
        2. 核心方法：完全委托给 HashMap
HashSet 的所有核心操作（添加、删除、查询），本质都是调用 HashMap 的对应方法，仅暴露「集合视角」的接口：
HashSet 方法	底层调用的 HashMap 方法	逻辑说明
add(E e)	map.put(e, PRESENT)	把元素 e 作为 HashMap 的 key，PRESENT 作为 value；
若 key 已存在（元素重复），put 返回旧值，add 则返回 false；
若 key 不存在，add 返回 true。
contains(Object o)	map.containsKey(o)	判断元素 o 是否作为 HashMap 的 key 存在。
remove(Object o)	map.remove(o)	删除 HashMap 中 key 为 o 的键值对，返回是否删除成功（即是否存在该元素）。
size()	map.size()	返回 HashMap 中 key 的数量（即集合元素个数）。
clear()	map.clear()	清空 HashMap，集合变为空。
示例：add 方法源码
java
        运行
public boolean add(E e) {
    // 核心逻辑：调用 HashMap.put，利用 key 唯一性实现去重
    return map.put(e, PRESENT) == null;
}
当元素首次添加：map.put 返回 null，add 返回 true（添加成功）；
当元素重复添加：map.put 返回旧值（PRESENT），add 返回 false（添加失败，保证集合无重复）。
        3. 迭代器 / 遍历：复用 HashMap 的 key 遍历
HashSet 的 iterator() 方法直接返回 HashMap.keySet().iterator()，即遍历 HashMap 的所有 key—— 这就是 HashSet 遍历元素的本质：
java
        运行
public Iterator<E> iterator() {
    return map.keySet().iterator();
}
三、HashSet 特性：完全继承 HashMap 的特性
因为底层是 HashMap，HashSet 的核心特性也和 HashMap 完全一致：
无序性：元素存储顺序不保证和插入顺序一致（依赖 key 的哈希值映射到数组下标）；
去重原理：依赖元素的 hashCode() 和 equals() 方法 ——HashMap 判断 key 重复的规则是：hashCode 相同且 equals 返回 true，因此 HashSet 去重也遵循这个规则；
        null 值支持：允许存储一个 null 元素（因为 HashMap 允许 key 为 null）；
线程不安全：和 HashMap 一样，多线程并发修改（如 add/remove）会导致数据异常（JDK 1.8 后不会出现循环链表，但可能丢失元素）；
扩容机制：HashSet 本身没有扩容逻辑，完全依赖内部 HashMap 的扩容（容量 × 负载因子触发扩容，默认初始容量 16，负载因子 0.75）。*/
