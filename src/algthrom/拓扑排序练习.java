package algthrom;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class 拓扑排序练习 {
    List<List<Integer>> adjList;
    int vertices;
    public 拓扑排序练习(int vertices) {
        this.vertices = vertices;
        adjList = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    public void addEge(int from,int to){
        adjList.get(from).add(to);
    }
    public List<Integer> topologicalSort(){
        //先找入度为零 的节点
        int[] inDegree = new int[vertices];
        for(int i=0;i<vertices;i++){
            for(int neighbor:adjList.get(i)){
                inDegree[neighbor]++;
            }
        }
        //计算入度为0的节点
        Queue<Integer>queue=new LinkedList<>();
        for(int i=0;i<vertices;i++){
            if(inDegree[i]==0){
                queue.offer(i);
            }
        }
        List<Integer>result=new ArrayList<>();
        //处理队列中的节点
        while(!queue.isEmpty()){
            //取出入度为零的节点
            int current=queue.poll();
            result.add(current);
            //更新邻居节点的入读
            for(int neighbor:adjList.get(current)){
                inDegree[neighbor]--;
                if(inDegree[neighbor]==0){
                    queue.offer(neighbor);
                }
            }
        }
        if(result.size()!=vertices){
            System.out.println("图中存在环，无法进行拓扑排序");
            return new ArrayList<>();
        }
        return result;
    }
}
