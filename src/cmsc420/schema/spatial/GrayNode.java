package cmsc420.schema.spatial;

import cmsc420.schema.City;

public class GrayNode implements TreeNode {

	private TreeNode quadrant1;
	private TreeNode quadrant2;
	private TreeNode quadrant3;
	private TreeNode quadrant4;
	
	public GrayNode() {
		this.quadrant1 = new WhiteNode();
		this.quadrant2 = new WhiteNode();
		this.quadrant3 = new WhiteNode();
		this.quadrant4 = new WhiteNode();
	}

	@Override
	public TreeNode add(City city) {
		// r = findRegion()
		// replace r with r.add(city)
		return this;
	}

	@Override
	public boolean contains(City city) {
		// r = getRegion()
		// return r.contains(city)
		return false;
	}
}
