typedef struct Node {
    int data;
    struct Node* next;
}Node;
int sequentialSearch(Node* head, int target) {
    Node* cur = head;
    int index = 0;
    while (cur != NULL) {
        if (cur->data == target) {
            return index;
        }
        cur = cur->next;
        index++;
    }
    return -1;
}
/**
 * 链表朴素查找 - 线性查找
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 */
 int sequentialSearch(Node* head, int target) {
    Node* cur = head;
    int index = 0;
    while (cur != NULL) {
        if (cur->data == target) {
            return index;
        }
        cur = cur->next;
        index++;
    }
    return -1;
}
/**
 * 顺序表二分查找
 * 时间复杂度：O(logn)
 * 空间复杂度：O(1)
 */
 int binarySearch(int arr[], int n, int target) {
    int left = 0;
    int right = n - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return -1;
}
//线索二叉树
typedef struct ThreadNode{
    int data;
    struct ThreadNode*left;
    struct ThreadNode*right;
    int ltag;
    int rtag;
}ThreadNode;
void InThread(ThreadNode*root){
    if(root==NULL){
        return;
    }
    InThread(root->left);
    if(root->left==NULL){
        root->ltag=1;
        root->left=pre;
    }
    if(pre!=NULL&&pre->right==NULL){
        pre->rtag=1;
        pre->right=root;
    }
    pre=root;
    InThread(root->right);
}
void CreateInThread(ThreadNode*root){
    pre=NULL;
    InThread(root);
    root->ltag=1;
    root->left=pre;
    pre->rtag=1;
    pre->right=root;
}
void InOrder(ThreadNode*root){
    ThreadNode*p=root->left;
    while(p!=NULL){
        while(p->ltag==0){
            p=p->left;
        }
        printf("%d ",p->data);
        while(p->rtag==1){
            p=p->right;
            printf("%d ",p->data);
        }
        p=p->right;
    }
}
void preOrder(ThreadNode*root){
    ThreadNode*p=root;
    while(p!=NULL){
        while(p->ltag==0){
            printf("%d ",p->data);
            p=p->left;
        }
        printf("%d ",p->data);
        while(p->rtag==1){
            p=p->right;
        }
        p=p->right;
    }
    printf("\n");

}