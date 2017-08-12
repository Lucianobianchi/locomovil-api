package tp.locomovil.webapp.dto;

import tp.locomovil.model.Location;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LocationDTO {

	private double coordX;
	private double coordY;

	public LocationDTO () {
	}

	public LocationDTO (Location l) {
		coordX = l.XCoordinate();
		coordY = l.YCoordinate();
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
}
