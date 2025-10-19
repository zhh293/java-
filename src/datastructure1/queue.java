package datastructure1;

import datastructureclass.queue1;

public class queue {
    public static void main(String[] args) {
        //顺序队列的实现
        queue1 q1 = new queue1();
    }
    public static void enqueue(queue1 q, int x) {
        if(q.size>=q.queue.length){
            System.out.println("Queue is full");
        }
        else {
            q.queue[++q.rear]=x;
            q.size++;
        }
    }
    public static int dequeue(queue1 q) {
        if(q.size<=0){
            System.out.println("Queue is empty");
        }
        else {
            q.queue[++q.front]=0;
            q.size--;
        }
        return 0;
    }
}
