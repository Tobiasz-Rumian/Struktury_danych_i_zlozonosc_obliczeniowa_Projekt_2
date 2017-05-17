package structure;

/**
 * Created by Tobiasz Rumian on 17.05.2017.
 */
public class TNode {
    TNode next;
    int v,weight;

    public TNode(TNode next, int v, int weight) {
        this.next = next;
        this.v = v;
        this.weight = weight;
    }

    public TNode getNext() {
        return next;
    }

    public int getV() {
        return v;
    }

    public int getWeight() {
        return weight;
    }
}
