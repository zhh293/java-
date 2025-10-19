package datastructure1;

import java.util.Deque;
import java.util.LinkedList;

public class FrontMiddleBackQueue {
    private Deque<Integer>queue;//这个队列存前半部分数据
    private Deque<Integer>queue2;//这个队列存后半部分数据
    public FrontMiddleBackQueue() {
       this.queue=new LinkedList<>();
       this.queue2=new LinkedList<>();
    }

    public void pushFront(int val) {
        queue.addFirst(val);

    }

    public void pushMiddle(int val) {

    }

    public void pushBack(int val) {
         queue2.addLast(val);
    }

    public int popFront() {
        return queue.isEmpty()?-1:queue.removeFirst();
    }

    public int popMiddle() {
        return queue.isEmpty()?-1:queue.removeLast();
    }

    public int popBack() {
       return queue2.isEmpty()?-1:queue2.removeLast();
    }
}
