package algthrom;

import java.util.Arrays;

public class 迪杰斯特拉算法的说明和理解 {
    /**
     * 图的顶点数量
     */
    private int vertices;

    /**
     * 邻接矩阵表示图
     */
    private int[][] adjacencyMatrix;

    /**
     * 构造函数
     * @param vertices 顶点数
     */
    public 迪杰斯特拉算法的说明和理解(int vertices) {
        this.vertices = vertices;
        this.adjacencyMatrix = new int[vertices][vertices];

        // 初始化邻接矩阵，用Integer.MAX_VALUE表示无连接
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (i == j) {
                    adjacencyMatrix[i][j] = 0;
                } else {
                    adjacencyMatrix[ i][j] = Integer.MAX_VALUE;
                }
            }
        }
    }

    /**
     * 添加边到图中
     * @param source 起始顶点
     * @param destination 目标顶点
     * @param weight 边的权重
     */
    public void addEdge(int source, int destination, int weight) {
        if (source >= 0 && source < vertices &&
                destination >= 0 && destination < vertices) {
            adjacencyMatrix[source][destination] = weight;
        }
    }

    /**
     * Dijkstra算法核心实现
     * @param source 源顶点
     * @return 从源顶点到各顶点最短距离数组
     */
    public int[] dijkstra(int source) {
        // 存储从源顶点到各顶点最短距离
        int[] distances = new int[vertices];

        // 记录顶点是否已被处理
        boolean[] processed = new boolean[vertices];

        // 初始化距离数组
        Arrays.fill(distances, Integer.MAX_VALUE);
        Arrays.fill(processed, false);

        // 源顶点到自身的距离为0
        distances[source] = 0;

        // 对每个顶点进行处理
        for (int count = 0; count < vertices; count++) {
            //准备找距离source最近的顶点
            //找到距离原点最近的点
            int u = minDistance(distances, processed, source);
        }
    }

    private int minDistance(int[] distances, boolean[] processed, int source) {
        //遍历邻接矩阵


        return 0;
    }

}
