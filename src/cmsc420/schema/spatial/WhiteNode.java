package cmsc420.schema.spatial;

import cmsc420.schema.City;

public class WhiteNode implements TreeNode {

	@Override
	public TreeNode add(City city) {
		return new BlackNode(city);
	}

	@Override
	public boolean contains(City city) {
		return false;
	}
	
	
}
