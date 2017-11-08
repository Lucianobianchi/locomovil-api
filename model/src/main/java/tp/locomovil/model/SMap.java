package tp.locomovil.model;


public class SMap {
	final String mapName;

	final long mapId;

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
