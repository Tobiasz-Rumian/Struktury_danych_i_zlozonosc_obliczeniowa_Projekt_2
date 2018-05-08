package addon;

import enums.Task;
import representation.AdjacencyLists;
import representation.AdjacencyMatrix;

public class GraphsContainer {

	private AdjacencyLists adjacencyLists;
	private AdjacencyMatrix adjacencyMatrix;
	private int startVertex;
	private enums.Task typeOfTask;
	private int graphSize;
	private int graphOrder;

	public void clear() {
		adjacencyLists = null;
		adjacencyMatrix = null;
		startVertex = -1;
		graphSize = -1;
	}

	public AdjacencyLists getAdjacencyLists() {
		return adjacencyLists;
	}

	public void setAdjacencyLists(AdjacencyLists adjacencyLists) {
		this.adjacencyLists = adjacencyLists;
	}

	public AdjacencyMatrix getAdjacencyMatrix() {
		return adjacencyMatrix;
	}

	public void setAdjacencyMatrix(AdjacencyMatrix adjacencyMatrix) {
		this.adjacencyMatrix = adjacencyMatrix;
	}

	public int getStartVertex() {
		return startVertex;
	}

	public void setStartVertex(int startVertex) {
		this.startVertex = startVertex;
	}

	public Task getTypeOfTask() {
		return typeOfTask;
	}

	public void setTypeOfTask(Task typeOfTask) {
		this.typeOfTask = typeOfTask;
	}

	public int getGraphSize() {
		return graphSize;
	}

	public void setGraphSize(int graphSize) {
		this.graphSize = graphSize;
	}

	public int getGraphOrder() {
		return graphOrder;
	}

	public void setGraphOrder(int graphOrder) {
		this.graphOrder = graphOrder;
	}
}
