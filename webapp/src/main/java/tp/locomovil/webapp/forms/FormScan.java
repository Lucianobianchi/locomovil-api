package tp.locomovil.webapp.forms;

import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

public class FormScan {

	@NotBlank
	private Double userCoordX, userCoordY;

	private float[] rotationMatrix;

	private Double geomagneticX, geomagneticY, geomagneticZ, geomagneticResolution,
		accelerationX, accelerationY, accelerationZ, accelerationResolution,
		latitude, longitude, altitude, locationResolution;

	private Long deviceMillis, NTPMillis;

	@NotBlank
	private Integer mapId;

	private List<FormWifi> wifis;

	public FormScan () {
	}

	public Integer getMapId() {
		return mapId;
	}

	public Long getDeviceMillis() {
		return deviceMillis;
	}

	public Long getNTPMillis() {
		return NTPMillis;
	}

	public Double getUserCoordX() {
		return userCoordX;
	}

	public Double getUserCoordY() {
		return userCoordY;
	}

	public float[] getRotationMatrix() {
		return rotationMatrix;
	}

	public Double getGeomagneticX() {
		return geomagneticX;
	}

	public Double getGeomagneticY() {
		return geomagneticY;
	}

	public Double getGeomagneticZ() {
		return geomagneticZ;
	}

	public Double getGeomagneticResolution() {
		return geomagneticResolution;
	}

	public Double getAccelerationX() {
		return accelerationX;
	}

	public Double getAccelerationY() {
		return accelerationY;
	}

	public Double getAccelerationZ() {
		return accelerationZ;
	}

	public Double getAccelerationResolution() {
		return accelerationResolution;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public Double getAltitude() {
		return altitude;
	}

	public Double getLocationResolution() {
		return locationResolution;
	}

	public List<FormWifi> getWifis () {
		return wifis;
	}

	public void setUserCoordX (Double userCoordX) {
		this.userCoordX = userCoordX;
	}

	public void setUserCoordY (Double userCoordY) {
		this.userCoordY = userCoordY;
	}

	public void setRotationMatrix (float[] rotationMatrix) {
		this.rotationMatrix = rotationMatrix;
	}

	public void setGeomagneticX (Double geomagneticX) {
		this.geomagneticX = geomagneticX;
	}

	public void setGeomagneticY (Double geomagneticY) {
		this.geomagneticY = geomagneticY;
	}

	public void setGeomagneticZ (Double geomagneticZ) {
		this.geomagneticZ = geomagneticZ;
	}

	public void setGeomagneticResolution (Double geomagneticResolution) {
		this.geomagneticResolution = geomagneticResolution;
	}

	public void setAccelerationX (Double accelerationX) {
		this.accelerationX = accelerationX;
	}

	public void setAccelerationY (Double accelerationY) {
		this.accelerationY = accelerationY;
	}

	public void setAccelerationZ (Double accelerationZ) {
		this.accelerationZ = accelerationZ;
	}

	public void setAccelerationResolution (Double accelerationResolution) {
		this.accelerationResolution = accelerationResolution;
	}

	public void setLatitude (Double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude (Double longitude) {
		this.longitude = longitude;
	}

	public void setAltitude (Double altitude) {
		this.altitude = altitude;
	}

	public void setLocationResolution (Double locationResolution) {
		this.locationResolution = locationResolution;
	}

	public void setDeviceMillis (Long deviceMillis) {
		this.deviceMillis = deviceMillis;
	}

	public void setNTPMillis (Long NTPMillis) {
		this.NTPMillis = NTPMillis;
	}

	public void setMapId (Integer mapId) {
		this.mapId = mapId;
	}

	public void setWifis (List<FormWifi> wifis) {
		this.wifis = wifis;
	}
}
