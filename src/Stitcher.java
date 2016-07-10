

/*************************************************************************
 *  YOU DO NOT NEED TO MODIFY THIS FILE
 *
 *  Compilation:  javac Stitcher.java
 *  Execution:    java Stitcher img1.png img2.png xoffset yoffset [ ref.img ]
 *  Dependencies: ImageSeam.java CostMatrix.java Picture.java
 *
 *  Stitch img1 and img2 together using a dynamic program to compute
 *  the optimal seam between them.  Display the result, or show where
 *  it differs from a reference image if one is specified.
 *
 *  % java Stitcher sea1.png sea2.png 318 -7
 *  % java Stitcher sea1.png sea2.png 318 -7 sea-stitched.png
 *
 *************************************************************************/

import java.awt.Color;

public class Stitcher {
	public static void main(String[] args) {
		Picture img1 = new Picture(args[0]);
		int w1 = img1.width();
		int h1 = img1.height();

		Picture img2 = new Picture(args[1]);
		int w2 = img2.width();
		int h2 = img2.height();

		int offsetx = 0, offsety = 0;
		if (args.length > 3) {
			offsetx = Integer.parseInt(args[2]);
			offsety = Integer.parseInt(args[3]);
		}

		int x1 = 0, x2 = 0, y1 = 0, y2 = 0, w = 0, h = 0;

		if (offsetx >= 0) {
			x1 = offsetx;
			w = Math.min(w1 - offsetx, w2);
		} else {
			x2 = -offsetx;
			w = Math.min(w1, w2 + offsetx);
		}

		if (offsety >= 0) {
			y1 = offsety;
			h = Math.min(h1 - offsety, h2);
		} else {
			y2 = -offsety;
			h = Math.min(h1, h2 + offsety);
		}

		Picture a = new Picture(w, h);
		Picture b = new Picture(w, h);

		for (int row = 0; row < h; row++) {
			for (int col = 0; col < w; col++) {
				a.set(col, row, img1.get(col + x1, row + y1));
				b.set(col, row, img2.get(col + x2, row + y2));
			}
		}

		if (offsetx >= 0) {
			flipHorizontal(a);
			flipHorizontal(b);
		}

		if (offsety <= 0) {
			flipVertical(a);
			flipVertical(b);
		}

		ImageSeam seam = new ImageSeam(b, a);
		PathNode p = CostMatrix.costIterative(seam);
		System.out.println("Stitching cost is " + p.cost);
		seam.showSeam(p);
		Picture stitched = seam.getStitchedPicture(p);

		if (offsetx >= 0) {
			flipHorizontal(a);
			flipHorizontal(b);
			flipHorizontal(stitched);
		}

		if (offsety <= 0) {
			flipVertical(a);
			flipVertical(b);
			flipVertical(stitched);
		}

		int out_w = w1 + w2 - w;
		int out_h = h1 + h2 - h;
		Picture out = new Picture(out_w, out_h);

		for (int row = 0; row < h1; row++)
			for (int col = 0; col < w1; col++)
				out.set(col + x2, row + y2, img1.get(col, row));

		for (int row = 0; row < h2; row++)
			for (int col = 0; col < w2; col++)
				out.set(col + x1, row + y1, img2.get(col, row));

		for (int row = 0; row < h; row++)
			for (int col = 0; col < w; col++)
				out.set(col + x1 + x2, row + y1 + y2, stitched.get(col, row));

		if (args.length > 4) {
			Picture ground_truth = new Picture(args[4]);
			if (ground_truth.height() != out_h || ground_truth.width() != out_w) {
				System.out.println("The stitched image's dimensions (" + out_w
						+ " x " + out_h
						+ ") differ from the ground truth image " + args[4]
						+ "'s dimensions (" + ground_truth.width() + " x "
						+ ground_truth.height() + ").");
				for (int row = 0; row < out_h; row++)
					for (int col = 0; col < out_w; col++)
						out.set(col, row, Color.RED);
			} else {
				for (int row = 0; row < out_h; row++)
					for (int col = 0; col < out_w; col++)
						if (out.get(col, row)
								.equals(ground_truth.get(col, row)))
							out.set(col, row, Color.WHITE);
						else
							out.set(col, row, Color.BLACK);
			}
		}

		out.show();
	}

	private static void flipHorizontal(Picture img) {
		int w = img.width();
		for (int row = 0; row < img.height(); row++) {
			for (int col = 0; col < w / 2; col++) {
				Color c = img.get(col, row);
				img.set(col, row, img.get(w - col - 1, row));
				img.set(w - col - 1, row, c);
			}
		}
	}

	private static void flipVertical(Picture img) {
		int h = img.height();
		for (int row = 0; row < h / 2; row++) {
			for (int col = 0; col < img.width(); col++) {
				Color c = img.get(col, row);
				img.set(col, row, img.get(col, h - row - 1));
				img.set(col, h - row - 1, c);
			}
		}
	}
}
