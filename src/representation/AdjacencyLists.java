package representation;

import java.util.ArrayList;

/**
 * Klasa reprezentująca reprezentacje listową.
 */
public class AdjacencyLists implements GraphRepresentation {

	private ArrayList<Integer>[] lists;
	private int size;

	public AdjacencyLists(int graphOrder) {
		lists = (ArrayList<Integer>[]) new ArrayList[graphOrder];
		for (int i = 0; i < graphOrder; i++) {
			lists[i] = new ArrayList<>();
		}
		size = graphOrder;
	}

	@Override
	public void add(int start, int end, int weight) {
		lists[start].add(end);
		lists[start].add(weight);
	}

	public ArrayList<Integer> getList(int i) {
		return lists[i];
	}

	public int getDegree(int vertice) {
		return lists[vertice].size() / 2;
	}

	public int getSize() {
		return size;
	}
}
