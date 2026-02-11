package LRU缓存重做;

import sun.misc.Unsafe;

import java.util.HashMap;
import java.util.Map;

public class LRU {
    //这里的头尾节点是不存数据的
    class Node {
        int key;
        int value;
        Node pre;
        Node next;
        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
    class LRUCache {
        private int maxSize;
        private volatile int curSize;
        private Node head;
        private Node tail;
        private Map<Integer, Node> map;
        private static final Unsafe UNSAFE = Unsafe.getUnsafe();
        public LRUCache(int capacity) {
            maxSize = capacity;
            curSize = 0;
            head = new Node(-1, -1);
            tail = new Node(-1, -1);
            head.next = tail;
            tail.pre = head;
            map = new HashMap<>();
        }
        public int get(int key) {
            if (!map.containsKey(key)) {
                return -1;
            }
            Node node = map.get(key);
            node.pre.next = node.next;
            node.next.pre = node.pre;
            addToHead(node);
            return node.value;
        }
        private void addToHead(Node node) {
            node.next = head.next;
            node.pre = head;
            head.next.pre = node;
            head.next = node;
        }
        public void put(int key, int value) {
            if(map.containsKey(key)){
                Node node = map.get(key);
                node.value = value;
                node.pre.next = node.next;
                node.next.pre = node.pre;
                addToHead(node);
            }else{
                int capacity=curSize+1;
//                do{
//
//                }while (UNSAFE.compareAndSwapInt())
                Node node = new Node(key, value);
                if(capacity>maxSize){
                    //移除最后一个节点，同时把新节点添加到头部
                    Node lastNode = tail.pre;
                    removeNode(lastNode);
                    addToHead(node);
                    curSize++;
                    map.put(key, node);
                    return;
                }
                addToHead(node);
                map.put(key, node);
                curSize++;
            }
        }
        private void removeNode(Node node) {
            node.pre.next = node.next;
            node.next.pre = node.pre;
        }
    }
}
