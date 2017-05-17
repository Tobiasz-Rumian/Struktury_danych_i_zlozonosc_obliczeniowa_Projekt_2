package algorithm;

import representation.AdjacencyLists;
import representation.AdjacencyMatrix;

/**
 * Created by Tobiasz Rumian on 17.05.2017.
 */
public interface Algorithm {
    String preprocessMatrix(AdjacencyMatrix matrix);
    String preprocessLists(AdjacencyLists lists);
}
