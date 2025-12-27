#include <stdio.h>
#include <stdlib.h>

/**
 * 顺序表朴素查找 - 线性查找
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 */
int sequentialSearch(int arr[], int n, int target) {
    for (int i = 0; i < n; i++) {
        if (arr[i] == target) {
            return i;  // 返回找到的元素索引
        }
    }
    return -1;  // 未找到返回-1
}

/**
 * 折半查找 - 二分查找（要求数组已排序）
 * 时间复杂度：O(log n)
 * 空间复杂度：O(1)
 */
int binarySearch(int arr[], int n, int target) {
    int left = 0;
    int right = n - 1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;  // 防止整数溢出
        
        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return -1;  // 未找到返回-1
}

/**
 * 插值查找（要求数组已排序且元素分布相对均匀）
 * 时间复杂度：平均O(log log n)，最坏O(n)
 * 空间复杂度：O(1)
 */
 //插值查找算是折半查找的变种，它不会盲目的每次都从数组的中间寻找，而是根据目标值和数组的分布，计算出更接近目标值的位置，然后缩小搜索范围。
 //但是这就要求数组的元素要尽量均匀分布，不能跨度太大
int interpolationSearch(int arr[], int n, int target) {
    int left = 0;
    int right = n - 1;
    
    while (left <= right && target >= arr[left] && target <= arr[right]) {
        // 如果左右边界相等，检查是否为目标值
        if (left == right) {
            if (arr[left] == target) {
                return left;
            } else {
                return -1;
            }
        }
        
        // 计算插值位置
        int pos = left + ((double)(target - arr[left]) / (arr[right] - arr[left])) * (right - left);
        
        // 检查计算的位置是否有效
        if (pos < left || pos > right) {
            return -1;
        }
        
        if (arr[pos] == target) {
            return pos;
        } else if (arr[pos] < target) {
            left = pos + 1;
        } else {
            right = pos - 1;
        }
    }
    
    return -1;  // 未找到返回-1
}

/**
 * 计算斐波那契数列，用于斐波那契查找
 */
int* generateFibonacci(int n) {
    int* fib = (int*)malloc(n * sizeof(int));
    if (n >= 1) fib[0] = 1;
    if (n >= 2) fib[1] = 1;
    
    for (int i = 2; i < n; i++) {
        fib[i] = fib[i-1] + fib[i-2];
    }
    
    return fib;
}

/**
 * 斐波那契查找（要求数组已排序）
 * 时间复杂度：O(log n)
 * 空间复杂度：O(log n) - 用于存储斐波那契数列
 */
int fibonacciSearch(int arr[], int n, int target) {
    // 生成斐波那契数列，找到第一个大于等于n的斐波那契数
    int* fib = generateFibonacci(20);  // 假设最多需要20个斐波那契数
    int fibIndex = 0;
    while (fib[fibIndex] < n) {
        fibIndex++;
    }
    
    int left = 0;
    int right = n - 1;
    
    while (left <= right) {
        // 计算分割点
        int k = (fibIndex >= 2) ? fibIndex - 2 : 0;
        int mid = left + ((right >= left) ? (fib[k] - 1) : 0);
        
        if (mid >= n) {
            // mid超出了数组范围，调整
            mid = right;
        }
        
        if (arr[mid] == target) {
            free(fib);
            return mid;
        } else if (arr[mid] < target) {
            left = mid + 1;
            fibIndex = (fibIndex >= 1) ? fibIndex - 1 : 0;
        } else {
            right = mid - 1;
            fibIndex = (fibIndex >= 2) ? fibIndex - 2 : 0;
        }
    }
    
    free(fib);
    return -1;  // 未找到返回-1
}

/**
 * 测试函数
 */
void testSearchAlgorithms() {
    int arr[] = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19};
    int n = sizeof(arr) / sizeof(arr[0]);
    int target = 7;
    
    printf("数组: ");
    for (int i = 0; i < n; i++) {
        printf("%d ", arr[i]);
    }
    printf("\n");
    printf("查找目标: %d\n", target);
    
    // 测试顺序查找
    int result = sequentialSearch(arr, n, target);
    printf("顺序查找结果: %d\n", result);
    
    // 测试二分查找
    result = binarySearch(arr, n, target);
    printf("二分查找结果: %d\n", result);
    
    // 测试插值查找
    result = interpolationSearch(arr, n, target);
    printf("插值查找结果: %d\n", result);
    
    // 测试斐波那契查找
    result = fibonacciSearch(arr, n, target);
    printf("斐波那契查找结果: %d\n", result);
    
    // 测试未找到的情况
    target = 8;
    printf("\n查找目标: %d (不存在)\n", target);
    printf("顺序查找结果: %d\n", sequentialSearch(arr, n, target));
    printf("二分查找结果: %d\n", binarySearch(arr, n, target));
    printf("插值查找结果: %d\n", interpolationSearch(arr, n, target));
    printf("斐波那契查找结果: %d\n", fibonacciSearch(arr, n, target));
}

/**
 * 主函数
 */
int main() {
    testSearchAlgorithms();
    return 0;
}