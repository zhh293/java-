# AVL树（平衡二叉树）详解

## 一、AVL树概述

### 1.1 基本概念

**AVL树**：由Adelson-Velsky和Landis在1962年提出，是一种自平衡二叉搜索树。AVL树在二叉搜索树的基础上增加了平衡条件：对于树中任意节点，其左右子树的高度差不超过1。

### 1.2 AVL树的性质

1. **二叉搜索树性质**：左子树所有节点值 < 根节点值 < 右子树所有节点值
2. **平衡性质**：任意节点的左右子树高度差绝对值 ≤ 1
3. **高度性质**：n个节点的AVL树高度为O(log n)
4. **平衡因子**：节点左子树高度 - 右子树高度，取值范围为{-1, 0, 1}

### 1.3 AVL树的优势

- **查找效率**：由于保持平衡，查找、插入、删除操作的时间复杂度均为O(log n)
- **自平衡**：通过旋转操作自动维持树的平衡
- **稳定性能**：在最坏情况下仍能保持对数时间复杂度

## 二、AVL树节点结构

```java
class AVLNode {
    int data;           // 节点值
    int height;         // 节点高度
    AVLNode left;       // 左子节点
    AVLNode right;      // 右子节点
    
    public AVLNode(int data) {
        this.data = data;
        this.height = 1;  // 新节点初始高度为1
        this.left = null;
        this.right = null;
    }
}
```

## 三、AVL树的基本操作

### 3.1 获取节点高度

```java
private int getHeight(AVLNode node) {
    if (node == null) {
        return 0;
    }
    return node.height;
}
```

### 3.2 计算平衡因子

```java
private int getBalanceFactor(AVLNode node) {
    if (node == null) {
        return 0;
    }
    return getHeight(node.left) - getHeight(node.right);
}
```

### 3.3 更新节点高度

```java
private void updateHeight(AVLNode node) {
    if (node != null) {
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }
}
```

## 四、AVL树的四种旋转操作

### 4.1 右旋转（LL旋转）

**触发条件**：插入节点在失衡节点的左子树的左子树中（左-左情况）

**旋转过程**：
1. 失衡节点A的左子节点B成为新的根节点
2. A成为B的右子节点
3. B原来的右子树成为A的左子树

```java
private AVLNode rotateRight(AVLNode y) {
    AVLNode x = y.left;
    AVLNode T2 = x.right;
    
    // 执行旋转
    x.right = y;
    y.left = T2;
    
    // 更新高度
    updateHeight(y);
    updateHeight(x);
    
    // 返回新的根节点
    return x;
}
```

**图示过程**：
```
     y                x
    / \              / \
   x   T3    -->    T1  y
  / \                  / \
 T1  T2               T2  T3
```

### 4.2 左旋转（RR旋转）

**触发条件**：插入节点在失衡节点的右子树的右子树中（右-右情况）

**旋转过程**：
1. 失衡节点A的右子节点B成为新的根节点
2. A成为B的左子节点
3. B原来的左子树成为A的右子树

```java
private AVLNode rotateLeft(AVLNode x) {
    AVLNode y = x.right;
    AVLNode T2 = y.left;
    
    // 执行旋转
    y.left = x;
    x.right = T2;
    
    // 更新高度
    updateHeight(x);
    updateHeight(y);
    
    // 返回新的根节点
    return y;
}
```

**图示过程**：
```
   x                    y
  / \                  / \
 T1  y        -->     x   T3
    / \              / \
   T2  T3           T1  T2
```

### 4.3 左右旋转（LR旋转）

**触发条件**：插入节点在失衡节点的左子树的右子树中（左-右情况）

**旋转过程**：
1. 先对失衡节点的左子树进行左旋转
2. 再对整个树进行右旋转

```java
private AVLNode rotateLeftRight(AVLNode node) {
    // 先对左子树进行左旋转
    node.left = rotateLeft(node.left);
    // 再对整棵树进行右旋转
    return rotateRight(node);
}
```

**图示过程**：
```
     z                 z               x
    / \               / \             / \
   y   T4    -->     x   T4   -->    y   z
  / \               / \             /|   |\
 T1  x             y   T3          T1T2 T3T4
    / \           / \
   T2  T3        T1  T2
```

### 4.4 右左旋转（RL旋转）

**触发条件**：插入节点在失衡节点的右子树的左子树中（右-左情况）

**旋转过程**：
1. 先对失衡节点的右子树进行右旋转
2. 再对整个树进行左旋转

```java
private AVLNode rotateRightLeft(AVLNode node) {
    // 先对右子树进行右旋转
    node.right = rotateRight(node.right);
    // 再对整棵树进行左旋转
    return rotateLeft(node);
}
```

**图示过程**：
```
   z                 z               x
  / \               / \             / \
 T1  y     -->     T1  x   -->     z   y
    / \               / \           |\  |\
   x   T4            T2  y         T1T2T3T4
  / \                   / \
 T2  T3                T3  T4
```

## 五、AVL树的插入操作

### 5.1 插入算法步骤

1. **标准BST插入**：按照二叉搜索树的规则插入新节点
2. **更新高度**：更新从插入节点到根节点路径上所有节点的高度
3. **计算平衡因子**：计算当前节点的平衡因子
4. **检查失衡**：如果平衡因子的绝对值大于1，则需要旋转
5. **执行旋转**：根据插入位置确定旋转类型并执行

### 5.2 插入操作实现

```java
public class AVLTree {
    private AVLNode root;
    
    public AVLNode insert(AVLNode node, int data) {
        // 1. 执行标准BST插入
        if (node == null) {
            return new AVLNode(data);
        }
        
        if (data < node.data) {
            node.left = insert(node.left, data);
        } else if (data > node.data) {
            node.right = insert(node.right, data);
        } else {
            // 相等的值不插入
            return node;
        }
        
        // 2. 更新当前节点的高度
        updateHeight(node);
        
        // 3. 获取平衡因子
        int balance = getBalanceFactor(node);
        
        // 4. 如果节点失衡，有4种情况
        
        // Left Left Case (LL旋转)
        if (balance > 1 && data < node.left.data) {
            return rotateRight(node);
        }
        
        // Right Right Case (RR旋转)
        if (balance < -1 && data > node.right.data) {
            return rotateLeft(node);
        }
        
        // Left Right Case (LR旋转)
        if (balance > 1 && data > node.left.data) {
            return rotateLeftRight(node);
        }
        
        // Right Left Case (RL旋转)
        if (balance < -1 && data < node.right.data) {
            return rotateRightLeft(node);
        }
        
        // 返回未改变的节点指针
        return node;
    }
    
    // 公共插入方法
    public void insert(int data) {
        root = insert(root, data);
    }
}
```

## 六、AVL树的删除操作

### 6.1 删除算法步骤

1. **标准BST删除**：按照二叉搜索树的规则删除节点
2. **更新高度**：更新从删除节点到根节点路径上所有节点的高度
3. **计算平衡因子**：计算当前节点的平衡因子
4. **检查失衡**：如果平衡因子的绝对值大于1，则需要旋转
5. **执行旋转**：根据子树情况确定旋转类型并执行

### 6.2 删除操作实现

```java
public AVLNode delete(AVLNode node, int data) {
    // 1. 执行标准BST删除
    if (node == null) {
        return node;
    }
    
    if (data < node.data) {
        node.left = delete(node.left, data);
    } else if (data > node.data) {
        node.right = delete(node.right, data);
    } else {
        // 找到要删除的节点
        if (node.left == null || node.right == null) {
            AVLNode temp = null;
            if (temp == node.left) {
                temp = node.right;
            } else {
                temp = node.left;
            }
            
            // 没有子节点的情况
            if (temp == null) {
                temp = node;
                node = null;
            } else {
                // 一个子节点的情况
                node = temp;
            }
        } else {
            // 有两个子节点的情况
            // 找到右子树中的最小值节点（中序后继）
            AVLNode temp = findMinNode(node.right);
            
            // 用后继节点的值替换当前节点的值
            node.data = temp.data;
            
            // 删除后继节点
            node.right = delete(node.right, temp.data);
        }
    }
    
    // 如果树为空，直接返回
    if (node == null) {
        return node;
    }
    
    // 2. 更新当前节点的高度
    updateHeight(node);
    
    // 3. 获取平衡因子
    int balance = getBalanceFactor(node);
    
    // 4. 如果节点失衡，有4种情况
    
    // Left Left Case
    if (balance > 1 && getBalanceFactor(node.left) >= 0) {
        return rotateRight(node);
    }
    
    // Left Right Case
    if (balance > 1 && getBalanceFactor(node.left) < 0) {
        return rotateLeftRight(node);
    }
    
    // Right Right Case
    if (balance < -1 && getBalanceFactor(node.right) <= 0) {
        return rotateLeft(node);
    }
    
    // Right Left Case
    if (balance < -1 && getBalanceFactor(node.right) > 0) {
        return rotateRightLeft(node);
    }
    
    return node;
}

private AVLNode findMinNode(AVLNode node) {
    AVLNode current = node;
    while (current.left != null) {
        current = current.left;
    }
    return current;
}
```

## 七、AVL树构建示例

### 7.1 构建过程示例

假设我们要依次插入序列 [10, 20, 30, 40, 50, 25]：

**步骤1**：插入10
```
  10
```

**步骤2**：插入20
```
  10
   \
    20
```

**步骤3**：插入30 - 发生RR情况，需要左旋转
```
    20
   /  \
  10   30
```

**步骤4**：插入40
```
    20
   /  \
  10   30
        \
         40
```

**步骤5**：插入50 - 发生RR情况，对节点30进行左旋转
```
    20
   /  \
  10   40
      /  \
     30   50
```

**步骤6**：插入25 - 发生RL情况，需要右左旋转
```
     20
    /  \
   10   40
      /   \
     25    50
    /
   30
```
先对节点40进行右旋转，再对根节点20进行左旋转：
```
     25
    /  \
   20   40
  /    /  \
 10   30   50
```

## 八、AVL树的性能分析

### 8.1 时间复杂度

- **查找操作**：O(log n) - 由于树保持平衡
- **插入操作**：O(log n) - 包括查找位置O(log n)和可能的旋转O(1)
- **删除操作**：O(log n) - 包括查找节点O(log n)和可能的旋转O(1)

### 8.2 空间复杂度

- **存储空间**：O(n) - 每个节点需要额外存储高度信息
- **递归栈空间**：O(log n) - 递归调用栈的深度

### 8.3 与普通BST的比较

| 操作 | 普通BST | AVL树 |
|------|---------|-------|
| 最好情况 | O(log n) | O(log n) |
| 平均情况 | O(log n) | O(log n) |
| 最坏情况 | O(n) | O(log n) |
| 空间开销 | O(n) | O(n) + 高度信息 |

## 九、AVL树的优缺点

### 9.1 优点

1. **稳定性能**：保证O(log n)的查找、插入、删除时间复杂度
2. **平衡性好**：严格控制左右子树高度差
3. **适合查找密集型应用**：查找性能优秀

### 9.2 缺点

1. **实现复杂**：需要维护平衡条件和执行旋转操作
2. **插入删除开销大**：频繁的旋转操作可能影响性能
3. **空间开销**：需要额外存储高度信息

## 十、实际应用场景

1. **数据库索引**：需要频繁查找的场景
2. **内存管理**：管理动态分配的内存块
3. **文件系统**：管理文件目录结构
4. **游戏开发**：场景管理、碰撞检测等

AVL树通过严格的平衡条件和旋转操作，确保了二叉搜索树在各种操作下都能保持良好的性能，是数据结构中重要的自平衡树结构之一。