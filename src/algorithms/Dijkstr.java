package algorithms;

import java.util.List;
import structure.PathElement;
import structure.TNode;

public class Dijkstr {

	/**
	 * Alorytm dijkstra'y.
	 *
	 * @param matrix true jeżeli ma być wykonywany dla postaci macierzowej.
	 * @return Wynik algorytmu.
	 */
	public static String compute(int graphOrder, List<PathElement> elements, int startVertex) {
		int[] costTable = new int[graphOrder],             // Tablica kosztów dojścia
			predecessorsTable = new int[graphOrder],              // Tablica poprzedników
			stack = new int[graphOrder],             // Stos
			heap = new int[graphOrder],             // Kopiec
			heapPointer = new int[graphOrder];             // Pozycje w kopcu
		int i, j, u, x, heapLength, parent, left, right, dMin, pMin, child, stackPointer = 0;
		StringBuilder sb = new StringBuilder();
		boolean[] QS = new boolean[graphOrder];             // Zbiory Q i S
		TNode[] graph = new TNode[graphOrder];                // Tablica list sąsiedztwa
		TNode pw;

		// Inicjujemy tablice dynamiczne

		for (i = 0; i < graphOrder; i++) {
			costTable[i] = Integer.MAX_VALUE;
			predecessorsTable[i] = -1;
			QS[i] = false;
			graph[i] = null;
			heap[i] = heapPointer[i] = i;
		}
		heapLength = graphOrder;
		// Odczytujemy dane wejściowe

		for (PathElement pathElement : elements) {
			graph[pathElement.getStartVertex()] = new TNode(graph[pathElement.getStartVertex()], pathElement.getEndVertex(),
				pathElement.getWeight());
		}

		costTable[startVertex] = 0;                       // Koszt dojścia v jest zerowy

		x = heap[0];
		heap[0] = heap[startVertex];
		heap[startVertex] = x; // odtwarzamy własność kopca
		heapPointer[startVertex] = 0;
		heapPointer[0] = startVertex;

		for (i = 0; i < graphOrder; i++) {
			u = heap[0];                     // Korzeń kopca jest zawsze najmniejszy

			// Usuwamy korzeń z kopca, odtwarzając własność kopca

			heap[0] = heap[--heapLength];             // W korzeniu umieszczamy ostatni element
			heapPointer[heap[0]] = parent = 0;        // Zapamiętujemy pozycję elementu w kopcu
			while (true)                   // W pętli idziemy w dół kopca, przywracając go
			{
				left = parent + parent + 1; // Pozycja lewego potomka
				right = left + 1;           // Pozycja prawego potomka
				if (left >= heapLength) {
					break;     // Kończymy, jeśli lewy potomek poza kopcem
				}
				dMin = costTable[heap[left]];          // Wyznaczamy mniejszego potomka
				pMin = left;
				if ((right < heapLength) && (dMin > costTable[heap[right]])) {
					dMin = costTable[heap[right]];
					pMin = right;
				}
				if (costTable[heap[parent]] <= dMin) {
					break; // Jeśli własność kopca zachowana, kończymy
				}
				x = heap[parent];
				heap[parent] = heap[pMin];
				heap[pMin] = x; // Przywracamy własność kopca
				heapPointer[heap[parent]] = parent;
				heapPointer[heap[pMin]] = pMin;      // na danym poziomie
				parent = pMin;              // i przechodzimy na poziom niższy kopca
			}

			// Znaleziony wierzchołek przenosimy do S

			QS[u] = true;

			// Modyfikujemy odpowiednio wszystkich sąsiadów u, którzy są w Q

			for (pw = graph[u]; pw != null; pw = pw.getNext()) {
				if (!QS[pw.getVertex()] && (costTable[pw.getVertex()] > costTable[u] + pw.getWeight())) {
					costTable[pw.getVertex()] = costTable[u] + pw.getWeight();
					predecessorsTable[pw.getVertex()] = u;

					// Po zmianie d[v] odtwarzamy własność kopca, idąc w górę

					for (child = heapPointer[pw.getVertex()]; child != 0; child = parent) {
						parent = child / 2;
						if (costTable[heap[parent]] <= costTable[heap[child]]) {
							break;
						}
						x = heap[parent];
						heap[parent] = heap[child];
						heap[child] = x;
						heapPointer[heap[parent]] = parent;
						heapPointer[heap[child]] = child;
					}
				}
			}
		}
		//Wyświetlanie wyniku.
		for (i = 0; i < graphOrder; i++) {
			sb.append(i).append(": ");
			for (j = i; j > -1; j = predecessorsTable[j]) {
				stack[stackPointer++] = j;
			}
			while (stackPointer != 0) {
				sb.append(stack[--stackPointer]).append(" ");
			}
			sb.append("$").append(costTable[i]).append("\n");
		}
		sb.append("\n");
		return sb.toString();
	}
}
