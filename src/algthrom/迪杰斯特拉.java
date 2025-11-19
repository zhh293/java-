package algthrom;

import java.util.*;

/**
 * Dijkstra最短路径算法实现
 * 用于计算从源顶点到图中所有其他顶点的最短路径
 */


/*Dijkstra算法（迪杰斯特拉算法）是一种用于解决单源最短路径问题的经典算法。
        主要用途
        计算最短路径：从图中某个顶点（源点）到其他各顶点的最短路径
        适用于有权图：处理带非负权重的有向图或无向图
        核心特点
        贪心策略：每次选择距离源点最近且未访问的顶点
        逐步扩展：从源点开始，逐层向外扩展，更新到达各顶点的最短距离
        非负权重：要求图中所有边的权重都为非负数
        常见应用场景
        网络路由协议
                地图导航系统
        社交网络分析
                交通运输路径规划
        算法复杂度
        时间复杂度：O(V²)，使用优先队列可优化至 O(E + V log V)
        空间复杂度：O(V)
        其中 V 表示顶点数量，E 表示边的数量。
        该算法通过维护一个距离数组和已访问标记数组，不断更新源点到各个顶点的最短距离，直到找到到达所有顶点的最短路径*/




public class 迪杰斯特拉 {

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
    public 迪杰斯特拉(int vertices) {
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
     * @return 从源顶点到各顶点的最短距离数组*/

    public int[] dijkstra(int source) {
        // 存储从源顶点到各顶点的最短距离
        int[] distances = new int[vertices];

        // 记录顶点是否已被处理
        boolean[] processed = new boolean[vertices];

        // 初始化距离数组
        Arrays.fill(distances, Integer.MAX_VALUE);
        Arrays.fill(processed, false);

        // 源顶点到自身的距离为0
        distances[source] = 0;

        // 对每个顶点进行处理
        for (int count = 0; count < vertices - 1; count++) {
            // 找到未处理顶点中距离最小的顶点
            int u = minDistance(distances, processed);

            // 标记该顶点已处理
            processed[u] = true;

            // 更新与顶点u相邻的顶点的距离值
            for (int v = 0; v < vertices; v++) {
                // 如果顶点v未被处理，且存在从u到v的边，
                // 且通过u可以获得更短路径，则更新距离
                if (!processed[v] &&
                        adjacencyMatrix[u][v] != Integer.MAX_VALUE &&
                        distances[u] != Integer.MAX_VALUE &&
                        distances[u] + adjacencyMatrix[u][v] < distances[v]) {

                    distances[v] = distances[u] + adjacencyMatrix[u][v];
                }
            }
        }

        return distances;
    }

   /* *
     * 在未处理的顶点中找到距离最小的顶点
     * @param distances 距离数组
     * @param processed 处理状态数组
     * @return 距离最小的顶点索引*/

    private int minDistance(int[] distances, boolean[] processed) {
        int min = Integer.MAX_VALUE;
        int minIndex = -1;

        for (int v = 0; v < vertices; v++) {
            if (!processed[v] && distances[v] <= min) {
                min = distances[v];
                minIndex = v;
            }
        }

        return minIndex;
    }

    /**
     * 打印最短路径结果
     * @param distances 距离数组
     * @param source 源顶点
     */
    public void printSolution(int[] distances, int source) {
        System.out.println("顶点 \t 最短距离");
        for (int i = 0; i < vertices; i++) {
            System.out.println(source + " -> " + i + " \t\t " +
                    (distances[i] == Integer.MAX_VALUE ? "不可达" : distances[i]));
        }
    }

    /**
     * 获取图的邻接矩阵
     * @return 邻接矩阵
     */
    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    /**
     * 示例主函数，演示Dijkstra算法的使用
     */
    public static void main(String[] args) {
        // 创建一个包含6个顶点的图
        迪杰斯特拉 graph = new 迪杰斯特拉(6);

        // 添加边 (起始顶点, 目标顶点, 权重)
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 8);
        graph.addEdge(2, 4, 10);
        graph.addEdge(3, 4, 2);
        graph.addEdge(3, 5, 6);
        graph.addEdge(4, 5, 3);

        int sourceVertex = 0;
        System.out.println("从顶点 " + sourceVertex + " 开始的最短路径:");

        // 执行Dijkstra算法
        int[] distances = graph.dijkstra(sourceVertex);

        // 打印结果
        graph.printSolution(distances, sourceVertex);

        // 测试另一个源顶点
        sourceVertex = 2;
        System.out.println("\n从顶点 " + sourceVertex + " 开始的最短路径:");
        distances = graph.dijkstra(sourceVertex);
        graph.printSolution(distances, sourceVertex);
    }
}
