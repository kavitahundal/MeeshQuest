package cmsc420.schema.spatial.PM;

import cmsc420.schema.City;

public interface PMNode {
	
	public PMNode addCity(City city);
	public PMNode addRoad(City city1, City city2);

}
