package br.unirio.bsi.pm.optimalpathfinder.model;

import java.util.ArrayList;

/**
 * Reads an ArrayList of coordinates and returns and ArrayList of moves needed
 * to generate the given path
 * 
 * @param path
 * @return
 */
public class PathTransformer {

	ArrayList<Coordinate2D> coordinatesPath;
	ArrayList<Integer> movesRaw;
	ArrayList<Integer> movesAdjusted;

	public PathTransformer() {
		coordinatesPath = new ArrayList<Coordinate2D>();
		movesRaw = new ArrayList<Integer>();
		movesAdjusted = new ArrayList<Integer>();
	}

	/**
	 * Reads an ArrayList of coordinates and returns and ArrayList of moves
	 * needed to generate the given path
	 * 
	 * @param path
	 * @return
	 */
	public ArrayList<Integer> coordinatesToMoves(ArrayList<Coordinate2D> path) {
		for (int i = 0; i < path.size() - 1; i++) {
			movesRaw.add(transform(path.get(i), path.get(i + 1)));
		}
		adjust();
		return movesAdjusted;
	}

	/**
	 * Translates the movement between 2 coordinates to a numPad input, ignoring
	 * speed
	 * 
	 * @param c1
	 *            Coordinate 1 (from)
	 * @param c2
	 *            Coordinate 2 (to)
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
		movesAdjusted.add(movesRaw.get(0));
		int detour = movesRaw.get(0);
		for (int i = 1; i < movesRaw.size(); i++) {
			if (movesRaw.get(i) == movesRaw.get(i - 1)) {
				movesAdjusted.add(i, 5);
			} else {
				movesAdjusted.add(i, movesRaw.get(i) + (detour - 5) * (-1));
				detour = movesRaw.get(i);
			}
		}
	}
}
