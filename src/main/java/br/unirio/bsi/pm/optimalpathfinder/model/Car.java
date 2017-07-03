package br.unirio.bsi.pm.optimalpathfinder.model;

/**
 * Class that represents the moving unit on the map. 
 * 
 * @author Rodrigo
 */

import java.util.ArrayList;

import br.unirio.bsi.pm.optimalpathfinder.model.Coordinate2D;

public class Car {

	private ArrayList<Coordinate2D> positionStack; // keeps a history of car
													// positions
	private ArrayList<Coordinate2D> speedStack; // keeps a history of the speed
												// at a given position index.

	private int[] playerMoves; // Keeps a history of numpad moves

	public Car() {

		positionStack = new ArrayList<Coordinate2D>();
		speedStack = new ArrayList<Coordinate2D>();
		playerMoves = new int[1000];
	}

	public void addPosition(Coordinate2D position) {
		this.positionStack.add(position);
	}

	public void addSpeed(Coordinate2D speed) {
		this.speedStack.add(speed);
	}

	public Coordinate2D getCurrentPosition() {
		return positionStack.get(this.positionStack.size() - 1);
	}

	public Coordinate2D getPositionAtIndex(int index) {
		return positionStack.get(index);
	}

	/**
	 * Sum the current speed to the current position and add a new position of
	 * the result of this sum.
	 */

	public void moveNext() {
		int currentIndex = this.positionStack.size() - 1;
		int currentSpeedIndex = this.speedStack.size() - 1;

		Coordinate2D nextPosition = new Coordinate2D();

		int x1 = this.positionStack.get(currentIndex).getX();
		int x2 = this.speedStack.get(currentSpeedIndex).getX();
		int y1 = this.positionStack.get(currentIndex).getY();
		int y2 = this.speedStack.get(currentSpeedIndex).getY();

		nextPosition.setX(x1 + x2);
		nextPosition.setY(y1 + y2);

		addPosition(nextPosition);
	}

	public void reset() {
		this.positionStack.clear();
		this.speedStack.clear();
	}

	/**
	 * Convert 1 to 9 numeric input into coordinate2D
	 */
	public void setDirection(int move) {
		int currentIndex = this.speedStack.size() - 1;
		Coordinate2D speed = new Coordinate2D();

		speed.setX(((move - 1) / 3) - 1);
		speed.setY(((move + 2) % 3) - 1);

		if (currentIndex < 0) {
			addSpeed(speed);
		} else {
			speed.sumCoordinate(this.speedStack.get(currentIndex));
			addSpeed(speed);

		}
	}

	/**
	 * Returns the car's next position considering no speed change. This is used
	 * to draw the next move circle grid. Note that this method doesn't add a
	 * new position to the car
	 */

	public Coordinate2D getNextMove() {
		int currentIndex = this.positionStack.size() - 1;
		int currentSpeedIndex = this.speedStack.size() - 1;

		Coordinate2D nextMove = new Coordinate2D();

		int x1 = this.positionStack.get(currentIndex).getX();
		int x2 = this.speedStack.get(currentSpeedIndex).getX();
		int y1 = this.positionStack.get(currentIndex).getY();
		int y2 = this.speedStack.get(currentSpeedIndex).getY();

		nextMove.setX(x1 + x2);
		nextMove.setY(y1 + y2);

		return nextMove;
	}

	public int getCurrentPositionIndex() {
		return this.positionStack.size();
	}

	public ArrayList<Coordinate2D> getPositions() {
		return this.positionStack;
	}

	/**
	 * Set the car's current speed to zero. This is used when the car is moved
	 * with path finder.
	 */
	public void brake() {
		int speedIndex = this.speedStack.size() - 1;
		this.speedStack.get(speedIndex).setX(0);
		this.speedStack.get(speedIndex).setY(0);
	}

	public void setMoves(int[] moves) {

		playerMoves = moves;

	}

	public int[] getMoves() {

		return playerMoves;

	}

	public void setMoveAtIndex(int index, int move) {

		playerMoves[index] = move;

	}
	
	public int getMoveAtIndex(int index) {

		return playerMoves[index];

	}
	
	public int getNumberOfMoves() {

		return this.getPositions().size() - 2;

	}

}
