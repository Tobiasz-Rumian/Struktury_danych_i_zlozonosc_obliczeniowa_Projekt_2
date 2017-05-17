package addon;

import enums.Algorithm;
import representation.AdjacencyLists;
import representation.AdjacencyMatrix;
import structure.*;
import view.View;

import java.util.ArrayList;
import java.util.List;

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
                sb.append(String.format("% 3d", adjacencyMatrix.getMatrixElement(i, j))).append("\t");
            sb.append("\n");
        }
        sb.append(View.title("lista sąsiadów")).append("\n");
        for (int i = 0; i < adjacencyLists.getSize(); i++) {
            for (int j : adjacencyLists.getList(i))
                sb.append(String.format("% 3d", j)).append("\t");
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


    public int getGraphOrder() {
        return graphOrder;
    }

    public String kruskal() {
        PathElement e;
        int i;
        DSStruct Z;
        BinaryHeap Q;
        MSTree T;
        StringBuilder sb = new StringBuilder();


        sb.append(View.title("Reprezentacja macierzowa"));
        Z = new DSStruct(graphOrder);                  // Struktura zbiorów rozłącznych
        Q = new BinaryHeap(graphSize);                     // Kolejka priorytetowa oparta na kopcu
        T = new MSTree(graphOrder);                    // Minimalne drzewo rozpinające
        for (i = 0; i < graphOrder; i++)
            Z.MakeSet(i);                 // Dla każdego wierzchołka tworzymy osobny zbiór
        //Dodawanie do kopca
        for (PathElement p : getElements(true)) Q.push(p);

        //Wykonywanie algorytmu
        for (i = 1; i < graphOrder; i++) {          // Pętla wykonuje się n - 1 razy !!!
            do e = Q.pop();
            while (e != null && Z.FindSet(e.getStartVertex()) == Z.FindSet(e.getEndVertex()));
            if (e != null) {
                T.addEdge(e);                 // Dodajemy krawędź do drzewa
                Z.UnionSets(e);// Zbiory z wierzchołkami łączymy ze sobą
            }
        }

        // Wyświetlamy wyniki
        sb.append(T.print()).append("\n");


        sb.append(View.title("Reprezentacja listowa"));
        Z = new DSStruct(graphOrder);                  // Struktura zbiorów rozłącznych
        Q = new BinaryHeap(graphSize * 2);                     // Kolejka priorytetowa oparta na kopcu
        T = new MSTree(graphOrder);                    // Minimalne drzewo rozpinające
        for (i = 0; i < graphOrder; i++)
            Z.MakeSet(i);                 // Dla każdego wierzchołka tworzymy osobny zbiór
        //Dodawanie do kopca
        for (PathElement p : getElements(false)) Q.push(p);
        //Wykonywanie algorytmu
        for (i = 0; i < graphOrder - 1; i++) {         // Pętla wykonuje się n - 1 razy !!!
            do e = Q.pop();
            while (e != null && Z.FindSet(e.getStartVertex()) == Z.FindSet(e.getEndVertex()));
            if (e != null) {
                T.addEdge(e);                 // Dodajemy krawędź do drzewa
                Z.UnionSets(e);// Zbiory z wierzchołkami łączymy ze sobą
            }
        }
        sb.append(T.print()).append("\n");
        return sb.toString();
    }

    private List<PathElement> getElements(boolean matrix) {
        int i,j;
        List<PathElement> p = new ArrayList<>();
        if (matrix) {
            for (i = 0; i < graphOrder; i++)
                for (j = i + 1; j < graphOrder; j++)
                    if (adjacencyMatrix.getMatrixElement(i, j) != -1)
                        p.add(new PathElement(i, j, adjacencyMatrix.getMatrixElement(i, j)));
        } else
            for (i = 0; i < graphOrder; i++)
                for (j = 0; j < adjacencyLists.getList(i).size(); j += 2)
                    p.add(new PathElement(i, adjacencyLists.getList(i).get(j), adjacencyLists.getList(i).get(j + 1)));
        return p;
    }

    public String prim() {
        StringBuilder sb = new StringBuilder();


        sb.append(View.title("Reprezentacja macierzowa"));
        PathElement e;
        TNode t;
        int i, v;
        boolean[] visited;
        BinaryHeap Q;                // Kolejka priorytetowa oparta na kopcu
        MSTree T,G;                    // Minimalne drzewo rozpinające i Graf

        Q = new BinaryHeap(graphSize);
        T = new MSTree(graphOrder);
        G = new MSTree(graphOrder);
        visited = new boolean[graphOrder];
        for (PathElement p : getElements(true)) G.addEdge(p);

        // Tworzymy minimalne drzewo rozpinające

        v = 0;                          // Wierzchołek startowy
        visited[v] = true;              // Oznaczamy go jako odwiedzonego

        for (i = 1; i < graphOrder; i++)          // Do drzewa dodamy n - 1 krawędzi grafu
        {
            for (t = G.getAList(v); t!=null; t = t.getNext()) // Przeglądamy listę sąsiadów
                if (!visited[t.getV()])          // Jeśli sąsiad jest nieodwiedzony,
                    Q.push(new PathElement(v,t.getV(),t.getWeight()));// Dodajemy ją do kolejki priorytetowej

            do e =  Q.pop();
            while (visited[e.getEndVertex()]);       // Krawędź prowadzi poza drzewo?

            T.addEdge(e);                 // Dodajemy krawędź do drzewa rozpinającego
            visited[e.getEndVertex()] = true;         // Oznaczamy drugi wierzchołek jako odwiedzony
            v = e.getEndVertex();
        }

        // Wyświetlamy wyniki

        sb.append(T.print()).append("\n");


        sb.append(View.title("Reprezentacja listowa"));
        Q = new BinaryHeap(graphSize*2);
        T = new MSTree(graphOrder);
        G = new MSTree(graphOrder);
        visited = new boolean[graphOrder];
        for (PathElement p : getElements(false)) G.addEdge(p);

        // Tworzymy minimalne drzewo rozpinające

        v = 0;                          // Wierzchołek startowy
        visited[v] = true;              // Oznaczamy go jako odwiedzonego

        for (i = 1; i < graphOrder; i++)          // Do drzewa dodamy n - 1 krawędzi grafu
        {
            for (t = G.getAList(v); t!=null; t = t.getNext()) // Przeglądamy listę sąsiadów
                if (!visited[t.getV()])          // Jeśli sąsiad jest nieodwiedzony,
                    Q.push(new PathElement(v,t.getV(),t.getWeight()));// Dodajemy ją do kolejki priorytetowej

            do e =  Q.pop();
            while (visited[e.getEndVertex()]);       // Krawędź prowadzi poza drzewo?

            T.addEdge(e);                 // Dodajemy krawędź do drzewa rozpinającego
            visited[e.getEndVertex()] = true;         // Oznaczamy drugi wierzchołek jako odwiedzony
            v = e.getEndVertex();
        }

        // Wyświetlamy wyniki

        sb.append(T.print()).append("\n");
        return sb.toString();
    }

}
