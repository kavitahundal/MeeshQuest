package cmsc420.schema.spatial;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.schema.City;

public interface TreeNode {

	public TreeNode add(City city);
	public boolean contains(City city);
	public TreeNode remove(City city);
	public Element elementize(Document doc);
}
