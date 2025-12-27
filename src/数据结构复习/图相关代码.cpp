#include <stdio.h>
#include <stdlib.h>

// 定义队列节点结构（用于BFS）
typedef struct QueueNode {
    int data;
    struct QueueNode* next;
} QueueNode;

// 定义队列结构（用于BFS）
typedef struct Queue {
    QueueNode* front;
    QueueNode* rear;
} Queue;

// 创建队列
Queue* createQueue() {
    Queue* queue = (Queue*)malloc(sizeof(Queue));
    queue->front = NULL;
    queue->rear = NULL;
    return queue;
}

// 入队
void enqueue(Queue* queue, int data) {
    QueueNode* newNode = (QueueNode*)malloc(sizeof(QueueNode));
    newNode->data = data;
    newNode->next = NULL;
    
    if (queue->rear == NULL) {
        queue->front = queue->rear = newNode;
    } else {
        queue->rear->next = newNode;
        queue->rear = newNode;
    }
}

// 出队
int dequeue(Queue* queue) {
    if (queue->front == NULL) {
        return -1; // 队列为空
    }
    
    QueueNode* temp = queue->front;
    int data = temp->data;
    queue->front = queue->front->next;
    
    if (queue->front == NULL) {
        queue->rear = NULL;
    }
    
    free(temp);
    return data;
}

// 检查队列是否为空
int isEmpty(Queue* queue) {
    return queue->front == NULL;
}

// 定义邻接表节点结构
typedef struct EdgeNode {
    int dest;                    // 边的终点
    int weight;                  // 边的权重
    struct EdgeNode* next;       // 指向下一个邻接点的指针
} EdgeNode;

// 定义顶点结构
typedef struct VertexNode {
    int vertex;                  // 顶点数据
    EdgeNode* firstEdge;         // 指向第一个邻接点的指针
} VertexNode;

// 定义图结构
typedef struct Graph {
    int vertexNum;               // 顶点数
    int edgeNum;                 // 边数
    VertexNode* adjList;         // 邻接表
} Graph;

/**
 * 初始化图
 * @param n 顶点数量
 * @return 初始化的图指针
 */
Graph* initGraph(int n) {
    Graph* graph = (Graph*)malloc(sizeof(Graph));
    if (!graph) {
        printf("内存分配失败\n");
        return NULL;
    }
    
    graph->vertexNum = n;
    graph->edgeNum = 0;
    graph->adjList = (VertexNode*)malloc(n * sizeof(VertexNode));
    if (!graph->adjList) {
        printf("内存分配失败\n");
        free(graph);
        return NULL;
    }
    
    // 初始化每个顶点
    for (int i = 0; i < n; i++) {
        graph->adjList[i].vertex = i;
        graph->adjList[i].firstEdge = NULL;
    }
    
    return graph;
}

/**
 * 创建边节点
 * @param dest 边的终点
 * @param weight 边的权重
 * @return 新创建的边节点指针
 */
EdgeNode* createEdgeNode(int dest, int weight) {
    EdgeNode* newNode = (EdgeNode*)malloc(sizeof(EdgeNode));
    if (!newNode) {
        printf("内存分配失败\n");
        return NULL;
    }
    
    newNode->dest = dest;
    newNode->weight = weight;
    newNode->next = NULL;
    
    return newNode;
}

/**
 * 添加边到图中（无向图）
 * @param graph 图指针
 * @param src 起点
 * @param dest 终点
 * @param weight 边的权重
 */
void addEdge(Graph* graph, int src, int dest, int weight) {
    if (!graph || src < 0 || src >= graph->vertexNum || dest < 0 || dest >= graph->vertexNum) {
        printf("参数错误\n");
        return;
    }
    
    // 为src->dest创建边
    EdgeNode* newNode = createEdgeNode(dest, weight);
    if (!newNode) return;
    
    newNode->next = graph->adjList[src].firstEdge;
    graph->adjList[src].firstEdge = newNode;
    
    // 如果是无向图，还需要添加dest->src的边
    if (src != dest) {  // 避免自环重复添加
        newNode = createEdgeNode(src, weight);
        if (!newNode) return;
        
        newNode->next = graph->adjList[dest].firstEdge;
        graph->adjList[dest].firstEdge = newNode;
    }
    
    graph->edgeNum++;
}

/**
 * 添加边到图中（有向图）
 * @param graph 图指针
 * @param src 起点
 * @param dest 终点
 * @param weight 边的权重
 */
void addDirectedEdge(Graph* graph, int src, int dest, int weight) {
    if (!graph || src < 0 || src >= graph->vertexNum || dest < 0 || dest >= graph->vertexNum) {
        printf("参数错误\n");
        return;
    }
    
    // 为src->dest创建边
    EdgeNode* newNode = createEdgeNode(dest, weight);
    if (!newNode) return;
    
    newNode->next = graph->adjList[src].firstEdge;
    graph->adjList[src].firstEdge = newNode;
    
    graph->edgeNum++;
}

/**
 * 打印图的邻接表表示
 * @param graph 图指针
 */
void printGraph(Graph* graph) {
    if (!graph) {
        printf("图为空\n");
        return;
    }
    
    printf("图的邻接表表示:\n");
    for (int i = 0; i < graph->vertexNum; i++) {
        printf("顶点 %d: ", graph->adjList[i].vertex);
        EdgeNode* current = graph->adjList[i].firstEdge;
        while (current) {
            printf("-> %d(w:%d) ", current->dest, current->weight);
            current = current->next;
        }
        printf("\n");
    }
    printf("总边数: %d\n", graph->edgeNum);
}

/**
 * 释放图占用的内存
 * @param graph 图指针
 */
void freeGraph(Graph* graph) {
    if (!graph) return;
    
    for (int i = 0; i < graph->vertexNum; i++) {
        EdgeNode* current = graph->adjList[i].firstEdge;
        while (current) {
            EdgeNode* temp = current;
            current = current->next;
            free(temp);
        }
    }
    
    free(graph->adjList);
    free(graph);
}

/**
 * 深度优先搜索（DFS）
 * @param graph 图指针
 * @param v 当前访问的顶点
 * @param visited 访问标记数组
 */
void DFS(Graph* graph, int v, int visited[]) {
    visited[v] = 1;
    printf("%d ", v);
    
    EdgeNode* current = graph->adjList[v].firstEdge;
    while (current) {
        if (!visited[current->dest]) {
            DFS(graph, current->dest, visited);
        }
        current = current->next;
    }
}

/**
 * 广度优先搜索（BFS）
 * @param graph 图指针
 * @param start 起始顶点
 */
void BFS(Graph* graph, int start) {
    int* visited = (int*)malloc(graph->vertexNum * sizeof(int));
    for (int i = 0; i < graph->vertexNum; i++) {
        visited[i] = 0;
    }
    
    Queue* queue = createQueue();
    visited[start] = 1;
    printf("%d ", start);
    enqueue(queue, start);
    
    while (!isEmpty(queue)) {
        int curr = dequeue(queue);
        // 遍历当前顶点的所有邻接点
        EdgeNode* current = graph->adjList[curr].firstEdge;
        while (current) {
            if (!visited[current->dest]) {
                visited[current->dest] = 1;
                printf("%d ", current->dest);
                enqueue(queue, current->dest);
            }
            current = current->next;
        }
    }
    
    free(visited);
    // 释放队列内存
    while (!isEmpty(queue)) {
        dequeue(queue);
    }
    free(queue);
}

/**
 * 对整个图进行DFS遍历（处理非连通图）
 * @param graph 图指针
 */
void DFSTraverse(Graph* graph) {
    int* visited = (int*)malloc(graph->vertexNum * sizeof(int));
    for (int i = 0; i < graph->vertexNum; i++) {
        visited[i] = 0;
    }
    
    printf("DFS遍历结果: ");
    for (int i = 0; i < graph->vertexNum; i++) {
        if (!visited[i]) {
            DFS(graph, i, visited);
        }
    }
    printf("\n");
    
    free(visited);
}

/**
 * 对整个图进行BFS遍历（处理非连通图）
 * @param graph 图指针
 */
void BFSTraverse(Graph* graph) {
    int* visited = (int*)malloc(graph->vertexNum * sizeof(int));
    for (int i = 0; i < graph->vertexNum; i++) {
        visited[i] = 0;
    }
    
    printf("BFS遍历结果: ");
    for (int i = 0; i < graph->vertexNum; i++) {
        if (!visited[i]) {
            visited[i] = 1;
            printf("%d ", i);
            Queue* queue = createQueue();
            enqueue(queue, i);
            
            while (!isEmpty(queue)) {
                int curr = dequeue(queue);
                
                EdgeNode* current = graph->adjList[curr].firstEdge;
                while (current) {
                    if (!visited[current->dest]) {
                        visited[current->dest] = 1;
                        printf("%d ", current->dest);
                        enqueue(queue, current->dest);
                    }
                    current = current->next;
                }
            }
            // 清空并释放队列
            while (!isEmpty(queue)) {
                dequeue(queue);
            }
            free(queue);
        }
    }
    printf("\n");
    
    free(visited);
}

/**
 * 测试函数
 */
void testGraph() {
    printf("创建包含5个顶点的图\n");
    Graph* graph = initGraph(5);
    
    // 添加边 (顶点, 顶点, 权重)
    addEdge(graph, 0, 1, 10);
    addEdge(graph, 0, 4, 5);
    addEdge(graph, 1, 2, 7);
    addEdge(graph, 1, 4, 3);
    addEdge(graph, 2, 3, 6);
    addEdge(graph, 3, 4, 2);
    
    printGraph(graph);
    
    printf("\nDFS遍历结果: ");
    int* visited = (int*)malloc(5 * sizeof(int));
    for (int i = 0; i < 5; i++) visited[i] = 0;
    DFS(graph, 0, visited);
    printf("\n");
    free(visited);
    
    printf("BFS遍历结果: ");
    BFS(graph, 0);
    printf("\n");
    
    printf("\n完整DFS遍历: ");
    DFSTraverse(graph);
    
    printf("完整BFS遍历: ");
    BFSTraverse(graph);
    
    printf("\n添加有向边:\n");
    Graph* directedGraph = initGraph(4);
    addDirectedEdge(directedGraph, 0, 1, 5);
    addDirectedEdge(directedGraph, 0, 2, 3);
    addDirectedEdge(directedGraph, 1, 2, 2);
    addDirectedEdge(directedGraph, 2, 3, 4);
    
    printGraph(directedGraph);
    
    // 释放内存
    freeGraph(graph);
    freeGraph(directedGraph);
}

/**
 * 主函数
 */
int main() {
    testGraph();
    return 0;
}