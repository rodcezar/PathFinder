package br.unirio.bsi.pm.optimalpathfinder.model;

/**
 * Main controller of the application. Loads a track and the user interface on the screen.
 * 
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.unirio.bsi.pm.optimalpathfinder.xml.TrackParser;

public class MapRenderer extends Component implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String trackCodes[] = { "be", "br", "hu", "it", "jp", "uk" }; // List
																						// of
																						// available
																						// tracks

	private int trackIndex = 0; // Index of the track selection ComboBox. This
								// determines the track to be displayed
	private BufferedImage originalFrame, finalFrame, gameOver, tempBuffer;
	private int w, h;
	private Car car;
	private Track track;
	private PathFinder pathFinder;
	private JButton jbnPFButton;
	private JButton jbnButtons[];
	private JPanel jplButtons;
	private JPanel jplPFButton;
	private JComboBox<String> jcbformats, choices;
	private Dimension screenSize;
	private int screenWidth;
	private int screenHeight;
	private boolean isGameOver, pathButtonPressed = false;

	protected JLabel movesCounter;

	public MapRenderer() throws Exception {

		createButtonPanel();
		createTrackOptionComboBox();
		createSaveFileComboBox();

		loadGameOverImage();

		loadMap();

	}

	/**
	 * Create the numeric keyboard to receive the player input
	 */

	private void createButtonPanel() {

		movesCounter = new JLabel("");

		jbnButtons = new JButton[9];
		jplButtons = new JPanel();
		jplButtons.setLayout(new GridLayout(3, 3, 2, 2));

		for (int i = 0; i < 9; i++) {
			jbnButtons[i] = new JButton(String.valueOf(i + 1));
			jplButtons.add(jbnButtons[i]);
		}

		for (int i = 0; i < jbnButtons.length; i++) {
			jbnButtons[i].addActionListener(this);
		}

		createPathFinderButtonPanel();
	}

	/**
	 * Create path finder button
	 */

	private void createPathFinderButtonPanel() {

		jbnPFButton = new JButton();
		jplPFButton = new JPanel();
		jplPFButton.setLayout(new GridLayout(1, 1, 2, 2));

		jbnPFButton = new JButton("!");
		jplPFButton.add(jbnPFButton);
		jbnPFButton.addActionListener(this);

	}

	/**
	 * Create a the ComboBox with the available tracks, allowing selection.
	 */

	private void createTrackOptionComboBox() {

		choices = new JComboBox<String>(this.getTrackCodes());
		choices.setActionCommand("SetTrack");
		choices.addActionListener(this);

	}

	/**
	 * Create a the ComboBox with the available image formats, allowing saving.
	 */

	private void createSaveFileComboBox() {

		jcbformats = new JComboBox<String>(this.getFormats());
		jcbformats.setActionCommand("Formats");
		jcbformats.addActionListener(this);

	}

	/**
	 * Loads the game over image to an image buffer
	 */

	private void loadGameOverImage() {

		try {
			gameOver = ImageIO.read(new File("data/tracks/be/gameover.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads a track to the screen, using the trackIndex to determine which
	 * track should be loaded
	 * 
	 * @throws Exception
	 */

	public void loadMap() throws Exception {

		try {

			String[] trackCodes = getTrackCodes();

			track = new TrackParser().loadFromFile(trackCodes[trackIndex]);
			car = new Car();
			pathFinder = new PathFinder(trackCodes[trackIndex], 200);

			Coordinate2D tileSize = new Coordinate2D();
			tileSize.setX(track.getFirstRowHeight());
			tileSize.setY(track.getFirstColumnWidth());

			car.addPosition(track.getStartPoint());

			originalFrame = ImageIO.read(new File(track.getPNGPath()));

			w = originalFrame.getWidth(null);
			h = originalFrame.getHeight(null);

			screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			screenWidth = (int) screenSize.getWidth();
			screenHeight = (int) screenSize.getHeight();

			if (originalFrame.getType() != BufferedImage.TYPE_INT_RGB) {
				// Draw the first screen with with player input = 5 (no
				// movement)
				drawNewFrame(5);
			}

		} catch (IOException e) {
			System.out.println("Image could not be read");
			System.exit(1);
		}

	}

	/**
	 * Render a new frame
	 */
	public void drawNewFrame(int playerInput) {

		// Create a temporary buffered image with the original image size
		tempBuffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

		Graphics big = tempBuffer.getGraphics();
		big.drawImage(originalFrame, 0, 0, null);

		Coordinate2D carPosition = transformToPNG(car.getCurrentPosition());

		big.clearRect(carPosition.getY() - 10, carPosition.getX() - 10, 20, 20);

		drawGrid();
		drawMoves(playerInput);

		movesCounter.setText(" Moves: " + String.valueOf(car.getNumberOfMoves()));

		finalFrame = drawScaledFrame(tempBuffer);

	}

	/**
	 * Scale the bufferedImage to fit the screen size
	 */
	public BufferedImage drawScaledFrame(BufferedImage bi) {

		BufferedImage biScaled = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);

		Graphics bigScaled = biScaled.getGraphics();
		bigScaled.drawImage(bi, 0, 0, screenWidth, screenHeight, 0, 0, w, h, null);

		return biScaled;

	}

	/**
	 * Transform a grid position to a coordinate position to fit the PNG image
	 */

	public Coordinate2D transformToPNG(Coordinate2D gridCoordinate) {

		Coordinate2D PNGCoordinate = new Coordinate2D();

		PNGCoordinate.setX(gridCoordinate.getX() * track.getFirstRowHeight() + track.getBorderHeight());
		PNGCoordinate.setY(gridCoordinate.getY() * track.getFirstColumnWidth() + track.getBorderWidth());

		return PNGCoordinate;
	}

	/**
	 * Transform as array of grid positions to an array of PNG coordinates
	 */

	public ArrayList<Coordinate2D> transformArrayToPNG(ArrayList<Coordinate2D> carPositions) {

		ArrayList<Coordinate2D> newArray = new ArrayList<Coordinate2D>();

		for (Coordinate2D position : carPositions) {
			newArray.add(transformToPNG(position));
		}

		return newArray;
	}

	/**
	 * Draw a grid of squares on the screen indicating valid (GREY) and invalid
	 * (BLACK) squares
	 */

	public void drawGrid() {

		Graphics big = tempBuffer.getGraphics();
		big.drawImage(tempBuffer, 0, 0, null);

		big.setColor(Color.BLUE);

		int tileSizeX = track.getFirstRowHeight();
		int tileSizeY = track.getFirstColumnWidth();

		for (int i = 0; i < track.getRows(); i++) {

			for (int j = 0; j < track.getColumns(); j++) {
				if (track.getTrackPoint(i, j).isValid())
					big.setColor(Color.GRAY);
				else
					big.setColor(Color.BLACK);

				// If the trackPoint is a checkpoint, draw its number on the
				// screen
				if (track.getTrackPoint(i, j).isCheckpoint()) {
					big.setColor(Color.RED);
					big.setFont(new Font("CourierNew", Font.PLAIN, 36));
					big.drawString(Integer.toString(track.getTrackPoint(i, j).getCheckpointIndex() - 1),
							j * tileSizeY + track.getBorderWidth() - 4, i * tileSizeX + track.getBorderHeight() - 4);

				} else {

					big.fillRect(j * tileSizeY + track.getBorderWidth() - 4,
							i * tileSizeX + track.getBorderHeight() - 4, 8, 8);
				}
			}

		}
	}

	/**
	 * Draw the 2D matrix of blue circles indicating the next possible moves
	 */

	public void drawNextMoveGrid(Coordinate2D nextMove) {

		Graphics big = tempBuffer.getGraphics();
		big.drawImage(tempBuffer, 0, 0, null);

		big.setColor(Color.BLUE);

		int tileSizeX = track.getFirstRowHeight();
		int tileSizeY = track.getFirstColumnWidth();

		for (int i = -1; i <= 1; i++) {

			for (int j = -1; j <= 1; j++) {

				big.drawOval(nextMove.getY() + (j * tileSizeY) - 16, nextMove.getX() + (i * tileSizeX) - 16, 32, 32);
			}
		}

	}

	/**
	 * @return a list of available track codes
	 */
	public String[] getTrackCodes() {
		return trackCodes;
	}

	/**
	 * Set the track index according to what was selected on the track ComboBox
	 */
	void setTrackIndex(int i) {
		trackIndex = i;
	}

	/**
	 * Overrides: getPreferredSize() in java.awt.Component
	 * 
	 * This sets the size of the area where the PNG will be drawn
	 * 
	 */
	public Dimension getPreferredSize() {
		return new Dimension(w, h);
	}

	/**
	 * Overrides: paint(...) in java.awt.ComponentComponent
	 * 
	 * This draws the finalFrame on the screen
	 * 
	 */
	public void paint(Graphics g) {

		g.drawImage(finalFrame, 0, 0, null);

	}

	/**
	 * Draw, on the tempBuffer, the sequence of positions stored in the car
	 * object
	 */
	public boolean drawMoves(int playerInput) {

		Graphics big = tempBuffer.getGraphics();
		big.drawImage(tempBuffer, 0, 0, null);
		big.setColor(Color.RED);

		if (playerInput > 0) {
			car.setDirection(playerInput);
			car.moveNext();
		} else {
			car.brake();
		}

		ArrayList<Coordinate2D> carPositions = new ArrayList<Coordinate2D>();
		carPositions = transformArrayToPNG(car.getPositions());

		for (int i = 1; i < carPositions.size(); i++) {

			big.setColor(Color.RED);
			// Draw a red the line between the last and current car positions
			big.drawLine(carPositions.get(i - 1).getY(), carPositions.get(i - 1).getX(), carPositions.get(i).getY(),
					carPositions.get(i).getX());
			// Draw a black square representing the car
			big.setColor(Color.BLACK);
			big.fillRect(carPositions.get(i).getY() - 10, carPositions.get(i).getX() - 10, 20, 20);

			// Draw the player moves
			if (pathButtonPressed) {
				big.setColor(Color.BLUE);
				big.setFont(new Font("CourierNew", Font.PLAIN, 36));
				big.drawString(Integer.toString(car.getMoveAtIndex(i)), carPositions.get(i).getY() - 10,
						carPositions.get(i).getX() - 10);
			}

		}

		if (colisionCheck(big))
			gameOver(big);

		if (!isGameOver)
			drawNextMoveGrid(transformToPNG(car.getNextMove()));

		finalFrame = drawScaledFrame(tempBuffer);

		return isGameOver;
	}

	/**
	 * ColisionCheck - return true if the car moved into an invalid position
	 */
	private boolean colisionCheck(Graphics big) {

		boolean colision = false;

		Coordinate2D previousGridPosition = new Coordinate2D();
		previousGridPosition = car.getPositionAtIndex(car.getCurrentPositionIndex() - 2);

		Coordinate2D gridPosition = new Coordinate2D();
		gridPosition = car.getCurrentPosition();

		// Off boundaries check
		if ((gridPosition.getX() < 0) || (gridPosition.getY() < 0) || (gridPosition.getX() > track.getRows())
				|| (gridPosition.getY() > track.getColumns())) {

			colision = true;

		} else {
			// if it's within the map boundaries, check if the move was valid

			colision = fullPathCollision(previousGridPosition.getX(), previousGridPosition.getY(), gridPosition.getX(),
					gridPosition.getY(), big);

		}

		return colision;
	}

	/**
	 * Use the Bresenham line drawing algorithm to traverse the track.map
	 * checking if any invalid points are on the car's path
	 * 
	 * http://tech-algorithm.com/articles/drawing-line-using-bresenham-algorithm/
	 */
	private boolean fullPathCollision(int x, int y, int x2, int y2, Graphics big) {

		boolean colision = false;

		int deltaX = x2 - x;
		int deltaY = y2 - y;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
		if (deltaX < 0)
			dx1 = -1;
		else if (deltaX > 0)
			dx1 = 1;
		if (deltaY < 0)
			dy1 = -1;
		else if (deltaY > 0)
			dy1 = 1;
		if (deltaX < 0)
			dx2 = -1;
		else if (deltaX > 0)
			dx2 = 1;
		int longest = Math.abs(deltaX);
		int shortest = Math.abs(deltaY);
		if (!(longest > shortest)) {
			longest = Math.abs(deltaY);
			shortest = Math.abs(deltaX);
			if (deltaY < 0)
				dy2 = -1;
			else if (deltaY > 0)
				dy2 = 1;
			dx2 = 0;
		}
		int numerator = longest >> 1;
		for (int i = 0; i <= longest; i++) {
			if (!track.map[x][y].isValid()) {
				big.setColor(Color.RED);
				big.fillRect(y * track.getFirstColumnWidth() + track.getBorderWidth(),
						x * track.getFirstRowHeight() + track.getBorderHeight(), 20, 20);
				colision = true;
			}
			numerator += shortest;
			if (!(numerator < longest)) {
				numerator -= longest;
				x += dx1;
				y += dy1;
			} else {
				x += dx2;
				y += dy2;
			}
		}

		return colision;

	}

	/**
	 * This method is called from the "Find path" button. It loads the car
	 * position stack with all the moves needed to get to the next check point
	 */
	public void findPath() {

		ArrayList<Coordinate2D> path = new ArrayList<Coordinate2D>();

		// Get a path from the pathFinder, giving the car position as the start
		// point, and the current check to be reached
		path = pathFinder.calculatePath(car.getCurrentPosition(), track.getNextCheckPoint());

		// Load this path into the car position stack
		for (int i = path.size() - 1; i >= 0; i--) {
			car.addPosition(path.get(i));
		}

		// Convert the pathfinder output to player moves

		PathTransformer pt = new PathTransformer(car.getPositions().size());
		pathButtonPressed = true;
		car.setMoves(pt.coordinatesToMoves(car.getPositions()));

		// Path Optimizer

		/*
		 * PathOptimizer po = new PathOptimizer(car.getPositions(),
		 * car.getMoves()); ArrayList<Coordinate2D> optimizedPath =
		 * po.optimize();
		 * 
		 * car.reset();
		 * 
		 * for (int i = 0; i < optimizedPath.size() - 1; i++) {
		 * car.addPosition(optimizedPath.get(i)); }
		 */

	}

	/**
	 * Called when the car collides. Draws the gameOver PNG on the screen and
	 * set the gameOver flag to true
	 */

	private void gameOver(Graphics big) {

		int w1 = gameOver.getWidth(null);
		int h1 = gameOver.getHeight(null);

		big.drawImage(gameOver, (w / 2 - w1 / 2), (h / 2 - h1 / 2), null);
		isGameOver = true;

	}

	/**
	 * @return the formats sorted alphabetically and in lower case
	 */
	public String[] getFormats() {
		String[] formats = ImageIO.getWriterFormatNames();
		TreeSet<String> formatSet = new TreeSet<String>();
		for (String s : formats) {
			formatSet.add(s.toLowerCase());
		}
		return formatSet.toArray(new String[0]);
	}

	public JPanel getJbuttons() {

		return jplButtons;

	}

	public JPanel getPFJbutton() {

		return jplPFButton;

	}

	public JComboBox<String> getChoices() {

		return choices;

	}

	public JComboBox<String> getJcbformats() {

		return jcbformats;

	}

	public JLabel getMovesLabel() {

		return movesCounter;

	}

	/**
	 * Button listeners
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == jbnPFButton) {
			findPath();
			drawNewFrame(-1);
			repaint();
		}

		// Search for the button pressed until end of array or key found
		if (!isGameOver) {

			for (int i = 0; i < jbnButtons.length; i++) {
				if (e.getSource() == jbnButtons[i]) {
					drawNewFrame(i + 1); // because button 1 has index 0
					repaint();
				}
			}
		}

		if ((e.getSource() == jcbformats) || (e.getSource() == choices)) {

			@SuppressWarnings("rawtypes")
			JComboBox cb = (JComboBox) e.getSource();

			if (cb.getActionCommand().equals("SetTrack")) {
				setTrackIndex(cb.getSelectedIndex());
				try {
					this.loadMap();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				isGameOver = false;
				repaint();
			}

			if (cb.getActionCommand().equals("Formats")) {

				/*
				 * Save the filtered image in the selected format. The selected
				 * item will be the name of the format to use
				 */
				String format = (String) cb.getSelectedItem();
				/*
				 * Use the format name to initialise the file suffix. Format
				 * names typically correspond to suffixes
				 */
				File saveFile = new File("savedimage." + format);
				JFileChooser chooser = new JFileChooser();
				chooser.setSelectedFile(saveFile);
				int rval = chooser.showSaveDialog(cb);
				if (rval == JFileChooser.APPROVE_OPTION) {
					saveFile = chooser.getSelectedFile();
					/*
					 * Write the filtered image in the selected format, to the
					 * file chosen by the user.
					 */
					try {
						ImageIO.write(finalFrame, format, saveFile);
					} catch (IOException ex) {
					}
				}

			}
		}
	}

}
