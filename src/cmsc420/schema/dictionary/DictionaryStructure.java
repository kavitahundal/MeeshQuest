package cmsc420.schema.dictionary;

import cmsc420.schema.City;

public interface DictionaryStructure {

	public void add(City city);
	public boolean contains(City city);
	public void remove(City city);
}
