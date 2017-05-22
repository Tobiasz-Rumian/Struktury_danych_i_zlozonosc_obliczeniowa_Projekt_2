package structure;

/**
 * Klasa reprezentująca krawędź.
 * @author Tobiasz Rumian
 */
public class PathElement implements Comparable{
    private int startVertex;
    private int endVertex;
    private int weight;

    public PathElement(int startVertex, int endVertex, int weight) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        this.weight = weight;
    }

    public int getStartVertex() {
        return startVertex;
    }

    public int getEndVertex() {
        return endVertex;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null || getClass() != o.getClass()) return -1;
        return ((Integer)weight).compareTo(((PathElement)o).getWeight());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathElement that = (PathElement) o;
        if (startVertex != that.startVertex) return false;
        if (endVertex != that.endVertex) return false;
        return weight == that.weight;
    }

    @Override
    public int hashCode() {
        int result = startVertex;
        result = 31 * result + endVertex;
        result = 31 * result + weight;
        return result;
    }

    @Override
    public String toString(){
        return "v1 "+startVertex+" v2 "+endVertex+" w "+weight;
    }
}
