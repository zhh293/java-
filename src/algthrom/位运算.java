package algthrom;

import java.util.Stack;
import java.util.stream.Collectors;

public class 位运算 {
    public String addBinary(String a, String b) {
        StringBuffer sb=new StringBuffer();
         if(a.length()<b.length()){
             for(int i=0;i<b.length()-a.length();i++){
                 sb.append("0");
             }
             sb.append(a);
             a=sb.toString();
         }
         if(b.length()<a.length()){
             for(int i=0;i<a.length()-b.length();i++){
                 sb.append("0");
             }
             sb.append(b);
             b=sb.toString();
         }
         //位运算
        StringBuilder sb2=new StringBuilder();
        for(int i=0;i<a.length();i++){
            sb2.append(a.charAt(i)-'0'+b.charAt(i)-'0');
        }
        String result=sb2.toString();
        Stack<Character> stack=new Stack<Character>();
        boolean flag=false;
        for(int i=result.length()-1;i>=0;i--){
            if(result.charAt(i)=='0'){
                if(flag){
                    flag=false;
                    stack.push('1');
                }else{
                    stack.push(result.charAt(i));
                }
                continue;
            }
            if(result.charAt(i)=='1'){
                if(flag){
                    flag=false;
                    stack.push('0');
                    flag=true;
                }
                else {
                    stack.push('1');
                }
                continue;
            }
            if(result.charAt(i)=='2'){
                if(flag){
                    flag=false;
                    stack.push('1');
                }else {
                    stack.push('0');
                }
                flag=true;
            }
        }
        if(flag){
            stack.push('1');
        }
        StringBuilder result2=new StringBuilder();
        while(!stack.isEmpty()){
            result2.append(stack.pop());
        }
        return result2.toString();
    }
    public  String stackBottomToTop(Stack<Character> stack) {
        if (stack.isEmpty()) {
            return ""; // 空栈返回空字符串
        }

        Stack<Character> temp = new Stack<>(); // 临时栈用于反转顺序
        StringBuilder sb = new StringBuilder();

        // 1. 将原栈元素全部弹出并压入临时栈（此时临时栈顶是原栈底）
        while (!stack.isEmpty()) {
            temp.push(stack.pop());
        }

        // 2. 从临时栈弹出元素（顺序为原栈底→原栈顶），同时恢复原栈
        while (!temp.isEmpty()) {
            char c = temp.pop();
            sb.append(c); // 拼接当前元素
            stack.push(c); // 推回原栈，恢复结构
        }

        return sb.toString();
    }
}


/*
int m=32-Integer.numberOfLeadingZeros(n);
这一步什么意思
这行代码的作用是计算整数n的二进制表示中有效数字的位数（即二进制形式下的长度，不包含前面多余的 0）。

具体来说，我们可以拆分为两部分理解：

        Integer.numberOfLeadingZeros(n)
这是 Java 中Integer类的一个静态方法，用于返回整数n的二进制表示中，从最高位（左侧）开始连续的 0 的个数。
例如：
当n=5时，二进制是000...000101（32 位 int 类型，前 29 位是 0），所以该方法返回29；
当n=1时，二进制是000...0001（前 31 位是 0），方法返回31；
当n=0时，二进制全是 0，方法返回32（因为 int 是 32 位）。
        32 - 上述结果
由于 int 类型在 Java 中是 32 位（包含符号位），用 32 减去 “前导 0 的个数”，得到的就是二进制中有效数字的位数（即从最高位的 1 到最低位的总长度）。
例如：
n=5时，32 - 29 = 3（二进制101的长度是 3）；
n=1时，32 - 31 = 1（二进制1的长度是 1）；
n=0时，32 - 32 = 0（0 没有有效数字位）。

因此，这行代码的最终效果是：计算整数n在二进制表示下的有效位数。
还没有其他的常用的操纵位数的方法了
在 Java 中，Integer（针对 32 位整数）和Long（针对 64 位整数）类提供了许多用于位操作的静态方法，非常适合操纵二进制位。除了numberOfLeadingZeros，还有这些常用方法：
        1. 计算尾部连续 0 的个数：numberOfTrailingZeros(int n)
作用：返回整数n的二进制表示中，从最低位（右侧）开始连续的 0 的个数。
示例：
n=8（二进制1000）：尾部有 3 个 0 → 返回3；
n=5（二进制101）：尾部有 0 个 0 → 返回0；
n=0：所有位都是 0 → 返回32（因为 int 是 32 位）。
        2. 计算二进制中 1 的个数：bitCount(int n)
作用：返回整数n的二进制表示中，值为 1 的位的总数（也叫 “汉明重量”）。
示例：
n=5（二进制101）：有 2 个 1 → 返回2；
n=7（二进制111）：有 3 个 1 → 返回3；
n=-1（二进制全 1，补码表示）：返回32。
        3. 获取最高位的 1：highestOneBit(int n)
作用：返回整数n的二进制表示中，最高位的 1 所对应的值（其他位为 0）。
示例：
n=5（二进制101）：最高位的 1 在第 3 位（值为 4） → 返回4；
n=8（二进制1000）：最高位的 1 在第 4 位（值为 8） → 返回8；
n=0：没有 1 → 返回0。
        4. 获取最低位的 1：lowestOneBit(int n)
作用：返回整数n的二进制表示中，最低位的 1 所对应的值（其他位为 0）。
示例：
n=5（二进制101）：最低位的 1 在第 1 位（值为 1） → 返回1；
n=6（二进制110）：最低位的 1 在第 2 位（值为 2） → 返回2；
n=0：返回0。
        5. 反转二进制位：reverse(int n)
作用：将整数n的 32 位二进制位整体反转（最高位和最低位交换，次高位和次低位交换，以此类推）。
示例：
n=1（二进制000...0001，31 个 0+1）：反转后为100...000 → 返回2^31；
n=0b101（二进制000...000101）：反转后为101000...000 → 具体值取决于 32 位长度。
        6. 循环移位：rotateLeft(int n, int distance) / rotateRight(...)
作用：对二进制位进行循环左移 / 右移（移出的位会补到另一端）。
示例：
rotateLeft(0b1001, 1)（二进制1001左移 1 位）：左侧移出的1补到右侧 → 结果为0b0011；
rotateRight(0b1001, 1)（右移 1 位）：右侧移出的1补到左侧 → 结果为0b1100。
补充：基础位运算符
除了上述方法，直接使用位运算符也是操纵位的核心方式：

        &：按位与（两位都为 1 则为 1）；
        |：按位或（两位有一个为 1 则为 1）；
        ^：按位异或（两位不同则为 1）；
        ~：按位非（反转所有位）；
        <<：左移（高位丢弃，低位补 0）；
        >>：右移（正数高位补 0，负数高位补 1）；
        >>>：无符号右移（无论正负，高位都补 0）。
*/



