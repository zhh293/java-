package algthrom;

public class 前缀和 {
    public static void main(String[] args) {
       /* 关键思路
        注：本文中的「子数组」均表示「连续子数组」。

        比如 nums=[1,2,3,4,5,6]，要想计算子数组 [3,4,5] 的元素和，可以用前缀 [1,2,3,4,5] 的元素和，减去另一个前缀 [1,2] 的元素和，就得到了子数组 [3,4,5] 的元素和，即

        3+4+5=(1+2+3+4+5)−(1+2)
        换句话说，把前缀 [1,2,3,4,5] 的前缀 [1,2] 去掉，就得到了子数组 [3,4,5]。

        一般地，任意子数组都是一个前缀去掉前缀后的结果。所以任意子数组的和，都可以表示为两个前缀和的差。

        nums 的子数组有 O(n
                2
        ) 个，但只有 O(n) 个前缀。那么，预处理 nums 的所有前缀和，就可以 O(1) 计算任意子数组的元素和。

        作者：灵茶山艾府
        链接：https://leetcode.cn/problems/range-sum-query-immutable/solutions/2693498/qian-zhui-he-ji-qi-kuo-zhan-fu-ti-dan-py-vaar/
        来源：力扣（LeetCode）
        著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。*/
    }
}
