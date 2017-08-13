package tp.locomovil.webapp.forms;

import org.hibernate.validator.constraints.NotBlank;

public class FormMap {

	@NotBlank
	String mapName;

	public FormMap () {
	}

	public String getMapName () {
		return mapName;
	}

	public void setMapName (String mapName) {
		this.mapName = mapName;
	}
}
