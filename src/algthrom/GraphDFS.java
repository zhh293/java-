package algthrom;

import java.util.*;

/**
 * 图的深度优先搜索实现
 */
public class GraphDFS {

    /**
     * 图的内部类表示
     */
    static class Graph {
        private int vertices;
        private List<List<Integer>> adjacencyList;

        /**
         * 构造函数
         * @param vertices 顶点数
         */
        public Graph(int vertices) {
            this.vertices = vertices;
            this.adjacencyList = new ArrayList<>();

            // 初始化邻接表
            for (int i = 0; i < vertices; i++) {
                adjacencyList.add(new ArrayList<>());
            }
        }

        /**
         * 添加边
         * @param source 源顶点
         * @param destination 目标顶点
         */
        public void addEdge(int source, int destination) {
            adjacencyList.get(source).add(destination);
            // 对于无向图，也需要添加反向边
            adjacencyList.get(destination).add(source);
        }

        /**
         * 深度优先搜索遍历 - 递归实现
         * @param start 起始顶点
         */
        public void dfsRecursive(int start) {
            boolean[] visited = new boolean[vertices];
            System.out.print("DFS递归遍历结果: ");
            dfsRecursiveUtil(start, visited);
            System.out.println();
        }

        /**
         * DFS递归辅助函数
         * @param vertex 当前顶点
         * @param visited 访问标记数组
         */
        private void dfsRecursiveUtil(int vertex, boolean[] visited) {
            visited[vertex] = true;
            System.out.print(vertex + " ");

            // 遍历所有邻接顶点
            for (int adjacent : adjacencyList.get(vertex)) {
                if (!visited[adjacent]) {
                    dfsRecursiveUtil(adjacent, visited);
                }
            }
        }

        /**
         * 深度优先搜索遍历 - 迭代实现(使用栈)
         * @param start 起始顶点
         */
        public void dfsIterative(int start) {
            boolean[] visited = new boolean[vertices];
            Stack<Integer> stack = new Stack<>();

            stack.push(start);
            System.out.print("DFS迭代遍历结果: ");

            while (!stack.isEmpty()) {
                int vertex = stack.pop();

                if (!visited[vertex]) {
                    visited[vertex] = true;
                    System.out.print(vertex + " ");

                    // 将邻接顶点压入栈中(逆序压入以保证正确的访问顺序)
                    List<Integer> adjacents = adjacencyList.get(vertex);
                    for (int i = adjacents.size() - 1; i >= 0; i--) {
                        int adjacent = adjacents.get(i);
                        if (!visited[adjacent]) {
                            stack.push(adjacent);
                        }
                    }
                }
            }
            System.out.println();
        }

        /**
         * 获取所有连通分量的DFS遍历
         */
        public void dfsAllComponents() {
            boolean[] visited = new boolean[vertices];
            System.out.print("所有连通分量的DFS遍历: ");

            for (int i = 0; i < vertices; i++) {
                if (!visited[i]) {
                    dfsRecursiveUtil(i, visited);
                }
            }
            System.out.println();
        }

        /**
         * 使用DFS查找路径
         * @param start 起始顶点
         * @param end 目标顶点
         * @return 从start到end的路径
         */
        public List<Integer> findPathDFS(int start, int end) {
            boolean[] visited = new boolean[vertices];
            List<Integer> path = new ArrayList<>();

            if (findPathDFSUtil(start, end, visited, path)) {
                return path;
            }
            return new ArrayList<>(); // 返回空路径表示未找到
        }

        /**
         * DFS查找路径辅助函数
         */
        private boolean findPathDFSUtil(int current, int end, boolean[] visited, List<Integer> path) {
            visited[current] = true;
            path.add(current);

            if (current == end) {
                return true;
            }

            for (int adjacent : adjacencyList.get(current)) {
                if (!visited[adjacent]) {
                    if (findPathDFSUtil(adjacent, end, visited, path)) {
                        return true;
                    }
                }
            }

            // 回溯
            path.remove(path.size() - 1);
            return false;
        }

        /**
         * 检查图是否连通
         * @return 如果图连通返回true，否则返回false
         */
        public boolean isConnected() {
            boolean[] visited = new boolean[vertices];
            dfsRecursiveUtil(0, visited);

            // 检查是否所有顶点都被访问过
            for (boolean v : visited) {
                if (!v) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 创建一个图实例
        Graph graph = new Graph(6);

        // 添加边构建图
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 4);
        graph.addEdge(3, 5);

        System.out.println("图的邻接表表示:");
        for (int i = 0; i < graph.adjacencyList.size(); i++) {
            System.out.println("顶点 " + i + ": " + graph.adjacencyList.get(i));
        }
        System.out.println();

        // 测试递归DFS
        graph.dfsRecursive(0);

        // 测试迭代DFS
        graph.dfsIterative(0);

        // 测试查找路径
        List<Integer> path = graph.findPathDFS(0, 5);
        System.out.println("从顶点0到顶点5的路径: " + path);

        // 测试连通性
        System.out.println("图是否连通: " + graph.isConnected());

        // 测试所有连通分量
        graph.dfsAllComponents();

        System.out.println("\n--- 测试不连通图 ---");
        // 创建不连通图进行测试
        Graph disconnectedGraph = new Graph(7);
        disconnectedGraph.addEdge(0, 1);
        disconnectedGraph.addEdge(0, 2);
        disconnectedGraph.addEdge(1, 3);
        disconnectedGraph.addEdge(4, 5);
        // 顶点6是孤立的

        System.out.println("不连通图是否连通: " + disconnectedGraph.isConnected());
        disconnectedGraph.dfsAllComponents();
    }
}

