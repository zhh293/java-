package datastructureclass;

import java.util.*;

class NumberContainers {
    Map<Integer,PriorityQueue<Integer>>map;
    Map<Integer,Integer>map1;
    public NumberContainers() {
        this.map = new HashMap<>();
        this.map1 = new HashMap<>();
    }

    public void change(int index, int number) {
        if(map.get(number) == null) {
            PriorityQueue<Integer> pq = new PriorityQueue<>();
            pq.add(index);
            if(map1.containsKey(index)) {
                Integer i = map1.get(index);
                PriorityQueue<Integer> integers = map.get(i);
                integers.remove(index);
            }
            map.put(number, pq);
        }else{
            PriorityQueue<Integer> pq = map.get(number);
            pq.add(index);
            if(map1.containsKey(index)) {
                Integer i = map1.get(index);
                PriorityQueue<Integer> integers = map.get(i);
                integers.remove(index);
            }
            map.put(number, pq);
        }
        map1.put(index, number);
    }

    public int find(int number) {
        PriorityQueue<Integer> integers = map.get(number);
        if(integers==null||integers.isEmpty()){
            return -1;
        }
        return integers.peek();
    }
}

/**
 * Your NumberContainers object will be instantiated and called as such:
 * NumberContainers obj = new NumberContainers();
 * obj.change(index,number);
 * int param_2 = obj.find(number);
 */