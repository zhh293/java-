package datastructure1;

import datastructureclass.queue1;
//这是循环队列，可以很好的避免两个指针在数据进出的过程中逐渐逼近导致空间的浪费
public class forqueue {
    public static void main(String[] args) {
         queue1 q1 = new queue1();
    }
    public static void enqueue(queue1 q,int data) {
        if(q.front==q.rear&&q.queue[q.front+1]!=0){
            System.out.println("Queue is full");
        }
        else {
            q.queue[++q.rear%q.queue.length]=data;
        }
    }
    public static void dequeue(queue1 q) {
        if(q.front==q.rear&&q.queue[q.front]==0){
            System.out.println("Queue is empty");
        }
        else {
            q.queue[++q.front%q.queue.length]=0;
        }
    }
}
