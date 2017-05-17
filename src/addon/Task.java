package addon;

import enums.Algorithm;
import representation.AdjacencyLists;
import representation.AdjacencyMatrix;
import view.View;

/**
 * Created by Tobiasz Rumian on 17.05.2017.
 */
public class Task {
    private AdjacencyLists adjacencyLists;
    private AdjacencyMatrix adjacencyMatrix;
    private int startVertex;
    private int endVertex;
    private enums.Task typeOfTask;
    private int graphSize;
    private int graphOrder;
    private algorithm.Algorithm algorithm;

    public Task(enums.Task typeOfTask) {
        this.typeOfTask = typeOfTask;
    }

    public void createStructures(int graphOrder) {
        this.graphOrder = graphOrder;
        adjacencyLists = new AdjacencyLists(graphOrder);
        adjacencyMatrix = new AdjacencyMatrix(graphOrder);
    }

    public void addToStructures(int start, int end, int weight) {
        if (adjacencyMatrix == null) return;
        adjacencyMatrix.add(start, end, weight);
        adjacencyLists.add(start, end, weight);
        if (typeOfTask == enums.Task.MST) {
            adjacencyMatrix.add(end, start, weight);
            adjacencyLists.add(end, start, weight);
        }
    }

    public AdjacencyLists getAdjacencyLists() {
        return adjacencyLists;
    }

    public AdjacencyMatrix getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public int getStartVertex() {
        if (typeOfTask == enums.Task.MST) return -1;
        return startVertex;
    }

    public void setStartVertex(int startVertex) {
        if (typeOfTask == enums.Task.MST) return;
        this.startVertex = startVertex;
    }

    public int getEndVertex() {
        if (typeOfTask != enums.Task.MP) return -1;
        return endVertex;
    }

    public void setEndVertex(int endVertex) {
        if (typeOfTask != enums.Task.MP) return;
        this.endVertex = endVertex;
    }

    public enums.Task getTypeOfTask() {
        return typeOfTask;
    }

    public int getGraphSize() {
        return graphSize;
    }

    public void setGraphSize(int graphSize) {
        this.graphSize = graphSize;
    }

    public Algorithm[] getAvailableAlgorithms() {
        Algorithm[] algorithm = new Algorithm[2];
        switch (typeOfTask) {
            case MST:
                algorithm[0] = Algorithm.PRIM;
                algorithm[1] = Algorithm.KRUSKAL;
                break;
            case NSWG:
                algorithm[0] = Algorithm.DIJKSTR;
                algorithm[1] = Algorithm.FORD_BELLMAN;
                break;
            case MP:
                algorithm[0] = Algorithm.FORD_FULKERSON;
                algorithm[1] = Algorithm.FORD_FULKERSON;
                break;
        }
        return algorithm;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(View.title("macierz sąsiedztwa")).append("\n");
        for (int i = 0; i < adjacencyMatrix.getSize(); i++) {
            for (int j = 0; j < adjacencyMatrix.getSize(); j++)
                sb.append(String.format("% 3d",adjacencyMatrix.getMatrixElement(i, j))).append("\t");
            sb.append("\n");
        }
        sb.append(View.title("lista sąsiadów")).append("\n");
        for (int i = 0; i < adjacencyLists.getSize(); i++) {
            for (int j : adjacencyLists.getList(i))
                sb.append(String.format("% 3d",j)).append("\t");
            sb.append("\n");
        }
        return sb.toString();
    }

    public void clear() {
        adjacencyLists = null;
        adjacencyMatrix = null;
        startVertex = -1;
        endVertex = -1;
        graphSize = -1;
    }

    public void setAlgorithm(algorithm.Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public String executeAlgorithm() {
        StringBuilder sb = new StringBuilder();
        sb.append(View.title("Reprezentacja macierzowa"));
        sb.append(algorithm.preprocessMatrix(adjacencyMatrix)).append("\n");
        sb.append(View.title("Reprezentacja listowa"));
        sb.append(algorithm.preprocessLists(adjacencyLists)).append("\n");
        return sb.toString();
    }

    public int getGraphOrder() {
        return graphOrder;
    }
}
