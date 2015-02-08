package cmsc420.schema.spatial;

import cmsc420.schema.City;

public interface SpatialStructure {

	public void add(City city);
	public boolean contains(City city);
	public void remove(City city);
	
	public void setBounds(float width, float height);
}
