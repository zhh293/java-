package datastructureclass;

public class numstack {
    public int top;
    public int []stack;
    public numstack() {
        top = -1;
        stack = new int[10];
    }
    public void push(int x) {
        if (top == stack.length - 1) {
            System.out.println("Stack is full");
            return;
        }
        stack[++top] = x;
        System.out.println("Pushed " + x + " to stack");

    }
    public int pop() {
        if (top == -1) {
            System.out.println("Stack is empty");
            return -1;
        }
        System.out.println("Popped " + stack[top] + " from stack");
        return stack[top--];

    }
}
