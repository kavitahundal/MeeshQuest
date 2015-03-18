package cmsc132;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Implements a graph. We use two maps: one map for adjacency properties
 * (adjacencyMap) and one map (dataMap) to keep track of the data associated
 * with a vertex.
 * 
 * @author CMSC132
 * @author Andrew Liu
 * 
 * @param <E>
 *            The data the graph holds.
 */
public class Graph<E> {

	/* You must use the following maps in your implementation */
	private HashMap<String, HashMap<String, Double>> adjacencyMap;
	private HashMap<String, E> dataMap;

	/**
	 * Initializes the adjacency and data maps
	 */
	public Graph() {
		this.adjacencyMap = new HashMap<>();
		this.dataMap = new HashMap<>();
	}

	/**
	 * Adds or updates a directed edge with the specified cost.
	 * 
	 * @param startVertexName
	 *            Starting vertex.
	 * @param endVertexName
	 *            Ending vertex.
	 * @param cost
	 *            Cost of the edge between the vertices.
	 * @throws IllegalArgumentException
	 *             If any of the vertices are not part of the graph.
	 */
	public void addDirectedEdge(String startVertexName, String endVertexName,
			double cost) throws IllegalArgumentException {
		if (!this.dataMap.containsKey(startVertexName)
				|| !this.dataMap.containsKey(endVertexName)) {
			throw new IllegalArgumentException("Missing vertices.");
		}
		HashMap<String, Double> map = this.adjacencyMap.get(startVertexName);
		map.put(endVertexName, cost);
		this.adjacencyMap.put(startVertexName, map);
	}

	/**
	 * Adds a vertex to the graph by adding to the adjacency map an entry for
	 * the vertex. This entry will be an empty map. An entry in the dataMap will
	 * store the provided data.
	 * 
	 * @param vertexName
	 *            The new vertex's name.
	 * @param data
	 *            The data the new vertex holds.
	 * @throws IllegalArgumentException
	 *             If the vertex already exists in the graph.
	 */
	public void addVertex(String vertexName, E data)
			throws IllegalArgumentException {
		if (this.dataMap.containsKey(vertexName)) {
			throw new IllegalArgumentException("Vertex already exists.");
		}
		this.dataMap.put(vertexName, data);
		this.adjacencyMap.put(vertexName, new HashMap<String, Double>());
	}

	/**
	 * Computes Breadth-First Search of the specified graph.
	 * 
	 * @param startVertexName
	 *            The starting vertex.
	 * @param callback
	 *            Represents the processing to apply to each vertex
	 * @throws IllegalArgumentException
	 *             If the vertex is not part of the graph.
	 */
	public void doBreadthFirstSearch(String startVertexName,
			CallBack<E> callback) throws IllegalArgumentException {
		if (!this.dataMap.containsKey(startVertexName)) {
			throw new IllegalArgumentException("Missing vertex.");
		}
		Set<String> usedVertices = new HashSet<>();
		Queue<String> discovered = new LinkedList<String>();
		discovered.add(startVertexName);
		while (discovered.size() != 0) {
			String ele = discovered.remove();
			if (!usedVertices.contains(ele)) {
				usedVertices.add(ele);
				callback.processVertex(ele, this.getData(ele));
				List<String> adj = new ArrayList<>(this
						.getAdjacentVertices(ele).keySet());
				Collections.sort(adj);
				for (String vertex : adj) {
					if (!usedVertices.contains(vertex)) {
						discovered.add(vertex);
					}
				}
			}
		}
	}

	/**
	 * Computes Depth-First Search of the specified graph.
	 * 
	 * @param startVertexName
	 *            The starting vertex.
	 * @param callback
	 *            Represents the processing to apply to each vertex
	 * @throws IllegalArgumentException
	 *             If the vertex is not part of the graph.
	 */
	public void doDepthFirstSearch(String startVertexName, CallBack<E> callback)
			throws IllegalArgumentException {
		if (!this.dataMap.containsKey(startVertexName)) {
			throw new IllegalArgumentException("Missing vertex.");
		}
		Set<String> usedVertices = new HashSet<>();
		Stack<String> discovered = new Stack<>();
		discovered.push(startVertexName);
		while (discovered.size() != 0) {
			String ele = discovered.pop();
			if (!usedVertices.contains(ele)) {
				usedVertices.add(ele);
				callback.processVertex(ele, this.getData(ele));
				List<String> adj = new ArrayList<>(this
						.getAdjacentVertices(ele).keySet());
				Collections.sort(adj);
				for (String vertex : adj) {
					if (!usedVertices.contains(vertex)) {
						discovered.push(vertex);
					}
				}
			}
		}
	}

	/**
	 * Computes the shortest path and shortest path cost using Dijkstras's
	 * algorithm. It initializes shortestPath with the names of the vertices
	 * corresponding to the shortest path. If there is no shortest path,
	 * shortestPath will be have entry "None".
	 * 
	 * @param startVertexName
	 *            The starting vertex.
	 * @param endVertexName
	 *            The vertex that is the goal.
	 * @param shortestPath
	 *            Initialized by the method with the shortest path or "None".
	 * @return Shortest path cost or -1 if no path exists.
	 * @throws IllegalArgumentException
	 *             If any of the vertices are not part of the graph.
	 */
	public double doDijkstras(String startVertexName, String endVertexName,
			ArrayList<String> shortestPath) throws IllegalArgumentException {
		if (!this.dataMap.containsKey(startVertexName)
				|| !this.dataMap.containsKey(endVertexName)) {
			throw new IllegalArgumentException("Missing vertices.");
		}

		/* initial setup */
		final double INFINITY = -1;
		final String NONE = "None";
		Set<String> usedVertices = new HashSet<>();
		Map<String, Double> costs = new HashMap<>();
		Map<String, String> predecessor = new HashMap<>();
		for (String vertex : this.getVertices()) {
			costs.put(vertex, INFINITY);
			predecessor.put(vertex, NONE);
		}
		costs.put(startVertexName, 0.0);

		/* starting the greedy algorithm */
		while (!usedVertices.contains(endVertexName)) {
			String cheapestVertex = NONE;
			// find "next" vertex, or lowest cost vertex
			for (String vertex : this.getVertices()) {
				if (usedVertices.contains(vertex)
						|| costs.get(vertex) == INFINITY) {
					continue;
				}
				Double cost = costs.get(cheapestVertex);
				if (cost == null || costs.get(vertex) < cost) {
					cheapestVertex = vertex;
				}
			}
			if (cheapestVertex == NONE) {
				break; // we know we can't find anything else anymore
			}
			usedVertices.add(cheapestVertex);
			// finding vertices with cost values to update
			for (String vertex : this.getVertices()) {
				if (!usedVertices.contains(vertex)) {
					try {
						Double cost = this.getCost(cheapestVertex, vertex);
						double oldCost = costs.get(vertex);
						double newCost = cost + costs.get(cheapestVertex);
						if (oldCost == INFINITY || oldCost > newCost) {
							costs.put(vertex, newCost);
							predecessor.put(vertex, cheapestVertex);
						}
					} catch (IllegalArgumentException e) {
						// do nothing
					}
				}
			}
		}

		/* retrieving shortest path */
		if (costs.get(endVertexName) == INFINITY) { // if there's no path
			shortestPath.add(NONE);
			return INFINITY;
		}
		Stack<String> ordering = new Stack<>();
		ordering.push(endVertexName);
		String currentEle = endVertexName;
		while (!predecessor.get(currentEle).equals(NONE)) {
			currentEle = predecessor.get(currentEle);
			ordering.push(currentEle);
		}
		while (ordering.size() > 0) {
			shortestPath.add(ordering.pop()); // reversing the order
		}
		return costs.get(endVertexName);
	}

	/**
	 * Returns a map with information about vertices adjacent to vertexName. If
	 * the vertex has no adjacent vertices, an empty map is returned.
	 * 
	 * @param vertexName
	 *            The starting vertex.
	 * @return Mapping of the vertices adjacent to the given vertex.
	 */
	public Map<String, Double> getAdjacentVertices(String vertexName) {
		return this.adjacencyMap.get(vertexName);
	}

	/**
	 * Returns the cost associated with the specified edge.
	 * 
	 * @param startVertexName
	 *            The starting vertex.
	 * @param endVertexName
	 *            The ending vertex.
	 * @return The edge cost.
	 * @throws IllegalArgumentException
	 *             If any of the vertices are not part of the graph.
	 */
	public double getCost(String startVertexName, String endVertexName)
			throws IllegalArgumentException {
		if (!this.dataMap.containsKey(startVertexName)
				|| !this.dataMap.containsKey(endVertexName)) {
			throw new IllegalArgumentException("Missing vertices.");
		}
		try {
			return this.adjacencyMap.get(startVertexName).get(endVertexName);
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Missing edge.");
		}
	}

	/**
	 * Returns the data component associated with the specified vertex.
	 * 
	 * @param vertex
	 *            The target vertex.
	 * @return The data associated with the vertex.
	 * @throws IllegalArgumentException
	 *             If the vertex is not part of the graph.
	 */
	public E getData(String vertex) throws IllegalArgumentException {
		if (!this.dataMap.containsKey(vertex)) {
			throw new IllegalArgumentException("Missing vertex.");
		}
		return this.dataMap.get(vertex);
	}

	/**
	 * Returns a Set with all the graph vertices.
	 * 
	 * @return The set with vertices.
	 */
	public Set<String> getVertices() {
		return new HashSet<String>(this.dataMap.keySet());
	}

	/**
	 * Returns a string with information about the Graph. Notice that vertices
	 * are printed in sorted order and information about adjacent edges is
	 * printed in sorted order (by vertex name). You may not use
	 * Collections.sort or Arrays.sort in order to implement this method. See
	 * the sample output for formatting details.
	 * 
	 * @return String with graph information.
	 */
	@Override
	public String toString() {
		return this.toStringAux().toString();
	}

	/**
	 * The helper method for the toString method. It has a very strict form.
	 * 
	 * @return The graph as a String.
	 */
	private StringBuffer toStringAux() {
		List<String> sortedVertices = this.sortVertices();
		StringBuffer s = new StringBuffer("Vertices: ");
		s.append(sortedVertices.toString());
		s.append("\nEdges:\n");
		for (String vertex : sortedVertices) {
			s.append("Vertex(" + vertex + ")--->{");
			HashMap<String, Double> edges = this.adjacencyMap.get(vertex);
			boolean first = true;
			for (String vertexAgain : sortedVertices) {
				Double cost = edges.get(vertexAgain);
				if (cost == null) {
					continue;
				}
				if (first) {
					first = false;
				} else {
					s.append(", ");
				}
				s.append(vertexAgain + "=" + cost);
			}
			s.append("}\n");
		}
		return s;
	}

	/**
	 * This is a selection sort I wrote since using built-in sorts for the
	 * toString methods was forbidden.
	 * 
	 * @return A list sorted alphabetically.
	 */
	private List<String> sortVertices() {
		List<String> sortedVertices = new ArrayList<>();
		List<String> vertices = new ArrayList<>(this.dataMap.keySet());
		while (vertices.size() > 0) {
			int minIndex = 0;
			for (int i = 1; i < vertices.size(); i++) {
				if (vertices.get(i).compareTo(vertices.get(minIndex)) < 0) {
					minIndex = i;
				}
			}
			sortedVertices.add(vertices.get(minIndex));
			vertices.remove(minIndex);
		}
		return sortedVertices;
	}
}