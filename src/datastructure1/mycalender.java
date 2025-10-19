package datastructure1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
public class mycalender {

    class Node{
        int data;
        boolean endflag;
        public Node(int data){
            this.data=data;
            this.endflag=false;
        }
        void setEndflag(boolean endflag) {
            this.endflag = endflag;
        }
        boolean isEndflag() {
            return endflag;
        }
    }
    public mycalender() {

    }
    //用一个集合维护有序怎么样

    List<Node> list=new ArrayList<>();
    public boolean book(int startTime, int endTime) {
        if(list.isEmpty()){
            Node node = new Node(startTime);
            Node node1 = new Node(endTime);
            node1.setEndflag(true);
            list.add(node);
            list.add(node1);
            return true;
        }
        if(endTime<=list.get(0).data) {
            Node node = new Node(startTime);
            Node node1 = new Node(endTime);
            node1.setEndflag(true);
            list.add(node);
            list.add(node1);
            //根据集合中每个结点的数据进行排序
            list.sort(new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    return o1.data - o2.data;
                }

            });
            return true;
        }
        if(startTime>=list.get(list.size()-1).data){
            Node node = new Node(startTime);
            Node node1 = new Node(endTime);
            node1.setEndflag(true);
            list.add(node);
            list.add(node1);
            return true;
        }
        //下面这个要判断哪些小区间是空白的，还没有被填充
        for(int i=0;i<list.size()-1;i++){
            if(startTime>=list.get(i).data&&endTime<=list.get(i+1).data&&list.get(i).isEndflag()&&!list.get(i+1).isEndflag()){
                Node node = new Node(startTime);
                Node node1 = new Node(endTime);
                node1.setEndflag(true);
                list.add(node);
                list.add(node1);
                //根据集合中每个结点的数据进行排序
                list.sort(new Comparator<Node>() {
                    @Override
                    public int compare(Node o1, Node o2) {
                        return o1.data - o2.data;
                    }

                });
                return true;
            }
        }
        return false;
    }
}

