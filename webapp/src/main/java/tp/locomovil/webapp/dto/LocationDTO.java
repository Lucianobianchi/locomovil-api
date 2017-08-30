package tp.locomovil.webapp.dto;

import tp.locomovil.model.Location;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LocationDTO {

	private double coordX;
	private double coordY;

	private double precision;

	private String projectName;
	private String mapName;

	public LocationDTO () {
	}

	public LocationDTO (Location l) {
		coordX = l.XCoordinate();
		coordY = l.YCoordinate();
		this.projectName = l.getProjectName();
		this.mapName = l.getMapName();
		this.precision = l.getPrecision();
	}

	public double getCoordX () {
		return coordX;
	}

	public double getCoordY () {
		return coordY;
	}

	public void setCoordX (double coordX) {
		this.coordX = coordX;
	}

	public void setCoordY (double coordY) {
		this.coordY = coordY;
	}

	public String getProjectName () {
		return projectName;
	}

	public void setProjectName (String projectName) {
		this.projectName = projectName;
	}

	public String getMapName () {
		return mapName;
	}

	public void setMapName (String mapName) {
		this.mapName = mapName;
	}

	public double getPrecision () {
		return precision;
	}

	public void setPrecision (double precision) {
		this.precision = precision;
	}
}
