package datastructure1;

import java.util.*;

public class 队列 {
    public int[] deckRevealedIncreasing(int[] deck) {
          int[]arr=new int[deck.length];
          //妥妥的队列，从头部出来再从尾部进去
          Arrays.sort(deck);
          Deque<Integer>queue=new LinkedList<>();
          queue.addLast(deck[deck.length-1]);
          for(int i=deck.length-2;i>=0;i--){
              Integer poll = queue.pollLast();
              queue.addFirst(poll);
              queue.addFirst(deck[i]);
          }
          for(int i=0;i<deck.length;i++){
              arr[i]=queue.poll();
          }
          return arr;
    }

    public List<String> wordSubsets(String[] words1, String[] words2) {
        List<String>list=new ArrayList<>();
        Map<Character,Integer>map1=new HashMap<>();
        Map<Character,Integer>map2=new HashMap<>();
        for(int i=0;i<words2.length;i++){
            for(int j=0;j<words2[i].length();j++){
                map2.put(words2[i].charAt(j),map2.getOrDefault(words2[i].charAt(j),0)+1);
            }
        }
        for(int i=0;i<words1.length;i++){
            for(int j=0;j<words1[i].length();j++){
                map1.put(words1[i].charAt(j),map1.getOrDefault(words1[i].charAt(j),0)+1);
            }
            //看看map1中每个字符都出现而且map2中有对应的不少于map1中字符的个数
            if(isValid(map1,map2)){
                list.add(words1[i]);
            }
        }
        return list;
    }
    public boolean isValid(Map<Character,Integer>map1,Map<Character,Integer>map2){
        for(Character key:map2.keySet()){
            if(!map1.containsKey(key)||map1.get(key)<map2.get(key)){
                return false;
            }
        }
        return true;
    }

}
