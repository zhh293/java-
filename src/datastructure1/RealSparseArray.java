package datastructure1;
import java.util.*;
public class RealSparseArray {



    /*关键特性说明
    内存效率：只存储非零元素，大大节省内存空间
    计算优化：矩阵运算时只处理非零元素，提高计算效率
    实用性：支持真实的矩阵操作，如乘法、转置等
    可扩展性：基于哈希表的实现，支持动态添加和删除元素*/
    public static void main(String[] args) {
        // 创建一个5x5的稀疏矩阵
        SparseMatrix matrix = new SparseMatrix(5, 5);

        // 设置一些非零元素
        matrix.set(0, 1, 3.0);
        matrix.set(0, 3, 5.0);
        matrix.set(1, 2, 2.0);
        matrix.set(2, 0, 1.0);
        matrix.set(2, 4, 4.0);
        matrix.set(4, 1, 6.0);
        matrix.set(4, 3, 7.0);

        System.out.println("原始矩阵:");
        System.out.println(matrix);

        // 转置矩阵
        SparseMatrix transposed = matrix.transpose();
        System.out.println("\n转置矩阵:");
        System.out.println(transposed);

        // 矩阵乘法示例
        SparseMatrix matrix2 = new SparseMatrix(5, 3);
        matrix2.set(0, 0, 1.0);
        matrix2.set(1, 1, 1.0);
        matrix2.set(2, 2, 1.0);
        matrix2.set(3, 0, 1.0);
        matrix2.set(4, 1, 1.0);

        System.out.println("\n第二个矩阵:");
        System.out.println(matrix2);

        SparseMatrix product = matrix.multiply(matrix2);
        System.out.println("\n矩阵相乘结果:");
        System.out.println(product);
    }


}





/**
 * 真正实用的稀疏数组实现 - 用于存储大型稀疏矩阵
 */
class SparseMatrix {
    private final int rows;
    private final int cols;
    private final Map<Coordinate, Double> values;

    public SparseMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.values = new HashMap<>();
    }

    /**
     * 设置矩阵元素值
     * @param row 行索引
     * @param col 列索引
     * @param value 值
     */
    public void set(int row, int col, double value) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        if (value == 0.0) {
            // 对于零值，从存储中移除以节省空间
            values.remove(new Coordinate(row, col));
        } else {
            values.put(new Coordinate(row, col), value);
        }
    }

    /**
     * 获取矩阵元素值
     * @param row 行索引
     * @param col 列索引
     * @return 元素值
     */
    public double get(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        return values.getOrDefault(new Coordinate(row, col), 0.0);
    }

    /**
     * 矩阵乘法运算
     * @param other 另一个矩阵
     * @return 相乘结果
     */
    public SparseMatrix multiply(SparseMatrix other) {
        if (this.cols != other.rows) {
            throw new IllegalArgumentException("Matrix dimensions don't match for multiplication");
        }

        SparseMatrix result = new SparseMatrix(this.rows, other.cols);

        // 遍历当前矩阵的所有非零元素
        for (Map.Entry<Coordinate, Double> entry : this.values.entrySet()) {
            Coordinate pos = entry.getKey();
            double value = entry.getValue();

            int row = pos.row;
            // 计算与另一矩阵对应行的乘积
            for (int k = 0; k < other.cols; k++) {
                double otherValue = other.get(pos.col, k);
                if (otherValue != 0) {
                    double product = value * otherValue;
                    double existing = result.get(row, k);
                    result.set(row, k, existing + product);
                }
            }
        }

        return result;
    }

    /**
     * 转置矩阵
     * @return 转置后的矩阵
     */
    public SparseMatrix transpose() {
        SparseMatrix transposed = new SparseMatrix(cols, rows);

        for (Map.Entry<Coordinate, Double> entry : values.entrySet()) {
            Coordinate pos = entry.getKey();
            double value = entry.getValue();
            transposed.set(pos.col, pos.row, value);
        }

        return transposed;
    }

    /**
     * 获取非零元素数量
     * @return 非零元素个数
     */
    public int nonZeroCount() {
        return values.size();
    }

    /**
     * 获取矩阵密度(非零元素比例)
     * @return 密度值
     */
    public double density() {
        return (double) nonZeroCount() / (rows * cols);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("SparseMatrix[%d x %d] (density: %.2f%%)\n",
                rows, cols, density() * 100));

        // 按行列顺序输出非零元素
        List<Map.Entry<Coordinate, Double>> sortedEntries =
                new ArrayList<>(values.entrySet());
        sortedEntries.sort(Comparator.comparing(e -> e.getKey()));

        for (Map.Entry<Coordinate, Double> entry : sortedEntries) {
            Coordinate pos = entry.getKey();
            sb.append(String.format("(%d,%d) = %.2f\n", pos.row, pos.col, entry.getValue()));
        }

        return sb.toString();
    }

    /**
     * 坐标类，用于作为Map的键
     */
    private static class Coordinate implements Comparable<Coordinate> {
        final int row;
        final int col;

        Coordinate(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Coordinate)) return false;
            Coordinate that = (Coordinate) obj;
            return row == that.row && col == that.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }

        @Override
        public int compareTo(Coordinate other) {
            if (this.row != other.row) {
                return Integer.compare(this.row, other.row);
            }
            return Integer.compare(this.col, other.col);
        }
    }
}

