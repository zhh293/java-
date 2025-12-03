package algthrom.查找;

/**
 * 插值查找算法实现类
 * 插值查找是二分查找的优化版本，适用于数据分布均匀且已排序的数组
 */
public class 插值查找 {

    /**
     * 插值查找算法
     * 根据关键字在数据表中的大概位置确定查找位置，而不是像二分查找那样始终从中间开始查找
     *
     * @param arr 已排序的数组（升序）
     * @param target 要查找的目标值
     * @return 目标值在数组中的索引，如果未找到则返回-1
     */
    public static int interpolationSearch(int[] arr, int target) {
        // 定义左右边界
        int left = 0;
        int right = arr.length - 1;

        // 当左边界小于等于右边界且目标值在数组范围内时继续查找
        while (left <= right && target >= arr[left] && target <= arr[right]) {
            // 如果数组中只有一个元素
            if (left == right) {
                if (arr[left] == target) {
                    return left;
                } else {
                    return -1;
                }
            }

            // 计算插值位置
            // 公式：pos = left + ((target - arr[left]) * (right - left)) / (arr[right] - arr[left])
            // 这个公式根据目标值在整个数据范围内的比例来预测其位置
            int pos = left + ((target - arr[left]) * (right - left)) / (arr[right] - arr[left]);

            // 如果找到了目标值
            if (arr[pos] == target) {
                return pos;
            }

            // 如果目标值大于当前位置的值，在右半部分查找
            if (arr[pos] < target) {
                left = pos + 1;
            } 
            // 如果目标值小于当前位置的值，在左半部分查找
            else {
                right = pos - 1;
            }
        }

        // 未找到目标值
        return -1;
    }

    /**
     * 插值查找的递归实现
     *
     * @param arr 已排序的数组（升序）
     * @param target 要查找的目标值
     * @param left 查找范围的左边界
     * @param right 查找范围的右边界
     * @return 目标值在数组中的索引，如果未找到则返回-1
     */
    public static int interpolationSearchRecursive(int[] arr, int target, int left, int right) {
        // 基本终止条件
        if (left <= right && target >= arr[left] && target <= arr[right]) {
            // 如果数组中只有一个元素
            if (left == right) {
                if (arr[left] == target) {
                    return left;
                } else {
                    return -1;
                }
            }

            // 计算插值位置
            int pos = left + ((target - arr[left]) * (right - left)) / (arr[right] - arr[left]);

            // 如果找到了目标值
            if (arr[pos] == target) {
                return pos;
            }

            // 如果目标值大于当前位置的值，在右半部分递归查找
            if (arr[pos] < target) {
                return interpolationSearchRecursive(arr, target, pos + 1, right);
            } 
            // 如果目标值小于当前位置的值，在左半部分递归查找
            else {
                return interpolationSearchRecursive(arr, target, left, pos - 1);
            }
        }

        // 未找到目标值
        return -1;
    }

    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 创建一个已排序的测试数组（数据分布相对均匀）
        int[] arr = {10, 12, 13, 16, 18, 19, 20, 21, 22, 23, 24, 33, 35, 42, 47};
        
        System.out.println("测试数组:");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println("\n");

        // 测试查找存在的元素
        int target1 = 18;
        int result1 = interpolationSearch(arr, target1);
        System.out.println("查找 " + target1 + ":");
        if (result1 != -1) {
            System.out.println("在索引 " + result1 + " 处找到");
        } else {
            System.out.println("未找到");
        }

        // 测试查找不存在的元素
        int target2 = 25;
        int result2 = interpolationSearch(arr, target2);
        System.out.println("\n查找 " + target2 + ":");
        if (result2 != -1) {
            System.out.println("在索引 " + result2 + " 处找到");
        } else {
            System.out.println("未找到");
        }

        // 测试查找第一个元素
        int target3 = 10;
        int result3 = interpolationSearch(arr, target3);
        System.out.println("\n查找 " + target3 + ":");
        if (result3 != -1) {
            System.out.println("在索引 " + result3 + " 处找到");
        } else {
            System.out.println("未找到");
        }

        // 测试查找最后一个元素
        int target4 = 47;
        int result4 = interpolationSearch(arr, target4);
        System.out.println("\n查找 " + target4 + ":");
        if (result4 != -1) {
            System.out.println("在索引 " + result4 + " 处找到");
        } else {
            System.out.println("未找到");
        }

        // 测试递归版本
        System.out.println("\n使用递归版本查找 " + target1 + ":");
        int result5 = interpolationSearchRecursive(arr, target1, 0, arr.length - 1);
        if (result5 != -1) {
            System.out.println("在索引 " + result5 + " 处找到");
        } else {
            System.out.println("未找到");
        }

        // 性能比较测试
        System.out.println("\n性能测试:");
        long startTime, endTime;
        
        // 测试插值查找性能
        startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            interpolationSearch(arr, target1);
        }
        endTime = System.nanoTime();
        System.out.println("插值查找 100000 次耗时: " + (endTime - startTime) / 1000000.0 + " ms");
        
        // 测试线性查找性能作为对比
        startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (arr[j] == target1) {
                    break;
                }
            }
        }
        endTime = System.nanoTime();
        System.out.println("线性查找 100000 次耗时: " + (endTime - startTime) / 1000000.0 + " ms");
    }
}





















/*

插值查找中计算插值位置的原理基于数学上的线性插值概念。让我详细解释一下：

        ### 插值位置计算原理

插值查找中的位置计算公式如下：
        ```
pos = left + ((target - arr[left]) * (right - left)) / (arr[right] - arr[left])
        ```


        #### 数学推导过程：

这个公式的本质是假设数组中的元素近似呈线性分布。我们可以这样理解：

        1. **线性关系假设**：
        - 假设数组中元素的值与索引之间存在近似的线性关系
   - 即：元素值随着索引线性增长

2. **比例关系**：
        ```
        (target - arr[left]) / (arr[right] - arr[left]) ≈ (pos - left) / (right - left)
        ```


        3. **解方程得**：
        ```
pos - left = (target - arr[left]) * (right - left) / (arr[right] - arr[left])

pos = left + (target - arr[left]) * (right - left) / (arr[right] - arr[left])
        ```


        #### 实际例子：

假设我们有数组：[10, 15, 20, 25, 30]，我们要查找 22

        - left = 0, arr[left] = 10
        - right = 4, arr[right] = 30
        - target = 22

按公式计算：
        ```
pos = 0 + (22 - 10) * (4 - 0) / (30 - 10)
pos = 0 + 12 * 4 / 20
pos = 0 + 48 / 20
pos = 2.4 ≈ 2
        ```


所以插值查找会先检查索引 2 的位置，该位置的值是 20，因为 22 > 20，所以下一次会在右侧区间 [3, 4] 中继续查找。

        #### 优势：

        1. **更快收敛**：对于均匀分布的数据，插值查找能够更准确地预测目标元素的位置，因此通常比二分查找需要更少的比较次数。

        2. **自适应性**：根据数据的实际分布情况动态调整查找位置。

        #### 注意事项：

        1. **数据分布要求**：只有在数据分布相对均匀的情况下，插值查找的效果才会比二分查找好。

        2. **整数除法问题**：由于使用整数除法，可能会出现精度损失。

        3. **最坏情况**：如果数据分布极不均匀（例如指数级增长），插值查找的时间复杂度可能退化到 O(n)。

总的来说，插值查找通过数学方法估算目标元素的位置，使得查找更加智能化，特别适合处理大量均匀分布的数据。*/
