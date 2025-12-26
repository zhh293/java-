# B+树详细操作教程

## 一、B+树基本概念

### 1.1 B+树定义
B+树是一种**平衡的多路查找树**，是B树的变种，广泛应用于数据库和操作系统的文件系统中。

### 1.2 B+树特性
- **阶数M**：除根节点外，每个节点最多包含M个子节点
- **键值对分布**：所有数据都存储在叶子节点，内部节点只存储索引信息
- **叶子节点链表**：所有叶子节点通过指针连接，便于范围查询
- **平衡性**：所有叶子节点到根节点的距离相同

### 1.3 节点结构
- **内部节点**：存储键值和指向子节点的指针
- **叶子节点**：存储键值和数据，以及指向相邻叶子节点的指针

## 二、B+树创建操作

### 2.1 初始化根节点
```cpp
// 创建空的B+树根节点
BPlusNode* createRoot() {
    BPlusNode* root = (BPlusNode*)malloc(sizeof(BPlusNode));
    root->isLeaf = true;  // 初始时根节点也是叶子节点
    root->keyCount = 0;
    root->keys = (int*)malloc(M * sizeof(int));
    root->children = NULL;
    root->nextLeaf = NULL;
    return root;
}
```

### 2.2 创建过程要点
1. **确定阶数M**：根据存储系统特性选择合适的阶数
2. **初始化根节点**：创建第一个叶子节点
3. **设置参数**：包括最小键值数、最大键值数等

## 三、B+树插入操作

### 3.1 插入基本原理
B+树插入遵循"**先插入，后分裂**"的原则：

#### 步骤1：定位插入位置
- 从根节点开始，根据键值大小向下遍历
- 对于内部节点，找到合适的子节点继续向下
- 直到找到目标叶子节点

#### 步骤2：在叶子节点插入
- 将新键值插入到叶子节点的合适位置
- 保持节点内键值有序

#### 步骤3：检查节点是否溢出
- 如果节点键值数量超过M-1，则需要分裂

### 3.2 节点分裂操作
当叶子节点满时的分裂过程：

```
分裂前（假设M=4，节点满）：
[10, 20, 30] -> [data1, data2, data3, data4]

分裂后：
叶子节点1：[10]
叶子节点2：[20, 30] 
叶子节点1 -> 叶子节点2
```

#### 分裂算法：
```cpp
void splitLeafNode(BPlusNode* parent, BPlusNode* leaf, int index) {
    BPlusNode* newLeaf = createNode(true);
    
    // 计算分裂点
    int splitPoint = M / 2;
    
    // 移动后半部分键值到新节点
    int j = 0;
    for (int i = splitPoint; i < leaf->keyCount; i++) {
        newLeaf->keys[j] = leaf->keys[i];
        newLeaf->data[j] = leaf->data[i];
        j++;
    }
    
    // 更新节点计数
    leaf->keyCount = splitPoint;
    newLeaf->keyCount = j;
    
    // 连接叶子节点链表
    newLeaf->nextLeaf = leaf->nextLeaf;
    leaf->nextLeaf = newLeaf;
    
    // 将新节点插入到父节点
    insertIntoParent(parent, index, newLeaf);
}
```

### 3.3 插入示例
插入序列：[10, 20, 30, 40, 50, 15, 25]

```
步骤1：插入10, 20, 30
根节点：[10, 20, 30]

步骤2：插入40
节点[10, 20, 30, 40]溢出，需要分裂
分裂为：[10, 20] 和 [30, 40]
创建内部节点：[20]
           [20]
         /      \
    [10,20]   [30,40]

步骤3：继续插入其他值...
```

## 四、B+树删除操作

### 4.1 删除基本原理
B+树删除遵循"**先删除，后合并/旋转**"的原则：

#### 情况1：从叶子节点删除
- 直接删除指定键值
- 检查是否低于最小键值数要求

#### 情况2：从内部节点删除
- 找到前驱或后继替换
- 递归删除替换的键值

### 4.2 节点合并与旋转

#### 4.2.1 节点合并（Merge）
当节点键值数低于最小要求时：

```
合并前：
父节点：[20, 40]
左子：[10, 15]
右子：[25]

合并后：
父节点：[40]  （删除20）
合并节点：[10, 15, 25]
```

#### 4.2.2 节点旋转（Rotation）
从兄弟节点借键值：

```
旋转前：
左节点：[10]  （少于最小要求）
右节点：[30, 40, 50]  （有多余键值）

旋转后：
左节点：[10, 30]
右节点：[40, 50]
父节点相应调整
```

### 4.3 删除算法实现
```cpp
bool deleteFromBPlusTree(BPlusNode* root, int key) {
    bool result = deleteKey(root, key);
    
    // 检查根节点是否为空
    if (root->keyCount == 0 && !root->isLeaf) {
        BPlusNode* oldRoot = root;
        root = (BPlusNode*)root->children[0];  // 更新根节点
        free(oldRoot);
    }
    
    return result;
}

bool deleteKey(BPlusNode* node, int key) {
    if (node->isLeaf) {
        // 在叶子节点中删除
        return deleteFromLeaf(node, key);
    } else {
        // 在内部节点中删除
        int index = findChildIndex(node, key);
        BPlusNode* child = (BPlusNode*)node->children[index];
        
        // 检查子节点键值数是否足够
        if (child->keyCount < (M + 1) / 2) {
            // 需要借键值或合并
            handleUnderflow(node, index);
        }
        
        return deleteKey(child, key);
    }
}
```

### 4.4 删除示例
从B+树中删除键值20：

```
删除前：
           [20, 40]
         /    |     \
    [10,15] [25,30] [45,50]

删除20后（需要从子节点获取前驱）：
用15替换20，然后删除叶子节点中的15：
           [15, 40]  
         /    |     \
    [10]   [25,30] [45,50]
```

## 五、B+树操作的时间复杂度

### 5.1 时间复杂度分析
- **搜索操作**：O(log_M n) - 树的高度
- **插入操作**：O(log_M n) - 最多需要向上分裂到根
- **删除操作**：O(log_M n) - 最多需要向上合并到根

### 5.2 空间复杂度
- O(n) - 每个键值需要存储空间
- 相比B树，B+树有更小的树高，因为内部节点不存储数据

## 六、B+树的优势

### 6.1 数据库应用优势
1. **范围查询高效**：叶子节点链表支持快速范围扫描
2. **磁盘I/O优化**：节点大小匹配磁盘页大小
3. **缓存友好**：顺序访问叶子节点

### 6.2 与其他数据结构对比
| 特性 | B+树 | B树 | 红黑树 |
|------|------|-----|--------|
| 范围查询 | 优秀 | 一般 | 差 |
| 磁盘友好 | 优秀 | 优秀 | 差 |
| 内存使用 | 高效 | 高效 | 较高 |

## 七、实际应用场景

### 7.1 数据库索引
- MySQL的InnoDB引擎使用B+树作为主键索引
- Oracle、SQL Server等数据库系统

### 7.2 文件系统
- NTFS、ReiserFS等文件系统的目录结构
- 操作系统中的虚拟内存管理

### 7.3 缓存系统
- Redis的有序集合底层实现
- 各种缓存系统中的LRU实现

B+树通过其优秀的平衡性和范围查询能力，在现代计算机系统中扮演着重要角色，是数据库和文件系统中不可或缺的核心数据结构。