package br.unirio.bsi.pm.optimalpathfinder.model;

import java.util.ArrayList;
import java.util.Collections;

import br.unirio.bsi.pm.optimalpathfinder.xml.TrackParser;

/**
 * This class implements the A* path finding algorithm to calculate the best
 * path on a track file
 * 
 * some good sources on the topic:
 * 
 * https://www.raywenderlich.com/4946/introduction-to-a-pathfinding
 * http://www.cokeandcode.com/main/tutorials/path-finding/
 * https://en.wikipedia.org/wiki/A*_search_algorithm
 * 
 * @author rodrigo.cezar@uniriotec.br
 */

public class PathFinder {

	private Track track;
	private int maxSearchDistance;
	private static final int bigNumber = 9999999;

	/** The track map will be loaded in this Node map */
	private Node[][] map;
	/** The set of nodes that have been searched through */
	private ArrayList<Node> closed = new ArrayList<Node>();
	/** The set of nodes that we do not yet consider fully searched */
	private SortedList open = new SortedList();

	/**
	 * Creates a new path finder
	 * 
	 * @param trackCode
	 *            - The code of the track to be loaded
	 * @param maxSearchDistance
	 *            - The max distance we want to search before giving up
	 * @throws Exception
	 */
	public PathFinder(String trackCode, int maxSearchDistance) throws Exception {

		this.maxSearchDistance = maxSearchDistance;

		track = new TrackParser().loadFromFile(trackCode);

		map = new Node[track.getRows()][track.getColumns()];

		for (int x = 0; x < track.getRows(); x++) {
			for (int y = 0; y < track.getColumns(); y++) {

				map[x][y] = new Node(x, y);

				if (track.map[x][y].isValid()) {
					map[x][y].setFree(); // free path
					map[x][y].cost = bigNumber;
				} else {
					map[x][y].setBlocked(); // blocked path
				}

			}
		}

	}

	/**
	 * Calculates the best path from a source to a target
	 * 
	 * @param source
	 *            - Source coordinate
	 * @param target
	 *            - target coordinate
	 * @return path - An array of coordinates containing the shortest path
	 */
	public ArrayList<Coordinate2D> calculatePath(Coordinate2D source, Coordinate2D target) {

		ArrayList<Coordinate2D> path = new ArrayList<Coordinate2D>();

		Node targetNode = map[target.getX()][target.getY()];
		Node sourceNode = map[source.getX()][source.getY()];

		int maxDepth = 0;

		closed.clear();
		open.clear();
		map[source.getX()][source.getY()].cost = 0;
		map[source.getX()][source.getY()].depth = 0;
		open.add(map[source.getX()][source.getY()]);

		map[target.getX()][target.getY()].parent = null;

		while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {

			Node currentNode = getFirstInOpen();

			if ((currentNode.x == target.getX()) && (currentNode.y == target.getY())) {
				break;
			}

			open.remove(currentNode);
			closed.add(currentNode);

			for (int x = -1; x < 2; x++) {
				for (int y = -1; y < 2; y++) {

					// not a neighbor, its the current tile
					if ((x == 0) && (y == 0)) {
						continue;
					}

					// determine the location of the neighbor and evaluate it
					Node candidateNode = new Node(x + currentNode.x, y + currentNode.y);

					// Off boundaries check
					if ((candidateNode.x > 0) && (candidateNode.y > 0) && (candidateNode.x < track.getRows())
							&& (candidateNode.y < track.getColumns())) {

						if (!map[candidateNode.x][candidateNode.y].isBlocked()) {
							int checkPointDistance = getDistance(candidateNode, targetNode); // depth
							int sourceDistance = getDistance(sourceNode, candidateNode); // cost
							int nextStepCost = sourceDistance + checkPointDistance;

							Node neighbor = map[candidateNode.x][candidateNode.y];

							if (nextStepCost < neighbor.cost) {
								if (inOpenList(neighbor)) {
									removeFromOpen(neighbor);
								}
								if (inClosedList(neighbor)) {
									removeFromClosed(neighbor);
								}

							}

							if (!inOpenList(neighbor) && !(inClosedList(neighbor))) {
								neighbor.cost = nextStepCost;
								neighbor.depth = sourceDistance;
								maxDepth = Math.max(maxDepth, neighbor.setParent(currentNode));
								open.add(neighbor);
							}
						}
					}
				}
			}

		}

		// This means the target node could not be reached, return null
		if (map[target.getX()][target.getY()].parent == null) {
			return null;
		}

		// Now backtrack the node map, from target to source, through the node
		// parents
		Node cursorNode = map[target.getX()][target.getY()];
		while ((cursorNode.x != source.getX()) || (cursorNode.y != source.getY())) {
			Coordinate2D pathCoordinate = new Coordinate2D(cursorNode.x, cursorNode.y);
			path.add(pathCoordinate);
			cursorNode = cursorNode.parent;
		}

		// Add the last node, the one used to begin the search
		path.add(source);

		return path;
	}

	/**
	 * Calculate the distance between to points using the "Manhattan distance".
	 * This is heuristic and gives an approximate distance.
	 */

	public int getDistance(Node source, Node target) {

		int deltax = Math.abs(target.x - source.x);
		int deltay = Math.abs(target.y - source.y);

		return deltax + deltay;
	}

	/**
	 * This class (Node) is analog to the TrackPoint to the track map.
	 * 
	 */

	private class Node implements Comparable<Object> {
		/** The x coordinate of the node */
		private int x;
		/** The y coordinate of the node */
		private int y;
		/**
		 * the movement cost (in number of squares) from the start point A to
		 * the current square
		 */
		private int cost;
		/**
		 * the estimated movement cost (in number of squares ) from the current
		 * square to the destination point
		 */
		private int depth;
		/** The parent of this node, how we reached it in the search */
		private Node parent;
		/** if the tile is valid according to the track map */
		private boolean blocked;

		/**
		 * Create a new node
		 * 
		 * @param x
		 *            The x coordinate of the node
		 * @param y
		 *            The y coordinate of the node
		 */
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * Set this coordinate as a blocked path
		 */

		public void setBlocked() {
			blocked = true;
		}

		/**
		 * Set this coordinate as a valid path
		 */
		public void setFree() {
			blocked = false;
		}

		/**
		 * Check if the coordinate is blocked
		 */
		public boolean isBlocked() {
			return blocked;
		}

		/**
		 * Set the parent of this node
		 * 
		 * @param parent
		 *            The parent node which lead us to this node
		 * @return The depth we have no reached in searching
		 */
		public int setParent(Node parent) {
			depth = parent.depth + 1;
			this.parent = parent;

			return depth;
		}

		/**
		 * @see Comparable#compareTo(Object)
		 */
		public int compareTo(Object other) {
			Node o = (Node) other;

			int f = depth + cost;
			int of = o.depth + o.cost;

			if (f < of) {
				return -1;
			} else if (f > of) {
				return 1;
			} else {
				return 0;
			}
		}

	}

	/**
	 * A simple sorted list
	 *
	 */
	private class SortedList {
		/** The list of elements */
		@SuppressWarnings("rawtypes")
		private ArrayList list = new ArrayList();

		/**
		 * Retrieve the first element from the list
		 * 
		 * @return The first element from the list
		 */
		public Object first() {
			return list.get(0);
		}

		/**
		 * Empty the list
		 */
		public void clear() {
			list.clear();
		}

		/**
		 * Add an element to the list - causes sorting
		 * 
		 * @param o
		 *            The element to add
		 */
		@SuppressWarnings("unchecked")
		public void add(Object o) {
			list.add(o);
			Collections.sort(list);
		}

		/**
		 * Remove an element from the list
		 * 
		 * @param o
		 *            The element to remove
		 */
		public void remove(Object o) {
			list.remove(o);
		}

		/**
		 * Get the number of elements in the list
		 * 
		 * @return The number of element in the list
		 */
		public int size() {
			return list.size();
		}

		/**
		 * Check if an element is in the list
		 * 
		 * @param o
		 *            The element to search for
		 * @return True if the element is in the list
		 */
		public boolean contains(Object o) {
			return list.contains(o);
		}
	}

	/**
	 * Get the first element from the open list. This is the next one to be
	 * searched.
	 * 
	 * @return The first element in the open list
	 */
	protected Node getFirstInOpen() {
		return (Node) open.first();
	}

	/**
	 * Check if a node is in the open list
	 * 
	 * @param node
	 *            The node to check for
	 * @return True if the node given is in the open list
	 */
	protected boolean inOpenList(Node node) {
		return open.contains(node);
	}

	/**
	 * Remove a node from the open list
	 * 
	 * @param node
	 *            The node to remove from the open list
	 */
	protected void removeFromOpen(Node node) {
		open.remove(node);
	}

	/**
	 * Add a node to the closed list
	 * 
	 * @param node
	 *            The node to add to the closed list
	 */
	protected void addToClosed(Node node) {
		closed.add(node);
	}

	/**
	 * Check if the node supplied is in the closed list
	 * 
	 * @param node
	 *            The node to search for
	 * @return True if the node specified is in the closed list
	 */
	protected boolean inClosedList(Node node) {
		return closed.contains(node);
	}

	/**
	 * Remove a node from the closed list
	 * 
	 * @param node
	 *            The node to remove from the closed list
	 */
	protected void removeFromClosed(Node node) {
		closed.remove(node);
	}
}
