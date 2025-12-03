package datastructure1;

import java.util.*;

/**
 * 哈希表桶实现详解
 * 
 * 详细解释哈希表中桶(bucket)的实现方式，特别是使用Java HashMap类似的哈希函数时的情况
 */
public class 哈希表桶实现详解 {
    
    /**
     * 模拟哈希表中的桶结构，使用更接近Java HashMap的实现方式
     * 
     * 在哈希表中：
     * 1. 桶是连续的数组结构
     * 2. 通过哈希函数计算得到的值用于确定元素应放入哪个桶中
     * 3. 每个桶中可以存储多个元素(当发生哈希冲突时)，使用拉链法解决冲突
     */
    static class HashMapWithBuckets<K, V> {
        // 桶数组 - 每个元素就是一个桶，是连续的数组结构
        private Bucket<K, V>[] buckets;
        private int capacity;
        
        public HashMapWithBuckets(int capacity) {
            // HashMap中的数组长度通常是2的幂次方，这里我们也采用这种设计
            this.capacity = powerOfTwoCapacity(capacity);
            // 初始化桶数组，所有桶初始为空
            this.buckets = new Bucket[this.capacity];
        }
        
        /**
         * 确保容量是2的幂次方
         * @param cap 初始容量
         * @return 不小于cap的最小2的幂次方
         */
        private int powerOfTwoCapacity(int cap) {
            int n = cap - 1;
            n |= n >>> 1;
            n |= n >>> 2;
            n |= n >>> 4;
            n |= n >>> 8;
            n |= n >>> 16;
            return (n < 0) ? 1 : (n >= 1 << 30) ? 1 << 30 : n + 1;
        }
        
        /**
         * 使用类似Java HashMap的哈希函数 - 计算键应该放在哪个桶中
         * @param key 键
         * @return 桶的索引位置
         */
        private int hash(K key) {
            if (key == null) return 0;
            // 使用HashMap中的扰动函数，让哈希值的高位和低位都参与运算
            int h = key.hashCode();
            int hashMapHash = h ^ (h >>> 16);
            // 计算桶索引，使用位运算替代取模运算提高效率
            return hashMapHash & (capacity - 1);
        }
        
        /**
         * 添加键值对
         * @param key 键
         * @param value 值
         */
        public void put(K key, V value) {
            // 1. 通过哈希函数计算桶的索引位置(桶地址)
            int bucketIndex = hash(key);
            System.out.println("键 '" + key + "' 通过哈希函数计算得到桶索引: " + bucketIndex);
            
            // 2. 如果该桶还未初始化，则创建新桶
            if (buckets[bucketIndex] == null) {
                buckets[bucketIndex] = new Bucket<>();
            }
            
            // 3. 获取对应的桶
            Bucket<K, V> bucket = buckets[bucketIndex];
            
            // 4. 将键值对添加到桶中(拉链上)
            bucket.add(key, value);
            System.out.println("键值对 ('" + key + "', '" + value + "') 已添加到索引为 " + bucketIndex + " 的桶中");
        }
        
        /**
         * 根据键获取值
         * @param key 键
         * @return 值
         */
        public V get(K key) {
            // 1. 通过哈希函数计算桶的索引位置
            int bucketIndex = hash(key);
            
            // 2. 获取对应的桶，如果桶不存在则返回null
            Bucket<K, V> bucket = buckets[bucketIndex];
            if (bucket == null) {
                return null;
            }
            
            // 3. 在桶中查找对应的值
            return bucket.get(key);
        }
        
        /**
         * 显示哈希表的内部结构
         */
        public void displayStructure() {
            System.out.println("\n=== 哈希表内部结构 ===");
            for (int i = 0; i < buckets.length; i++) {
                Bucket<K, V> bucket = buckets[i];
                System.out.println("桶[" + i + "]: " + (bucket == null ? "空桶" : bucket));
            }
            System.out.println("=====================\n");
        }
    }
    
    /**
     * 桶类 - 代表哈希表中的每个桶
     * 
     * 每个桶实际上是一个链表结构，用于存储发生冲突的元素(拉链法)
     */
    static class Bucket<K, V> {
        // 使用链表存储桶中的元素
        private List<Entry<K, V>> entries = new LinkedList<>();
        
        /**
         * 向桶中添加键值对
         * @param key 键
         * @param value 值
         */
        public void add(K key, V value) {
            // 检查是否已存在相同的键
            for (Entry<K, V> entry : entries) {
                if (Objects.equals(entry.key, key)) {
                    // 更新已有键的值
                    entry.value = value;
                    return;
                }
            }
            // 添加新的键值对到拉链末尾
            entries.add(new Entry<>(key, value));
        }
        
        /**
         * 根据键获取值
         * @param key 键
         * @return 值
         */
        public V get(K key) {
            for (Entry<K, V> entry : entries) {
                if (Objects.equals(entry.key, key)) {
                    return entry.value;
                }
            }
            return null; // 未找到
        }
        
        @Override
        public String toString() {
            if (entries.isEmpty()) {
                return "空桶";
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("拉链[");
            for (int i = 0; i < entries.size(); i++) {
                if (i > 0) sb.append(" -> ");
                Entry<K, V> entry = entries.get(i);
                sb.append("(").append(entry.key).append("=").append(entry.value).append(")");
            }
            sb.append("]");
            return sb.toString();
        }
    }
    
    /**
     * 键值对实体类
     */
    static class Entry<K, V> {
        K key;
        V value;
        
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    
    /**
     * 演示哈希表中桶的实现方式
     */
    public static void demonstrateBucketImplementation() {
        System.out.println("=== 哈希表桶实现演示 ===\n");
        
        // 创建一个容量为10的哈希表（实际会调整为最接近的2的幂次方）
        HashMapWithBuckets<String, String> hashMap = new HashMapWithBuckets<>(10);
        System.out.println("哈希表容量设置为10，实际容量调整为: " + getCapacityByReflection(hashMap) + "\n");
        
        // 添加一些键值对
        String[] keys = {"apple", "banana", "orange", "grape", "kiwi", "melon", "cherry", "strawberry"};
        String[] values = {"苹果", "香蕉", "橙子", "葡萄", "猕猴桃", "甜瓜", "樱桃", "草莓"};
        
        for (int i = 0; i < keys.length; i++) {
            hashMap.put(keys[i], values[i]);
            System.out.println();
        }
        
        // 显示哈希表内部结构
        hashMap.displayStructure();
        
        // 查找示例
        System.out.println("=== 查找示例 ===");
        String searchKey = "orange";
        String result = hashMap.get(searchKey);
        System.out.println("查找键 '" + searchKey + "' 的结果: " + result);
        
        searchKey = "grape";
        result = hashMap.get(searchKey);
        System.out.println("查找键 '" + searchKey + "' 的结果: " + result);
        
        searchKey = "notexist";
        result = hashMap.get(searchKey);
        System.out.println("查找键 '" + searchKey + "' 的结果: " + result);
    }
    
    /**
     * 通过反射获取哈希表的实际容量
     */
    private static int getCapacityByReflection(HashMapWithBuckets map) {
        try {
            java.lang.reflect.Field capacityField = HashMapWithBuckets.class.getDeclaredField("capacity");
            capacityField.setAccessible(true);
            return capacityField.getInt(map);
        } catch (Exception e) {
            return -1;
        }
    }
    
    /**
     * 详细解释哈希表桶的实现原理
     */
    public static void explainBucketImplementation() {
        System.out.println("=== 哈希表桶实现原理解释 ===\n");
        
        System.out.println("1. 桶数组的物理结构：");
        System.out.println("   - 桶是连续的数组结构，每个数组元素就是一个桶");
        System.out.println("   - 数组在内存中占据连续的空间，支持O(1)的随机访问");
        System.out.println("   - 数组长度通常设计为2的幂次方，便于使用位运算\n");
        
        System.out.println("2. 哈希函数与桶索引计算：");
        System.out.println("   - 使用扰动函数混合hashCode的高低位信息：h ^ (h >>> 16)");
        System.out.println("   - 使用位运算计算桶索引：hash & (capacity - 1)");
        System.out.println("   - 当capacity为2的幂次方时，(capacity - 1)的二进制低位全为1");
        System.out.println("   - 这种方式比取模运算更高效\n");
        
        System.out.println("3. 桶的初始化时机：");
        System.out.println("   - 桶数组本身在构造时就全部初始化完成");
        System.out.println("   - 每个桶内的链表是延迟初始化的，即首次使用时才创建");
        System.out.println("   - 这样可以节省内存，避免创建空的链表对象\n");
        
        System.out.println("4. 拉链法处理冲突：");
        System.out.println("   - 当多个键映射到同一桶时，使用链表将它们串联起来");
        System.out.println("   - 每个桶维护自己的链表，互不影响");
        System.out.println("   - 查找时先定位桶，再在链表中查找目标元素\n");
        
        System.out.println("5. 时间复杂度分析：");
        System.out.println("   - 理想情况下，各桶的负载均衡，查找时间复杂度为O(1)");
        System.out.println("   - 最坏情况下，所有元素都映射到同一桶，退化为链表查找O(n)");
        System.out.println("   - 通过良好的哈希函数设计可尽量避免最坏情况的发生\n");
    }
    
    public static void main(String[] args) {
        // 解释桶的实现原理
        explainBucketImplementation();
        
        // 演示桶的具体实现
        demonstrateBucketImplementation();
    }
}