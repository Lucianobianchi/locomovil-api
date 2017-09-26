package tp.locomovil.model;


public class SMap {
	String mapName;

	long mapId;

	public SMap (String mapName, long mapId) {
		this.mapName = mapName;
		this.mapId = mapId;
	}

	public String getMapName () {
		return mapName;
	}

	public long getMapId () {
		return mapId;
	}
}
