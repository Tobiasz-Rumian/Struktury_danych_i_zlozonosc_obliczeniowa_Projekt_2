package structure;

/**
 * Klasa reprezentująca drzewo rozpinające
 *
 * @author Tobiasz Rumian
 */
public class MSTree {

	private TNode[] table;
	private int weight;

	public MSTree(int size) {
		table = new TNode[size];
		weight = 0;                     // Zerujemy wagę drzewa
	}

	/**
	 * Dodaje krawędź do drzewa.
	 *
	 * @param p Krawędź.
	 */
	public void addEdge(PathElement p) {
		this.weight += p.getWeight();
		table[p.getStartVertex()] = new TNode(table[p.getStartVertex()], p.getEndVertex(), p.getWeight());
		table[p.getEndVertex()] = new TNode(table[p.getEndVertex()], p.getStartVertex(), p.getWeight());
	}

	/**
	 * Wypisuje wynik minimalnego drzewa.
	 *
	 * @return Minimalne drzewo.
	 */
	public String print() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < table.length; i++) {
			sb.append("Vertex ").append(i).append(" - ");
			for (TNode p = table[i]; p != null; p = p.getNext()) {
				sb.append(p.getVertex()).append(":").append(p.getWeight()).append(" ");
			}
			sb.append("\n");
		}
		sb.append("Minimalna waga drzewa rozpinającego: ").append(weight).append("\n");
		return sb.toString();
	}

	public TNode getAList(int n) {
		return table[n];
	}
}
