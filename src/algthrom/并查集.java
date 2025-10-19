package algthrom;

public class 并查集 {
    // 父节点数组，记录每个元素的父节点
    private int[] parent;
    // 秩数组，用于优化合并操作
    private int[] rank;

    // 初始化并查集
    public 并查集(int n) {
        parent = new int[n];
        rank = new int[n];
        // 每个元素初始时父节点是自己，秩为1
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 1;
        }
    }

    // 查找元素的根节点，带路径压缩优化
    public int find(int x) {
        // 如果x不是根节点，递归查找其父节点，并将x直接指向根节点（路径压缩）
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    // 合并两个元素所在的集合，带按秩合并优化
    public void union(int x, int y) {
        // 找到两个元素的根节点
        int rootX = find(x);
        int rootY = find(y);

        // 如果已经在同一个集合中，无需合并
        if (rootX == rootY) {
            return;
        }

        // 按秩合并：将秩较小的树合并到秩较大的树下面
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            // 如果秩相等，合并后秩加1
            parent[rootY] = rootX;
            rank[rootX]++;
        }
    }

    // 判断两个元素是否在同一个集合中
    public boolean isConnected(int x, int y) {
        return find(x) == find(y);
    }

    // 测试代码
    public static void main(String[] args) {
        // 创建一个包含5个元素的并查集
        并查集 uf=new 并查集(5);

        // 合并一些元素
        uf.union(0, 1);
        uf.union(1, 2);
        uf.union(3, 4);

        // 检查连接性
        System.out.println("0和2是否连通：" + uf.isConnected(0, 2)); // true
        System.out.println("0和3是否连通：" + uf.isConnected(0, 3)); // false

        // 再合并2和3
        uf.union(2, 3);
        System.out.println("0和3是否连通：" + uf.isConnected(0, 3)); // true
    }
}
