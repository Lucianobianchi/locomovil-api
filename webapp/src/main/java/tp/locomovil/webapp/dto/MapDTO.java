package tp.locomovil.webapp.dto;

import tp.locomovil.model.SMap;

import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@XmlRootElement
public class MapDTO {

	private String name;

	private long mapId;

	private URI url;

	public MapDTO () {
	}

	public MapDTO (String name, long projectId, long mapId, URI baseUri) {
		this.name = name;
		this.mapId = mapId;
		url = baseUri.resolve("projects/" + projectId + "/maps/" + mapId);
	}

	public MapDTO(SMap map, long projectId, URI baseUri) {
		this.name = map.getMapName();
		this.mapId = map.getMapId();
		url = baseUri.resolve("projects/" + projectId + "/maps/" + mapId);
	}

	public String getName () {
		return name;
	}

	public long getMapId () {
		return mapId;
	}

	public void setName (String name) {
		this.name = name;
	}

	public void setMapId (long mapId) {
		this.mapId = mapId;
	}
}
