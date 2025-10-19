/*
package datastructure1;

class BrowserHistory {
    private String homepage;
    private node head;
    public BrowserHistory(String homepage) {
        this.homepage=homepage;
        head=new node(homepage);
    }
    //创建一个双向链表


    public void visit(String url) {
         //查到head节点前面并且成为头节点
        node node=new node(url);
        node.next=head;
        head.prev=node;
        head=node;
        if(head.prev!=null){
            head.prev.next=null;
        }
        head.prev=null;
    }

    public String back(int steps) {
        while (steps>0){
            if(head.next!= null){
                head=head.next;
                steps--;
            }
            else{
                return head.url;
            }
        }
        return head.url;
    }

    public String forward(int steps) {
         while (steps>0){
            if(head.prev!= null){
                head=head.prev;
                steps--;
            }
            else{
                return head.url;
            }
        }
         return head.url;
    }
}
class Node{
    public String url;
    public node next;
    public node prev;
    public Node(String url){
        this.url=url;
    }
    public Node(){}
}
*/
/**
 * Your BrowserHistory object will be instantiated and called as such:
 * BrowserHistory obj = new BrowserHistory(homepage);
 * obj.visit(url);
 * String param_2 = obj.back(steps);
 * String param_3 = obj.forward(steps);
 */
