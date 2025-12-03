package datastructure1;

/**
 * 哈希冲突处理方法笔记
 * 
 * 在哈希表中，当不同的键通过哈希函数计算后得到相同的存储位置时，
 * 就会发生哈希冲突。这是哈希表设计中必须解决的关键问题。
 * 
 * 下面介绍几种常用的哈希冲突处理方法：
 */

public class 哈希冲突处理方法 {

    /**
     * 1. 链地址法（拉链法）
     * 
     * 原理：将所有哈希地址相同的记录存储在一个链表中。
     * 实现方式：哈希表的每个槽位存储一个链表头指针，所有哈希到该位置的元素都链接在该链表上。
     * 
     * 优点：
     * - 删除节点操作简单
     * - 对于频繁删除操作的场景比较适用
     * - 空间效率较高，只需要存储实际元素数量的空间
     * 
     * 缺点：
     * - 需要额外的空间存储指针
     * - 可能出现大量冲突导致某些链表过长，查找效率下降
     * 
     * 时间复杂度：
     * - 查找、插入、删除平均时间复杂度：O(1+α)，其中α=n/m，n为元素个数，m为哈希表长度
     * - 最坏情况时间复杂度：O(n)
     */
    public void 链地址法() {
        // 示例伪代码结构：
        // class HashTable {
        //     ListNode[] table = new ListNode[size];
        //     
        //     void insert(Key key, Value value) {
        //         int index = hash(key);
        //         ListNode newNode = new ListNode(key, value);
        //         newNode.next = table[index];
        //         table[index] = newNode;
        //     }
        //     
        //     Value search(Key key) {
        //         int index = hash(key);
        //         ListNode current = table[index];
        //         while (current != null) {
        //             if (current.key.equals(key)) {
        //                 return current.value;
        //             }
        //             current = current.next;
        //         }
        //         return null;
        //     }
        // }
    }

    /**
     * 2. 开放地址法（开地址法）
     * 
     * 原理：当发生冲突时，按照某种探测方法在哈希表中寻找下一个空闲位置。
     * 实现方式：所有元素都存储在哈希表数组中，不需要额外的存储空间。
     * 
     * 常见的探测方法包括：
     * 
     * 2.1 线性探测法
     * 公式：Hi=(H(key)+i) MOD m, i=1,2,…,k(k<=m-1)
     * 特点：容易产生"二次聚集"现象，导致性能下降
     * 
     * 2.2 二次探测法
     * 公式：Hi=(H(key)+di) MOD m, di = 1², -1², 2², -2², ..., k², -k² (k≤m/2)
     * 特点：能够缓解线性探测的聚集问题
     * 
     * 2.3 双重哈希法
     * 公式：Hi=(H(key)+i*H2(key)) MOD m, i=1,2,…,k(k<=m-1)
     * H2(key)是另一个哈希函数，且H2(key)不能为0
     * 特点：最有效的开放地址法，但计算相对复杂
     * 
     * 优点：
     * - 不需要额外的存储空间
     * - 所有元素都在哈希表内，缓存友好
     * 
     * 缺点：
     * - 容易产生聚集现象
     * - 删除操作较复杂，需要用特殊标记标识已删除节点
     * - 装填因子不能太大，否则性能急剧下降
     */
    public void 开放地址法() {
        // 示例伪代码结构：
        // class HashTable {
        //     Object[] table = new Object[size];
        //     boolean[] deleted = new boolean[size]; // 标记删除节点
        //     
        //     void insert(Key key, Value value) {
        //         int index = hash(key);
        //         int i = 0;
        //         while (table[index] != null && !deleted[index]) {
        //             i++;
        //             index = (hash(key) + probe(i)) % size; // probe为探测函数
        //         }
        //         table[index] = new Entry(key, value);
        //         deleted[index] = false;
        //     }
        // }
    }

    /**
     * 3. 再哈希法
     * 
     * 原理：构造多个不同的哈希函数，当一个哈希函数产生冲突时，
     * 使用下一个哈希函数计算新的地址，直到不再冲突为止。
     * 
     * 公式：Hi=RHi(key) i=1,2,...,k
     * RHi表示不同的哈希函数
     * 
     * 优点：
     * - 不易产生聚集
     * - 查找速度快
     * 
     * 缺点：
     * - 增加了计算时间
     * - 需要构造多个哈希函数
     */
    public void 再哈希法() {
        // 示例思路：
        // 当hash1(key)冲突时，尝试hash2(key)，再冲突则尝试hash3(key)...
    }

    /**
     * 4. 建立公共溢出区
     * 
     * 原理：将哈希表分为基本表和溢出表两部分：
     * - 基本表：存储原始的哈希地址元素
     * - 溢出表：存储所有发生冲突的元素
     * 
     * 查找过程：
     * 1. 先在基本表中查找，如果发生冲突，则再到溢出表中查找
     * 2. 如果在基本表中没有找到，再到溢出表中查找
     * 
     * 优点：
     * - 处理冲突简单
     * - 适用于冲突较少的情况
     * 
     * 缺点：
     * - 查找时间可能较长
     * - 溢出表可能变得很大
     */
    public void 公共溢出区法() {
        // 示例结构：
        // class HashTable {
        //     Entry[] baseTable;   // 基本表
        //     Entry[] overflowTable; // 溢出表
        // }
    }

    /**
     * 各种方法的比较与适用场景
     * 
     * 1. 链地址法适用场景：
     * - 频繁删除操作
     * - 事先不知道数据量大小
     * - 装填因子较大（>0.75）
     * 
     * 2. 开放地址法适用场景：
     * - 数据量相对固定
     * - 装填因子较小（<0.5）
     * - 对空间要求严格
     * 
     * 3. 性能优化建议：
     * - 选择合适的哈希函数以减少冲突
     * - 控制装填因子，及时扩容
     * - 根据实际使用场景选择合适的冲突解决方法
     * 
     * 装填因子α的定义：
     * α = 表中填入的记录数 / 哈希表的长度
     * α越小，发生冲突的可能性越小，但空间浪费越大
     * α越大，空间利用率越高，但发生冲突的概率越大
     * 一般建议控制α在0.75以下
     */
    
    /**
     * 实际应用示例：
     * 
     * Java中的HashMap采用链地址法处理冲突，在JDK8中当链表长度超过8时，
     * 会转换为红黑树以提高查找效率。
     * 
     * Python中的dict也采用类似的方法处理冲突。
     */
}