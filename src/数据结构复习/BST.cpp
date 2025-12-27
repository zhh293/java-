#include<stdio.h>
#include<stdlib.h>
typedef struct node{
    int data;
    struct node *left;
    struct node *right;
}Node;
Node *insert(Node *root,int data){
   if(root==NULL){
      root = new Node(data);
   }else if(data<=root->data){
      root.left=insert(root.left,data);
   }else{
      root.right=insert(root.right,data);
   }
   return root;
}
Node *search(Node *root,int data){
      if(root==NULL){
         return NULL;
      }
      if(data==root->data){
      return root;
      }else if(data<=root->data){
      return search(root->left,data);
      }else{
      return search(root->right,data);
      }
}
Node *delete(Node *root,int data){
    if(root==NULL){
     return NULL;
    }
    if(data<root->data){
    root->left=delete(root->left,data);
    }else if(data>root->data){
    root->right=delete(root->right,data);
    }else{
    if(root->left==NULL){
     return root->right;
    }else if(root->right==NULL){
     return root->left;
    }else{
        //两个孩子都有
        Node*temp=root;
        root=min(temp->right);
        root->right=deleteMin(temp->right);
        root->left=temp->left;
    }
    }
    return root;
}
//找右子树中的最小节点
Node *min(Node *root){
  if(root.left==NULL){
   return root;
  }
  return min(root.left);
}

//删除最小节点
Node *deleteMin(Node *root){
    //把删除结点的孩子易接到父节点，完美，，，，，然后把根节点挂在root->right(这一步已写)
    if(root->left==NULL){
      return root->right;
    }
    root->left=deleteMin(root->left);
    return root;
}

int main(){
//测试
//测试你们自己写吧，我就不写了
  return 0;
}