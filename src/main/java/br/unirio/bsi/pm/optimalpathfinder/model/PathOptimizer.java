package br.unirio.bsi.pm.optimalpathfinder.model;

import java.util.ArrayList;

public class PathOptimizer {

	private ArrayList<Coordinate2D> coordinates;
	private int[] moves;

	public PathOptimizer(ArrayList<Coordinate2D> coordinates, int[] moves) {
		this.coordinates = coordinates;
		this.moves = moves;
	}

	public ArrayList<Coordinate2D> optimize() {

		int i = 0;
		int startIndex = 0;
		int endIndex = 0;
		int delta = 0;
		boolean recording = false;

		while (i < moves.length) {

			if ((moves[i] == 5) && (!recording)) {

				startIndex = i;
				recording = true;
			}

			if ((moves[i] != 5) && (recording)) {

				endIndex = i;
				delta = endIndex - startIndex;

				if (delta > 4) {

					removeNodes(startIndex, endIndex);

				}

				recording = false;
			}

			i++;
		}

		return coordinates;
	}

	public void removeNodes(int begin, int end) {

		int delta = end - begin;
		int divisionResult = delta;
		int factorX = 0, factorY = 1;
		int j = 0, i = 0;

		while (divisionResult > 3) {
			divisionResult = divisionResult / 2;
			factorX++;
		}

		for (i = (begin + 2); i < (end - 2); i += factorY) {

			coordinates.remove(i);

		}

	}

}
