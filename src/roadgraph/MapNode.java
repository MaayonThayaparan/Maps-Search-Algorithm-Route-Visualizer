/**
 * A class to represent a node in the map
 */
package roadgraph;

import java.util.HashSet;
import java.util.Set;

import geography.GeographicPoint;

/**
 * @author UCSD MOOC development team
 * 
 * Class representing a vertex (or node) in our MapGraph
 *
 */
class MapNode implements Comparable<MapNode>
{
	/** The list of edges out of this node */
	private HashSet<MapEdge> edges;
		
	/** the latitude and longitude of this node */
	private GeographicPoint location;
	double fromStartDist;
	double toGoalDist; 
	String roadType; //this is only used when Node is created as a neighbor to another Node

		
	/** 
	 * Create a new MapNode at a given Geographic location
	 * @param loc the location of this node
	 */
	MapNode(GeographicPoint loc)
	{
		location = loc;
		edges = new HashSet<MapEdge>();
		fromStartDist = 0.0;
		toGoalDist = 0.0;
		roadType = "";
	}
		
	/**
	 * Add an edge that is outgoing from this node in the graph
	 * @param edge The edge to be added
	 */
	
	public void setFromDist(double d) {
		this.fromStartDist = d;
	}
	
	
	public void setToDist(double d) {
		this.toGoalDist = d;
	}
	
	
	public double getFromDist() {
		return fromStartDist;
	}
	
	public double getToDist() {
		return toGoalDist;
	}
	
	public void setRoadType(String road) {
		this.roadType = road;
	}
	
	public String getRoadType() {
		return roadType;
	}
	
	
	
	public int compareTo(MapNode m) {
		double thisDistance = this.getFromDist() + this.getToDist();
		double mDistance = m.getFromDist() + m.getToDist();
		
		if (thisDistance > mDistance) {
			return 1;
		}
		if (thisDistance < mDistance) {
			return -1;
		}
		if (thisDistance == mDistance) {
			return 0;
		}
		//int diff = (int) (thisDistance - mDistance);
		
		return -1; 
	}
	
	void addEdge(MapEdge edge)
	{
		edges.add(edge);
	}
	
	/**  
	 * Return the neighbors of this MapNode 
	 * @return a set containing all the neighbors of this node
	 */
	Set<MapNode> getNeighbors()
	{
		Set<MapNode> neighbors = new HashSet<MapNode>();
		for (MapEdge edge : edges) {
			MapNode curr = edge.getOtherNode(this);
			
			//this sets the roadType for this node
			curr.setRoadType(edge.getRoadType()); 
			neighbors.add(curr);
		}
		return neighbors;
	}
	
	/**
	 * Get the geographic location that this node represents
	 * @return the geographic location of this node
	 */
	GeographicPoint getLocation()
	{
		return location;
	}
	
	/**
	 * return the edges out of this node
	 * @return a set contianing all the edges out of this node.
	 */
	Set<MapEdge> getEdges()
	{
		return edges;
	}
	
	/** Returns whether two nodes are equal.
	 * Nodes are considered equal if their locations are the same, 
	 * even if their street list is different.
	 * @param o the node to compare to
	 * @return true if these nodes are at the same location, false otherwise
	 */
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof MapNode) || (o == null)) {
			return false;
		}
		MapNode node = (MapNode)o;
		return node.location.equals(this.location);
	}
	
	/** Because we compare nodes using their location, we also 
	 * may use their location for HashCode.
	 * @return The HashCode for this node, which is the HashCode for the 
	 * underlying point
	 */
	@Override
	public int hashCode()
	{
		return location.hashCode();
	}
	
	/** ToString to print out a MapNode object
	 *  @return the string representation of a MapNode
	 */
	@Override
	public String toString()
	{
		String toReturn = "[NODE at location (" + location + ")";
		toReturn += " intersects streets: ";
		for (MapEdge e: edges) {
			toReturn += e.getRoadName() + ", ";
		}
		toReturn += "]";
		return toReturn;
	}

	// For debugging, output roadNames as a String.
	public String roadNamesAsString()
	{
		String toReturn = "(";
		for (MapEdge e: edges) {
			toReturn += e.getRoadName() + ", ";
		}
		toReturn += ")";
		return toReturn;
	}

}