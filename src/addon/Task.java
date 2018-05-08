package addon;

import algorithms.BellmanFord;
import algorithms.Dijkstr;
import algorithms.Kruskal;
import algorithms.Prim;
import enums.Algorithm;
import java.util.ArrayList;
import java.util.List;
import representation.AdjacencyLists;
import representation.AdjacencyMatrix;
import structure.PathElement;
import view.View;

/**
 * Klasa reprezentująca zadanie do wykonania.
 *
 * @author Tobiasz Rumian
 */
public class Task {

	private AdjacencyLists adjacencyLists;
	private AdjacencyMatrix adjacencyMatrix;
	private int startVertex;
	private enums.Task typeOfTask;
	private int graphSize;
	private int graphOrder;

	public Task(enums.Task typeOfTask) {
		this.typeOfTask = typeOfTask;
	}

	/**
	 * Tworzy reprezentacje listową i macierzową.
	 *
	 * @param graphOrder Ilość wierzchołków.
	 */
	public void createStructures(int graphOrder) {
		this.graphOrder = graphOrder;
		adjacencyLists = new AdjacencyLists(graphOrder);
		adjacencyMatrix = new AdjacencyMatrix(graphOrder);
	}

	/**
	 * Pozwala na dodanie krawędzi do struktur.
	 *
	 * @param start początek krawędzi.
	 * @param end koniec krawędzi.
	 * @param weight waga krawędzi.
	 */
	public void addToStructures(int start, int end, int weight) {
		if (adjacencyMatrix == null) {
			return;
		}
		adjacencyMatrix.add(start, end, weight);
		adjacencyLists.add(start, end, weight);
		if (typeOfTask == enums.Task.MST) {
			adjacencyMatrix.add(end, start, weight);
			adjacencyLists.add(end, start, weight);
		}
	}

	/**
	 * Pozwala ustawić początkowy węzeł.
	 *
	 * @param startVertex początkowy węzeł.
	 */
	public void setStartVertex(int startVertex) {
		if (typeOfTask == enums.Task.MST) {
			return;
		}
		this.startVertex = startVertex;
	}

	public enums.Task getTypeOfTask() {
		return typeOfTask;
	}

	public void setGraphSize(int graphSize) {
		this.graphSize = graphSize;
	}

	/**
	 * Pokazuje dostępne dla danego zadania algorytmy.
	 *
	 * @return Tablica dostępnych algorytmów.
	 */
	public Algorithm[] getAvailableAlgorithms() {
		Algorithm[] algorithm = new Algorithm[2];
		switch (typeOfTask) {
		case MST:
			algorithm[0] = Algorithm.PRIM;
			algorithm[1] = Algorithm.KRUSKAL;
			break;
		case NSWG:
			algorithm[0] = Algorithm.DIJKSTR;
			algorithm[1] = Algorithm.BELLMAN_FORD;
			break;
		}
		return algorithm;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(View.title("macierz sąsiedztwa")).append("\n");
		for (int i = 0; i < adjacencyMatrix.getSize(); i++) {
			for (int j = 0; j < adjacencyMatrix.getSize(); j++) {
				if (adjacencyMatrix.getMatrixElement(i, j) == Integer.MIN_VALUE) {
					sb.append("  -").append("\t");
				} else {
					sb.append(String.format("% 3d", adjacencyMatrix.getMatrixElement(i, j))).append("\t");
				}
			}

			sb.append("\n");
		}
		sb.append(View.title("lista sąsiadów")).append("\n");
		for (int i = 0; i < adjacencyLists.getSize(); i++) {
			for (int j : adjacencyLists.getList(i)) {
				sb.append(String.format("% 3d", j)).append("\t");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public void clear() {
		adjacencyLists = null;
		adjacencyMatrix = null;
		startVertex = -1;
		graphSize = -1;
	}

	/**
	 * Wykonuje zadany algorytm.
	 *
	 * @param algorithm typ algorytmu.
	 * @param matrix true jeżeli ma być wykonany na reprezentacji macierzowej.
	 */
	public void testAlgorithm(Algorithm algorithm, boolean matrix) {
		switch (algorithm) {
		case KRUSKAL:
			Kruskal.compute(graphOrder, graphSize, getElements(matrix));
			break;
		case PRIM:
			Prim.compute(graphOrder, graphSize, getElements(matrix));
			break;
		case DIJKSTR:
			Dijkstr.compute(graphOrder, getElements(matrix), startVertex);
			break;
		case BELLMAN_FORD:
			BellmanFord.compute(graphOrder, getElements(matrix), startVertex);
			break;
		}
	}

	/**
	 * Zwraca wynik algorytmudla postaci macierzowej oraz listowej.
	 *
	 * @param algorithm typ algorytmu.
	 * @return Wynik algorytmu.
	 */
	public String getAlgorithm(Algorithm algorithm) {
		StringBuilder sb = new StringBuilder();
		switch (algorithm) {
		case KRUSKAL:
			computeKruskal(sb);
			break;
		case PRIM:
			computePrim(sb);
			break;
		case DIJKSTR:
			computeDijkstr(sb);
			break;
		case BELLMAN_FORD:
			computeBellmanFord(sb);
			break;
		}
		return sb.toString();
	}

	private void computeBellmanFord(StringBuilder sb) {
		sb.append(View.title("Reprezentacja macierzowa"));
		sb.append(BellmanFord.compute(graphOrder, getElements(true), startVertex));
		sb.append("\n");
		sb.append(View.title("Reprezentacja listowa"));
		sb.append(BellmanFord.compute(graphOrder, getElements(false), startVertex));
		sb.append("\n");
	}

	private void computeDijkstr(StringBuilder sb) {
		sb.append(View.title("Reprezentacja macierzowa"));
		sb.append(Dijkstr.compute(graphOrder, getElements(true), startVertex));
		sb.append("\n");
		sb.append(View.title("Reprezentacja listowa"));
		sb.append(Dijkstr.compute(graphOrder, getElements(false), startVertex));
		sb.append("\n");
	}

	private void computePrim(StringBuilder sb) {
		sb.append(View.title("Reprezentacja macierzowa"));
		sb.append(Prim.compute(graphOrder, graphSize, getElements(true)));
		sb.append("\n");
		sb.append(View.title("Reprezentacja listowa"));
		sb.append(Prim.compute(graphOrder, graphSize, getElements(false)));
		sb.append("\n");
	}

	private void computeKruskal(StringBuilder sb) {
		sb.append(View.title("Reprezentacja macierzowa"));
		sb.append(Kruskal.compute(graphOrder, graphSize, getElements(true)));
		sb.append("\n");
		sb.append(View.title("Reprezentacja listowa"));
		sb.append(Kruskal.compute(graphOrder, graphSize, getElements(false)));
		sb.append("\n");
	}

	/**
	 * Zwraca listę krawędzi grafu.
	 *
	 * @param matrix true jeżeli pobieranie krawędzi ma być z reprezentacji macierzowej.
	 * @return Lista krawędzi grafu.
	 */
	private List<PathElement> getElements(boolean matrix) {
		return matrix ? getElementsFromMatrix() :getElementsFromList();
	}

	private List<PathElement> getElementsFromList() {
		List<PathElement> p = new ArrayList<>();
		for (int i = 0; i < graphOrder; i++) {
			for (int j = 0; j < adjacencyLists.getList(i).size(); j += 2) {
				p.add(new PathElement(i, adjacencyLists.getList(i).get(j), adjacencyLists.getList(i).get(j + 1)));
			}
		}
		return p;
	}

	private List<PathElement> getElementsFromMatrix() {
		List<PathElement> p = new ArrayList<>();
		if (typeOfTask == enums.Task.MST) {
			for (int i = 0; i < graphOrder; i++) {
				for (int j = i + 1; j < graphOrder; j++) {
					if (adjacencyMatrix.getMatrixElement(i, j) != Integer.MIN_VALUE) {
						p.add(new PathElement(i, j, adjacencyMatrix.getMatrixElement(i, j)));
					}
				}
			}
		} else {
			for (int i = 0; i < graphOrder; i++) {
				for (int j = 0; j < graphOrder; j++) {
					if (adjacencyMatrix.getMatrixElement(i, j) != Integer.MIN_VALUE) {
						p.add(new PathElement(i, j, adjacencyMatrix.getMatrixElement(i, j)));
					}
				}
			}
		}
		return p;
	}

	public void generateRandomGraph(int graphOrder, int density) {
		GraphsContainer graphsContainer = RandomGraphGenerator.generateRandomGraph(graphOrder, density, graphSize, typeOfTask);
		this.adjacencyLists=graphsContainer.getAdjacencyLists();
		this.adjacencyMatrix=graphsContainer.getAdjacencyMatrix();
		this.typeOfTask=graphsContainer.getTypeOfTask();
		this.graphOrder=graphsContainer.getGraphOrder();
		this.graphSize=graphsContainer.getGraphSize();
	}
}
