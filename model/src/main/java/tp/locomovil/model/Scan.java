package tp.locomovil.model;

import java.util.List;

public class Scan {

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

	private Scan (ScanDataBuilder builder) {
		// Geomagnetic Field
		this.geomagneticX = builder.geomagneticX;
		this.geomagneticY = builder.geomagneticY;
		this.geomagneticZ = builder.geomagneticZ;
		this.geomagneticResolution = builder.geomagneticResolution;

		// Accelerometer
		this.accelerationX = builder.accelerationX;
		this.accelerationY = builder.accelerationY;
		this.accelerationZ = builder.accelerationZ;
		this.accelerationResolution = builder.accelerationResolution;

		// Location
		this.latitude = builder.latitude;
		this.longitude = builder.longitude;
		this.altitude = builder.altitude;
		this.locationResolution = builder.locationResolution;

		this.userCoordX = builder.userCoordX;
		this.userCoordY = builder.userCoordY;

		this.rotationMatrix = builder.rotationMatrix;

		this.deviceMillis = builder.deviceMillis;
		this.NTPMillis = builder.NTPMillis;

		this.mapId = builder.mapId;
		this.projectId = builder.projectId;
		this.wifis = builder.wifis;
		this.wifiScanId = builder.wifiScanId;

		this.MACAddress = builder.MACAddress;
		this.deviceName = builder.deviceName;
	}

	public Double getUserCoordX () {
		return userCoordX;
	}

	public Double getUserCoordY () {
		return userCoordY;
	}

	public float[] getRotationMatrix () {
		return rotationMatrix;
	}

	public Double getGeomagneticX () {
		return geomagneticX;
	}

	public Double getGeomagneticY () {
		return geomagneticY;
	}

	public Double getGeomagneticZ () {
		return geomagneticZ;
	}

	public Double getGeomagneticResolution () {
		return geomagneticResolution;
	}

	public Double getAccelerationX () {
		return accelerationX;
	}

	public Double getAccelerationY () {
		return accelerationY;
	}

	public Double getAccelerationZ () {
		return accelerationZ;
	}

	public Double getAccelerationResolution () {
		return accelerationResolution;
	}

	public Double getLatitude () {
		return latitude;
	}

	public Double getLongitude () {
		return longitude;
	}

	public Double getAltitude () {
		return altitude;
	}

	public Double getLocationResolution () {
		return locationResolution;
	}

	public Long getDeviceMillis () {
		return deviceMillis;
	}

	public Long getNTPMillis () {
		return NTPMillis;
	}

	public Integer getMapId () {
		return mapId;
	}

	public Integer getProjectId () {
		return projectId;
	}

	public List<WifiData> getWifis () {
		return wifis;
	}

	public Long getWifiScanId () {
		return wifiScanId;
	}

	public String getMACAddress () {
		return MACAddress;
	}

	public String getDeviceName () {
		return deviceName;
	}

	@Override
	public boolean equals (Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Scan scan = (Scan) o;

		if (mapId != null ? !mapId.equals(scan.mapId) : scan.mapId != null)
			return false;
		return !(wifiScanId != null ?
				!wifiScanId.equals(scan.wifiScanId) :
				scan.wifiScanId != null);

	}

	@Override
	public int hashCode () {
		int result = mapId != null ? mapId.hashCode() : 0;
		result = 31 * result + (wifiScanId != null ? wifiScanId.hashCode() : 0);
		return result;
	}


	public static class ScanDataBuilder {

		private Double userCoordX, userCoordY;

		private float[] rotationMatrix;

		private Double geomagneticX, geomagneticY, geomagneticZ, geomagneticResolution,
			accelerationX, accelerationY, accelerationZ, accelerationResolution,
			latitude, longitude, altitude, locationResolution;

		private Long deviceMillis, NTPMillis;

		private Integer mapId;

		private Integer projectId;

		List<WifiData> wifis;

		private Long wifiScanId;

		private String MACAddress;

		private String deviceName;

		public ScanDataBuilder geomagneticField(Double X, Double Y, Double Z) {
			geomagneticX = X;
			geomagneticY = Y;
			geomagneticZ = Z;
			return this;
		}

		public ScanDataBuilder geomagneticFieldResolution(Double resolution) {
			geomagneticResolution = resolution;
			return this;
		}

		public ScanDataBuilder acceleration(Double X, Double Y, Double Z) {
			accelerationX = X;
			accelerationY = Y;
			accelerationZ = Z;
			return this;
		}

		public ScanDataBuilder accelerationResolution(Double resolution) {
			accelerationResolution = resolution;
			return this;
		}

		public ScanDataBuilder location(Double latitude, Double longitude, Double altitude) {
			this.latitude = latitude;
			this.longitude = longitude;
			this.altitude = altitude;
			return this;
		}

		public ScanDataBuilder locationResolution(Double resolution) {
			locationResolution = resolution;
			return this;
		}

		public ScanDataBuilder userCoordinates(Double X, Double Y) {
			userCoordX = X;
			userCoordY = Y;
			return this;
		}

		public ScanDataBuilder rotationMatrix(float[] matrix) {
			if (matrix != null) {
				rotationMatrix = matrix.clone();
			}
			return this;
		}

		public ScanDataBuilder deviceMillis(Long millis) {
			deviceMillis = millis;
			return this;
		}

		public ScanDataBuilder NTPMillis(Long millis) {
			NTPMillis = millis;
			return this;
		}

		public ScanDataBuilder mapId(Integer id) {
			this.mapId = id;
			return this;
		}

		public ScanDataBuilder projectId(Integer id) {
			this.projectId = id;
			return this;
		}

		public ScanDataBuilder wifis(List<WifiData> w) {
			this.wifis = w;
			return this;
		}

		public ScanDataBuilder wifiScanId(Long id) {
			this.wifiScanId = id;
			return this;
		}

		public ScanDataBuilder MACAddress(String mac) {
			this.MACAddress = mac;
			return this;
		}

		public ScanDataBuilder deviceName(String name) {
			this.deviceName = name;
			return this;
		}

		public Scan build() {
			return new Scan(this);
		}
	}
}
