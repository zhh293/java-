package datastructureclass;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class 哈希表简易实现<K, V> {
    private int capacity;
    private Bucket[]buckets;

    public 哈希表简易实现(int capacity){
        this.capacity = capacity;
        buckets = new Bucket[capacity];
    }
    public void put(K key, V value){
        int index = key.hashCode() % capacity;
        if(buckets[index] == null){
            buckets[index] = new Bucket<>();
        }
        buckets[index].add(key, value);
        System.out.println("添加键值对(" + key + ", " + value + ")到索引为" + index + "的桶中");
    }
    public V get(K key){
        int index = key.hashCode() % capacity;
        if(buckets[index] == null){
            return null;
        }
        return (V) buckets[index].get(key);
    }
    static class Bucket<K, V>{
        private List<Entry<K, V>> entries;
        public Bucket(){
            entries = new LinkedList<>();
        }
        public void add(K key, V value){
            for(Entry<K, V> entry : entries){
                if(entry.key.equals(key)){
                    entry.value = value;
                    return;
                }
            }
            entries.add(new Entry<>(key, value));
        }
        public V get(K key){
            for(Entry<K, V> entry : entries){
                if(entry.key.equals(key)){
                    return entry.value;
                }
            }
            return null;
        }

    }
    static class Entry<K, V>{
        K key;
        V value;
        public Entry(K key, V value){
            this.key = key;
            this.value = value;
        }
    }
    public static void main(String[] args) {
        哈希表简易实现<Integer, String> hashTable = new 哈希表简易实现<>(10);
        hashTable.put(1, "One");
        hashTable.put(2, "Two");
        hashTable.put(3, "Three");
        hashTable.put(4, "Four");
        hashTable.put(5, "Five");
        System.out.println("获取键1对应的值：" + hashTable.get(1));
    }
}
