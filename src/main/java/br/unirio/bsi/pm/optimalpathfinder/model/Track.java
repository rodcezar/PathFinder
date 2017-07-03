package br.unirio.bsi.pm.optimalpathfinder.model;

/**
 * Class representing the model of a track. Data for this class must be loaded from a track XML
 * 
 * @author rodrigo.cezar@uniriotec.br
 */

import java.util.ArrayList;

import br.unirio.bsi.pm.optimalpathfinder.model.Track;
import br.unirio.bsi.pm.optimalpathfinder.model.TrackPoint;

public class Track {

	private String filePath;
	private int rows;
	private int columns;
	private int borderWidth;
	private int borderHeight;
	private int firstColumnWidth;
	private int firstRowHeight;
	private int checkPointIndex = 0; // Index of the current checkpoint to be
										// reached
	private Coordinate2D startPoint;
	private boolean startPointSet;
	private ArrayList<Coordinate2D> checkpoints;

	TrackPoint[][] map;

	public Track() {
		filePath = "";
		rows = 0;
		columns = 0;
		startPointSet = false;
		startPoint = new Coordinate2D();
		checkpoints = new ArrayList<Coordinate2D>();
	}

	/**
	 * Get the PNG file path
	 */
	public String getPNGPath() {
		return filePath;
	}

	/**
	 * Set the PNG file path
	 */
	public void setPNGPath(String name) {
		this.filePath = name;
	}

	/**
	 * Get the number of rows (height) of this track
	 * 
	 * @return
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Set the number of rows (height) of this track
	 * 
	 * @return
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * Get the number of columns (width) of this track
	 * 
	 * @return
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * Set the number of columns (width) of this track
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}

	/**
	 * Get the width of the off map area.
	 */
	public int getBorderWidth() {
		return borderWidth;
	}

	/**
	 * Set the width of the off map area.
	 */
	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}

	public int getFirstRowHeight() {
		return firstRowHeight;
	}

	public void setfirstRowHeight(int firstColumnHeight) {
		this.firstRowHeight = firstColumnHeight;
	}

	public int getFirstColumnWidth() {
		return firstColumnWidth;
	}

	public void setFirstColumnWidth(int firstColumnWidth) {
		this.firstColumnWidth = firstColumnWidth;
	}

	public int getBorderHeight() {
		return borderHeight;
	}

	public void setBorderHeight(int borderHeight) {
		this.borderHeight = borderHeight;
	}

	/**
	 * initialize the TrackPoint Map
	 */
	public void newTrackPointMap(int rows, int columns) {
		this.map = new TrackPoint[rows][columns];
		TrackPoint Point = new TrackPoint(false, false);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				map[i][j] = Point;
			}
		}

	}

	public void setTrackPoint(int x, int y, boolean checkpoint, boolean valid) {
		TrackPoint point = new TrackPoint(checkpoint, valid);
		map[x][y] = point;
	}

	public void setTrackPoint(int x, int y, boolean checkpoint, boolean valid, int checkpointIndex) {
		TrackPoint point = new TrackPoint(checkpoint, valid, checkpointIndex);
		map[x][y] = point;
	}

	public TrackPoint getTrackPoint(int x, int y) {
		return map[x][y];
	}

	public void setStartPoint(int x, int y) {
		startPoint.setX(x);
		startPoint.setY(y);
		;
	}

	public Coordinate2D getStartPoint() {
		return startPoint;
	}

	public boolean isStartPointSet() {
		return startPointSet;
	}

	public void setStartPoint() {
		this.startPointSet = true;
	}

	public void createCheckPoint(int x, int y) {
		Coordinate2D checkPoint = new Coordinate2D();
		checkPoint.setX(x);
		checkPoint.setY(y);
		checkpoints.add(checkPoint);
	}

	public Coordinate2D getCheckPoint(int x) {
		return this.checkpoints.get(x);
	}

	public Coordinate2D getNextCheckPoint() {

		if (checkPointIndex + 1 > this.checkpoints.size() - 1) {
			this.checkPointIndex = 0;
			return this.checkpoints.get(0);
		} else {
			checkPointIndex++;
			return this.checkpoints.get(checkPointIndex);
		}
	}

	public ArrayList<Coordinate2D> getCheckPoints() {
		return this.checkpoints;
	}

	public int getNumberOfCheckPoints() {
		return this.checkpoints.size();
	}
	
}
