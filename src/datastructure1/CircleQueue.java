package datastructure1;

public class CircleQueue {
    private int [] data;
    private int front;
    private int rear;
    private int maxSize;
    public CircleQueue(int maxSize) {
        this.maxSize = maxSize;
        data = new int[maxSize];
        front = 0;
        rear = 0;
    }
    public boolean isFull() {
        return (rear + 1) % maxSize == front;
    }
    public boolean isEmpty() {
        return rear == front;
    }
    public void add(int n) {
        if (isFull()) {
            System.out.println("队列已满");
        } else {
            data[rear] = n;
            rear = (rear + 1) % maxSize;
        }
    }
    public int get() {
        if (isEmpty()) {
            System.out.println("队列已空");
            return -1;
        } else {
            int temp = data[front];
            front = (front + 1) % maxSize;
            return temp;
        }
    }
    public int peek(){
        if(isEmpty()){
            System.out.println("队列已空");
            return -1;
        }
        else {
            return data[front];
        }
    }
}
