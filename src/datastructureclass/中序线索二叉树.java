package datastructureclass;

public class 中序线索二叉树 {
    static class Node {
        int val;
        Node left;
        Node right;
        int leftType;  // 0表示指向左子树，1表示指向前驱节点
        int rightType;// 0表示指向右子树，1表示指向后驱节点
        public Node(int val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        //先构建线索二叉树
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.right = new Node(7);
        Node pre=null;
        threadBinaryTree(root,pre);
    }

    private static void threadBinaryTree(Node root,Node  pre) {
        if(root==null){
            return;
        }
        //中序遍历线索化
        //思路 ：中序遍历，记录上一个访问的节点，将当前节点的左指针指向上一个节点，将当前节点的右指针指向下一个节点
        threadBinaryTree(root.left,pre);
        if(root.left==null){
            root.left=pre;
            root.leftType=1;
        }

        if(pre!=null&&pre.right==null){
            pre.right=root;
            pre.rightType=1;
        }
        pre=root;
        threadBinaryTree(root.right,pre);
    }
    private static void insert(Node root,int val){
        if(root==null){
            root=new Node(val);
        }else if(val<root.val){
            insert(root.left,val);
        }else{
            insert(root.right,val);
        }
    }
    private static void print(Node root){
        Node current=root;
        while(current!=null){
            while(current.leftType==0){
                current=current.left;
            }
            System.out.println(current.val);
            while(current.rightType==1){
                current=current.right;
                System.out.println(current.val);
            }
            current=current.right;
        }
    }
}
