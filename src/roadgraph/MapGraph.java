/**
 * @author UCSD MOOC development team and Maayon Thayaparan
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import geography.GeographicPoint;
import util.GraphLoader;


public class MapGraph {
	// Maintain both nodes and edges as you will need to
	// be able to look up nodes by lat/lon or by roads
	// that contain those nodes.
	private HashMap<GeographicPoint,MapNode> pointNodeMap;
	private HashSet<MapEdge> edges;

	
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		pointNodeMap = new HashMap<GeographicPoint,MapNode>();
		edges = new HashSet<MapEdge>();
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		return pointNodeMap.values().size();
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		return pointNodeMap.keySet();
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		return edges.size();
	}

	
	
	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		if (location == null) {
			return false;
		}
		MapNode n = pointNodeMap.get(location);
		if (n == null) {
			n = new MapNode(location);
			pointNodeMap.put(location, n);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {

		MapNode n1 = pointNodeMap.get(from);
		MapNode n2 = pointNodeMap.get(to);

		// check nodes are valid
		if (n1 == null)
			throw new NullPointerException("addEdge: pt1:"+from+"is not in graph");
		if (n2 == null)
			throw new NullPointerException("addEdge: pt2:"+to+"is not in graph");

		MapEdge edge = new MapEdge(roadName, roadType, n1, n2, length);
		edges.add(edge);
		n1.addEdge(edge);
		
	}
		
	/** 
	 * Get a set of neighbor nodes from a mapNode
	 * @param node  The node to get the neighbors from
	 * @return A set containing the MapNode objects that are the neighbors 
	 * 	of node
	 */
	private Set<MapNode> getNeighbors(MapNode node) {
		return node.getNeighbors();
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, 
			 					     Consumer<GeographicPoint> nodeSearched)
	{
		/* Note that this method is a little long and we might think
		 * about refactoring it to break it into shorter methods as we 
		 * did in the Maze search code in week 2 */
		
		// Setup - check validity of inputs
		if (start == null || goal == null)
			throw new NullPointerException("Cannot find route from or to null node");
		MapNode startNode = pointNodeMap.get(start);
		MapNode endNode = pointNodeMap.get(goal);
		if (startNode == null) {
			System.err.println("Start node " + start + " does not exist");
			return null;
		}
		if (endNode == null) {
			System.err.println("End node " + goal + " does not exist");
			return null;
		}

		// setup to begin BFS
		HashMap<MapNode,MapNode> parentMap = new HashMap<MapNode,MapNode>();
		Queue<MapNode> toExplore = new LinkedList<MapNode>();
		HashSet<MapNode> visited = new HashSet<MapNode>();
		toExplore.add(startNode);
		MapNode curr = null;

		while (!toExplore.isEmpty()) {
			curr = toExplore.remove();
			
			 // hook for visualization
			nodeSearched.accept(curr.getLocation());
			
			if (curr.equals(endNode)) break;
			Set<MapNode> neighbors = getNeighbors(curr);
			for (MapNode neighbor : neighbors) {
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					parentMap.put(neighbor, curr);
					toExplore.add(neighbor);
				}
			}
		}
		if (!curr.equals(endNode)) {
			System.out.println("No path found from " +start+ " to " + goal);
			return null;
		}
		// Reconstruct the parent path
		List<GeographicPoint> path =
				reconstructPath(parentMap, startNode, endNode);

		return path;
	
	}
	


	/** Reconstruct a path from start to goal using the parentMap
	 *
	 * @param parentMap the HashNode map of children and their parents
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from
	 *   start to goal (including both start and goal).
	 */
	private List<GeographicPoint>
	reconstructPath(HashMap<MapNode,MapNode> parentMap,
					MapNode start, MapNode goal)
	{
		LinkedList<GeographicPoint> path = new LinkedList<GeographicPoint>();
		MapNode current = goal;

		while (!current.equals(start)) {
			path.addFirst(current.getLocation());
			current = parentMap.get(current);
		}

		// add start
		path.addFirst(start.getLocation());
		return path;
	}


	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{

		
		// Setup - check validity of inputs
		
		if (start == null || goal == null)
			throw new NullPointerException("Cannot find route from or to null node");
		MapNode startNode = pointNodeMap.get(start);
		MapNode endNode = pointNodeMap.get(goal);
		if (startNode == null) {
			System.err.println("Start node " + start + " does not exist");
			return null;
		}
		if (endNode == null) {
			System.err.println("End node " + goal + " does not exist");
			return null;
		}
		


		
		/* ----EXTENSION ----
		 * When date and time variables are set to null, the search will behave as normal.
		 * When date and time variables are valued, the 
		 */
		
		LocalDate date = LocalDate.now();
		LocalTime time = LocalTime.now();
		
		// setup to begin Dijkstra
		
		HashMap<MapNode,MapNode> parentMap = new HashMap<MapNode,MapNode>();
		boolean found = aStarSearch(startNode, endNode, parentMap, nodeSearched, false, date, time);
		
		
		if (found == false) {
			System.out.println("No path found from " +start+ " to " + goal);
			return null;
		}
		
		
		// Reconstruct the parent path
		List<GeographicPoint> path =
				reconstructPath(parentMap, startNode, endNode);

		return path;
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
											 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		
		// Setup - check validity of inputs
		
		if (start == null || goal == null)
			throw new NullPointerException("Cannot find route from or to null node");
		MapNode startNode = pointNodeMap.get(start);
		MapNode endNode = pointNodeMap.get(goal);
		if (startNode == null) {
			System.err.println("Start node " + start + " does not exist");
			return null;
		}
		if (endNode == null) {
			System.err.println("End node " + goal + " does not exist");
			return null;
		}
		
		/* ----EXTENSION ----
		 * When date and time variables are set to null, the search will behave as normal.
		 * When date and time variables are valued, the 
		 */
		
		LocalDate date = LocalDate.now();
		LocalTime time = LocalTime.now();

		// setup to begin Dijkstra
		
		HashMap<MapNode,MapNode> parentMap = new HashMap<MapNode,MapNode>();
		boolean found = aStarSearch(startNode, endNode, parentMap, nodeSearched, true, date, time);

		if (found == false) {
			System.out.println("No path found from " +start+ " to " + goal);
			return null;
		}		
		
		// Reconstruct the parent path
		List<GeographicPoint> path =
				reconstructPath(parentMap, startNode, endNode);

		return path;
	}	

	
	// ----- EXTENSION ------
	/* The aStarSearch includes parameters for date and time.
	 * When date and time are set to null, the search will behave the same as in the course without extension
	 * When date and time are valued, the search will modify nodes in the search based on road type depending on whether it is rush hour or not
	 */

	private boolean aStarSearch(MapNode startNode, MapNode endNode, HashMap<MapNode,MapNode> parentMap, Consumer<GeographicPoint> nodeSearched, boolean aStar, LocalDate date,  LocalTime time)	
	{
		
		/* When using dijkstra, this just needs to be false since dijkstra 
		 * is just a special case of aStar where distance to goal is 0.
		 * */
		 
		
		for (GeographicPoint key : pointNodeMap.keySet()) {
			pointNodeMap.get(key).setFromDist(Double.POSITIVE_INFINITY);
			
			if (aStar == true) {
				pointNodeMap.get(key).setToDist(key.distance(endNode.getLocation()));
			}
		}
			
		PriorityQueue<MapNode> toExplore = new PriorityQueue<MapNode>();
		HashSet<MapNode> visited = new HashSet<MapNode>();
		boolean found = false;
		
		startNode.setFromDist(0.0);
		toExplore.add(startNode);
		MapNode curr = null;
		int count = 0;

		while (!toExplore.isEmpty()) {
			curr = toExplore.remove();
			count++;
			System.out.println(curr + " " + curr.getRoadType() + " " + count);
			
			
			 // hook for visualization
			nodeSearched.accept(curr.getLocation());
			
			
			if (!visited.contains(curr)) {
				visited.add(curr);
				
				if (curr.equals(endNode)) {
					found = true;
					return found;
				}
				
				Set<MapNode> neighbors = getNeighbors(curr);
				for (MapNode neighbor : neighbors) {
					
					if (!visited.contains(neighbor)) {
							
						double neighborDistance = neighbor.getFromDist() + neighbor.getToDist();
						double neighborFromDistance = curr.getFromDist() + curr.getLocation().distance(neighbor.getLocation()); 
						double neighborTrueDistance = neighborFromDistance + neighbor.getToDist();
						
						if (neighborTrueDistance < neighborDistance) {
							
							/* ------ EXTENSION -------
							 * This helper method checks to see if it is rush hour.
							 * Will modify neighborFromDistance based on road type based on whether it is rus hour or not
							 */
							neighborFromDistance = rushHourCheck(neighborFromDistance, neighbor, date, time); 
							
							neighbor.setFromDist(neighborFromDistance);
							parentMap.put(neighbor, curr);
							toExplore.add(neighbor);
						}
					}
				}
			}
		}
		return found; 
	}
	
	/* 
	 *  --------- EXTENSION -----------
	 * 
	 * This is a helper method to check if the inputed date/time is during rush hour.
	 * When date and time are set to null, the search will behave the same as in the course without extension
	 * When date and time are valued, the search will modify nodes in the search based on road type depending on whether it is rush hour or not
	 * Residential roads will be considered of lower distance during rush hour. 
	 * Residential roads will be considered longer distance outside of rush hour. 
	 * Can optimize this method to look at additional road types
	 */
	
	private double rushHourCheck(double fromDistance, MapNode neighbor, LocalDate date, LocalTime time) {
		
		
		// Initialize rush hour values for morning and evening
		// Morning rush hour is set to 6AM - 9AM on weekdays
		// Evening rush hour is set to 4PM - 7AM on weekdays 
		
		LocalTime morningRushStart = LocalTime.of(6, 0);
		LocalTime morningRushEnd = LocalTime.of(9, 0);
		LocalTime eveningRushStart = LocalTime.of(16, 0);
		LocalTime eveningRushEnd = LocalTime.of(19, 0);
		boolean rushHour = false; 
		
		// if date or time parameter is null, we will not alter fromDistance value
		
		if (date == null || time == null ) {  
			return fromDistance;
		}
		
		/* Checks if it is a weekday and if the date and time set are during rush hour. 
		 * If it is rush hour, we will set boolean of rushHour to true which will be used 
		 * to determine how we adjust fromDistance 
		 */
		
		
		if ((!date.getDayOfWeek().toString().equals("SATURDAY") || 
				!date.getDayOfWeek().toString().equals("SUNDAY")) &&
				(time.isAfter(morningRushStart) && time.isBefore(morningRushEnd)) ||
				(time.isAfter(eveningRushStart) && time.isBefore(eveningRushEnd)) ) 
		{
			rushHour = true;
		}
		
		/* if it is rush hour and road type is residential, we will lower fromDistance since 
		 * taking residential road may be favorable during rush hour
		 * This clause can be optimized for additional road types 
		 * Can also change the modifier accordingly 
		 */
		
		
		if (rushHour == true) {
			if (neighbor.getRoadType().equals("residential")) {
				fromDistance = fromDistance * 0.5;
			}
		}
		
		/* If it is NOT rush hour and road type is residential, we will increase fromDistance since 
		 * taking residential road is less favorable outside of rush hour
		 * This clause can be optimized for additional road types
		 */
		
		if (rushHour == false) {
			if (neighbor.getRoadType().equals("residential")) {
				fromDistance = fromDistance * 1.5;
			}
		}
		

		return fromDistance;
	}



	
	public static void main(String[] args)
	{
		
		/*
		System.out.print("Making a new map...");
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", theMap);
		System.out.println("DONE.");
		
		// You can use this method for testing.  
		
		/* Use this code in Week 3 End of Week Quiz
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
		
		
		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);

		*/
		
		// ----------------Debug Test Cases ----------------
		
	    MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
		GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		
		System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
		System.out.println("DIJKSTRA");
		List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
		System.out.println("A*");
		List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
		
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.869423, -117.220917);
		testEnd = new GeographicPoint(32.869255, -117.216927);
		System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
		System.out.println("DIJKSTRA");
		testroute = testMap.dijkstra(testStart,testEnd);
		System.out.println("A*");
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		// A slightly more complex test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
		System.out.println("DIJKSTRA");
		testroute = testMap.dijkstra(testStart,testEnd);
		System.out.println("A*");
		testroute2 = testMap.aStarSearch(testStart,testEnd);	
		
		
		// ---------- Quiz --------------------
		
		
		/*
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);

		System.out.println("DIJKSTRA");
		List<GeographicPoint> route = theMap.dijkstra(start,end);
		System.out.println("A*");
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);
		
		*/

		
	}
	
}