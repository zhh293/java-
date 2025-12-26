#include <stdio.h>
#include <stdlib.h>
typedef struct Node{
    int data;
    struct Node*left;
    struct Node*right;
}Node;
void preorder(Node*root){
    if(root==NULL){
        return;
    }
    printf("%d ",root->data);
    preorder(root->left);
    preorder(root->right);
}

void inorder(Node*root){
    if(root==NULL){
        return;
    }
    inorder(root->left);
    printf("%d ",root->data);
    inorder(root->right);
}

void postorder(Node*root){
    if(root==NULL){
        return;
    }
    postorder(root->left);
    postorder(root->right);
    printf("%d ",root->data);
}
//已知前序遍历和中序遍历序列，可以唯一确定一棵二叉树
//已知后序遍历和中序遍历序列，可以唯一确定一颗二叉树
void CreateBiTree(Node*head){
    int data;
    scanf("%d",&data);
    if(data==0){
        head=NULL;
        return;
    }else{
        head=(Node*)malloc(sizeof(Node));
        head->data=data;
        CreateBiTree(head->left);
        CreateBiTree(head->right);
    }
}

//线索二叉树
