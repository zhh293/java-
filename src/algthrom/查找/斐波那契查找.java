package algthrom.查找;

import java.util.Arrays;

/**
 * 斐波那契查找算法实现类
 * 斐波那契查找是二分查找的一种改进算法，利用斐波那契数列的特性来分割数组
 * 相比二分查找，斐波那契查找的优点在于它只涉及加减运算，而不涉及乘除运算
 */
public class 斐波那契查找 {

    /**
     * 生成斐波那契数列
     *
     * @param length 需要生成的斐波那契数列长度
     * @return 斐波那契数列数组
     */
    private static int[] generateFibonacci(int length) {
        int[] fib = new int[length];
        fib[0] = 0;
        fib[1] = 1;
        
        for (int i = 2; i < length; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
        }
        
        return fib;
    }

    /**
     * 斐波那契查找算法
     *
     * @param arr 已排序的数组（升序）
     * @param target 要查找的目标值
     * @return 目标值在数组中的索引，如果未找到则返回-1
     */
    public static int fibonacciSearch(int[] arr, int target) {
        int n = arr.length;
        
        // 生成斐波那契数列
        int[] fib = generateFibonacci(20); // 生成足够长的斐波那契数列
        
        // 找到第一个大于等于数组长度的斐波那契数
        int k = 0;
        while (fib[k] < n) {
            k++;
        }
        
        // 创建临时数组，长度为fib[k]，不足的部分用数组最后一个元素填充
        int[] temp = Arrays.copyOf(arr, fib[k]);
        for (int i = n; i < temp.length; i++) {
            temp[i] = arr[n - 1];
        }
        
        int left = 0;
        int right = n - 1;
        
        // 进行斐波那契查找
        while (left <= right) {
            // 计算分割点
            int mid = left + fib[k - 1] - 1;
            
            // 确保mid不超过right边界
            if (mid > right) {
                mid = right;
            }
            
            if (temp[mid] == target) {
                // 如果找到的元素在原数组范围内，直接返回索引
                if (mid < n) {
                    return mid;
                } 
                // 如果找到的元素是填充的元素，则返回right
                else {
                    return right;
                }
            } else if (temp[mid] < target) {
                // 在右半部分查找
                left = mid + 1;
                k -= 2;
            } else {
                // 在左半部分查找
                right = mid - 1;
                k -= 1;
            }
            
            if (k < 2) {
                break;
            }
        }
        
        // 检查边界情况
        if (left == right && arr[left] == target) {
            return left;
        }
        
        return -1; // 未找到目标值
    }

    /**
     * 另一种实现方式的斐波那契查找
     *
     * @param arr 已排序的数组（升序）
     * @param target 要查找的目标值
     * @return 目标值在数组中的索引，如果未找到则返回-1
     */
    public static int fibonacciSearchAlternative(int[] arr, int target) {
        int n = arr.length;
        
        // 生成斐波那契数列
        int[] fib = new int[20];
        fib[0] = 0;
        fib[1] = 1;
        for (int i = 2; i < 20; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
        }
        
        // 找到最小的斐波那契数，使其大于等于数组长度
        int k = 0;
        while (fib[k] < n) {
            k++;
        }
        
        // 如果斐波那契数恰好等于数组长度
        if (fib[k] == n) {
            k--;
        }
        
        int left = 0;
        int right = n - 1;
        
        while (k >= 0 && left <= right) {
            // 计算黄金分割点
            int mid = left + fib[k] - 1;
            
            // 确保mid不超过数组边界
            if (mid > right) {
                mid = right;
            }
            
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] > target) {
                // 在左侧部分查找，k减少1
                right = mid - 1;
                k -= 1;
            } else {
                // 在右侧部分查找，k减少2
                left = mid + 1;
                k -= 2;
            }
        }
        
        return -1; // 未找到目标值
    }

    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 创建一个已排序的测试数组
        int[] arr = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35};
        
        System.out.println("测试数组:");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println("\n");
        
        // 测试查找存在的元素
        int target1 = 13;
        int result1 = fibonacciSearch(arr, target1);
        System.out.println("斐波那契查找 " + target1 + ":");
        if (result1 != -1) {
            System.out.println("在索引 " + result1 + " 处找到");
        } else {
            System.out.println("未找到");
        }
        
        // 测试查找不存在的元素
        int target2 = 14;
        int result2 = fibonacciSearch(arr, target2);
        System.out.println("\n斐波那契查找 " + target2 + ":");
        if (result2 != -1) {
            System.out.println("在索引 " + result2 + " 处找到");
        } else {
            System.out.println("未找到");
        }
        
        // 测试查找第一个元素
        int target3 = 1;
        int result3 = fibonacciSearch(arr, target3);
        System.out.println("\n斐波那契查找 " + target3 + ":");
        if (result3 != -1) {
            System.out.println("在索引 " + result3 + " 处找到");
        } else {
            System.out.println("未找到");
        }
        
        // 测试查找最后一个元素
        int target4 = 35;
        int result4 = fibonacciSearch(arr, target4);
        System.out.println("\n斐波那契查找 " + target4 + ":");
        if (result4 != -1) {
            System.out.println("在索引 " + result4 + " 处找到");
        } else {
            System.out.println("未找到");
        }
        
        // 使用另一种实现方式测试
        System.out.println("\n使用另一种实现方式查找 " + target1 + ":");
        int result5 = fibonacciSearchAlternative(arr, target1);
        if (result5 != -1) {
            System.out.println("在索引 " + result5 + " 处找到");
        } else {
            System.out.println("未找到");
        }
        
        // 性能测试
        System.out.println("\n性能测试:");
        long startTime, endTime;
        
        // 测试斐波那契查找性能
        startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            fibonacciSearch(arr, target1);
        }
        endTime = System.nanoTime();
        System.out.println("斐波那契查找 100000 次耗时: " + (endTime - startTime) / 1000000.0 + " ms");
        
        // 测试二分查找性能作为对比
        startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            binarySearch(arr, target1);
        }
        endTime = System.nanoTime();
        System.out.println("二分查找 100000 次耗时: " + (endTime - startTime) / 1000000.0 + " ms");
    }
    
    /**
     * 二分查找实现，用于性能对比
     */
    public static int binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return -1;
    }
}