package tp.locomovil.model;

public class Project {

	private final String name;
	private final long id;

	public Project (String name, long id) {
		this.name = name;
		this.id = id;
	}

	public String getName () {
		return name;
	}

	public long getId () {
		return id;
	}
}
