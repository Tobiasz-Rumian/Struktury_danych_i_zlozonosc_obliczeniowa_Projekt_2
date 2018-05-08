package algorithms;

import java.util.List;
import structure.PathElement;
import structure.TNode;

public class BellmanFord {

	/**
	 * Alorytm Bellman'a-Ford'a.
	 */
	public static String compute(int graphOrder, List<PathElement> elements, int startVertex) {
		TNode[] tNodes = new TNode[graphOrder];// Tablica dynamiczna list sąsiedztwa
		double[] accessCosts = new double[graphOrder];
		int[] predecessors = new int[graphOrder], S;
		int i, j, stackPointer;
		boolean negativeCycle = false, test;
		TNode tNode;
		StringBuilder sb = new StringBuilder();
		for (i = 0; i < graphOrder; i++) {          // Inicjujemy struktury danych
			accessCosts[i] = Integer.MAX_VALUE;
			predecessors[i] = -1;
			tNodes[i] = null;
		}

		for (PathElement pathElement : elements) {
			tNodes[pathElement.getStartVertex()] = new TNode(tNodes[pathElement.getStartVertex()], pathElement.getEndVertex(),
				pathElement.getWeight());
		}

		// Wyznaczamy najkrótsze ścieżki algorytmem Bellmana-Forda

		accessCosts[startVertex] = 0;                       // Zerujemy koszt dojścia do v
		for (i = 1; i < graphOrder; i++) {        // Pętla relaksacji
			test = true;                  // Oznacza, że algorytm nie wprowadził zmian do d i p
			for (j = 0; j < graphOrder; j++)        // Przechodzimy przez kolejne wierzchołki grafu
			{
				for (tNode = tNodes[j]; tNode != null; tNode = tNode.getNext()) // Przeglądamy listę sąsiadów wierzchołka x
				{
					if (accessCosts[tNode.getVertex()] > accessCosts[j] + tNode.getWeight()) { // Sprawdzamy warunek relaksacji
						test = false;           // Jest zmiana w d i p
						accessCosts[tNode.getVertex()] =
							accessCosts[j] + tNode.getWeight(); // Relaksujemy krawędź z x do jego sąsiada
						predecessors[tNode.getVertex()] = j;           // Poprzednikiem sąsiada będzie x
					}
				}
			}
			if (test) {
				negativeCycle = true;
				// Sprawdzamy istnienie ujemnego cyklu
				for (j = 0; j < graphOrder; j++) {
					for (tNode = tNodes[j]; tNode != null; tNode = tNode.getNext()) {
						if (!(accessCosts[tNode.getVertex()] > accessCosts[j] + tNode.getWeight())) {
							negativeCycle = true;
						}
					}
				}
				break;// Jeśli nie było zmian, to kończymy
			}
		}

		if (negativeCycle) {
			S = new int[graphOrder];              // Tworzymy prosty stos
			stackPointer = 0;

			for (i = 0; i < graphOrder; i++) {
				sb.append(i).append(": ");
				for (j = i; j != -1; j = predecessors[j]) // Wierzchołki ścieżki umieszczamy na stosie
				{
					S[stackPointer++] = j;            // w kolejności od ostatniego do pierwszego
				}

				while (stackPointer != 0) {
					sb.append(S[--stackPointer]).append(" ");// Wierzchołki ze stosu drukujemy
				}
				sb.append("$").append(accessCosts[i]).append("\n");

			}
		} else {
			sb.append("Znaleziono negatywny cykl!").append("\n");
		}
		return sb.toString();
	}
}
