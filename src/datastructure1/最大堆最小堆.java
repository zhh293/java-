package datastructure1;

import java.math.BigInteger;
import java.util.*;

public class 最大堆最小堆 {
    public static void main(String[] args) {
//        堆的复习要开始了，重新学习堆的知识，忘完了

  int []arr={4, 10, 3, 5, 1};
  buildHeap(arr);
  System.out.println(Arrays.toString( arr));










        /*Java 中的 PriorityQueue 类
        PriorityQueue是 Java 集合框架中的一个实现类，它基于最小堆实现优先队列。以下是它的核心用法：

        java-priority-queue
        Java PriorityQueue基本用法
        V1
        生成 PriorityQueueExample.java
        PriorityQueue 核心方法
        添加元素：
        offer(E e): 添加元素到队列，成功返回 true
        add(E e): 同上，但可能抛出异常
        移除元素：
        poll(): 移除并返回队列头部元素（优先级最高的元素）
        remove(Object o): 移除指定元素
        查看元素：
        peek(): 返回队列头部元素但不移除
        element(): 同上，但队列为空时抛出异常
        其他常用方法：
        size(): 返回队列中元素个数
        isEmpty(): 判断队列是否为空
        contains(Object o): 判断队列是否包含指定元素
                注意事项
        不保证排序顺序：
        PriorityQueue只保证peek()和poll()操作返回的是优先级最高的元素
        遍历队列时（如使用迭代器），元素顺序不保证是有序的
        自定义排序：
        对于自定义对象，需要实现Comparable接口或提供Comparator
        可以通过Comparator.reverseOrder()创建最大堆
        性能特点：
        插入和删除操作的时间复杂度为 O (log n)
        查找操作的时间复杂度为 O (n)
                适用于需要频繁获取最值的场景
        线程安全：
        PriorityQueue是非线程安全的
        如需线程安全，可使用PriorityBlockingQueue

        优先队列在许多算法问题中非常有用，如 Dijkstra 最短路径算法、哈夫曼编码、任务调度等场景。*/
    }
    public static void buildHeap(int[] arr){
        for(int i=(arr.length-1)/2;i>=0;i--){
            buildHeap(arr,i);
        }
    }
    private static void buildHeap(int[] arr,int i){
        if(i>arr.length){
            return;
        }
        //比较大小并且换位置
        if(arr[i]<arr[2*i+1]||arr[i]<arr[2*i+2]){
            int temp,index=0;
            if(arr[2*i+1]>arr[2*i+2]){
                temp=arr[2*i+1];
                index=2*i+1;
            }else{
                temp=arr[2*i+2];
                index=2*i+2;
            }
            arr[index]=arr[i];
            arr[i]=temp;
            buildHeap(arr,index);
        }
    }





    public int nthUglyNumber(int n) {
            PriorityQueue<Integer> pq=new PriorityQueue<>();
            if(n==1) return 1;
            pq.offer(1);
            n-=1;
            int target=0;
            int count=0;
            while(!pq.isEmpty()){
                Integer poll = pq.poll();
                count++;
                if(count==n){
                    n=0;
                    target=poll*2;
                    break;
                }
                pq.offer(poll*2);
                count++;
                if(count==n){
                    n=0;
                    target=poll*3;
                    break;
                }
                pq.offer(poll*3);
                count++;
                if(count==n){
                    n=0;
                    target=poll*5;
                    break;
                }
                pq.offer(poll*5);
            }
        return target;
    }


    public int maximumProduct(int[] nums, int k) {
         //始终让增长率最大的元素在第一个，写一个比较器
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                // 用long计算避免溢出
                long pre1 = (long) (o1 + 1) * o2;
                long pre2 = (long) (o2 + 1) * o1;

                if (pre1 > pre2) {
                    return -1; // o1优先级更高（应先被取出加1）
                } else if (pre1 < pre2) {
                    return 1;  // o2优先级更高
                } else {
                    return 0;  // 相等时返回0，符合比较器规范
                }
            }
        });
        for(int i=0;i<nums.length;i++){
            pq.offer(nums[i]);
        }
        while(k>0){
            int temp=pq.poll();
            pq.offer(temp+1);
            k--;
        }
        // 计算乘积，用long存储中间结果，每次乘法后取模
        long result = 1;
        int MOD = 1000000007;
        while (!pq.isEmpty()) {
            long num = pq.poll();
            // 关键：每次乘法后立即取模，确保结果在long安全范围内
            result = (result * num) % MOD;
        }

        return (int) result;
    }

    public int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();
        for(int i=0;i<tokens.length;i++){
            if(tokens[i].equals("+")){
                Integer a = stack.pop();
                Integer b = stack.pop();
                stack.push(a+b);
            }
            if(tokens[i].equals("-")){
                Integer a = stack.pop();
                Integer b = stack.pop();
                stack.push(b-a);
            }
            if(tokens[i].equals("*")){
                Integer a = stack.pop();
                Integer b = stack.pop();
                stack.push(a*b);
            }
            if(tokens[i].equals("/")){
                Integer a = stack.pop();
                Integer b = stack.pop();
                stack.push(b/a);
            }
            if(tokens[i].matches("-?\\d+")){
                stack.push(Integer.parseInt(tokens[i]));
            }
        }
        return stack.pop();
    }


















    /*public static void buildMaxHeap(int[] arr) {
        for(int i = arr.length / 2 - 1; i >= 0; i--){
            adjustHeap(arr, i, arr.length);
        }
    }

    private static void adjustHeap(int[] arr, int i, int length) {
        while(i<(length-1)/2){
            int left = 2*i+1;
            int right = 2*i+2;
            int max;
            if(right>=length){
                max=arr[left];
            }else {
                max=Math.max(arr[left], arr[right]);
            }
            if(arr[i]<max){
                int pre=max==arr[left]?left:right;
                int temp=arr[i];
                arr[i]=max;
                arr[pre]=temp;
                i=pre;
            }else{
                break;
            }
        }
    }*/
   /* public static void buildMaxHeap(int[] arr) {
        for(int i = arr.length / 2 - 1; i >= 0; i--){
            adjustHeap(arr, i, arr.length);
        }
    }

    private static void adjustHeap(int[] arr, int i, int length) {
        int temp = arr[i];
        // 从当前节点的左子节点开始
        for(int k = 2 * i + 1; k < length; k = 2 * k + 1) {
            // 如果右子节点存在且大于左子节点
            if(k + 1 < length && arr[k] < arr[k + 1]) {
                k++; // k指向右子节点
            }
            // 如果子节点大于父节点
            if(arr[k] > temp) {
                arr[i] = arr[k];
                i = k; // 继续向下调整
            } else {
                break;
            }
        }
        arr[i] = temp; // 放入最终位置
    }*/
}
