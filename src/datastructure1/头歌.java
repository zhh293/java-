package datastructure1;

import java.util.Scanner;

public class 头歌 {
    public static void main(String[] args) {
        int student=0;
        Scanner sc=new Scanner(System.in);
        student=sc.nextInt();
        int[] nums=new int[student];
        for(int i=0;i<student;i++){
            nums[i]=sc.nextInt();
        }
        int g=sc.nextInt();
        int count1=0;
        for(int i=0;i<student;i++){
            if(nums[i]==g){
                count1++;
            }
        }
        System.out.println(count1);
    }

    //设 a [0:n-1] 是有 n 个元素的数组，k (0≤k≤n-1) 是一个非负整数。设计一个算法将子数组 a [0:k-1] 和 a [k:n-1] 换位。要求算法在最坏情况下耗时 O (n)，且只用到 O (1) 的辅助空间。

}
