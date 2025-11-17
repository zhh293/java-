package datastructure1;

import java.util.Scanner;

public class 头歌 {
    public static void main(String[] args) {



        /*int student=0;
        Scanner sc=new Scanner(System.in);
        student=sc.nextInt();
        int[] nums=new int[student];
        for(int i=0;i<student;i++){
            nums[i]=sc.nextInt();
        }
        int g=sc.nextInt();
        int count1=0;
        for(int i=0;i<student;i++){
            if(nums[i]==g){
                count1++;
            }
        }
        System.out.println(count1);*/
    }

    //设 a [0:n-1] 是有 n 个元素的数组，k (0≤k≤n-1) 是一个非负整数。设计一个算法将子数组 a [0:k-1] 和 a [k:n-1] 换位。要求算法在最坏情况下耗时 O (n)，且只用到 O (1) 的辅助空间。
    class Node {
        int val;
        Node left;
        Node right;
        Node(int x) {
            val = x;
        }
    }
    public class BST{
        public Node root;
        public BST() {
            root = null;
        }
        public void insert(int x){
            root = insert(root, x);
        }
        private Node insert(Node node, int x) {
            if (node == null) {
                node = new Node(x);
            } else if (x < node.val) {
                node.left = insert(node.left, x);
            } else {
                node.right = insert(node.right, x);
            }
            return node;
        }
        public Node find(int x) {
            return find(root, x);
        }
        private Node find(Node node, int x) {
            if (node == null) {
                return null;
            }
            if (x == node.val) {
                return node;
            } else if (x < node.val) {
                return find(node.left, x);
            } else {
                return find(node.right, x);
            }
        }
       /* 当要删除的节点有两个子节点时，不能简单地删除它
        需要找到一个合适的替代者：通常选择右子树中的最小节点（中序后继）
        用这个后继节点替换被删除的节点位置
                同时从原来的位置上移除这个后继节点*/
        public void delete(int x) {
            root = delete(root, x);
        }
        private Node delete(Node node, int x) {
            if (node == null) {
                return null;
            }
            if (x < node.val) {
                node.left = delete(node.left, x);
            }
            else if (x > node.val) {
                node.right = delete(node.right, x);
            } else {
                if (node.left == null) {
                    return node.right;
                } else if (node.right == null) {
                    return node.left;
                } else {
                    Node temp = node;
                    node = min(temp.right);
                    node.right = deleteMin(temp.right);
                    node.left = temp.left;
                }
            }
            return node;
        }
        private Node min(Node node) {
            if (node.left == null) {
                return node;
            }
            return min(node.left);
        }
        private Node deleteMin(Node node) {
            if (node.left == null) {
                return node.right;
            }
            node.left = deleteMin(node.left);
            return node;
        }
    }
}



/*题目：二叉搜索树

描述：
习题要求：
a. 构建二叉搜索树
b. 二叉搜索树的查找值为x的结点，并返回此结点
c. 二叉搜索树的插入x。
d. 二叉搜索树的删除：复制删除
用例：
假如输入为：
        1 2 3 4 5 0
        5
        5
则输出为：
输入int型，以0为结束
1 2 3 4 5
        1 2 3 4 5 5
输入一个需要查找的值
查找的值为: 5
        1 2 3 4 5*/



/*

题目：最大堆

描述：
习题要求：
a. 最大堆构建
b. SiftDown函数从left向下调整堆，使序列成为堆
c. SiftUp函数从position向上调整堆，使序列成为堆
d. 删除给定下标的元素
e. 从堆中插入新元素newNode
f. 从堆顶删除最大值

用例：
假如输入为：
        10 20
        20  12   35   15   10   80   30    17    2   1
        6  3   74
则输出为：
构建堆后为:80 17 35 15 10 20 30 12 2 1
位置6是叶结点
80 17 35 15 10 20 30 12 2 1
移除最大值后:35 17 30 15 10 20 1 12 2
删除下标为3的元素之后为:35 17 30 12 10 20 1 2
删除下标为3的元素为15
插入74后为:74 35 30 17 10 20 1 2 12

*/
