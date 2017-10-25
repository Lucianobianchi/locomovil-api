package tp.locomovil.webapp.forms;

import org.hibernate.validator.constraints.NotBlank;
import tp.locomovil.model.Scan;
import tp.locomovil.model.WifiData;

import java.util.LinkedList;
import java.util.List;

public class FormScan {

	private Double userCoordX, userCoordY;

	private float[] rotationMatrix;

	private Double geomagneticX, geomagneticY, geomagneticZ, geomagneticResolution,
		accelerationX, accelerationY, accelerationZ, accelerationResolution,
		latitude, longitude, altitude, locationResolution;

	private Long deviceMillis, NTPMillis;

	@NotBlank
	private String MACAddress;

	private String deviceName;

	@NotBlank
	private Integer mapId;

	@NotBlank
	private Integer projectId;

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

	public Integer getProjectId () {
		return projectId;
	}

	public void setProjectId (Integer projectId) {
		this.projectId = projectId;
	}

	public String getMACAddress () {
		return MACAddress;
	}

	public void setMACAddress (String MACAddress) {
		this.MACAddress = MACAddress;
	}

	public String getDeviceName () {
		return deviceName;
	}

	public void setDeviceName (String deviceName) {
		this.deviceName = deviceName;
	}

	public Scan toScan () {
		return buildScan(this);
	}

	private static Scan buildScan(FormScan f) {
		final Scan.ScanDataBuilder b = new Scan.ScanDataBuilder();
		final List<WifiData> wifis = new LinkedList<>();

		if (f.getWifis() != null) {
			for (FormWifi w: f.getWifis()) {
				WifiData.WifiDataBuilder wb = new WifiData.WifiDataBuilder();
				wb.frequency(w.getFrequency());
				wb.level(w.getLevel());
				wb.bssid(w.getBSSID());
				wifis.add(wb.build());
			}
		}

		b.userCoordinates(f.getUserCoordX(), f.getUserCoordY());
		b.rotationMatrix(f.getRotationMatrix());
		b.mapId(f.getMapId());
		b.projectId(f.getProjectId());
		b.location(f.getLatitude(), f.getLongitude(), f.getAltitude());
		b.locationResolution(f.getLocationResolution());
		b.NTPMillis(f.getNTPMillis());
		b.deviceMillis(f.getDeviceMillis());
		b.geomagneticFieldResolution(f.getGeomagneticResolution());
		b.geomagneticField(f.getGeomagneticX(), f.getGeomagneticY(), f.getGeomagneticZ());
		b.accelerationResolution(f.getAccelerationResolution());
		b.acceleration(f.getAccelerationX(), f.getAccelerationY(), f.getAccelerationZ());
		b.wifis(wifis);
		b.MACAddress(f.getMACAddress());
		b.deviceName(f.getDeviceName());

		return b.build();
	}
}
