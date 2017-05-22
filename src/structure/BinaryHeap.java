package structure;

/**
 * Klasa reprezentująca tablice.
 * Korzysta z interfejsu struktur.
 *
 * @author Tobiasz Rumian.
 */
public class BinaryHeap{
    private int heapSize = 0; //przechowuje informacje o wielkości kopca
    private PathElement[] heapTable ; //tablica przechowująca kopiec
    /*
    numer lewego syna = 2k + 1
    numer prawego syna = 2k + 2
    numer ojca = [(k - 1) / 2], dla k > 0
    lewy syn istnieje, jeśli 2k + 1 < n
    prawy syn istnieje, jeśli 2k + 2 < n
    węzeł k jest liściem, jeśli 2k + 2 > n
     */

    public BinaryHeap(int size){
        heapTable = new PathElement[size];
    }

    /**
     * Usuwa z kopca korzeń
     * @return korzeń kopca
     */
    public PathElement pop(){
        if(heapSize==0)return null;
        PathElement e = heapTable[0];
        if(heapSize==1) clear();
        else{
            heapSize--;
            heapTable[0] = heapTable[heapSize];
            heapifyDown(0);
        }
        return e;
    }

    /**
     * Dodaje wartość do kopca.
     * @param element dodawana wartość.
     */
    public void push(PathElement element){
        heapTable[heapSize] = element;
        if (heapSize == 0) heapSize++;
        else{
            heapifyUp(heapSize);//Sortujemy kopiec w górę
            heapSize++;//Zwiększamy rozmiar kopca o 1
        }
    }

    private void clear() {
        heapSize = 0;
    }


    /**
     * Funkcja układająca kopiec po dodaniu nowego elementu.
     * @param index indeks nowego elementu.
     */
    private void heapifyUp(int index) {
        if (index == 0) return;
        PathElement value = heapTable[index];
        int parentIndex = (index - 1) / 2;
        if (value.compareTo(heapTable[parentIndex])!=1) {
            heapTable[index] = heapTable[parentIndex];
            heapTable[parentIndex] = value;
        }
        heapifyUp(parentIndex);//Rekurencyjnie przechodzi w górę kopca
    }
    /**
     * Funkcja układająca kopiec po usunięciu elementu.
     * @param index indeks usuwanego elementu.
     */
    private void heapifyDown(int index) {
        if (((2 * index) + 2) > heapSize) return;//Sprawdza, czy indeks dziecka nie przekroczy wielkości kopca
        PathElement value = heapTable[index];
        int leftChildIndex = (2 * index) + 1;
        int rightChildIndex = (2 * index) + 2;
        int biggestChild=leftChildIndex;
        if(heapTable[leftChildIndex].compareTo(heapTable[rightChildIndex])==1)biggestChild=rightChildIndex;
        if (value.compareTo(heapTable[biggestChild])!=-1 ) {//Jeżeli wartość jest mniejsza od sprawdzanego węzła, zamienia je miejscami
            heapTable[index] = heapTable[biggestChild];
            heapTable[biggestChild] = value;
        }
        heapifyDown(biggestChild);//Rekurencyjnie przechodzi w dół kopca
    }
}
