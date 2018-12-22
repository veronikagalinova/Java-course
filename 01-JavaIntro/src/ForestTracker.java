
public class ForestTracker {
	public char[][] trackForestTerrain(char[][] forest, int years) {
		char[][] result = new char[forest.length][forest[0].length];
		if (years < 10)
			return forest;
		for (int i = 0; i < years / 10; i++) {
			updateForest(forest, result);
		}
		return result;
	}

	private void updateForest(char[][] forest, char[][] result) {
		for (int i = 0; i < forest.length; i++) {
			for (int j = 0; j < forest[i].length; j++) {
				if (forest[i][j] == '1') {
					result[i][j] = '2';
				} else if (forest[i][j] == '2') {
					result[i][j] = '3';
				} else if (forest[i][j] == '3') {
					result[i][j] = '4';
				} else if (forest[i][j] == '4' && checkNeighbours(forest, i, j)) {
					result[i][j] = '3';
				} else {
					result[i][j] = forest[i][j];
				}
			}

		}
	}

	private boolean checkNeighbours(char[][] forest, int i, int j) {
		int count = 0;

		int[][] offsetRowCol = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };

		for (int[] offset : offsetRowCol) {
			int x = i + offset[0];
			int y = j + offset[1];

			if (x >= 0 && x < forest.length && y >= 0 && y < forest[i].length) {
				if (forest[x][y] == '4')
					count++;
			}
		}
		return count >= 3 ? true : false;
	}
}
