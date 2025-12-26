//看似是cpp，其实全是c语言，c++语法我不会啊
#include <stdio.h>
#include <stdlib.h>
typedef struct node{
    node*next;
    int data;
}Node;
Node* init(int*nums){
    Node*head=(Node*)malloc(sizeof(Node));
    head->next=NULL;
    Node*p=head;
    for(int i=0;i<10;i++){
        Node*q=(Node*)malloc(sizeof(Node));
        q->data=nums[i];
        q->next=NULL;
        p->next=q;
        p=q;
    }
    return head;
}

void print(Node*head){
    while(head!=NULL){
        printf("%d ",head->data);
        head=head->next;
    }
    printf("\n");
    printf("打印完毕");
}
Node* insert(Node*head,int pos,int data){
    int i=0;
    Node*p=head;
    while(p!=NULL&&i<pos){
        p=p->next;
        i++;
        if(p==NULL){
            return NULL;
        }
    }
    Node*q=(Node*)malloc(sizeof(Node));
    q->data=data;
    q->next=p->next;
    p->next=q;
    return head;
}
Node* delete(Node*head,int pos){
    Node*p=head;
    int i=0;
    while(p!=NULL&&i<pos){
        p=p->next;
        i++;
        if(p==NULL){
            return NULL;
        }
    }
    Node*q=p->next;
    p->next=p->next->next;
    delete q;
    return head;
}
Node* reverse(Node*head){
    Node*one=head;
    Node*two=one->next;
    Node*three=two->next;
    while(three->next!=NULL){
        two->next=one;
        one=two;
        two=three;
        three=three->next;
    }
    three.next=two;
    two->next=one;
    head=three;
    return head;
}
Node* merge(Node*head1,Node*head2){
    Node*newNode=(Node*)malloc(sizeof(Node));
    newNode->next=NULL;
    Node*p=newNode;
    while(head1!=NULL&&head2!=NULL){
        if(head1->data<head2->data){
            newNode->next=head1;
            head1=head1->next;
            newNode=head1;
        }else{
            newNode->next=head2;
            head2=head2->next;
            newNode=head2;
        }
    }
    if(head1==NULL&&head2==NULL){
        return p;
    }else if(head1!=NULL&&head2==NULL){
        while(head1!=NULL){
            newNode->next=head1;
            head1=head1->next;
            newNode=head1;
        }
        return p;
    }else{
        while(head2!=NULL){
            newNode->next=head2;
            head2=head2->next;
            newNode=head2;
        }
        return p;
    }
}
//顺序表的插入删除
typedef struct SqList{
    int size;
    int currentLength;
    int*data;
}List;
List* init(int size){
    List*list=(List*)malloc(sizeof(List));
    list->size=size;
    list->data=(int*)malloc(size*sizeof(int));
    list->currentLength=0;
    for(int i=0;i<size;i++){
        list->data[i]=0;
    }
    return list;
}
void insert(List*list,int data,int index){
    if(index>list->currentLength){
        printf("不能跳着插入");
        return;
    }
    if(list->currentLength>=list->size){
        //进行扩容
        list->size=list->size*2;
        list->data=(int*)reallocate(list->data,list->size*sizeof(int));
        for(int i=list->currentLength;i>index;i--){
            list->data[i]=list->data[i-1];
        }
        list->data[index]=data;
//        list->data[list->currentLength]=data;
        list->currentLength++;
        return list;
    }
    for(int i=list->currentLength;i>index;i--){
        list->data[i]=list->data[i-1];
    }
    list->data[index]=data;
    list->currentLength++;
}
void delete(List*list,int index){
    if(index>=list->currentLength||index<0){
        printf("非法插入");
        return;
    }
    if(list->currentLength==0){
        printf("空表");
        return;
    }

    for(int i=index;i<list->currentLength-1;i++){
        list->data[i]=list->data[i+1];
    }
    list->currentLength--;
}













//队列
typedef struct Queue{
    int*data;
    int front;
    int rear;
    int size;
}Queue;
Queue* init(int size){
    Queue*queue=(Queue*)malloc(sizeof(Queue));
    queue->data=(int*)malloc(size*sizeof(int));
    queue->front=0;
    queue->rear=0;
    queue->size=size;
    return queue;
}
void enqueue(Queue*queue,int data){
    //入队
    if(queue->rear==queue->size){
        printf("队列已满");
        return;
    }
    queue->data[queue->rear]=data;
    queue->rear++;
    return;
}
int dequeue(Queue*queue){
    if(queue->front==queue->rear){
        printf("队列为空");
        return;
    }
    int data=queue->data[queue->front];
    queue->front++;
    return data;
}
int peek(){
    return queue->data[queue->front];
}
int isEmpty(Queue*queue){
    if(queue->front==queue->rear){
        return 1;
    }
    return 0;
}

//优化，循环队列
typedef struct LoopQueue{
    int*data;
    int front;
    int rear;
    int size;
}loopqueue;
loopqueue* init(int size){
    loopqueue*queue=(loopqueue*)malloc(sizeof(loopqueue));
    queue->data=(int*)malloc(size*sizeof(int));
    queue->front=0;
    queue->rear=0;
    queue->size=size;
    return queue;
}

void enqueue(loopqueue*queue,int data){
    if((queue->front+1)%queue->size==queue->rear){
        printf("队列已满");
        return;
    }
    queue->data[queue->rear]=data;
    queue->rear=(queue->rear+1)%queue->size;
    return;
}

int dequeue(loopqueue*queue){
    if(queue->front==queue->rear){
        printf("队列为空");
        return;
    }
    int data=queue->data[queue->front];
    queue->front=(queue->front+1)%queue->size;
    return data;
}



//栈


typedef struct Stack{
    int*data;
    int size;
    int top;
}Stack;

Stack* init(int size){
    Stack*stack=(Stack*)malloc(sizeof(Stack));
    stack->data=(int*)malloc(size*sizeof(int));
    stack->size=size;
    stack->top=-1;
    return stack;
}

void push(Stack*stack,int data){
    if(stack->top==stack->size-1){
        printf("栈已经满了");
        return;
    }
    stack->top++;
    stack->data[stack->top]=data;
    return;
}

int pop(Stack*stack){
    if(stack->top==-1){
        printf("栈已经空了");
        return;
    }
    return stack->data[stack->top--];
}
int peek(Stack*stack){
    return stack->data[stack->top];
}

int isEmpty(Stack*stack){
    if(stack->top==-1){
        return 1;
    }
    return 0;
}
//递归

int factorial(int n){
    if(n<=1){
        return 1;
    }
    return n*factorial(n-1);
}


void hanoi(int n,char*from,char*to,char*aux){
    if(n==1){
        printf("Move disk from %c to %c",*from,*to);
    }else{
        hanoi(n-1,from,to,aux);
        hanoi(n-1,aux,from,to);
    }
}













/*
你的核心思路是完全正确的！这正是用栈实现中缀表达式求值的经典、标准思路，只是在代码落地的细节上有一些需要补充 / 调整的点（并非思路本身的问题）。
先明确：你的核心思路全部正确的关键点
✅ 数字直接入数栈：这是中缀表达式求值的基础，数字无需优先级判断，直接暂存。
✅ 左括号直接入符号栈：左括号是 “计算边界”，必须先入栈，等待右括号触发后续计算。
✅ 右括号触发 “出栈计算直到左括号”：这是处理括号优先级的核心逻辑，完全符合栈的使用规则。
✅ 运算符按优先级处理：“当前符号优先级≤栈顶符号优先级时，先弹出计算，直到栈空 / 遇到更高优先级 / 左括号，再入当前符号”—— 这个核心逻辑是中缀求值的关键，你抓得非常准（你说的 “小于” 本质上是 “小于等于” 的简化表述，属于细节补充，并非思路错误）。


你的核心思路 100% 正确，完全契合中缀表达式求值的经典算法逻辑




*/





// 运算符优先级判断
int priority(char c) {
    switch(c) {
        case '+': return 1;
        case '-': return 1;
        case '*': return 2;
        case '/': return 2;
        case '(': return 0;  // 左括号优先级最低，避免被提前计算
        default: return -1; // 非运算符返回-1
    }
}

// 执行运算（修复减法/除法顺序问题）
int calc(int num1, int num2, char op) {
    switch(op) {
        case '+': return num2 + num1;
        case '-': return num2 - num1;  // 注意顺序：后出栈的是被减数/被除数
        case '*': return num2 * num1;
        case '/':
            if (num1 == 0) {
                printf("除数不能为0\n");
                return -1;
            }
            return num2 / num1;
        default: return 0;
    }
}

// 中缀表达式计算核心函数
int calculate(char* exp) {
    Stack *numStack = init(100);  // 数字栈
    Stack *opStack = init(100);   // 运算符栈

    for (int i = 0; exp[i] != '\0'; i++) {
        char c = exp[i];

        // 1. 处理数字（单个数字，若要支持多位数可扩展此处）
        if (c >= '0' && c <= '9') {
            push(numStack, c - '0');
            continue;
        }

        // 2. 处理左括号：直接入栈
        if (c == '(') {
            push(opStack, c);
            continue;
        }

        // 3. 处理右括号：计算直到左括号
        if (c == ')') {
            while (!isEmpty(opStack) && peek(opStack) != '(') {
                char op = (char)pop(opStack);
                int num1 = pop(numStack);
                int num2 = pop(numStack);
                push(numStack, calc(num1, num2, op));
            }
            pop(opStack);  // 弹出左括号（不参与计算）
            continue;
        }

        // 4. 处理运算符（+、-、*、/）
        if (c == '+' || c == '-' || c == '*' || c == '/') {
            int cur_priority = priority(c);
            // 核心逻辑：当前运算符优先级 ≤ 栈顶运算符优先级时，先计算栈顶
            while (!isEmpty(opStack) && peek(opStack) != '('
                   && cur_priority <= priority((char)peek(opStack))) {
                char op = (char)pop(opStack);
                int num1 = pop(numStack);
                int num2 = pop(numStack);
                push(numStack, calc(num1, num2, op));
            }
            // 把当前运算符入栈
            push(opStack, c);
        }
    }

    // 5. 处理栈中剩余的运算符
    while (!isEmpty(opStack)) {
        char op = (char)pop(opStack);
        int num1 = pop(numStack);
        int num2 = pop(numStack);
        push(numStack, calc(num1, num2, op));
    }

    // 最终结果在数字栈的栈顶
    int result = pop(numStack);

    // 释放栈内存
    freeStack(numStack);
    freeStack(opStack);

    return result;
}
int main(){
 return 0;
}

//15 7 1 1 + - / 3 * 2 1 1 + + -


//
//- 1. 实现顺序表的初始化、按位插入元素、按位删除元素操作
//- 2. 实现顺序表的按值查找，返回元素第一次出现的位置
//
//- 3. 实现单链表的初始化、按位插入节点、按位删除节点操作（要求包含头节点）
//
//- 4. 实现单链表的按值查找，返回对应的节点
//
//- 5. 用迭代法实现单链表的反转
//
//- 6. 用递归法实现单链表的反转
//
//- 7. 合并两个有序的顺序表，生成一个新的有序顺序表（假设升序）
//
//- 8. 合并两个有序的单链表，生成一个新的有序单链表（假设升序）
//
//- 9. 查找单链表的中间节点（要求时间复杂度O(n)，空间复杂度O(1)）
//
//- 10. 检测单链表是否存在环
//
//- 11. 若单链表存在环，找到环的入口节点
//
//- 12. 求两个无环单链表的第一个公共节点
//
//- 13. 删除单链表中倒数第k个节点（要求时间复杂度O(n)，空间复杂度O(1)）
//
//- 14. 实现双链表的初始化、按位插入节点、按位删除节点操作
//
//- 15. 实现循环单链表的初始化、表尾插入节点操作