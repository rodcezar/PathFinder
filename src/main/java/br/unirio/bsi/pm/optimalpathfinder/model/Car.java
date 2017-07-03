package br.unirio.bsi.pm.optimalpathfinder.model;

/**
 * Class that represents the moving unit on the map. 
 * 
 * 
 * @author Rodrigo
 */

import java.util.ArrayList;

import br.unirio.bsi.pm.optimalpathfinder.model.Coordinate2D;

public class Car {

	/**
	 * keeps a history of car positions (Coordinates)
	 */
	private ArrayList<Coordinate2D> positionStack;

	/**
	 * keeps a history of the speed state at a given position index
	 */
	private ArrayList<Coordinate2D> speedStack;

	/**
	 * Keeps a history of numpad moves
	 */
	private ArrayList<Integer> playerMoves;

	public Car() {

		positionStack = new ArrayList<Coordinate2D>();
		speedStack = new ArrayList<Coordinate2D>();
		playerMoves = new ArrayList<Integer>();
	}

	/**
	 * add a position to the car positionStack
	 * 
	 */

	public void addPosition(Coordinate2D position) {
		this.positionStack.add(position);
	}

	/**
	 * add a speed state to the car positionStack
	 * 
	 */
	public void addSpeed(Coordinate2D speed) {
		this.speedStack.add(speed);
	}

	/**
	 * Get the car current position
	 */
	public Coordinate2D getCurrentPosition() {
		return positionStack.get(this.positionStack.size() - 1);
	}

	/**
	 * Get the car position (a coordinate) at a given array index
	 */
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

	/**
	 * Clear history
	 */
	public void reset() {
		this.positionStack.clear();
		this.speedStack.clear();
		this.playerMoves.clear();
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
	 * to draw the next move blue circles grid. Note that, unlike the
	 * moveNext(), this method doesn't add a new position to the car
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

	/**
	 * @return the car coordinate (position) at a given index in the
	 *         positionStack
	 * 
	 */
	public int getCurrentPositionIndex() {
		return this.positionStack.size();
	}

	/**
	 * 
	 * @return an array of all car positions
	 */

	public ArrayList<Coordinate2D> getPositions() {
		return this.positionStack;
	}

	/**
	 * Set the car's current speed to zero. This is used when the car is moved
	 * with the pathFinder.
	 */
	public void brake() {
		int speedIndex = this.speedStack.size() - 1;
		this.speedStack.get(speedIndex).setX(0);
		this.speedStack.get(speedIndex).setY(0);
	}

	/**
	 * Loads the playerMoves arrayList with a list of player inputs. This is
	 * used when the car is moved with the pathFinder
	 */
	public void setMoves(ArrayList<Integer> moves) {

		playerMoves = moves;

	}

	/**
	 * 
	 * @return the list of playerMoves required to produce the car's current
	 *         path
	 */
	public ArrayList<Integer> getMoves() {

		return playerMoves;

	}

	/**
	 * Add a numeric keyboard input to the playerMoves ArrayList, at a given
	 * index
	 * 
	 * @param index
	 * @param move
	 */
	public void setMoveAtIndex(int index, Integer move) {

		playerMoves.add(index, move);

	}

	/**
	 * Add a move (numeric keyboard input) to the end of playerMoves ArrayList
	 * 
	 * @param move
	 */
	public void addMove(Integer move) {

		playerMoves.add(move);

	}

	/**
	 * Return the move done at a given index
	 * @param index
	 * @return
	 */
	public int getMoveAtIndex(int index) {

		return playerMoves.get(index);

	}
	/**
	 * 
	 * @return the number of moves the car did up to the moment
	 */
	public int getNumberOfMoves() {

		return this.getPositions().size() - 2;

	}

}
