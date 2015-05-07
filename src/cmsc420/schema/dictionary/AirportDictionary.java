package cmsc420.schema.dictionary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import cmsc420.schema.Airport;

public class AirportDictionary implements Iterable<Airport> {

	// this can use hashes since we don't care about order
	// name -> airport
	// coords -> airport
	// airline -> set<airport>
	
	private HashMap<String, Airport> nameToAirport;
	private HashMap<Airport, String> airportToName;
	private HashMap<String, HashSet<Airport>> airlineToAirports;
	private int size;
	
	public AirportDictionary() {
		this.nameToAirport = new HashMap<>();
		this.airportToName = new HashMap<>();
		this.airlineToAirports = new HashMap<>();
		this.size = 0;
	}
	
	public void add(Airport airport) {
		this.nameToAirport.put(airport.getName(), airport);
		this.airportToName.put(airport, airport.getName());
		if (!this.airlineToAirports.containsKey(airport.getAirlineName())) {
			this.airlineToAirports.put(airport.getAirlineName(), new HashSet<>());
		}
		this.airlineToAirports.get(airport.getAirlineName()).add(airport);
		this.size++;
	}
	
	public void remove(Airport airport) {
		this.nameToAirport.remove(airport.getName());
		this.airportToName.remove(airport);
		this.airlineToAirports.get(airport.getAirlineName()).remove(airport);
		this.size--;
	}
	
	public Airport get(String name) {
		return this.nameToAirport.get(name);
	}
	
	public HashSet<Airport> getAirline(String airlineName) {
		return this.airlineToAirports.get(airlineName);
	}
	
	public boolean containsName(String name) {
		return this.nameToAirport.containsKey(name);
	}
	
	public boolean containsCoordinates(Airport airport) {
		return this.airportToName.containsKey(airport);
	}
	
	public int size() {
		return this.size;
	}

	@Override
	public Iterator<Airport> iterator() {
		return this.airportToName.keySet().iterator();
	}
	
}
