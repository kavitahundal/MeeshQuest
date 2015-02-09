package cmsc420.schema.spatial;

import cmsc420.schema.City;

public class BlackNode implements TreeNode {

	private City city;
	
	public BlackNode(City city) {
		this.city = city;
	}

	@Override
	public TreeNode add(City city) {
		GrayNode node = new GrayNode();
		node.add(this.city);
		node.add(city);
		return node;
	}

	@Override
	public boolean contains(City city) {
		return city.equals(this.city);
	}
	
}
