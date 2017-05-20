package structure;

/**
 * Created by Tobiasz Rumian on 20.05.2017.
 */
public class LNode {
    private LNode next;
    private int data;

    public LNode(LNode next, int data) {
        this.next = next;
        this.data = data;
    }

    public LNode getNext() {
        return next;
    }

    public int getData() {
        return data;
    }

    public void setNext(LNode next) {
        this.next = next;
    }

    public void setData(int data) {
        this.data = data;
    }
}
