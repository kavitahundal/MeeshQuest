package cmsc420.schema.spatial.PM;

import cmsc420.schema.City;

public interface Validator {

	/**
	 * Checks if adding the city to the black node is safe without partitioning.
	 * 
	 * @param node
	 *            The tree node.
	 * @param city
	 *            The city to add.
	 * @return Whether adding the city without partitioning keeps the tree
	 *         valid.
	 */
	public boolean valid(PMBlackNode node, City city);

	/**
	 * Checks if adding the road to the black node is safe without partitioning.
	 * 
	 * @param node
	 *            The tree node.
	 * @param city1
	 *            An end point of the road to add.
	 * @param city2
	 *            An end point of the road to add.
	 * @return Whether adding the road without partitioning keeps the tree
	 *         valid.
	 */
	public boolean valid(PMBlackNode node, City city1, City city2);
}
