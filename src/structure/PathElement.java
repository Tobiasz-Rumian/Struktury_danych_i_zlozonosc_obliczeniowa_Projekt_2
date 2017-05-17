package structure;

/**
 * Created by Tobiasz Rumian on 17.05.2017.
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
    static public PathElement max(PathElement p1,PathElement p2){
        if(p1.compareTo(p2)==1)return p1;
        else return p2;
    }
}
