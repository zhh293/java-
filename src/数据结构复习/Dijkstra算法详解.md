# Dijkstra算法详解

## 一、算法概述

Dijkstra算法是由荷兰计算机科学家艾兹赫尔·戴克斯特拉提出的一种解决单源最短路径问题的贪心算法。该算法用于计算从单一源点到图中所有其他顶点的最短路径，适用于边权重非负的有向图或无向图。

### 1.1 算法特点
- **适用范围**：单源最短路径问题
- **时间复杂度**：O(V²)或O((V+E)logV)（使用优先队列）
- **空间复杂度**：O(V)
- **算法类型**：贪心算法

### 1.2 适用场景
- 需要求解从单一源点到其他所有点的最短路径
- 图中不存在负权边
- 网络路由协议中的路径计算
- 地图导航系统

## 二、算法原理

Dijkstra算法基于贪心策略，其核心思想是：
> 每次从未确定最短路径的顶点中选择距离源点最近的顶点，然后以该顶点为中间点更新其他顶点的最短距离。

算法维护两个集合：
- **S集合**：已确定最短路径的顶点集合
- **Q集合**：未确定最短路径的顶点集合

### 2.1 算法步骤

1. **初始化**：将源点距离设为0，其他顶点距离设为无穷大
2. **选择顶点**：从未处理顶点中选择距离最小的顶点
3. **松弛操作**：以选中的顶点为中间点，更新其邻居的最短距离
4. **重复**：重复步骤2-3直到所有顶点都被处理

### 2.2 状态转移方程

```
if dist[u] + weight(u, v) < dist[v]:
    dist[v] = dist[u] + weight(u, v)
```

其中：
- `dist[u]` 是源点到顶点u的最短距离
- `weight(u, v)` 是边(u,v)的权重
- `dist[v]` 是当前记录的源点到顶点v的距离

## 三、算法实现

### 3.1 基础实现（邻接矩阵）

```java
import java.util.*;

public class DijkstraBasic {
    private int vertices;
    private int[][] adjacencyMatrix;

    public DijkstraBasic(int vertices) {
        this.vertices = vertices;
        this.adjacencyMatrix = new int[vertices][vertices];
        
        // 初始化邻接矩阵
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

    public void addEdge(int source, int destination, int weight) {
        adjacencyMatrix[source][destination] = weight;
    }

    public int[] dijkstra(int source) {
        // 存储从源顶点到各顶点的最短距离
        int[] dist = new int[vertices];
        // 记录顶点是否已处理
        boolean[] visited = new boolean[vertices];

        // 初始化距离数组
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(visited, false);
        dist[source] = 0;

        // 对每个顶点进行处理
        for (int count = 0; count < vertices - 1; count++) {
            // 找到未处理顶点中距离最小的顶点
            int u = minDistance(dist, visited);

            // 标记该顶点已处理
            visited[u] = true;

            // 更新与顶点u相邻的顶点的距离值
            for (int v = 0; v < vertices; v++) {
                if (!visited[v] && 
                    adjacencyMatrix[u][v] != Integer.MAX_VALUE &&
                    dist[u] != Integer.MAX_VALUE &&
                    dist[u] + adjacencyMatrix[u][v] < dist[v]) {
                    dist[v] = dist[u] + adjacencyMatrix[u][v];
                }
            }
        }

        return dist;
    }

    private int minDistance(int[] dist, boolean[] visited) {
        int min = Integer.MAX_VALUE;
        int minIndex = -1;

        for (int v = 0; v < vertices; v++) {
            if (!visited[v] && dist[v] <= min) {
                min = dist[v];
                minIndex = v;
            }
        }

        return minIndex;
    }
}
```

### 3.2 优化实现（优先队列）

```java
import java.util.*;

public class DijkstraOptimized {
    static class Node implements Comparable<Node> {
        int vertex;
        int distance;
        
        Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
        
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
    
    private int vertices;
    private List<List<Edge>> adjacencyList;

    static class Edge {
        int destination;
        int weight;
        
        Edge(int destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    public DijkstraOptimized(int vertices) {
        this.vertices = vertices;
        this.adjacencyList = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        adjacencyList.get(source).add(new Edge(destination, weight));
    }

    public int[] dijkstra(int source) {
        int[] dist = new int[vertices];
        boolean[] visited = new boolean[vertices];
        PriorityQueue<Node> pq = new PriorityQueue<>();
        
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        pq.offer(new Node(source, 0));
        
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int u = current.vertex;
            
            if (visited[u]) continue;
            visited[u] = true;
            
            for (Edge edge : adjacencyList.get(u)) {
                int v = edge.destination;
                int weight = edge.weight;
                
                if (!visited[v] && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.offer(new Node(v, dist[v]));
                }
            }
        }
        
        return dist;
    }
}
```

## 四、算法执行示例

### 4.1 示例图

假设有一个包含5个顶点的有向图，邻接矩阵如下：

```
     0   1   2   3   4
0    0   10  ∞   5   ∞
1    ∞   0   1   2   ∞
2    ∞   ∞   0   ∞   4
3    ∞   3   9   0   2
4    7   ∞   6   ∞   0
```

### 4.2 算法执行过程（从顶点0开始）

**初始化：**
```
dist = [0, ∞, ∞, ∞, ∞]
visited = [false, false, false, false, false]
```

**第1轮：选择顶点0（距离0）**
- 更新邻接顶点：dist[1]=10, dist[3]=5
- visited[0] = true

**第2轮：选择顶点3（距离5）**
- 更新邻接顶点：dist[1]=min(10, 5+3)=8, dist[4]=5+2=7
- visited[3] = true

**第3轮：选择顶点4（距离7）**
- 更新邻接顶点：dist[2]=7+6=13
- visited[4] = true

**第4轮：选择顶点1（距离8）**
- 更新邻接顶点：dist[2]=min(13, 8+1)=9, dist[3]=min(5, 8+2)=5
- visited[1] = true

**第5轮：选择顶点2（距离9）**
- 无更新
- visited[2] = true

**最终结果：**
```
dist = [0, 8, 9, 5, 7]
```

## 五、路径追踪

除了计算最短距离，我们还可以记录路径信息：

```java
public class DijkstraWithPath {
    static class Node implements Comparable<Node> {
        int vertex;
        int distance;
        
        Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
        
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
    
    private int vertices;
    private List<List<Edge>> adjacencyList;

    static class Edge {
        int destination;
        int weight;
        
        Edge(int destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    public DijkstraWithPath(int vertices) {
        this.vertices = vertices;
        this.adjacencyList = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        adjacencyList.get(source).add(new Edge(destination, weight));
    }

    public Result dijkstraWithPath(int source) {
        int[] dist = new int[vertices];
        int[] parent = new int[vertices];
        boolean[] visited = new boolean[vertices];
        PriorityQueue<Node> pq = new PriorityQueue<>();
        
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[source] = 0;
        pq.offer(new Node(source, 0));
        
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int u = current.vertex;
            
            if (visited[u]) continue;
            visited[u] = true;
            
            for (Edge edge : adjacencyList.get(u)) {
                int v = edge.destination;
                int weight = edge.weight;
                
                if (!visited[v] && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    parent[v] = u;
                    pq.offer(new Node(v, dist[v]));
                }
            }
        }
        
        return new Result(dist, parent);
    }
    
    static class Result {
        int[] distances;
        int[] parents;
        
        Result(int[] distances, int[] parents) {
            this.distances = distances;
            this.parents = parents;
        }
    }
    
    // 获取从源点到目标点的路径
    public List<Integer> getPath(int source, int destination, int[] parent) {
        List<Integer> path = new ArrayList<>();
        int current = destination;
        
        while (current != -1) {
            path.add(current);
            current = parent[current];
        }
        
        Collections.reverse(path);
        return path;
    }
}
```

## 六、算法复杂度分析

### 6.1 时间复杂度
- **基础实现**：O(V²)
  - 外层循环V次
  - 每次寻找最小距离需要O(V)
  - 总时间复杂度O(V²)

- **优先队列实现**：O((V+E)logV)
  - 每个顶点最多入队一次，出队一次，O(V log V)
  - 每条边最多被松弛一次，O(E log V)
  - 总时间复杂度O((V+E)logV)

### 6.2 空间复杂度
- **存储图**：O(V²)（邻接矩阵）或O(V+E)（邻接表）
- **辅助数组**：O(V)
- **优先队列**：O(V)
- **总空间复杂度**：O(V²)或O(V+E)

## 七、算法优缺点

### 7.1 优点
1. **正确性保证**：对于非负权图，算法保证找到最短路径
2. **实现简单**：算法逻辑清晰，易于理解
3. **适用性广**：可用于有向图和无向图
4. **可扩展性**：容易扩展以获取路径信息

### 7.2 缺点
1. **负权边限制**：无法处理负权边
2. **时间复杂度**：对于稀疏图，Bellman-Ford可能更优
3. **空间消耗**：需要额外空间存储距离和访问状态

## 八、算法应用场景

### 8.1 实际应用
- **网络路由**：OSPF等路由协议
- **地图导航**：GPS导航系统
- **社交网络**：计算最短关系链
- **游戏开发**：AI寻路算法

### 8.2 算法比较

| 算法 | 时间复杂度 | 空间复杂度 | 负权边 | 适用场景 |
|------|------------|------------|--------|----------|
| Dijkstra | O((V+E)logV) | O(V) | 不支持 | 非负权单源 |
| Bellman-Ford | O(VE) | O(V) | 支持 | 单源 |
| Floyd | O(V³) | O(V²) | 支持 | 多源 |

## 九、注意事项

1. **负权边**：算法不适用于包含负权边的图
2. **溢出处理**：距离更新时注意整数溢出问题
3. **图的表示**：稀疏图使用邻接表，稠密图使用邻接矩阵
4. **优先队列**：使用优先队列可显著提升性能

## 十、练习题

### 10.1 基础练习
给定一个带权有向图，使用Dijkstra算法计算从源点到所有其他点的最短距离。

### 10.2 进阶练习
扩展Dijkstra算法，使其能够处理路径重构，并找出最短路径。

Dijkstra算法是图论中最重要和最常用的算法之一，掌握其原理和实现对于理解图算法至关重要。