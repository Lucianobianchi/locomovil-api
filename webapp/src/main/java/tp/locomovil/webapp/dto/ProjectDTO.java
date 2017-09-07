package tp.locomovil.webapp.dto;

import tp.locomovil.model.Project;
import tp.locomovil.model.SMap;

import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@XmlRootElement
public class ProjectDTO {

	private String name;

	private long id;

	private List<MapDTO> maps;

	private URI uri;

	public ProjectDTO() {
	}

	public ProjectDTO(Project p, List<SMap> maps, URI baseUri) {
		this.name = p.getName();
		this.id = p.getId();
		this.uri = baseUri.resolve("projects/" + this.id);

		this.maps = new LinkedList<MapDTO>();
		for (SMap m : maps) {
			this.maps.add(new MapDTO(m, this.id, this.uri));
		}
	}

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public List<MapDTO> getMaps () {
		return maps;
	}

	public void setMaps (List<MapDTO> maps) {
		this.maps = maps;
	}

	public long getId () {
		return id;
	}

	public void setId (long id) {
		this.id = id;
	}

	public URI getUri () {
		return uri;
	}

	public void setUri (URI uri) {
		this.uri = uri;
	}
}
