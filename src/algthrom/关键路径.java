// 关键路径.java
package algthrom;

import java.util.*;

/**
 * 关键路径算法实现类
 * 用于求解AOE网（Activity On Edge network）中的关键路径
 * 关键路径是从源点到汇点的最长路径，决定了整个工程的最短完成时间
 */
public class 关键路径 {

    /**
     * 图的顶点数（事件数）
     */
    private int vertices;

    /**
     * 邻接表表示图，存储每个顶点的邻接点及活动持续时间
     * Pair<目标顶点, 活动持续时间>
     */
    private List<List<Edge>> adjacencyList;

    /**
     * 边的内部类，表示活动
     */
    static class Edge {
        int destination;    // 目标顶点
        int duration;       // 活动持续时间

        public Edge(int destination, int duration) {
            this.destination = destination;
            this.duration = duration;
        }
    }

    /**
     * 构造函数，初始化AOE网
     * @param vertices 顶点数量（事件数量）
     */
    public 关键路径(int vertices) {
        this.vertices = vertices;
        this.adjacencyList = new ArrayList<>();

        // 初始化邻接表
        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    /**
     * 添加活动（边）到AOE网中
     * @param from 起始事件
     * @param to 结束事件
     * @param duration 活动持续时间
     */
    public void addActivity(int from, int to, int duration) {
        adjacencyList.get(from).add(new Edge(to, duration));
    }

    /**
     * 计算关键路径
     * 算法步骤：
     * 1. 拓扑排序计算事件最早发生时间
     * 2. 逆拓扑排序计算事件最晚发生时间
     * 3. 计算活动的最早开始时间和最晚开始时间
     * 4. 确定关键活动（最早开始时间等于最晚开始时间的活动）
     *
     * @return 关键路径上的活动序列
     */
    public List<String> calculateCriticalPath() {
        // step1: 计算每个事件的最早发生时间
        int[] earliestTime = new int[vertices];
        Arrays.fill(earliestTime, 0);

        // 计算入度，用于拓扑排序
        int[] inDegree = new int[vertices];
        for (int i = 0; i < vertices; i++) {
            for (Edge edge : adjacencyList.get(i)) {
                inDegree[edge.destination]++;
            }
        }

        // 拓扑排序计算最早发生时间
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < vertices; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        while (!queue.isEmpty()) {
            int current = queue.poll();

            // 更新邻接点的最早发生时间
            for (Edge edge : adjacencyList.get(current)) {
                int destination = edge.destination;
                // 最早发生时间 = max(当前最早发生时间, 当前事件最早发生时间 + 活动持续时间)
                earliestTime[destination] = Math.max(
                        earliestTime[destination],
                        earliestTime[current] + edge.duration
                );

                // 减少入度
                inDegree[destination]--;
                if (inDegree[destination] == 0) {
                    queue.offer(destination);
                }
            }
        }

        // step2: 计算每个事件的最晚发生时间
        int[] latestTime = new int[vertices];
        Arrays.fill(latestTime, earliestTime[vertices - 1]); // 汇点的最晚发生时间等于最早发生时间

        // 重新计算入度
        for (int i = 0; i < vertices; i++) {
            inDegree[i] = 0;
        }
        for (int i = 0; i < vertices; i++) {
            for (Edge edge : adjacencyList.get(i)) {
                inDegree[edge.destination]++;
            }
        }

        // 逆拓扑排序计算最晚发生时间
        queue.clear();
        for (int i = 0; i < vertices; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        List<Integer> topologicalOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.poll();
            topologicalOrder.add(current);

            for (Edge edge : adjacencyList.get(current)) {
                inDegree[edge.destination]--;
                if (inDegree[edge.destination] == 0) {
                    queue.offer(edge.destination);
                }
            }
        }

        // 按逆拓扑顺序计算最晚发生时间
        for (int i = topologicalOrder.size() - 1; i >= 0; i--) {
            int current = topologicalOrder.get(i);

            for (Edge edge : adjacencyList.get(current)) {
                int destination = edge.destination;
                // 最晚发生时间 = min(当前最晚发生时间, 后继事件最晚发生时间 - 活动持续时间)
                latestTime[current] = Math.min(
                        latestTime[current],
                        latestTime[destination] - edge.duration
                );
            }
        }

        // step3: 找出关键活动
        List<String> criticalPath = new ArrayList<>();
        System.out.println("关键活动分析:");
        System.out.println("活动\t最早开始时间\t最晚开始时间\t松弛时间\t是否关键");

        for (int i = 0; i < vertices; i++) {
            for (Edge edge : adjacencyList.get(i)) {
                int destination = edge.destination;
                // 活动的最早开始时间 = 起始事件的最早发生时间
                int earliestStart = earliestTime[i];
                // 活动的最晚开始时间 = 结束事件的最晚发生时间 - 活动持续时间
                int latestStart = latestTime[destination] - edge.duration;
                // 松弛时间 = 最晚开始时间 - 最早开始时间
                int slack = latestStart - earliestStart;

                String activity = "a" + i + "->" + destination;
                System.out.println(activity + "\t" + earliestStart + "\t\t" + latestStart +
                        "\t\t" + slack + "\t\t" + (slack == 0 ? "是" : "否"));

                // 松弛时间为0的活动是关键活动
                if (slack == 0) {
                    criticalPath.add(activity + "(" + edge.duration + ")");
                }
            }
        }

        return criticalPath;
    }

    /**
     * 获取工程的最短完成时间
     * @return 最短完成时间
     */
    public int getMinimumCompletionTime() {
        // 计算最早发生时间
        int[] earliestTime = new int[vertices];
        Arrays.fill(earliestTime, 0);

        int[] inDegree = new int[vertices];
        for (int i = 0; i < vertices; i++) {
            for (Edge edge : adjacencyList.get(i)) {
                inDegree[edge.destination]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < vertices; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        while (!queue.isEmpty()) {
            int current = queue.poll();

            for (Edge edge : adjacencyList.get(current)) {
                int destination = edge.destination;
                earliestTime[destination] = Math.max(
                        earliestTime[destination],
                        earliestTime[current] + edge.duration
                );

                inDegree[destination]--;
                if (inDegree[destination] == 0) {
                    queue.offer(destination);
                }
            }
        }

        // 返回汇点的最早发生时间
        return earliestTime[vertices - 1];
    }

    /**
     * 主方法，演示关键路径算法的使用
     */
    public static void main(String[] args) {
        // 创建一个7个事件的AOE网
        // 工程项目示例：软件开发项目
        关键路径 aoeNetwork = new 关键路径(7);

        // 添加活动（边）
        // 格式：addActivity(起始事件, 结束事件, 持续时间)
        aoeNetwork.addActivity(0, 1, 3);  // 需求分析 -> 系统设计 (3天)
        aoeNetwork.addActivity(0, 2, 2);  // 需求分析 -> 技术选型 (2天)
        aoeNetwork.addActivity(1, 3, 4);  // 系统设计 -> 数据库设计 (4天)
        aoeNetwork.addActivity(2, 3, 1);  // 技术选型 -> 数据库设计 (1天)
        aoeNetwork.addActivity(1, 4, 2);  // 系统设计 -> 前端开发 (2天)
        aoeNetwork.addActivity(3, 5, 3);  // 数据库设计 -> 后端开发 (3天)
        aoeNetwork.addActivity(4, 5, 2);  // 前端开发 -> 后端开发 (2天)
        aoeNetwork.addActivity(5, 6, 2);  // 后端开发 -> 测试部署 (2天)

        System.out.println("AOE网（Activity On Edge Network）表示:");
        System.out.println("顶点表示事件，边表示活动，权重表示活动持续时间");
        for (int i = 0; i < 7; i++) {
            System.out.print("事件" + i + "的后续活动: ");
            for (Edge edge : aoeNetwork.adjacencyList.get(i)) {
                System.out.print("(到事件" + edge.destination + ",耗时" + edge.duration + "天) ");
            }
            System.out.println();
        }

        System.out.println("\n工程最短完成时间: " + aoeNetwork.getMinimumCompletionTime() + "天");

        // 计算并输出关键路径
        List<String> criticalPath = aoeNetwork.calculateCriticalPath();

        System.out.println("\n关键路径上的活动:");
        for (String activity : criticalPath) {
            System.out.println(activity);
        }

        System.out.println("\n关键路径说明:");
        System.out.println("关键路径决定了项目的最短完成时间");
        System.out.println("关键路径上的活动延迟会导致整个项目延期");
        System.out.println("非关键路径上的活动有一定的时间余量（松弛时间）");
    }
}
