package datastructure1;

public class 线段树自己实现 {
    public static void main(String[] args) {
    }
    public static class SimpleSegmentTree{
        private int []tree;
        private int n;
        public SimpleSegmentTree(int[] arr){
            this.n=arr.length;
            tree=new int[4*n];
            build(arr,0,0,n-1);
        }
        private void build(int []arr,int node,int left,int right){
            if(left==right){
                tree[node]=arr[left];
                return;
            }
                int mid=(left+right)/2;
                build(arr,node*2+1,left,mid);
                build(arr,node*2+2,mid+1,right);
                tree[node]=tree[node*2+1]+tree[node*2+2];
        }
        public int query(int node,int queryLeft,int queryRight){
            return query(0,0,n-1,queryLeft,queryRight);
        }
        private int query(int node,int left,int right,int queryLeft,int queryRight){
            if(right<queryLeft||left>queryRight){
                return 0;
            }
            // 当前区间完全在查询区间内
            if (queryLeft <= left && right <= queryRight) {
                return tree[node];
            }
            int mid=(left+right)/2;
            int leftSum=query(node*2+1,left,mid,queryLeft,queryRight);
            int rightSum=query(node*2+2,mid+1,right,queryLeft,queryRight);
            return leftSum+rightSum;
        }
        public void update(int index,int newValue){
            if(index<0||index>=n){
                throw new RuntimeException("index out of range");
            }
            update(0,0,n-1,index,newValue);
        }
        private void update(int node,int left,int right,int index,int newValue){
            if(left==right){
                tree[node]=newValue;
                return;
            }
            int mid=(left+right)/2;
            if(index<=mid){
                update(node*2+1,left,mid,index,newValue);
            }else {
                update(node*2+2,mid+1,right,index,newValue);
            }
            tree[node]=tree[node*2+1]+tree[node*2+2];
        }
    }
}
