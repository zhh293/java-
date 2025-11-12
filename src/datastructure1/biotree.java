package datastructure1;

public class biotree {
    public static void main(String[] args) {

    }
    class Node{
        int data;
        Node left;
        Node right;
    }
    public void medium(Node root){
        if(root==null){
            return;
        }
        medium(root.left);
        System.out.println(root.data);
        medium(root.right);
    }
    public void back(Node root){
        if(root==null){
            return;
        }
        System.out.println(root.data);
        back(root.left);
        back(root.right);
    }
    class Node1{
        int data;
        Node1 left;
        Node1 right;
        boolean ltag;
        boolean rtag;
    }
    // 非递归方式线索化二叉树
    public void createInThread(Node1 root) {
        if (root == null) return;

        Node1 pre = null;
        Node1 p = root;
        java.util.Stack<Node1> stack = new java.util.Stack<>();

        // 非递归中序遍历并线索化
        while (p != null || !stack.isEmpty()) {
            // 一直向左走到底
            while (p != null && p.ltag == false && p.left != null) {
                stack.push(p);
                p = p.left;
            }

            if (p == null) {
                p = stack.pop();
            }

            // 线索化当前节点
            if (p.left == null) {
                p.ltag = true;
                p.left = pre;
            }
            if (pre != null && pre.right == null) {
                pre.rtag = true;
                pre.right = p;
            }

            pre = p;
            p = p.right;
        }
    }

}
