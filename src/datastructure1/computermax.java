package datastructure1;

import datastructureclass.numstack;
import datastructureclass.stack1;

public class computermax {
    public static void main(String[] args) {
        String str = "(700+89)*9-10+9*9";
        numstack num = new numstack();
        stack1 stack = new stack1();
        int num1;
        int num2;
        char ch;
        int result = 0;
        StringBuilder str1= new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if(ch>='0'&&ch<='9'){
                while(i + 1 < str.length() && str.charAt(i + 1) >= '0' && str.charAt(i + 1) <= '9'){
                    str1.append(str.charAt(i));
                    i++;
                }
                str1.append(str.charAt(i));
                num.push(Integer.parseInt(str1.toString()));
                str1 = new StringBuilder();
            } else if (ch=='(') {
                stack.push(ch);
                ch=str.charAt(++i);
                while(str.charAt(i)!=')'){
                    if(ch>='0'&&ch<='9'){
                           while(i + 1 < str.length() && str.charAt(i + 1) >= '0' && str.charAt(i + 1) <= '9'){
                               str1.append(str.charAt(i));
                               i++;
                           }
                           str1.append(str.charAt(i));
                           num.push(Integer.parseInt(str1.toString()));
                           str1 = new StringBuilder();
                           i++;
                       }


                    else if (ch=='+'||ch=='-') {
                        if(stack.top==-1){
                            stack.push(ch);
                        }
                        else{
                            char top = stack.pop();
                            stack.push(ch);
                            num1=num.pop();
                            num2=num.pop();
                            result+=result(num2,num1,top);
                            num.push(result);
                        }
                       i++;
                    }
                    else if (ch=='*'||ch=='/') {
                        stack.push(ch);
                        if(str.charAt(i+1)>='0'&&str.charAt(i+1)<='9'){
                            i++;
                            while(i + 1 < str.length() && str.charAt(i + 1) >= '0' && str.charAt(i + 1) <= '9'){
                                str1.append(str.charAt(i));
                                i++;
                            }
                            str1.append(str.charAt(i));
                            num.push(Integer.parseInt(str1.toString()));
                        }
                        num1 = num.pop();
                        num2 = num.pop();
                        result+=result(num2,num1,ch);
                        num.push(result);
                        stack.pop();
                        i++;
                    }

                }
                stack.push(str.charAt(i));
                stack.pop();
                stack.pop();
            } else if (ch=='+'||ch=='-') {
                if(stack.top==-1){
                    stack.push(ch);
                }
                else{
                    char top = stack.pop();
                    stack.push(ch);
                    num1=num.pop();
                    num2=num.pop();
                    result+=result(num2,num1,top);
                    num.push(result);
                }

            } else if (ch=='*'||ch=='/') {
                stack.push(ch);
                if(str.charAt(i+1)>='0'&&str.charAt(i+1)<='9'){
                    i++;
                    while(i + 1 < str.length() && str.charAt(i + 1) >= '0' && str.charAt(i + 1) <= '9'){
                        str1.append(str.charAt(i));
                        i++;
                    }
                    str1.append(str.charAt(i));
                    num.push(Integer.parseInt(str1.toString()));
                    str1 = new StringBuilder();
                }
                num1 = num.pop();
                num2 = num.pop();
                result+=result(num2,num1,ch);
                num.push(result);
                stack.pop();
            }
        }
        System.out.println(result);
    }

        public static int result(int a,int b,char c) {
            switch (c) {
                case '+':
                    return a + b;
                case '-':
                    return b - a;
                case '*':
                    return a * b;
                case '/':
                    return b / a;
                default:
                    return 0;
            }
        }
}
