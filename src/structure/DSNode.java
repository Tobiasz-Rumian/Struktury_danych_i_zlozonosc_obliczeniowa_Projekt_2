package structure;

/**
 * Klasa reprezentująca element zbioru rozłącznego.
 *
 * @author Tobiasz Rumian
 */
public class DSNode {

	private int up, rank;

	public DSNode(int up, int rank) {
		this.up = up;
		this.rank = rank;
	}

	public int getUp() {
		return up;
	}

	public void setUp(int up) {
		this.up = up;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
}
