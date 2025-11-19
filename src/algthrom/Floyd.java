package algthrom;

import java.util.Arrays;

/**
 * Floyd-Warshall算法实现
 * 用于计算图中所有顶点对之间的最短路径
 */
public class Floyd {

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
    public Floyd(int vertices) {
        this.vertices = vertices;
        this.adjacencyMatrix = new int[vertices][vertices];

        // 初始化邻接矩阵，用Integer.MAX_VALUE表示无连接
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (i == j) {
                    adjacencyMatrix[i][j] = 0;
                } else {
                    adjacencyMatrix[i][j] = Integer.MAX_VALUE;
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
     * Floyd-Warshall算法核心实现
     * 计算所有顶点对之间的最短路径
     * @return 包含所有顶点对之间最短距离的二维数组
     */
    public int[][] floydWarshall() {
        // 创建距离矩阵副本
        int[][] dist = new int[vertices][vertices];

        // 初始化距离矩阵
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                dist[i][j] = adjacencyMatrix[i][j];
            }
        }

        // Floyd-Warshall核心算法
        // k为中间顶点
        for (int k = 0; k < vertices; k++) {
            // i为起始顶点
            for (int i = 0; i < vertices; i++) {
                // j为目标顶点
                for (int j = 0; j < vertices; j++) {
                    // 如果经过顶点k可以获得更短路径，则更新距离
                    if (dist[i][k] != Integer.MAX_VALUE &&
                            dist[k][j] != Integer.MAX_VALUE &&
                            dist[i][k] + dist[k][j] < dist[i][j]) {

                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        return dist;
    }

    /**
     * 打印所有顶点对之间的最短路径
     * @param distances 距离矩阵
     */
    public void printSolution(int[][] distances) {
        System.out.println("所有顶点对之间的最短距离:");
        System.out.printf("%4s", " ");
        for (int i = 0; i < vertices; i++) {
            System.out.printf("%7d", i);
        }
        System.out.println();

        for (int i = 0; i < vertices; i++) {
            System.out.printf("%4d", i);
            for (int j = 0; j < vertices; j++) {
                if (distances[i][j] == Integer.MAX_VALUE) {
                    System.out.printf("%7s", "∞");
                } else {
                    System.out.printf("%7d", distances[i][j]);
                }
            }
            System.out.println();
        }
    }

    /**
     * 获取从指定源顶点到目标顶点的最短距离
     * @param distances 距离矩阵
     * @param source 源顶点
     * @param destination 目标顶点
     * @return 最短距离
     */
    public int getShortestDistance(int[][] distances, int source, int destination) {
        return distances[source][destination];
    }

    /**
     * 示例主函数，演示Floyd-Warshall算法的使用
     */
    public static void main(String[] args) {
        // 创建一个包含4个顶点的图
        Floyd graph = new Floyd(4);

        // 添加边 (起始顶点, 目标顶点, 权重)
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 3, 10);
        graph.addEdge(1, 2, 3);
        graph.addEdge(2, 3, 1);

        System.out.println("图的邻接矩阵:");
        graph.printSolution(graph.adjacencyMatrix);

        // 执行Floyd-Warshall算法
        int[][] distances = graph.floydWarshall();

        System.out.println("\nFloyd-Warshall算法结果:");
        graph.printSolution(distances);

        // 查询特定顶点对的最短距离
        System.out.println("\n查询特定路径:");
        System.out.println("从顶点0到顶点3的最短距离: " +
                graph.getShortestDistance(distances, 0, 3));
        System.out.println("从顶点1到顶点3的最短距离: " +
                graph.getShortestDistance(distances, 1, 3));
    }
}
