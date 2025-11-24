package algthrom.排序;

import java.util.ArrayList;
import java.util.List;

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


    public List<Integer> topologicalSort(){
        //找一个集合存储入度为零的结点
        List<Integer> zeroInDegree = new ArrayList<>();
        //来一个集合或者数组存储节点的入度
        int[] inDegree = new int[vertices];
        List<Integer>result = new ArrayList<>();
        //计算每个节点的入度
        for(int i=0;i<vertices;i++){
            for(int neighbor:adjacencyList.get(i)){
                inDegree[neighbor]++;
            }
        }

        for(int i=0;i<vertices;i++){
            if(inDegree[i]==0){
                zeroInDegree.add(i);
            }
        }


        while (!zeroInDegree.isEmpty()){
            int current = zeroInDegree.remove(0);
            result.add(current);
            //去除所有从它出去的边
            for(int neighbor:adjacencyList.get(current)){
                inDegree[neighbor]--;
                if(inDegree[neighbor]==0){
                    zeroInDegree.add(neighbor);
                }
            }
        }

        if(result.size()!=vertices){
            throw new RuntimeException("图中存在环，无法进行拓扑排序");
        }
        return result;
    }

    public static void main(String[] args) {
    }
}
