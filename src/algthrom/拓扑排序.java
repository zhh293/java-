// 拓扑排序.java
package algthrom;

import java.util.*;

/**
 * 拓扑排序实现类
 * 使用Kahn算法实现拓扑排序
 */
public class 拓扑排序 {

    /**
     * 图的顶点数
     */
    private int vertices;

    /**
     * 邻接表表示图
     */
    private List<List<Integer>> adjacencyList;

    /**
     * 构造函数，初始化图
     * @param vertices 顶点数量
     */
    public 拓扑排序(int vertices) {
        this.vertices = vertices;
        this.adjacencyList = new ArrayList<>();

        // 初始化邻接表
        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    /**
     * 添加边到图中
     * @param from 起始顶点
     * @param to 目标顶点
     */
    public void addEdge(int from, int to) {
        adjacencyList.get(from).add(to);
    }

    /**
     * 执行拓扑排序
     * 使用Kahn算法：
     * 1. 计算每个节点的入度
     * 2. 将入度为0的节点加入队列
     * 3. 从队列中取出节点，并减少其邻居节点的入度
     * 4. 如果邻居节点入度变为0，则将其加入队列
     * 5. 重复直到队列为空
     *
     * @return 拓扑排序结果，如果图中有环则返回空列表
     */
    public List<Integer> topologicalSort() {
        // 存储每个节点的入度
        int[] inDegree = new int[vertices];

        // 计算每个节点的入度
        for (int i = 0; i < vertices; i++) {
            for (int neighbor : adjacencyList.get(i)) {
                inDegree[neighbor]++;
            }
        }

        // 将所有入度为0的节点加入队列
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < vertices; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        // 存储拓扑排序结果
        List<Integer> result = new ArrayList<>();

        // 处理队列中的节点
        while (!queue.isEmpty()) {
            // 取出入度为0的节点
            int current = queue.poll();
            result.add(current);

            // 更新其邻居节点的入度
            for (int neighbor : adjacencyList.get(current)) {
                inDegree[neighbor]--;

                // 如果邻居节点入度变为0，加入队列
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // 检查是否存在环
        // 如果拓扑排序包含所有节点，则无环；否则存在环
        if (result.size() != vertices) {
            System.out.println("图中存在环，无法进行拓扑排序");
            return new ArrayList<>(); // 返回空列表表示排序失败
        }

        return result;
    }

    /**
     * 主方法，演示拓扑排序的使用
     */
    public static void main(String[] args) {
        // 创建一个6个节点的有向无环图
        // 示例图结构:
        // 5 -> 2 -> 3 -> 1
        //  \      \
        //   \      -> 0
        //    \
        //     -> 4 -> 1
        //     \
        //      -> 0

        拓扑排序 graph = new 拓扑排序(6);

        // 添加边
        graph.addEdge(5, 2);  // 课程5是课程2的先修课
        graph.addEdge(5, 0);  // 课程5是课程0的先修课
        graph.addEdge(4, 0);  // 课程4是课程0的先修课
        graph.addEdge(4, 1);  // 课程4是课程1的先修课
        graph.addEdge(2, 3);  // 课程2是课程3的先修课
        graph.addEdge(3, 1);  // 课程3是课程1的先修课

        System.out.println("图的邻接表表示:");
        for (int i = 0; i < 6; i++) {
            System.out.println("节点 " + i + " 的邻居: " + graph.adjacencyList.get(i));
        }

        // 执行拓扑排序
        List<Integer> result = graph.topologicalSort();

        if (!result.isEmpty()) {
            System.out.println("\n拓扑排序结果:");
            for (int vertex : result) {
                System.out.print(vertex + " ");
            }
            System.out.println();

            System.out.println("\n实际应用场景示例:");
            System.out.println("假设节点代表课程，边代表先修关系");
            System.out.println("有效的学习顺序为:");
            String[] courses = {"算法基础", "高级算法", "数据结构", "操作系统", "计算机网络", "编程入门"};
            for (int i = 0; i < result.size(); i++) {
                System.out.println((i+1) + ". " + courses[result.get(i)]);
            }
        }

        // 演示有环的情况
        System.out.println("\n\n演示有环图的情况:");
        拓扑排序 cyclicGraph = new 拓扑排序(3);
        cyclicGraph.addEdge(0, 1);
        cyclicGraph.addEdge(1, 2);
        cyclicGraph.addEdge(2, 0); // 形成环

        System.out.println("尝试对有环图进行拓扑排序:");
        List<Integer> cyclicResult = cyclicGraph.topologicalSort();
        if (cyclicResult.isEmpty()) {
            System.out.println("由于图中存在环，拓扑排序失败");
        }
    }
}
