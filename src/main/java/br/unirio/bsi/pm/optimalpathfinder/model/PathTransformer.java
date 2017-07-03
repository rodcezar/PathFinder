package br.unirio.bsi.pm.optimalpathfinder.model;

import java.util.ArrayList;

public class PathTransformer {

	ArrayList<Coordinate2D> coordinatesPath;
	int[] movesRaw;
	int[] movesAdjusted;

	public PathTransformer(int size) {

		coordinatesPath = new ArrayList<Coordinate2D>();
		movesRaw = new int[size];
		movesAdjusted = new int[size];

	}

	public int[] coordinatesToMoves(ArrayList<Coordinate2D> path) {

		for (int i = 0; i < path.size() - 2; i++) {

			movesRaw[i] = transform(path.get(i), path.get(i + 1));
		}

		adjust();

		return movesAdjusted;

	}

	/**
	 * Translates the movement between 2 coordinates to a numpad input,
	 * ***ignoring speed***
	 * 
	 * @param c1
	 *            - Coordinate 1 (from)
	 * @param c2
	 *            - Coordinate 2 (to)
	 * @return the corresponding player input
	 */

	private int transform(Coordinate2D c1, Coordinate2D c2) {

		int moveX = c2.getX() - c1.getX();
		int moveY = c2.getY() - c1.getY();

		return (moveY + 2) + (moveX + 1) * 3;

	}

	/**
	 * From the movesRaw array, creates the movesAdjusted array, with the speed
	 * variant
	 */

	public void adjust() {

		movesAdjusted[0] = movesRaw[0];

		int detour = movesRaw[0];

		for (int i = 1; i < movesRaw.length; i++) {

			if (movesRaw[i] == movesRaw[i - 1]) {

				movesAdjusted[i] = 5;

			} else {

				movesAdjusted[i] = movesRaw[i] + (detour - 5) * (-1);
				detour = movesRaw[i];

			}

		}

	}

}
