package addon;

import enums.Algorithm;
import representation.AdjacencyLists;
import representation.AdjacencyMatrix;
import structure.*;
import view.View;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Tobiasz Rumian on 17.05.2017.
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

    public void setAdjacencyLists(AdjacencyLists adjacencyLists) {
        this.adjacencyLists = adjacencyLists;
    }

    public void setAdjacencyMatrix(AdjacencyMatrix adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
    }

    public void setStartVertex(int startVertex) {
        if (typeOfTask == enums.Task.MST) return;
        this.startVertex = startVertex;
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
        graphSize = -1;
    }

    public void generateRandomGraph(int graphOrder, int density) {
        clear();
        BigDecimal b = new BigDecimal(((double)density / 100) * (((double)graphOrder * (double)graphOrder)-graphOrder));
        b=b.round(new MathContext(0, RoundingMode.FLOOR));
        int graphSize2= b.toBigInteger().intValue();
        graphSize = b.toBigInteger().intValue();
        int graphSize1 = (b.divide(new BigDecimal(2),0,RoundingMode.FLOOR)).intValue();
        boolean[][] used = new boolean[graphOrder][graphOrder];
        this.graphOrder = graphOrder;
        adjacencyLists = new AdjacencyLists(graphOrder);
        adjacencyMatrix = new AdjacencyMatrix(graphOrder);
        Random random = new Random();
        int i, j, src, dst, weight, edgesLeft=graphSize;
            src = random.nextInt(graphOrder-1);
            i = 0;
            while (i < (graphOrder - 1) && edgesLeft > 0) //PIERWSZA PETLA LACZY WSZYSTKIE WIERZCHOLKI ABY GRAF BYL SPOJNY
            {
                do {
                    dst = random.nextInt(graphOrder);
                } while (adjacencyMatrix.getDegree(dst) > 0 || dst == src);
                weight = random.nextInt(9) + 1;
                if(typeOfTask== enums.Task.MST){
                    adjacencyMatrix.add(src,dst,weight);
                    adjacencyLists.add(src,dst,weight);
                    used[src][dst]=true;
                    adjacencyMatrix.add(dst,src,weight);
                    adjacencyLists.add(dst,src,weight);
                    used[dst][src]=true;
                }else{
                    adjacencyMatrix.add(src,dst,weight);
                    adjacencyLists.add(src,dst,weight);
                    used[src][dst]=true;
                }
                src = dst;
                --edgesLeft;
                graphSize--;
                graphSize1-=2;
                i++;
            }

        for(i=0;i<graphOrder;i++) used[i][i]=true;
        if (typeOfTask == enums.Task.MST) {
            for (int x = 0; x < graphSize1; x++) {
                do {
                    i = random.nextInt(graphOrder);
                    j = random.nextInt(graphOrder);
                } while (used[i][j]);
                used[i][j] = true;
                used[j][i] = true;
                weight = random.nextInt(9) + 1;

                adjacencyLists.add(i, j, weight);
                adjacencyMatrix.add(i, j, weight);
                adjacencyLists.add(j, i, weight);
                adjacencyMatrix.add(j, i, weight);
            }
        }else {
            for (int x = 0; x < this.graphSize; x++) {
                do {
                    i = random.nextInt(graphOrder);
                    j=random.nextInt(graphOrder);
                } while (used[i][j]);
                used[i][j]=true;
                weight = random.nextInt(9) + 1;
                adjacencyMatrix.add(i, j, weight);
                adjacencyLists.add(i, j, weight);
            }
        }
        this.graphSize=graphSize2;
        System.out.println("gęstość:" + ((double) graphSize2 / (double) ((this.graphOrder * this.graphOrder)-graphOrder)));
    }

    public void testAlgorithm(Algorithm algorithm,boolean matrix){
        switch (algorithm) {
            case KRUSKAL:kruskal(matrix);
                break;
            case PRIM:prim(matrix);
                break;
            case DIJKSTR:dijkstr(matrix);
                break;
            case BELLMAN_FORD:bellmanFord(matrix);
                break;
        }
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
        heap = new BinaryHeap(graphSize * graphSize);                     // Kolejka priorytetowa oparta na kopcu
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
        BinaryHeap heap = new BinaryHeap(graphSize * graphSize);                // Kolejka priorytetowa oparta na kopcu
        MSTree mSTree = new MSTree(graphOrder), graph = new MSTree(graphOrder);                    // Minimalne drzewo rozpinające i Graf

        for (PathElement p : getElements(matrix)) graph.addEdge(p);

        // Tworzymy minimalne drzewo rozpinające

        visited[v] = true;              // Oznaczamy go jako odwiedzonego

        for (i = 1; i < graphOrder; i++) {         // Do drzewa dodamy n - 1 krawędzi grafu
            for (tNode = graph.getAList(v); tNode != null; tNode = tNode.getNext()) // Przeglądamy listę sąsiadów
                if (!visited[tNode.getV()])          // Jeśli sąsiad jest nieodwiedzony,
                    heap.push(new PathElement(v, tNode.getV(), tNode.getWeight()));// Dodajemy ją do kolejki priorytetowej

            do pathElement = heap.pop();
            while (pathElement != null &&visited[pathElement.getEndVertex()]);       // Krawędź prowadzi poza drzewo?
            if (pathElement != null) {
                mSTree.addEdge(pathElement);                 // Dodajemy krawędź do drzewa rozpinającego
                visited[pathElement.getEndVertex()] = true;         // Oznaczamy drugi wierzchołek jako odwiedzony
                v = pathElement.getEndVertex();
            }
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
}
