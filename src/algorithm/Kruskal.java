package algorithm;

import addon.Task;
import representation.AdjacencyLists;
import representation.AdjacencyMatrix;
import structure.BinaryHeap;
import structure.DSStruct;
import structure.MSTree;
import structure.PathElement;

/**
 * Created by Tobiasz Rumian on 17.05.2017.
 */
public class Kruskal implements Algorithm {
    private Task task;

    public Kruskal(Task task) {
        this.task = task;
    }

    @Override
    public String preprocessMatrix(AdjacencyMatrix matrix) {

        PathElement e;
        int i;


        DSStruct Z = new DSStruct(task.getGraphOrder());                  // Struktura zbiorów rozłącznych
        BinaryHeap Q = new BinaryHeap(task.getGraphSize());                     // Kolejka priorytetowa oparta na kopcu
        MSTree T = new MSTree(task.getGraphOrder());                    // Minimalne drzewo rozpinające

        for (i = 0; i < task.getGraphOrder(); i++) Z.MakeSet(i);                 // Dla każdego wierzchołka tworzymy osobny zbiór

        for (i = 0; i < matrix.getSize(); i++)
            for (int j = i+1; j < matrix.getSize(); j++)
                if(matrix.getMatrixElement(i, j)!=-1)
                    Q.push(new PathElement(i, j, matrix.getMatrixElement(i, j)));

        for (i = 1; i < task.getGraphOrder(); i++)          // Pętla wykonuje się n - 1 razy !!!
        {
            do {
                e = Q.pop();

            } while (e!=null&&Z.FindSet(e.getStartVertex()) == Z.FindSet(e.getEndVertex()));
            if(e!=null){
                T.addEdge(e);                 // Dodajemy krawędź do drzewa
                Z.UnionSets(e);// Zbiory z wierzchołkami łączymy ze sobą
            }
        }

        // Wyświetlamy wyniki
        return T.print();
    }

    @Override
    public String preprocessLists(AdjacencyLists lists) {
        return "";
    }
}
