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