package algorithms;

import java.util.List;
import structure.BinaryHeap;
import structure.MSTree;
import structure.PathElement;
import structure.TNode;

public class Prim {

	/**
	 * Alorytm Prim'a.
	 *
	 * @param matrix true jeżeli ma być wykonywany dla postaci macierzowej.
	 * @return Wynik algorytmu.
	 */
	public static String compute(int graphOrder, int graphSize, List<PathElement> elements) {
		PathElement pathElement;
		TNode tNode;
		int i, v = 0;
		boolean[] visited = new boolean[graphOrder];
		BinaryHeap heap = new BinaryHeap(graphSize * graphSize);                // Kolejka priorytetowa oparta na kopcu
		MSTree mSTree = new MSTree(graphOrder), graph = new MSTree(
			graphOrder);                    // Minimalne drzewo rozpinające i Graf

		for (PathElement p : elements) {
			graph.addEdge(p);
		}

		// Tworzymy minimalne drzewo rozpinające

		visited[v] = true;              // Oznaczamy go jako odwiedzonego

		for (i = 1; i < graphOrder; i++) {         // Do drzewa dodamy n - 1 krawędzi grafu
			for (tNode = graph.getAList(v); tNode != null; tNode = tNode.getNext()) // Przeglądamy listę sąsiadów
			{
				if (!visited[tNode.getVertex()])          // Jeśli sąsiad jest nieodwiedzony,
				{
					heap.push(new PathElement(v, tNode.getVertex(), tNode.getWeight()));// Dodajemy ją do kolejki priorytetowej
				}
			}

			do {
				pathElement = heap.pop();
			} while (pathElement != null && visited[pathElement.getEndVertex()]);       // Krawędź prowadzi poza drzewo?
			if (pathElement != null) {
				mSTree.addEdge(pathElement);                 // Dodajemy krawędź do drzewa rozpinającego
				visited[pathElement.getEndVertex()] = true;         // Oznaczamy drugi wierzchołek jako odwiedzony
				v = pathElement.getEndVertex();
			}
		}
		// Wyświetlamy wyniki
		return mSTree.print() + "\n";
	}
}
