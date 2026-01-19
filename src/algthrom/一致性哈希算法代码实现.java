package algthrom;

import java.util.*;

public class 一致性哈希算法代码实现 {
    //模仿nginx的一致性hash算法
    public static void main(String[] args) {
        String[] servers = {"192.168.0.0:8080", "192.168.0.1:8080", "192.168.0.2:8080", "192.168.0.3:8080", "192.168.0.4:8080"};
        ConsistentHashing consistentHashing = new ConsistentHashing(servers);
        System.out.println(consistentHashing.getServer("a"));
        System.out.println(consistentHashing.getServer("b"));
        System.out.println(consistentHashing.getServer("c"));
    }
}

class ConsistentHashing {
    private static final int VIRTUAL_NODES = 160; // 每个物理节点对应的虚拟节点数
    private final SortedMap<Integer, String> circle = new TreeMap<>(); // 哈希环，使用SortedMap维护有序性
    
    public ConsistentHashing(String[] servers) {
        for (String server : servers) {
            // 为每个物理节点添加多个虚拟节点
            for (int i = 0; i < VIRTUAL_NODES / 4; i++) {
                String virtualNode = server + "&VN" + i; // 虚拟节点名称
                int hash = getHash(virtualNode);
                circle.put(hash, server); // 将虚拟节点映射到物理节点
            }
        }
    }
    
    public String getServer(String key) {
        if (circle.isEmpty()) {
            return null;
        }
        
        int hash = getHash(key);
        
        // 在哈希环上找到大于等于该hash值的节点
        SortedMap<Integer, String> tailMap = circle.tailMap(hash);
        
        // 如果tailMap为空，说明key的hash值比环上最大hash值还大，需要顺时针转一圈
        // 返回环上第一个节点
        int nodeHash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        
        return circle.get(nodeHash);
    }
    
    // 使用FNV1_32_HASH算法计算哈希值
    private int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }
}