package tp.locomovil.webapp.dto;

import tp.locomovil.model.Scan;
import tp.locomovil.model.WifiData;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class ScanDTO {
	private Double userCoordX, userCoordY;

	private float[] rotationMatrix;

	private Double geomagneticX, geomagneticY, geomagneticZ, geomagneticResolution,
		accelerationX, accelerationY, accelerationZ, accelerationResolution,
		latitude, longitude, altitude, locationResolution;

	private Long deviceMillis, NTPMillis;

	private Integer mapId;

	private Integer projectId;

	private List<WifiData> wifis;

	private Long wifiScanId;

	private String MACAddress;

	private String deviceName;

	public ScanDTO(){
	}

	public ScanDTO(Scan s) {
		// Geomagnetic Field
		this.geomagneticX = s.getGeomagneticX();
		this.geomagneticY = s.getGeomagneticY();
		this.geomagneticZ = s.getGeomagneticZ();
		this.geomagneticResolution = s.getGeomagneticResolution();

		// Accelerometer
		this.accelerationX = s.getAccelerationX();
		this.accelerationY = s.getAccelerationY();
		this.accelerationZ = s.getAccelerationZ();
		this.accelerationResolution = s.getAccelerationResolution();

		// Location
		this.latitude = s.getLatitude();
		this.longitude = s.getLongitude();
		this.altitude = s.getAltitude();
		this.locationResolution = s.getLocationResolution();

		this.userCoordX = s.getUserCoordX();
		this.userCoordY = s.getUserCoordY();

		this.rotationMatrix = s.getRotationMatrix();

		this.deviceMillis = s.getDeviceMillis();
		this.NTPMillis = s.getNTPMillis();

		this.mapId = s.getMapId();
		this.projectId = s.getProjectId();
		this.wifis = s.getWifis();
		this.wifiScanId = s.getWifiScanId();

		this.MACAddress = s.getMACAddress();
		this.deviceName = s.getDeviceName();
	}

	public Double getUserCoordX () {
		return userCoordX;
	}

	public void setUserCoordX (Double userCoordX) {
		this.userCoordX = userCoordX;
	}

	public Double getUserCoordY () {
		return userCoordY;
	}

	public void setUserCoordY (Double userCoordY) {
		this.userCoordY = userCoordY;
	}

	public float[] getRotationMatrix () {
		return rotationMatrix;
	}

	public void setRotationMatrix (float[] rotationMatrix) {
		this.rotationMatrix = rotationMatrix;
	}

	public Double getGeomagneticX () {
		return geomagneticX;
	}

	public void setGeomagneticX (Double geomagneticX) {
		this.geomagneticX = geomagneticX;
	}

	public Double getGeomagneticY () {
		return geomagneticY;
	}

	public void setGeomagneticY (Double geomagneticY) {
		this.geomagneticY = geomagneticY;
	}

	public Double getGeomagneticZ () {
		return geomagneticZ;
	}

	public void setGeomagneticZ (Double geomagneticZ) {
		this.geomagneticZ = geomagneticZ;
	}

	public Double getGeomagneticResolution () {
		return geomagneticResolution;
	}

	public void setGeomagneticResolution (Double geomagneticResolution) {
		this.geomagneticResolution = geomagneticResolution;
	}

	public Double getAccelerationX () {
		return accelerationX;
	}

	public void setAccelerationX (Double accelerationX) {
		this.accelerationX = accelerationX;
	}

	public Double getAccelerationY () {
		return accelerationY;
	}

	public void setAccelerationY (Double accelerationY) {
		this.accelerationY = accelerationY;
	}

	public Double getAccelerationZ () {
		return accelerationZ;
	}

	public void setAccelerationZ (Double accelerationZ) {
		this.accelerationZ = accelerationZ;
	}

	public Double getAccelerationResolution () {
		return accelerationResolution;
	}

	public void setAccelerationResolution (Double accelerationResolution) {
		this.accelerationResolution = accelerationResolution;
	}

	public Double getLatitude () {
		return latitude;
	}

	public void setLatitude (Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude () {
		return longitude;
	}

	public void setLongitude (Double longitude) {
		this.longitude = longitude;
	}

	public Double getAltitude () {
		return altitude;
	}

	public void setAltitude (Double altitude) {
		this.altitude = altitude;
	}

	public Double getLocationResolution () {
		return locationResolution;
	}

	public void setLocationResolution (Double locationResolution) {
		this.locationResolution = locationResolution;
	}

	public Long getDeviceMillis () {
		return deviceMillis;
	}

	public void setDeviceMillis (Long deviceMillis) {
		this.deviceMillis = deviceMillis;
	}

	public Long getNTPMillis () {
		return NTPMillis;
	}

	public void setNTPMillis (Long NTPMillis) {
		this.NTPMillis = NTPMillis;
	}

	public Integer getMapId () {
		return mapId;
	}

	public void setMapId (Integer mapId) {
		this.mapId = mapId;
	}

	public Integer getProjectId () {
		return projectId;
	}

	public void setProjectId (Integer projectId) {
		this.projectId = projectId;
	}

	public List<WifiData> getWifis () {
		return wifis;
	}

	public void setWifis (List<WifiData> wifis) {
		this.wifis = wifis;
	}

	public Long getWifiScanId () {
		return wifiScanId;
	}

	public void setWifiScanId (Long wifiScanId) {
		this.wifiScanId = wifiScanId;
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
}
