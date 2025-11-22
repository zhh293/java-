package algthrom;

import java.util.*;

/*
离散化算法的核心思想
数据压缩: 将大范围的数据映射到小范围内
保持相对关系: 映射后元素间的大小关系不变
节省空间和时间: 减少内存占用，提高算法效率
        经典应用场景
坐标离散化: 处理大规模坐标点，如平面扫描线算法
数值离散化: 将大整数映射为小整数索引
区间问题: 线段树、树状数组等数据结构的预处理
*/

public class 离散化 {
    /**
     * 对数组进行离散化处理
     * @param nums 原始数据数组
     * @return 离散化映射器
     */
    public static Discretizer discretize(int[] nums) {
        return new Discretizer(nums);
    }

    /**
     * 离散化映射器类
     */
    public static class Discretizer {
        private Map<Integer, Integer> originalToDiscrete;  // 原值->离散值映射
        private int[] discreteToOriginal;                  // 离散值->原值映射

        public Discretizer(int[] nums) {
            // 去重并排序
            TreeSet<Integer> uniqueValues = new TreeSet<>();
            for (int num : nums) {
                uniqueValues.add(num);
            }

            // 建立映射关系
            originalToDiscrete = new HashMap<>();
            discreteToOriginal = new int[uniqueValues.size()];

            int index = 0;
            for (Integer value : uniqueValues) {
                originalToDiscrete.put(value, index);
                discreteToOriginal[index] = value;
                index++;
            }
        }

        /**
         * 将原始值转换为离散值
         * @param original 原始值
         * @return 离散值（从0开始）
         */
        public int getDiscreteValue(int original) {
            return originalToDiscrete.getOrDefault(original, -1);
        }

        /**
         * 将离散值转换为原始值
         * @param discrete 离散值
         * @return 原始值
         */
        public int getOriginalValue(int discrete) {
            if (discrete < 0 || discrete >= discreteToOriginal.length) {
                throw new IndexOutOfBoundsException("Invalid discrete value");
            }
            return discreteToOriginal[discrete];
        }

        /**
         * 获取离散化后的值域大小
         * @return 值域大小
         */
        public int size() {
            return discreteToOriginal.length;
        }
    }

    /**
     * 区间离散化工具 - 处理区间端点
     */
    public static class IntervalDiscretizer {
        private Discretizer discretizer;

        public IntervalDiscretizer(List<int[]> intervals) {
            Set<Integer> points = new HashSet<>();
            for (int[] interval : intervals) {
                points.add(interval[0]);  // 区间左端点
                points.add(interval[1]);  // 区间右端点
            }

            int[] pointArray = points.stream().mapToInt(Integer::intValue).toArray();
            this.discretizer = new Discretizer(pointArray);
        }

        public int discretizePoint(int point) {
            return discretizer.getDiscreteValue(point);
        }

        public int restorePoint(int discretePoint) {
            return discretizer.getOriginalValue(discretePoint);
        }

        public int size() {
            return discretizer.size();
        }
    }
}




public class DiscretizationExample {
    public static void main(String[] args) {
        // 示例1: 数值离散化
        int[] data = {1000000, 3000000, 1000000, 5000000, 2000000};
        离散化.Discretizer discretizer = 离散化.discretize(data);

        System.out.println("原始数据: " + Arrays.toString(data));
        System.out.println("离散化后:");
        for (int i = 0; i < discretizer.size(); i++) {
            System.out.println("离散值 " + i + " -> 原始值 " + discretizer.getOriginalValue(i));
        }

        // 映射原始数据到离散值
        System.out.print("数据映射: ");
        for (int value : data) {
            System.out.print(discretizer.getDiscreteValue(value) + " ");
        }
        System.out.println();

        // 示例2: 区间离散化
        List<int[]> intervals = Arrays.asList(
                new int[]{100, 200},
                new int[]{150, 300},
                new int[]{250, 400}
        );

        离散化.IntervalDiscretizer intervalDiscretizer =
                new 离散化.IntervalDiscretizer(intervals);

        System.out.println("\n区间离散化:");
        for (int[] interval : intervals) {
            int left = intervalDiscretizer.discretizePoint(interval[0]);
            int right = intervalDiscretizer.discretizePoint(interval[1]);
            System.out.printf("[%d,%d] -> [%d,%d]\n",
                    interval[0], interval[1], left, right);
        }
    }
}


/*
算法优势
空间优化: 将大范围数据压缩到小范围索引
性能提升: 在离散值上操作通常比原始值更快
通用性强: 适用于多种数据结构和算法场景*/
