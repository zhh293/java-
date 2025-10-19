package datastructureclass;

import com.sun.source.tree.Tree;

import java.util.*;

/*
public class TaskManager {


    //根据优先级对task排序，同时设计一个map，将taskid为键，userid为值
    PriorityQueue<int[]>priorityQueue=new PriorityQueue<>((o1, o2) -> o2[2]-o1[2]);
    Map<Integer,Integer>map=new HashMap<>();
    Map<Integer,Integer>map2=new HashMap<>();
    public TaskManager(List<List<Integer>> tasks) {
         for (List<Integer> task : tasks) {
             map.put(task.get(1), task.get(0));
             map2.put(task.get(1),task.get(2));
             priorityQueue.add(new int[]{task.get(0), task.get(1),task.get(2)});
         }
    }

    public void add(int userId, int taskId, int priority) {
        Integer i = map.get(taskId);
        if(i==null) {
            map.put(taskId,userId);
            map2.put(taskId,priority);
            priorityQueue.add(new int[]{userId,taskId,priority});
        }
    }

    public void edit(int taskId, int newPriority) {
         Integer i = map.get(taskId);
         Integer i1 = map2.get(taskId);
        if(i!=null&&i1!=null) {
             priorityQueue.remove(new int[]{i,taskId,i1});
         }
    }

    public void rmv(int taskId) {

    }

    public int execTop() {


    }
}
*/
