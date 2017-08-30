package tp.locomovil.model;

import javax.xml.bind.annotation.XmlRootElement;

public class Location {
	private double XCoordinate;

	private double YCoordinate;

	private double precision;

	private String projectName;
	private String mapName;

	public Location (String projectName,
			String mapName, double XCoordinate, double YCoordinate, double precision) {
		this.XCoordinate = XCoordinate;
		this.YCoordinate = YCoordinate;
		this.precision = precision;
		this.projectName = projectName;
		this.mapName = mapName;
	}

	public double XCoordinate () {
		return XCoordinate;
	}

	public double YCoordinate () {
		return YCoordinate;
	}

	public double getPrecision () {
		return precision;
	}

	public String getProjectName () {
		return projectName;
	}

	public String getMapName () {
		return mapName;
	}
}
