package cmsc420.schema.spatial.PM;

import java.awt.geom.Point2D;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface PMNode {

	public PMNode addVertex(Point2D.Float landmark);

	public PMNode addRoad(Point2D.Float endpoint1, Point2D.Float endpoint2);

	public Element elementize(Document doc);

	public boolean contains(Point2D.Float landmark);

	public Point2D.Float origin();

	public int width();

	public int height();

}
