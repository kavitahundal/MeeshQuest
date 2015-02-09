package cmsc420.schema.mediator;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cmsc420.drawing.CanvasPlus;
import cmsc420.exceptions.CityAlreadyMappedException;
import cmsc420.exceptions.CityDoesNotExistException;
import cmsc420.exceptions.CityNotMappedException;
import cmsc420.exceptions.CityOutOfBoundsException;
import cmsc420.exceptions.DuplicateCityCoordinatesException;
import cmsc420.exceptions.DuplicateCityNameException;
import cmsc420.exceptions.MapIsEmptyException;
import cmsc420.exceptions.NameNotInDictionaryException;
import cmsc420.exceptions.NoCitiesExistInRangeException;
import cmsc420.exceptions.NoCitiesToListException;
import cmsc420.schema.City;
import cmsc420.schema.CityColor;
import cmsc420.schema.SortType;
import cmsc420.schema.adjacencylist.AdjacencyListStructure;
import cmsc420.schema.dictionary.DictionaryStructure;
import cmsc420.schema.spatial.PRQuadTree;
import cmsc420.schema.spatial.SpatialStructure;

public class CommandRunner {

	private DictionaryStructure dictionary;
	private SpatialStructure spatial;
	private AdjacencyListStructure adjacencyList;
	CanvasPlus canvas;

	CommandRunner(DictionaryStructure dict, SpatialStructure spatial, AdjacencyListStructure adj) {
		this.dictionary = dict;
		this.spatial = spatial;
		this.adjacencyList = adj;
		this.canvas = new CanvasPlus("MeeshQuest", (int) this.spatial.getSpatialWidth(),
				(int) this.spatial.getSpatialHeight());
		this.canvas.addRectangle(0, 0, (int) this.spatial.getSpatialWidth(), (int) this.spatial.getSpatialHeight(),
				Color.BLACK, false);
	}

	/**
	 * Creates a city (a vertex) with the specified name, coordinates, radius,
	 * and color (the last two attributes will be used in later project parts).
	 * A city can be successfully created if its name is unique (i.e., there
	 * isn’t already another city with the same name) and its coordinates are
	 * also unique (i.e., there isn’t already another city with the same (x, y)
	 * coordinate). Names are case-sensitive.
	 * 
	 * @param name
	 * @param x
	 * @param y
	 * @param radius
	 * @param color
	 * @throws DuplicateCityNameException
	 * @throws DuplicateCityCoordinatesException
	 */
	void createCity(String name, int x, int y, int radius, CityColor color) throws DuplicateCityNameException,
			DuplicateCityCoordinatesException {
		City city = new City(name, x, y, color, radius);
		if (this.dictionary.containsName(name)) {
			throw new DuplicateCityNameException();
		} else if (this.dictionary.containsCoordinates(city)) {
			throw new DuplicateCityCoordinatesException();
		} else {
			this.dictionary.add(city);
		}
	}

	City deleteCity(String name) throws CityDoesNotExistException {
		if (!this.dictionary.containsName(name)) {
			throw new CityDoesNotExistException();
		}
		City city = this.dictionary.getCity(name);
		this.dictionary.remove(city);
		if (this.spatial.contains(city)) {
			try {
				this.unmapCity(name);
			} catch (NameNotInDictionaryException | CityNotMappedException e) {
			}
			return city;
		} else {
			return null;
		}
	}

	void clearAll() {
		this.dictionary = (DictionaryStructure) this.dictionary.reset();
		this.spatial = (SpatialStructure) this.spatial.reset();
		if (this.adjacencyList != null) {
			this.adjacencyList = (AdjacencyListStructure) this.adjacencyList.reset();
		}
		this.canvas = new CanvasPlus("MeeshQuest", (int) this.spatial.getSpatialWidth(),
				(int) this.spatial.getSpatialHeight());
		this.canvas.addRectangle(0, 0, (int) this.spatial.getSpatialWidth(), (int) this.spatial.getSpatialHeight(),
				Color.BLACK, false);
	}

	List<City> listCities(SortType sortBy) throws NoCitiesToListException {
		// citylist = 1+ cities = sorted by sortBy
		// citylist -> city tags
		if (this.dictionary.size() == 0) {
			throw new NoCitiesToListException();
		}
		return this.dictionary.listCities(sortBy);
	}

	void mapCity(String name) throws NameNotInDictionaryException, CityAlreadyMappedException, CityOutOfBoundsException {
		if (!this.dictionary.containsName(name)) {
			throw new NameNotInDictionaryException();
		}
		City city = this.dictionary.getCity(name);
		if (this.spatial.contains(city)) {
			throw new CityAlreadyMappedException();
		}
		if (city.x > this.spatial.getSpatialWidth() || city.y > this.spatial.getSpatialHeight()) {
			throw new CityOutOfBoundsException();
		}
		this.spatial.add(city);
		// TODO canvas plus + need to draw graynode quadrants
	}

	void unmapCity(String name) throws NameNotInDictionaryException, CityNotMappedException {
		if (!this.dictionary.containsName(name)) {
			throw new NameNotInDictionaryException();
		}
		City city = this.dictionary.getCity(name);
		if (!this.spatial.contains(city)) {
			// param is name of city
			throw new CityNotMappedException();
		} else {
			// remove city
			this.spatial.remove(city);
			// canvas plus
			// TODO
		}
	}

	PRQuadTree printPRQuadTree() throws MapIsEmptyException {
		if (this.spatial.size() == 0) {
			throw new MapIsEmptyException();
		} else {
			return (PRQuadTree) this.spatial;
		}
	}

	void saveMap(String name) {
		try {
			this.canvas.save(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	List<City> rangeCities(int x, int y, int radius, String name) throws NoCitiesExistInRangeException {
		// one citylist = multiple cities = asciibetical
		// citylist -> city tags
		// TODO implement
		List<City> cities = new ArrayList<>();
		// if list size == 0 throw exception
		if (cities.size() == 0) {
			throw new NoCitiesExistInRangeException();
		} else {
			return cities;
		}
	}

	City nearestCity(int x, int y) throws MapIsEmptyException {
		// single city
		// city tag
		// ASSUMING CITY MUST BE IN THE SPATIAL
		if (this.spatial.size() == 0) {
			throw new MapIsEmptyException();
		}
		// TODO
		return null;
	}

	void close() {
		this.canvas.dispose();
	}
}
