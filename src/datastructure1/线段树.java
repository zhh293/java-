package datastructure1;

public class 线段树 {
    public void main(String[] args) {
        int[] arr = {1, 3, 5, 7, 9};
        SimpleSegmentTree st = new SimpleSegmentTree(arr);

        // 查询区间[1, 3]的和（对应原数组索引1到3，即3+5+7=15）
        System.out.println("Sum of interval [1, 3]: " + st.query(1, 3)); // 输出15

        // 更新索引1的值为10
        st.update(1, 10);
        System.out.println("Updated sum of interval [1, 3]: " + st.query(1, 3)); // 输出10+5+7=22

    }

    public class SimpleSegmentTree {
        private int[] tree;  // 存储线段树的数组
        private int n;       // 原始数组的长度

        // 构造函数：根据原始数组构建线段树
        public SimpleSegmentTree(int[] arr) {
            n = arr.length;
            tree = new int[4 * n];  // 线段树数组大小通常为4n
            buildTree(0, arr, 0, n - 1);
        }

        // 递归构建线段树
        private void buildTree(int node, int[] arr, int left, int right) {
            if (left == right) {
                // 叶节点：直接存储元素值
                tree[node] = arr[left];
            } else {
                int mid = (left + right) / 2;
                // 递归构建左子树和右子树
                buildTree(2 * node + 1, arr, left, mid);
                buildTree(2 * node + 2, arr, mid + 1, right);
                // 当前节点的值为左右子树的和
                tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
            }
        }

        // 查询区间[queryLeft, queryRight]的和
        public int query(int queryLeft, int queryRight) {
            return query(0, 0, n - 1, queryLeft, queryRight);
        }

        private int query(int node, int left, int right, int queryLeft, int queryRight) {
            // 当前区间完全在查询区间外
            if (right < queryLeft || left > queryRight) {
                return 0;
            }
            // 当前区间完全在查询区间内
            if (queryLeft <= left && right <= queryRight) {
                return tree[node];
            }
            // 部分重叠，递归查询左右子树
            int mid = (left + right) / 2;
            int leftSum = query(2 * node + 1, left, mid, queryLeft, queryRight);
            int rightSum = query(2 * node + 2, mid + 1, right, queryLeft, queryRight);
            return leftSum + rightSum;
        }
/*
* 0 4
* 0 2 3 4
* 0 1 2 2 3 3 4 4
* */
        // 更新指定位置的值
        public void update(int index, int newValue) {
            update(0, 0, n - 1, index, newValue);
        }

        private void update(int node, int left, int right, int index, int newValue) {
            if (left == right) {
                // 找到对应叶节点，更新值
                tree[node] = newValue;
            } else {
                int mid = (left + right) / 2;
                if (index <= mid) {
                    // 索引在左子树
                    update(2 * node + 1, left, mid, index, newValue);
                } else {
                    // 索引在右子树
                    update(2 * node + 2, mid + 1, right, index, newValue);
                }
                // 更新父节点的值
                tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
            }
        }

    }
}
