package cmsc420.schema.spatial;

import cmsc420.schema.City;

public interface TreeNode {

	public TreeNode add(City city);
	public boolean contains(City city);
	public TreeNode remove(City city);
}
