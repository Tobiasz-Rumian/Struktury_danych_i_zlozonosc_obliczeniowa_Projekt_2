package algorithms;

import java.util.List;
import structure.BinaryHeap;
import structure.DSStruct;
import structure.MSTree;
import structure.PathElement;

public class Kruskal {

	/**
	 * Alorytm Kruskal'a.
	 *
	 * @param matrix true jeżeli ma być wykonywany dla postaci macierzowej.
	 * @return Wynik algorytmu.
	 */
	public static String compute(int graphOrder, int graphSize, List<PathElement> elements) {
		PathElement pathElement;
		int i;
		DSStruct dsStruct;
		BinaryHeap heap;
		MSTree msTree;

		dsStruct = new DSStruct(graphOrder);                  // Struktura zbiorów rozłącznych
		heap = new BinaryHeap(graphSize * graphSize);                     // Kolejka priorytetowa oparta na kopcu
		msTree = new MSTree(graphOrder);                    // Minimalne drzewo rozpinające
		for (i = 0; i < graphOrder; i++) {
			dsStruct.MakeSet(i);                 // Dla każdego wierzchołka tworzymy osobny zbiór
		}
		//Dodawanie do kopca
		for (PathElement p : elements) {
			heap.push(p);
		}

		//Wykonywanie algorytmu
		for (i = 1; i < graphOrder; i++) {          // Pętla wykonuje się n - 1 razy !!!
			do {
				pathElement = heap.pop();
			} while (pathElement != null && dsStruct.FindSet(pathElement.getStartVertex()) == dsStruct.FindSet(pathElement.getEndVertex()));
			if (pathElement != null) {
				msTree.addEdge(pathElement);                 // Dodajemy krawędź do drzewa
				dsStruct.UnionSets(pathElement);// Zbiory z wierzchołkami łączymy ze sobą
			}
		}
		return msTree.print() + "\n";
	}
}
