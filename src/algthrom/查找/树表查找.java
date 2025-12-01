package algthrom.查找;

/**
 * 树表查找实现类
 * 使用二叉搜索树(BST)实现高效的查找、插入和删除操作
 */
public class 树表查找 {

    /**
     * 二叉树节点定义
     */
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {}

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * 在二叉搜索树中搜索指定值的节点
     *
     * @param root 二叉搜索树的根节点
     * @param val 要搜索的值
     * @return 找到的节点，如果未找到则返回null
     */
    public TreeNode searchBST(TreeNode root, int val) {
        if (root == null || root.val == val) {
            return root;
        }

        // 如果要查找的值小于当前节点值，在左子树中查找
        if (val < root.val) {
            return searchBST(root.left, val);
        } else {
            // 如果要查找的值大于当前节点值，在右子树中查找
            return searchBST(root.right, val);
        }
    }

    /**
     * 在二叉搜索树中插入新节点
     *
     * @param root 二叉搜索树的根节点
     * @param val 要插入的值
     * @return 插入后的二叉搜索树根节点
     */
    public TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null) {
            return new TreeNode(val);
        }

        // 如果插入值小于当前节点值，插入到左子树
        if (val < root.val) {
            root.left = insertIntoBST(root.left, val);
        } 
        // 如果插入值大于当前节点值，插入到右子树
        else if (val > root.val) {
            root.right = insertIntoBST(root.right, val);
        }
        
        // 如果值相等，则不插入重复元素
        return root;
    }

    /**
     * 在二叉搜索树中删除指定值的节点
     *
     * @param root 二叉搜索树的根节点
     * @param val 要删除的值
     * @return 删除后的二叉搜索树根节点
     */
    public TreeNode deleteNode(TreeNode root, int val) {
        if (root == null) {
            return null;
        }

        // 如果要删除的值小于当前节点值，在左子树中删除
        if (val < root.val) {
            root.left = deleteNode(root.left, val);
        } 
        // 如果要删除的值大于当前节点值，在右子树中删除
        else if (val > root.val) {
            root.right = deleteNode(root.right, val);
        } 
        // 找到了要删除的节点
        else {
            // 情况1：节点没有子节点（叶子节点）
            if (root.left == null && root.right == null) {
                return null;
            }
            
            // 情况2：节点只有一个子节点
            if (root.left == null) {
                return root.right;
            }
            if (root.right == null) {
                return root.left;
            }
            
            // 情况3：节点有两个子节点
            // 找到右子树中的最小值节点（中序后继）
            TreeNode minNode = findMin(root.right);
            // 用中序后继的值替换当前节点的值
            root.val = minNode.val;
            // 删除中序后继节点
            root.right = deleteNode(root.right, minNode.val);
        }
        
        return root;
    }

    /**
     * 查找以指定节点为根的子树中的最小值节点
     *
     * @param node 子树的根节点
     * @return 最小值节点
     */
    private TreeNode findMin(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * 查找以指定节点为根的子树中的最大值节点
     *
     * @param node 子树的根节点
     * @return 最大值节点
     */
    private TreeNode findMax(TreeNode node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    /**
     * 在二叉搜索树中查找最小值
     *
     * @param root 二叉搜索树的根节点
     * @return 最小值，如果树为空则返回-1
     */
    public int findMinValue(TreeNode root) {
        if (root == null) {
            return -1; // 或抛出异常表示树为空
        }
        TreeNode minNode = findMin(root);
        return minNode.val;
    }

    /**
     * 在二叉搜索树中查找最大值
     *
     * @param root 二叉搜索树的根节点
     * @return 最大值，如果树为空则返回-1
     */
    public int findMaxValue(TreeNode root) {
        if (root == null) {
            return -1; // 或抛出异常表示树为空
        }
        TreeNode maxNode = findMax(root);
        return maxNode.val;
    }

    /**
     * 中序遍历二叉搜索树（输出有序序列）
     *
     * @param root 二叉搜索树的根节点
     */
    public void inorderTraversal(TreeNode root) {
        if (root != null) {
            inorderTraversal(root.left);
            System.out.print(root.val + " ");
            inorderTraversal(root.right);
        }
    }

    /**
     * 测试用例
     */
    public static void main(String[] args) {
        树表查找 bst = new 树表查找();
        
        // 创建一个空的二叉搜索树
        TreeNode root = null;
        
        // 插入一些节点
        System.out.println("插入节点: 50, 30, 70, 20, 40, 60, 80");
        root = bst.insertIntoBST(root, 50);
        root = bst.insertIntoBST(root, 30);
        root = bst.insertIntoBST(root, 70);
        root = bst.insertIntoBST(root, 20);
        root = bst.insertIntoBST(root, 40);
        root = bst.insertIntoBST(root, 60);
        root = bst.insertIntoBST(root, 80);
        
        // 中序遍历输出（应该是有序的）
        System.out.print("中序遍历结果: ");
        bst.inorderTraversal(root);
        System.out.println();
        
        // 搜索测试
        System.out.println("\n搜索测试:");
        TreeNode node = bst.searchBST(root, 40);
        if (node != null) {
            System.out.println("找到了节点 40");
        } else {
            System.out.println("未找到节点 40");
        }
        
        node = bst.searchBST(root, 90);
        if (node != null) {
            System.out.println("找到了节点 90");
        } else {
            System.out.println("未找到节点 90");
        }
        
        // 查找最值测试
        System.out.println("\n最值测试:");
        System.out.println("最小值: " + bst.findMinValue(root));
        System.out.println("最大值: " + bst.findMaxValue(root));
        
        // 删除测试
        System.out.println("\n删除节点 30 后:");
        root = bst.deleteNode(root, 30);
        System.out.print("中序遍历结果: ");
        bst.inorderTraversal(root);
        System.out.println();
        
        System.out.println("\n删除节点 50 (根节点) 后:");
        root = bst.deleteNode(root, 50);
        System.out.print("中序遍历结果: ");
        bst.inorderTraversal(root);
        System.out.println();
    }
}