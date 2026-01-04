package algthrom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class 最小生成树Self {
    class Edge {
        int source;
        int destination;
        int weight;
        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }
    //顶点数量
    private int verticles;
    //邻接矩阵
    private List<List<Edge>> adjacencyMatrix;

    public 最小生成树Self(int verticles, List<List<Edge>> adjacencyMatrix) {
        this.verticles = verticles;
        this.adjacencyMatrix = adjacencyMatrix;
    }
    public void addEdge(int source, int destination, int weight){
        if (source >= 0 && source < verticles &&
                destination >= 0 && destination < verticles) {
            adjacencyMatrix.get(source).add(new Edge(source, destination, weight));
        }
    }
    public Edge[][] primMST(int source){
        Edge[][] mst = new Edge[verticles][verticles];
        Arrays.fill(mst, null);
        boolean[] inMST = new boolean[verticles];
        PriorityQueue<Edge> pq = new PriorityQueue<>(verticles, (a, b) -> a.weight - b.weight);
        inMST[source] = true;
        for (Edge edge : adjacencyMatrix.get(source)) {
            pq.offer(edge);
        }
        while (!pq.isEmpty()) {
            Edge edge = pq.poll();
            if(!inMST[edge.destination]){
                mst[edge.source][edge.destination] = edge;
                mst[edge.destination][edge.source] = new Edge(edge.destination, edge.source, edge.weight);
                inMST[edge.destination] = true;
                for (Edge nextEdge : adjacencyMatrix.get(edge.destination)) {
                    if (!inMST[nextEdge.destination]) {
                        pq.offer(nextEdge);
                    }
                }
            }
        }
        return mst;
    }

}
