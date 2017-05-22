package structure;

/**
 * Klasa reprezentująca zbiory rozłączne.
 * @author Tobiasz Rumian
 */
public class DSStruct {
    private DSNode[] z;
    public DSStruct(int n){
        z = new DSNode [n];
    }
    public void MakeSet(int v){
        z[v]=new DSNode(v,0);
    }
    public int FindSet(int v){
        if(z[v].getUp() != v) z[v].setUp(FindSet(z[v].getUp()));
        return z[v].getUp();
    }
    public void UnionSets(PathElement e){
        int ru,rv;

        ru = FindSet(e.getStartVertex());             // Wyznaczamy korzeń drzewa z węzłem u
        rv = FindSet(e.getEndVertex());             // Wyznaczamy korzeń drzewa z węzłem v
        if(ru != rv)                    // Korzenie muszą być różne
        {
            if(z[ru].getRank() > z[rv].getRank())   // Porównujemy rangi drzew
                z[rv].setUp(ru);           // ru większe, dołączamy rv
            else
            {
                z[ru].setUp(rv);            // równe lub rv większe, dołączamy ru
                if(z[ru].getRank() == z[rv].getRank()) z[rv].setRank(z[rv].getRank()+1);
            }
        }
    }
}
