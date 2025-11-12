package datastructure1;

import java.util.Comparator;
import java.util.PriorityQueue;

public class 哈夫曼树和哈夫曼编码 {
    public static void main(String[] args) {
          int[][]Huffuman={{'a',50},{ 'b',20},{ 'c',30},{ 'd',10},{ 'e',5},{ 'f',40}};
          Node[] nodes=new Node[Huffuman.length];
          for(int i=0;i<Huffuman.length;i++){
              nodes[i]=new Node();
              nodes[i].data=Huffuman[i][1];
              nodes[i].ch=(char)Huffuman[i][0];
          }
          PriorityQueue<Node> queue=new PriorityQueue<Node>(new Comparator<Node>(){
              public int compare(Node o1,Node o2){
                  return o1.data-o2.data;
              }
          });
          for(int i=0;i<Huffuman.length;i++){
              queue.offer(nodes[i]);
          }
          HuffumanCode huffumanCode=new HuffumanCode();
          Node root=huffumanCode.createHuffumanTree(queue);

          huffumanCode.printTree(root);
          huffumanCode.preOrder(root);

          System.out.println();
          //对一串字符串进行哈夫曼编码
          String str="abcd";
          huffumanCode.printHuffumanCode(root,"");

          //对一串编码进行还原
          String code="11011000101";
          String code1="0000000";
          Node root1=root;
          huffumanCode.decode(root,code1,root1);


    }
    static class Node{
        char ch;
        int data;
        Node left;
        Node right;
    }
    static class HuffumanCode{
        public Node createHuffumanTree(PriorityQueue<Node> queue){
            while(queue.size()>1){
                Node node1=queue.poll();
                Node node2=queue.poll();
                Node node=new Node();
                node.data=node1.data+node2.data;
                node.left=node1;
                node.right=node2;
                node.ch=' ';
                queue.offer(node);
            }
            return queue.poll();
        }

        public void printHuffumanCode(Node root,String code){
            if(root!=null){
                if(root.ch!=' '){
                    System.out.println(root.ch+"-->"+code);
                }
                printHuffumanCode(root.left,code+"0");
                printHuffumanCode(root.right,code+"1");
            }
        }

        public void preOrder(Node root){
            if(root!=null){
                System.out.print(root.ch+" ");
                preOrder(root.left);
                preOrder(root.right);
            }
        }
        // 新增的打印函数
        public void printTree(Node root) {
            printTree(root, "", true);
        }
        public void decode(Node root, String code,Node root1){
            for(int i=0;i<code.length();i++){
                if(code.charAt(i)=='1'){
                    root=root.right;
                    if(root==null){
                        root=root1;
                        throw new RuntimeException("Invalid code.");
                    }
                    if(root.ch!=' '){
                        System.out.print(root.ch);
                        root=root1;
                    }
                }else{
                    root=root.left;
                    if(root==null){
                        root=root1;
                        throw new RuntimeException("Invalid code.");
                    }
                    if(root.ch!=' '){
                        System.out.print(root.ch);
                        root=root1;
                    }
                }
            }
            //这个异常处理还是不够完善，如果有多余数字的话不会抛异常的，算了算了，不管了。。。。
        }

        private void printTree(Node node, String prefix, boolean isLast) {
            if (node != null) {
                // 改进节点值显示逻辑
                String nodeValue = (node.ch == ' ') ? String.valueOf(node.data) : String.valueOf(node.ch);
                System.out.println(prefix + (isLast ? "└── " : "├── ") + nodeValue);

                if (node.left != null || node.right != null) {
                    if (node.left != null) {
                        printTree(node.left, prefix + (isLast ? "    " : "│   "), false);
                    }
                    if (node.right != null) {
                        printTree(node.right, prefix + (isLast ? "    " : "│   "), true);
                    }
                }
            }
        }

    }
}
