package cmsc132.legacy;

/**
 * Represents the processing we apply to a vertex of a graph.
 */
public interface CallBack<E> {
	public void processVertex(String vertex, E vertexData);
}
