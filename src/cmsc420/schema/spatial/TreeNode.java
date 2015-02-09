package cmsc420.schema.spatial;

import cmsc420.schema.City;

public interface TreeNode {

	public abstract TreeNode add(City city);
	public abstract boolean contains(City city);
}
