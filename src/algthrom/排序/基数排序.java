package algthrom.排序;

import java.util.Arrays;

public class 基数排序 {
    // 基数排序从个位数开始比较，从个位数一直比较到最高位
    // 在每一轮都对数组中的元素进行排序
    // 最后一定可以排序成功
    
    /**
     * 基数排序主方法
     * @param arr 待排序的数组
     */
    public static void radixSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        
        // 找到数组中的最大值
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        
        // 对每一位进行计数排序
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSort(arr, exp);
            System.out.println("按第" + exp + "位排序后: " + Arrays.toString(arr));
        }
    }
    
    /**
     * 按指定位数进行计数排序
     * @param arr 待排序数组
     * @param exp 位数(1表示个位, 10表示十位, 100表示百位...)
     */
    public static void countingSort(int[] arr, int exp) {
        int[] output = new int[arr.length];
        int[] count = new int[10];
        
        // 统计每个数字(0-9)出现的次数
        for (int i = 0; i < arr.length; i++) {
            count[(arr[i] / exp) % 10]++;
        }
        
        // 计算累积计数
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }
        
        // 从后向前构建输出数组，保证排序的稳定性
        for (int i = arr.length - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }
        
        // 将排序后的结果复制回原数组
        for (int i = 0; i < arr.length; i++) {
            arr[i] = output[i];
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        int[] arr = {170, 45, 75, 90, 2, 802, 24, 66};
        System.out.println("原始数组: " + Arrays.toString(arr));
        radixSort(arr);
        System.out.println("最终排序结果: " + Arrays.toString(arr));
    }
}