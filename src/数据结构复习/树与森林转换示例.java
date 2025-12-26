package 数据结构复习;

import java.util.*;

/**
 * 树与森林转换示例代码
 * 演示树、森林与二叉树之间的相互转换
 */
public class 树与森林转换示例 {
    
    // 普通树节点
    static class TreeNode {
        int val;
        List<TreeNode> children;
        
        TreeNode(int val) {
            this.val = val;
            this.children = new ArrayList<>();
        }
    }
    
    // 二叉树节点
    static class BinaryTreeNode {
        int val;
        BinaryTreeNode left;
        BinaryTreeNode right;
        
        BinaryTreeNode(int val) {
            this.val = val;
            this.left = null;
            this.right = null;
        }
    }
    
    /**
     * 树转换为二叉树
     * 转换规则：左子树表示第一个孩子，右子树表示右兄弟
     */
    public static BinaryTreeNode treeToBinaryTree(TreeNode root) {
        if (root == null) return null;
        
        // 创建二叉树根节点
        BinaryTreeNode binaryRoot = new BinaryTreeNode(root.val);
        
        if (!root.children.isEmpty()) {
            // 左子树是第一个孩子
            binaryRoot.left = treeToBinaryTree(root.children.get(0));
            
            // 将其他孩子作为第一个孩子的右兄弟链
            BinaryTreeNode current = binaryRoot.left;
            for (int i = 1; i < root.children.size(); i++) {
                current.right = treeToBinaryTree(root.children.get(i));
                current = current.right;
            }
        }
        
        return binaryRoot;
    }
    
    /**
     * 二叉树转换为树
     */
    public static TreeNode binaryTreeToTree(BinaryTreeNode root) {
        if (root == null) return null;
        
        TreeNode treeRoot = new TreeNode(root.val);
        
        // 左子树转换为第一个孩子
        BinaryTreeNode child = root.left;
        while (child != null) {
            treeRoot.children.add(binaryTreeToTree(child));
            child = child.right; // 右子树是兄弟
        }
        
        return treeRoot;
    }
    
    /**
     * 森林转换为二叉树
     * 森林中的每棵树都转换为二叉树，然后将后一棵树作为前一棵树根节点的右子树
     */
    public static BinaryTreeNode forestToBinaryTree(List<TreeNode> forest) {
        if (forest == null || forest.isEmpty()) return null;
        
        BinaryTreeNode result = null;
        BinaryTreeNode current = null;
        
        for (TreeNode tree : forest) {
            BinaryTreeNode binaryTree = treeToBinaryTree(tree);
            if (result == null) {
                result = binaryTree;
                current = result;
            } else {
                current.right = binaryTree;
                current = current.right;
            }
        }
        
        return result;
    }
    
    /**
     * 二叉树转换为森林
     */
    public static List<TreeNode> binaryTreeToForest(BinaryTreeNode root) {
        List<TreeNode> forest = new ArrayList<>();
        
        BinaryTreeNode current = root;
        while (current != null) {
            forest.add(binaryTreeToTree(current));
            current = current.right;
        }
        
        return forest;
    }
    
    /**
     * 层序遍历二叉树（用于输出结果）
     */
    public static void printBinaryTree(BinaryTreeNode root) {
        if (root == null) return;
        
        Queue<BinaryTreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                BinaryTreeNode node = queue.poll();
                System.out.print(node.val + " ");
                
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            System.out.println(); // 换行表示一层
        }
    }
    
    /**
     * 输出树结构（用于验证转换结果）
     */
    public static void printTree(TreeNode root, String prefix) {
        if (root == null) return;
        
        System.out.println(prefix + root.val);
        
        for (int i = 0; i < root.children.size(); i++) {
            if (i == root.children.size() - 1) {
                printTree(root.children.get(i), prefix + "    "); // 最后一个孩子
            } else {
                printTree(root.children.get(i), prefix + "   |"); // 非最后一个孩子
            }
        }
    }
    
    public static void main(String[] args) {
        // 创建一个示例树
        //       1
        //    /  |  \
        //   2   3   4
        //  /|   |
        // 5 6   7
        TreeNode root = new TreeNode(1);
        TreeNode node2 = new TreeNode(2);
        TreeNode node3 = new TreeNode(3);
        TreeNode node4 = new TreeNode(4);
        TreeNode node5 = new TreeNode(5);
        TreeNode node6 = new TreeNode(6);
        TreeNode node7 = new TreeNode(7);
        
        root.children.add(node2);
        root.children.add(node3);
        root.children.add(node4);
        
        node2.children.add(node5);
        node2.children.add(node6);
        
        node3.children.add(node7);
        
        System.out.println("原始树结构：");
        printTree(root, "");
        System.out.println();
        
        // 树转换为二叉树
        BinaryTreeNode binaryTree = treeToBinaryTree(root);
        System.out.println("转换为二叉树后的层序遍历：");
        printBinaryTree(binaryTree);
        System.out.println();
        
        // 二叉树转换回树
        TreeNode convertedTree = binaryTreeToTree(binaryTree);
        System.out.println("二叉树转换回树后的结构：");
        printTree(convertedTree, "");
        System.out.println();
        
        // 演示森林转换
        TreeNode tree1 = new TreeNode(10);
        TreeNode tree1Child = new TreeNode(11);
        tree1.children.add(tree1Child);
        
        TreeNode tree2 = new TreeNode(20);
        TreeNode tree2Child1 = new TreeNode(21);
        TreeNode tree2Child2 = new TreeNode(22);
        tree2.children.add(tree2Child1);
        tree2.children.add(tree2Child2);
        
        List<TreeNode> forest = Arrays.asList(tree1, tree2);
        System.out.println("原始森林：");
        for (int i = 0; i < forest.size(); i++) {
            System.out.println("树 " + (i + 1) + ":");
            printTree(forest.get(i), "  ");
        }
        
        // 森林转换为二叉树
        BinaryTreeNode forestAsBinaryTree = forestToBinaryTree(forest);
        System.out.println("\n森林转换为二叉树后的层序遍历：");
        printBinaryTree(forestAsBinaryTree);
        
        // 二叉树转换回森林
        List<TreeNode> convertedForest = binaryTreeToForest(forestAsBinaryTree);
        System.out.println("\n二叉树转换回森林：");
        for (int i = 0; i < convertedForest.size(); i++) {
            System.out.println("树 " + (i + 1) + ":");
            printTree(convertedForest.get(i), "  ");
        }
    }
}