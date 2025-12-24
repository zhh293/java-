package algthrom;


import java.util.*;

public class 广度优先遍历 {

    /**
     * 图的BFS遍历实现
     */
    static class Graph {
        private int vertices;
        private List<List<Integer>> adjacencyList;

        public Graph(int vertices) {
            this.vertices = vertices;
            this.adjacencyList = new ArrayList<>();

            for (int i = 0; i < vertices; i++) {
                adjacencyList.add(new ArrayList<>());
            }
        }

        public void addEdge(int source, int destination) {
            adjacencyList.get(source).add(destination);
            adjacencyList.get(destination).add(source);
        }

        /**
         * 广度优先搜索遍历
         * @param start 起始顶点
         */
        public void bfs(int start) {
            boolean[] visited = new boolean[vertices];
            Queue<Integer> queue = new LinkedList<>();

            visited[start] = true;
            queue.offer(start);

            System.out.print("BFS遍历结果: ");

            while (!queue.isEmpty()) {
                int vertex = queue.poll();
                System.out.print(vertex + " ");

                for (int adjacent : adjacencyList.get(vertex)) {
                    if (!visited[adjacent]) {
                        visited[adjacent] = true;
                        queue.offer(adjacent);
                    }
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // 基于文件中提供的边数据创建图
        Graph graph = new Graph(33); // 至少需要33个顶点(0-32)

        // 添加边
        int[][] edges = {
                {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {6, 7}, {7, 8}, {8, 9},
                {9, 10}, {10, 11}, {11, 12}, {12, 13}, {13, 14}, {14, 15}, {15, 16},
                {16, 17}, {17, 18}, {18, 19}, {19, 20}, {20, 21}, {21, 22}, {22, 23},
                {23, 24}, {24, 25}, {25, 26}, {26, 27}, {27, 28}, {28, 29}, {29, 30},
                {30, 31}, {31, 32}
        };

        for (int[] edge : edges) {
            graph.addEdge(edge[0], edge[1]);
        }

        // 执行BFS遍历
        graph.bfs(1);
    }
//    public List<List<Integer>> permute(int[] nums) {
//        //写完这道题背八股
//    }
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>>list=new ArrayList<>();

        Deque<TreeNode>queue=new LinkedList<>();
        queue.offer(root);
        int count=0;
        while(!queue.isEmpty()){

            int size=queue.size();
            List<Integer>list1=new ArrayList<>();
            for(int i=0;i<size;i++){
                TreeNode root1=queue.pop();
                list1.add(root1.val);
                if(root1.left!=null){
                    queue.offer(root1.left);
                }
                if(root1.right!=null){
                    queue.offer(root1.right);
                }
            }
            if(count%2==1){
                Collections.reverse(list1);
            }
            list.add(list1);
            count++;
        }
        return list;
    }
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
}

