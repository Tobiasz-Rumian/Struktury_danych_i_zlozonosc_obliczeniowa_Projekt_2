package representation;

import java.util.ArrayList;

/**
 * Created by Tobiasz Rumian on 14.05.2017.
 */
public class AdjacencyLists implements GraphRepresentation {
    ArrayList<Integer>[] lists;
    public AdjacencyLists(int graphOrder){
        lists = (ArrayList<Integer>[])new ArrayList[graphOrder];

    }

    @Override
    public void add(int start, int end, int weight) {
        lists[start].add(end);
        lists[start].add(weight);
    }
}
