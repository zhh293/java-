package datastructureclass;

import java.util.PriorityQueue;

public class stack1 {
    public int top;
    public char []stack;
    public stack1() {
        top = -1;
        stack = new char[10];
    }
    public void push(char x) {
        if (top == stack.length - 1) {
            System.out.println("Stack is full");
            return;
        }
        stack[++top] = x;
        System.out.println("Pushed " + x + " to stack");

    }
    public char pop() {
        if (top == -1) {
            System.out.println("Stack is empty");
            return ' ';
        }
        System.out.println("Popped " + stack[top] + " from stack");
        return stack[top--];

    }
}
