

/**
 * Interface for Matchable
 * 
 */
public interface Matchable {
	public int height();

	public int width();

	public int matchCost(int row, int col, char direction);
}
