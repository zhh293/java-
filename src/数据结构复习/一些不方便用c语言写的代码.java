package 数据结构复习;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class 一些不方便用c语言写的代码 {
    //哈夫曼编码
    class HuffumanCode{
        static class Node{
            char data;
            int weight;
            Node left;
            Node right;
            public Node(char data,int weight){
                this.data = data;
                this.weight = weight;
            }
        }
        private PriorityQueue<Node> queue;
        public HuffumanCode(){
            queue = new PriorityQueue<Node>();
        }
        public Node HuffumanTree(char[] chars,int[] weights){
            if (chars.length != weights.length){
                return null;
            }
            if(chars.length <2){
                System.out.println("输入数据不合法");
                return null;
            }
            for(int i=0;i<chars.length;i++){
                queue.add(new Node(chars[i],weights[i]));
            }
            while(queue.size()>1){
                Node node1 = queue.poll();
                Node node2 = queue.poll();
                Node node = new Node('\0',node1.weight+node2.weight);
                node.left = node1;
                node.right = node2;
                queue.add(node);
            }
            return queue.poll();
        }
        public String HuffumanCode(char[]chars,int[]weights){
            Node root = HuffumanTree(chars,weights);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i=0;i<chars.length;i++){
                stringBuilder.append(getCode(root,new StringBuilder(),chars[i]));
            }
            return stringBuilder.toString();
        }
        private String getCode(Node root,StringBuilder stringBuilder,char ch){
            if(root==null){
                return "";
            }
            if(root.data!='\0'&&root.data==ch){
                return stringBuilder.toString();
            }
            String left = getCode(root.left,stringBuilder.append("0"), ch);
            String right = getCode(root.right,stringBuilder.append("1"), ch);
            if(left.isEmpty()&&right.isEmpty()){
                System.out.println("没有找到对应的编码");
                return "";
            }else if (left.isEmpty()){
                return right;
            }else{
                return left;
            }
        }
    }

    class 拓扑排序{
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
            //找到出度为零的顶点
            List<Integer> zeroInDegree = new ArrayList<>();
            for (int i=0;i<vertices;i++){
                if(adjacencyList.get(i).isEmpty()){
                    zeroInDegree.add(i);
                }
            }
            List<Integer> result = new ArrayList<>();
            while (!zeroInDegree.isEmpty()){
                int current = zeroInDegree.remove(0);
                result.add(current);
                for(int neighbor:adjacencyList.get(current)){
                    adjacencyList.get(neighbor).remove(current);
                    if(adjacencyList.get(neighbor).isEmpty()){
                        zeroInDegree.add(neighbor);
                    }
                }
            }
            if(result.size()!=vertices){
                throw new RuntimeException("图中存在环，无法进行拓扑排序");
            }
            return result;
        }
    }
    class 堆排序{
        //先把数组处理为最大堆
        public void buildMaxHeap(int[] arr){
            for (int i=arr.length/2-1;i>=0;i--){
                adjustHeap(arr,i,arr.length);
            }
        }
        private void adjustHeap(int[] arr,int i,int length){
            int temp = arr[i];
            for (int k=2*i+1;k<length;k=2*k+1){
                if(k+1<length&&arr[k]<arr[k+1]){
                    k++;
                }
                if(arr[k]>temp){
                    arr[i] = arr[k];
                    i = k;
                }else{
                    break;
                }
            }
        }
        //递归建堆
        private void adjustHeap1(int[] arr,int i,int length){
            if(2*i+1>=length) {
                return;
            }
            if(2*i+1<length&&2*i+2>=length){
                if(arr[i]<arr[2*i+1]){
                    int temp = arr[i];
                    arr[i] = arr[2*i+1];
                    arr[2*i+1] = temp;
                }
            }
            if(2*i+1<length&&2*i+2<length){
                if(arr[i]<arr[2*i+1]||arr[i]<arr[2*i+2]){
                    int temp = arr[i];
                    if(arr[2*i+1]>arr[2*i+2]){
                        arr[i] = arr[2*i+1];
                        arr[2*i+1] = temp;
                        adjustHeap1(arr,2*i+1,length);
                    }else{
                        arr[i] = arr[2*i+2];
                        arr[2*i+2] = temp;
                        adjustHeap1(arr,2*i+2,length);
                    }
                }
            }
        }
        public void heapSort(int[] arr){
            buildMaxHeap(arr);
            for (int i=arr.length-1;i>0;i--){
                int temp = arr[i];
                arr[i] = arr[0];
                arr[0] = temp;
                adjustHeap(arr,0,i);
            }
        }
    }
    class 迪杰斯特拉{
        class Node{
            int vertex;
            int distance;
            public Node(int vertex, int distance) {
                this.vertex = vertex;
                this.distance = distance;
            }
        }
        private int vertices;
        private List<List<Node>>lists;
        public 迪杰斯特拉(int vertices) {
            this.vertices = vertices;
            this.lists = new ArrayList<>();
            for (int i = 0; i < vertices; i++) {
                lists.add(new ArrayList<>());
            }
        }
        public void addEdge(int from, int to, int weight) {
            lists.get(from).add(new Node(to, weight));
        }
        //迪杰斯特拉算法思路
        //创建一个距离数组，用于存储源点到其他点的最短距离
        public void dijkstra(int source) {
            int[] distance = new int[vertices];
            for (int i = 0; i < vertices; i++) {
                distance[i] = Integer.MAX_VALUE;
            }
            boolean[] visited = new boolean[vertices];
            PriorityQueue<Node> queue = new PriorityQueue<>(new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    return o1.distance - o2.distance;
                }
            });
            queue.offer(new Node(source, 0));
            distance[source] = 0;
            while (!queue.isEmpty()) {
                Node poll = queue.poll();
                if(visited[poll.vertex]){
                    continue;
                }
                visited[poll.vertex] = true;
                for(Node node:lists.get(poll.vertex)){
                    if(distance[node.vertex]>distance[poll.vertex]+node.distance&&!visited[node.vertex]){
                        distance[node.vertex] = distance[poll.vertex]+node.distance;
                        queue.offer(new Node(node.vertex,distance[node.vertex]));
                    }
                }
            }
        }
    }
    class 弗洛伊德{
        class Node{
            int vertex;
            int distance;
            public Node(int vertex, int distance) {
                this.vertex = vertex;
                this.distance = distance;
            }
        }
        private int vertices;
        private List<List<Node>> lists;
        public 弗洛伊德(int vertices) {
            this.vertices = vertices;
            this.lists = new ArrayList<>();
            for (int i = 0; i < vertices; i++) {
                lists.add(new ArrayList<>());
            }
        }
        public void addEdge(int from, int to, int weight) {
            lists.get(from).add(new Node(to, weight));
        }

        public void floyd(){
            int[][] distance = new int[vertices][vertices];
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    distance[i][j] = Integer.MAX_VALUE;
                }
            }
        }

    }
}
