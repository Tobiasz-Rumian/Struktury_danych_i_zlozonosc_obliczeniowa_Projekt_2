package addon;

import enums.Algorithm;
import representation.AdjacencyLists;
import representation.AdjacencyMatrix;
import structure.*;
import view.View;

import java.util.ArrayList;
import java.util.LinkedList;
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

    public void setStartVertex(int startVertex) {
        if (typeOfTask == enums.Task.MST) return;
        this.startVertex = startVertex;
    }

    public void setEndVertex(int endVertex) {
        if (typeOfTask != enums.Task.MP) return;
        this.endVertex = endVertex;
    }

    public enums.Task getTypeOfTask() {
        return typeOfTask;
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
                algorithm[1] = Algorithm.BELLMAN_FORD;
                break;
            case MP:
                algorithm[0] = Algorithm.FORD_FULKERSON;
                algorithm[1] = Algorithm.EDMONS_KARP;
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
                } else sb.append(String.format("% 3d", adjacencyMatrix.getMatrixElement(i, j))).append("\t");
            }

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

    public String getAlgorithm(Algorithm algorithm) {
        StringBuilder sb = new StringBuilder();
        switch (algorithm) {
            case KRUSKAL:
                sb.append(View.title("Reprezentacja macierzowa"));
                sb.append(kruskal(true));
                sb.append("\n");
                sb.append(View.title("Reprezentacja listowa"));
                sb.append(kruskal(false));
                sb.append("\n");
                break;
            case PRIM:
                sb.append(View.title("Reprezentacja macierzowa"));
                sb.append(prim(true));
                sb.append("\n");
                sb.append(View.title("Reprezentacja listowa"));
                sb.append(prim(false));
                sb.append("\n");
                break;
            case DIJKSTR:
                sb.append(View.title("Reprezentacja macierzowa"));
                sb.append(dijkstr(true));
                sb.append("\n");
                sb.append(View.title("Reprezentacja listowa"));
                sb.append(dijkstr(false));
                sb.append("\n");
                break;
            case BELLMAN_FORD:
                sb.append(View.title("Reprezentacja macierzowa"));
                sb.append(bellmanFord(true));
                sb.append("\n");
                sb.append(View.title("Reprezentacja listowa"));
                sb.append(bellmanFord(false));
                sb.append("\n");
                break;
            case FORD_FULKERSON:
                sb.append(View.title("Reprezentacja macierzowa"));
                sb.append(fordFulkerson(true));
                sb.append("\n");
                sb.append(View.title("Reprezentacja listowa"));
                sb.append(fordFulkerson(false));
                sb.append("\n");
                break;
            case EDMONS_KARP:
                sb.append(View.title("Reprezentacja macierzowa"));
                sb.append(edmondsKarp(true));
                sb.append("\n");
                sb.append(View.title("Reprezentacja listowa"));
                sb.append(edmondsKarp(false));
                sb.append("\n");
                break;
        }
        return sb.toString();
    }

    private List<PathElement> getElements(boolean matrix) {
        int i, j;
        List<PathElement> p = new ArrayList<>();
        if (matrix) {
            if (typeOfTask == enums.Task.MST) {
                for (i = 0; i < graphOrder; i++)
                    for (j = i + 1; j < graphOrder; j++)
                        if (adjacencyMatrix.getMatrixElement(i, j) != Integer.MIN_VALUE)
                            p.add(new PathElement(i, j, adjacencyMatrix.getMatrixElement(i, j)));
            } else {
                for (i = 0; i < graphOrder; i++)
                    for (j = 0; j < graphOrder; j++)
                        if (adjacencyMatrix.getMatrixElement(i, j) != Integer.MIN_VALUE)
                            p.add(new PathElement(i, j, adjacencyMatrix.getMatrixElement(i, j)));
            }
        } else
            for (i = 0; i < graphOrder; i++)
                for (j = 0; j < adjacencyLists.getList(i).size(); j += 2)
                    p.add(new PathElement(i, adjacencyLists.getList(i).get(j), adjacencyLists.getList(i).get(j + 1)));
        return p;
    }

    private String kruskal(boolean matrix) {
        PathElement pathElement;
        int i;
        DSStruct dsStruct;
        BinaryHeap heap;
        MSTree msTree;

        dsStruct = new DSStruct(graphOrder);                  // Struktura zbiorów rozłącznych
        heap = new BinaryHeap(graphSize);                     // Kolejka priorytetowa oparta na kopcu
        msTree = new MSTree(graphOrder);                    // Minimalne drzewo rozpinające
        for (i = 0; i < graphOrder; i++)
            dsStruct.MakeSet(i);                 // Dla każdego wierzchołka tworzymy osobny zbiór
        //Dodawanie do kopca
        for (PathElement p : getElements(matrix)) heap.push(p);

        //Wykonywanie algorytmu
        for (i = 1; i < graphOrder; i++) {          // Pętla wykonuje się n - 1 razy !!!
            do pathElement = heap.pop();
            while (pathElement != null && dsStruct.FindSet(pathElement.getStartVertex()) == dsStruct.FindSet(pathElement.getEndVertex()));
            if (pathElement != null) {
                msTree.addEdge(pathElement);                 // Dodajemy krawędź do drzewa
                dsStruct.UnionSets(pathElement);// Zbiory z wierzchołkami łączymy ze sobą
            }
        }
        return msTree.print() + "\n";
    }

    private String prim(boolean matrix) {
        PathElement pathElement;
        TNode tNode;
        int i, v = 0;
        boolean[] visited = new boolean[graphOrder];
        BinaryHeap heap = new BinaryHeap(graphSize);                // Kolejka priorytetowa oparta na kopcu
        MSTree mSTree = new MSTree(graphOrder), graph = new MSTree(graphOrder);                    // Minimalne drzewo rozpinające i Graf

        for (PathElement p : getElements(matrix)) graph.addEdge(p);

        // Tworzymy minimalne drzewo rozpinające

        visited[v] = true;              // Oznaczamy go jako odwiedzonego

        for (i = 1; i < graphOrder; i++) {         // Do drzewa dodamy n - 1 krawędzi grafu
            for (tNode = graph.getAList(v); tNode != null; tNode = tNode.getNext()) // Przeglądamy listę sąsiadów
                if (!visited[tNode.getV()])          // Jeśli sąsiad jest nieodwiedzony,
                    heap.push(new PathElement(v, tNode.getV(), tNode.getWeight()));// Dodajemy ją do kolejki priorytetowej

            do pathElement = heap.pop();
            while (visited[pathElement.getEndVertex()]);       // Krawędź prowadzi poza drzewo?

            mSTree.addEdge(pathElement);                 // Dodajemy krawędź do drzewa rozpinającego
            visited[pathElement.getEndVertex()] = true;         // Oznaczamy drugi wierzchołek jako odwiedzony
            v = pathElement.getEndVertex();
        }
        // Wyświetlamy wyniki
        return mSTree.print() + "\n";
    }

    private String dijkstr(boolean matrix) {
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

        for (PathElement pathElement : getElements(matrix))
            graph[pathElement.getStartVertex()] = new TNode(
                    graph[pathElement.getStartVertex()],
                    pathElement.getEndVertex(),
                    pathElement.getWeight());

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
                if (left >= heapLength) break;     // Kończymy, jeśli lewy potomek poza kopcem
                dMin = costTable[heap[left]];          // Wyznaczamy mniejszego potomka
                pMin = left;
                if ((right < heapLength) && (dMin > costTable[heap[right]])) {
                    dMin = costTable[heap[right]];
                    pMin = right;
                }
                if (costTable[heap[parent]] <= dMin) break; // Jeśli własność kopca zachowana, kończymy
                x = heap[parent]; heap[parent] = heap[pMin]; heap[pMin] = x; // Przywracamy własność kopca
                heapPointer[heap[parent]] = parent; heapPointer[heap[pMin]] = pMin;      // na danym poziomie
                parent = pMin;              // i przechodzimy na poziom niższy kopca
            }

            // Znaleziony wierzchołek przenosimy do S

            QS[u] = true;

            // Modyfikujemy odpowiednio wszystkich sąsiadów u, którzy są w Q

            for (pw = graph[u]; pw != null; pw = pw.getNext())
                if (!QS[pw.getV()] && (costTable[pw.getV()] > costTable[u] + pw.getWeight())) {
                    costTable[pw.getV()] = costTable[u] + pw.getWeight();
                    predecessorsTable[pw.getV()] = u;

                    // Po zmianie d[v] odtwarzamy własność kopca, idąc w górę

                    for (child = heapPointer[pw.getV()]; child != 0; child = parent) {
                        parent = child / 2;
                        if (costTable[heap[parent]] <= costTable[heap[child]]) break;
                        x = heap[parent]; heap[parent] = heap[child]; heap[child] = x;
                        heapPointer[heap[parent]] = parent; heapPointer[heap[child]] = child;
                    }
                }
        }
        //Wyświetlanie wyniku.
        for (i = 0; i < graphOrder; i++) {
            sb.append(i).append(": ");
            for (j = i; j > -1; j = predecessorsTable[j]) stack[stackPointer++] = j;
            while (stackPointer != 0) sb.append(stack[--stackPointer]).append(" ");
            sb.append("$").append(costTable[i]).append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    private String bellmanFord(boolean matrix) {
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

        for (PathElement pathElement : getElements(matrix))
            tNodes[pathElement.getStartVertex()] = new TNode(
                    tNodes[pathElement.getStartVertex()],
                    pathElement.getEndVertex(),
                    pathElement.getWeight());

        // Wyznaczamy najkrótsze ścieżki algorytmem Bellmana-Forda


        accessCosts[startVertex] = 0;                       // Zerujemy koszt dojścia do v
        for (i = 1; i < graphOrder; i++) {        // Pętla relaksacji
            test = true;                  // Oznacza, że algorytm nie wprowadził zmian do d i p
            for (j = 0; j < graphOrder; j++)        // Przechodzimy przez kolejne wierzchołki grafu
                for (tNode = tNodes[j]; tNode != null; tNode = tNode.getNext()) // Przeglądamy listę sąsiadów wierzchołka x
                    if (accessCosts[tNode.getV()] > accessCosts[j] + tNode.getWeight()) { // Sprawdzamy warunek relaksacji
                        test = false;           // Jest zmiana w d i p
                        accessCosts[tNode.getV()] = accessCosts[j] + tNode.getWeight(); // Relaksujemy krawędź z x do jego sąsiada
                        predecessors[tNode.getV()] = j;           // Poprzednikiem sąsiada będzie x
                    }
            if (test) {
                negativeCycle = true;
                // Sprawdzamy istnienie ujemnego cyklu
                for (j = 0; j < graphOrder; j++)
                    for (tNode = tNodes[j]; tNode != null; tNode = tNode.getNext())
                        if (!(accessCosts[tNode.getV()] > accessCosts[j] + tNode.getWeight())) negativeCycle = true;
                break;// Jeśli nie było zmian, to kończymy
            }
        }

        if (negativeCycle) {
            S = new int[graphOrder];              // Tworzymy prosty stos
            stackPointer = 0;

            for (i = 0; i < graphOrder; i++) {
                sb.append(i).append(": ");
                for (j = i; j != -1; j = predecessors[j]) // Wierzchołki ścieżki umieszczamy na stosie
                    S[stackPointer++] = j;            // w kolejności od ostatniego do pierwszego

                while (stackPointer != 0) sb.append(S[--stackPointer]).append(" ");// Wierzchołki ze stosu drukujemy
                sb.append("$").append(accessCosts[i]).append("\n");

            }
        } else sb.append("Znaleziono negatywny cykl!").append("\n");
        return sb.toString();
    }

    private String edmondsKarp(boolean matrix) {
        Queue Q = new Queue();                        // Kolejka
        int[][] C = new int[graphOrder][graphOrder];// C - przepustowości krawędzi
        int[][] F = new int[graphOrder][graphOrder];// F - przepływy w krawędziach
        int[] P = new int[graphOrder];// P   - poprzedniki na ścieżkach tworzonych przez BFS
        int[] CFP = new int[graphOrder]; // CFP - przepustowość ścieżek
        int fmax, cp, x, y, i;    // Zmienne proste algorytmu
        boolean esc;                       // Do wychodzenia z zagnieżdżonych pętli
        StringBuilder sb = new StringBuilder();

        for (PathElement pathElement : getElements(matrix))
            C[pathElement.getStartVertex()][pathElement.getEndVertex()] = pathElement.getWeight();

        fmax = 0; //Maksymalny przepływ równy zero
        while (true) {
            for (i = 0; i < graphOrder; i++) P[i] = -1;
            P[startVertex] = -2;
            CFP[startVertex] = Integer.MAX_VALUE;
            while (!Q.empty()) Q.pop();
            Q.push(startVertex);
            esc = false;
            while (!Q.empty()) {
                x = Q.front();
                Q.pop();
                for (y = 0; y < graphOrder; y++) {
                    cp = C[x][y] - F[x][y];
                    if (cp != 0 && (P[y] == -1)) {
                        P[y] = x;
                        if (CFP[x] > cp) CFP[y] = cp;
                        else CFP[y] = CFP[x];
                        if (y == endVertex) {
                            fmax += CFP[endVertex];
                            i = y;
                            while (i != startVertex) {
                                x = P[i];
                                F[x][i] += CFP[endVertex];
                                F[i][x] -= CFP[endVertex];
                                i = x;
                            }
                            esc = true; break;
                        }
                        Q.push(y);
                    }
                }
            if (esc) break;
            }
            if (!esc) break;
        }
        sb.append("\n").append("fmax = ").append(fmax).append("\n\n"); // wartość maksymalnego przepływu
        for (x = 0; x < graphOrder; x++)
            for (y = 0; y < graphOrder; y++)
                if (C[x][y] != 0) sb.append(x).
                        append(" -> ").
                        append(y).append(" ").
                        append(F[x][y]).append(":").
                        append(C[x][y]).append("\n");
        sb.append("\n");
        return sb.toString();
    }

    private String fordFulkerson(boolean matrix) {
        int[][] graph = new int[graphOrder][graphOrder];
        int u, v;
        for (PathElement pathElement:getElements(matrix))
            graph[pathElement.getStartVertex()][pathElement.getEndVertex()] = pathElement.getWeight();


        // Create a residual graph and fill the residual graph
        // with given capacities in the original graph as
        // residual capacities in residual graph
        // Residual graph where rGraph[i][j] indicates
        // residual capacity of edge from i to j (if there
        // is an edge. If rGraph[i][j] is 0, then there is
        // not)
        int rGraph[][] = new int[graphOrder][graphOrder];
        for (u = 0; u < graphOrder; u++)
            for (v = 0; v < graphOrder; v++)
                rGraph[u][v] = graph[u][v];
        int parent[] = new int[graphOrder];// This array is filled by BFS and to store path
        int max_flow = 0;  // There is no flow initially
        // Augment the flow while there is path from source to sink
        do {

            /* Returns true if there is a path from source 's' to sink
             't' in residual graph. Also fills parent[] to store the
             path */

            boolean visited[] = new boolean[graphOrder];// Create a visited array and mark all vertices as not visited
            for (int i = 0; i < graphOrder; ++i) visited[i] = false;
            // Create a queue, enqueue source vertex and mark source vertex as visited
            LinkedList<Integer> queue = new LinkedList<>();
            queue.add(startVertex);
            visited[startVertex] = true;
            parent[startVertex] = -1;
            while (queue.size() != 0) {
                u = queue.poll();
                for (v = 0; v < graphOrder; v++) {
                    if (!visited[v] && rGraph[u][v] > 0) {
                        queue.add(v);
                        parent[v] = u;
                        visited[v] = true;
                    }
                }
            }
            if(visited[endVertex])break; // Jeżeli nie dotarliśmy do odpływu ze źródła, zakończ.

            // Find minimum residual capacity of the edhes
            // along the path filled by BFS. Or we can say
            // find the maximum flow through the path found.
            int path_flow = Integer.MAX_VALUE;
            for (v = endVertex; v != startVertex; v = parent[v]) {
                u = parent[v];
                path_flow = Math.min(path_flow, rGraph[u][v]);
            }
            // update residual capacities of the edges and
            // reverse edges along the path
            for (v = endVertex; v != startVertex; v = parent[v]) {
                u = parent[v];
                rGraph[u][v] -= path_flow;
                rGraph[v][u] += path_flow;
            }
            max_flow += path_flow;// Add path flow to overall flow
        } while (true);
       return "The maximum possible flow is " + max_flow;
    }
}
