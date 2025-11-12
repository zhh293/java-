package datastructure1;

public class 二叉搜索树 {
    class Node {
        int balance;
        int key;
        Node left;
        Node right;

        public Node(int key) {
            this.key = key;
            this.balance = 0;
        }
    }

    class BST {
        private Node root;

        public BST(int[] arr) {
            for (int i = 0; i < arr.length; i++) {
                root = insert(root, arr[i]);
            }
        }

        // 递归插入并自动平衡
        private Node insert(Node node, int key) {
            if (node == null) {
                return new Node(key);
            }

            if (key < node.key) {
                node.left = insert(node.left, key);
                node.balance--;
            } else if (key > node.key) {
                node.right = insert(node.right, key);
                node.balance++;
            } else {
                return node; // 重复键值不插入
            }

            // 检查是否需要平衡
            return balance(node);
        }

        // 平衡节点
        private Node balance(Node node) {
            if (node.balance < -1) { // 左侧过重
                if (node.left.balance > 0) { // LR情况
                    node.left = rotateLeft(node.left);
                }
                return rotateRight(node); // LL情况
            } else if (node.balance > 1) { // 右侧过重
                if (node.right.balance < 0) { // RL情况
                    node.right = rotateRight(node.right);
                }
                return rotateLeft(node); // RR情况
            }
            return node;
        }

        // 右单旋
        private Node rotateRight(Node node) {
            Node newRoot = node.left;
            node.left = newRoot.right;
            newRoot.right = node;

            // 更新平衡因子
            node.balance = node.balance - 1 - Math.max(0, newRoot.balance);
            newRoot.balance = newRoot.balance - 1 + Math.min(0, node.balance);

            return newRoot;
        }

        // 左单旋
        private Node rotateLeft(Node node) {
            Node newRoot = node.right;
            node.right = newRoot.left;
            newRoot.left = node;

            // 更新平衡因子
            node.balance = node.balance + 1 - Math.min(0, newRoot.balance);
            newRoot.balance = newRoot.balance + 1 + Math.max(0, node.balance);

            return newRoot;
        }

        // 打印树结构
        public void printTree() {
            System.out.println("二叉搜索树结构:");
            printTree(root, "", true);
        }

        private void printTree(Node node, String prefix, boolean isLast) {
            if (node != null) {
                System.out.println(prefix + (isLast ? "└── " : "├── ") +
                        node.key + "(bf:" + node.balance + ")");

                if (node.left != null || node.right != null) {
                    if (node.left != null) {
                        printTree(node.left, prefix + (isLast ? "    " : "│   "),
                                node.right == null);
                    }
                    if (node.right != null) {
                        printTree(node.right, prefix + (isLast ? "    " : "│   "), true);
                    }
                }
            }
        }

        // 中序遍历打印
        public void inOrder() {
            System.out.print("中序遍历: ");
            inOrder(root);
            System.out.println();
        }

        private void inOrder(Node node) {
            if (node != null) {
                inOrder(node.left);
                System.out.print(node.key + " ");
                inOrder(node.right);
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        二叉搜索树 tree = new 二叉搜索树();
        BST bst = tree.new BST(arr);

        bst.printTree();
        bst.inOrder();
    }
}
