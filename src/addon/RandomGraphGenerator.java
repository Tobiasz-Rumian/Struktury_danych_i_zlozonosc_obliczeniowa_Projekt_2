package addon;

import enums.Task;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;
import representation.AdjacencyLists;
import representation.AdjacencyMatrix;

public class RandomGraphGenerator {

	/**
	 * Generuje losowy graf.
	 *
	 * @param graphOrder Ilość wierzchołków grafu.
	 * @param density Gęstość grafu w procentach.
	 */
	public static GraphsContainer generateRandomGraph(int graphOrder, int density, int graphSize, Task typeOfTask) {
		GraphsContainer graphsContainer = new GraphsContainer();
		graphsContainer.setGraphOrder(graphOrder);
		graphsContainer.setGraphSize(graphSize);
		graphsContainer.setTypeOfTask(typeOfTask);
		BigDecimal b = new BigDecimal(
			((double) density / 100) * (((double) graphOrder * (double) graphOrder) - graphOrder));
		b = b.round(new MathContext(0, RoundingMode.FLOOR));
		int graphSize2 = b.toBigInteger().intValue();
		graphsContainer.setGraphSize(b.toBigInteger().intValue());
		int graphSize1 = (b.divide(new BigDecimal(2), 0, RoundingMode.FLOOR)).intValue();
		boolean[][] used = new boolean[graphOrder][graphOrder];
		graphsContainer.setGraphOrder(graphOrder);
		graphsContainer.setAdjacencyLists(new AdjacencyLists(graphOrder));
		graphsContainer.setAdjacencyMatrix(new AdjacencyMatrix(graphOrder));
		Random random = new Random();
		int i, j, src, dst, weight, edgesLeft = graphSize;
		src = random.nextInt(graphOrder - 1);
		i = 0;
		while (i < (graphOrder - 1) && edgesLeft > 0) { //PIERWSZA PETLA LACZY WSZYSTKIE WIERZCHOLKI ABY GRAF BYL SPOJNY
			do {
				dst = random.nextInt(graphOrder);
			} while (graphsContainer.getAdjacencyMatrix().getDegree(dst) > 0 || dst == src);
			weight = random.nextInt(9) + 1;
			if (typeOfTask == enums.Task.MST) {
				graphsContainer.getAdjacencyMatrix().add(src, dst, weight);
				graphsContainer.getAdjacencyLists().add(src, dst, weight);
				used[src][dst] = true;
				graphsContainer.getAdjacencyMatrix().add(dst, src, weight);
				graphsContainer.getAdjacencyLists().add(dst, src, weight);
				used[dst][src] = true;
			} else {
				graphsContainer.getAdjacencyMatrix().add(src, dst, weight);
				graphsContainer.getAdjacencyLists().add(src, dst, weight);
				used[src][dst] = true;
			}
			src = dst;
			--edgesLeft;
			graphSize--;
			graphSize1 -= 2;
			i++;
		}

		for (i = 0; i < graphOrder; i++) {
			used[i][i] = true;
		}
		if (typeOfTask == enums.Task.MST) {
			for (int x = 0; x < graphSize1; x++) {
				do {
					i = random.nextInt(graphOrder);
					j = random.nextInt(graphOrder);
				} while (used[i][j]);
				used[i][j] = true;
				used[j][i] = true;
				weight = random.nextInt(9) + 1;

				graphsContainer.getAdjacencyLists().add(i, j, weight);
				graphsContainer.getAdjacencyMatrix().add(i, j, weight);
				graphsContainer.getAdjacencyLists().add(j, i, weight);
				graphsContainer.getAdjacencyMatrix().add(j, i, weight);
			}
		} else {
			for (int x = 0; x < graphSize; x++) {
				do {
					i = random.nextInt(graphOrder);
					j = random.nextInt(graphOrder);
				} while (used[i][j]);
				used[i][j] = true;
				weight = random.nextInt(9) + 1;
				graphsContainer.getAdjacencyMatrix().add(i, j, weight);
				graphsContainer.getAdjacencyLists().add(i, j, weight);
			}
		}
		graphsContainer.setGraphSize(graphSize2);
		System.out.println("gęstość:" + ((double) graphSize2 / (double) ((graphOrder * graphOrder) - graphOrder)));
		return graphsContainer;
	}
}
