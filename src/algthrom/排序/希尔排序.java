package algthrom.排序;


/**
 * 希尔排序（Shell Sort）
 *
 * 希尔排序是插入排序的一种改进版本，也称为"缩小增量排序"。
 * 它通过将原始数组按照一定的间隔（增量）分成若干子序列，
 * 对每个子序列进行插入排序，然后逐步缩小间隔，最后当间隔为1时，
 * 对整个数组进行一次插入排序。
 */
public class 希尔排序 {

    /**
     * 希尔排序算法实现
     *
     * @param arr 待排序的整型数组
     */
    public static void shellSort(int[] arr) {
        int n = arr.length;

        // 初始间隔设为数组长度的一半，每次缩小一半
        for (int gap = n / 2; gap > 0; gap /= 2) {
            // 对每个分组进行插入排序
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j = i;

                // 在分组内进行插入排序
                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                arr[j] = temp;
            }
        }
    }

    /**
     * 希尔排序优化版本 - 使用Knuth序列
     *
     * @param arr 待排序的整型数组
     */
    public static void shellSortKnuth(int[] arr) {
        int n = arr.length;

        // 计算最大的Knuth序列值 (3^k - 1) / 2
        int gap = 1;
        while (gap < n / 3) {
            gap = gap * 3 + 1; // Knuth序列: 1, 4, 13, 40, 121, ...
        }

        // 从最大间隔开始，逐步缩小
        while (gap >= 1) {
            // 对每个分组进行插入排序
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j = i;

                // 在分组内进行插入排序
                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                arr[j] = temp;
            }
            gap /= 3;
        }
    }

    /**
     * 打印数组元素
     *
     * @param arr 要打印的数组
     */
    public static void printArray(int[] arr) {
        for (int value : arr) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    /**
     * 测试希尔排序算法
     */
    public static void main(String[] args) {
        // 测试用例1: 一般情况
        int[] arr1 = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("原数组:");
        printArray(arr1);

        shellSort(arr1);
        System.out.println("希尔排序后:");
        printArray(arr1);

        // 测试用例2: 已排序数组
        int[] arr2 = {1, 2, 3, 4, 5, 6, 7};
        System.out.println("\n已排序数组:");
        printArray(arr2);

        shellSort(arr2);
        System.out.println("希尔排序后:");
        printArray(arr2);

        // 测试用例3: 逆序数组
        int[] arr3 = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        System.out.println("\n逆序数组:");
        printArray(arr3);

        shellSort(arr3);
        System.out.println("希尔排序后:");
        printArray(arr3);

        // 测试用例4: 包含重复元素
        int[] arr4 = {5, 2, 8, 2, 9, 1, 5, 5};
        System.out.println("\n包含重复元素的数组:");
        printArray(arr4);

        shellSortKnuth(arr4);
        System.out.println("使用Knuth序列的希尔排序后:");
        printArray(arr4);
    }
}

