package algthrom;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/*拓扑排序主要解决以下几类问题：
核心问题
依赖关系排序：处理有向无环图(DAG)中节点之间的依赖关系，确保被依赖的节点在依赖它的节点之前出现
        典型应用场景
任务调度问题
        项目管理中的任务执行顺序安排
课程学习的先修课程依赖关系
        软件编译过程中的模块编译顺序
工程规划
        工程项目的工序安排
生产流水线的任务分配
        事件驱动系统的事件处理顺序
数据处理流程
        大数据处理中的作业依赖关系
工作流引擎中的流程控制
        数据库事务的执行顺序*/

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
