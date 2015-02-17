package cmsc420.schema.spatial;

/**
 * An class that encapsulates an object that can generate spatial structures.
 * The decision to make seedlings to get instances of trees was based off the
 * fact that I needed an instance of the tree before I had the necessary data
 * (width and height) to support the tree.
 * 
 * @author Andrew Liu
 *
 */
public interface Seedling {

	/**
	 * Creates an instance of the spatial structure associated with this
	 * seedling object.
	 * 
	 * @param name
	 *            the name of the spatial structure
	 * @param width
	 *            the width of the spatial structure
	 * @param height
	 *            the height of the spatial structure
	 * @return a new instance of the spatial structure
	 */
	public SpatialStructure generate(String name, float width, float height);
}
