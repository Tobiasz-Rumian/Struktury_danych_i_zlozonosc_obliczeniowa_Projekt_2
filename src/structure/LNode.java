package structure;

/**
 * Klasa reprezentująca element listy.
 *
 * @author Tobiasz Rumian
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

	public void setNext(LNode next) {
		this.next = next;
	}

	public int getData() {
		return data;
	}
}
