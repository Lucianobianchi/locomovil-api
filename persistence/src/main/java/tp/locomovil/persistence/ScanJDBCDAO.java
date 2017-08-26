package tp.locomovil.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import tp.locomovil.inter.ScanDAO;
import tp.locomovil.model.Location;
import tp.locomovil.model.Scan;
import tp.locomovil.model.WifiData;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ScanJDBCDAO implements ScanDAO {

	private JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;

	private final static RowMapper<Scan> ROW_MAPPER = new RowMapper<Scan>() {
		public Scan mapRow (ResultSet rs, int rowNum) throws SQLException {
			Scan.ScanDataBuilder builder = new Scan.ScanDataBuilder();
//			builder.rotationMatrix() TODO
			builder.NTPMillis(rs.getLong("ntp_time"));
			builder.deviceMillis(rs.getLong("device_time"));

			builder.acceleration(rs.getDouble("accel_x"), rs.getDouble("accel_y"), rs.getDouble("accel_z"));
			builder.accelerationResolution(rs.getDouble("accel_res"));
			builder.location(rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getDouble("altitude"));
			builder.locationResolution(rs.getDouble("location_res"));
			builder.geomagneticField(rs.getDouble("geomag_x"), rs.getDouble("geomag_y"), rs.getDouble("geomag_z"));
			builder.geomagneticFieldResolution(rs.getDouble("geomag_res"));
			builder.userCoordinates(rs.getDouble("coord_x"), rs.getDouble("coord_y"));
			builder.mapId(rs.getInt("map_id"));
			builder.wifiScanId(rs.getLong("wifi_scan_id"));

			return builder.build();
		}
	};

	@Autowired
	public ScanJDBCDAO (DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
						.withTableName("scans")
						.usingGeneratedKeyColumns("wifi_scan_id");
	}

	public int saveScan(Scan scan) {
		final Map<String, Object> args = new HashMap<String, Object>();
		args.put("latitude", scan.getLatitude());
		args.put("longitude", scan.getLongitude());
		args.put("altitude", scan.getAltitude());
		args.put("location_res", scan.getLocationResolution());
		args.put("geomag_x", scan.getGeomagneticX());
		args.put("geomag_y", scan.getGeomagneticY());
		args.put("geomag_z", scan.getGeomagneticZ());
		args.put("geomag_res", scan.getGeomagneticResolution());
		args.put("accel_x", scan.getAccelerationX());
		args.put("accel_y", scan.getAccelerationY());
		args.put("accel_z", scan.getAccelerationZ());
		args.put("accel_res", scan.getAccelerationResolution());
		args.put("coord_x", scan.getUserCoordX());
		args.put("coord_y", scan.getUserCoordY());
		args.put("device_time", scan.getDeviceMillis());
		args.put("ntp_time", scan.getNTPMillis());
		args.put("map_id", scan.getMapId());
		args.put("project_id", scan.getProjectId());
		args.put("rotation_matrix", scan.getRotationMatrix());

		final Number wifiScanId = jdbcInsert.executeAndReturnKey(args);

		return wifiScanId.intValue();
	}

	public List<Scan> getAllScansByMapId(long mapId) {
		return jdbcTemplate.query("SELECT * FROM scans WHERE map_id = ?;", ROW_MAPPER, mapId);
	}

	public List<Scan> getAllScansByProjectId(long projectId) {
		return jdbcTemplate.query("SELECT * FROM scans WHERE project_id = ?;", ROW_MAPPER, projectId);
	}

	public List<Scan> getScansByLocation(long mapId, Location location) { // La precisión la "calcula" el service
		final long precision = 10; // TODO: calculo inteligente de la precisión. (?)
		return jdbcTemplate.query("SELECT * FROM scans "
				+ "WHERE map_id = ? AND abs(coord_x - ?) < ? AND abs(coord_y - ?) < ?;",
				ROW_MAPPER, mapId, location.XCoordinate(), precision, location.YCoordinate(), precision);
	}
}
