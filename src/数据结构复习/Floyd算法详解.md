# Floyd算法详解

## 一、算法概述

Floyd算法，也称为Floyd-Warshall算法，是一种用于求解给定加权图中任意两点间最短路径的经典算法。该算法可以正确处理有向图或负权边（但不可存在负权环），同时也被用于解决有向无环图的传递闭包问题。

### 1.1 算法特点
- **适用范围**：多源最短路径问题
- **时间复杂度**：O(n³)
- **空间复杂度**：O(n²)
- **算法类型**：动态规划算法

### 1.2 适用场景
- 需要求解图中任意两点间的最短路径
- 图的规模不是特别大（通常n<200）
- 需要得到完整的最短路径矩阵

## 二、算法原理

Floyd算法基于动态规划思想，其核心思想是：
> 对于图中的任意两个顶点i和j，最短路径要么直接从i到j，要么经过若干中间顶点到达j。

算法逐步引入中间顶点，更新任意两点间的最短距离。

### 2.1 状态转移方程

```
dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])
```

其中：
- `dist[i][j]` 表示从顶点i到顶点j的最短距离
- `k` 是考虑的中间顶点
- 算法会依次考虑所有可能的中间顶点

### 2.2 算法步骤

1. **初始化**：构建邻接矩阵，对角线元素为0，直接相连的顶点为边权，不直接相连的为无穷大
2. **三重循环**：依次考虑每个顶点作为中间节点
3. **松弛操作**：对每一对顶点(i,j)，检查经过中间节点k是否能缩短距离
4. **更新最短路径**：如果`dist[i][k] + dist[k][j] < dist[i][j]`，则更新`dist[i][j]`

## 三、算法实现

```java
public class Floyd {
    private int vertices;
    private int[][] distanceMatrix;

    public Floyd(int vertices) {
        this.vertices = vertices;
        this.distanceMatrix = new int[vertices][vertices];

        // 初始化距离矩阵
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (i == j) {
                    distanceMatrix[i][j] = 0;
                } else {
                    distanceMatrix[i][j] = Integer.MAX_VALUE;
                }
            }
        }
    }

    public void addEdge(int source, int destination, int weight) {
        distanceMatrix[source][destination] = weight;
    }

    public int[][] floydWarshall() {
        // 创建距离矩阵副本
        int[][] dist = new int[vertices][vertices];

        // 初始化距离矩阵
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                dist[i][j] = distanceMatrix[i][j];
            }
        }

        // Floyd-Warshall核心算法
        for (int k = 0; k < vertices; k++) {
            for (int i = 0; i < vertices; i++) {
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
}
```

## 四、算法执行示例

### 4.1 示例图

假设有一个包含4个顶点的有向图，邻接矩阵如下：

```
     0   1   2   3
0    0   5   ∞   10
1    ∞   0   3   ∞
2    ∞   ∞   0   1
3    ∞   ∞   ∞   0
```

### 4.2 算法执行过程

**初始状态：**
```
dist = [
    [0,  5,  ∞, 10],
    [∞,  0,  3, ∞ ],
    [∞,  ∞,  0, 1 ],
    [∞,  ∞,  ∞, 0 ]
]
```

**k=0（考虑顶点0作为中间节点）：**
- 检查所有可能的(i,j)对，看是否能通过顶点0缩短路径
- 例如：从1到3，dist[1][0] + dist[0][3] = ∞ + 10 = ∞，无变化
- 从2到3，dist[2][0] + dist[0][3] = ∞ + 10 = ∞，无变化

**k=1（考虑顶点1作为中间节点）：**
- 从0到2：dist[0][1] + dist[1][2] = 5 + 3 = 8 < ∞，更新dist[0][2] = 8
- 从0到3：dist[0][1] + dist[1][3] = 5 + ∞ = ∞，无变化

**k=2（考虑顶点2作为中间节点）：**
- 从0到3：dist[0][2] + dist[2][3] = 8 + 1 = 9 < 10，更新dist[0][3] = 9
- 从1到3：dist[1][2] + dist[2][3] = 3 + 1 = 4 < ∞，更新dist[1][3] = 4

**k=3（考虑顶点3作为中间节点）：**
- 没有进一步的更新

**最终结果：**
```
dist = [
    [0,  5,  8,  9 ],
    [∞,  0,  3,  4 ],
    [∞,  ∞,  0,  1 ],
    [∞,  ∞,  ∞,  0 ]
]
```

### 4.3 详细执行步骤

让我们跟踪一个具体的路径更新过程：

1. **初始矩阵：**
   ```
   i=0,j=2: dist[0][2] = ∞
   ```

2. **k=1时：**
   ```
   dist[0][1] + dist[1][2] = 5 + 3 = 8
   8 < ∞，所以 dist[0][2] = 8
   ```

3. **k=2时：**
   ```
   i=0,j=3: dist[0][2] + dist[2][3] = 8 + 1 = 9
   9 < 10 (原dist[0][3])，所以 dist[0][3] = 9
   ```

## 五、路径追踪

除了计算最短距离，我们还可以记录路径信息：

```java
public class FloydWithPath {
    private int vertices;
    private int[][] distanceMatrix;
    private int[][] pathMatrix;  // 用于记录路径

    public int[][] floydWarshallWithPath() {
        int[][] dist = new int[vertices][vertices];
        pathMatrix = new int[vertices][vertices];

        // 初始化
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                dist[i][j] = distanceMatrix[i][j];
                if (i == j || dist[i][j] == Integer.MAX_VALUE) {
                    pathMatrix[i][j] = -1;
                } else {
                    pathMatrix[i][j] = i;
                }
            }
        }

        // Floyd-Warshall算法
        for (int k = 0; k < vertices; k++) {
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE &&
                            dist[k][j] != Integer.MAX_VALUE &&
                            dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        pathMatrix[i][j] = pathMatrix[k][j];
                    }
                }
            }
        }

        return dist;
    }

    // 获取从i到j的路径
    public void printPath(int i, int j) {
        if (i == j) {
            System.out.print(i + " ");
            return;
        }
        
        if (pathMatrix[i][j] == -1) {
            System.out.println("No path exists");
            return;
        }
        
        printPath(i, pathMatrix[i][j]);
        System.out.print(j + " ");
    }
}
```

## 六、算法复杂度分析

### 6.1 时间复杂度
- **三重嵌套循环**：O(n³)
- 每次循环执行常数时间操作
- 总时间复杂度：**O(n³)**

### 6.2 空间复杂度
- **距离矩阵**：O(n²)
- 可能需要额外的路径矩阵：O(n²)
- 总空间复杂度：**O(n²)**

## 七、算法优缺点

### 7.1 优点
1. **简单易懂**：代码实现简单，逻辑清晰
2. **处理多源**：一次运行得到所有点对间的最短路径
3. **处理负权边**：可以处理负权边（但不能有负权环）
4. **适用范围广**：适用于稠密图

### 7.2 缺点
1. **时间复杂度高**：O(n³)对于大规模图效率较低
2. **空间消耗大**：需要O(n²)的额外空间
3. **不能处理负权环**：如果存在负权环，算法结果不正确

## 八、算法应用场景

### 8.1 实际应用
- **网络路由**：计算网络中任意两点间的最短传输路径
- **地图导航**：计算城市间最短路径
- **社交网络**：计算任意两人间的最短关系链
- **游戏开发**：AI寻路算法

### 8.2 算法比较
| 算法 | 时间复杂度 | 适用场景 |
|------|------------|----------|
| Dijkstra | O(V²)或O(E+VlogV) | 单源最短路径，非负权 |
| Bellman-Ford | O(VE) | 单源最短路径，可负权 |
| Floyd | O(V³) | 多源最短路径 |

## 九、注意事项

1. **初始化**：确保对角线元素为0，不相连的点为无穷大
2. **数据类型**：使用足够大的数表示无穷大，注意溢出
3. **负权环**：算法无法处理负权环，需要额外检测
4. **矩阵更新**：确保在原矩阵上更新，避免覆盖原始值

## 十、练习题

### 10.1 基础练习
给定一个有向图，使用Floyd算法计算所有点对间的最短距离。

### 10.2 进阶练习
在Floyd算法基础上，扩展实现在有负权边但无负权环的图中求最短路径。

Floyd算法是图论中的经典算法，虽然时间复杂度较高，但其实现简单、思路清晰，对于中等规模的多源最短路径问题非常有效。