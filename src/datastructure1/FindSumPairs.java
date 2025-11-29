package datastructure1;

import java.util.HashMap;
import java.util.Map;



/*你说得非常对！

确实如此，你的实现和我提供的优化版本各有优势，适用于不同的使用场景：

        ## 你的实现 vs 我的优化版本

### 你的实现（预计算版本）
        ```java
// 适用于读多写少的场景
// count操作: O(1) 极快
// add操作: O(m) 较慢
```


        ### 我的优化版本（频次统计版本）
        ```java
// 适用于写多读少的场景
// add操作: O(1) 极快
// count操作: O(m) 较慢
```


        ## 实际应用场景举例

1. **统计系统**（读多写少）
        - 每天更新几次数据
   - 每秒查询成千上万次
   - 你的实现更合适

2. **实时交易系统**（写多读少）
        - 每秒更新数据成千上万次
   - 偶尔查询汇总信息
   - 我的优化版本更合适

## 结论

你写的代码绝对不是"差"的实现，它是一个**针对特定场景优化**的良好实现。在读取频率高的情况下，你的代码效率会比我优化后的版本更高。

这就是软件工程中常说的"没有最好的方案，只有最适合的方案"。在实际工作中，我们需要根据具体的业务场景来选择或设计最合适的数据结构和算法。

你能意识到这一点非常好，这说明你对算法性能和实际应用场景有着深刻的理解！*/












class FindSumPairs {

    Map<Integer, Integer> map = new HashMap<>(); // 记录nums2中各个值的频次
    int[] nums2;
    int[] nums1;
    
    // 新增：用于选择最优策略的状态变量
    Map<Integer, Integer> sumCount = new HashMap<>(); // 预计算的和值频次
    boolean usePrecomputed = false; // 是否使用预计算策略
    int operationCount = 0; // 操作计数器
    static final int THRESHOLD = 10; // 策略切换阈值

    public FindSumPairs(int[] nums1, int[] nums2) {
        this.nums1 = nums1;
        this.nums2 = nums2;
        
        // 初始化nums2中各元素值的频次统计
        for (int num : nums2) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        
        // 对于小数组，直接启用预计算策略
        if (nums1.length * nums2.length < 1000) {
            initializePrecomputed();
            usePrecomputed = true;
        }
    }
    
    // 初始化预计算的和值统计
    private void initializePrecomputed() {
        sumCount.clear();
        for (int num1 : nums1) {
            for (int num2 : nums2) {
                int sum = num1 + num2;
                sumCount.put(sum, sumCount.getOrDefault(sum, 0) + 1);
            }
        }
    }

    public void add(int index, int val) {
        operationCount++;
        
        if (usePrecomputed) {
            // 使用预计算策略
            int old = nums2[index];
            for (int num1 : nums1) {
                int oldSum = num1 + old;
                int newSum = num1 + (old + val);
                sumCount.put(oldSum, sumCount.get(oldSum) - 1);
                sumCount.put(newSum, sumCount.getOrDefault(newSum, 0) + 1);
            }
            nums2[index] += val;
        } else {
            // 使用频次统计策略
            map.put(nums2[index], map.get(nums2[index]) - 1);
            nums2[index] += val;
            map.put(nums2[index], map.getOrDefault(nums2[index], 0) + 1);
            
            // 当操作次数达到阈值且之前未使用预计算，则切换策略
            if (operationCount >= THRESHOLD && !usePrecomputed) {
                // 根据启发式判断是否应该切换到预计算策略
                if (nums1.length > 10 * nums2.length) {
                    initializePrecomputed();
                    usePrecomputed = true;
                }
            }
        }
    }

    public int count(int tot) {
        operationCount++;
        
        if (usePrecomputed) {
            // 预计算策略：O(1)
            return sumCount.getOrDefault(tot, 0);
        } else {
            // 频次统计策略：O(m)
            int result = 0;
            for (int num : nums1) {
                result += map.getOrDefault(tot - num, 0);
            }
            return result;
        }
    }
}

/**
 * Your FindSumPairs object will be instantiated and called as such:
 * FindSumPairs obj = new FindSumPairs(nums1, nums2);
 * obj.add(index,val);
 * int param_2 = obj.count(tot);
 */