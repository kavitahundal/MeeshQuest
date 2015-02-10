package cmsc420.schema.spatial;

import cmsc420.drawing.CanvasPlus;

/**
 * @author Andrew Liu
 * 
 *         The decision to make seedlings to get instances of trees was based
 *         off the fact that I needed an instance of the tree before I had the
 *         necessary data (width and height) to support the tree.
 *
 */
public interface Seedling {

	public SpatialStructure generate(float width, float height, CanvasPlus canvas);
}
