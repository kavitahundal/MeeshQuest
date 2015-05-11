package cmsc420.schema.spatial.PM;

import java.awt.geom.Point2D;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.schema.City;

public interface PMNode {

	public PMNode addVertex(Point2D.Float landmark);

	public PMNode addRoad(City city1, City city2);

	public Element elementize(Document doc);

	public boolean contains(Point2D.Float landmark);

	public Point2D.Float origin();

	public int width();

	public int height();
	
	public PMNode remove(Point2D.Float element);
	
	public PMNode removeRoad(City city1, City city2);

}
