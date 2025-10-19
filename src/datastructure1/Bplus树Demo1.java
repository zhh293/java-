package datastructure1;

public class Bplus树Demo1 {

    private static final int DEGREE = 3; // 树的度
    private Node root;

    // 节点类
    private static class Node {
        boolean isLeaf;
        int[] keys;
        Node[] children;
        int keyCount;
        Node next; // 叶子节点的链表指针

        public Node(boolean isLeaf) {
            this.isLeaf = isLeaf;
            keys = new int[2 * DEGREE - 1];
            children = new Node[2 * DEGREE];
            keyCount = 0;
            next = null;
        }
    }

    public Bplus树Demo1() {
        root = new Node(true);
    }

    // 插入操作
    public void insert(int key) {
        Node r = root;
        if (r.keyCount == 2 * DEGREE - 1) {
            Node s = new Node(false);
            root = s;
            s.children[0] = r;
            splitChild(s, 0, r);
            insertNonFull(s, key);
        } else {
            insertNonFull(r, key);
        }
    }

    private void insertNonFull(Node x, int key) {
        int i = x.keyCount - 1;
        if (x.isLeaf) {
            while (i >= 0 && key < x.keys[i]) {
                x.keys[i + 1] = x.keys[i];
                i--;
            }
            x.keys[i + 1] = key;
            x.keyCount++;
        } else {
            while (i >= 0 && key < x.keys[i]) {
                i--;
            }
            i++;
            if (x.children[i].keyCount == 2 * DEGREE - 1) {
                splitChild(x, i, x.children[i]);
                if (key > x.keys[i]) {
                    i++;
                }
            }
            insertNonFull(x.children[i], key);
        }
    }

    private void splitChild(Node parent, int index, Node child) {
        Node newNode = new Node(child.isLeaf);
        newNode.keyCount = DEGREE - 1;

        for (int j = 0; j < DEGREE - 1; j++) {
            newNode.keys[j] = child.keys[j + DEGREE];
        }

        if (!child.isLeaf) {
            for (int j = 0; j < DEGREE; j++) {
                newNode.children[j] = child.children[j + DEGREE];
            }
        }

        child.keyCount = DEGREE - 1;

        for (int j = parent.keyCount; j > index; j--) {
            parent.children[j + 1] = parent.children[j];
        }
        parent.children[index + 1] = newNode;

        for (int j = parent.keyCount - 1; j >= index; j--) {
            parent.keys[j + 1] = parent.keys[j];
        }
        parent.keys[index] = child.keys[DEGREE - 1];
        parent.keyCount++;

        if (child.isLeaf) {
            newNode.next = child.next;
            child.next = newNode;
        }
    }

    // 删除操作
    public void delete(int key) {
        if (root.keyCount == 0) {
            return;
        }
        delete(root, key);
        if (root.keyCount == 0 && !root.isLeaf) {
            root = root.children[0];
        }
    }

    private void delete(Node x, int key) {
        int index = findKeyIndex(x, key);

        if (x.isLeaf) {
            if (index < x.keyCount && x.keys[index] == key) {
                removeKey(x, index);
            }
            return;
        }

        if (index < x.keyCount && x.keys[index] == key) {
            Node leftChild = x.children[index];
            Node rightChild = x.children[index + 1];

            if (leftChild.keyCount >= DEGREE) {
                int predecessor = getPredecessor(leftChild);
                x.keys[index] = predecessor;
                delete(leftChild, predecessor);
            } else if (rightChild.keyCount >= DEGREE) {
                int successor = getSuccessor(rightChild);
                x.keys[index] = successor;
                delete(rightChild, successor);
            } else {
                mergeChildren(x, index, leftChild, rightChild);
                delete(leftChild, key);
            }
        } else {
            Node child = x.children[index];
            if (child.keyCount < DEGREE) {
                fixShortage(x, index, child);
            }
            delete(child, key);
        }
    }

    private int findKeyIndex(Node x, int key) {
        int index = 0;
        while (index < x.keyCount && key > x.keys[index]) {
            index++;
        }
        return index;
    }

    private void removeKey(Node x, int index) {
        for (int i = index; i < x.keyCount - 1; i++) {
            x.keys[i] = x.keys[i + 1];
        }
        x.keyCount--;
    }

    private int getPredecessor(Node x) {
        Node current = x;
        while (!current.isLeaf) {
            current = current.children[current.keyCount];
        }
        return current.keys[current.keyCount - 1];
    }

    private int getSuccessor(Node x) {
        Node current = x;
        while (!current.isLeaf) {
            current = current.children[0];
        }
        return current.keys[0];
    }

    private void mergeChildren(Node parent, int index, Node left, Node right) {
        left.keys[DEGREE - 1] = parent.keys[index];

        for (int i = 0; i < right.keyCount; i++) {
            left.keys[i + DEGREE] = right.keys[i];
        }

        if (!left.isLeaf) {
            for (int i = 0; i <= right.keyCount; i++) {
                left.children[i + DEGREE] = right.children[i];
            }
        }

        removeKey(parent, index);

        for (int i = index + 1; i < parent.keyCount + 1; i++) {
            parent.children[i] = parent.children[i + 1];
        }

        left.keyCount += right.keyCount + 1;

        if (left.isLeaf) {
            left.next = right.next;
        }
    }

    private void fixShortage(Node parent, int index, Node child) {
        if (index > 0 && parent.children[index - 1].keyCount > DEGREE - 1) {
            borrowFromLeftSibling(parent, index, child);
        } else if (index < parent.keyCount && parent.children[index + 1].keyCount > DEGREE - 1) {
            borrowFromRightSibling(parent, index, child);
        } else {
            if (index > 0) {
                mergeWithLeftSibling(parent, index - 1, parent.children[index - 1], child);
            } else {
                mergeWithLeftSibling(parent, index, child, parent.children[index + 1]);
            }
        }
    }

    private void borrowFromLeftSibling(Node parent, int index, Node child) {
        Node leftSibling = parent.children[index - 1];

        for (int i = child.keyCount; i > 0; i--) {
            child.keys[i] = child.keys[i - 1];
        }

        if (!child.isLeaf) {
            for (int i = child.keyCount + 1; i > 0; i--) {
                child.children[i] = child.children[i - 1];
            }
        }

        child.keys[0] = parent.keys[index - 1];

        if (!child.isLeaf) {
            child.children[0] = leftSibling.children[leftSibling.keyCount];
        }

        parent.keys[index - 1] = leftSibling.keys[leftSibling.keyCount - 1];

        child.keyCount++;
        leftSibling.keyCount--;
    }

    private void borrowFromRightSibling(Node parent, int index, Node child) {
        Node rightSibling = parent.children[index + 1];

        child.keys[child.keyCount] = parent.keys[index];

        if (!child.isLeaf) {
            child.children[child.keyCount + 1] = rightSibling.children[0];
        }

        parent.keys[index] = rightSibling.keys[0];

        for (int i = 0; i < rightSibling.keyCount - 1; i++) {
            rightSibling.keys[i] = rightSibling.keys[i + 1];
        }

        if (!rightSibling.isLeaf) {
            for (int i = 0; i < rightSibling.keyCount; i++) {
                rightSibling.children[i] = rightSibling.children[i + 1];
            }
        }

        child.keyCount++;
        rightSibling.keyCount--;
    }

    private void mergeWithLeftSibling(Node parent, int index, Node left, Node right) {
        left.keys[left.keyCount] = parent.keys[index];

        for (int i = 0; i < right.keyCount; i++) {
            left.keys[left.keyCount + 1 + i] = right.keys[i];
        }

        if (!left.isLeaf) {
            for (int i = 0; i <= right.keyCount; i++) {
                left.children[left.keyCount + 1 + i] = right.children[i];
            }
        }

        removeKey(parent, index);

        for (int i = index + 1; i < parent.keyCount + 1; i++) {
            parent.children[i] = parent.children[i + 1];
        }

        left.keyCount += right.keyCount + 1;

        if (left.isLeaf) {
            left.next = right.next;
        }
    }
    /*. B + 树的基本结构与你的描述一致
    数据库中的 B + 树确实满足：

    叶子节点：存储实际数据（如主键 ID 对应的整行数据），并且按键值（雪花 ID）从小到大排序。
    非叶子节点：仅存储索引键（通常是子节点的最大值或最小值）和指针，用于快速定位数据所在的叶子节点。

    例如，假设非叶子节点存储子节点的最大值，则结构可能如下：

    plaintext
                 [50, 100]        // 根节点：记录两个子节点的最大值
                /        \
                [1-20] [21-50]  [51-100]  // 中间节点：记录叶子节点的范围
                ...    ...     ...      // 叶子节点：存储实际数据，按ID有序排列
                2. 非叶子节点的索引键选择
    你的描述中提到 “上面的节点记录的全是叶子节点中的最大值”，这在理论上是可行的，但实际数据库实现中更常见的是：

    非叶子节点存储子节点的最小值（而非最大值）。
    例如，一个非叶子节点的键值为 [21, 51]，表示其左子树的所有键值都小于 21，中间子树的键值范围是 [21, 51)，右子树的键值大于等于 51。
    叶子节点之间通过指针相连，形成一个有序链表，支持范围查询（如 WHERE id BETWEEN 10 AND 20）。

    这种实现方式更便于快速定位和遍历数据。
            3. 数据库 B + 树的其他特性
    除了上述结构，数据库 B + 树还有以下特点：

    聚簇索引与非聚簇索引：
    主键索引（聚簇索引）的叶子节点直接存储整行数据。
    非主键索引（辅助索引）的叶子节点存储主键值，需要二次查询才能获取完整数据。
    页结构：
    B + 树的每个节点对应一个物理页（如 InnoDB 的页大小为 16KB），节点内部的数据按页组织，以提高 IO 效率。
    自平衡：
    插入 / 删除数据时，B + 树会通过分裂、合并节点等操作保持树的平衡，确保高度始终保持在 O (logN)。
            4. 雪花算法与 B + 树的适配性
    雪花算法生成的 ID 天然适合 B + 树：

    有序性：时间戳部分保证 ID 大致按时间递增，新插入的数据通常落在 B + 树的右侧，减少页分裂。
    唯一性：分布式环境下保证不重复，满足主键要求。

    相比随机 UUID，雪花 ID 能显著提升 B + 树的插入性能和空间利用率。
    总结
    你的理解基本正确：数据库中的 B + 树确实将数据按键值有序存储在叶子节点，非叶子节点作为索引层辅助快速定位。
    不过实际实现中，非叶子节点通常存储子节点的最小值而非最大值，并且叶子节点之间通过链表相连以支持范围查询。雪花算法生成的 ID 与这种结构高度适配，能有效发挥 B + 树的性能优势。*/
}
