package representation;

/**
 * Created by Tobiasz Rumian on 14.05.2017.
 */
public class AdjacencyMatrix implements GraphRepresentation {
    private int[][] matrix;
    public AdjacencyMatrix(int graphOrder){
        matrix=new int[graphOrder][graphOrder];
        for(int i=0;i<graphOrder;i++){
            for (int j=0;j<graphOrder;j++){
                matrix[i][j]=-1;
            }
        }
    }

    @Override
    public void add(int start, int end, int weight) {
        matrix[start][end]=weight;
    }
}
