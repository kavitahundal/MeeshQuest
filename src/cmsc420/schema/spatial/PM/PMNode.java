package cmsc420.schema.spatial.PM;

import java.awt.geom.Point2D;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.schema.City;

public interface PMNode {

	public PMNode addCity(City city);

	public PMNode addRoad(City city1, City city2);

	public Element elementize(Document doc);

	public boolean contains(City city);

	public Point2D.Float origin();

	public int width();

	public int height();

}
