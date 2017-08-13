package tp.locomovil.webapp.dto;

import tp.locomovil.model.SMap;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MapDTO {

	private String name;

	private long mapId;

	public MapDTO () {
	}

	public MapDTO (String name, long mapId) {
		this.name = name;
		this.mapId = mapId;
	}

	public MapDTO(SMap map) {
		this.name = map.getMapName();
		this.mapId = map.getMapId();
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
