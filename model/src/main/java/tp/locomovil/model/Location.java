package tp.locomovil.model;

import javax.xml.bind.annotation.XmlRootElement;

public class Location {
	private double XCoordinate;

	private double YCoordinate;

	public Location (double XCoordinate, double YCoordinate) {
		this.XCoordinate = XCoordinate;
		this.YCoordinate = YCoordinate;
	}

	public double XCoordinate () {
		return XCoordinate;
	}

	public double YCoordinate () {
		return YCoordinate;
	}
}
