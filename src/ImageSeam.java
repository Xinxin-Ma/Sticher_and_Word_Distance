

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;

public class ImageSeam implements Matchable {
	private Picture left, right;

	public ImageSeam(Picture left, Picture right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * make an Image seam object with left - left image filename right - right
	 * image filename
	 * 
	 * @throws IOException
	 */
	public ImageSeam(String left, String right) throws IOException {
		Picture leftPicture = new Picture(left);
		this.left = leftPicture;
		Picture rightPicture = new Picture(right);
		this.right = rightPicture;
	}

	public int width() {
		if (left.width() != right.width()) {
			throw new IllegalArgumentException();
		}
		return left.width();
	}

	public int height() {
		if (left.height() != right.height()) {
			throw new IllegalArgumentException();
		}
		return left.height();
	}

	public int matchCost(int row, int col, char direction) {
		if (row < 0 || col < 0 || col >= left.width() || col >= right.width()
				|| row >= left.height() || row >= right.height()) {
			throw new IndexOutOfBoundsException();
		}

		int rLeft = left.get(col, row).getRed();
		int gLeft = left.get(col, row).getGreen();
		int bLeft = left.get(col, row).getBlue();
		int rRight = right.get(col, row).getRed();
		int gRight = right.get(col, row).getGreen();
		int bRight = right.get(col, row).getBlue();
		int cost = (rLeft - rRight) * (rLeft - rRight) + (bLeft - bRight)
				* (bLeft - bRight) + (gLeft - gRight) * (gLeft - gRight);

		return cost;
	}

	public void showSeam(PathNode path) {

		Color white = new Color(255, 255, 255);
		Picture newPicture = new Picture(left.width(), left.height());
		// PathNode current = path;
		while (path != null) {
			int i = path.col;
			int j = path.row;
			newPicture.set(i, j, white);
			path = path.next;
		}
		newPicture.show();
	}

	public Picture getStitchedPicture(PathNode path) {
		Picture pic = new Picture(left.width(), left.height());

		PathNode current = path;
		int colPath = 0;
		int rowPath = 0;
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		while (current != null) {
			colPath = current.col;
			rowPath = current.row;
			map.put(rowPath, colPath);
			current = current.next;
		}

		for (int i = 0; i < left.height(); i++) {
			int pathMarker = map.get(i);
			for (int j = 0; j < left.width(); j++) {
				if (j < pathMarker) {
					Color leftColor = left.get(j, i);
					pic.set(j, i, leftColor);
				} else {
					Color rightColor = right.get(j, i);
					pic.set(j, i, rightColor);
				}
			}
		}

		return pic;
	}

}