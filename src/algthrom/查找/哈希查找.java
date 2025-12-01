package algthrom.查找;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class 哈希查找 {
    //说白了不就是哈希表吗
    Map<String, String>map=new TreeMap<>();

    public static void main(String[] args) {
        System.out.println("哈希查找");
        //树状哈希表
        TreeMap<String, String> treeMap = new TreeMap<>();


        treeMap.put("3", "3");treeMap.put("2", "2");
        treeMap.put("4", "4");treeMap.put("1", "1");
        treeMap.put("5", "5");
        Set<Map.Entry<String, String>> entries = treeMap.entrySet();
        for(Map.Entry<String, String> entry : entries){
            System.out.println(entry.getKey()+" "+entry.getValue());
        }
        //树状map的特点
        //1.key是唯一的
        //2.key是排序的
        //带来的好处，key是有序的，所以可以进行二分查找

    }
}
