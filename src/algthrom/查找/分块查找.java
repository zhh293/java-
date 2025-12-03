package algthrom.查找;

import java.util.ArrayList;
import java.util.List;

/**
 * 分块查找算法实现类
 * 分块查找又称索引顺序查找，是介于顺序查找和二分查找之间的一种查找方法
 * 它将数组分成若干块，块内元素可以无序，但块间必须有序（前一块的最大值小于后一块的最小值）
 */
public class 分块查找 {

    /**
     * 块信息类，用于存储每块的索引信息
     */
    static class Block {
        int startIndex;  // 块的起始索引
        int endIndex;    // 块的结束索引
        int maxValue;    // 块内的最大值
        
        Block(int startIndex, int endIndex, int maxValue) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.maxValue = maxValue;
        }
    }

    /**
     * 构建分块索引
     *
     * @param arr 已分块的数组（块内无序，块间有序）
     * @param blockSize 每块的大小
     * @return 块索引列表
     */
    public static List<Block> buildBlockIndex(int[] arr, int blockSize) {
        List<Block> blocks = new ArrayList<>();
        
        for (int i = 0; i < arr.length; i += blockSize) {
            int start = i;
            int end = Math.min(i + blockSize - 1, arr.length - 1);
            
            // 找到当前块的最大值
            int max = arr[start];
            for (int j = start + 1; j <= end; j++) {
                if (arr[j] > max) {
                    max = arr[j];
                }
            }
            
            blocks.add(new Block(start, end, max));
        }
        
        return blocks;
    }

    /**
     * 分块查找算法
     *
     * @param arr 已分块的数组（块内无序，块间有序）
     * @param blocks 块索引列表
     * @param target 要查找的目标值
     * @return 目标值在数组中的索引，如果未找到则返回-1
     */
    public static int blockSearch(int[] arr, List<Block> blocks, int target) {
        // 第一步：在索引表中查找目标值所在的块
        int blockIndex = -1;
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            // 如果目标值小于等于当前块的最大值，则在当前块中查找
            if (target <= block.maxValue) {
                blockIndex = i;
                break;
            }
        }
        
        // 如果没有找到合适的块，说明目标值不在数组中
        if (blockIndex == -1) {
            return -1;
        }
        
        // 第二步：在确定的块内进行顺序查找
        Block targetBlock = blocks.get(blockIndex);
        for (int i = targetBlock.startIndex; i <= targetBlock.endIndex; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        
        // 在块内未找到目标值
        return -1;
    }

    /**
     * 另一种实现方式的分块查找（使用二分查找在索引表中定位块）
     *
     * @param arr 已分块的数组（块内无序，块间有序）
     * @param blocks 块索引列表（按照maxValue有序）
     * @param target 要查找的目标值
     * @return 目标值在数组中的索引，如果未找到则返回-1
     */
    public static int blockSearchOptimized(int[] arr, List<Block> blocks, int target) {
        // 第一步：使用二分查找在索引表中查找目标值所在的块
        int blockIndex = binarySearchBlocks(blocks, target);
        
        // 如果没有找到合适的块，说明目标值不在数组中
        if (blockIndex == -1) {
            return -1;
        }
        
        // 第二步：在确定的块内进行顺序查找
        Block targetBlock = blocks.get(blockIndex);
        for (int i = targetBlock.startIndex; i <= targetBlock.endIndex; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        
        // 在块内未找到目标值
        return -1;
    }

    /**
     * 在块索引中使用二分查找定位目标值所在的块
     *
     * @param blocks 块索引列表（按照maxValue有序）
     * @param target 要查找的目标值
     * @return 目标值所在的块索引，如果未找到则返回-1
     */
    private static int binarySearchBlocks(List<Block> blocks, int target) {
        int left = 0;
        int right = blocks.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            Block midBlock = blocks.get(mid);
            
            if (target <= midBlock.maxValue) {
                // 检查是否是第一个满足条件的块
                if (mid == 0 || target > blocks.get(mid - 1).maxValue) {
                    return mid;
                }
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return -1;
    }

    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 创建一个分块数组（块大小为4）
        // 块1: [21, 13, 19, 15] 最大值21
        // 块2: [25, 33, 27, 29] 最大值33  
        // 块3: [37, 45, 39, 41] 最大值45
        // 块4: [51, 49, 53]      最大值53
        // 块间满足有序性：21 < 33 < 45 < 53
        int[] arr = {21, 13, 19, 15, 25, 33, 27, 29, 37, 45, 39, 41, 51, 49, 53};
        int blockSize = 4;
        
        System.out.println("测试数组（按块显示）:");
        System.out.println("块1: [21, 13, 19, 15]");
        System.out.println("块2: [25, 33, 27, 29]");
        System.out.println("块3: [37, 45, 39, 41]");
        System.out.println("块4: [51, 49, 53]");
        System.out.println("注意：块内元素无序，但块间有序（各块最大值递增）\n");
        
        // 构建分块索引
        List<Block> blocks = buildBlockIndex(arr, blockSize);
        System.out.println("构建的分块索引:");
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            System.out.println("块" + (i+1) + ": 起始索引=" + block.startIndex + 
                             ", 结束索引=" + block.endIndex + 
                             ", 最大值=" + block.maxValue);
        }
        System.out.println();
        
        // 测试查找存在的元素
        int target1 = 33;
        int result1 = blockSearch(arr, blocks, target1);
        System.out.println("分块查找 " + target1 + ":");
        if (result1 != -1) {
            System.out.println("在索引 " + result1 + " 处找到");
        } else {
            System.out.println("未找到");
        }
        
        // 测试查找不存在的元素
        int target2 = 34;
        int result2 = blockSearch(arr, blocks, target2);
        System.out.println("\n分块查找 " + target2 + ":");
        if (result2 != -1) {
            System.out.println("在索引 " + result2 + " 处找到");
        } else {
            System.out.println("未找到");
        }
        
        // 测试查找第一个块的元素
        int target3 = 13;
        int result3 = blockSearch(arr, blocks, target3);
        System.out.println("\n分块查找 " + target3 + ":");
        if (result3 != -1) {
            System.out.println("在索引 " + result3 + " 处找到");
        } else {
            System.out.println("未找到");
        }
        
        // 测试查找最后一个块的元素
        int target4 = 53;
        int result4 = blockSearch(arr, blocks, target4);
        System.out.println("\n分块查找 " + target4 + ":");
        if (result4 != -1) {
            System.out.println("在索引 " + result4 + " 处找到");
        } else {
            System.out.println("未找到");
        }
        
        // 使用优化版本测试
        System.out.println("\n使用优化版本（二分查找确定块）查找 " + target1 + ":");
        int result5 = blockSearchOptimized(arr, blocks, target1);
        if (result5 != -1) {
            System.out.println("在索引 " + result5 + " 处找到");
        } else {
            System.out.println("未找到");
        }
        
        // 性能测试
        System.out.println("\n性能测试:");
        long startTime, endTime;
        
        // 测试普通分块查找性能
        startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            blockSearch(arr, blocks, target1);
        }
        endTime = System.nanoTime();
        System.out.println("分块查找 100000 次耗时: " + (endTime - startTime) / 1000000.0 + " ms");
        
        // 测试优化版分块查找性能
        startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            blockSearchOptimized(arr, blocks, target1);
        }
        endTime = System.nanoTime();
        System.out.println("优化版分块查找 100000 次耗时: " + (endTime - startTime) / 1000000.0 + " ms");
    }
}