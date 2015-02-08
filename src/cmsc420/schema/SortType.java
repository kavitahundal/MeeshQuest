package cmsc420.schema;

public enum SortType {

	name,
	coordinate;
	
	public static SortType getSortType(String sortType) {
		if (sortType.equals("name")) {
			return name;
		} else if (sortType.equals("coordinate")) {
			return coordinate;
		} else {
			return null;
		}
	}
}
