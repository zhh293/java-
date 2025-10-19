package datastructureclass;

import java.util.Map;

public class LRUCache {
    /*public List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        backtrack(result, new StringBuilder(), 0, 0, n);
        return result;
    }

    private void backtrack(List<String> result, StringBuilder current, int open, int close, int max) {
        if (current.length() == max * 2) {
            result.add(current.toString());
            return;
        }

        if (open < max) {
            current.append('(');
            backtrack(result, current, open + 1, close, max);
            current.deleteCharAt(current.length() - 1);
        }

        if (close < open) {
            current.append(')');
            backtrack(result, current, open, close + 1, max);
            current.deleteCharAt(current.length() - 1);
        }
    }*/
    private final int capacity;
    private Map<Integer,Integer> map;
    public LRUCache(int capacity) {
        this.capacity=capacity;
    }

    public int get(int key) {
        if(map.containsKey( key)){
            return map.get(key);
        }

        return -1;
    }

    public void put(int key, int value) {
        if(map.size()>=capacity){
            //找到最久没有使用的





        }else{
            map.put((Integer)key,(Integer)value);
        }
    }

   /* public static void main(String[] args) {
        singleNode solution = new singleNode();
        List<String> list7 = solution.generateParenthesis(7);
        List<String> list= []
        System.out.println(list7);
    }*/
}