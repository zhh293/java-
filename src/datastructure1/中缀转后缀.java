package datastructure1;

import javax.imageio.plugins.tiff.GeoTIFFTagSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;







//自己实现的哦，哈哈哈哈，我好厉害，耶耶耶。夸夸自己，muamuamua，别人不心疼我，我自己爱自己。。。。。。。。。。。。。。
public class 中缀转后缀 {
    // 定义运算符优先级

    // 将中缀表达式转换为后缀表达式
    private static int getPriority(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }
    public static String convert(String expression) {
        //不管那个‘&’了，太几把傻逼了，而且这不就是脱裤子放屁吗
        //创建运算符栈，来存储运算符，当遇到一个运算符的时候，先看看栈顶符号的优先级，如果低的话，就出栈。
        Stack<Character> operatorStack = new Stack<>();
        StringBuilder postfix = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isDigit(c)) {
                // 如果是数字，则直接添加到后缀表达式中
                postfix.append(c);
            }else if(c=='('){
                System.out.println(c);
                operatorStack.push(c);
            }else if (c == '+' || c == '-' || c == '*' || c == '/'){
                if(operatorStack.isEmpty()){
                    operatorStack.push(c);
                    continue;
                }
                if(getPriority(c)>getPriority(operatorStack.peek())){
                    operatorStack.push(c);
                    continue;
                }
                while (!operatorStack.isEmpty()&&getPriority(c)<=getPriority(operatorStack.peek())){
                    postfix.append(operatorStack.pop());
                }
                operatorStack.push(c);
            }else if(c==')'){
                while(!operatorStack.isEmpty()&&operatorStack.peek()!='('){
                    System.out.println(operatorStack.peek());
                    postfix.append(operatorStack.pop());
                }
                if (!operatorStack.isEmpty()) {
                    operatorStack.pop(); // 弹出 '('
                }
            }
        }
        while (!operatorStack.isEmpty()) {
            postfix.append(operatorStack.pop());
        }
        return postfix.toString();
    }

    public static int caculateMiddle(String expression) {
        Stack<Integer>numStack=new Stack<>();
        Stack<Character>operatorStack=new Stack<>();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if(Character.isDigit(c)){
                numStack.push(c-'0');
                continue;
            }
            if(c=='('){
                operatorStack.push(c);
                continue;
            }
            if(c=='+'||c=='-'||c=='*'||c=='/'){
                while(!operatorStack.isEmpty()&&getPriority(c)<=getPriority(operatorStack.peek())){
                    char pop = operatorStack.pop();
                    int num1 = numStack.pop();
                    int num2 = numStack.pop();
                    numStack.push(calculate(num1,num2,pop));
                }
            }
            if(c==')'){
                while(!operatorStack.isEmpty()&&operatorStack.peek()!='('){
                    char pop = operatorStack.pop();
                    int num1 = numStack.pop();
                    int num2 = numStack.pop();
                    numStack.push(calculate(num1,num2,pop));
                }
            }
        }
        return numStack.pop()-'0';
    }
    public static int calculate(int num1,int num2,char op){
        return switch (op) {
            case '+' -> num1 + num2;
            case '-' -> num1 - num2;
            case '*' -> num1 * num2;
            case '/' -> num1 / num2;
            default -> 0;
        };
    }

    public static void main(String[] args) {
        // 测试示例
        String testExpression = "2*(3+5)+7/1-4";
        String postfix = convert(testExpression);
        System.out.println("中缀表达式: " + testExpression);
        System.out.println("后缀表达式: " + postfix); // 应输出: 2&3&5&+7&1&/+4&-

        // 其他测试用例
        System.out.println(convert("3+4*2/(1-5)"));  // 3&4&2&*1&5&-/+
        System.out.println(convert("10+20*(30-5)/2"));  // 10&20&30&5&-*2&/+
    }
}
