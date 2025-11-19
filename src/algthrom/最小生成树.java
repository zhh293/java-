package algthrom;



import java.util.*;

/**
 * 最小生成树实现类
 * 包含Kruskal算法和Prim算法两种实现
 */
public class 最小生成树 {


    /*
    最小生成树(MST)概念与用途
    什么是最小生成树？
    最小生成树(Minimum Spanning Tree)是图论中的一个重要概念：
    生成树: 在一个连通无向图中，生成树是包含图中所有顶点的极小连通子图，且不包含环路
    最小生成树: 在带权图中，权重之和最小的生成树就是最小生成树
    一个具有V个顶点的连通图，其生成树恰好有V-1条边
            主要用途
    网络设计
    网络布线规划，使连接成本最低
    电力线路铺设，减少电缆使用量
    通信网络构建，优化传输路径
            聚类分析
    数据挖掘中的层次聚类
            图像分割处理
    近似算法
    旅行商问题(TSP)的近似解
    其他NP难问题的启发式解决方案
    */


    /**
     * 边的内部类
     */
    static class Edge implements Comparable<Edge> {
        int source;
        int destination;
        int weight;

        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }

        @Override
        public String toString() {
            return "(" + source + ", " + destination + ", " + weight + ")";
        }
    }

    /**
     * 并查集内部类 - 用于Kruskal算法
     */
    static class UnionFind {
        private int[] parent;
        private int[] rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        /**
         * 查找根节点（带路径压缩）
         */
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // 路径压缩
            }
            return parent[x];
        }

        /**
         * 合并两个集合
         */
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return false; // 已经在同一个集合中
            }

            // 按秩合并
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            return true;
        }
    }

    /**
     * 图的内部类
     */
    static class Graph {
        private int vertices;
        private List<Edge> edges;
        private List<List<Edge>> adjacencyList;

        public Graph(int vertices) {
            this.vertices = vertices;
            this.edges = new ArrayList<>();
            this.adjacencyList = new ArrayList<>();

            for (int i = 0; i < vertices; i++) {
                adjacencyList.add(new ArrayList<>());
            }
        }

        /**
         * 添加边
         */
        public void addEdge(int source, int destination, int weight) {
            Edge edge = new Edge(source, destination, weight);
            edges.add(edge);
            adjacencyList.get(source).add(new Edge(source, destination, weight));
            adjacencyList.get(destination).add(new Edge(destination, source, weight));
        }

        /**
         * Kruskal算法实现最小生成树
         * 时间复杂度: O(E log E)
         */
        public List<Edge> kruskalMST() {
            List<Edge> result = new ArrayList<>();

            // 1. 按权重排序所有边
            Collections.sort(edges);

            // 2. 初始化并查集
            UnionFind uf = new UnionFind(vertices);

            // 3. 遍历排序后的边
            for (Edge edge : edges) {
                // 如果添加这条边不会形成环，则加入MST
                if (uf.union(edge.source, edge.destination)) {
                    result.add(edge);
                    // MST有V-1条边
                    if (result.size() == vertices - 1) {
                        break;
                    }
                }
            }

            return result;
        }

        /**
         * Prim算法实现最小生成树
         * 时间复杂度: O(V^2) - 使用邻接矩阵优化可以达到O(E + V log V)
         */
        public List<Edge> primMST() {
            List<Edge> result = new ArrayList<>();
            boolean[] inMST = new boolean[vertices]; // 标记顶点是否已在MST中
            int[] minEdge = new int[vertices]; // 到MST的最小边权重
            int[] parent = new int[vertices]; // MST中每个顶点的父节点

            // 初始化
            Arrays.fill(minEdge, Integer.MAX_VALUE);
            Arrays.fill(parent, -1);

            // 从顶点0开始
            minEdge[0] = 0;

            // MST需要包含所有顶点
            for (int count = 0; count < vertices; count++) {
                // 找到不在MST中且权重最小的顶点
                int u = findMinVertex(minEdge, inMST);

                // 将顶点加入MST
                inMST[u] = true;

                // 如果不是起始顶点，添加边到结果中
                if (parent[u] != -1) {
                    result.add(new Edge(parent[u], u, minEdge[u]));
                }

                // 更新相邻顶点的最小边权重
                for (Edge edge : adjacencyList.get(u)) {
                    int v = edge.destination;
                    int weight = edge.weight;

                    // 如果顶点v不在MST中，且通过u到v的边权重更小
                    if (!inMST[v] && weight < minEdge[v]) {
                        minEdge[v] = weight;
                        parent[v] = u;
                    }
                }
            }

            return result;
        }

        /**
         * 在Prim算法中找到权重最小的顶点
         */
        private int findMinVertex(int[] minEdge, boolean[] inMST) {
            int minWeight = Integer.MAX_VALUE;
            int minVertex = -1;

            for (int v = 0; v < vertices; v++) {
                if (!inMST[v] && minEdge[v] < minWeight) {
                    minWeight = minEdge[v];
                    minVertex = v;
                }
            }

            return minVertex;
        }

        /**
         * 计算MST的总权重
         */
        public int calculateMSTWeight(List<Edge> mst) {
            int totalWeight = 0;
            for (Edge edge : mst) {
                totalWeight += edge.weight;
            }
            return totalWeight;
        }
    }

    public static void main(String[] args) {
        // 创建图实例
        Graph graph = new Graph(6);

        // 添加边 (顶点, 顶点, 权重)
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 3);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 2);
        graph.addEdge(2, 3, 4);
        graph.addEdge(3, 4, 2);
        graph.addEdge(3, 5, 6);
        graph.addEdge(4, 5, 3);

        System.out.println("=== 最小生成树算法测试 ===");

        // 使用Kruskal算法
        System.out.println("\n1. Kruskal算法结果:");
        List<Edge> kruskalMST = graph.kruskalMST();
        for (Edge edge : kruskalMST) {
            System.out.println(edge);
        }
        System.out.println("Kruskal MST总权重: " + graph.calculateMSTWeight(kruskalMST));

        // 使用Prim算法
        System.out.println("\n2. Prim算法结果:");
        List<Edge> primMST = graph.primMST();
        for (Edge edge : primMST) {
            System.out.println(edge);
        }
        System.out.println("Prim MST总权重: " + graph.calculateMSTWeight(primMST));

        // 验证两种算法结果的权重应该相同
        System.out.println("\n验证: 两种算法的MST权重相同: " +
                (graph.calculateMSTWeight(kruskalMST) == graph.calculateMSTWeight(primMST)));
    }
}

