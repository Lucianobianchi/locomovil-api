package tp.locomovil.model;

public class Project {

	private String name;
	private long id;

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
