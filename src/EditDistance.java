

/**
 * fill in javadoc comments
 */
public class EditDistance implements Matchable {
	private String x, y;

	public EditDistance(String x, String y) {
		this.x = x;
		this.y = y;
	}

	public int width() {
		return x.length() + 1;
	}

	public int height() {
		return y.length() + 1;
	}

	/*
	 * cost of lining up either a character with a character or a character with
	 * a gap. There are only 3 possible directions - 'S', 'E' and 'D' for south,
	 * east and diagonal if direction is 'D' - it is a character lined with a a
	 * character if direction is 'S' or 'E' - it is a character lined with a gap
	 */
	public int matchCost(int row, int col, char direction) {
		if (col > x.length() || col < 0 || row > y.length() || row < 0) {
			return 0;
		}

		int cost = 0;

		if (direction == 'D') {
			if (col == x.length() || row == y.length()) {
				return 0;
			} else if (x.charAt(col) != y.charAt(row)) {
				cost = 1;
			} else {
				cost = 0;
			}
		} else if (direction == 'S' || direction == 'E') {
			cost = 2;
		} else {
			cost = 0;
		}
		return cost;
	}

}
