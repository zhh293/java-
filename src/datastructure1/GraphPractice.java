package datastructure1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphPractice {
    class Node {
        public int val;
        public List<Node> neighbors;
        public Node() {
            val = 0;
            neighbors = new ArrayList<Node>();
        }
        public Node(int _val) {
            val = _val;
            neighbors = new ArrayList<Node>();
        }
        public Node(int _val, ArrayList<Node> _neighbors) {
            val = _val;
            neighbors = _neighbors;
        }
    }

    public class Solution {
        public Node cloneGraph(Node node) {
            if (node == null) {
                return null;
            }
            // 1. 哈希表改为存储「原图节点→克隆节点」的映射（核心修改）
            Map<Node, Node> map = new HashMap<>();
            Node head = new Node(node.val);
            head.neighbors = new ArrayList<>();
            // 2. 先把起点的映射关系存入（关键：确保循环引用时能找到已克隆的节点）
            map.put(node, head);
            createNewGraph(node, map, head);
            return head;
        }

        public void createNewGraph(Node node, Map<Node, Node> map, Node head) {
            if (node == null) {
                return;
            }
            for (int i = 0; i < node.neighbors.size(); i++) {
                Node pre = node.neighbors.get(i);
                if (map.containsKey(pre)) {
                    // 3. 已克隆过的节点：直接从映射中获取克隆节点并添加到邻居（修复引用缺失问题）
                    head.neighbors.add(map.get(pre));
                    continue;
                }
                // 4. 未克隆的节点：创建新节点并记录映射关系
                Node newNode = new Node(pre.val);
                newNode.neighbors = new ArrayList<>();
                map.put(pre, newNode); // 先存映射再递归，避免循环引用时重复创建
                head.neighbors.add(newNode);
                createNewGraph(pre, map, newNode);
                // 5. 移除错误的回溯操作（克隆是一次性的，无需取消标记）
            }
        }
    }

}
