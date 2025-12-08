package datastructure1;

import java.util.HashMap;
import java.util.Map;

public class LRUCache {
    class Node{
        int key;
        int value;
        Node next;
        Node pre;
        public Node(int key, int value){
            this.key = key;
            this.value = value;
        }
    }
    private Integer maxCapacity;
    private Integer size;
    private Map<Integer, Node>map;
    private Node head;
    private Node tail;
    //按最近使用时长对键值进行排序
    //使用什么数据结构呢，哈希表
    public LRUCache(int capacity) {
        maxCapacity = capacity;
        map = new HashMap<>();
        head = new Node(-1,-1);
        tail = new Node(-1,-1);
        head.next = tail;
        tail.pre = head;
        size = 0;
    }

    public int get(int key) {
        if(!map.containsKey(key)){
            return -1;
        }
        Node node = map.get(key);
        //将节点从当前位置移除
        node.pre.next = node.next;
        node.next.pre = node.pre;
        //将节点移动到头部
        addToHead(node);
        return node.value;
    }

    public void put(int key, int value) {
        if(map.containsKey(key)){
            // 如果key已存在，更新value并将节点移到头部
            Node node = map.get(key);
            node.value = value;
            // 将节点从当前位置移除
            node.pre.next = node.next;
            node.next.pre = node.pre;
            // 将节点移动到头部
            addToHead(node);
        }else{
            // 如果key不存在
            if(size >= maxCapacity){
                // 如果容量已满，删除尾部节点
                Node lastNode = tail.pre;
                removeNode(lastNode);
                map.remove(lastNode.key);
                size--;
            }
            // 创建新节点并添加到头部
            Node newNode = new Node(key, value);
            addToHead(newNode);
            map.put(key, newNode);
            size++;
        }
    }
    
    // 将节点添加到头部
    private void addToHead(Node node) {
        node.next = head.next;
        node.pre = head;
        head.next.pre = node;
        head.next = node;
    }
    
    // 从链表中删除节点
    private void removeNode(Node node) {
        node.pre.next = node.next;
        node.next.pre = node.pre;
    }
    //get和put都以O1的时间复杂度运行
}