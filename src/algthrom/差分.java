package algthrom;

public class 差分 {
    public static void main(String[] args) {
        int[]arr=new int[1001];

    }
    public boolean carPooling(int[][] trips, int capacity) {
        int[]arr=new int[1001];
        for(int i=0;i<trips.length;i++){
            arr[trips[i][1]]+=trips[i][0];
            arr[trips[i][2]]-=trips[i][0];
        }
        int s=0;
        for(int i=0;i<arr.length;i++){
            s+=arr[i];
            if(s>capacity){
                return false;
            }
        }
        return true;
    }
    public int countTestedDevices(int[] batteryPercentages) {
        int count=0;
        int mul=1;
        for(int i=0;i<batteryPercentages.length;i++){
            if(batteryPercentages[i]>=mul){
                count++;
                mul++;
            }
        }
        return count;

    }
   /* 举例
    考虑数组 a=[1,3,3,5,8]，对其中的相邻元素两两作差（右边减左边），得到数组 [2,0,2,3]。然后在开头补上 a[0]，得到差分数组

    d=[1,2,0,2,3]
    这有什么用呢？如果从左到右累加 d 中的元素，我们就「还原」回了 a 数组 [1,3,3,5,8]。这类似求导与积分。

    这又有什么用呢？现在把连续子数组 a[1],a[2],a[3] 都加上 10，得到 a
′
        =[1,13,13,15,8]。再次两两作差，并在开头补上 a
′
        [0]，得到差分数组

            d
′
        =[1,12,0,2,−7]
    对比 d 和 d
′
        ，可以发现只有 d[1] 和 d[4] 变化了，这意味着对 a 中连续子数组的操作，可以转变成对差分数组 d 中两个数的操作。

    定义和性质
    对于数组 a，定义其差分数组（difference array）为

    d[i]={
        a[0],
                a[i]−a[i−1],
​

        i=0
        i≥1
​

        性质 1：从左到右累加 d 中的元素，可以得到数组 a。

        性质 2：如下两个操作是等价的。

        把 a 的子数组 a[i],a[i+1],…,a[j] 都加上 x。
        把 d[i] 增加 x，把 d[j+1] 减少 x。
        利用性质 2，我们只需要 O(1) 的时间就可以完成对 a 的子数组的操作。最后利用性质 1 从差分数组复原出数组 a。

        注：也可以这样理解，d[i] 表示把下标 ≥i 的数都加上 d[i]。

        本题思路
        对于本题，设 a[i] 表示车行驶到位置 i 时车上的人数。我们需要判断是否所有 a[i] 都不超过 capacity。

        trips[i] 相当于把 a 中下标从 from
        i
​
        到 to
        i
​
 −1 的数都增加 numPassengers
        i
​
 。这正好可以用上面讲的差分数组解决。

        例如示例 1 对应的 d 数组，d[1]=2, d[5]=−2, d[3]=3, d[7]=−3，即

        d=[0,2,0,3,0,−2,0,−3,…]
        从左到右累加，得到

        a=[0,2,2,5,5,3,3,0,…]
        capacity=4，由于 max(a)=5>4，所以返回 false。

        实现方法
        有两种写法：

        第一种写法是，创建一个长为 1001 的差分数组，这可以保证 d 数组不会下标越界。
        第二种写法是，用平衡树（C++ 中的 map，Java 中的 TreeMap）代替差分数组，因为我们只需要考虑在 from
        i
​
        和 to
        i
​
        这些位置上的乘客数，其余位置的乘客是保持不变的，无需考虑。平衡树可以保证我们是从小到大遍历这些位置的。当然，如果你不想用平衡树的话，也可以用哈希表，把哈希表的 key 取出来排序，就可以从小到大遍历这些位置了。

        作者：灵茶山艾府
        链接：https://leetcode.cn/problems/car-pooling/solutions/2550264/suan-fa-xiao-ke-tang-chai-fen-shu-zu-fu-9d4ra/
        来源：力扣（LeetCode）
        著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。*/
}
