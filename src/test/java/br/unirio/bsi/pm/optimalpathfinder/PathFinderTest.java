package br.unirio.bsi.pm.optimalpathfinder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import br.unirio.bsi.pm.optimalpathfinder.model.Car;
import br.unirio.bsi.pm.optimalpathfinder.model.Coordinate2D;
import br.unirio.bsi.pm.optimalpathfinder.model.PathFinder;
import br.unirio.bsi.pm.optimalpathfinder.model.PathTransformer;
import br.unirio.bsi.pm.optimalpathfinder.model.Track;
import br.unirio.bsi.pm.optimalpathfinder.xml.TrackParser;

public class PathFinderTest {

	@Test
	public void testLoadTrackBE() throws Exception {
		Track track = new TrackParser().loadFromFile("be");
		assertEquals("data/tracks/be/track.png", track.getPNGPath());
		assertEquals(63, track.getColumns());
		assertEquals(39, track.getRows());
		assertEquals(49, track.getBorderWidth());
		assertEquals(5, track.getNumberOfCheckPoints());

	}

	public void testLoadTrackBR() throws Exception {
		Track track = new TrackParser().loadFromFile("be");
		assertEquals("data/tracks/br/track.png", track.getPNGPath());
		assertEquals(63, track.getColumns());
		assertEquals(39, track.getRows());
		assertEquals(49, track.getBorderWidth());
		assertEquals(3, track.getNumberOfCheckPoints());

	}

	@Test
	public void testPathFinderBE() throws Exception {

		Track track = new TrackParser().loadFromFile("be");

		Car car = new Car();
		car.addPosition(track.getStartPoint());

		PathFinder pathFinder = new PathFinder("be", 200);
		ArrayList<Coordinate2D> path = new ArrayList<Coordinate2D>();

		for (int i = 0; i < track.getNumberOfCheckPoints(); i++) {
			path = pathFinder.calculatePath(car.getCurrentPosition(), track.getNextCheckPoint());

			// Load this path into the car position stack
			for (int j = path.size() - 1; j >= 0; j--) {
				car.addPosition(path.get(j));
			}
		}

		assertEquals(true, car.getNumberOfMoves() < 200);
		assertEquals(153, car.getNumberOfMoves());

	}

	@Test
	public void testPathFinderBR() throws Exception {

		Track track = new TrackParser().loadFromFile("br");

		Car car = new Car();
		car.addPosition(track.getStartPoint());

		PathFinder pathFinder = new PathFinder("br", 200);
		ArrayList<Coordinate2D> path = new ArrayList<Coordinate2D>();

		for (int i = 0; i < track.getNumberOfCheckPoints(); i++) {
			path = pathFinder.calculatePath(car.getCurrentPosition(), track.getNextCheckPoint());

			// Load this path into the car position stack
			for (int j = path.size() - 1; j >= 0; j--) {
				car.addPosition(path.get(j));
			}
		}

		assertEquals(true, car.getNumberOfMoves() < 200);
		assertEquals(187, car.getNumberOfMoves());

	}

	@Test
	public void testPathTransformer() throws Exception {

		Track track = new TrackParser().loadFromFile("be");

		PathFinder pathFinder = new PathFinder("be", 200);

		// Reminder: The pathFinder returns a path in reverse order!
		ArrayList<Coordinate2D> pathBackwards = new ArrayList<Coordinate2D>();
		ArrayList<Coordinate2D> pathOnwards = new ArrayList<Coordinate2D>();

		// Loads the reverse path on pathBackwards
		pathBackwards = pathFinder.calculatePath(track.getCheckPoint(0), track.getCheckPoint(1));

		// Reorder it on the pathOnwards
		for (int j = pathBackwards.size() - 1; j >= 0; j--) {
			pathOnwards.add(pathBackwards.get(j));
		}

		ArrayList<Integer> moves = new ArrayList<Integer>();
		PathTransformer pt = new PathTransformer();
		moves = pt.coordinatesToMoves(pathOnwards);

		for (int i = 0; i < moves.size(); i++) {
			assertEquals(true, ((moves.get(i) < 10) && (moves.get(i) > 0)));
			System.out.println(moves.get(i));
		}

		assertEquals(6, moves.get(0).intValue());
		assertEquals(2, moves.get(1).intValue());
		assertEquals(5, moves.get(2).intValue());
		assertEquals(8, moves.get(17).intValue());
	}

	@Test
	// expected to fail
	public void testLoadNonExistingTrack() throws Exception {

		Track track = new TrackParser().loadFromFile("xx");
	}

}
