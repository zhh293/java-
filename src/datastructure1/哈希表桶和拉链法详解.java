package datastructure1;

import java.util.*;

/**
 * 哈希表桶和拉链法详解
 * 
 * 解释哈希表中桶(Bucket)的概念以及拉链法(Chaining)的工作原理
 */
public class 哈希表桶和拉链法详解 {
    
    /**
     * 模拟哈希表中的桶结构
     * 
     * 在哈希表中：
     * 1. 每个数组元素称为一个"桶"(Bucket)
     * 2. 通过哈希函数计算得到的值用于确定元素应放入哪个桶中
     * 3. 每个桶中可以存储多个元素(当发生哈希冲突时)
     */
    static class SimpleHashMap<K, V> {
        // 桶数组 - 每个元素就是一个桶
        private Bucket<K, V>[] buckets;
        private int capacity;
        
        public SimpleHashMap(int capacity) {
            this.capacity = capacity;
            // 初始化桶数组
            this.buckets = new Bucket[capacity];
            // 为每个桶位置创建一个空的桶
            for (int i = 0; i < capacity; i++) {
                buckets[i] = new Bucket<>();
            }
        }
        
        /**
         * 简单的哈希函数 - 计算键应该放在哪个桶中
         * @param key 键
         * @return 桶的索引位置
         */
        private int hash(K key) {
            if (key == null) return 0;
            // 使用hashCode并结合数组长度计算桶索引
            return Math.abs(key.hashCode()) % capacity;
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
            
            // 2. 获取对应的桶
            Bucket<K, V> bucket = buckets[bucketIndex];
            
            // 3. 将键值对添加到桶中(拉链上)
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
            
            // 2. 获取对应的桶
            Bucket<K, V> bucket = buckets[bucketIndex];
            
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
                System.out.println("桶[" + i + "]: " + bucket);
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
     * 演示哈希表中桶和拉链的概念
     */
    public static void demonstrateBucketAndChaining() {
        System.out.println("=== 哈希表桶和拉链法演示 ===\n");
        
        // 创建一个容量为5的简单哈希表
        SimpleHashMap<String, String> hashMap = new SimpleHashMap<>(5);
        
        // 添加一些键值对
        String[] keys = {"apple", "banana", "orange", "grape", "kiwi", "melon", "cherry"};
        String[] values = {"苹果", "香蕉", "橙子", "葡萄", "猕猴桃", "甜瓜", "樱桃"};
        
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
    }
    
    /**
     * 详细解释哈希表中的概念
     */
    public static void explainConcepts() {
        System.out.println("=== 哈希表核心概念详解 ===\n");
        
        System.out.println("1. 桶(Bucket)的概念：");
        System.out.println("   - 哈希表底层数组的每个元素称为一个'桶'");
        System.out.println("   - 每个桶都有一个索引位置，从0开始");
        System.out.println("   - 桶是存储数据的基本单位\n");
        
        System.out.println("2. 哈希函数的作用：");
        System.out.println("   - 哈希函数将键映射到桶的索引位置");
        System.out.println("   - 计算公式：bucket_index = hash(key) % array_length");
        System.out.println("   - 返回值就是桶的地址(索引)\n");
        
        System.out.println("3. 拉链法(Chaining)：");
        System.out.println("   - 当多个键映射到同一个桶时，使用链表解决冲突");
        System.out.println("   - 每个桶中的链表就像一条'拉链'，可以连接多个元素");
        System.out.println("   - 链表中的每个节点存储一个键值对\n");
        
        System.out.println("4. 工作流程：");
        System.out.println("   步骤1: 通过哈希函数计算键的桶索引");
        System.out.println("   步骤2: 根据索引定位到对应的桶");
        System.out.println("   步骤3: 在桶的拉链中添加或查找元素\n");
        
        System.out.println("5. 优势：");
        System.out.println("   - 实现简单，易于理解");
        System.out.println("   - 删除操作方便");
        System.out.println("   - 对装载因子的容忍度较高\n");
    }
    
    public static void main(String[] args) {
        // 解释核心概念
        explainConcepts();
        
        // 演示桶和拉链法
        demonstrateBucketAndChaining();
    }















   /* 是的，您的理解完全正确！让我详细解释一下哈希表中的这些术语和概念。

    在哈希表中：

            1. **桶(bucket)**：哈希表底层数组的每个元素确实被称为"桶"。每个桶对应一个索引位置，可以存储一个或多个键值对。

            2. **哈希函数计算**：通过哈希函数计算出的值确实是用来确定数据应该放在哪个桶中，也就是确定桶的索引地址。

            3. **拉链法**：在使用拉链法解决冲突时，每个桶中存储的是一个链表(或其他数据结构)，这个链表就像是一个"拉链"，可以连接多个发生冲突的元素。

    让我创建一个详细的示例来说明这个概念：




            [哈希表桶和拉链法详解.java](E:\java\datastructure\src\datastructure1\哈希表桶和拉链法详解.java)


    是的，您的理解完全正确！让我详细解释一下哈希表中的这些重要概念。

            ## 哈希表中的桶(Bucket)和拉链法(Chaining)

            ### 1. 桶(Bucket)的概念

    您说得对，哈希表底层数组的每个元素确实被称为"桶"(Bucket)。每个桶都有一个索引位置，从0开始计数。

            ```java
    // 例如，一个简单的哈希表结构
    Entry[] buckets = new Entry[16]; // 16个桶的数组
```


        ### 2. 哈希函数与桶地址

    通过哈希函数计算得到的值确实用于确定数据应该放在哪个桶中，也就是确定桶的索引地址：

            ```java
    // 哈希函数计算桶索引的经典实现
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    // 计算数组索引(桶地址)
    int index = hash(key) & (table.length - 1);
```


        ### 3. 拉链法(Chaining)

    在拉链法中，每个桶实际上存储的是一个链表结构，这条链表就像是"拉链"一样，可以把多个发生冲突的元素串在一起：

            ```java
    // 每个桶的结构
    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next; // 指向链表中的下一个节点

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
```


        ### 概念关系图：

            ```
    哈希表数组
+----------+
        |  桶[0]   | ---> 拉链: (key1,val1) -> (key2,val2) -> null
            +----------+
            |  桶[1]   | ---> 拉链: (key3,val3) -> null
            +----------+
            |  桶[2]   | ---> 拉链: 空
+----------+
        |    ...   |
        +----------+
        |  桶[n-1] | ---> 拉链: (key4,val4) -> (key5,val5) -> (key6,val6) -> null
            +----------+
            ```


            ### 工作流程：

            1. **插入元素**：
            - 通过哈希函数计算键的桶索引
   - 根据索引定位到对应的桶
   - 在桶的拉链末尾添加新元素

2. **查找元素**：
            - 通过哈希函数计算键的桶索引
   - 根据索引定位到对应的桶
   - 在桶的拉链中遍历查找目标元素

3. **处理冲突**：
            - 当多个键映射到同一个桶时，它们会被依次添加到该桶的拉链中

    您的理解非常准确！我已经创建了一个详细的示例文件 [哈希表桶和拉链法详解.java](file:///E:/java/datastructure/src/datastructure1/%E5%93%88%E5%B8%8C%E8%A1%A8%E6%A1%B6%E5%92%8C%E6%8B%89%E9%93%BE%E6%B3%95%E8%AF%A6%E8%A7%A3.java) 来演示这些概念，您可以运行它来更好地理解哈希表中桶和拉链法的工作原理。
*/}