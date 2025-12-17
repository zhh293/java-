package JVM;

public class Demo {

    /*




    Java Virtual Machine (JVM) 的内存模型是指在运行 Java 程序时，JVM 如何组织和管理内存的结构。JVM 内存分为多个区域，每个区域有不同的用途，用于存储不同类型的数据。下面是 JVM 内存模型的五大区域，以及相应的模型图解：
            1. 方法区（Method Area）:
            • 主要存储类的结构信息，如类的字段信息、方法信息、构造方法信息等。
            • 运行时常量池，用于存储编译期生成的各种字面量和符号引用。
            • 永久代（Permanent Generation）：在 JDK 7 及之前版本使用，JDK 8 开始移除了永久代，用元空间（Metaspace）代替。


            2. 堆（Heap）:
• 存储对象实例，包括程序中创建的对象以及 Java 虚拟机自动创建的对象。
• 堆空间可以分为新生代和老年代，还包括持久代（JDK 7 及之前版本）或元空间（JDK 8 及之后版本）。
3. 栈（Stack）:
• 存储局部变量、操作数栈、方法出口等信息。
• 每个线程都有一个私有的栈，用于存储方法的局部变量和部分结果。
• 栈（Stack）是一种数据结构，它按照后进先出（Last In, First Out，LIFO）的原则管理数据，即最后进入的元素最先被访问。栈可以看作是一种特殊的线性表，只允许在一端进行插入和删除操作，该端被称为栈顶（Top），而另一端被称为栈底（Bottom）。
4. 本地方法栈（Native Method Stack）:
• 与栈类似，用于存储执行本地（native）方法的数据。
• 本地方法栈（Native Method Stack）是Java虚拟机（JVM）内存模型中的一部分，用于支持本地方法的调用。本地方法指的是用非Java语言（如C、C++）编写的，通过Java Native Interface（JNI）在Java程序中调用的方法。
在Java程序中，当需要调用本地方法时，JVM会创建一个本地方法栈，用于执行本地方法的操作。与虚拟机栈类似，本地方法栈也是线程私有的，每个线程都有自己的本地方法栈。
5. 程序计数器（Program Counter Register）:
• 每个线程都有一个程序计数器，用于存储当前线程正在执行的指令的地址。
• 线程切换时，程序计数器也会切换到相应线程的执行地址。
这些区域共同组成了 Java 程序在 JVM 中运行时的内存结构。不同区域的作用和生命周期有所不同，了解这些区域对于理解 Java 内存管理和调优非常重要。请注意，JVM 的一些实现可能对内存模型有所调整，上述描述主要适用于经典的 HotSpot JVM。










            */




























   /*

    在 Java 中，String 是一个非常核心且特殊的类，它有很多细节值得注意。下面我会从特性、内存机制、常用方法、性能优化等方面做一个全面总结。

            1. 基本特性
    不可变性（Immutable）
    一旦创建 String 对象，其内容不可更改。任何修改操作（如拼接、替换）都会生成新的 String 对象。
    final 类
    String 被 final 修饰，不能被继承。
    底层存储
    JDK 9 之前：char[] 存储字符（UTF-16 编码）。
    JDK 9 之后：byte[] + coder（紧凑字符串 Compact Strings，节省内存）。
    线程安全
    因为不可变性，String 是线程安全的。
            2. 创建方式与内存机制
2.1 字面量方式
            Java

    复制代码
    String s1 = "Hello";
    String s2 = "Hello"; // s1 == s2 为 true
    字面量存储在 字符串常量池（String Pool） 中。
    相同字面量只会在常量池中存一份，节省内存。
            2.2 构造方法
            Java

    复制代码
    String s3 = new String("Hello");
    会在堆中创建一个新的对象，即使常量池中已有相同内容。
    s3 == s1 为 false，但 s3.equals(s1) 为 true。
            2.3 intern() 方法
    Java

            复制代码
    String s4 = new String("Hello").intern();
    intern() 会返回常量池中该字符串的引用，如果不存在则将其加入常量池。
            3. 常用方法
            Java

    复制代码
    String str = "Java Programming";
str.length();           // 长度
str.charAt(0);          // 获取字符
str.substring(5);       // 截取
str.indexOf("Pro");     // 查找
str.equals("Java");     // 比较内容
str.equalsIgnoreCase("java"); // 忽略大小写比较
str.toUpperCase();      // 转大写
str.trim();             // 去除首尾空格
str.replace("Java", "C++"); // 替换
    注意：这些方法不会修改原字符串，而是返回新对象。

            4. 性能与优化
    频繁修改字符串 → 使用 StringBuilder（单线程）或 StringBuffer（多线程）。
    避免不必要的 new String()，优先使用字面量。
    intern() 可减少重复字符串对象，但会增加常量池压力。
            5. 内存与常量池细节
    JDK 6 及之前：字符串常量池在 方法区（PermGen）。
    JDK 7+：常量池移到 堆内存，减少 PermGen OOM 风险。
    JDK 9+：引入紧凑字符串（Compact Strings），ASCII 字符用 LATIN1 存储，节省 50% 内存。
            6. 示例：不可变性验证
            Java

    复制代码
    public class StringTest {
        public static void main(String[] args) {
            String a = "Hello";
            String b = a;
            a = a + " World";
            System.out.println(a); // Hello World
            System.out.println(b); // Hello
        }
    }
    输出结果表明，修改 a 并不会影响 b，因为 String 是不可变的。*/
}
