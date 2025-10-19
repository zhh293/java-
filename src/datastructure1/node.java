/*
package datastructure1;

import datastructureclass.doubleNode;
import datastructureclass.singleNode;

import java.util.Scanner;

public class node {
    public static void main(String[] arg){
        Scanner sc = new Scanner(System.in);
         singleNode n1 = new singleNode(sc.nextInt());
         n1.next=null;
         singleNode n2 = n1;
         while(n2.value!=9999){
             singleNode n= new singleNode(sc.nextInt());
             n2.next=n;
             n2=n;
             n.next=null;
         }
         while(n1.next!=null){
             System.out.println(n1.value);
             n1=n1.next;
         }
         //其他链表的创建都是一个样子，一个道理好吧
        //再来创建一个双链表
        doubleNode n3=new doubleNode(sc.nextInt());
         n3.left=null;
         n3.right=null;
         doubleNode n4=n3;
         while(n4.data!=9999){
             doubleNode n5=new doubleNode(sc.nextInt());
             n5.left=n4;
             n4.right=n5;
             n4=n5;
             n5.right=null;
         }
         while(n3.right!=null){
             System.out.println(n3.data);
             n3=n3.right;
         }

    }
}
*/
package datastructure1;
public class node {
    public static void main(String[] arg){
        int a=4/3*3;
        System.out.println(a);
    }
}
