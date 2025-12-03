package datastructure1;

/**
 * Java中hashCode计算方法详解
 * 
 * 在Java中，hashCode()方法用于返回对象的哈希码值。对于不同的类，hashCode的计算方式不同。
 * 这个文件详细解释了Java中各种类型的hashCode计算方法。
 */
public class JavaHashCode详解 {

    /**
     * Object类的hashCode方法
     * 
     * Object类中的hashCode()方法通常返回对象的内存地址的哈希值。
     * 这意味着即使两个对象的内容完全相同，但如果它们是不同的实例，
     * 它们的hashCode值也可能不同。
     * 
     * 这对于HashMap和HashSet来说是不可接受的，因为它们依赖于equals和hashCode来判断对象的相等性。
     */
    public void objectHashCode() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        
        // 即使内容相同（都是空对象），不同的实例有不同的hashCode
        System.out.println("obj1.hashCode(): " + obj1.hashCode());
        System.out.println("obj2.hashCode(): " + obj2.hashCode());
    }

    /**
     * String类的hashCode方法
     * 
     * String类的hashCode方法是一个经典的例子，它通过以下公式计算哈希码：
     * s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
     * 
     * 其中，s[i]是字符串的第i个字符，n是字符串的长度，^表示幂运算。
     * 
     * 选择31作为乘数的原因：
     * 1. 31是一个奇素数
     * 2. 31可以被JVM优化为位运算（i * 31 == (i << 5) - i），提高计算效率
     * 3. 这种算法能够有效地将字符串内容映射到哈希码，并且具有较好的均匀分布性
     * 
     * String的hashCode()方法源码实现（简化版）：
     * public int hashCode() {
     *     int h = hash;
     *     if (h == 0 && value.length > 0) {
     *         char val[] = value;
     *         for (int i = 0; i < value.length; i++) {
     *             h = 31 * h + val[i];
     *         }
     *         hash = h;
     *     }
     *     return h;
     * }
     */
    public void stringHashCode() {
        // 示例计算过程
        String str = "hash";
        
        // 计算过程：
        // h = 0
        // h = 31 * 0 + 'h' = 104
        // h = 31 * 104 + 'a' = 3224 + 97 = 3321
        // h = 31 * 3321 + 's' = 102951 + 115 = 103066
        // h = 31 * 103066 + 'h' = 3195046 + 104 = 3195150
        
        System.out.println("\"hash\"的hashCode: " + str.hashCode()); // 输出: 3195150
        
        // 空字符串的hashCode为0
        System.out.println("\"\"的hashCode: " + "".hashCode()); // 输出: 0
        
        // 相同内容的字符串具有相同的hashCode
        String str1 = "example";
        String str2 = "example";
        System.out.println("str1.hashCode(): " + str1.hashCode());
        System.out.println("str2.hashCode(): " + str2.hashCode());
        System.out.println("str1.hashCode() == str2.hashCode(): " + (str1.hashCode() == str2.hashCode()));
    }

    /**
     * 自定义类的hashCode方法
     * 
     * 对于自定义类，通常需要重写hashCode方法以配合equals方法使用。
     * 
     * 编写hashCode方法的最佳实践：
     * 1. 使用质数作为乘数（如17, 31等）
     * 2. 包含所有用于equals比较的字段
     * 3. 对于null值要有特殊处理
     * 4. 保持一致性：如果两个对象通过equals比较相等，它们的hashCode必须相等
     */
    public void customHashCode() {
        // 参考Router.java中的Pack类实现
        /*
        @Override
        public int hashCode() {
            if (data == null) return 0; // 若数组为null，返回固定值0
            int result = 17; // 初始质数（选17可减少初始碰撞）
            for (int num : data) { // 遍历数组，累积哈希值
                result = 31 * result + num; // 31为乘数，兼顾效率和低碰撞
            }
            return result;
        }
        */
    }

    /**
     * 数组的hashCode方法
     * 
     * Java中数组的hashCode计算方法不同于我们通常认为的基于内容的计算。
     * 数组继承自Object类，因此默认使用Object的hashCode方法，基于内存地址计算。
     * 
     * 如果要基于内容计算数组的hashCode，应该使用Arrays.hashCode()方法。
     */
    public void arrayHashCode() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        
        // 数组的hashCode基于内存地址，即使内容相同，hashCode也不同
        System.out.println("arr1.hashCode(): " + arr1.hashCode());
        System.out.println("arr2.hashCode(): " + arr2.hashCode());
        System.out.println("arr1.hashCode() == arr2.hashCode(): " + (arr1.hashCode() == arr2.hashCode()));
        
        // 使用Arrays.hashCode()基于内容计算hashCode
        System.out.println("Arrays.hashCode(arr1): " + java.util.Arrays.hashCode(arr1));
        System.out.println("Arrays.hashCode(arr2): " + java.util.Arrays.hashCode(arr2));
        System.out.println("Arrays.hashCode(arr1) == Arrays.hashCode(arr2): " + 
                          (java.util.Arrays.hashCode(arr1) == java.util.Arrays.hashCode(arr2)));
    }

    /**
     * 包装类的hashCode方法
     * 
     * Java的基本类型包装类（Integer, Long, Double等）都有自己的hashCode实现。
     * 通常情况下，它们的hashCode就是其值本身或者与其值相关的计算结果。
     */
    public void wrapperHashCode() {
        Integer i1 = 100;
        Integer i2 = 100;
        Integer i3 = new Integer(100);
        
        System.out.println("Integer(100)的hashCode: " + i1.hashCode()); // 100
        System.out.println("Integer(100)的hashCode: " + i2.hashCode()); // 100
        System.out.println("new Integer(100)的hashCode: " + i3.hashCode()); // 100
        
        // 对于Integer类型，hashCode就是其值本身
        System.out.println("i1.hashCode() == i2.hashCode(): " + (i1.hashCode() == i2.hashCode()));
    }

    /**
     * hashCode的重要规则
     * 
     * 1. 在程序执行期间，只要对象的equals方法所用到的信息没有被修改，
     *    那么对这同一个对象调用多次，hashCode方法必须始终如一地返回同一个整数。
     *    
     * 2. 如果两个对象根据equals(Object)方法比较是相等的，
     *    那么调用这两个对象中任一个对象的hashCode方法都必须产生同样的整数结果。
     *    
     * 3. 如果两个对象根据equals(Object)方法比较是不相等的，
     *    那么调用这两个对象中任一个对象的hashCode方法，不要求产生不同的整数结果。
     *    但是程序员应该意识到，给不相等的对象产生截然不同的整数结果，有可能提高散列表的性能。
     */
    public void hashCodeRules() {
        // 规则1示例
        String str = "test";
        int hash1 = str.hashCode();
        int hash2 = str.hashCode();
        System.out.println("同一对象多次调用hashCode结果一致: " + (hash1 == hash2));
        
        // 规则2示例
        String str1 = new String("example");
        String str2 = new String("example");
        System.out.println("equals相等的对象hashCode必须相等: " + 
                          (str1.equals(str2) && str1.hashCode() == str2.hashCode()));
        
        // 规则3示例：不相等的对象可以有相同的hashCode（哈希冲突）
        // 虽然不强制要求不同对象有不同的hashCode，但好的hashCode实现应该尽量避免冲突
    }

    public static void main(String[] args) {
        JavaHashCode详解 demo = new JavaHashCode详解();
        
        System.out.println("=== Object hashCode ===");
        demo.objectHashCode();
        
        System.out.println("\n=== String hashCode ===");
        demo.stringHashCode();
        
        System.out.println("\n=== Array hashCode ===");
        demo.arrayHashCode();
        
        System.out.println("\n=== Wrapper hashCode ===");
        demo.wrapperHashCode();
    }
}