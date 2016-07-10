

/**
 * 
 */
public class CostMatrix {

	/**
	 * given a matchable object (two EditDistance objects or two images) fill up
	 * the cost matrix (the dynamic programming table)
	 */
	public static PathNode costRecursive(Matchable m) {
		PathNode[][] dp = new PathNode[m.height()][m.width()];
		for (int i = 0; i < m.height(); i++) {
			for (int j = 0; j < m.width(); j++) {
				dp[i][j] = costHelper(m, i, j);
				System.out.printf("%4d", dp[i][j].cost);
			}
			System.out.println();
		}

		return dp[0][0];
	}

	private static PathNode costHelper(Matchable m, int i, int j) {
		PathNode p = new PathNode(m.height() - 1, m.width() - 1, m.matchCost(
				m.height() - 1, m.width() - 1, 'D'), null);
		if (i == m.height() - 1 && j == m.width() - 1) {
			return p;
		}
		if (i == m.height() - 1) {
			p = new PathNode(m.height() - 1, j, m.matchCost(m.height() - 1, j,
					'E') + costHelper(m, m.height() - 1, j + 1).cost,
					costHelper(m, m.height() - 1, j + 1));
		} else if (j == m.width() - 1) {
			p = new PathNode(i, m.width() - 1, m.matchCost(i, m.width() - 1,
					'S') + costHelper(m, i + 1, m.width() - 1).cost,
					costHelper(m, i + 1, m.width() - 1));
		} else {
			int costD = costHelper(m, i + 1, j + 1).cost
					+ m.matchCost(i, j, 'D');
			int costS = costHelper(m, i + 1, j).cost + m.matchCost(i, j, 'S');
			int costE = costHelper(m, i, j + 1).cost + m.matchCost(i, j, 'E');
			int cost = Math.min(costD, Math.min(costE, costS));
			if (cost == costD) {
				p = new PathNode(i, j, costD, costHelper(m, i + 1, j + 1));
			} else if (cost == costS) {
				p = new PathNode(i, j, costS, costHelper(m, i + 1, j));
			} else {
				p = new PathNode(i, j, costE, costHelper(m, i, j + 1));
			}
		}

		return p;
	}

	/**
	 * given a matchable object (two EditDistance objects or two images) fill up
	 * the cost matrix in an iterative (nested loops) manner
	 */
	public static PathNode costIterative(Matchable m) {
		PathNode[][] dp = new PathNode[m.height()][m.width()];

		dp[m.height() - 1][m.width() - 1] = new PathNode(m.height() - 1,
				m.width() - 1, m.matchCost(m.height() - 1, m.width() - 1, 'D'),
				null);

		if (m.height() == 1 && m.width() == 1) {
			return dp[0][0];
		}
		int[][] costMatrix = new int[m.height()][m.width()];

		if (m.width() == 1) {
			for (int i = m.height() - 2; i >= 0; i--) {
				dp[i][m.width() - 1] = new PathNode(i, m.width() - 1,
						m.matchCost(i, m.width() - 1, 'S')
								+ dp[i + 1][m.width() - 1].cost,
						dp[i + 1][m.width() - 1]);
				costMatrix[i][m.width() - 1] = dp[i][m.width() - 1].cost;
			}

//			for (int i = 0; i < m.height(); i++) {
//				for (int j = 0; j < m.width(); j++) {
//					System.out.printf("%4d", costMatrix[i][j]);
//				}
//				System.out.println();
//			}

			return dp[0][0];
		}

		if (m.height() == 1) {
			for (int j = m.width() - 2; j >= 0; j--) {
				dp[m.height() - 1][j] = new PathNode(m.height() - 1, j,
						m.matchCost(m.height() - 1, j, 'E')
								+ dp[m.height() - 1][j + 1].cost,
						dp[m.height() - 1][j + 1]);
				costMatrix[m.height() - 1][j] = dp[m.height() - 1][j].cost;
			}

//			for (int i = 0; i < m.height(); i++) {
//				for (int j = 0; j < m.width(); j++) {
//					System.out.printf("%4d", costMatrix[i][j]);
//				}
//				System.out.println();
//			}

			return dp[0][0];
		}

		for (int i = m.height() - 2; i >= 0; i--) {
			dp[i][m.width() - 1] = new PathNode(i, m.width() - 1, m.matchCost(
					i, m.width() - 1, 'S') + dp[i + 1][m.width() - 1].cost,
					dp[i + 1][m.width() - 1]);
			costMatrix[i][m.width() - 1] = dp[i][m.width() - 1].cost;
		}

		for (int j = m.width() - 2; j >= 0; j--) {
			dp[m.height() - 1][j] = new PathNode(m.height() - 1, j,
					m.matchCost(m.height() - 1, j, 'E')
							+ dp[m.height() - 1][j + 1].cost,
					dp[m.height() - 1][j + 1]);
			costMatrix[m.height() - 1][j] = dp[m.height() - 1][j].cost;
		}

		for (int i = m.height() - 2; i >= 0; i--) {
			for (int j = m.width() - 2; j >= 0; j--) {
				int costD = m.matchCost(i, j, 'D') + dp[i + 1][j + 1].cost;
				int costS = m.matchCost(i, j, 'S') + dp[i + 1][j].cost;
				int costE = m.matchCost(i, j, 'E') + dp[i][j + 1].cost;
				int cost = Math.min(costD, Math.min(costS, costE));

				PathNode nextNode;
				if (cost == costD) {
					nextNode = dp[i + 1][j + 1];
				} else if (cost == costS) {
					nextNode = dp[i + 1][j];
				} else {
					nextNode = dp[i][j + 1];
				}
				dp[i][j] = new PathNode(i, j, cost, nextNode);
				costMatrix[i][j] = dp[i][j].cost;
			}
		}
//		for (int i = 0; i < m.height(); i++) {
//			for (int j = 0; j < m.width(); j++) {
//				System.out.printf("%4d", costMatrix[i][j]);
//			}
//			System.out.println();
//		}

		return dp[0][0];
	}

	/**
	 * fill main up for testing. alternative test with unit tests!
	 */
	public static void main(String args[]) {
		EditDistance e = new EditDistance("saturday", "sunday");

		PathNode current = costIterative(e);
		System.out.println(current.cost);
		while (current != null) {
			System.out.printf("cost: %3d  ", current.cost);
			System.out.printf("row: %3d  ", current.row);
			System.out.printf("col: %3d  ", current.col);
			System.out.println();
			current = current.next;
		}

		System.out.println(costRecursive(e).cost);

//		 Picture leftPic = new Picture("example-blue.png");
//		 Picture rightPic = new Picture("example-red.png");
		 Picture leftPic = new Picture("sea1.png");
		 Picture rightPic = new Picture("sea2.png");
		 ImageSeam is = new ImageSeam(leftPic, rightPic);
		 PathNode path = costIterative(is);
		 System.out.println(path.cost);
		 is.showSeam(path);
		 Picture pic = is.getStitchedPicture(path);
		 pic.show();

	}

}
