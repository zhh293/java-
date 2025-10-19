package datastructure1;

public class 并查集 {
    public static void main(String[] args) {
    }
    public int findCircleNumNum(int[][] isConnected) {
        // 初始化并查集（使用英文类名）
        UnionFind union = new UnionFind(isConnected.length);

        // 优化循环：只遍历上三角（i < j），避免重复合并
        for (int i = 0; i < isConnected.length; i++) {
            for (int j = i + 1; j < isConnected.length; j++) {  // j从i+1开始
                if (isConnected[i][j] == 1) {
                    union.union(i, j);
                }
            }
        }

        // 统计根节点数量（即省份数量）
        int count = 0;
        for (int i = 0; i < isConnected.length; i++) {
            if (union.find(i) == i) {  // 必须用find()，因为parent可能未路径压缩完全
                count++;
            }
        }
        return count;
    }
}
class UnionFind {
    private int[] parent;
    private int[] size;
    public UnionFind(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }
    public int find(int x) {
        while (x != parent[x]) {
            parent[x] = find(parent[x]);
            x = parent[x];
        }
        return x;
    }
    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX != rootY) {
            if (size[rootX] > size[rootY]) {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            } else {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            }
        }
    }
}