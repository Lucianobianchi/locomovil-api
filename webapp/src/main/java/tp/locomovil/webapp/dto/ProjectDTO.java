package tp.locomovil.webapp.dto;

import tp.locomovil.model.Project;
import tp.locomovil.model.SMap;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@XmlRootElement
public class ProjectDTO {

	private String name;

	private long id;

	private List<MapDTO> maps;

	public ProjectDTO() {
	}

	public ProjectDTO(Project p, List<SMap> maps) {
		this.name = p.getName();
		this.id = p.getId();

		this.maps = new LinkedList<MapDTO>();
		for (SMap m : maps) {
			this.maps.add(new MapDTO(m));
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
}
