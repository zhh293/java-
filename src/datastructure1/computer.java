package datastructure1;

import datastructureclass.numstack;
import datastructureclass.stack1;

public class computer {
    public static void main(String[] args) {
          String arr="5/5+3*2+4-7";
          numstack numstack =new numstack();
          stack1 operandstack = new stack1();
          for(int i=0; i<arr.length(); i++){
              char ch = arr.charAt(i);
              if(ch>='0' && ch<='9'){
                  numstack.push((int) ch-48);
              }
              else if(ch=='+'||ch=='-'){
                  if(operandstack.top==-1){
                      operandstack.push(ch);
                  }
                  else{
                      char ch1=operandstack.pop();
                      operandstack.push(ch);
                      int num1=numstack.pop();
                      int num2=numstack.pop();
                      int result=result(num1,num2,ch1);
                      numstack.push(result);
                  }
              }
              else if(ch=='*'||ch=='/'){
                  operandstack.push(ch);
                  numstack.push((int)arr.charAt(++i)-48);
                  int num1=numstack.pop();
                  int num2=numstack.pop();
                  int result=result(num1,num2,ch);
                  numstack.push(result);
                  operandstack.pop();
              }
          }
          System.out.println(result(numstack.pop(),numstack.pop(), operandstack.pop()));
    }
   public static int result(int a,int b,char c){
        switch(c){
                case '+':
                return a+b;
                case '-':
                return b-a;
                case '*':
                return a*b;
                case '/':
                 return  b/a;
                 default:
                     return 0;
        }
   }
}
