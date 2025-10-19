package datastructure1;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

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
}
