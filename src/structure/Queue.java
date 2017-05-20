package structure;

/**
 * Created by Tobiasz Rumian on 20.05.2017.
 */
public class Queue {
    private LNode  head=null;
    private LNode tail=null;



    public boolean empty(){
        return head == null;
    }
    public int  front(){
        if(head!=null) return head.getData();
        else     return Integer.MIN_VALUE;
    }
    public void push(int v){
        LNode  p = new LNode(null,v);
        if(tail!=null) tail.setNext(p);
        else     head = p;
        tail = p;
    }
    public void pop(){
        if(head!=null) {
            LNode p = head;
            head = head.getNext();
            if(head==null) tail = null;
        }
    }
}
