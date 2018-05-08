package representation;

/**
 * Klasa reprezentująca reprezentacje macierzową.
 */
public class AdjacencyMatrix implements GraphRepresentation {

	private int[][] matrix;
	private int size;

	public AdjacencyMatrix(int graphOrder) {
		size = graphOrder;
		matrix = new int[graphOrder][graphOrder];
		for (int i = 0; i < graphOrder; i++) {
			for (int j = 0; j < graphOrder; j++) {
				matrix[i][j] = Integer.MIN_VALUE;
			}
		}
	}

	@Override
	public void add(int start, int end, int weight) {
		matrix[start][end] = weight;
	}

	public int getMatrixElement(int x, int y) {
		return matrix[x][y];
	}

	public int getDegree(int vertice) {
		int degree = 0;
		for (int i = 0; i < size; i++) {
			if (matrix[vertice][i] != Integer.MIN_VALUE) {
				degree++;
			}
		}
		return degree;
	}

	public int getSize() {
		return size;
	}
}
