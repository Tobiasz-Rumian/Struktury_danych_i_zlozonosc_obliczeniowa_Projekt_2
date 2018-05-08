package structure;

/**
 * Klasa reprezentująca element drzewa.
 *
 * @author Tobiasz Rumian
 */
public class TNode {

	private TNode next;
	private int vertex, weight;

	public TNode(TNode next, int vertex, int weight) {
		this.next = next;
		this.vertex = vertex;
		this.weight = weight;
	}

	public TNode getNext() {
		return next;
	}

	public int getVertex() {
		return vertex;
	}

	public int getWeight() {
		return weight;
	}
}
