package cmsc420.schema.spatial;

import java.util.List;

import cmsc420.schema.City;
import cmsc420.schema.DataStructure;

public interface SpatialStructure extends DataStructure<City> {
	
	public int size();
	public float getSpatialWidth();
	public float getSpatialHeight();
	public List<String> range();
}
